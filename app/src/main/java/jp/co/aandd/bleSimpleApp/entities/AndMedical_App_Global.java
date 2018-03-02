package jp.co.aandd.bleSimpleApp.entities;

import jp.co.aandd.bleSimpleApp.MeasuDataManager;
import jp.co.aandd.bleSimpleApp.utilities.ADSharedPreferences;
import android.app.Application;


public class AndMedical_App_Global extends Application {
	
	private MeasuDataManager measuDataManager;

	String firstName, lastName, emailAddress, password, dateOfBirth, gender,
			timezone, defaultHeight = "61", heightUnit = "feet", accountStatus, lastChangedBy,
			lastChangedDate, accountRegistrationDate,maonthDate,activityDate;

	
	@Override
	public void onCreate() {
		super.onCreate();
		
		ADSharedPreferences.SharedInstance(this);
	}

	public String getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(String activityDate) {
		this.activityDate = activityDate;
	}

	public String getMaonthDate() {
		return maonthDate;
	}

	public void setMaonthDate(String maonthDate) {
		this.maonthDate = maonthDate;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getDefaultHeight() {
		return defaultHeight;
	}

	public void setDefaultHeight(String defaultHeight) {
		this.defaultHeight = defaultHeight;
	}

	public String getHeightUnit() {
		return heightUnit;
	}

	public void setHeightUnit(String heightUnit) {
		this.heightUnit = heightUnit;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getLastChangedBy() {
		return lastChangedBy;
	}

	public void setLastChangedBy(String lastChangedBy) {
		this.lastChangedBy = lastChangedBy;
	}

	public String getLastChangedDate() {
		return lastChangedDate;
	}

	public void setLastChangedDate(String lastChangedDate) {
		this.lastChangedDate = lastChangedDate;
	}

	public String getAccountRegistrationDate() {
		return accountRegistrationDate;
	}

	public void setAccountRegistrationDate(String accountRegistrationDate) {
		this.accountRegistrationDate = accountRegistrationDate;
	}

	public MeasuDataManager getMeasuDataManager() {
		return measuDataManager;
	}

	public void setMeasuDataManager(MeasuDataManager measuDataManager) {
		this.measuDataManager = measuDataManager;
	}

}
