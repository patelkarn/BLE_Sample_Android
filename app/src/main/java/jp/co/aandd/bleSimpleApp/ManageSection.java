package jp.co.aandd.bleSimpleApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import jp.co.aandd.bleSimpleApp.entities.ANDmedicalCommons;
import jp.co.aandd.bleSimpleApp.entities.AndMedical_App_Global;
import jp.co.aandd.bleSimpleApp.entities.DataBase;
import jp.co.aandd.bleSimpleApp.entities.RegistrationInfoBean;
import jp.co.aandd.bleSimpleApp.utilities.ANDMedicalUtilities;

import org.joda.time.DateMidnight;
import org.joda.time.Years;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.slidemenu.SlideMenu;

/*
 * Class for  Managing Create Account
 */
public class ManageSection extends ADBaseActivity {

	private SlideMenu slidemenu;
	private boolean ismale = true;
	String username = "", userType = "", userPassword = "";
	SharedPreferences prefs;
	private boolean isChecked;
	private LinearLayout linearDate;
	static final int DATE_DIALOG_ID = 1;
	private Calendar myCalendar = Calendar.getInstance();
	SimpleDateFormat fmtDate = new SimpleDateFormat("MM dd yyyy");
	SimpleDateFormat day = new SimpleDateFormat("dd");
	SimpleDateFormat month = new SimpleDateFormat("MM");
	SimpleDateFormat year = new SimpleDateFormat("yyyy");
	String readingTakenTimeValues = "";
	private int mYear, mMonth, mDay, yearOld1, mMonth1, mDay1, mYear1;
	private String finalDate, fname, lname, email1, password1, confrmpassword1,
			final_height, timezoneID, response_code, statusMessage;
	private TextView txtdate, txtmonth, txtyear, tvDeleteReading;
	private Context context;
	private Button btncreateaccnt, btncancel;
	private EditText firstname, lastname, emailid, password, confirmpassword;
	private int diffYears = 0;
	boolean isSpinnerWeight = false;
	View hiddenInfo;
	boolean isHiddenInfowindow = false;

	// US Height
	String Us_height[] = new String[] { "2", "3", "4", "5", "6", "7" };
	String Us_height1[] = new String[] { "feet" };
	String Us_height2[] = new String[] { "0", "1", "2", "3", "4", "5", "6",
			"7", "8", "9", "10", "11" };
	String Us_height3[] = new String[] { "inch" };

	private boolean wheelScrolled = false;
	Context ctx;
	String height = "", weight = "", metric_height = "61 cm",
			us_height = "2 feet 0 inch", mGender = "MALE";

	private TextView spinnerheight, spinnerweight, manageaccnt;
	private RadioButton radiobtnmetric, radiobtnUS, radio_btn_male,
			radio_btn_female;
	ProgressDialog progressDialog;
	public static Pattern alphanumeric = Pattern.compile("[A-Za-z0-9]+");
	DataBase data;
	AndMedical_App_Global app_global;

	LinearLayout myLayout;
	LinearLayout hiddenLayout;
	RadioGroup radiogrp_sex;
	TextView continueBtn;
	String user_lastname = "", user_firstname = "", user_emailid = "",
			user_password = "", user_defaultheight = "",
			user_defaultweight = "", user_dob = "", auth_token = "",
			userID = "", timestampVal = "";
	private int weightPosition1 = 0, weightPosition2 = 0, heightPosition1 = 0,
			heightPosition2 = 0;
	private int heightPositionMetric = 0, HeightFeetPosition = 0,
			HeightInchPosition = 0;
	SharedPreferences.Editor meEditor;
	int final_metric_height, final_us_height;
	String groupName = "";
	long timestamp;
	String userheightUnit = "", userGender = "", userDob = "";
	RadioGroup radio_grp_height_weight;
	String[] dateofBirthYear;
	ANDMedicalUtilities andMedicalUtils;
	String metricValue = "60", feetValue = "2", inchValue = "0";

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		prefs = getSharedPreferences("ANDMEDICAL", MODE_PRIVATE);
		userType = prefs.getString("login_username", "");
		userPassword = prefs.getString("login_password", "");
		data = new DataBase(getApplicationContext(), userType);
		andMedicalUtils = new ANDMedicalUtilities();
		context = ManageSection.this;
		auth_token = prefs.getString("auth_token", "");
		userID = prefs.getString("userId", "");

		app_global = (AndMedical_App_Global) getApplication();
		if (!userType.equalsIgnoreCase("Guest")) {
			user_lastname = app_global.getLastName();
			user_firstname = app_global.getFirstName();
			user_emailid = app_global.getEmailAddress();
			// user_password = app_global.getPassword();
			user_defaultheight = app_global.getDefaultHeight();
			user_dob = app_global.getDateOfBirth();
			userheightUnit = app_global.getHeightUnit();
			userGender = app_global.getGender();
			timestampVal = app_global.getDateOfBirth();
			if (user_dob != null) {
				readingTakenTimeValues = ANDMedicalUtilities
						.convertmstodate(Long.parseLong(user_dob)); // 1989-04-01T01:27
			}
			if (userheightUnit.equalsIgnoreCase("cm")) {
				final_metric_height = Integer.valueOf(user_defaultheight);
			} else {
				final_us_height = Integer.valueOf(user_defaultheight);

			}

		} else {

			RegistrationInfoBean infobean = data
					.getUserDetailAccount("guest@gmail.com");
			if (infobean != null) {
				user_firstname = infobean.getUserName();
				user_defaultheight = infobean.getUserHeight();
				user_dob = infobean.getUserDateBirth();
				userheightUnit = infobean.getUserHeightunit();
				userGender = infobean.getUserSex();
				timestampVal = infobean.getUserDateBirth();

			}

		}

		manageaccnt = (TextView) findViewById(R.id.header);
		slidemenu = (SlideMenu) findViewById(R.id.slideMenu);


		radio_btn_male.setChecked(true);
		String unitType = prefs.getString("measuringUnit", "US");
		meEditor = prefs.edit();
		if (userType.equalsIgnoreCase("Guest")) {
			lastname.setEnabled(false);

		}

		try {
			String dateOfBirth[] = readingTakenTimeValues.split("T");
			dateofBirthYear = dateOfBirth[0].split("-");
			@SuppressWarnings("deprecation")
			DateMidnight start = new DateMidnight(dateofBirthYear[0] + "-"
					+ dateofBirthYear[1] + "-" + dateofBirthYear[2]);
			mYear = Integer.valueOf(dateofBirthYear[0]);
			mMonth = Integer.valueOf(dateofBirthYear[1]);
			mDay = Integer.valueOf(dateofBirthYear[2]);
			DateMidnight end = new DateMidnight(new Date());

			int year1 = Years.yearsBetween(start, end).getYears();
			user_dob = String.valueOf(year1);
		} catch (Exception e) {
			user_dob = app_global.getDateOfBirth();
		}

		if (userGender.equalsIgnoreCase("MALE")) {
			radiogrp_sex.check(radio_btn_male.getId());
		} else {
			radiogrp_sex.check(radio_btn_female.getId());

		}

		if (!userType.equalsIgnoreCase("Guest")) {
			if (user_firstname != null) {
				firstname.setText(ANDMedicalUtilities
						.capitalLizeFirstLetter(user_firstname));
			}
			if (lastname != null) {

				lastname.setText(ANDMedicalUtilities
						.capitalLizeFirstLetter(user_lastname));
			}
			emailid.setText(user_emailid);
			password.setText(userPassword);
			confirmpassword.setText(userPassword);
			spinnerheight.setText(user_defaultheight);
			spinnerweight.setText("0");

			// txtdate.setText(user_dob);
		} else {
			firstname.setText(userType);
			emailid.setEnabled(false);
			password.setEnabled(false);
			confirmpassword.setEnabled(false);
		}
		slidemenu.setVisibility(View.VISIBLE);
		String addnewUserVisiblity = prefs.getString("addnewuservisiblity", "");
		String manageuservisibility = prefs.getString("manageuservisibility",
				"");
		String frommanagevisibility = prefs.getString("frommanagevisibility",
				"");

		slidemenu.init(this, this, 333, username, userType,
				addnewUserVisiblity, manageuservisibility,
				frommanagevisibility);

		slidemenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				slidemenu.show();
			}
		});

		myLayout.addView(hiddenInfo);

		ctx = ManageSection.this;
		
		if (userheightUnit.equalsIgnoreCase("in")) {
			radio_grp_height_weight.check(radiobtnUS.getId());
			String heightCalcValue = user_defaultheight;
			try {
				int _height = Integer.parseInt(heightCalcValue);

				int feet = (int) (Math.round(_height / 12));
				int inches = (int) (Math.round(_height % 12));
				spinnerheight
						.setText(feet + " feet" + " " + (inches + " inch"));
			} catch (Exception e) {
			}

		} else {
			radio_grp_height_weight.check(radiobtnmetric.getId());

			try {
				heightPositionMetric = Integer.valueOf(user_defaultheight) - 60;
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				heightPositionMetric = 0;
			}

		}

		radiobtnmetric.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int heightpostion = 0;
				meEditor.putString("measuringUnit", "metric");
				meEditor.commit();
				String heightTextValue = spinnerheight.getText().toString();
				if (heightTextValue != null && heightTextValue.length() > 0) {
					if (heightTextValue.contains("feet")) {
						String heightCalcValue = heightTextValue
								.replace("feet", ".").replace("inch", "")
								.replace(" ", "");

						int feet = Integer.parseInt((heightCalcValue
								.split("\\."))[0]);
						int inch1 = Integer.parseInt((heightCalcValue
								.split("\\."))[1]);
						double cm2 = (double) ((feet * 12) + inch1);
						int total = (int) Math.round((cm2 * 2.54));
						heightpostion = total;
						spinnerheight.setText(total + " cm");
						final_metric_height = total;

					}
				}
				// initWheelMetricDistance(hiddenInfo, R.id.p3, wheelMenu3,
				// );

			}
		});

		radiobtnUS.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				meEditor.putString("measuringUnit", "US");
				meEditor.commit();
				String heightTextValue = spinnerheight.getText().toString();

				String heightCalcValue = heightTextValue
						.replace(" meters ", ".").replace(" cm", "")
						.replace(" ", "");
				try {
					int _height = Integer.parseInt(heightCalcValue);

					int feet = (int) (Math.round(_height / 30.48));
					int inches = (int) (((_height / 2.54)) - ((int) (feet * 12)));
					spinnerheight.setText(feet + " feet" + " "
							+ (inches + " inch"));
					final_us_height = (Integer.parseInt(feetValue) * 12)
							+ Integer.parseInt(inchValue);
				} catch (Exception e) {
				}
				// }

			}
		});


		// Display present date
		linearDate.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID);
			}
		});
	}

	protected Dialog onCreateDialog(int id) {

		if (id == DATE_DIALOG_ID) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date d = null;
			try {
				d = sdf.parse("01/01/1900");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DatePickerDialog date = new DatePickerDialog(this,
					mDateSetListener, mYear, mMonth, mDay) {

				@Override
				public void onDateChanged(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					Log.v("", "");
				}

			};

			date.getDatePicker().setMinDate(d.getTime());
			return date;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPrepareDialog(int, android.app.Dialog)
	 */
	protected void onPrepareDialog(int id, Dialog dialog) {

		if (id == DATE_DIALOG_ID) {
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
		}
	}

	/*
	 * Method for Updating Display for all the Text used Wheel
	 */
	private void updateDisplay() {
		@SuppressWarnings("deprecation")
		DateMidnight start = new DateMidnight(mYear + "-" + mMonth + "-" + mDay);
		@SuppressWarnings("deprecation")
		DateMidnight end = new DateMidnight(new Date());

		int year1 = Years.yearsBetween(start, end).getYears();

		SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm:s.SSSZ");
		String strDate = timeformat.format(myCalendar.getTime().getTime());
		finalDate = mYear + "-" + mMonth + "-" + mDay + "T" + strDate;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v("Resume Called ..", "Resume..");
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			diffYears = ANDmedicalCommons.getYears2(year, monthOfYear,
					dayOfMonth);
			// myCalendar.set(Calendar.YEAR, year);
			// myCalendar.set(Calendar.MONTH, monthOfYear);
			// myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			mYear = year;
			mMonth = monthOfYear + 1;
			mDay = dayOfMonth;
			updateDisplay();
		}

	};

	/*
	 * My On Item Selected Listener
	 */
	public class MyOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			final_height = arg0.getItemAtPosition(arg2).toString();
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}

	/*
	 * Asysnc task for Create Account Registration
	 */

	/*
	 * Method for Parsing Json Data to Post in Webservice
	 */
	private JSONObject getConvertedinJson(String fname, String lname,
			int final_metric_height2, String email, String pwd, long timestamp,
			String timezone, String heightUnit) {

		JSONObject object = new JSONObject();
		try {

			object.put("userId", userID);
			object.put("firstName", fname);
			object.put("lastName", lname);
			object.put("defaultHeight", final_metric_height2);
			object.put("heightUnit", heightUnit);
			object.put("emailAddress", email);
			object.put("password", pwd);
			object.put("dateOfBirth", timestamp);
			object.put("gender", mGender);
			object.put("timezone", timezone);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object;
	}

	private void inFo(String str) {
		String help = str;
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ManageSection.this);
		builder.setMessage(help).setCancelable(true);
		builder.setTitle("Info");
		builder.setIcon(R.drawable.app_icon);
		builder.setCancelable(true);
		builder.setIcon(null);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}

		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/*
	 * Method for Password and confirm Password Match
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if (myLayout.getVisibility() == View.VISIBLE) {
			myLayout.setVisibility(View.GONE);
		} else {
			finish();
		}
	}

	@Override
	public SlideMenu getSlideMenu() {
		return slidemenu;
	}

	/*
	 * Method for Calling Login Webservice
	 */

}
