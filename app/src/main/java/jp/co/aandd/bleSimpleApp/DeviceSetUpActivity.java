package jp.co.aandd.bleSimpleApp;


import jp.co.aandd.bleSimpleApp.base.ADInstructionActivity;
import jp.co.aandd.bleSimpleApp.entities.AndMedical_App_Global;
import jp.co.aandd.bleSimpleApp.utilities.ADSharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slidemenu.SlideMenu;

public class DeviceSetUpActivity extends ADBaseActivity {

	private SlideMenu mSlideMenu;

	private LinearLayout mThermometerImageLayout;
	private LinearLayout mBloodPressureImageLayout;
	private LinearLayout mWeightScaleImageLayout;

	private TextView mHeader;

	private String mUserName = "";
	private String mUserType = "";

	AndMedical_App_Global app_global;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		android.util.Log.d("SN","DeviceSetup onCreate");

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.and_devicesetup);
		app_global = (AndMedical_App_Global) getApplication();
		mThermometerImageLayout = (LinearLayout) findViewById(R.id.thermometer);
//		mmThermometerImageLayout.setVisibility(View.GONE);
		mBloodPressureImageLayout = (LinearLayout) findViewById(R.id.blood_pressure_monitor);
		mWeightScaleImageLayout = (LinearLayout) findViewById(R.id.weight_scale);
		mHeader = (TextView) findViewById(R.id.header);
		mHeader.setText(getString(R.string.device_set_up));

		mUserType = ADSharedPreferences.getString(ADSharedPreferences.KEY_LOGIN_USER_NAME, "");

		// 血圧計
		mBloodPressureImageLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ADSharedPreferences.putString(ADSharedPreferences.KEY_DEVICE_SET_UP_MODE, ADSharedPreferences.VALUE_DEVICE_SET_UP_MODE_BP);
				// Connection_activity();
				Intent intent = new Intent(DeviceSetUpActivity.this, InstructionActivity.class);
				intent.putExtra(ADInstructionActivity.DEVICE_SCAN_MODE_KEY, ADInstructionActivity.DEVICE_SCAN_MODE_BP);
				startActivity(intent);
			}
		});

		// 体重計
		mWeightScaleImageLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ADSharedPreferences.putString(ADSharedPreferences.KEY_DEVICE_SET_UP_MODE, ADSharedPreferences.VALUE_DEVICE_SET_UP_MODE_WS);
				// Connection_activity();
				Intent intent = new Intent(DeviceSetUpActivity.this, InstructionActivity.class);
				// in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(ADInstructionActivity.DEVICE_SCAN_MODE_KEY, ADInstructionActivity.DEVICE_SCAN_MODE_WS);
				startActivity(intent);
			}
		});

		// スライドメニュー
		mSlideMenu = (SlideMenu) findViewById(R.id.slideMenu);
		String addnewUserVisiblity = ADSharedPreferences.getString(ADSharedPreferences.KEY_ADD_NEW_USER_VISIBLITY, "");
		String manageuservisibility = ADSharedPreferences.getString(ADSharedPreferences.KEY_MANAGER_USER_VISIBILITY, "");
		String frommanagevisibility = ADSharedPreferences.getString(ADSharedPreferences.KEY_FROM_MANAGER_VISIBILITY, "");
		mSlideMenu.init(this, this, 333, mUserName, mUserType, addnewUserVisiblity,
					manageuservisibility, frommanagevisibility);;

		mSlideMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSlideMenu.show();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		android.util.Log.d("SN","DeviceSetup onResume");
	}

	@Override
	protected void onStop() {
		super.onStop();
		android.util.Log.d("SN","DeviceSetup onStop");
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		android.util.Log.d("SN","DeviceSetup onPause");
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		android.util.Log.d("SN","DeviceSetup onDestroy");
	}

	@Override
	public SlideMenu getSlideMenu() {
		return mSlideMenu;
	}
}
