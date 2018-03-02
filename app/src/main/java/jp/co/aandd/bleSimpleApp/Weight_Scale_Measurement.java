package jp.co.aandd.bleSimpleApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import jp.co.aandd.bleSimpleApp.entities.DataBase;
import jp.co.aandd.bleSimpleApp.entities.Lifetrack_infobean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/*
 * Class for Weight Scale Measurement
 */
public class Weight_Scale_Measurement extends Activity {
	boolean bScanning = true;
	LinearLayout background;
	Context context;
	DataBase db;
	SQLiteDatabase sqldbObj;
	String formattedDate,formattedTime;
	Handler handler;
	int i = 0;
	int j = 1;
	int result;
	TextView header, txt1, txt2;
	Button btnsave;
	BluetoothAdapter mBluetoothAdapter;
	private static final int REQUEST_ENABLE_BT = 0;
	protected Handler objDevListHandler = null;
	private boolean mScanning;
	private Handler mHandler;
	boolean flag = false;
	// Stops scanning after 10 seconds.
	private static final long SCAN_PERIOD = 10000;
	Callback callback;
	String Weightscale_address;
	Handler mhandler;
	double weightresult;
	ArrayList<Lifetrack_infobean> lstUnsync = new ArrayList<Lifetrack_infobean>();
	ArrayList<Lifetrack_infobean> lstInfoSynced = new ArrayList<Lifetrack_infobean>();
	private String userID = "", auth_token = "";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weight_measurement);
		mHandler = new Handler();
		context = Weight_Scale_Measurement.this;
		SharedPreferences prefs = getSharedPreferences("ANDMEDICAL",
				MODE_PRIVATE);
		String login_username = prefs.getString("login_username", "");
		userID = prefs.getString("userId", "");
		auth_token = prefs.getString("auth_token", "");
		db = new DataBase(context, login_username);
		mhandler = new Handler();
		mHandler = new Handler(callback);

		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		formattedDate = df.format(c.getTime());
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		formattedTime = timeFormat.format(c.getTime());

		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();

		// Checks if Bluetooth is supported on the device.
		if (mBluetoothAdapter == null) {

			finish();
			return;
		} else {
		}
	}
	/*
	 * Broadcast Reciever for Updating Weigh Scale Values
	 */
	private final BroadcastReceiver mGattUpdateReceiverWeight = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
		}
	};
	/*
	 * Method for Scan Ble Device
	 */
	private void scanLeDevice(boolean enable) {

		mBluetoothAdapter.startLeScan(mLeScanCallback);

	}
	/*
	 * Method for BLE Call Back for BLE Device Scan
	 */
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			Log.v("Deviceaddress", device.getAddress());
			Weightscale_address = device.getAddress();
			if (device.getName().contains("A&D") && !flag) {
				flag = true;
				mHandler.sendEmptyMessage(0);
			}
		}
	};
	/*
	 * Method to Check the Bluetooth of the DEvice is Enable or Disabled
	 */
	public boolean isBluetoothEnable() {
		boolean status = true;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}

		return status;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (!mBluetoothAdapter.isEnabled()) {
			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}

		registerReceiver(mGattUpdateReceiverWeight,makeGattUpdateIntentFilter());
	}

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();

		return intentFilter;
	}
	/*
	 * Calling Webservice for Updating the Reading of Weight Scale
	 */
	private synchronized void makeJSONPOST (ArrayList<Lifetrack_infobean> lstActivityReading, int position) throws ParseException
	{
		String readingDatetime = lstActivityReading.get(position).getDate()+"T"+lstActivityReading.get(position).getTime()+":00";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = sdf.parse(readingDatetime);
		SimpleDateFormat sdf2 = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		sdf2.setTimeZone(TimeZone.getTimeZone("GMT"));
		String formattedDate = sdf2.format(date);
		String[] readingType = {"weight"};
		JSONArray arrayMain = new JSONArray();
		if(lstActivityReading != null && lstActivityReading.size() > 0)
		{
			JSONObject objMain = new JSONObject();
			try 
			{
				objMain.put("userId", userID);
				objMain.put("readingType", "weight");
				objMain.put("readingTakenTime", formattedDate.replace("+", "-"));

				JSONArray arrayMeasure = new JSONArray();
				for (int i = 0; i < readingType.length; i++) 
				{
					JSONObject objMeasurement = new JSONObject();
					objMeasurement.put("measurementType", readingType[i]);
					if(readingType[i].equalsIgnoreCase("weight"))
					{
						objMeasurement.put("units", lstActivityReading.get(position).getWeightUnit());
						objMeasurement.put("value", lstActivityReading.get(position).getWeight());

					}
					arrayMeasure.put(objMeasurement);
				}
				JSONObject jsonMetaData = new JSONObject();
				jsonMetaData.put("lastChangedBy", "self");
				objMain.put("measurements", (Object)arrayMeasure);
				objMain.put("metadata", jsonMetaData);
				arrayMain.put(objMain);
			} catch (JSONException e) 
			{
				e.printStackTrace();
			}
		}
	}

	/*
	 * Method for Getting Usynced Activity MOnitor Reading
	 */
	private class getUnsyncedActivityReadings extends AsyncTask<String, String, ArrayList<Lifetrack_infobean>> 
	{
		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see android.os.AsyncTask#doInBackground(Params[])
			 */
		}

		@Override
		protected ArrayList<Lifetrack_infobean> doInBackground(String... params) 
		{
			ArrayList<Lifetrack_infobean> lstLifeBean = db.getUnSyncWeightDetails("no");
			return lstLifeBean;
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(ArrayList<Lifetrack_infobean> lstLifeBean) 
		{
			lstUnsync = lstLifeBean;
			if(lstLifeBean != null && lstLifeBean.size() > 0)
			{
				for (int i = 0; i < lstLifeBean.size(); i++) 
				{
					try {
						makeJSONPOST(lstLifeBean, i);
					} catch (ParseException e) 
					{
						e.printStackTrace();
					}
				}
			}
		}

	}
}
