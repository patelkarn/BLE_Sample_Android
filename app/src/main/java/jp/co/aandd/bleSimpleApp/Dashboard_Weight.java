package jp.co.aandd.bleSimpleApp;

import java.util.ArrayList;

import jp.co.aandd.bleSimpleApp.entities.DataBase;
import jp.co.aandd.bleSimpleApp.entities.Lifetrack_infobean;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/*
 * Dashboard Weight Class
 */
public class Dashboard_Weight extends Activity {
	ListView listview_weight_history;
	TextView dashboard_weight;
	Context contextObj;
	ImageView btn_activity, btn_history;
	LinearLayout li_image, ll_weight_history;
	Weight_History_Adapter adapter;
	DataBase db;
	ArrayList<Lifetrack_infobean> data_weight_from_db;

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


		contextObj = Dashboard_Weight.this;
		SharedPreferences prefs = getSharedPreferences("ANDMEDICAL",
				MODE_PRIVATE);
		String login_username = prefs.getString("login_username", "");
		db = new DataBase(this, login_username);
		data_weight_from_db = new ArrayList<Lifetrack_infobean>();

		data_weight_from_db = db.getAllWeightDetails();
		Lifetrack_infobean infobeanObj = data_weight_from_db.get(0);
	}

	// Method for Customizing History Adapter for Weight History

	public class Weight_History_Adapter extends BaseAdapter {
		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount()

		{
			return data_weight_from_db.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int arg0) {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			Lifetrack_infobean infobeanObj = data_weight_from_db.get(position);
			LayoutInflater inf = (LayoutInflater) Dashboard_Weight.this
					.getSystemService(Dashboard_Weight.this.LAYOUT_INFLATER_SERVICE);


			if (position % 2 == 0) {
				convertView.setBackgroundResource(R.color.lightblue);

			} else {
				convertView.setBackgroundResource(R.color.white);

			}

			return convertView;

		}

	}
}
