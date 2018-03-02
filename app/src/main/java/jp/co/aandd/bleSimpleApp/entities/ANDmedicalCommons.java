package jp.co.aandd.bleSimpleApp.entities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.Period;

public class ANDmedicalCommons {


	public static long DateDifference(String dateStart , String dateStop) {
 
		//String dateStart = "01/14/2012 09:29:58";
		//String dateStop = "01/15/2012 10:31:48";
 
		//HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
 
		Date d1 = null;
		Date d2 = null;
		long diffDays=-1;
		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);
 
			//in milliseconds
			long diff = d1.getTime() - d2.getTime();
 
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			diffDays = diff / (24 * 60 * 60 * 1000);
 
		/*	Log.v("days",diffDays + " days, ");
			Log.v("Hours",diffHours + " hours, ");
			Log.v("Minutes",diffMinutes + " minutes, ");
			Log.v("Seconds",diffSeconds + " seconds.");*/
			
			
		} catch (Exception e) {
			e.printStackTrace();
			diffDays = -1;
		}
		return diffDays;
 
	}

	
	public static int getYears (int year, int month, int day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, noofyears;         

        y = cal.get(Calendar.YEAR);// current year ,
        m = cal.get(Calendar.MONTH);// current month 
        d = cal.get(Calendar.DAY_OF_MONTH);//current day
        cal.set(year, month, day);// here ur date 
        noofyears = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))|| ((m == cal.get(Calendar.MONTH)) && (d < cal.get(Calendar.DAY_OF_MONTH)))) {
                --noofyears;
        }
       try{
        if(noofyears < 0)
                throw new IllegalArgumentException("age < 0");
       }catch(IllegalArgumentException ile)
       {
    	   ile.printStackTrace();
       }
        System.out.println(noofyears);
        return noofyears;
	}
	
	public static int getYears2 (int year, int month, int day) {
		int years=0,months=0,days=0;
		
		String dateStart=year+"-"+month+"-"+day;
		String dateStop="";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		int diffYear=0;
		Date d1 = null;
		Date d2 = null;
		long diffDays=-1;
		try {
			d1 = format.parse(dateStart);

			 GregorianCalendar cal = new GregorianCalendar();
		        int y, m, d, noofyears;         

		        y = cal.get(Calendar.YEAR);// current year ,
		        m = cal.get(Calendar.MONTH);// current month 
		        d = cal.get(Calendar.DAY_OF_MONTH);//current day
		        dateStop=y+"-"+m+"-"+d;
			d2 = format.parse(dateStop);
 
			Calendar startCalendar = new GregorianCalendar();
			startCalendar.setTime(d1);
			Calendar endCalendar = new GregorianCalendar();
			endCalendar.setTime(d2);

			diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
			
			years=y-year;
			    months=month-m;
			    days=day-d;
			    
			    if (m < month || (month == m && d < day)) {
			    	years--;
			    }
			    DateTime myBirthDate = new DateTime(year, month, day, 0, 0, 0, 0);
			    DateTime now = new DateTime();
			    Period period = new Period(myBirthDate, now);
			    years = period.getYears();
			    System.out.println(years); 
			   
		} catch (Exception e) {
			e.printStackTrace();
			diffDays = -1;
		}
		  System.out.println(years);
	//int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		  
		 
	
	return years;
	}
}
