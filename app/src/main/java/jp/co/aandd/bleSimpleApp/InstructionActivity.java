package jp.co.aandd.bleSimpleApp;

import jp.co.aandd.bleSimpleApp.base.ADInstructionActivity;
import jp.co.aandd.bleSimpleApp.gatt.BleConnectService;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


public class InstructionActivity extends ADInstructionActivity {

	// One-Time Flag
	private boolean isShowPairing = false;
	private boolean isShowDialog = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		if (mDeviceScanMode == DEVICE_SCAN_MODE_WS) { // 体重計
			setDialogImageWithResourceID(R.drawable.ws_pairing);
			setDialogBackGroundColorWithResorceID(R.color.dashboard_theme_color_weightscale);
			setDialogMessageWithResorceID(R.string.paring_message_weight_scale);

		} else if (mDeviceScanMode == DEVICE_SCAN_MODE_BP) { // 血圧計
			setDialogImageWithResourceID(R.drawable.bp_pairing);
			setDialogBackGroundColorWithResorceID(R.color.dashboard_theme_color_bloodpresure);
			setDialogMessageWithResorceID(R.string.paring_message_bloodpressure_monitor);

		} else if (mDeviceScanMode == DEVICE_SCAN_MODE_AM) { // 活量動計
			//setDialogImageWithResourceID();
		} else if (mDeviceScanMode == DEVICE_SCAN_MODE_TM) { // 体温計
			setDialogImageWithResourceID(R.drawable.th_pairing);
		}
	}
	
	@Override
	protected void onServiceConnected() {
		super.onServiceConnected();
		
		mMainThreadHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if(mBleService != null) {
					mBleService.startScanDevices();
				}
			}
		}, 6 * 1000);
	}
	
	@Override
	protected void onFindConnectDevice(String address) {
		super.onFindConnectDevice(address);

		if(mBleService != null) {
			mBleService.stopScanDevice();
		}

		if(!isShowPairing) {
			isShowPairing = true;

			BluetoothDevice device = null;
			device = BleConnectService.getBluetoothDevice(InstructionActivity.this,address);

			if (device != null) {
				if (mDeviceScanMode == DEVICE_SCAN_MODE_WS) {
					SharedPreferences.Editor editor = getSharedPreferences(
							"ANDMEDICAL", MODE_PRIVATE).edit();
					editor.putString("weightdeviceid",
							"" + device.getUuids());
					editor.commit();
				}
				else if (mDeviceScanMode == DEVICE_SCAN_MODE_BP) {
					SharedPreferences.Editor editor = getSharedPreferences(
							"ANDMEDICAL", MODE_PRIVATE).edit();
					editor.putString("bpdeviceid", "" + device.getUuids());
					editor.commit();
				}
				
				if(mBleService != null) {
					mBleService.connectDevice(device.getAddress());
				}
			}
		}
	}
	
	@Override
	protected void onDevicePairingResult(boolean result) {
		super.onDevicePairingResult(result);

		if(result) {
			Intent intent = new Intent(InstructionActivity.this,
					DashboardActivity.class);

			startActivity(intent);
			finish();
		}
		else {
			// to do Nothing
		}
	}
}
