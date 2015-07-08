package jp.co.aandd.bleSimpleApp.gatt;

import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class BleConnectService extends Service {
	
    public class BleConnectionBinder extends Binder {
    	public BleConnectService getService() {
            return BleConnectService.this;
        }
    }
    private final IBinder mBinder = new BleConnectionBinder();
	
	public static final String ACTION_DEVICE_SCAN = "com.andmedical.gatt.action.device_scan";
	public static final String ACTION_DEVICE_SETUP = "com.andmedical.gatt.action.device_setup";
	public static final String ACTION_DEVICE_CONNECT = "com.andmedical.gatt.action.device_connect";
	public static final String ACTION_DEVICE_DISCONNECT = "com.andmedical.gatt.action.device_disconnect";
	public static final String ACTION_DEVICE_REQUEST_PAIRING = "com.andmedical.gatt.action.device_request_pairing";
	public static final String ACTION_READ_CHARACTER = "com.andmedical.gatt.action.read_character";
	public static final String ACTION_WRITE_CHARACTER = "com.andmedical.gatt.action.write_character";
	public static final String ACTION_DISCOVERED_SERVICES = "com.andmedical.gatt.action.discovered_services";
	public static final IntentFilter BleConnectionFilter = new IntentFilter(){{
		addAction(ACTION_DEVICE_SCAN);
		addAction(ACTION_DEVICE_SETUP);
		addAction(ACTION_DEVICE_CONNECT);
		addAction(ACTION_DEVICE_DISCONNECT);
		addAction(ACTION_DEVICE_REQUEST_PAIRING);
		addAction(ACTION_READ_CHARACTER);
		addAction(ACTION_WRITE_CHARACTER);
		addAction(ACTION_DISCOVERED_SERVICES);
	}};
	
	public static final String KEY_DEVICE_ADDRES = "com.andmedical.gatt.DeviceAddressKey";
	public static final String KEY_RESULT = "com.andmedical.gatt.ResultKey";
	public static final String KEY_UUID_STRING = "com.andmedical.gatt.UUIDStringKey";

	public static final int REQUEST_ENABLE_BT = 200;
	
	private BluetoothAdapter mBluetoothAdapter;
	protected SetupDevice mSetupDevice;
	protected boolean mIsReserveScan = false;
	private Handler delayHandler;
	@Override
	public void onCreate() {
		super.onCreate();
		delayHandler = new Handler();
		IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
	    registerReceiver(mBluetoothStateChangeReceiver, filter);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		unregisterReceiver(mBluetoothStateChangeReceiver);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
	}
	
	public void startScanDevices() {

		if(isEnableBluetoothFunction(this)) {
			BluetoothAdapter adapter = getBluetoothAdapter();
			if(adapter.isEnabled()) {
				adapter.startLeScan(mLeScanCallback);
				mIsReserveScan = false;
			}
			else {
				mIsReserveScan = true;
				adapter.enable();
			}
		}
	}
	
	public void stopScanDevice() {

		if(isEnableBluetoothFunction(this)) {
			BluetoothAdapter adapter = getBluetoothAdapter();
			adapter.stopLeScan(mLeScanCallback);
			adapter.cancelDiscovery();
		}
	}
	
	private final BroadcastReceiver mBluetoothStateChangeReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        final String action = intent.getAction();

	        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
	            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
	                                                 BluetoothAdapter.ERROR);
	            switch (state) {
	            case BluetoothAdapter.STATE_OFF:
	            	// to do Nothing
	                break;
	            case BluetoothAdapter.STATE_TURNING_OFF:
	            	// to do Nothing
	                break;
	            case BluetoothAdapter.STATE_ON:
	            	// Bluetoothが有効になってから、
	            	// デバイス検索を始める.
	            	if(mIsReserveScan) {
	            		startScanDevices();
	            	}
	                break;
	            case BluetoothAdapter.STATE_TURNING_ON:
	            	// to do Nothing
	                break;
	            }
	        }
	    }
	};

	protected void sendMessage(String action,Bundle bundle) {
        
        Intent intent = new Intent();
        intent.setAction(action);
        if(bundle != null) {
        	intent.putExtras(bundle);
        }
        sendBroadcast(intent);
	}
	
	protected BluetoothAdapter getBluetoothAdapter() {
		
		if(mBluetoothAdapter == null) {
			if(Build.VERSION.SDK_INT >= 18) {
				// for API 18
				final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
				mBluetoothAdapter = bluetoothManager.getAdapter();
			}
			else {
				mBluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
			}
		}
		return mBluetoothAdapter;
	}
	
	// ----------------  ------------------//
	// BLE Connetion Methods
	// ----------------  ------------------//
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, int rssi,
				byte[] scanRecord) {

			if (ableToScanDevice(device)) {
				// 特定の端末のみペアリング
		        Bundle bundle = new Bundle();
		        bundle.putString(KEY_DEVICE_ADDRES, device.getAddress());
		        sendMessage(ACTION_DEVICE_SCAN, bundle);
			}
		}
	};
	
	protected boolean ableToScanDevice(BluetoothDevice device) {
		String deviceName = device.getName();
		return (deviceName.contains("A&D"));
	}
	
	public void connectDevice(String address) {
		if(getBluetoothAdapter() == null || mSetupDevice != null) {
			return ;
		}
		
		BluetoothDevice device = getBluetoothAdapter().getRemoteDevice(address);
		if(device != null) {
			
			mSetupDevice = new SetupDevice(device);
			mSetupDevice.connect(this);
		}
	}
	
	public void reConnectDevice(String address) {
		if(getBluetoothAdapter() == null || mSetupDevice == null) {
			return ;
		}
		mSetupDevice.reConnect();
		mSetupDevice.discoverServices();
	}
	
	public void disConnectDevice() {
		if(mSetupDevice != null) {
			
			// 接続終了時に設定が完了している場合、成功とみなす。
			boolean isSuccess = mSetupDevice.isSetupFinish;
			Bundle bundle = new Bundle();
	        bundle.putBoolean(KEY_RESULT, isSuccess);
	        sendMessage(ACTION_DEVICE_SETUP, bundle);
			
			mSetupDevice.disconnect();
			mSetupDevice = null;
		}
	}
	
	/**
	 * Gatt CallBack
	 */
	protected final BluetoothGattCallback mBleCallback = new BluetoothGattCallback() {
		
		/** Tips
		 * CallBack内でのGATTへのアクセスは、引数を使用すること。
		 * また、メソッドを継承用に外に出している。
		 * 今後、OverRideするメソッドを増やすようなら、同様の実装をするように。
		 */
		
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			super.onConnectionStateChange(gatt, status, newState);
			BleConnectService.this.onConnectionStateChange(gatt, status, newState);
		}

		@Override
		public void onServicesDiscovered(final BluetoothGatt gatt,final int status) {
			super.onServicesDiscovered(gatt, status);
			
			delayHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					BleConnectService.this.onServicesDiscovered(gatt, status);
				}
			}, 1000L);
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			super.onCharacteristicRead(gatt, characteristic, status);
			BleConnectService.this.onCharacteristicRead(gatt, characteristic, status);
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			super.onCharacteristicWrite(gatt, characteristic, status);
			BleConnectService.this.onCharacteristicWrite(gatt, characteristic, status);
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			super.onCharacteristicChanged(gatt, characteristic);
			BleConnectService.this.onCharacteristicChanged(gatt, characteristic);
		}

		@Override
		public void onDescriptorRead(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			super.onDescriptorRead(gatt, descriptor, status);
			BleConnectService.this.onDescriptorRead(gatt, descriptor, status);
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			super.onDescriptorWrite(gatt, descriptor, status);
			BleConnectService.this.onDescriptorWrite(gatt, descriptor, status);
		}
	};
	
	protected void onConnectionStateChange(BluetoothGatt gatt, int status,
			int newState) {
		
		if(mSetupDevice == null) {
			return ;
		}
		
		if(status != BluetoothGatt.GATT_SUCCESS) {
			Log.d("SN", "GATT Connect Failed : "+status);
		}

		if (newState == BluetoothProfile.STATE_DISCONNECTED) {
			Log.d("SN", "Disconnec Gatt");
			sendMessage(ACTION_DEVICE_DISCONNECT, null);
		}
		else if (newState == BluetoothProfile.STATE_CONNECTED) {

			// ペアリング状態を取得
			BluetoothDevice device = gatt.getDevice();
			int bondstate = device.getBondState();
			
			if(bondstate != BluetoothDevice.BOND_BONDED) {
				// ペアリングしていない場合、ペアリングを要求する。
				sendMessage(ACTION_DEVICE_REQUEST_PAIRING, null);
			}
			else {
				// ペアリング済みなので、サービスを取得しにいく。
				mSetupDevice.discoverServices();
			}
		}
	}

	protected void onServicesDiscovered(BluetoothGatt gatt, int status) {
		Log.d("SN", "Discovered");
		Bundle bundle = new Bundle();
	    sendMessage(ACTION_DISCOVERED_SERVICES, bundle);
	}

	protected void onCharacteristicRead(BluetoothGatt gatt,
			BluetoothGattCharacteristic characteristic, int status) {
		Log.d("SN", "Read");
		Bundle bundle = new Bundle();
		bundle.putBoolean(KEY_RESULT, (status == BluetoothGatt.GATT_SUCCESS));
		bundle.putString(KEY_UUID_STRING, characteristic.getUuid().toString());
	    sendMessage(ACTION_READ_CHARACTER, bundle);
	}

	protected void onCharacteristicWrite(BluetoothGatt gatt,
			BluetoothGattCharacteristic characteristic, int status) {
		Log.d("SN", "Write");
		Bundle bundle = new Bundle();
		bundle.putBoolean(KEY_RESULT, (status == BluetoothGatt.GATT_SUCCESS));
		bundle.putString(KEY_UUID_STRING, characteristic.getUuid().toString());
	    sendMessage(ACTION_WRITE_CHARACTER, bundle);
	}

	protected void onCharacteristicChanged(BluetoothGatt gatt,
			BluetoothGattCharacteristic characteristic) {
		Log.d("SN", "Change :");
	}

	protected void onDescriptorRead(BluetoothGatt gatt,
			BluetoothGattDescriptor descriptor, int status) {
		Log.d("SN", "Read Desc :");
	}

	public void onDescriptorWrite(BluetoothGatt gatt,
			BluetoothGattDescriptor descriptor, int status) {
		Log.d("SN", "Write Desc :");
	}
	
	public void setupDateTime() {
		if(mSetupDevice != null && mSetupDevice.gatt != null) {
			setDateTimeSetting(mSetupDevice.gatt, Calendar.getInstance());
		}
	}
	
	protected void setDateTimeSetting(BluetoothGatt gatt,Calendar cal) {
		
		BluetoothGattService gattService = getGattSearvice(gatt);
		if(gattService != null) {
			BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(ADGattUUID.DateTime);
			if(characteristic != null) {
				characteristic = DateTime.writeCharacteristic(characteristic, Calendar.getInstance());
				gatt.writeCharacteristic(characteristic);
			}
		}
	}
	
	protected static void setNotificationSetting(BluetoothGatt gatt,boolean notificationEnable) {

		BluetoothGattService gattService = getGattSearvice(gatt);
		
		if(gattService != null) {
			BluetoothGattCharacteristic characteristic = getGattMeasuCharacteristic(gattService);
			
			if(characteristic != null) {
				
				// Notification を要求する
                boolean registered = gatt.setCharacteristicNotification(characteristic, notificationEnable);
 
                // Characteristic の Notification 有効化
                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(ADGattUUID.ClientCharacteristicConfiguration);
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                gatt.writeDescriptor(descriptor);
 
                if (registered) {
                	// Characteristics通知設定が成功
                } else {
                    // Characteristics通知設定が失敗
                }
			}
			else {
				// Error
				/** Tips: キャラクタが取得できない
				 * 原因は
				 * ・接続対象のデバイスではない
				 * ・UUIDが間違っている
				 */
			}
		}
		else {
			/** Tips: サービスが取得できない
			 * 原因は
			 * ・ペアリングが成功していない
			 * ・接続対象のデバイスではない
			 * ・UUIDが間違っている
			 */
		}
	}
	
	public void setupSuccess() {
		mSetupDevice.isSetupFinish = true;
		disConnectDevice();
	}
	
	protected static BluetoothGattService getGattSearvice(BluetoothGatt gatt) {
		BluetoothGattService service = null;
		for(UUID uuid : ADGattUUID.ServicesUUIDs) {
			service = gatt.getService(uuid);
			if(service != null)break;
		}
		return service;
	}
	
	protected static BluetoothGattCharacteristic getGattMeasuCharacteristic(BluetoothGattService service) {
		BluetoothGattCharacteristic characteristic = null;
		for(UUID uuid : ADGattUUID.MeasuCharacUUIDs) {
			characteristic = service.getCharacteristic(uuid);
			if(characteristic != null)break;
		}
		return characteristic;
	}
	
	public static boolean isEnableBluetooth(Context context) {
		if(Build.VERSION.SDK_INT >= 18) {
			// for API 18
			final BluetoothManager bluetoothManager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
			return bluetoothManager.getAdapter().isEnabled();
		}
		else {
			return BluetoothAdapter.getDefaultAdapter().isEnabled();
		}
	}
	
	public static boolean isEnableBluetoothFunction(Context context) {
		if(Build.VERSION.SDK_INT >= 18) {
			// for API 18
			final BluetoothManager bluetoothManager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
			return (bluetoothManager.getAdapter() != null);
		}
		else {
			return (BluetoothAdapter.getDefaultAdapter() != null);
		}
	}
	
	public static BluetoothDevice getBluetoothDevice(Context context ,String address) {
		if(isEnableBluetoothFunction(context)) {
			
			if(Build.VERSION.SDK_INT >= 18) {
				// for API 18
				final BluetoothManager bluetoothManager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
				return bluetoothManager.getAdapter().getRemoteDevice(address);
			}
			else {
				return BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
			}
		}
		
		return null;
	}
	
	public static boolean isPairingDevice(Context context, String address) {
		if(!isEnableBluetoothFunction(context)) {
			return false;
		}
		
		Set<BluetoothDevice> pairingDevices = null;
		BluetoothDevice device = null;
		
		if(Build.VERSION.SDK_INT >= 18) {
			// for API 18
			final BluetoothManager bluetoothManager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
			pairingDevices = bluetoothManager.getAdapter().getBondedDevices();
			device = bluetoothManager.getAdapter().getRemoteDevice(address);
		}
		else {
			pairingDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
			device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
		}
		
		if(pairingDevices == null || device == null) {
			return false;
		}
		
		return pairingDevices.contains(device);
	}

	protected class SetupDevice {
		
		private final BluetoothDevice device;
		public BluetoothGatt gatt;
		public boolean isSetupFinish = false;
		
		public SetupDevice(BluetoothDevice device) {
			super();
			this.device = device;
		}
		
		public boolean discoverServices() {
			return (gatt != null && gatt.discoverServices());
		}
		
		public void connect(final Context context) {
			if(gatt != null) {
				disconnect();
			}
			new Handler().postDelayed(new Runnable(){

				@Override
				public void run() {
					gatt = device.connectGatt(context, true, mBleCallback);
				}}, 500);
			
		}
		
		public void reConnect(){
			if(gatt != null) {
				gatt.connect();
			}
		}
		
		public void disconnect() {
			if(gatt != null) {
				gatt.disconnect();
				gatt.close();
				gatt = null;
			}
		}
	}
}
