package jp.co.aandd.bleSimpleApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.slidemenu.SlideMenu;
import com.slidemenu.SlideMenuInterface.OnSlideMenuItemClickListener;

public abstract class ADBaseActivity extends Activity implements
		OnSlideMenuItemClickListener {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onSlideMenuItemClick(int itemId) {
		switch (itemId) {
		case 2:
			sliderScreens(2, this);
			break;
		case 3:
			sliderScreens(3, this);
			break;
		case 8:
			sliderScreens(8, this);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.slidemenu.SlideMenuInterface.OnSlideMenuItemClickListener#onAccount()
	 */
	public void onAccount() {
		finish();
	}

	/*
	 * SliderScreens for Calling the Activity from Slider for each case
	 */

	private void sliderScreens(int screenNumber, Activity act) {
		Intent intent = null;
		switch (screenNumber) {
		case 2: // ダッシュボード
			if (act.getClass() != DashboardActivity.class) {
				intent = new Intent(act, DashboardActivity.class);
				act.startActivity(intent);
				finish();
			} else {
				((DashboardActivity)act).getSlideMenu().hide();
			}
			break;
		case 3: //機器設定
			if (act.getClass() != DeviceSetUpActivity.class) {
				intent = new Intent(act, DeviceSetUpActivity.class);
				act.startActivity(intent);
				finish();
			} else {
				((DeviceSetUpActivity)act).getSlideMenu().hide();
			}
			break;
		}

	}

	abstract public SlideMenu getSlideMenu();
}
