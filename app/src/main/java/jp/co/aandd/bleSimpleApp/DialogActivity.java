package jp.co.aandd.bleSimpleApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class DialogActivity extends Activity {
	
	public static String INTENT_KEY_TITLE = "com.andmedical.intent.key.title";
	public static String INTENT_KEY_MESSAGE = "com.andmedical.intent.key.message";
	public static int REQUEST_CODE = 200 ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		Intent intent = getIntent();
		if(intent == null) {
			finish();
		}
		
		Bundle bundle = intent.getExtras();
		String title = null;
		String message = null;
		if(bundle != null) {
			title = bundle.getString(INTENT_KEY_TITLE);
			message = bundle.getString(INTENT_KEY_MESSAGE);

			if(title ==null)title = "";
			if(message == null)message = "";
		}

	}
}