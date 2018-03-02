package jp.co.aandd.bleSimpleApp.utilities;

import android.content.Context;

import java.text.DateFormat;
import java.util.Date;

public class ADDateParser {

	/**
	 * yyyy/mm/dd のフォーマットされた文字列を返します
	 * @param context
	 * @param date
	 * @return yyyy/mm/ddにフォーマットされた文字列
	 */
	public static String parseDate(Context context, Date date) {
		String result;
		DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
		result = dateFormat.format(date);
		return result;
	}

	/**
	 * yyyy/mm/dd のフォーマットされた文字列を返します
	 * @param context
	 * @param time
	 * @return dateString
	 */
	public static String parseDate(Context context, long time) {
		Date date = new Date(time);
		return parseDate(context, date);
	}

	/**
	 * Dec. 31, 1999のように月を省略した文字列を返します。
	 * 日本のロケールで使用した場合はparseDate()と同じ結果を返します
	 * @param context
	 * @param date
	 * @return dateString
	 */
	public static String parseMediumDate(Context context, Date date) {
		String result;
		DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(context);
				
		result = dateFormat.format(date);
		return result;
	}

	/**
	 * Dec. 31, 1999のように月を省略した文字列を返します。
	 * 日本のロケールで使用した場合はparseDate()と同じ結果を返します
	 * @param context
	 * @param time
	 * @return dateString
	 */
	public static String parseMediumDate(Context context, long time) {
		Date date = new Date(time);
		return parseMediumDate(context, date);
	}

	/**
	 * 2015年02月25日というような結果を返します。
	 * 日本以外のロケールで実行した場合は、
	 * February 25,2015というような結果を返します。
	 * @param context
	 * @param date
	 * @return
	 */
	public static String parseLongDate(Context context, Date date) {
		String result;
		DateFormat dateFormat = android.text.format.DateFormat.getLongDateFormat(context);
		result = dateFormat.format(date);
		return result;
	}

	/**
	 * 2015年02月25日というような結果を返します。
	 * 日本以外のロケールで実行した場合は、
	 * February 25,2015というような結果を返します。
	 * @param context
	 * @param time
	 * @return
	 */
	public static String parseLongDate(Context context, long time) {
		Date date = new Date(time);
		return parseLongDate(context, date);
	}

	/**
	 * 20:00または 08:00午後 というような結果を返します。
	 * 「設定」→「日付と時刻」→「24時間表記」のチェックが入っていない場合、午前・午後表記が付きます。そうでない場合は24時間表記になります。
	 * @param context
	 * @param date
	 * @return
	 */
	public static String parseTime(Context context, Date date) {
		String result;
		DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(context);
		result = dateFormat.format(date);
		return result;
	}

	/**
	 * 20:00または 08:00午後 というような結果を返します。
	 * 「設定」→「日付と時刻」→「24時間表記」のチェックが入っていない場合、午前・午後表記が付きます。そうでない場合は24時間表記になります。
	 * @param context
	 * @param date
	 * @return
	 */
	public static String parseTime(Context context, long time) {
		Date date = new Date(time);
		return parseTime(context, date);
	}

}
