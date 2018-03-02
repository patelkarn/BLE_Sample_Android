package jp.co.aandd.bleSimpleApp.base;

import jp.co.aandd.bleSimpleApp.entities.Lifetrack_infobean;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


public class ADDisplayDataLinearLayout extends LinearLayout {

	protected SharedPreferences prefs;
	
	public ADDisplayDataLinearLayout(Context context) {
		super(context);
	}
	
	public ADDisplayDataLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ADDisplayDataLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	protected void init() {
		prefs = getContext().getSharedPreferences("ANDMEDICAL",  Context.MODE_PRIVATE);
	}
	
	public void setData(Lifetrack_infobean data) {
		init();
	}
	
	public void setHide(boolean isHide) {
		this.setVisibility( (isHide)? View.GONE : View.VISIBLE );
	}
}
