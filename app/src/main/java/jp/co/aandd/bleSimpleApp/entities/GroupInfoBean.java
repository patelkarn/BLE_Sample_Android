package jp.co.aandd.bleSimpleApp.entities;

public class GroupInfoBean {

	String groupname = "", Username = "", Lastname = "", Email = "",
			Password = "", Sex = "", HeightUnit = "", TimeZone = "";

	public String getHeightUnit() {
		return HeightUnit;
	}

	public void setHeightUnit(String heightUnit) {
		HeightUnit = heightUnit;
	}

	public String getTimeZone() {
		return TimeZone;
	}

	public void setTimeZone(String timeZone) {
		TimeZone = timeZone;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	int Height = 0;
	long Birthdate = 0;

	public long getBirthdate() {
		return Birthdate;
	}

	public void setBirthdate(long birthdate) {
		Birthdate = birthdate;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getLastname() {
		return Lastname;
	}

	public void setLastname(String lastname) {
		Lastname = lastname;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public int getHeight() {
		return Height;
	}

	public void setHeight(int height) {
		Height = height;
	}

}
