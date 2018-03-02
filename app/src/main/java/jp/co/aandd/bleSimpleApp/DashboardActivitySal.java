package jp.co.aandd.bleSimpleApp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;

import jp.co.aandd.bleSimpleApp.entities.AndMedical_App_Global;
import jp.co.aandd.bleSimpleApp.entities.DataBase;
import jp.co.aandd.bleSimpleApp.entities.Lifetrack_infobean;
import jp.co.aandd.bleSimpleApp.utilities.ANDMedicalUtilities;
import jp.co.aandd.bleSimpleApp.utilities.AndMedicalLogic;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.slidemenu.SlideMenu;

public class DashboardActivitySal extends ADBaseActivity {

	String monthString, daystring, localYear, final_date;
	private SlideMenu slidemenu;
	private static final int REQUEST_ENABLE_BT = 0;
	private String username = "", userType = "", today_date, display_date,
			display_calories, display_distance, display_sleep, display_steps,
			formattedDate;

	double totalcalorie1, totaldistance1, totalsleep1, totalsteps1;
	Context context;
	// RelativeLayout total_box;
	private ImageView left_arrow, main_image, right_arrow, leftArrowBP,
			rightArrowBP;

	String units = "lb";
	BluetoothDevice device;
	Handler handler;
	boolean flag = false, flagBp = false;
	private int i = 0;
	private int j = 1;
	private DataBase db;
	private String text, activityText;
	private ArrayList<Lifetrack_infobean> dataActivity_db;
	private String weightText;
	private ArrayList<Lifetrack_infobean> dbWeight;
	private ArrayList<Lifetrack_infobean> dbBPListReading;
	private Set<String> lstActivityMonitor;
	private ArrayList<String> lstActivityDate;
	private ArrayList<Date> lstActivityDateTemp;
	private ImageView ivWeightScalLeft, ivWeightScalRight, sharing;
	private LinearLayout ll_weight, ll_bpm;
	private int weightLeft = 0, activityLeft = 0, bpLeft = 0;
	private Calendar c;
	private String userID = "", auth_token = "";
	private View viewPopup;
	private RelativeLayout mainlayout;
	private ProgressDialog progressDialog;
	private boolean weightFlag, activityFlag, bpFlag;
	private String activityMinDate, bpMinDate, weightMinDate, maxDate,
			activity_date;
	private boolean bScanning = false, bscanningBp = false;
	private Lifetrack_infobean lt_info;
	ArrayList<Lifetrack_infobean> Lifetrackitems, lstUnsyncItems;
	String login_username;
	double weightresult;
	Handler mhandler;
	private Handler mHandler, mHandlerObjBp;
	Callback callback, callbackBp;
	String Weightscale_address = "";
	Dialog progress;
	ArrayList<Lifetrack_infobean> lstUnsync = new ArrayList<Lifetrack_infobean>();
	ArrayList<Lifetrack_infobean> lstInfoSynced = new ArrayList<Lifetrack_infobean>();
	SharedPreferences prefs;
	AndMedical_App_Global appGlobal;
	String newbpDate = "", sysVal = "", diaVal = "", pulVal = "",
			newBpTime = "";
	String unitType;
	String deviceSetupMode = "";
	String sys = "0", dia = "0";
	String pul = "0";
	Timer tmrConnect;
	String watchAddress;

	static String yourDate;
	int activityMonitorstatus = 0;

	static String formattedTimeWeight1, formattedTimeWeight, heartRate = "0";
	String finaldate, finaltime;
	AndMedicalLogic andMedicalLogic;

	String WeightScaleAddress, bpAddress;
	BluetoothAdapter mBluetoothAdapter;
	ArrayList<Lifetrack_infobean> lstBroadcastBpData;

	ArrayList<Lifetrack_infobean> lstBroadCastWeightdb = new ArrayList<Lifetrack_infobean>();

	@Override
	protected void onStart() {
		super.onStart();
		// Uncomment the lines for BP data Syncing and in ondestory uncomment
		// the unbind method and Uncomment in manifest file(Service declartion
		// Salbleservice43_BpMachine) also

		// Intent intentBp = new Intent(DashboardActivity.this,
		// SALBLEService43_BPmachine.class);
		// bindService(intentBp, objServiceConnectionBP,
		// Context.BIND_AUTO_CREATE);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.and_dashboard_new);
		
		sharing = (ImageView) findViewById(R.id.top_iv_right);
		appGlobal = (AndMedical_App_Global) getApplication();
		
		Lifetrackitems = new ArrayList<Lifetrack_infobean>();
		lstUnsyncItems = new ArrayList<Lifetrack_infobean>();
		prefs = getSharedPreferences("ANDMEDICAL", MODE_PRIVATE);
		login_username = prefs.getString("login_username", "");
		userType = prefs.getString("login_username", "");
		userID = prefs.getString("userId", "");
		auth_token = prefs.getString("auth_token", "");
		unitType = prefs.getString("measuringUnit", "US");
		deviceSetupMode = prefs.getString("deviceSetupMode", "weightscale");
		context = DashboardActivitySal.this;
		c = Calendar.getInstance();
		db = new DataBase(this, login_username);
		andMedicalLogic = new AndMedicalLogic();
		lstBroadcastBpData = new ArrayList<Lifetrack_infobean>();
		Calendar cTime = Calendar.getInstance();
		int mYear = cTime.get(Calendar.YEAR);
		int mMonth = cTime.get(Calendar.MONTH) + 1;
		int mDay = cTime.get(Calendar.DAY_OF_MONTH);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cTime1 = Calendar.getInstance();
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
		String test = sdf1.format(cTime1.getTime());
		Date date = new Date();

		int minute = cTime.get(Calendar.MINUTE);
		int hour = cTime.get(Calendar.HOUR);
		yourDate = mYear + "-" + pad(mMonth) + "-" + pad(mDay);
		String[] timeSpilt = test.split(":");
		formattedTimeWeight = timeSpilt[0] + ":" + timeSpilt[1];

		mhandler = new Handler();
		mHandler = new Handler(callback);

		callbackBp = new Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				Log.v("Piaring", "trying to connect service"
						+ "Callback for bp" + bpAddress);

				return false;
			}
		};
		mHandlerObjBp = new Handler(callbackBp);

		try {
			Date dateCurrent = new Date();
			DateTime dateTime = new DateTime();
			dateTime = dateTime.minusMonths(12);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
			SimpleDateFormat sdf2 = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			sdf2.setTimeZone(TimeZone.getTimeZone("GMT"));
			Lifetrack_infobean minDataReading = db.getLatestActivityDateTime();
			if (minDataReading == null) {
				activityMinDate = sdf2.format(dateTime.toDate());
			} else {
				Date dateActivity = sdf.parse(minDataReading.getDate() + "T"
						+ minDataReading.getTime());
				activityMinDate = sdf2.format(dateActivity);
			}

			minDataReading = db.getLatestBPDateTime();
			if (minDataReading == null) {
				bpMinDate = sdf2.format(dateTime.toDate());
			} else {
				Date dateBP = sdf.parse(minDataReading.getDate() + "T"
						+ minDataReading.getTime());
				bpMinDate = sdf2.format(dateBP);
			}
			db.getAllWeightDetails();
			minDataReading = db.getLatestWeightDateTime();
			if (minDataReading == null) {
				weightMinDate = sdf2.format(dateTime.toDate());
			} else {
				Date dateWeight = sdf.parse(minDataReading.getDate() + "T"
						+ minDataReading.getTime());
				weightMinDate = sdf2.format(dateWeight);
			}
			maxDate = sdf2.format(dateCurrent);
		} catch (Exception e) {
			e.printStackTrace();
		}

		mainlayout = (RelativeLayout) findViewById(R.id.root);
		layoutInflater();
		left_arrow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				right_arrow.setVisibility(View.VISIBLE);
				activityLeft++;

				setUI(dataActivity_db.get(activityLeft));
				if (activityLeft == dataActivity_db.size() - 1) {
					left_arrow.setVisibility(View.INVISIBLE);
				}
			}
		});
		right_arrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				left_arrow.setVisibility(View.VISIBLE);
				activityLeft--;

				setUI(dataActivity_db.get(activityLeft));
				if (activityLeft == 0) {
					right_arrow.setVisibility(View.INVISIBLE);
				}
			}
		});

		leftArrowBP.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				rightArrowBP.setVisibility(View.VISIBLE);
				bpLeft++;

				setBPUI(dbBPListReading.get(bpLeft));
				if (bpLeft == dbBPListReading.size() - 1) {
					leftArrowBP.setVisibility(View.INVISIBLE);
				}
			}
		});

		rightArrowBP.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				leftArrowBP.setVisibility(View.VISIBLE);
				bpLeft--;

				setBPUI(dbBPListReading.get(bpLeft));
				if (bpLeft == 0) {
					rightArrowBP.setVisibility(View.INVISIBLE);
				}
			}
		});

		ivWeightScalLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ivWeightScalRight.setVisibility(View.VISIBLE);
				weightLeft++;

				WeightScaleData(dbWeight.get(weightLeft));
				if (weightLeft == dbWeight.size() - 1) {
					ivWeightScalLeft.setVisibility(View.INVISIBLE);
				}
			}
		});

		ivWeightScalRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ivWeightScalLeft.setVisibility(View.VISIBLE);

				weightLeft--;

				WeightScaleData(dbWeight.get(weightLeft));
				if (weightLeft == 0) {
					ivWeightScalRight.setVisibility(View.INVISIBLE);
				}
			}
		});

		slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
		String addnewUserVisiblity = prefs.getString("addnewuservisiblity", "");
		String manageuservisibility = prefs.getString("manageuservisibility",
				"");
		String frommanagevisibility = prefs.getString("frommanagevisibility",
				"");

		slidemenu
				.init(this, this, 333, username, userType, addnewUserVisiblity,
						manageuservisibility, frommanagevisibility,(RelativeLayout)findViewById(R.id.rl_SwipeListner));

		slidemenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				slidemenu.show();
			}
		});
		progress = new Dialog(context);
		// AlertDialog.Builder builder = new AlertDialog.Builder(context);
		progress.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progress.setContentView(R.layout.custom_alert);
		// View progressView =
		// LayoutInflater.from(context).inflate(R.layout.custom_alert, null);
		// builder.setView(progressView);
		// progress = builder.create();
		progress.setCancelable(false);
		// progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (!login_username.equalsIgnoreCase("Guest")) {

			progress.show();

			// new RefresgTokenApi().executeOnExecutor(
			// AsyncTask.THREAD_POOL_EXECUTOR, "weight");
			Handler mbpHandler1 = new Handler();

			Handler mhandHandler = new Handler();


			Handler mbpHandler = new Handler();

		}
	}

	private void layoutInflater() {

		viewPopup = getLayoutInflater().inflate(R.layout.sync_dialog,
				mainlayout, false);

		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewPopup
				.getLayoutParams();
		mainlayout.addView(viewPopup);
		viewPopup.setVisibility(View.GONE);
		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	}

	private void scanLeDevice(boolean enable) {
		mBluetoothAdapter.startLeScan(mLeScanCallback);

	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			String address = device.getName();

			if (address.contains("A&D_UC-352") && !flag) {
				WeightScaleAddress = device.getAddress();
				flag = true;

				mHandler.sendEmptyMessage(0);

			} else
			if ((address.contains("651") || address.contains("BLP")) && !flagBp) {
				Log.v("Piaring", "Device Found" + "Scanning Callback"
						+ bpAddress);
				flagBp = true;

				// bpAddress = device.getAddress();
				// mHandlerObjBp.sendEmptyMessage(0);

			}
		}
	};

	private void setUI(Lifetrack_infobean ActivityReading) {

		float calFloat = Float.valueOf(ActivityReading.getCal());

		calFloat = Math.round(calFloat);
		int calINT = (int) calFloat;
		display_calories = String.valueOf(calINT);
		display_sleep = String.valueOf(ActivityReading.getSleep());
		display_steps = String.valueOf(ActivityReading.getSteps());

		activity_date = ANDMedicalUtilities.formatDisplayDate(ActivityReading
				.getDate() + "T" + ActivityReading.getTime());
	}

	private void setBPUI(Lifetrack_infobean lstBPReading) {
		// systolicValue.setText(lstBPReading.getSystolic());
		// diastolicValue.setText(lstBPReading.getDiastolic());
		// pulseValue.setText(lstBPReading.getPulse());
		newbpDate = lstBPReading.getDate();
		sysVal = lstBPReading.getSystolic();
		diaVal = lstBPReading.getDiastolic();
		pulVal = lstBPReading.getPulse();
		newBpTime = lstBPReading.getTime();
		if (andMedicalLogic.checkBPError(lstBPReading.getSystolic(),
				lstBPReading.getDiastolic(), lstBPReading.getPulse()))

		if (dbBPListReading.size() == 1) {
			leftArrowBP.setVisibility(View.INVISIBLE);
		}
	}

	public ArrayList<Lifetrack_infobean> WeightCalc(
			ArrayList<Lifetrack_infobean> lstReading) {
		if (lstReading != null && lstReading.size() > 0) {
			Collections.sort(lstReading, new Comparator<Lifetrack_infobean>() {
				@Override
				public int compare(Lifetrack_infobean lhs,
						Lifetrack_infobean rhs) {
					final SimpleDateFormat df = new SimpleDateFormat(
							"yyyy-MM-dd'T'HH:mm");
					formattedDate = df.format(c.getTime());
					try {
						j = df.parse(rhs.getDate() + "T" + rhs.getTime())
								.compareTo(
										df.parse(lhs.getDate() + "T"
												+ lhs.getTime()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return j;
				}
			});
			return lstReading;
		} else {
			return new ArrayList<Lifetrack_infobean>();
		}

	}

	public ArrayList<Lifetrack_infobean> activitySorting(
			ArrayList<Lifetrack_infobean> lstReading) {
		if (lstReading != null && lstReading.size() > 0) {
			Collections.sort(lstReading, new Comparator<Lifetrack_infobean>() {
				@Override
				public int compare(Lifetrack_infobean lhs,
						Lifetrack_infobean rhs) {
					final SimpleDateFormat df = new SimpleDateFormat(
							"yyyy-MM-dd'T'HH:mm");
					formattedDate = df.format(c.getTime());
					try {
						j = df.parse(rhs.getDate() + "T" + rhs.getTime())
								.compareTo(
										df.parse(lhs.getDate() + "T"
												+ lhs.getTime()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return j;
				}
			});
			return lstReading;
		} else {
			return new ArrayList<Lifetrack_infobean>();
		}

	}

	public String WeightScalConv(String weight1, String weightunit) {
		String weightString = weight1;

		if (weightString != null) {
			// String weightUnit = infoBean.getWeightUnit();

			String height = appGlobal.getDefaultHeight();
			if (height == null) {
				height = "0";
			}
			String heightUnit = appGlobal.getHeightUnit();
			float weight = Float.valueOf(weightString);

			if (weightunit.equalsIgnoreCase("lbs")) {
				weight = (float) (weight / 2.2046);
				weightString = String.format("%.1f", weight);
				weightunit = "kg";
				float mweight = (float) (weight * 2.2046);

			}
			// String mweightString = String.format("%.1f", mweight);
			// setBMIUSKG(heightUnit, height, weight);

		}
		return weightString;

	}

	public String WeightScaleData(Lifetrack_infobean infoBean) {
		if (infoBean != null) {
			weightText = infoBean.getDate();
			String weightString = infoBean.getWeight();
			String weightUnit = infoBean.getWeightUnit();

			String height = appGlobal.getDefaultHeight();
			if (height == null) {
				height = "0";
			}
			String heightUnit = appGlobal.getHeightUnit();
			if (unitType.equalsIgnoreCase("US")) {
				float weight = Float.valueOf(weightString);
				if (weightUnit.equalsIgnoreCase("kg")) {
					weight = (float) (weight * 2.2046);

					weightString = String.format("%.1f", weight);
				}
				setBMIUSKG(heightUnit, height, weight);

				weightUnit = "lbs";
			} else if (unitType.equalsIgnoreCase("metric")) {
				float weight = Float.valueOf(weightString);
				if (weightUnit.equalsIgnoreCase("lbs")) {
					weight = (float) (weight / 2.2046);
					weightString = String.format("%.1f", weight);
				}
				weightUnit = "kg";
				// float mweight = (float) (weight * 2.2046);
				// String mweightString = String.format("%.1f", mweight);
				setBMIUSKG(heightUnit, height, weight);

			}
		}
		return infoBean.getWeight();

	}

	private void setBMIUSKG(String heightUnit, String height, float weight) {
		float heightFloat = Float.valueOf(height);

		if (heightUnit.equalsIgnoreCase("feet")) {
			heightFloat = (float) (heightFloat * 12.0);
		} else {
			heightFloat = (float) (heightFloat * 0.39);
		}

		if (heightFloat != 0) {
			String bmi = String.format("%.1f",
					((weight * 703) / (heightFloat * heightFloat)));

		}
	}

	private class parseActivityData
			extends
			AsyncTask<ArrayList<Lifetrack_infobean>, String, ArrayList<Lifetrack_infobean>> {

		@Override
		protected ArrayList<Lifetrack_infobean> doInBackground(
				ArrayList<Lifetrack_infobean>... params) {
			if (params[0].size() > 0) {
				db.lifetrackentry(params[0]);
			}

			ArrayList<Lifetrack_infobean> lstActivityReading = activitySorting(db
					.getAllActivityDetails());

			return lstActivityReading;
		}
	}

	private class UploadSyncingDatabase
			extends
			AsyncTask<ArrayList<Lifetrack_infobean>, String, ArrayList<Lifetrack_infobean>> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected ArrayList<Lifetrack_infobean> doInBackground(
				ArrayList<Lifetrack_infobean>... params) {
			if (params[0].size() > 0) {
				db.lifetrackentry(params[0]);
			}

			ArrayList<Lifetrack_infobean> lstActivityReading = activitySorting(db
					.getAllActivityDetails());

			return lstActivityReading;
		}

		@Override
		protected void onPostExecute(ArrayList<Lifetrack_infobean> lstLifeBean) {
			activityFlag = true;

			if (lstLifeBean != null && lstLifeBean.size() > 0) {
				// setUI(lstLifeBean.get(0));

				dataActivity_db = lstLifeBean;
				// setUI(lstLifeBean.get(0));
				if (dataActivity_db.size() == 1) {
					left_arrow.setVisibility(View.INVISIBLE);
				}
			} 
		}

	}

	private class parseBPData
			extends
			AsyncTask<ArrayList<Lifetrack_infobean>, String, ArrayList<Lifetrack_infobean>> {

		@Override
		protected ArrayList<Lifetrack_infobean> doInBackground(
				ArrayList<Lifetrack_infobean>... params) {
			if (params[0].size() > 0) {
				db.bpEntry(params[0]);
			}
			ArrayList<Lifetrack_infobean> lstBPReading = WeightCalc(db
					.getbpDetails());

			return lstBPReading;
		}

		@Override
		protected void onPostExecute(ArrayList<Lifetrack_infobean> lstBPReading) {
			bpFlag = true;

			if (lstBPReading != null && lstBPReading.size() > 0) {
				dbBPListReading = lstBPReading;
				setBPUI(lstBPReading.get(0));
			}
			if (bpFlag && weightFlag && activityFlag) {
				progress.dismiss();
				viewPopup.setVisibility(View.GONE);
				TranslateAnimation trans = new TranslateAnimation(0, 0, 0, -300);
				trans.setDuration(500);
				viewPopup.setAnimation(trans);
				viewPopup.startAnimation(trans);
				Log.v("check status", "BP");

			}
		}

	}

	private class parseWeightData
			extends
			AsyncTask<ArrayList<Lifetrack_infobean>, String, ArrayList<Lifetrack_infobean>> {

		@Override
		protected ArrayList<Lifetrack_infobean> doInBackground(
				ArrayList<Lifetrack_infobean>... params) {
			if (params[0].size() > 0) {
				db.weighttrackentry(params[0]);
			}
			ArrayList<Lifetrack_infobean> lstWeightDate = WeightCalc(db
					.getAllWeightDetails());

			return lstWeightDate;
		}

		@Override
		protected void onPostExecute(ArrayList<Lifetrack_infobean> lstWeightDate) {
			dbWeight = lstWeightDate;
			if (lstWeightDate != null && lstWeightDate.size() == 1) {
				ivWeightScalRight.setVisibility(View.INVISIBLE);
				ivWeightScalLeft.setVisibility(View.INVISIBLE);
			}
			weightFlag = true;

			if (bpFlag && weightFlag && activityFlag) {
				Log.v("check status", "Weight");

				progress.dismiss();
				viewPopup.setVisibility(View.GONE);
				TranslateAnimation trans = new TranslateAnimation(0, 0, 0, -300);
				trans.setDuration(500);
				viewPopup.setAnimation(trans);
				viewPopup.startAnimation(trans);

			}
		}

	}

	public boolean isBluetoothEnable() {
		boolean status = true;

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}

		return status;
	}

	@Override
	protected void onResume() {
		bscanningBp = false;
		super.onResume();

	}


	private class getUnsyncedWeighhtReadings extends
			AsyncTask<String, String, ArrayList<Lifetrack_infobean>> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected ArrayList<Lifetrack_infobean> doInBackground(String... params) {
			ArrayList<Lifetrack_infobean> lstLifeBean = db
					.getUnSyncWeightDetails("no");
			return lstLifeBean;
		}

		@Override
		protected void onPostExecute(ArrayList<Lifetrack_infobean> lstLifeBean) {
			lstUnsync = lstLifeBean;
			if (lstLifeBean != null && lstLifeBean.size() > 0) {
				for (int i = 0; i < lstLifeBean.size(); i++) {
					try {
						makeJSONPOSTWeight(lstLifeBean, i);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				// weight_scale_value.setText(String.valueOf(weightresult));

			}
		}

	}

	private synchronized void makeJSONPOSTWeight(
			ArrayList<Lifetrack_infobean> lstActivityReading, int position)
			throws ParseException {
		// uploadPosition = position;

		// TODO CHangeging in future
		// String readingDatetime = lstActivityReading.get(position).getDate()
		// + "T" + lstActivityReading.get(position).getTime() + ":00";
		String readingDatetime;

		readingDatetime = lstActivityReading.get(position).getDate() + "T"
				+ lstActivityReading.get(position).getTime() + ":00";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = sdf.parse(readingDatetime);
		SimpleDateFormat sdf2 = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		sdf2.setTimeZone(TimeZone.getTimeZone("GMT"));
		String formattedDate = sdf2.format(date);
		String[] readingType = { "weight" };
		JSONArray arrayMain = new JSONArray();
		if (lstActivityReading != null && lstActivityReading.size() > 0) {
			// for (int j = 0; j < lstActivityReading.size(); j++)
			// {
			JSONObject objMain = new JSONObject();
			try {
				objMain.put("userId", userID);
				objMain.put("readingType", "weight");
				objMain.put("readingTakenTime", ANDMedicalUtilities
						.convertDateintoMs(formattedDate.replace("+", "-")));
				// if (readingType[i].equalsIgnoreCase("weight")) {
				// String deviceid=prefs.getString("weightdeviceid", "");
				objMain.put("deviceId", "9DEA020D-1795-3B89-D184-DE7CD609FAD0");
				// }

				JSONArray arrayMeasure = new JSONArray();
				for (int i = 0; i < readingType.length; i++) {
					JSONObject objMeasurement = new JSONObject();
					objMeasurement.put("measurementType", readingType[i]);
					if (readingType[i].equalsIgnoreCase("weight")) {
						objMeasurement.put("units",
								lstActivityReading.get(position)
										.getWeightUnit());
						objMeasurement.put("value",
								lstActivityReading.get(position).getWeight());

					}
					arrayMeasure.put(objMeasurement);
				}
				JSONObject jsonMetaData = new JSONObject();
				jsonMetaData.put("lastChangedBy", "self");
				objMain.put("measurements", (Object) arrayMeasure);
				objMain.put("metadata", jsonMetaData);
				arrayMain.put(objMain);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// }
		}
	}

	// BP Enty
	private class getUnsyncedBPReadings extends
			AsyncTask<String, String, ArrayList<Lifetrack_infobean>> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected ArrayList<Lifetrack_infobean> doInBackground(String... params) {
			ArrayList<Lifetrack_infobean> lstLifeBean = db
					.getUnSyncBPtDetails("no");
			return lstLifeBean;
		}

		@Override
		protected void onPostExecute(ArrayList<Lifetrack_infobean> lstLifeBean) {
			lstUnsync = lstLifeBean;
			if (lstLifeBean != null && lstLifeBean.size() > 0) {
				for (int i = 0; i < lstLifeBean.size(); i++) {
					try {
						makeJSONPOSTBP(lstLifeBean, i);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}

			}
		}

	}

	private synchronized void makeJSONPOSTBP(
			ArrayList<Lifetrack_infobean> lstActivityReading, int position)
			throws ParseException {
		// uploadPosition = position;

		// TODO CHangeging in future
		// String readingDatetime = lstActivityReading.get(position).getDate()
		// + "T" + lstActivityReading.get(position).getTime() + ":00";
		String readingDatetime;

		readingDatetime = lstActivityReading.get(position).getDate() + "T"
				+ lstActivityReading.get(position).getTime() + ":00";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = sdf.parse(readingDatetime);
		SimpleDateFormat sdf2 = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		sdf2.setTimeZone(TimeZone.getTimeZone("GMT"));
		String formattedDate = sdf2.format(date);
		String[] readingType = { "pulse", "systolic", "diastolic" };
		JSONArray arrayMain = new JSONArray();
		if (lstActivityReading != null && lstActivityReading.size() > 0) {
			// for (int j = 0; j < lstActivityReading.size(); j++)
			// {
			JSONObject objMain = new JSONObject();
			try {
				objMain.put("userId", Integer.parseInt(userID));
				objMain.put("readingType", "bp");
				objMain.put("readingTakenTime", String
						.valueOf(ANDMedicalUtilities
								.convertDateintoMs(formattedDate.replace("+",
										"-"))));
				// if (readingType[i].equalsIgnoreCase("weight")) {
				// String deviceid=prefs.getString("weightdeviceid", "");
				objMain.put("deviceId", "7FD55184-262A-5F46-52E6-57E97F7F368C");
				JSONArray arrayMeasure = new JSONArray();
				for (int i = 0; i < readingType.length; i++) {
					JSONObject objMeasurement = new JSONObject();
					objMeasurement.put("measurementType", readingType[i]);
					if (readingType[i].equalsIgnoreCase("pulse")) {
						objMeasurement.put("value",
								lstActivityReading.get(position).getPulse());
						objMeasurement.put("units", "bpm");
						objMeasurement.put("startTime", null);
						objMeasurement.put("endTime", null);
						objMeasurement.put("quality", null);

					} else if (readingType[i].equalsIgnoreCase("systolic")) {
						objMeasurement.put("value",
								lstActivityReading.get(position).getSystolic());
						objMeasurement.put("units", "mmHg");
						objMeasurement.put("startTime", null);
						objMeasurement.put("endTime", null);
						objMeasurement.put("quality", null);

					} else if (readingType[i].equalsIgnoreCase("diastolic")) {
						objMeasurement
								.put("value", lstActivityReading.get(position)
										.getDiastolic());
						objMeasurement.put("units", "mmHg");
						objMeasurement.put("startTime", null);
						objMeasurement.put("endTime", null);
						objMeasurement.put("quality", null);

					}
					// objMeasurement.put("startTime", "");
					// objMeasurement.put("endTime", "");
					// objMeasurement.put("quality", "");

					arrayMeasure.put(objMeasurement);
				}
				JSONObject jsonMetaData = new JSONObject();
				jsonMetaData.put("lastChangedBy", "self");
				objMain.put("measurements", (Object) arrayMeasure);
				objMain.put("metadata", jsonMetaData);
				arrayMain.put(objMain);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// }
		}
	}

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		return intentFilter;
	}

	private ServiceConnection objServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// objService = new SALBLEService43();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	};

	public void onBackPressed() {
		super.onBackPressed();

		Intent intentobj = new Intent(DashboardActivitySal.this,
				DeviceSetUpActivity.class);
		startActivity(intentobj);

	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindService(objServiceConnection);
		// if(objService!=null)

		// uncomment the code for bp data sync
		// unbindService(objServiceConnectionBP);

		mBluetoothAdapter.stopLeScan(mLeScanCallback);
	}

	@Override
	protected void onStop() {

		super.onStop();

	}

	public void showmsgopen(String open) {

		viewPopup.setVisibility(View.GONE);
		TranslateAnimation trans = new TranslateAnimation(0, 0, -300, 0);
		trans.setDuration(500);
		viewPopup.setAnimation(trans);
		viewPopup.startAnimation(trans);

	}

	public void showmsgclose() {
		viewPopup.setVisibility(View.GONE);
		TranslateAnimation trans = new TranslateAnimation(0, 0, 0, -300);
		trans.setDuration(500);
		viewPopup.setAnimation(trans);
		viewPopup.startAnimation(trans);
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	public static long convertDateintoMs(String date) {
		long final_birth_date_timestamp = 0;
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date converteddate = null;
		try {
			converteddate = (Date) formatter.parse(date);
			final_birth_date_timestamp = converteddate.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return final_birth_date_timestamp;
	}

	public static long cnvrtDateintoMs(String date) {
		long final_birth_date_timestamp = 0;
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		Date converteddate = null;
		try {
			converteddate = (Date) formatter.parse(date);
			final_birth_date_timestamp = converteddate.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return final_birth_date_timestamp;
	}

	@Override
	public SlideMenu getSlideMenu() {
		return slidemenu;
	}

}
