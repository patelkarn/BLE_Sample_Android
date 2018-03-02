package jp.co.aandd.bleSimpleApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import jp.co.aandd.bleSimpleApp.entities.DataBase;
import jp.co.aandd.bleSimpleApp.entities.GroupInfoBean;
import jp.co.aandd.bleSimpleApp.entities.Lifetrack_infobean;
import jp.co.aandd.bleSimpleApp.utilities.ANDMedicalUtilities;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/*
 * Class for Activity Monitor History and Activity Monitor Value Tabs
 */
public class DashboardActivity1 extends Activity {
	ArrayList<Lifetrack_infobean> lstMoveReadingAM;
	String yourDate = "";
	ImageView backbtn, logo, btn_activity, btn_history, left_arrow,
			right_arrow;
	TextView header, txt_distance, txt_sleep, txt_bpm, txt_cal,
			steps_dashboard_1, tv_goal_Value, txt_date, tv_goal_distance_Value,
			tv_distance_unit;
	LinearLayout li_activity, li_history, li_image1, ll_calories_graph,
			ll_heart_graph, ll_distance_graph, ll_sleep_graph, ll_cancel;
	Bundle extra;
	Float f;
	DataBase db, dbhandlerobj;
	int j = 1;
	String unitType, date_activity, display_calories, login_email;
	String UserName;
	ListView listview_life_track_history, list_users_history;
	// ArrayList<Lifetrack_infobean> lstHistory;
	Context contextObj;
	History_Adapter adapter;
	ArrayList<Lifetrack_infobean> datafrom_db;
	ArrayList<String> userLst = new ArrayList<String>();
	int position = 0;
	Calendar c;
	Lifetrack_infobean infobeanObj;
	ArrayAdapter<String> adap_list_users;
	private Lifetrack_infobean lt_info;
	String deviceID, dateTimeStamp, auth_token, GroupName;
	private int activityLeft = 0;
	Dialog dialog;
	RelativeLayout main_rl;
	ArrayList<GroupInfoBean> Lstgroupdata;
	String mUsername, mPassword;
	int movePos;
	String dashboarPosition = "activity";

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		c = Calendar.getInstance();
		lstMoveReadingAM = new ArrayList<Lifetrack_infobean>();
		lt_info = new Lifetrack_infobean();
		extra = getIntent().getExtras();

		SharedPreferences prefs = getSharedPreferences("ANDMEDICAL",
				MODE_PRIVATE);
		String login_username = prefs.getString("login_username", "");
		auth_token = prefs.getString("auth_token", "");
		db = new DataBase(this, login_username);
		unitType = prefs.getString("measuringUnit", "US");
		position = extra.getInt("position");
		date_activity = extra.getString("activitydate");

		contextObj = DashboardActivity1.this;
		datafrom_db = new ArrayList<Lifetrack_infobean>();
		datafrom_db = LifeTrackSorting(db.getAllActivityDetails());
		infobeanObj = datafrom_db.get(position);
		txt_date.setText(date_activity);

		li_image1.setVisibility(View.VISIBLE);
		main_rl = (RelativeLayout) findViewById(R.id.main_rl);
		backbtn.setVisibility(View.GONE);
		backbtn.setBackgroundResource(R.drawable.left_top_arrow);

		// Move Reading POPup list

		Lstgroupdata = new ArrayList<GroupInfoBean>();
		GroupName = prefs.getString("groupName", "");
		login_email = prefs.getString("login_email", "");
		dbhandlerobj = new DataBase(this, "Allaccount.db");

		Lstgroupdata = dbhandlerobj.getUserbyGroup(GroupName);
		if (Lstgroupdata.size() != 0) {
			for (int i = 0; i < Lstgroupdata.size(); i++) {
				if (Lstgroupdata.get(i).getEmail()
						.equalsIgnoreCase(login_email)) {
					Lstgroupdata.remove(i);

				}
			}

		}
		for (GroupInfoBean username : Lstgroupdata) {
			UserName = username.getUsername();
			userLst.add(UserName);
		}
		Calendar cTime = Calendar.getInstance();
		int mYear = cTime.get(Calendar.YEAR);
		int mMonth = cTime.get(Calendar.MONTH) + 1;
		int mDay = cTime.get(Calendar.DAY_OF_MONTH);
		yourDate = mYear + "-" + pad(mMonth) + "-" + pad(mDay);

		dialog = new Dialog(DashboardActivity1.this);
		dialog.requestWindowFeature(dialog.getWindow().FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		backbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		adap_list_users = new ArrayAdapter<String>(DashboardActivity1.this,
				R.layout.cutom_list_historymange, R.id.tvCustomList, userLst);

		list_users_history.setAdapter(adap_list_users);

		left_arrow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				right_arrow.setVisibility(View.VISIBLE);
				activityLeft++;

				setUI(datafrom_db.get(activityLeft));
				if (activityLeft == datafrom_db.size() - 1) {
					left_arrow.setVisibility(View.INVISIBLE);
				}
			}
		});
		right_arrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				left_arrow.setVisibility(View.VISIBLE);
				activityLeft--;

				setUI(datafrom_db.get(activityLeft));
				if (activityLeft == 0) {
					right_arrow.setVisibility(View.INVISIBLE);
				}
			}
		});

		steps_dashboard_1.setText(infobeanObj.getSteps());

		// Distance value
		if (unitType.equalsIgnoreCase("US")) {
			Double va = Double.valueOf(infobeanObj.getDistance()) * 0.621371;
			String display_distance = String.format("%.2f", va);
			String styledText = display_distance
					+ "<font size='5' color='#ad63b0'>" + "mile" + "</font>";
			txt_distance.setText(Html.fromHtml(styledText),
					TextView.BufferType.SPANNABLE);
			tv_distance_unit.setText("miles");

		} else {
			String styledText = infobeanObj.getDistance()
					+ "<font size='10' color='#ad63b0'>" + "km" + "</font>";
			txt_distance.setText(Html.fromHtml(styledText),
					TextView.BufferType.SPANNABLE);
			tv_distance_unit.setText("km");

		}

		// sleep values
		if (infobeanObj.getSleep() != null) {

			String total_hr = "", styledText = "";
			int t = Integer.parseInt(infobeanObj.getSleep());
			int hours = (int) Math.floor(t / 60);
			int minutes = t % 60;
			if (hours == 0) {
				styledText = "00:00<font color='#ad63b0'>" + " mins"
						+ "</font>";
			} else {
				total_hr = String.valueOf(hours) + ":"
						+ String.valueOf(minutes);
				styledText = total_hr + "<font color='#ad63b0'>" + " mins"
						+ "</font>";
			}
			txt_sleep.setText(Html.fromHtml(styledText),
					TextView.BufferType.SPANNABLE);

		} else {
			String styledText = "00:00<font color='#ad63b0'>" + " mins"
					+ "</font>";
			txt_sleep.setText(Html.fromHtml(styledText),
					TextView.BufferType.SPANNABLE);
		}

		// Calories value
		String styledText = infobeanObj.getCal() + "<font color='#ad63b0'>"
				+ " cal" + "</font>";
		txt_cal.setText(Html.fromHtml(styledText),
				TextView.BufferType.SPANNABLE);

		// Goal values
		header.setText("Activity Monitor");

		// Heart rate value
		styledText = infobeanObj.getHeartRate() + "<font color='#ad63b0'>"
				+ " bpm" + "</font>";
		txt_bpm.setText(Html.fromHtml(styledText),
				TextView.BufferType.SPANNABLE);



		// Method for Circular Progress Bar Animation
	}

	public void saveBitmap(Bitmap bitmap) {
		String filePath = Environment.getExternalStorageDirectory()
				+ File.separator + "Pictures/screenshot.png";
		File imagePath = new File(filePath);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(imagePath);
			bitmap.compress(CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
			sendMail(filePath);
		} catch (FileNotFoundException e) {
			Log.e("GREC", e.getMessage(), e);
		} catch (IOException e) {
			Log.e("GREC", e.getMessage(), e);
		}
	}

	public void sendMail(String path) {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				new String[] { "" });
		emailIntent
				.putExtra(android.content.Intent.EXTRA_SUBJECT, "ScreenShot of Activity Dashboard");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Please find the attached file");
		emailIntent.setType("image/png");
		Uri myUri = Uri.parse("file://" + path);
		emailIntent.putExtra(Intent.EXTRA_STREAM, myUri);
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}

	// Method to Set the Reading for Activity MOnitor
	private void setUI(Lifetrack_infobean ActivityReading) {

		steps_dashboard_1.setText(ActivityReading.getSteps());

		if (unitType.equalsIgnoreCase("US")) {
			Double va = Double.valueOf(ActivityReading.getDistance()) * 0.621371;
			String display_distance = String.format("%.2f", va);
			String styledText = display_distance
					+ "<font size='12' color='#ad63b0'>" + "mile" + "</font>";
			txt_distance.setText(Html.fromHtml(styledText),
					TextView.BufferType.SPANNABLE);
			tv_distance_unit.setText("miles");

		} else {
			String styledText = ActivityReading.getDistance()
					+ "<font size='12' color='#ad63b0'>" + "Km" + "</font>";
			txt_distance.setText(Html.fromHtml(styledText),
					TextView.BufferType.SPANNABLE);
			tv_distance_unit.setText("km");

		}

		// sleep values
		if (ActivityReading.getSleep() != null) {
			String total_hr = "", styledText = "";
			int t = Integer.parseInt(ActivityReading.getSleep());
			int hours = (int) Math.floor(t / 60);
			int minutes = t % 60;

			if (hours == 0) {
				styledText = "00:00<font color='#ad63b0'>" + " mins"
						+ "</font>";
			} else {
				total_hr = String.valueOf(hours) + ":"
						+ String.valueOf(minutes);
				styledText = total_hr + "<font color='#ad63b0'>" + " mins"
						+ "</font>";
			}

			txt_sleep.setText(Html.fromHtml(styledText),
					TextView.BufferType.SPANNABLE);

		} else {
			String styledText = "00:00<font color='#ad63b0'>" + " mins"
					+ "</font>";
			txt_sleep.setText(Html.fromHtml(styledText),
					TextView.BufferType.SPANNABLE);
		}

		// Calories value
		float calFloat = Float.valueOf(ActivityReading.getCal());

		calFloat = Math.round(calFloat);
		int calINT = (int) calFloat;
		display_calories = String.valueOf(calINT);

		String styledText = display_calories + "<font color='#ad63b0'>"
				+ " cal" + "</font>";
		txt_cal.setText(Html.fromHtml(styledText),
				TextView.BufferType.SPANNABLE);

		// Goal values
		header.setText("Activity Monitor");

		// Heart rate value
		styledText = ActivityReading.getHeartRate() + "<font color='#ad63b0'>"
				+ " bpm" + "</font>";
		txt_bpm.setText(Html.fromHtml(styledText),
				TextView.BufferType.SPANNABLE);
		if (yourDate.equals(ActivityReading.getDate())) {

			txt_date.setText(ANDMedicalUtilities
					.formatDisplayDate(ActivityReading.getDate() + "T"
							+ ActivityReading.getTime()));

		} else {

			txt_date.setText(ANDMedicalUtilities
					.formatDisplayActivityDate(ActivityReading.getDate()));
		}

	}

	// Customize Adapter Class for Showing Activity Monitor History
	public class History_Adapter extends BaseAdapter {
		int _position;

		@Override
		public int getCount()

		{
			return datafrom_db.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			Lifetrack_infobean infobeanObj = datafrom_db.get(position);
			LayoutInflater inf = (LayoutInflater) DashboardActivity1.this
					.getSystemService(DashboardActivity1.this.LAYOUT_INFLATER_SERVICE);

			_position = position;

			if (position % 2 == 0) {
				convertView.setBackgroundResource(R.color.lightgray);

			} else {
				convertView.setBackgroundResource(R.color.white);

			}

			ll_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();

				}
			});
			convertView.setTag(position);

			return convertView;

		}
	}

	// Method to Sort the Life Track Reading

	public ArrayList<Lifetrack_infobean> LifeTrackSorting(

	ArrayList<Lifetrack_infobean> lstReading) {

		if (lstReading != null && lstReading.size() > 0) {
			Collections.sort(lstReading, new Comparator<Lifetrack_infobean>() {
				@Override
				public int compare(Lifetrack_infobean lhs,
						Lifetrack_infobean rhs) {
					final SimpleDateFormat df = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm");
					String formattedDate = df.format(c.getTime());
					try {
						j = df.parse(rhs.getDate() + " " + rhs.getTime())
								.compareTo(
										df.parse(lhs.getDate() + " "
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

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

}
