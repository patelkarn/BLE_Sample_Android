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


/**
 * This class includes a small subset of standard GATT attributes for
 * demonstration purposes.
 */
public class SampleGattAttributes {

	public static String WEIGHT_AD_SERVICE = "23434100-1FE4-1EFF-80CB-00FF78297D8B";
	public static String WEIGHT_AD_CHAR = "23434101-1FE4-1EFF-80CB-00FF78297D8B";
	public static String WEIGHT_AD_FEATURE = "23434102-1FE4-1EFF-80CB-00FF78297D8B";
	
	public static String DATE_TIME_CHAR = "00002A08-0000-1000-8000-00805f9b34fb";

	public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

	public static String BP_SERVICE = "1810-0000-1000-8000-00805f9b34fb";
	public static String BP_CHAR = "2A35-0000-1000-8000-00805f9b34fb";
	public static String BP_FEATURE = "2a49-0000-1000-8000-00805f9b34fb";
	
	public static String WEIGHT_SERVICE = "181D-0000-1000-8000-00805f9b34fb";
	public static String WEIGHT_CHAR = "2A9D-0000-1000-8000-00805f9b34fb";
	public static String WEIGHT_FEATURE = "2A9E-0000-1000-8000-00805f9b34fb";
}
