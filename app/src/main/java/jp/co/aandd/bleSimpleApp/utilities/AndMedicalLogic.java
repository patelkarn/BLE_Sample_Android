package jp.co.aandd.bleSimpleApp.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AndMedicalLogic {

	// For BP
	// Having Same Value in All Blood Pressure Classes
	public static boolean checkBPError(String systolic, String diastolic,
			String pulse) {

		if (systolic.equalsIgnoreCase(diastolic)
				&& diastolic.equalsIgnoreCase(pulse)) {
			return true;

		}

		return false;

	}

	public  String convertTimeFormat(String Time) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

		String ddates = null;
		SimpleDateFormat Mdf3 = new SimpleDateFormat("hh:mm a",
				Locale.getDefault());
		Date tmpD;
		try {
			tmpD = formatter.parse(Time);
			ddates = Mdf3.format(tmpD);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ddates;
	}

}
