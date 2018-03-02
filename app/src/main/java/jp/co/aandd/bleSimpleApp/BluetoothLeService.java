/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.aandd.bleSimpleApp;

import java.util.UUID;

import jp.co.aandd.bleSimpleApp.gatt.ADGattUUID;
import jp.co.aandd.bleSimpleApp.gatt.BloodPressureMeasurement;
import jp.co.aandd.bleSimpleApp.gatt.WeightMeasurement;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;


/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */
public class BluetoothLeService extends Service {
	private final static String TAG = "ANDMEDICAL";

	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private String mBluetoothDeviceAddress;
	private BluetoothGatt mBluetoothGatt;
	private int mConnectionState = STATE_DISCONNECTED;

	private static final int STATE_DISCONNECTED = 0;
	private static final int STATE_CONNECTING = 1;
	private static final int STATE_CONNECTED = 2;

	public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
	public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
	public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";

	public final static UUID UUID_WEIGHT_AD_CHAR = UUID
			.fromString(SampleGattAttributes.WEIGHT_AD_CHAR);
	public final static UUID UUID_WEIGHT_AD_SERVICE = UUID
			.fromString(SampleGattAttributes.WEIGHT_AD_SERVICE);
	
	public final static UUID UUID_BP_CHAR = UUID
			.fromString(SampleGattAttributes.BP_CHAR);
	public final static UUID UUID_BP_SERVICE = UUID
			.fromString(SampleGattAttributes.BP_SERVICE);
	
	public final static UUID UUID_WEIGHT_CHAR = UUID
			.fromString(SampleGattAttributes.WEIGHT_CHAR);
	public final static UUID UUID_WEIGHT_SERVICE = UUID
			.fromString(SampleGattAttributes.WEIGHT_SERVICE);

	public final static String CONNECTION_WEIGHT = "weight";
	public final static String CONNECTION_BP = "bp";

	// Implements callback methods for GATT events that the app cares about. For
	// example,
	// connection change and services discovered.
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			String intentAction;
			Log.v("Sample", "status is " + status);
			Log.v("Sample", "newstate is " + newState);

			if (newState == BluetoothProfile.STATE_CONNECTED) {
				intentAction = ACTION_GATT_CONNECTED;
				mConnectionState = STATE_CONNECTED;
				broadcastUpdate(intentAction);
				Log.v(TAG, "Connected to GATT server.");
				// Attempts to discover services after successful connection.
				Log.i(TAG, "Attempting to start service discovery:"
						+ mBluetoothGatt.discoverServices());

			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				intentAction = ACTION_GATT_DISCONNECTED;
				mConnectionState = STATE_DISCONNECTED;
				Log.v(TAG, "Disconnected from GATT server.");
				broadcastUpdate(intentAction);
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			Log.v(TAG, "Servuce discovered");
			Log.v(TAG, "on service discovered" + " Status " + status + "="
					+ BluetoothGatt.GATT_SUCCESS);

			Log.d("SN", "ServicesDiscovered !!!");
			if (status == BluetoothGatt.GATT_SUCCESS) {
				Log.v("1111", "GATT_SUCCESS");
				delayHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
					}
				}, 1000L);
				
			} else {
				Log.v(TAG, "onServicesDiscovered received: " + status);
			}
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			Log.d("SN", "Read Char");
			Log.v(TAG, "onCharacteristicRead " + " Status " + status + "="
					+ BluetoothGatt.GATT_SUCCESS);
			Log.v("111122", "GATT_SUCCESS Entered");
			if (status == BluetoothGatt.GATT_SUCCESS) {
				Log.v("111122", "GATT_SUCCESS");
				broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			}
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			Log.v(TAG, "onCharacteristicChanged");

			broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			super.onDescriptorWrite(gatt, descriptor, status);
			
			Log.d("SN", "Descriptor Write");
			
			if(isDelayEnableNotify) {
				isDelayEnableNotify = false;
				setNotificationSetting(mBluetoothGatt, true);
			}
		}
		
	};

	private void broadcastUpdate(final String action) {
		Log.v("On broadcast Updated.", "Entered..");
		final Intent intent = new Intent(action);
		sendBroadcast(intent);
	}

	public void broadcastUpdate(final String action,
			final BluetoothGattCharacteristic characteristic) {
		
		Log.v("On broadcast Updated.", "With Gatt Entered..");
		final Intent intent = new Intent(action);
		Log.v(TAG, "broadcastUpdate" + characteristic.getUuid());

		if (UUID_WEIGHT_AD_CHAR.equals(characteristic.getUuid())) {

			String unitsvalue = "";

			byte[] byteReceivedData = characteristic.getValue();
			Log.v(TAG, "bytes values is " + byteReceivedData[5] + "");
			Log.v(TAG, "bytes values is " + byteReceivedData[6] + "");

			int weight = ((byteReceivedData[2] & 0xFF) * 256 + (byteReceivedData[1] & 0xFF));

			int year = ((byteReceivedData[4] & 0xFF) * 256 + (byteReceivedData[3] & 0xFF));

			if (byteReceivedData.length > 3) {
				try {
					if (byteReceivedData[0] == 2) {
						unitsvalue = "kg";
					} else {
						unitsvalue = "lbs";
					}
				} catch (Exception e) {
					unitsvalue = "lbs";

				}
				intent.putExtra("state", weight);
				intent.putExtra("units", unitsvalue);
				intent.putExtra(
						
						"date" , year + "-"
								+ pad(Integer.valueOf(byteReceivedData[5]))
								+ "-"
								+ pad(Integer.valueOf(byteReceivedData[6])));
				intent.putExtra("time",
						pad(Integer.valueOf(byteReceivedData[7])) + ":"
								+ pad(Integer.valueOf(byteReceivedData[8])));
				intent.putExtra(EXTRA_DATA, CONNECTION_WEIGHT);

				Log.v(TAG ,"Piaring "+ "date" + year + "-" + pad(Integer.valueOf(byteReceivedData[5]))
						+ "-"
						+ pad(Integer.valueOf(byteReceivedData[6])) + "");
				Log.v(TAG ,"Piaring "+ "Weight " + weight + "");
				Log.v(TAG ,"Piaring "+ "Units " + unitsvalue + "");

			} else {

				int year1 = ((byteReceivedData[4] & 0xFF) * 256 + (byteReceivedData[3] & 0xFF));

				try {
					if (byteReceivedData[0] == 0) {
						unitsvalue = "kg";
					} else {
						unitsvalue = "lbs";
					}
				} catch (Exception e) {
					unitsvalue = "lbs";

				}
				intent.putExtra("state", weight);
				intent.putExtra("units", unitsvalue);
				intent.putExtra(
						"date",
						year1 + "-" + pad(Integer.valueOf(byteReceivedData[5]))
								+ "-"
								+ pad(Integer.valueOf(byteReceivedData[6])));
				intent.putExtra("time",
						pad(Integer.valueOf(byteReceivedData[7])) + ":"
								+ pad(Integer.valueOf(byteReceivedData[8])));
				Log.v(TAG ,"Piaring "+ "date" + year1 + "-" + pad(Integer.valueOf(byteReceivedData[5]))
						+ "-"
						+ pad(Integer.valueOf(byteReceivedData[6])) + "");
				Log.v(TAG,"Piaring "+ "Weight " + weight + "");

				Log.v(TAG,"Piaring "+ "Units " + unitsvalue + "");

			}
			intent.putExtra(EXTRA_DATA, CONNECTION_WEIGHT);
			sendBroadcast(intent);

		} else if (UUID_BP_CHAR.equals(characteristic.getUuid())) {
			Log.v(TAG, "broadcastUpdate " + "Pairing data received");

			// For all other profiles, writes the data formatted in HEX.
			byte[] byteReceivedData = characteristic.getValue();

			if (byteReceivedData != null && byteReceivedData.length > 0) {

				Bundle valueBundle = BloodPressureMeasurement.readCharacteristic(characteristic);
				
				int sys = (int)valueBundle.getFloat(BloodPressureMeasurement.KEY_SYSTOLIC);
				int dia = (int)valueBundle.getFloat(BloodPressureMeasurement.KEY_DIASTOLIC);
				int pul = (int)valueBundle.getFloat(BloodPressureMeasurement.KEY_PULSE_RATE);
				intent.putExtra("sys", sys + "");
				intent.putExtra("dia", dia + "");
				intent.putExtra("pul", pul + "");
				
				int year = (int)valueBundle.getInt(BloodPressureMeasurement.KEY_YEAR);
				int month = (int)valueBundle.getInt(BloodPressureMeasurement.KEY_MONTH);
				int day = (int)valueBundle.getInt(BloodPressureMeasurement.KEY_DAY);
				intent.putExtra(
						"date",
						year + "-" + pad(month)+ "-" + pad(day)
						);
				
				int hours = (int)valueBundle.getInt(BloodPressureMeasurement.KEY_HOURS);
				int minutes = (int)valueBundle.getInt(BloodPressureMeasurement.KEY_MINUTES);
				// not used
				//int seconds = (int)valueBundle.getInt(BloodPressureMeasurement.KEY_SECONDS);
				
				intent.putExtra(
						"time",
						pad(hours) + ":" + pad(minutes)
						);
				
				intent.putExtra(EXTRA_DATA, CONNECTION_BP);
				sendBroadcast(intent);

			}
		}
		else if (UUID_WEIGHT_CHAR.equals(characteristic.getUuid())) {

			Bundle valueBundle = WeightMeasurement.readCharacteristic(characteristic);
			
			int weight = valueBundle.getInt(WeightMeasurement.KEY_WEIGHT);
			String unitsvalue = valueBundle.getString(WeightMeasurement.KEY_UNIT, "lbs");
			intent.putExtra("state", weight);
			intent.putExtra("units", unitsvalue);

			int year = valueBundle.getInt(WeightMeasurement.KEY_YEAR);
			int month = valueBundle.getInt(WeightMeasurement.KEY_MONTH);
			int day = valueBundle.getInt(WeightMeasurement.KEY_DAY);
			int hours = valueBundle.getInt(WeightMeasurement.KEY_HOURS);
			int minutes = valueBundle.getInt(WeightMeasurement.KEY_MINUTES);
			// not used
			//int seconds = valueBundle.getInt(WeightMeasurement.KEY_SECONDS);
			
			intent.putExtra(
					"date" ,
					year + "-" + pad(month) + "-" + pad(day)
					);
			intent.putExtra(
					"time",
					pad(hours) + ":" + pad(minutes)
					);
			intent.putExtra(EXTRA_DATA, CONNECTION_WEIGHT);

			sendBroadcast(intent);
		}
	}

	public class LocalBinder extends Binder {
		BluetoothLeService getService() {
			return BluetoothLeService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		delayHandler = new Handler();
	}
	
	private Handler delayHandler;

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.v(TAG, "UNbind");

		// After using a given device, you should make sure that
		// BluetoothGatt.close() is called
		// such that resources are cleaned up properly. In this particular
		// example, close() is
		// invoked when the UI is disconnected from the Service.
		close();
		return super.onUnbind(intent);
	}

	private final IBinder mBinder = new LocalBinder();

	/**
	 * Initializes a reference to the local Bluetooth adapter.
	 * 
	 * @return Return true if the initialization is successful.
	 */
	public boolean initialize() {
		// For API level 18 and above, get a reference to BluetoothAdapter
		// through
		// BluetoothManager.
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				Log.e(TAG, "Unable to initialize BluetoothManager.");
				return false;
			}
		}

		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
			return false;
		}
		Log.v(TAG, "Intialize");

		return true;
	}

	/**
	 * Connects to the GATT server hosted on the Bluetooth LE device.
	 * 
	 * @param address
	 *            The device address of the destination device.
	 * 
	 * @return Return true if the connection is initiated successfully. The
	 *         connection result is reported asynchronously through the
	 *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 *         callback.
	 */
	public boolean connect(final String address) {
		if (mBluetoothAdapter == null || address == null) {
			Log.v(TAG,
					"BluetoothAdapter not initialized or unspecified address.");
			return false;
		}

		// Previously connected device. Try to reconnect.
		if (mBluetoothDeviceAddress != null
				&& address.equals(mBluetoothDeviceAddress)
				&& mBluetoothGatt != null) {
			Log.d(TAG,
					"Trying to use an existing mBluetoothGatt for connection.");
			if (mBluetoothGatt.connect()) {
				mConnectionState = STATE_CONNECTING;
				return true;
			} else {
				return false;
			}
		}
		Log.v(TAG, "Connect");

		final BluetoothDevice device = mBluetoothAdapter
				.getRemoteDevice(address);
		if (device == null) {
			Log.w(TAG, "Device not found.  Unable to connect.");
			return false;
		}
		// We want to directly connect to the device, so we are setting the
		// autoConnect
		// parameter to false.
		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
		Log.v("ANDMEDICAL", "Connect" + "Connecting");

		Log.d(TAG, "Trying to create a new connection.");
		mBluetoothDeviceAddress = address;
		mConnectionState = STATE_CONNECTING;
		return true;
	}

	/**
	 * Disconnects an existing connection or cancel a pending connection. The
	 * disconnection result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 * callback.
	 */
	public void disconnect() {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.v(TAG, "BluetoothAdapter not initialized");
			return;
		}
		Log.v(TAG, "Disonnect");

		mBluetoothGatt.disconnect();
	}

	/**
	 * After using a given BLE device, the app must call this method to ensure
	 * resources are released properly.
	 */
	public void close() {
		if (mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt = null;
		Log.v(TAG, "close");

	}

	/**
	 * Request a read on a given {@code BluetoothGattCharacteristic}. The read
	 * result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
	 * callback.
	 * 
	 * @param characteristic
	 *            The characteristic to read from.
	 */
	public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
		Log.v(TAG, "BluetoothGattCharacteristic called.");
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		boolean s = mBluetoothGatt.readCharacteristic(characteristic);
		Log.v(TAG, "Read Charchtertics :"+s);
		Log.d("SN", ""+characteristic.getProperties());

	}

	/**
	 * Enables or disables notification on a give characteristic.
	 * 
	 * @param characteristic
	 *            Characteristic to act on.
	 * @param enabled
	 *            If true, enable notification. False otherwise.
	 */
	public BluetoothAdapter getBluetoothAdapter() {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return null;
		}
		return mBluetoothAdapter;
	}
	
	private boolean isDelayEnableNotify = false;
	
	public void setCharacteristicNotification(
			BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
//		mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
//		Log.v(TAG, "Set Charactertics Notification");
//
//
//		if (UUID_WEIGHT_AD_CHAR.equals(characteristic.getUuid())) {
//			Log.v("Weight Measure : ", ""+characteristic.getUuid());
//			BluetoothGattDescriptor descriptor = characteristic
//					.getDescriptor(UUID
//							.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
//			descriptor
//					.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
//			mBluetoothGatt.writeDescriptor(descriptor);
//			Log.v(TAG, "Write Descriptor");
//
//		} else if (UUID_BP_CHAR.equals(characteristic.getUuid())) {
//			isDelayEnableNotify = true;
//			Log.v("BP Measure : ", ""+characteristic.getUuid());
//			final BluetoothGattDescriptor descriptor = characteristic
//					.getDescriptor(UUID
//							.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
//			descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
//			mBluetoothGatt.writeDescriptor(descriptor);
//			Log.v(TAG, "Write Descriptor");
//		}
//		else if (UUID_WEIGHT_CHAR.equals(characteristic.getUuid())) {
//			isDelayEnableNotify = true;
//			Log.v("Weight Measure : ", ""+characteristic.getUuid());
//			final BluetoothGattDescriptor descriptor = characteristic
//					.getDescriptor(UUID
//							.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
//			descriptor
//					.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
//			mBluetoothGatt.writeDescriptor(descriptor);
//		}
		isDelayEnableNotify = true;
		setNotificationSetting(mBluetoothGatt, false);
	}

	/**
	 * Retrieves a list of supported GATT services on the connected device. This
	 * should be invoked only after {@code BluetoothGatt#discoverServices()}
	 * completes successfully.
	 * 
	 * @return A {@code List} of supported services.
	 */
	public BluetoothGatt getSupportedGattServices() {
		if (mBluetoothGatt == null)
			return null;

		return mBluetoothGatt;
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
	
	private void setNotificationSetting(BluetoothGatt gatt,boolean notificationEnable) {

		BluetoothGattService gattService = getGattSearvice(gatt);
		
		if(gattService != null) {
			BluetoothGattCharacteristic characteristic = getGattMeasuCharacteristic(gattService);
			
			if(characteristic != null) {
				
				// Notification を要求する
                boolean registered = gatt.setCharacteristicNotification(characteristic, notificationEnable);
 
                // Characteristic の Notification 有効化
                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(ADGattUUID.ClientCharacteristicConfiguration);
                if(notificationEnable) {
                	descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                }
                else {
                	descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                }
                gatt.writeDescriptor(descriptor);
 
                if (registered) {
                	// Characteristics通知設定が成功
                	Log.d("SN", " Succcess 111111111");
                } else {
                    // Characteristics通知設定が失敗
                	Log.d("SN", "Failed 211111111");
                }
			}
			else {
				// Error
				/** Tips: キャラクタが取得できない
				 * 原因は
				 * ・接続対象のデバイスではない
				 * ・UUIDが間違っている
				 */
			}
		}
		else {
			/** Tips: サービスが取得できない
			 * 原因は
			 * ・ペアリングが成功していない
			 * ・接続対象のデバイスではない
			 * ・UUIDが間違っている
			 */
		}
	}
	
	private BluetoothGattService getGattSearvice(BluetoothGatt gatt) {
		BluetoothGattService service = null;
		for(UUID uuid : ADGattUUID.ServicesUUIDs) {
			service = gatt.getService(uuid);
			if(service != null)break;
		}
		return service;
	}
	
	private BluetoothGattCharacteristic getGattMeasuCharacteristic(BluetoothGattService service) {
		BluetoothGattCharacteristic characteristic = null;
		for(UUID uuid : ADGattUUID.MeasuCharacUUIDs) {
			characteristic = service.getCharacteristic(uuid);
			if(characteristic != null)break;
		}
		return characteristic;
	}

}