package jp.co.aandd.bleSimpleApp.view;

import jp.co.aandd.bleSimpleApp.R;
import jp.co.aandd.bleSimpleApp.base.ADDisplayDataLinearLayout;
import jp.co.aandd.bleSimpleApp.entities.AndMedical_App_Global;
import jp.co.aandd.bleSimpleApp.entities.DataBase;
import jp.co.aandd.bleSimpleApp.entities.Lifetrack_infobean;
import jp.co.aandd.bleSimpleApp.entities.RegistrationInfoBean;
import jp.co.aandd.bleSimpleApp.utilities.ADSharedPreferences;
import jp.co.aandd.bleSimpleApp.utilities.ANDMedicalUtilities;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class WeightScaleDisplayDataLayout extends ADDisplayDataLinearLayout {
	
	private TextView weight_scale_value;
	private TextView txt_weightScale_Date;
	private TextView weight_unit;
	private TextView bmi;
	private TextView bmi_unit;
	private Context mContext;

	public WeightScaleDisplayDataLayout(Context context) {
		super(context);
		mContext = context;
	}
	
	public WeightScaleDisplayDataLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public WeightScaleDisplayDataLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	@Override
	protected void init() {
		super.init();
		
		txt_weightScale_Date = (TextView) findViewById(R.id.tvWeigScalDate);

		LinearLayout weightLayout = (LinearLayout) findViewById(R.id.value_layout_weight);
		weight_scale_value = (TextView)weightLayout.findViewById(R.id.disp_data_ws_value_textview);
		weight_unit = (TextView)weightLayout.findViewById(R.id.disp_data_ws_unit_textview);
		
		LinearLayout bmiLayout = (LinearLayout) findViewById(R.id.value_layout_bmi);
		bmi = (TextView)bmiLayout.findViewById(R.id.disp_data_ws_value_textview);
		bmi_unit = (TextView)bmiLayout.findViewById(R.id.disp_data_ws_unit_textview);
		bmi_unit.setText("BMI");
	}

	@Override
	public void setData(Lifetrack_infobean data) {
		super.setData(data);
		
		AndMedical_App_Global appGlobal = (AndMedical_App_Global)getContext().getApplicationContext();
		String weightString = data.getWeight();
		String weightUnit = data.getWeightUnit();


		String height = appGlobal.getDefaultHeight();
		String heightUnit = appGlobal.getHeightUnit();
		
		String userName = ADSharedPreferences.getString(ADSharedPreferences.KEY_LOGIN_USER_NAME, "");
		DataBase db = new DataBase(mContext, userName);

		// ゲストの場合は"guest@gmail.com"のメールアドレスでユーザ情報を取得する。
		RegistrationInfoBean registerInfo = db.getUserDetailAccount("guest@gmail.com");

		if (registerInfo != null) {
			height = registerInfo.getUserHeight();
			if (height == null) {
				height = "0";
			}
			heightUnit = registerInfo.getUserHeightunit();
		} else {
			height = "0";
		}
		
		String currentUnitType = ADSharedPreferences.getString(ADSharedPreferences.KEY_WEIGHT_SCALE_UNITS, ADSharedPreferences.DEFAULT_WEIGHT_SCALE_UNITS);
		float weight = Float.valueOf(weightString);

		if (currentUnitType.equalsIgnoreCase(ADSharedPreferences.VALUE_WEIGHT_SCALE_UNITS_LBS)) {
			if (weightUnit.equalsIgnoreCase(ADSharedPreferences.VALUE_WEIGHT_SCALE_UNITS_KG)) {
				setBMIUSKG(heightUnit, height, weight); // データがkgの場合は先にBMIを表示する。
				weight = (float) (weight / 0.45359);
			} else {
				setBMIUSKG(heightUnit, height, (float)(weight * 0.45359)); // kgに直してからBMIを計算する
			}

			weightUnit = ADSharedPreferences.VALUE_WEIGHT_SCALE_UNITS_LBS;

		} else if (currentUnitType.equalsIgnoreCase(ADSharedPreferences.VALUE_WEIGHT_SCALE_UNITS_KG)) {
			if (weightUnit.equalsIgnoreCase(ADSharedPreferences.VALUE_WEIGHT_SCALE_UNITS_LBS)) {
				weight = (float) (weight * 0.45359);
				setBMIUSKG(heightUnit, height, weight); // kgに直してからBMIを計算する
			} else {
				setBMIUSKG(heightUnit, height, weight);
			}

			weightUnit = ADSharedPreferences.VALUE_WEIGHT_SCALE_UNITS_KG;
		}
		weight_scale_value.setText(String.format("%.1f", weight));
		
		try {
			txt_weightScale_Date.setText(ANDMedicalUtilities
					.FormatDashboardDispDate(mContext, data.getDate() + "T"
							+ data.getTime()));
		} catch (Exception e) {
		}
		weight_unit.setText(weightUnit);
	}
	
	private void setBMIUSKG(String heightUnit, String height, float weight) {
		float heightFloat = Float.valueOf(height);
		
		if (heightUnit.equalsIgnoreCase("in")) { //inchで値が来るので、mに変換
			heightFloat = (float) (heightFloat * 0.0254); // mに変換
		} else { // cm で来た場合はmに変換
			heightFloat = (float) (heightFloat / 100);
		}
		
		if (heightFloat != 0) {
			String bmi = String.format("%.1f",((weight) / (heightFloat * heightFloat)));
			this.bmi.setText(bmi);
			this.bmi.setVisibility(View.VISIBLE);
		} else {
			this.bmi.setText("0");
		}
	}
}
