package jp.co.aandd.bleSimpleApp.gatt;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Gatt通信に使用するUUID
 */
public class ADGattUUID {
	
	public static final UUID ClientCharacteristicConfiguration = uuidFromShortString("2902");

	/*
	 * A&D Custom
	 */
	public static final UUID AndCustomWeightScaleService = UUID.fromString("23434100-1FE4-1EFF-80CB-00FF78297D8B");
	public static final UUID AndCustomWeightScaleMeasurement = UUID.fromString("23434101-1FE4-1EFF-80CB-00FF78297D8B");

	/*
	 * Services
	 */
	/*public static final UUID AlertNotificationService = uuidFromShortString("1811");
	public static final UUID BatteryService = uuidFromShortString("180f");*/
	public static final UUID BloodPressureService = uuidFromShortString("1810");
/*	public static final UUID CurrentTimeService = uuidFromShortString("1805");
	public static final UUID CyclingPowerService = uuidFromShortString("1818");
	public static final UUID CyclingSpeedandCadenceService = uuidFromShortString("1816");
	public static final UUID DeviceInformationService = uuidFromShortString("180a");
	public static final UUID GenericAccessService = uuidFromShortString("1800");
	public static final UUID GenericAttributeService = uuidFromShortString("1801");
	public static final UUID GlucoseService = uuidFromShortString("1808");
	public static final UUID HealthThermometerService = uuidFromShortString("1809");
	public static final UUID HeartRateService = uuidFromShortString("180d");
	public static final UUID HumanInterfaceDeviceService = uuidFromShortString("1812");
	public static final UUID ImmediateAlertService = uuidFromShortString("1802");
	public static final UUID LinkLossService = uuidFromShortString("1803");
	public static final UUID LocationandNavigationService = uuidFromShortString("1819");
	public static final UUID NextDSTChangeService = uuidFromShortString("1807");
	public static final UUID PhoneAlertStatusService = uuidFromShortString("180e");
	public static final UUID ReferenceTimeUpdateService = uuidFromShortString("1806");
	public static final UUID RunningSpeedandCadenceService = uuidFromShortString("1814");
	public static final UUID ScanParametersService = uuidFromShortString("1813");
	public static final UUID TxPowerService = uuidFromShortString("1804");*/
	public static final UUID WeightScaleService = uuidFromShortString("181d");

	/*
	 * Characteristics
	 */
	/*public static final UUID AlertCategoryID = uuidFromShortString("2a43");
	public static final UUID AlertCategoryIDBitMask = uuidFromShortString("2a42");
	public static final UUID AlertLevel = uuidFromShortString("2a06");
	public static final UUID AlertNotificationControlPoint = uuidFromShortString("2a44");
	public static final UUID AlertStatus = uuidFromShortString("2a3f");
	public static final UUID Appearance = uuidFromShortString("2a01");
	public static final UUID BatteryLevel = uuidFromShortString("2a19");*/
	public static final UUID BloodPressureFeature = uuidFromShortString("2a49");
	public static final UUID BloodPressureMeasurement = uuidFromShortString("2a35");
/*	public static final UUID BodySensorLocation = uuidFromShortString("2a38");
	public static final UUID BootKeyboardInputReport = uuidFromShortString("2a22");
	public static final UUID BootKeyboardOutputReport = uuidFromShortString("2a32");
	public static final UUID BootMouseInputReport = uuidFromShortString("2a33");
	public static final UUID CSCFeature = uuidFromShortString("2a5c");
	public static final UUID CSCMeasurement = uuidFromShortString("2a5b");
	public static final UUID CurrentTime = uuidFromShortString("2a2b");
	public static final UUID CyclingPowerControlPoint = uuidFromShortString("2a66");
	public static final UUID CyclingPowerFeature = uuidFromShortString("2a65");
	public static final UUID CyclingPowerMeasurement = uuidFromShortString("2a63");
	public static final UUID CyclingPowerVector = uuidFromShortString("2a64");
	*/
	public static final UUID DateTime = uuidFromShortString("2a08");
	/*
	public static final UUID DayDateTime = uuidFromShortString("2a0a");
	public static final UUID DayofWeek = uuidFromShortString("2a09");
	public static final UUID DeviceName = uuidFromShortString("2a00");
	public static final UUID DSTOffset = uuidFromShortString("2a0d");
	public static final UUID ExactTime256 = uuidFromShortString("2a0c");
	public static final UUID FirmwareRevisionString = uuidFromShortString("2a26");
	public static final UUID GlucoseFeature = uuidFromShortString("2a51");
	public static final UUID GlucoseMeasurement = uuidFromShortString("2a18");
	public static final UUID GlucoseMeasurementContext = uuidFromShortString("2a34");
	public static final UUID HardwareRevisionString = uuidFromShortString("2a27");
	public static final UUID HeartRateControlPoint = uuidFromShortString("2a39");
	public static final UUID HeartRateMeasurement = uuidFromShortString("2a37");
	public static final UUID HIDControlPoint = uuidFromShortString("2a4c");
	public static final UUID HIDInformation = uuidFromShortString("2a4a");
	public static final UUID CertificationDataList = uuidFromShortString("2a2a");
	public static final UUID IntermediateCuffPressure = uuidFromShortString("2a36");
	public static final UUID IntermediateTemperature = uuidFromShortString("2a1e");
	public static final UUID LNControlPoint = uuidFromShortString("2a6b");
	public static final UUID LNFeature = uuidFromShortString("2a6a");
	public static final UUID LocalTimeInformation = uuidFromShortString("2a0f");
	public static final UUID LocationandSpeed = uuidFromShortString("2a67");
	public static final UUID ManufacturerNameString = uuidFromShortString("2a29");
	public static final UUID MeasurementInterval = uuidFromShortString("2a21");
	public static final UUID ModelNumberString = uuidFromShortString("2a24");
	public static final UUID Navigation = uuidFromShortString("2a68");
	public static final UUID NewAlert = uuidFromShortString("2a46");
	public static final UUID PeripheralPreferredConnectionParameters = uuidFromShortString("2a04");
	public static final UUID PeripheralPrivacyFlag = uuidFromShortString("2a02");
	public static final UUID PnPID = uuidFromShortString("2a50");
	public static final UUID PositionQuality = uuidFromShortString("2a69");
	public static final UUID ProtocolMode = uuidFromShortString("2a4e");
	public static final UUID ReconnectionAddress = uuidFromShortString("2a03");
	public static final UUID RecordAccessControlPoint = uuidFromShortString("2a52");
	public static final UUID ReferenceTimeInformation = uuidFromShortString("2a14");
	public static final UUID Report = uuidFromShortString("2a4d");
	public static final UUID ReportMap = uuidFromShortString("2a4b");
	public static final UUID RingerControlPoint = uuidFromShortString("2a40");
	public static final UUID RingerSetting = uuidFromShortString("2a41");
	public static final UUID RSCFeature = uuidFromShortString("2a54");
	public static final UUID RSCMeasurement = uuidFromShortString("2a53");
	public static final UUID SCControlPoint = uuidFromShortString("2a55");
	public static final UUID ScanIntervalWindow = uuidFromShortString("2a4f");
	public static final UUID ScanRefresh = uuidFromShortString("2a31");
	public static final UUID SensorLocation = uuidFromShortString("2a5d");
	public static final UUID SerialNumberString = uuidFromShortString("2a25");
	public static final UUID ServcieChanged = uuidFromShortString("2a05");
	public static final UUID SoftwareRevisionString = uuidFromShortString("2a28");
	public static final UUID SupportedNewAlertCategory = uuidFromShortString("2a47");
	public static final UUID SupportedUnreadAlertCategory = uuidFromShortString("2a48");
	public static final UUID SystemID = uuidFromShortString("2a23");
	public static final UUID TemperatureMeasurement = uuidFromShortString("2a1c");
	public static final UUID TemperatureType = uuidFromShortString("2a1d");
	public static final UUID TimeAccuracy = uuidFromShortString("2a12");
	public static final UUID TimeSource = uuidFromShortString("2a13");
	public static final UUID TimeUpdateControlPoint = uuidFromShortString("2a16");
	public static final UUID TimeUpdateState = uuidFromShortString("2a17");
	public static final UUID TimewithDST = uuidFromShortString("2a11");
	public static final UUID TimeZone = uuidFromShortString("2a0e");
	public static final UUID TxPowerLevel = uuidFromShortString("2a07");
	public static final UUID UnreadAlertStatus = uuidFromShortString("2a45");*/
	public static final UUID WeightScaleMeasurement = uuidFromShortString("2a9d");
	public static final UUID WeightScaleFeature = uuidFromShortString("2a9e");
	
	public static List<UUID> ServicesUUIDs = new ArrayList<UUID>();
	public static List<UUID> MeasuCharacUUIDs = new ArrayList<UUID>();
	
	static {
		ServicesUUIDs.add(AndCustomWeightScaleService);
		ServicesUUIDs.add(BloodPressureService);
		ServicesUUIDs.add(WeightScaleService);
		
		MeasuCharacUUIDs.add(AndCustomWeightScaleMeasurement);
		MeasuCharacUUIDs.add(BloodPressureMeasurement);
		MeasuCharacUUIDs.add(WeightScaleMeasurement);
	}
	
	
	public static UUID uuidFromShortString(String uuid) {
		return UUID.fromString(String.format("0000%s-0000-1000-8000-00805f9b34fb", uuid));
	}
}
