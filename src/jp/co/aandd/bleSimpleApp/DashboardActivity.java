package jp.co.aandd.bleSimpleApp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import jp.co.aandd.bleSimpleApp.base.ADGattService;
import jp.co.aandd.bleSimpleApp.entities.AndMedical_App_Global;
import jp.co.aandd.bleSimpleApp.entities.DataBase;
import jp.co.aandd.bleSimpleApp.entities.Lifetrack_infobean;
import jp.co.aandd.bleSimpleApp.gatt.ADGattUUID;
import jp.co.aandd.bleSimpleApp.gatt.BleReceivedService;
import jp.co.aandd.bleSimpleApp.utilities.ADSharedPreferences;
import jp.co.aandd.bleSimpleApp.utilities.ANDMedicalUtilities;
import jp.co.aandd.bleSimpleApp.utilities.AndMedicalLogic;
import jp.co.aandd.bleSimpleApp.view.BloodPressureDispalyDataLayout;
import jp.co.aandd.bleSimpleApp.view.WeightScaleDisplayDataLayout;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.slidemenu.SlideMenu;

public class DashboardActivity extends ADBaseActivity implements
		OnRefreshListener, BluetoothAdapter.LeScanCallback{

	private static final String TAG = "SN";
	private static final int REQUEST_ENABLE_BLUETOOTH = 1000;

	private boolean isScanning = false;
	private boolean shouldStartConnectDevice = false;

	private BluetoothManager bluetoothManager;
	private BluetoothAdapter bluetoothAdapter;
	
	private BloodPressureDispalyDataLayout bloodpressure;
	private WeightScaleDisplayDataLayout weightscale;
	private FrameLayout rightArrow;
	private FrameLayout leftArrow;
	private DataBase db;
	private SwipeRefreshLayout swipeLayout;
	private ArrayList<Lifetrack_infobean> insertObjectList = new ArrayList<Lifetrack_infobean>();
	private SlideMenu mSlideMenu; 
	
	private boolean mIsSending = false;
	private boolean mIsSendCancel = false;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"Dashboard onCreate " + this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.and_dashboard_new);
		
		registerReceiver(mMeasudataUpdateReceiver, MeasuDataManager.MeasuDataUpdateIntentFilter());

		initializeUI();
		
		initializeBluetooth();
		
		String login_username = ADSharedPreferences.getString(ADSharedPreferences.KEY_LOGIN_USER_NAME, "");
		
		db = new DataBase(this, login_username);

		mSlideMenu = (SlideMenu) findViewById(R.id.slideMenu);
		String addnewUserVisiblity = ADSharedPreferences.getString(ADSharedPreferences.KEY_ADD_NEW_USER_VISIBLITY, "");
		String manageuservisibility = ADSharedPreferences.getString(ADSharedPreferences.KEY_MANAGER_USER_VISIBILITY,
				"");
		String frommanagevisibility = ADSharedPreferences.getString(ADSharedPreferences.KEY_FROM_MANAGER_VISIBILITY,
				"");

		RelativeLayout mainlayout = (RelativeLayout) findViewById(R.id.root);
		mSlideMenu.init(this, this, 333, login_username, login_username,
				addnewUserVisiblity, manageuservisibility,
				frommanagevisibility,
				mainlayout);

		mSlideMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSlideMenu.show();
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"Dashboard onDestroy " + this);

		unregisterReceiver(mMeasudataUpdateReceiver);
		
		unregisterReceiver(bleServiceReceiver);
		Intent intent = new Intent(this, BleReceivedService.class);
		unbindService(serviceConnection);
		stopService(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"Dashboard onResume " + this);
		if (BleReceivedService.getInstance() != null) {
			if (!isScanning) {
				startScan();
			}
		}
				
		// Data Sync
		// 初回のみデータ取得・更新
		MeasuDataManager measuDataManager = ((AndMedical_App_Global)getApplication()).getMeasuDataManager();
		if(measuDataManager == null) {
			measuDataManager = new MeasuDataManager(this);
			((AndMedical_App_Global)getApplication()).setMeasuDataManager(measuDataManager);
			measuDataManager.syncAllMeasuDatas(true);
		}
		else {
			refreshDisplay();
		}
		if (mIsSendCancel) {
			mIsSendCancel = false;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG,"Dashboard onPause " + this);
		if (BleReceivedService.getInstance() != null) {
			if (isScanning) {
				stopScan();
			}
		}

		if (!mIsSendCancel) {
			mIsSendCancel = true;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
			if (resultCode == RESULT_OK) {
				startBleService();
			}
			else {
				//Toast.makeText(this, "BluetoothAdapter cannot be enabled", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	@Override
	public void onRefresh() {
		
	}
	
	@Override
	public SlideMenu getSlideMenu() {
		return mSlideMenu;
	}
	
	private void initializeUI() {
		// Stand Alone 対応
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setEnabled(false);
		/* Function Disable
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		*/
			
		// Display Sub Dashboard
		bloodpressure = (BloodPressureDispalyDataLayout) findViewById(R.id.llinear_bp);
		bloodpressure.setHide(true);
		weightscale = (WeightScaleDisplayDataLayout) findViewById(R.id.weightclick);
		weightscale.setHide(true);
		
		// Arrows
		rightArrow = (FrameLayout)findViewById(R.id.right_arrow);
		rightArrow.setVisibility(View.INVISIBLE);
		rightArrow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AndMedical_App_Global appGlobal = (AndMedical_App_Global) getApplication();
				MeasuDataManager manager = appGlobal.getMeasuDataManager();
				if(manager != null) {
					manager.moveDatasToTheFuture();
					refreshDisplay();
				}
			}
		});
		
		leftArrow =  (FrameLayout)findViewById(R.id.left_arrow);
		leftArrow.setVisibility(View.INVISIBLE);
		leftArrow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AndMedical_App_Global appGlobal = (AndMedical_App_Global) getApplication();
				MeasuDataManager manager = appGlobal.getMeasuDataManager();
				if(manager != null) {
					manager.moveDatasToThePast();
					refreshDisplay();
				}
			}
		});

		

		TextView header = (TextView) findViewById(R.id.header);
		header.setText(R.string.header_dashboard);

			}
	
	private void initializeBluetooth() {
		
		bluetoothManager = (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
		if (bluetoothManager == null) {
			//Toast.makeText(this, "BluetoothManager does not exist", Toast.LENGTH_LONG).show();
		}
		else {
			bluetoothAdapter = bluetoothManager.getAdapter();
			if (bluetoothAdapter == null) {
				//Toast.makeText(this, "BluetoothAdapter does not exist", Toast.LENGTH_LONG).show();
			}
			else {
				if (!bluetoothAdapter.isEnabled()) {
					Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
				}
			}
		}

		IntentFilter filter = new IntentFilter(BleReceivedService.ACTION_BLE_SERVICE);
		registerReceiver(bleServiceReceiver, filter);
		
		if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
			// to do Nothing
		}
		else {
			startBleService();
		}
	}
	
	private Dialog progress;
	private void showIndicator(String message) {
		if(progress == null) {
			progress = new Dialog(DashboardActivity.this);
			// AlertDialog.Builder builder = new AlertDialog.Builder(context);
			progress.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
			progress.setContentView(R.layout.custom_alert);
			progress.setCancelable(false);
		}
		
		setIndicatorMessage(message);
		
		if(!progress.isShowing()) {
			progress.show();
		}
	}
	
	private void setIndicatorMessage(String message) {
		if(progress == null) {
			return ;
		}
		TextView syncMessages = (TextView) progress.findViewById(R.id.syncMessages1);
		
		if(message == null) {
			message = "";
		}
		
		if(syncMessages != null) {
			syncMessages.setText(message);
		}
	}
	
	private void dismissIndicator() {
		if(progress == null) {
			return ;
		}
		
		progress.dismiss();
		progress = null;
	}
	
	private void startBleService() {
		Intent intent = new Intent(this, BleReceivedService.class);
		startService(intent);
		boolean result = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
		if (result) {
			Log.d(TAG, "BleService - success");
		}
		else {
			Log.e(TAG, "BleService - failed");
			//Toast.makeText(this, "Failed to bind BleService", Toast.LENGTH_LONG).show();
		}
	}

	private final ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d(TAG, "BleService - connected");
			
			startScan();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d(TAG, "BleService - disconnected");
		}
	};

	private void startScan() {
		if (BleReceivedService.getInstance().isConnectedDevice()) {
			BleReceivedService.getInstance().disconnectDevice();
		}
		isScanning = true;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bluetoothAdapter.startLeScan(DashboardActivity.this);
			}
		});
	}

	private void stopScan() {
		isScanning = false;
		
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bluetoothAdapter.stopLeScan(DashboardActivity.this);
			}
		});
		
	}
	
	@Override
	public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
		//Log.d(TAG, "Found device - " + device.getName());
		Log.d(TAG, "DashBoard onLeScan");
		if (!isScanning) {
			Log.d(TAG, "DashBoard Scanning was stopped");
			return;
		}
		
		if(!isAbleToConnectDevice(device) || shouldStartConnectDevice) {
			//Log.d(TAG, "UnBonded Device");
			Log.d(TAG, "DashBoard UnBonded Device " + isAbleToConnectDevice(device) + " " + shouldStartConnectDevice);
			return ;
		}
		
		shouldStartConnectDevice = true;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				stopScan();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				BleReceivedService.getInstance().connectDevice(device);
			}
		});
	}
	
	private boolean isAbleToConnectDevice(BluetoothDevice device) {
		
		// すでに何かが接続中ならFalse
		if(BleReceivedService.getInstance().isConnectedDevice()) {
			Log.d(TAG, "Is Connected Device");
			return false;
		}
		
		// ペアリング済みのデバイスのみ許可
		if (bluetoothAdapter != null) {
			Set<BluetoothDevice> pairingDevices = bluetoothAdapter.getBondedDevices();
			return pairingDevices.contains(device);
		}
		return false;
	}
	
	private Handler uiThreadHandler = new Handler();
	private final BroadcastReceiver bleServiceReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String type = intent.getExtras().getString(BleReceivedService.EXTRA_TYPE);
			if (BleReceivedService.TYPE_GATT_CONNECTED.equals(type)) {
				Log.d(TAG, "DashBoard onReceive TYPE_GATT_CONNECTED");
				BleReceivedService.getGatt().discoverServices();
				//Toast.makeText(DashboardActivity.this, "Connect device", Toast.LENGTH_LONG).show();
				Log.d(TAG, "Connect Device");
			}
			else if (BleReceivedService.TYPE_GATT_DISCONNECTED.equals(type)) {
				Log.d(TAG, "Disconnect Device");
				Log.d(TAG, "DashBoard onReceive TYPE_GATT_DISCONNECTED");
				//Toast.makeText(DashboardActivity.this, "Disconnect device", Toast.LENGTH_LONG).show();
				BleReceivedService.getInstance().disconnectDevice();
				uiThreadHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						if (!isScanning) {
							startScan();
						}
					}
				}, 80L);
				
			}
			else if (BleReceivedService.TYPE_GATT_ERROR.equals(type)) {
				Toast.makeText(DashboardActivity.this, "Erro Gatt Status :"+intent.getExtras().getInt(BleReceivedService.EXTRA_STATUS), Toast.LENGTH_LONG).show();
				Log.d(TAG, "DashBoard onReceive TYPE_GATT_ERROR");
				
			}
			else {
				if (BleReceivedService.TYPE_GATT_SERVICES_DISCOVERED.equals(type)) {
					Log.d(TAG, "DashBoard onReceive TYPE_GATT_SERVICES_DISCOVERED");
					//Toast.makeText(DashboardActivity.this, "Discovered Services", Toast.LENGTH_LONG).show();
					if (shouldStartConnectDevice) {
						shouldStartConnectDevice = false;
						uiThreadHandler.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								BluetoothGatt gatt = BleReceivedService.getGatt();
								Log.d(TAG , "TYPE_GATT_SERVICES_DISCOVERED gatt " + gatt + " service " + BleReceivedService.getInstance());
								boolean writeResult = BleReceivedService.getInstance().setIndication(gatt, true);
								if(writeResult == false) {
									Log.d(TAG, "Write Error");
								}
							}
						}, 80L);
					}
				}
				else if(BleReceivedService.TYPE_INDICATION_VALUE.equals(type)) {
					Log.d(TAG, "DashBoard onReceive TYPE_INDICATION_VALUE");
					Bundle bundle = intent.getBundleExtra(BleReceivedService.EXTRA_VALUE);
					String uuidString = intent.getExtras().getString(BleReceivedService.EXTRA_CHARACTERISTIC_UUID);
					Log.d(TAG, "Parse :"+bundle);
					Log.d(TAG, "UUID :"+uuidString);
					receivedData(uuidString, bundle);
				}
			}
		}
	};

	private void refreshBloodPressureLayout() {
		MeasuDataManager measuDataManager = ((AndMedical_App_Global)getApplication()).getMeasuDataManager();
		Lifetrack_infobean data = measuDataManager.getCurrentDispData(MeasuDataManager.MEASU_DATA_TYPE_BP);
		boolean isExistData = (data != null);
		if(isExistData) {
			bloodpressure.setData(data);
		}
		bloodpressure.setHide(!isExistData);
	}
	
	private void refreshWeightScaleLayout() {
		MeasuDataManager measuDataManager = ((AndMedical_App_Global)getApplication()).getMeasuDataManager();
		Lifetrack_infobean data = measuDataManager.getCurrentDispData(MeasuDataManager.MEASU_DATA_TYPE_WS);
		boolean isExistData = (data != null);
		if(isExistData) {
			weightscale.setData(data);
		}
		weightscale.setHide(!isExistData);
	}
	
	private void refreshArrowVisible() {
		MeasuDataManager measuDataManager = ((AndMedical_App_Global)getApplication()).getMeasuDataManager();
		rightArrow.setVisibility( (measuDataManager.isExistFutureDatas())? View.VISIBLE : View.INVISIBLE );
		leftArrow.setVisibility( (measuDataManager.isExistPastDatas())? View.VISIBLE : View.INVISIBLE );
	}
	
	private void refreshDisplay() {
		refreshBloodPressureLayout();
		refreshWeightScaleLayout();
		refreshArrowVisible();
	}
	
	private final BroadcastReceiver mMeasudataUpdateReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(MeasuDataManager.ACTION_BP_DATA_UPDATE.equals(action)) {
				refreshBloodPressureLayout();
			}
			else if(MeasuDataManager.ACTION_WS_DATA_UPDATE.equals(action)) {
				refreshWeightScaleLayout();
			}
			
			refreshArrowVisible();
		}
	};

	public void onBackPressed() {
		ANDMedicalUtilities.CreateDialog(this, getResources().getString(R.string.dialog_confirm_exit), this);
	};

	private void receivedData(String characteristicUuidString, Bundle bundle) {
		
		if (ADGattUUID.WeightScaleMeasurement.toString().equals(characteristicUuidString) ||
			ADGattUUID.AndCustomWeightScaleMeasurement.toString().equals(characteristicUuidString)) {
			
			showIndicator(getResources().getString(R.string.indicator_start_receive));

			Log.d(TAG, "Received Data WS");
			double weight = bundle.getDouble(ADGattService.KEY_WEIGHT);
			String units = bundle.getString(ADGattService.KEY_UNIT, ADSharedPreferences.DEFAULT_WEIGHT_SCALE_UNITS);

			int year = bundle.getInt(ADGattService.KEY_YEAR);
			int month = bundle.getInt(ADGattService.KEY_MONTH);
			int day = bundle.getInt(ADGattService.KEY_DAY);
			int hours = bundle.getInt(ADGattService.KEY_HOURS);
			int minutes = bundle.getInt(ADGattService.KEY_MINUTES);
			int seconds = bundle.getInt(ADGattService.KEY_SECONDS);
			
			String weightString = String.format("%.1f", weight);
			String finaldate = String.format("%04d-%02d-%02d",year,month,day);
			String finaltime = String.format("%02d:%02d",hours,minutes);
			String finalTimeStamp = String.format("%04d-%02d-%02dT%02d:%02d:%02d",year,month,day,hours,minutes,seconds);
			
			Lifetrack_infobean infoBeanObj = new Lifetrack_infobean();
			infoBeanObj.setWeight(weightString);
			infoBeanObj.setDate(finaldate);
			infoBeanObj.setTime(finaltime);
			infoBeanObj.setWeightUnit(units);

			// 体重計の単位は、計測毎に体重計から送られてくる単位を設定する。
			ADSharedPreferences.putString(ADSharedPreferences.KEY_WEIGHT_SCALE_UNITS, units);

			infoBeanObj.setIsSynced("no");
			long dateValue = convertDateintoMs(finalTimeStamp);
			infoBeanObj.setDateTimeStamp(String.valueOf(dateValue));

			String weightDeviceId = "9DEA020D-1795-3B89-D184-DE7CD609FAD0";

			infoBeanObj.setDeviceId(weightDeviceId);

			insertObjectList.add(infoBeanObj);
			
			setIndicatorMessage(getResources().getString(R.string.indicator_during_receive));
			
			Handler handlerObj = new Handler();
			handlerObj.postDelayed(new Runnable() {
				@Override
				public void run() {

					db.weighttrackentry(insertObjectList);

					setIndicatorMessage(getResources().getString(R.string.indicator_complete_receive));

					Handler handlerObj1 = new Handler();
					handlerObj1.postDelayed(new Runnable() {
						@Override
						public void run() {

							insertObjectList.clear();
							
							MeasuDataManager measuDataManager = ((AndMedical_App_Global)getApplication()).getMeasuDataManager();
							measuDataManager.syncMeasudata(MeasuDataManager.MEASU_DATA_TYPE_WS, true);
							
							dismissIndicator();
						}
					}, 1300);
				}
			}, 1500);

		}
		else if (ADGattUUID.BloodPressureMeasurement.toString().equals(characteristicUuidString)) {
			
			showIndicator(getResources().getString(R.string.indicator_start_receive));

			Log.d(TAG, "Received Data BP");
			
			int sys = (int)bundle.getFloat(ADGattService.KEY_SYSTOLIC);
			int dia = (int)bundle.getFloat(ADGattService.KEY_DIASTOLIC);
			int pul = (int)bundle.getFloat(ADGattService.KEY_PULSE_RATE);
			int irregularPulseDetection = (int) bundle.getFloat(ADGattService.KEY_IRREGULAR_PULSE_DETECTION);
			
			int year = (int)bundle.getInt(ADGattService.KEY_YEAR);
			int month = (int)bundle.getInt(ADGattService.KEY_MONTH);
			int day = (int)bundle.getInt(ADGattService.KEY_DAY);
			
			int hours = (int)bundle.getInt(ADGattService.KEY_HOURS);
			int minutes = (int)bundle.getInt(ADGattService.KEY_MINUTES);
			int seconds = (int)bundle.getInt(ADGattService.KEY_SECONDS);
			
			String finaldate = String.format("%04d-%02d-%02d",year,month,day);
			String finaltime = String.format("%02d:%02d",hours,minutes);
			String finalTimeStamp = String.format("%04d-%02d-%02dT%02d:%02d:%02d",year,month,day,hours,minutes,seconds);
			
			Lifetrack_infobean infoBeanObj = new Lifetrack_infobean();
			infoBeanObj.setDate(finaldate);
			infoBeanObj.setTime(finaltime);
			infoBeanObj.setPulse(String.valueOf(pul));
			infoBeanObj.setSystolic(String.valueOf(sys));
			infoBeanObj.setDiastolic(String.valueOf(dia));
			infoBeanObj.setPulseUnit("bpm");
			infoBeanObj.setSystolicUnit("mmhg");
			infoBeanObj.setDiastolicUnit("mmhg");
			infoBeanObj.setIsSynced("no");
			infoBeanObj.setIrregularPulseDetection(String.valueOf(irregularPulseDetection));
			long dateValue = convertDateintoMs(finalTimeStamp);
			infoBeanObj.setDateTimeStamp(String.valueOf(dateValue));
			String weightDeviceId = "web." + ADSharedPreferences.getString(ADSharedPreferences.KEY_USER_ID, "");

			infoBeanObj.setDeviceId(weightDeviceId);

			insertObjectList.add(infoBeanObj);

			setIndicatorMessage(getResources().getString(R.string.indicator_during_receive));

			Handler handlerObj = new Handler();
			handlerObj.postDelayed(new Runnable() {
				@Override
				public void run() {

					db.bpEntry(insertObjectList);
					ArrayList<Lifetrack_infobean> hvList = new ArrayList<Lifetrack_infobean>(0);
					for (int i = 0; i < insertObjectList.size(); i++) {
						Log.d("SN", "Dashboard Systolic " + insertObjectList.get(i).getSystolic() + " Diastolic " +insertObjectList.get(i).getDiastolic() + " Pulse " + insertObjectList.get(i).getPulse());
						if (!AndMedicalLogic.checkBPError(insertObjectList.get(i).getSystolic(), insertObjectList.get(i).getDiastolic(), insertObjectList.get(i).getPulse())) {
							hvList.add(insertObjectList.get(i));
						}
					}

					setIndicatorMessage(getResources().getString(R.string.indicator_complete_receive));

					Handler handlerObj1 = new Handler();
					handlerObj1.postDelayed(new Runnable() {
						@Override
						public void run() {

							insertObjectList.clear();
							MeasuDataManager measuDataManager = ((AndMedical_App_Global)getApplication()).getMeasuDataManager();
							measuDataManager.syncMeasudata(MeasuDataManager.MEASU_DATA_TYPE_BP, true);
							
							dismissIndicator();
						}
					}, 1300);
				}
			}, 1500);
		}
	}
	
	public long convertDateintoMs(String date) {
		long final_birth_date_timestamp = 0;
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date converteddate = null;
		try {
			converteddate = (Date) formatter.parse(date);
			final_birth_date_timestamp = converteddate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return final_birth_date_timestamp;
	}
}