package jp.co.aandd.bleSimpleApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

/*
 * Splash Activity
 */
public class SplashActivity extends Activity {
	
	private Handler splashHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.and_splash);
		splashHandler = new Handler();
		splashHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				final Intent mainIntent = new Intent(SplashActivity.this,
						DashboardActivity.class);
				SplashActivity.this.startActivity(mainIntent);
				finish();
			}
		}, 3000);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		splashHandler.removeCallbacksAndMessages(null);
	}
}
