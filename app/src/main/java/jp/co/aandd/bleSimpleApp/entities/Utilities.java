package jp.co.aandd.bleSimpleApp.entities;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


public class Utilities {
	static ProgressDialog pb;

	public static void dissmisDialog(Context mContext) {
		pb.dismiss();
	}

	public static void showToast(Context context, String message, int duration) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

	}

	public static boolean isOnline(Context context) {
		boolean flag;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	public static int getDayOfWeekOfDate(String specific_date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(Date.valueOf(specific_date));
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	public static String getStringDayFromIntDay(int day) {
		switch (day) {
		case 1:
			return "Sun";
		case 2:
			return "Mon";
		case 3:
			return "Tue";
		case 4:
			return "Wed";
		case 5:
			return "Thu";
		case 6:
			return "Fri";
		case 7:
			return "Sat";
		}

		return null;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getPreviousDateOfGivenDate(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(date));
			calendar.add(Calendar.DATE, -1);
			// System.out.println("Utilities.getPreviousDateOfGivenDate()......sdf.format(calendar.getTime()) : "
			// + sdf.format(calendar.getTime()));
			return sdf.format(calendar.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static String timestampconvertor(Long input) {
		Date date = new Date(input);
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return (cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.MONTH) + "/"
				+ cal.get(Calendar.DATE) + " " + cal.get(Calendar.HOUR) + ":"
				+ cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND) + (cal
					.get(Calendar.AM_PM) == 0 ? "AM" : "PM"));
	}

}
