package jp.co.aandd.bleSimpleApp.utilities;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.aandd.bleSimpleApp.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class ANDMedicalUtilities {
	
	//** is Application StandAlone Mode */
	public static final boolean APP_STAND_ALONE_MODE = true;
	
	public static boolean isLanguageJa() {
		return Locale.JAPANESE.toString().equalsIgnoreCase(Locale.getDefault().getLanguage());
	}

	public synchronized static String FormatDashboardDispDate(String dateString) {
		String formattedDateDisplay = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String format = "";
		if(isLanguageJa()) {
			format = "yyyy/MM/dd HH:mm";
		}
		else {
			format = "dd/MM/yyyy HH:mm";
		}
		
		SimpleDateFormat sdf2 = new SimpleDateFormat(format);
		formattedDateDisplay = sdf2.format(date);
		
		return formattedDateDisplay;
	}

	public synchronized static String FormatDashboardDispDate(Context context, String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return ADDateParser.parseMediumDate(context, date) +" "+ ADDateParser.parseTime(context, date);
	}



	public synchronized static String FormatGraphDayTitle(String dateString) {
		String formattedDateDisplay = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String format = "";
		if(isLanguageJa()) {
			format = "yyyy/MM/dd";
		}
		else {
			format = "dd/MM/yyyy";
		}
		
		SimpleDateFormat sdf2 = new SimpleDateFormat(format);
		formattedDateDisplay = sdf2.format(date);
		
		return formattedDateDisplay;
	}

	public synchronized static String FormatGraphDayTitle(Context context, String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return ADDateParser.parseMediumDate(context, date);
	}



	public synchronized static String FormatGraphWeekTitle(String dateString) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		calendar.setTime(date);
		calendar.clear(Calendar.HOUR);
		calendar.clear(Calendar.MINUTE);
		calendar.clear(Calendar.SECOND);
		calendar.clear(Calendar.MILLISECOND);
		
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - dayOfWeek);
		Date firstDate = calendar.getTime();
		
		calendar.add(Calendar.WEEK_OF_MONTH, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		Date endDate = calendar.getTime();
		
		String format = "";
		if(isLanguageJa()) {
			format = "yyyy/MM/dd";
		}
		else {
			format = "dd/MM/yyyy";
		}
		
		SimpleDateFormat sdf2 = new SimpleDateFormat(format);
		
		StringBuilder builder = new StringBuilder("");
		builder.append(sdf2.format(firstDate));
		builder.append(" - ");
		builder.append(sdf2.format(endDate));
		
		return builder.toString();
	}

	public synchronized static String FormatGraphWeekTitle(Context context, String dateString) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		calendar.setTime(date);
		calendar.clear(Calendar.HOUR);
		calendar.clear(Calendar.MINUTE);
		calendar.clear(Calendar.SECOND);
		calendar.clear(Calendar.MILLISECOND);
		
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - dayOfWeek);
		Date firstDate = calendar.getTime();
		
		calendar.add(Calendar.WEEK_OF_MONTH, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		Date endDate = calendar.getTime();
		
		StringBuilder builder = new StringBuilder("");
		builder.append(ADDateParser.parseMediumDate(context, firstDate));
		builder.append(" - ");
		builder.append(ADDateParser.parseMediumDate(context, endDate));
		
		return builder.toString();
	}



	public synchronized static String FormatGraphMonthTitle(String dateString) {
		String formattedDateDisplay = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String format = "";
		if(isLanguageJa()) {
			format = "yyyy/MM";
		}
		else {
			format = "MMM, yyyy";
		}
		
		SimpleDateFormat sdf2 = new SimpleDateFormat(format);
		formattedDateDisplay = sdf2.format(date);
		
		return formattedDateDisplay;
	}



	public synchronized static String FormatDispdateAdapter(String date) {
		String formattedDateDisplay = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		Date dateFormat = null;
		try {
			dateFormat = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String format = "";
		if(isLanguageJa()) {
			format = "yyyy/MM/dd'T'HH:mm";
		}
		else {
			format = "yyyy/MM/dd'T'HH:mm";
		}
		
		SimpleDateFormat sdf2 = new SimpleDateFormat(format);
		formattedDateDisplay = sdf2.format(dateFormat);

		String[] dateTimeArray = formattedDateDisplay.split("T");

		return dateTimeArray[0] + "\n" + dateTimeArray[1];
	}

	public synchronized static String FormatDispdateAdapter(Context context, String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ADDateParser.parseMediumDate(context, date) + "\n" + ADDateParser.parseTime(context, date);
	}



	public synchronized static String formatDisplayDate(String date) {
		String formattedDateDisplay = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		Date dateFormat = null;
		try {
			dateFormat = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy'T'hh:mm a");
		formattedDateDisplay = sdf2.format(dateFormat);

		String[] dateTimeArray = formattedDateDisplay.split("T");

		return dateTimeArray[0] + " " + dateTimeArray[1];
	}

	public synchronized static String formatDisplayActivityDate(String date) {
		String formattedDateDisplay = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dateFormat = null;
		try {
			dateFormat = sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
		formattedDateDisplay = sdf2.format(dateFormat);

		String[] dateTimeArray = formattedDateDisplay.split("T");

		return formattedDateDisplay;
	}

	/*
	 * TO DO may be remove in future
	 */
	public synchronized static String formatDispdateAdapter(String date) {
		String formattedDateDisplay = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		Date dateFormat = null;
		try {
			dateFormat = sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy'T'hh:mm a");
		formattedDateDisplay = sdf2.format(dateFormat);

		String[] dateTimeArray = formattedDateDisplay.split("T");

		return dateTimeArray[0] + " " + dateTimeArray[1];
	}

	public static long convertDateintoMs(String date) {
		long final_birth_date_timestamp = 0;
		DateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
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

	public static String convertmstodate(long timeStamp) {
		String result;
		Timestamp timestamp = new Timestamp(timeStamp);
		Date date = new Date(timestamp.getTime());

		// S is the millisecond
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm");
		result = simpleDateFormat.format(timestamp);
		// System.out.println(simpleDateFormat.format(timestamp));
		// System.out.println(simpleDateFormat.format(date));

		return result;
	}

	public static AlertDialog CreateDialog(final Context context, String Message, final Activity activity) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set title
		alertDialogBuilder.setTitle(Message);

		// set dialog message
		alertDialogBuilder
				.setMessage(R.string.dialog_to_exit)
				.setCancelable(false)
				.setPositiveButton(R.string.text_yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent homeIntent = new Intent(
										Intent.ACTION_MAIN);
								homeIntent.addCategory(Intent.CATEGORY_HOME);
								// homeIntent
								// .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								// );
								context.startActivity(homeIntent);
								activity.finish();

							}
						})
				.setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
		return alertDialog;
	}

	/*
	 * Method for Email Validation
	 */

	public boolean isEmailValid1(String email) {
		String regExpn = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
				+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
				+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";

		CharSequence inputStr = email;
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(inputStr);

		if (matcher.matches())
			return true;
		else
			return false;
	}

	/*
	 * Method for Validating Email
	 */
	public static boolean isEmailValid(String s) {
		String regExpn = "^(?=.*[A-Z])(?=.*[0-9]).{7,}$";
		CharSequence inputStr = s;
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile(regExpn);
		matcher = pattern.matcher(inputStr);

		if (matcher.matches())
			return true;
		else
			return false;
	}

	/*
	 * Method for Checking Internet Connection
	 */
	public boolean isConnectingToInternet(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	public boolean regExpression(String PASSWORD_PATTERN, String Password) {
		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		Matcher matcher = pattern.matcher(Password);

		return matcher.matches();

	}

	public static String capitalLizeFirstLetter(String source) {
		StringBuffer res = new StringBuffer();

		if (source != null) {

			String[] strArr = source.split(" ");
			for (String str : strArr) {
				char[] stringArray = str.trim().toCharArray();
				stringArray[0] = Character.toUpperCase(stringArray[0]);
				str = new String(stringArray);

				res.append(str).append(" ");
			}
		}
		return res.toString().trim();
	}
}
