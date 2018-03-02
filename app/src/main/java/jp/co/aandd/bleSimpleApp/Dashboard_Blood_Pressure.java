package jp.co.aandd.bleSimpleApp;

import java.util.ArrayList;

import jp.co.aandd.bleSimpleApp.entities.DataBase;
import jp.co.aandd.bleSimpleApp.entities.Lifetrack_infobean;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/*
 * Blood Pressure Dashboard Activity Class
 */
public class Dashboard_Blood_Pressure extends Activity {
	ListView listview_blood_pressure_history;
	TextView dashboard_systolic, dashboard_diastolic, dashboard_pulse_rate;
	Context contextObj;
	ImageView btn_activity, btn_history;
	LinearLayout li_image, ll_blood_pressure_history;
	DataBase db;
	ArrayList<Lifetrack_infobean> data_bp_from_db;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		contextObj = Dashboard_Blood_Pressure.this;
		li_image.setVisibility(View.VISIBLE);

		SharedPreferences prefs = getSharedPreferences("ANDMEDICAL",
				MODE_PRIVATE);
		String login_username = prefs.getString("login_username", "");
		db = new DataBase(this, login_username);
		data_bp_from_db = new ArrayList<Lifetrack_infobean>();

		data_bp_from_db = db.getbpDetails();
		Lifetrack_infobean infobeanObj = data_bp_from_db.get(0);
}
}
