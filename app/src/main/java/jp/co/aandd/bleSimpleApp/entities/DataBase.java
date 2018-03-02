package jp.co.aandd.bleSimpleApp.entities;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase extends SQLiteOpenHelper {

	private String DB_PATH = "";
	private static final String DATABASE_NAME = "aanddmedical.db";
	private static final String REGISTER_TABLE = "aanddmedical_register";
	private static final String GROUP_TABLE = "aanddmedical_group";

	private static final String ZLIFETRACK_TABLE = "aandmedical_lifetrack";
	private static final String ZLIFETRACK_TABLE_UNSYNC = "aandmedical_lifetrack_unsync";
	private static final String ZWEIGHTTRACK_TABLE = "aandmedical_weighttrack";
	private static final String ZWEIGHTTRACK_TABLE_UNSYNC = "aandmedical_weighttrack_unsync";
	private static final String BPTRACK_TABLE = "aandmedical_bptrack";
	private static final String BPTRACK_TABLE_UNSYNC = "aandmedical_bptrack_unsync";

	private static final String GOALVALUE_TABLE = "aandmedical_goalvalue";
	private static final String REMINDER_TABLE = "aandmedical_reminder";
	private static final String REMINDER_ADD_LABEL = "aandmedical_reminder_label";
	private static final String ZANDMANAGEDOBJECT_TABLE = "aanddmedical_managedobject";
	private static final String ZUSERS_TABLE = "aanddmedical_users";
	private static final String Z_METADATA_TABLE = "aanddmedical_metadata";
	private static final String Z_PRIMARYKEY_TABLE = "aanddmedical_primarykey";

	private static final int DATABASE_VERSION = 1;

	static SQLiteDatabase db;
	Context context;

	// Register _Table

	private static final String KEY_ID = "_id";
	private static final String KEY_FIRST_NAME = "firstname";
	private static final String KEY_LAST_NAME = "lastname";
	private static final String KEY_EMAILID = "email_id";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_USER_HEIGHT = "UserHeight";
	private static final String KEY_USER_HEIGHT_UNIT = "UserHeightUnit";
	private static final String KEY_USER_SEX = "UserSex";
	private static final String KEY_USER_BIRTHDATE = "UserBirthDate";
	private static final String KEY_USER_TIMEZONE = "UserTimeZone";

	// LIFE TRACK DATA
	private static final String LT_DATE = "date";
	private static final String LT_HEART_RATE = "heart_rate";

	private static final String LT_TIME = "time";
	private static final String LT_STEPS = "steps";
	private static final String LT_STEPS_UNITS = "steps_units";
	private static final String LT_TIME_STAMP = "dateTimeStamp";
	private static final String LT_DEVICE_ID = "deviceid";

	private static final String LT_CAL = "cal";
	private static final String LT_CAL_UNITS = "cal_units";

	private static final String LT_MILES = "miles";
	private static final String LT_DISTANCE_MILES = "distance_miles";
	private static final String LT_MILES_UNITS = "miles_units";

	private static final String LT_SLEEP = "sleep";
	private static final String LT_SLEEP_UNITS = "sleep_units";
	private static final String LT_SYNC_STATUS = "sync_status";

	// Weight Tracker

	private static final String WT_WEIGHT = "weight";
	private static final String WT_WEIGHT_UNITS = "weight_units";

	// Goal Values

	private static final String GOAL_USER_NAME = "user_name";
	private static final String GOAL_STEPS = "goal_steps";
	private static final String GOAL_CALORIES = "goal_calories";
	private static final String GOAL_DISTANCE = "goal_distance";
	private static final String GOAL_HOUR = "goal_hour";
	private static final String GOAL_MINT = "goal_mint";
	private static final String GOAL_WEIGHT = "goal_weight";
	private static final String Unit_type = "Unit_type";

	// REMINDER VALUES

	private static final String REMINDER_USER_NAME = "rem_user_name";
	private static final String REMINDER_TIME = "reminder_time";
	private static final String REMINDER_REPEAT_DAYS = "reminder_repeat_days";
	private static final String REMINDER_LABEL = "reminder_label";
	private static final String REMINDER_SOUND = "reminder_sound";
	private static final String REMINDER_SOUND_URI = "reminder_sound_uri";
	private static final String REMINDER_SNOOZE = "reminder_snooze";
	private static final String REMINDER_PENDING_INTENT = "reminder_pending_intent";

	// REMINDER CUSTOM LABEL
	private static final String REMINDER_CUSTOM_LABEL = "reminder_custom_label";

	// BP TRACK

	private static final String PULSE = "pulse";
	private static final String SYSTOLIC = "systolic";
	private static final String DIASTOLIC = "diastolic";
	private static final String PULSE_UNIT = "pulse_unit";
	private static final String SYSTOLIC_UNIT = "systolic_unit";
	private static final String DIASTOLIC_UNIT = "diastolic_unit";
	private static final String IRRWGULAR_PULSE_DETECTION = "irregular_pulse_detection";
	
	// Group Table
	private static final String GROUP_NAME = "groupname";
	private static final String GROUP_USER_NAME = "groupuser";
	private static final String GROUP_USER_LAST_NAME = "groupuser_lastname";
	private static final String GROUP_USER_EMAIL = "groupuser_email";
	private static final String GROUP_USER_PASSWORD = "groupuser_password";
	private static final String GROUP_USER_HEIGHT = "groupuser_height";
	private static final String GROUP_USER_HEIGHT_UNIT = "groupuser_heightunit";
	private static final String GROUP_USER_SEX = "groupuser_sex";
	private static final String GROUP_USER_BIRTHDATE = "groupuser_birthdate";
	private static final String GROUP_USER_TIMEZONE = "groupuser_timezone";
	
	public DataBase(Context context, String database_name) {
		super(context, database_name.toLowerCase().trim() + ".db", null,
				DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		//レジスタ
		String sqlCreateRegisterTable = 
				" create table " + REGISTER_TABLE + "( " + 
						KEY_ID + " integer primary key autoincrement," + 
						KEY_FIRST_NAME + " varchar(256), " + 
						KEY_LAST_NAME + " varchar(256)," + 
						KEY_EMAILID + " varchar(256)," + 
						KEY_PASSWORD + " varchar(256), " + 
						KEY_USER_HEIGHT + " varchar(256), " + 
						KEY_USER_HEIGHT_UNIT + " varchar(256), " + 
						KEY_USER_SEX + " varchar(256)," + 
						KEY_USER_BIRTHDATE + " varchar(256)," + 
						KEY_USER_TIMEZONE + " varchar(256)" +
						"  ); ";
		db.execSQL(sqlCreateRegisterTable);

		//グループ
		String sqlCreateGroupTable = 
				" create table " + GROUP_TABLE + "( " + 
						KEY_ID + " INTEGER PRIMARY KEY," + 
						GROUP_NAME + " varchar(256), " + 
						GROUP_USER_NAME + " varchar(256), " + 
						GROUP_USER_LAST_NAME + " varchar(256)," + 
						GROUP_USER_EMAIL + " varchar(256)," + 
						GROUP_USER_PASSWORD + " varchar(256), " + 
						GROUP_USER_HEIGHT + " varchar(256), " + 
						GROUP_USER_HEIGHT_UNIT + " varchar(256), " + 
						GROUP_USER_SEX + " varchar(256)," + 
						GROUP_USER_BIRTHDATE + " varchar(256)," + 
						GROUP_USER_TIMEZONE + " varchar(256) " + 
						" ); ";
		db.execSQL(sqlCreateGroupTable);

		//ライフトラック
		String sqlCreateLifeTrackTable = 
				" create table " + ZLIFETRACK_TABLE + "( " + 
						KEY_ID + " integer primary key autoincrement," + 
						LT_DATE + " varchar(256), " + 
						LT_TIME + " varchar(256), " + 
						LT_STEPS + " varchar(256)," + 
						LT_STEPS_UNITS + " varchar(256)," + 
						LT_CAL + " varchar(256)," + 
						LT_CAL_UNITS + " varchar(256)," + 
						LT_MILES + " varchar(256)," + 
						LT_DEVICE_ID + " varchar(256)," + 
						LT_DISTANCE_MILES + " varchar(256), " + 
						LT_MILES_UNITS + " varchar(256)," + 
						LT_SLEEP + " varchar(256)," + 
						LT_HEART_RATE + " varchar(256), " + 
						LT_TIME_STAMP + " varchar(256), " + 
						LT_SLEEP_UNITS + " varchar(256) " +
						" ); ";
		db.execSQL(sqlCreateLifeTrackTable);

		//ライフトラック(web連結)
		String sqlCreateLifeTrackUnsyncTable = 
				" create table " + ZLIFETRACK_TABLE_UNSYNC + "( " + 
						KEY_ID + " integer primary key autoincrement," + 
						LT_DATE + " varchar(256), " + 
						LT_TIME + " varchar(256), " + 
						LT_STEPS + " varchar(256)," + 
						LT_SYNC_STATUS + " varchar(256)," + 
						LT_STEPS_UNITS + " varchar(256)," + 
						LT_CAL + " varchar(256)," + 
						LT_CAL_UNITS + " varchar(256)," + 
						LT_MILES + " varchar(256), " + 
						LT_MILES_UNITS + " varchar(256)," + 
						LT_DEVICE_ID + " varchar(256)," + 
						LT_SLEEP + " varchar(256)," + 
						LT_TIME_STAMP + " varchar(256), " + 
						LT_HEART_RATE + " varchar(256), " + 
						LT_SLEEP_UNITS + " varchar(256) " +
						" ); ";
		db.execSQL(sqlCreateLifeTrackUnsyncTable);

		//体重計
		String sqlCreateWeightTrackTable = 
				" create table " + ZWEIGHTTRACK_TABLE + "( " + 
						KEY_ID + " INTEGER PRIMARY KEY," + 
						LT_DATE + " varchar(256), " + 
						LT_TIME + " varchar(256), " + 
						LT_DEVICE_ID + " varchar(256)," + 
						WT_WEIGHT + " varchar(256), " + 
						LT_TIME_STAMP + " varchar(256), " + 
						WT_WEIGHT_UNITS + " varchar(256) " +
						" ); ";
		db.execSQL(sqlCreateWeightTrackTable);

		//体重計(web連結)
		String sqlCreateWeightTrackUnsyncTable = 
				" create table " + ZWEIGHTTRACK_TABLE_UNSYNC + "( " + 
						KEY_ID + " integer primary key autoincrement," + 
						LT_DATE + " varchar(256), " + 
						LT_SYNC_STATUS + " varchar(256)," + 
						LT_DEVICE_ID + " varchar(256)," + 
						LT_TIME + " varchar(256), " + 
						LT_TIME_STAMP + " varchar(256), " + 
						WT_WEIGHT + " varchar(256), " + 
						WT_WEIGHT_UNITS + " varchar(256) " +
						" ); ";
		db.execSQL(sqlCreateWeightTrackUnsyncTable);

		//血圧計
		String sqlCreateBPTrackTable = 
				" create table " + BPTRACK_TABLE + "( " + 
						KEY_ID + " INTEGER PRIMARY KEY," + 
						LT_DATE + " varchar(256), " + 
						LT_TIME + " varchar(256), " + 
						PULSE + " varchar(256), " + 
						SYSTOLIC + " varchar(256), " + 
						DIASTOLIC + " varchar(256), " + 
						PULSE_UNIT + " varchar(256), " + 
						LT_DEVICE_ID + " varchar(256)," + 
						SYSTOLIC_UNIT + " varchar(256), " + 
						LT_TIME_STAMP + " varchar(256), " + 
						DIASTOLIC_UNIT + " varchar(256), " +
						IRRWGULAR_PULSE_DETECTION + " varchar(256) " +
						" ); ";
		db.execSQL(sqlCreateBPTrackTable);

		//血圧計(web連結)
		String sqlCreateBPTrackUnsyncTable = 
				" create table " + BPTRACK_TABLE_UNSYNC + "( " + 
						KEY_ID + " INTEGER PRIMARY KEY," + 
						LT_DATE + " varchar(256), " + 
						LT_TIME + " varchar(256), " + 
						PULSE + " varchar(256), " + 
						SYSTOLIC + " varchar(256), " + 
						DIASTOLIC + " varchar(256), " + 
						PULSE_UNIT + " varchar(256), " + 
						LT_SYNC_STATUS + " varchar(256)," + 
						LT_DEVICE_ID + " varchar(256)," + 
						SYSTOLIC_UNIT + " varchar(256), " + 
						LT_TIME_STAMP + " varchar(256), " + 
						DIASTOLIC_UNIT + " varchar(256), " +
						IRRWGULAR_PULSE_DETECTION + " varchar(256) " +
						" ); ";

		db.execSQL(sqlCreateBPTrackUnsyncTable);

		//目標値
		String sqlCreateGoalValueTable = 
				" create table " + GOALVALUE_TABLE + "( " + 
						KEY_ID + " integer primary key autoincrement ," + 
						GOAL_USER_NAME + " varchar(256), " + 
						Unit_type + " varchar(256), "  + 
						GOAL_STEPS + " varchar(256), " + 
						GOAL_CALORIES + " varchar(256)," + 
						GOAL_DISTANCE + " varchar(256), " + 
						GOAL_HOUR + " varchar(256), " + 
						GOAL_MINT + " varchar(256)," + 
						GOAL_WEIGHT + " varchar(256) " +
						" ); ";

		db.execSQL(sqlCreateGoalValueTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + REGISTER_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + ZLIFETRACK_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + ZWEIGHTTRACK_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + BPTRACK_TABLE);

		// Create tables again
		onCreate(db);
	}

	public void GroupEntry(String groupName, String userName, String lastName,
			String email, String password, int height, String heightUnit,
			String sex, long finalBirthDateTimestamp, String timeZone) {

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			ContentValues cv;
			cv = new ContentValues();

			cv.put(GROUP_NAME, groupName);
			cv.put(GROUP_USER_NAME, userName);
			cv.put(GROUP_USER_LAST_NAME, lastName);
			cv.put(GROUP_USER_EMAIL, email);
			cv.put(GROUP_USER_PASSWORD, password);
			cv.put(GROUP_USER_HEIGHT, height);
			cv.put(GROUP_USER_HEIGHT_UNIT, heightUnit);
			cv.put(GROUP_USER_SEX, sex);
			cv.put(GROUP_USER_BIRTHDATE, finalBirthDateTimestamp);
			cv.put(GROUP_USER_TIMEZONE, timeZone);

			String sql = "SELECT * FROM " + GROUP_TABLE +
											" WHERE " + GROUP_USER_EMAIL + " = ?";
			String[] selectionArgs = new String[]{email}; 

			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql, selectionArgs);
				if (cursor != null && cursor.getCount() != 0) {
					Log.d("SN","GroupEntry is Update !");
					String whereClause = GROUP_USER_EMAIL + " =  ?";
					String[] whereArgs = new String[]{email};
					db.update(GROUP_TABLE, cv, whereClause, whereArgs);
				} else {
					Log.d("SN","GroupEntry is Insert !");
					db.insert(GROUP_TABLE, null, cv);
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
	}

	public String getGroupbyuser(String userName) {
		SQLiteDatabase db = null;
		String groupName = "";
		try {
			db = this.getWritableDatabase();
			String sql = "SELECT * FROM "+ GROUP_TABLE + " WHERE " + GROUP_USER_EMAIL + " = ?";
			String[] selectionArgs = new String[]{userName};
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql , selectionArgs);
				if (cursor.moveToFirst()) {
					do {
						groupName = cursor.getString(cursor.getColumnIndex(GROUP_NAME));
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		return groupName;
	}

	public void deleteUserinGroup(String userEmail) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String whereClause = "";
			String[] whereArgs = new String[]{userEmail};
			db.delete(GROUP_TABLE, whereClause, whereArgs);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<GroupInfoBean> getallUserbyGroup() {
		SQLiteDatabase db = null;
		ArrayList<GroupInfoBean> groupInfoList = null;
		try {
			db = this.getWritableDatabase();
			String sql = "SELECT * FROM "+ GROUP_TABLE + ";";
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql, null);
				groupInfoList = new ArrayList<GroupInfoBean>();
				if (cursor.moveToFirst()) {
					do {
						GroupInfoBean groupInfoBean = new GroupInfoBean();
						groupInfoBean.setGroupname(cursor.getString(cursor
								.getColumnIndex(GROUP_NAME)));
						groupInfoBean.setUsername(cursor.getString(cursor
								.getColumnIndex(GROUP_USER_NAME)));
						groupInfoBean.setLastname(cursor.getString(cursor
								.getColumnIndex(GROUP_USER_LAST_NAME)));
						groupInfoBean.setEmail(cursor.getString(cursor
								.getColumnIndex(GROUP_USER_EMAIL)));
						groupInfoBean.setPassword(cursor.getString(cursor
								.getColumnIndex(GROUP_USER_PASSWORD)));
						groupInfoBean.setHeight(cursor.getInt(cursor
								.getColumnIndex(GROUP_USER_HEIGHT)));
						groupInfoBean.setHeightUnit(cursor.getString(cursor
								.getColumnIndex(GROUP_USER_HEIGHT_UNIT)));
						groupInfoBean.setSex(cursor.getString(cursor
								.getColumnIndex(GROUP_USER_SEX)));
						groupInfoBean.setBirthdate(cursor.getLong(cursor
								.getColumnIndex(GROUP_USER_BIRTHDATE)));
						groupInfoBean.setTimeZone(cursor.getString(cursor
								.getColumnIndex(GROUP_USER_TIMEZONE)));
		
						groupInfoList.add(groupInfoBean);
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		return groupInfoList;
	}

	public ArrayList<GroupInfoBean> getUserbyGroup(String groupName) {
		SQLiteDatabase db = null;
		ArrayList<GroupInfoBean> groupInfoList = null;
		try {
			db = this.getWritableDatabase();
			String sql = "SELECT * FROM " + GROUP_TABLE + " WHERE " + GROUP_NAME +" = ?";
			String[] selectionArgs = new String[]{groupName};
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql, selectionArgs);
				groupInfoList = new ArrayList<GroupInfoBean>();
				if (cursor.moveToFirst()) {
					do {
						GroupInfoBean groupInfo = new GroupInfoBean();
						groupInfo.setGroupname(groupName);
						groupInfo.setUsername(cursor.getString(cursor
								.getColumnIndex(GROUP_USER_NAME)));
						groupInfo.setLastname(cursor.getString(cursor
								.getColumnIndex(GROUP_USER_LAST_NAME)));
						groupInfo.setEmail(cursor.getString(cursor
								.getColumnIndex(GROUP_USER_EMAIL)));
						groupInfo.setPassword(cursor.getString(cursor
								.getColumnIndex(GROUP_USER_PASSWORD)));
						groupInfo.setHeight(cursor.getInt(cursor
								.getColumnIndex(GROUP_USER_HEIGHT)));
						groupInfo.setHeightUnit(cursor.getString(cursor
								.getColumnIndex(GROUP_USER_HEIGHT_UNIT)));
						groupInfo.setSex(cursor.getString(cursor
								.getColumnIndex(GROUP_USER_SEX)));
						groupInfo.setBirthdate(cursor.getLong(cursor
								.getColumnIndex(GROUP_USER_BIRTHDATE)));
						groupInfo.setTimeZone(cursor.getString(cursor
								.getColumnIndex(GROUP_USER_TIMEZONE)));
						groupInfoList.add(groupInfo);
	
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					} 
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		return groupInfoList;
	}

	public long registerEntry(RegistrationInfoBean infoBeanObj) {
		long result = 0;
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put(KEY_FIRST_NAME, infoBeanObj.getUserName());
			cv.put(KEY_LAST_NAME, infoBeanObj.getUserLastname());
			cv.put(KEY_EMAILID, infoBeanObj.getUserEmail());
			cv.put(KEY_PASSWORD, infoBeanObj.getUserPassword());
			cv.put(KEY_USER_HEIGHT, infoBeanObj.getUserHeight());
			cv.put(KEY_USER_HEIGHT_UNIT, infoBeanObj.getUserHeightunit());
			cv.put(KEY_USER_SEX, infoBeanObj.getUserSex());
			cv.put(KEY_USER_BIRTHDATE, infoBeanObj.getUserDateBirth());
			cv.put(KEY_USER_TIMEZONE, infoBeanObj.getUserTimeZone());
			result = db.insert(REGISTER_TABLE, null, cv);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public void registerupdateEntry(RegistrationInfoBean infoBeanObj) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put(KEY_FIRST_NAME, infoBeanObj.getUserName());
			cv.put(KEY_LAST_NAME, infoBeanObj.getUserLastname());
			cv.put(KEY_EMAILID, infoBeanObj.getUserEmail());
			cv.put(KEY_PASSWORD, infoBeanObj.getUserPassword());
			cv.put(KEY_USER_HEIGHT, infoBeanObj.getUserHeight());
			cv.put(KEY_USER_HEIGHT_UNIT, infoBeanObj.getUserHeightunit());
			cv.put(KEY_USER_SEX, infoBeanObj.getUserSex());
			cv.put(KEY_USER_BIRTHDATE, infoBeanObj.getUserDateBirth());
			cv.put(KEY_USER_TIMEZONE, infoBeanObj.getUserTimeZone());
	
			String sql = "SELECT * FROM " + REGISTER_TABLE + 
						 " WHERE " + KEY_EMAILID + " = ?";
			String[] selectionArgs = new String[]{infoBeanObj.getUserEmail()};
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql, selectionArgs);
				if (cursor != null && cursor.getCount() != 0) {
					Log.d("SN","registerupdateEntry is Update !");
					String whereClause = KEY_EMAILID + " = ?"; 
					String[] whereArgs = new String[]{infoBeanObj.getUserEmail()};
					db.update(REGISTER_TABLE, cv, whereClause, whereArgs);
				} else {
					Log.d("SN","registerupdateEntry is Insert !");
					db.insert(REGISTER_TABLE, null, cv);
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
	}

	public RegistrationInfoBean getUserDetailAccount(String emailId) {
		SQLiteDatabase db = null;
		RegistrationInfoBean infoBeanObj = null;
		try {
			db = this.getWritableDatabase();

			String sql = "SELECT * FROM " + REGISTER_TABLE + " WHERE " + KEY_EMAILID + " = ?";
			String[] selectionArgs = new String[]{emailId};
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql, selectionArgs);
				if (cursor.moveToFirst()) {
					do {
						infoBeanObj = new RegistrationInfoBean();
						infoBeanObj.setUserName(cursor.getString(cursor
								.getColumnIndex(KEY_FIRST_NAME)));
						infoBeanObj.setUserLastname(cursor.getString(cursor
								.getColumnIndex(KEY_LAST_NAME)));
						infoBeanObj.setUserEmail(cursor.getString(cursor
								.getColumnIndex(KEY_EMAILID)));
						infoBeanObj.setUserPassword(cursor.getString(cursor
								.getColumnIndex(KEY_PASSWORD)));
						infoBeanObj.setUserHeight(cursor.getString(cursor
								.getColumnIndex(KEY_USER_HEIGHT)));
						infoBeanObj.setUserHeightunit(cursor.getString(cursor
								.getColumnIndex(KEY_USER_HEIGHT_UNIT)));
						infoBeanObj.setUserSex(cursor.getString(cursor
								.getColumnIndex(KEY_USER_SEX)));
		
						infoBeanObj.setUserDateBirth(cursor.getString(cursor
								.getColumnIndex(KEY_USER_BIRTHDATE)));
		
						infoBeanObj.setUserTimeZone(cursor.getString(cursor
								.getColumnIndex(KEY_USER_TIMEZONE)));
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		return infoBeanObj;

	}

	// Remove a entry based on its index
	public boolean deleteEntry(long rowId) {
		boolean result = false;
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			result = db.delete(REGISTER_TABLE, KEY_ID + "=" + rowId, null) > 0;
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		return result;

	}

	// retrieves all the entries
	public Cursor getAllEntries() {
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.query(REGISTER_TABLE, new String[] { KEY_ID,
				KEY_FIRST_NAME, KEY_LAST_NAME, KEY_EMAILID, KEY_PASSWORD },
				null, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}

	// retrieves all the Update entries
	public Cursor getUpdateEntries(String user) {
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db
				.rawQuery("SELECT * from aanddmedical_register", null);

		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}

	// Life Tracker entry's
	public void lifetrackentry(ArrayList<Lifetrack_infobean> data) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			ContentValues cv;

			for (int i = 0; i < data.size(); i++) {
				cv = new ContentValues();
				cv.put(LT_DATE, data.get(i).getDate());
				cv.put(LT_TIME, data.get(i).getTime());
				cv.put(LT_TIME_STAMP, data.get(i).getDateTimeStamp());

				cv.put(LT_STEPS, data.get(i).getSteps());
				cv.put(LT_STEPS_UNITS, data.get(i).getStepsUnits());

				cv.put(LT_CAL, data.get(i).getCal());
				cv.put(LT_CAL_UNITS, data.get(i).getCalorieUnits());

				cv.put(LT_MILES, data.get(i).getDistance());
				cv.put(LT_MILES_UNITS, data.get(i).getDistanceUnit());
				cv.put(LT_DISTANCE_MILES, data.get(i).getDistanceInMiles());

				cv.put(LT_SLEEP, data.get(i).getSleep());
				cv.put(LT_DEVICE_ID, data.get(i).getDeviceId());

				cv.put(LT_SLEEP_UNITS, data.get(i).getStepsUnits());

				cv.put(LT_HEART_RATE, data.get(i).getHeartRate());

				String sql = "SELECT * FROM " + ZLIFETRACK_TABLE + " WHERE " + LT_DATE + " = ?";
				String[] selectionArgs = new String[]{data.get(i).getDate()};
				Cursor cursor = null;
				try {
					cursor = db.rawQuery(sql, selectionArgs);
					if (cursor != null && cursor.getCount() != 0) {
						Log.d("SN", "lifetrackentry is Update !");
						String whereClause = LT_DATE + " = ?";
						String[] whereArgs = new String[]{data.get(i).getDate()};
						db.update(ZLIFETRACK_TABLE, cv, whereClause, whereArgs);
					} else {
						Log.d("SN", "lifetrackentry is Insert !");
						db.insert(ZLIFETRACK_TABLE, null, cv);
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				} finally {
					try {
						if (cursor != null) {
							cursor.close();
						}
					} catch (SQLiteException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
	}

	// Goal Enteries

	public void lifetrackentryUnsynced(ArrayList<Lifetrack_infobean> data) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			for (int i = 0; i < data.size(); i++) {
				ContentValues cv = new ContentValues();
				cv.put(LT_DATE, data.get(i).getDate());
				cv.put(LT_TIME, data.get(i).getTime());

				cv.put(LT_STEPS, data.get(i).getSteps());
				cv.put(LT_STEPS_UNITS, data.get(i).getStepsUnits());

				cv.put(LT_CAL, data.get(i).getCal());
				cv.put(LT_CAL_UNITS, data.get(i).getCalorieUnits());

				cv.put(LT_MILES, data.get(i).getDistance());
				cv.put(LT_MILES_UNITS, data.get(i).getDistanceUnit());
				cv.put(LT_TIME_STAMP, data.get(i).getDateTimeStamp());
				cv.put(LT_DEVICE_ID, data.get(i).getDeviceId());

				cv.put(LT_SLEEP, data.get(i).getSleep());
				cv.put(LT_SLEEP_UNITS, data.get(i).getStepsUnits());
//				if (!data.get(i).getHeartRate().equalsIgnoreCase("0")) {
					cv.put(LT_HEART_RATE, data.get(i).getHeartRate());

//				}

				cv.put(LT_SYNC_STATUS, data.get(i).getIsSynced());
				String sql = "SELECT * FROM " + ZLIFETRACK_TABLE_UNSYNC
						+ " WHERE " + LT_DATE + " = ?";
				String[] selectionArgs = new String[]{data.get(i).getDate()};
				Cursor cursor = null;
				try {
					cursor = db.rawQuery(sql, selectionArgs);
					if (cursor != null && cursor.getCount() != 0) {
						String whereClause = LT_DATE + " = ?";
						String[] whereArgs = new String[]{data.get(i).getDate()};
	
						db.update(ZLIFETRACK_TABLE_UNSYNC, cv, whereClause, whereArgs);
					} else {
						db.insert(ZLIFETRACK_TABLE_UNSYNC, null, cv);
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				} finally {
					try {
						if (cursor != null) {
							cursor.close();
						}
					} catch (SQLiteException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
	}

	public void weightTrackEntryUnsynced(ArrayList<Lifetrack_infobean> Data) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			for (int i = 0; i < Data.size(); i++) {
				ContentValues cv = new ContentValues();
				cv.put(LT_DATE, Data.get(i).getDate());
				cv.put(LT_TIME, Data.get(i).getTime());
				cv.put(WT_WEIGHT, Data.get(i).getWeight());
				cv.put(WT_WEIGHT_UNITS, Data.get(i).getWeightUnit());
				cv.put(LT_TIME_STAMP, Data.get(i).getDateTimeStamp());
				cv.put(LT_DEVICE_ID, Data.get(i).getDeviceId());

				cv.put(LT_SYNC_STATUS, Data.get(i).getIsSynced());

				Cursor cursorTimeStump = null;
				Cursor cursorTime = null;
				try {
					String sql = "SELECT * FROM " + ZWEIGHTTRACK_TABLE_UNSYNC + 
							" WHERE " + LT_TIME_STAMP + " = ?";
					String selectionArgs[] = new String[]{Data.get(i).getDateTimeStamp()};	
					cursorTimeStump = db.rawQuery(sql, selectionArgs);

					sql = "SELECT * FROM " + ZWEIGHTTRACK_TABLE_UNSYNC + 
							" WHERE " + LT_TIME_STAMP + " = ?";
					selectionArgs = new String[]{Data.get(i).getDateTimeStamp()};
					cursorTime = db.rawQuery(sql, selectionArgs);
	
					if (cursorTimeStump != null && cursorTimeStump.getCount() != 0 
					&&  cursorTime != null && cursorTime.getCount() != 0) {
						String whereClause = LT_TIME_STAMP + " = ?";
						String[] whereArgs = new String[]{Data.get(i).getDateTimeStamp()};
						db.update(ZWEIGHTTRACK_TABLE_UNSYNC, cv, whereClause, whereArgs);
					} else {
						db.insert(ZWEIGHTTRACK_TABLE_UNSYNC, null, cv);
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				} finally {
					try {
						if (cursorTimeStump != null) {
							cursorTimeStump.close();
						}
						if (cursorTime != null) {
							cursorTime.close();
						}
					} catch (SQLiteException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
	}

	public void weighttrackentry(ArrayList<Lifetrack_infobean> data) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			ContentValues cv;
			for (int i = 0; i < data.size(); i++) {
				cv = new ContentValues();
				cv.put(LT_DATE, data.get(i).getDate());
				cv.put(LT_TIME, data.get(i).getTime());
				cv.put(WT_WEIGHT, data.get(i).getWeight());
				cv.put(WT_WEIGHT_UNITS, data.get(i).getWeightUnit());
				cv.put(LT_TIME_STAMP, data.get(i).getDateTimeStamp());

				cv.put(LT_DEVICE_ID, data.get(i).getDeviceId());

				Cursor cursorTimeStamp = null;
				Cursor cursorTime = null;
				try {
					String sql = "SELECT * FROM " + ZWEIGHTTRACK_TABLE
							+ " WHERE " + LT_TIME_STAMP + " = ?";
					String[] selectionArgs = new String[]{data.get(i).getDateTimeStamp()};
					cursorTimeStamp = db.rawQuery(sql, selectionArgs);

					sql = "SELECT * FROM " + ZWEIGHTTRACK_TABLE
							+ " WHERE " + LT_TIME + " = ?";
					selectionArgs = new String[]{data.get(i).getTime()};
					cursorTime = db.rawQuery(sql, selectionArgs);

					if (cursorTimeStamp != null && cursorTimeStamp.getCount() != 0 
					&&  cursorTime != null && cursorTime.getCount() != 0) {
						String whereClause = LT_TIME_STAMP + " = ?";
						String[] whereArgs = new String[]{data.get(i).getDateTimeStamp()};
						db.update(ZWEIGHTTRACK_TABLE, cv, whereClause, whereArgs);
					} else {
						db.insert(ZWEIGHTTRACK_TABLE, null, cv);
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				} finally {
					try {
						if (cursorTimeStamp != null) {
							cursorTimeStamp.close();
						}
						if (cursorTime != null) {
							cursorTime.close();
						}
					} catch (SQLiteException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<Lifetrack_infobean> getUnSyncAcivityDetails(
			String syncStatus) {
		ArrayList<Lifetrack_infobean> lifeList = new ArrayList<Lifetrack_infobean>();
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String sql = "SELECT * FROM " + ZLIFETRACK_TABLE_UNSYNC + " WHERE " + LT_SYNC_STATUS + " = ?";
			String[] selectionArgs = new String[]{syncStatus};
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql, selectionArgs);
				if (cursor.moveToFirst()) {
					do {
						Lifetrack_infobean lifeDetails = new Lifetrack_infobean();
						lifeDetails.setDate(cursor.getString(cursor
								.getColumnIndex(LT_DATE)));
						lifeDetails.setTime(cursor.getString(cursor
								.getColumnIndex(LT_TIME)));
						lifeDetails.setSteps(cursor.getString(cursor
								.getColumnIndex(LT_STEPS)));
						lifeDetails.setCal(cursor.getString(cursor
								.getColumnIndex(LT_CAL)));
						lifeDetails.setDistance(cursor.getString(cursor
								.getColumnIndex(LT_MILES)));
						lifeDetails.setSleep(cursor.getString(cursor
								.getColumnIndex(LT_SLEEP)));
						lifeDetails.setDateTimeStamp(cursor.getString(cursor
								.getColumnIndex(LT_TIME_STAMP)));
						lifeDetails.setDeviceId(cursor.getString(cursor
								.getColumnIndex(LT_DEVICE_ID)));
						lifeDetails.setHeartRate(cursor.getString(cursor
								.getColumnIndex(LT_HEART_RATE)));
						lifeDetails.setStepsUnits(cursor.getString(cursor
								.getColumnIndex(LT_STEPS_UNITS)));
						lifeDetails.setCalorieUnits(cursor.getString(cursor
								.getColumnIndex(LT_CAL_UNITS)));
						lifeDetails.setDistanceUnit(cursor.getString(cursor
								.getColumnIndex(LT_MILES_UNITS)));
						lifeDetails.setSleepUnit(cursor.getString(cursor
								.getColumnIndex(LT_SLEEP_UNITS)));
						// Adding contact to list
						lifeList.add(lifeDetails);
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		return lifeList;

	}

	public ArrayList<Lifetrack_infobean> getUnSyncWeightDetails(
			String syncStatus) {
		ArrayList<Lifetrack_infobean> weightList = new ArrayList<Lifetrack_infobean>();
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String sql = "SELECT * FROM " + ZWEIGHTTRACK_TABLE_UNSYNC +" WHERE " + LT_SYNC_STATUS + " = ?";
			String[] selectionArgs = new String[]{syncStatus};
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql, selectionArgs);
				if (cursor.moveToFirst()) {
					do {
						Lifetrack_infobean weightDetails = new Lifetrack_infobean();
						weightDetails.setDate(cursor.getString(cursor
								.getColumnIndex(LT_DATE)));
						weightDetails.setWeight(cursor.getString(cursor
								.getColumnIndex(WT_WEIGHT)));
						weightDetails.setTime(cursor.getString(cursor
								.getColumnIndex(LT_TIME)));
						weightDetails.setDateTimeStamp(cursor.getString(cursor
								.getColumnIndex(LT_TIME_STAMP)));
						weightDetails.setDeviceId(cursor.getString(cursor
								.getColumnIndex(LT_DEVICE_ID)));
						weightDetails.setWeightUnit(cursor.getString(cursor
								.getColumnIndex(WT_WEIGHT_UNITS)));
						weightDetails.setIsSynced(cursor.getString(cursor
								.getColumnIndex(LT_SYNC_STATUS)));
						weightList.add(weightDetails);
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		return weightList;

	}

	public ArrayList<Lifetrack_infobean> getUnSyncBPtDetails(String syncStatus) {
		ArrayList<Lifetrack_infobean> bloodPressureList = new ArrayList<Lifetrack_infobean>();
		SQLiteDatabase db = null;

		try {
			db = this.getWritableDatabase();
			String sql = "SELECT * FROM " + BPTRACK_TABLE_UNSYNC + " WHERE " + LT_SYNC_STATUS + " = ?";
			String[] selectionArgs = new String[]{syncStatus};
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql, selectionArgs);
				if (cursor.moveToFirst()) {
					do {
						Lifetrack_infobean lifeDetails = new Lifetrack_infobean();
						lifeDetails.setKeyidbp(cursor.getInt(cursor
								.getColumnIndex(KEY_ID)));
						lifeDetails.setDate(cursor.getString(cursor
								.getColumnIndex(LT_DATE)));
						lifeDetails.setTime(cursor.getString(cursor
								.getColumnIndex(LT_TIME)));
						lifeDetails.setPulse(cursor.getString(cursor
								.getColumnIndex(PULSE)));
						lifeDetails.setSystolic(cursor.getString(cursor
								.getColumnIndex(SYSTOLIC)));
						lifeDetails.setDiastolic(cursor.getString(cursor
								.getColumnIndex(DIASTOLIC)));
						lifeDetails.setPulseUnit(cursor.getString(cursor
								.getColumnIndex(PULSE_UNIT)));
						lifeDetails.setSystolicUnit(cursor.getString(cursor
								.getColumnIndex(SYSTOLIC_UNIT)));
						lifeDetails.setDiastolicUnit(cursor.getString(cursor
								.getColumnIndex(DIASTOLIC_UNIT)));
						lifeDetails.setDateTimeStamp(cursor.getString(cursor
								.getColumnIndex(LT_TIME_STAMP)));
						lifeDetails.setIsSynced(cursor.getString(cursor
								.getColumnIndex(LT_SYNC_STATUS)));
						lifeDetails.setDeviceId(cursor.getString(cursor
								.getColumnIndex(LT_DEVICE_ID)));
						// Adding contact to list
						bloodPressureList.add(lifeDetails);
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}

		return bloodPressureList;

	}

	public ArrayList<Lifetrack_infobean> getBPtDetailsLatestValue(String date) {
		ArrayList<Lifetrack_infobean> bloodPressureList = new ArrayList<Lifetrack_infobean>();
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String sql = "SELECT * FROM " + BPTRACK_TABLE + " WHERE " + LT_DATE + " = ?";
			String[] selectionArgs = new String[]{date};
	
			Cursor cursor = null;
			cursor = db.rawQuery(sql, selectionArgs);
			try {
				if (cursor.moveToFirst()) {
					do {
						Lifetrack_infobean lifeDetails = new Lifetrack_infobean();
						lifeDetails.setKeyidbp(cursor.getInt(cursor
								.getColumnIndex(KEY_ID)));
						lifeDetails.setDate(cursor.getString(cursor
								.getColumnIndex(LT_DATE)));
						lifeDetails.setTime(cursor.getString(cursor
								.getColumnIndex(LT_TIME)));
						lifeDetails.setPulse(cursor.getString(cursor
								.getColumnIndex(PULSE)));
						lifeDetails.setSystolic(cursor.getString(cursor
								.getColumnIndex(SYSTOLIC)));
						lifeDetails.setDiastolic(cursor.getString(cursor
								.getColumnIndex(DIASTOLIC)));
	
						// Adding contact to list
						bloodPressureList.add(lifeDetails);
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				db.close();
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		return bloodPressureList;

	}

	public ArrayList<Lifetrack_infobean> getBPLatestValueMonth(String Month) {
		ArrayList<Lifetrack_infobean> bloodPressureList = new ArrayList<Lifetrack_infobean>();
		SQLiteDatabase db = null;

		try {
			db = this.getWritableDatabase();
			Cursor cursor = null;
			try {
				String sql = "SELECT * FROM " + BPTRACK_TABLE;
				cursor = db.rawQuery(sql, null);
				if (cursor.moveToFirst()) {
					do {
						String[] month = Month.split("-");
						String[] date = cursor
								.getString(cursor.getColumnIndex(LT_DATE)).split("-");
						String currentMonth = month[0] + "-" + month[1];
						String databaseMonth = date[0] + "-" + date[1];
						if (currentMonth.equalsIgnoreCase(databaseMonth)) {
							Lifetrack_infobean lifeDetails = new Lifetrack_infobean();
							lifeDetails.setKeyidbp(cursor.getInt(cursor
									.getColumnIndex(KEY_ID)));
							lifeDetails.setDate(cursor.getString(cursor
									.getColumnIndex(LT_DATE)));
							lifeDetails.setTime(cursor.getString(cursor
									.getColumnIndex(LT_TIME)));
							lifeDetails.setPulse(cursor.getString(cursor
									.getColumnIndex(PULSE)));
							lifeDetails.setSystolic(cursor.getString(cursor
									.getColumnIndex(SYSTOLIC)));
							lifeDetails.setDiastolic(cursor.getString(cursor
									.getColumnIndex(DIASTOLIC)));
		
							// Adding contact to list
							bloodPressureList.add(lifeDetails);
						}
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		return bloodPressureList;
	}

	public ArrayList<Lifetrack_infobean> getBPLatestValueYear(String year) {
		ArrayList<Lifetrack_infobean> bloodPressureList = new ArrayList<Lifetrack_infobean>();
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			Cursor cursor = null;
			try {
				String sql = "SELECT * FROM " + BPTRACK_TABLE;
				cursor = db.rawQuery(sql, null);
				if (cursor.moveToFirst()) {
					do {
						String[] month = year.split("-");
						String[] date = cursor
								.getString(cursor.getColumnIndex(LT_DATE)).split("-");
						String currentMonth = month[0];
						String databaseMonth = date[0];
						if (currentMonth.equalsIgnoreCase(databaseMonth)) {
							Lifetrack_infobean lifeDetails = new Lifetrack_infobean();
							lifeDetails.setKeyidbp(cursor.getInt(cursor
									.getColumnIndex(KEY_ID)));
							lifeDetails.setDate(cursor.getString(cursor
									.getColumnIndex(LT_DATE)));
							lifeDetails.setTime(cursor.getString(cursor
									.getColumnIndex(LT_TIME)));
							lifeDetails.setPulse(cursor.getString(cursor
									.getColumnIndex(PULSE)));
							lifeDetails.setSystolic(cursor.getString(cursor
									.getColumnIndex(SYSTOLIC)));
							lifeDetails.setDiastolic(cursor.getString(cursor
									.getColumnIndex(DIASTOLIC)));

							// Adding contact to list
							bloodPressureList.add(lifeDetails);
						}
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		return bloodPressureList;

	}

	public void bpEntry(ArrayList<Lifetrack_infobean> data) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			ContentValues cv;
			for (int i = 0; i < data.size(); i++) {
				cv = new ContentValues();

				cv.put(LT_DATE, data.get(i).getDate());
				cv.put(LT_TIME, data.get(i).getTime());

				cv.put(PULSE, data.get(i).getPulse());
				cv.put(SYSTOLIC, data.get(i).getSystolic());

				cv.put(DIASTOLIC, data.get(i).getDiastolic());
				cv.put(PULSE_UNIT, data.get(i).getPulseUnit());

				cv.put(SYSTOLIC_UNIT, data.get(i).getSystolicUnit());

				cv.put(DIASTOLIC_UNIT, data.get(i).getDiastolicUnit());
				cv.put(LT_TIME_STAMP, data.get(i).getDateTimeStamp());
				cv.put(LT_DEVICE_ID, data.get(i).getDeviceId());
				cv.put(IRRWGULAR_PULSE_DETECTION, data.get(i).getIrregularPulseDetection());
				
				Cursor cursorTimeStump = null;
				Cursor cursorTime = null;
				try {
					String sql = "SELECT * FROM " + BPTRACK_TABLE + 
							" WHERE " + LT_TIME_STAMP + " = ?";
					String[] selectionArgs = new String[]{data.get(i).getDateTimeStamp()};
					cursorTimeStump = db.rawQuery(sql, selectionArgs);
	
					sql = "SELECT * FROM " + BPTRACK_TABLE + 
							" WHERE " + LT_TIME + " = ?";
					selectionArgs = new String[]{data.get(i).getTime()};
					cursorTime = db.rawQuery(sql, selectionArgs);
	
					if (cursorTimeStump != null && cursorTimeStump.getCount() != 0 
					&&  cursorTime != null && cursorTime.getCount() != 0) {
						Log.d("SN", "BP Data Update !!");
						String whereClause = LT_TIME_STAMP + " = ?";
						String[] whereArgs = new String[]{data.get(i).getDateTimeStamp()};
						db.update(BPTRACK_TABLE, cv, whereClause, whereArgs);
					} else {
						Log.d("SN", "BP Data Insert !!");
						db.insert(BPTRACK_TABLE, null, cv);
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				} finally {
					try {
						if (cursorTimeStump != null) {
							cursorTimeStump.close();
						}
						if (cursorTime != null) {
							cursorTime.close();
						}
					} catch (SQLiteException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
	}

	public void bpEntry_unsync(ArrayList<Lifetrack_infobean> data) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			ContentValues cv;
			for (int i = 0; i < data.size(); i++) {
				cv = new ContentValues();

				cv.put(LT_DATE, data.get(i).getDate());
				cv.put(LT_TIME, data.get(i).getTime());

				cv.put(PULSE, data.get(i).getPulse());
				cv.put(SYSTOLIC, data.get(i).getSystolic());

				cv.put(DIASTOLIC, data.get(i).getDiastolic());
				cv.put(PULSE_UNIT, data.get(i).getPulseUnit());

				cv.put(SYSTOLIC_UNIT, data.get(i).getSystolicUnit());

				cv.put(DIASTOLIC_UNIT, data.get(i).getDiastolicUnit());
				cv.put(LT_TIME_STAMP, data.get(i).getDateTimeStamp());
				cv.put(LT_DEVICE_ID, data.get(i).getDeviceId());
				cv.put(LT_SYNC_STATUS, data.get(i).getIsSynced());

				Cursor cursorTimeStamp = null;
				Cursor cursorTime = null;
				try {
					String sql = "SELECT * FROM " + BPTRACK_TABLE_UNSYNC
							+ " WHERE " + LT_TIME_STAMP + " = ?";
					String[] selectionArgs = new String[]{data.get(i).getDateTimeStamp()};
					cursorTimeStamp = db.rawQuery(sql, selectionArgs);

					sql = "SELECT * FROM " + BPTRACK_TABLE_UNSYNC
							+ " WHERE " + LT_TIME + " = ?";
					selectionArgs = new String[]{data.get(i).getTime()};
					cursorTime = db.rawQuery(sql, selectionArgs);
					if (cursorTimeStamp != null && cursorTimeStamp.getCount() != 0 
					&&  cursorTime != null && cursorTime.getCount() != 0) {
						String whereClause = LT_TIME_STAMP + " = ?";
						String[] whereArgs = new String[]{data.get(i).getDateTimeStamp()};
						db.update(BPTRACK_TABLE_UNSYNC, cv, whereClause, whereArgs);
					} else {
						db.insert(BPTRACK_TABLE_UNSYNC, null, cv);
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				} finally {
					try {
						if (cursorTimeStamp != null) {
							cursorTimeStamp.close();
						}
						if (cursorTime != null) {
							cursorTime.close();
						}
					} catch (SQLiteException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}

	}

	public ArrayList<Lifetrack_infobean> getLifeDetails() {
		ArrayList<Lifetrack_infobean> lifeList = new ArrayList<Lifetrack_infobean>();

		String selectQuery = "SELECT * FROM " + ZLIFETRACK_TABLE;
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst()) {
					do {
						Lifetrack_infobean lifeDetails = new Lifetrack_infobean();
						lifeDetails.setDate(cursor.getString(cursor
								.getColumnIndex(LT_DATE)));
						lifeDetails.setTime(cursor.getString(cursor
								.getColumnIndex(LT_TIME)));
						lifeDetails.setSteps(cursor.getString(cursor
								.getColumnIndex(LT_STEPS)));
						lifeDetails.setCal(cursor.getString(cursor
								.getColumnIndex(LT_CAL)));
						lifeDetails.setDistance(cursor.getString(cursor
								.getColumnIndex(LT_MILES)));
						lifeDetails.setSleep(cursor.getString(cursor
								.getColumnIndex(LT_SLEEP)));
						lifeDetails.setStepsUnits(cursor.getString(cursor
								.getColumnIndex(LT_STEPS_UNITS)));
						lifeDetails.setCalorieUnits(cursor.getString(cursor
								.getColumnIndex(LT_CAL_UNITS)));
						lifeDetails.setDistanceUnit(cursor.getString(cursor
								.getColumnIndex(LT_MILES_UNITS)));
						lifeDetails.setHeartRate(cursor.getString(cursor
								.getColumnIndex(LT_HEART_RATE)));
						lifeDetails.setSleepUnit(cursor.getString(cursor
								.getColumnIndex(LT_SLEEP_UNITS)));
						lifeDetails.setDateTimeStamp(cursor.getString(cursor
								.getColumnIndex(LT_TIME_STAMP)));
						lifeDetails.setDeviceId(cursor.getString(cursor
								.getColumnIndex(LT_DEVICE_ID)));
		
						// Adding contact to list
						lifeList.add(lifeDetails);
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}

		return lifeList;
	}

	public void deletelifetrackRow(String rowId) {
		SQLiteDatabase db = this.getWritableDatabase();
		String whereClause = "date = ?";
		String[] whereArgs = new String[]{rowId};
		db.delete(ZLIFETRACK_TABLE, whereClause, whereArgs);
		db.close();
	}

	public ArrayList<Lifetrack_infobean> getbpDetails() {
		ArrayList<Lifetrack_infobean> lifeList = new ArrayList<Lifetrack_infobean>();

		String selectQuery = "SELECT  * FROM " + BPTRACK_TABLE;
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(selectQuery, null);		
				if (cursor.moveToFirst()) {
					do {
						Lifetrack_infobean lifeDetails = new Lifetrack_infobean();
						lifeDetails.setKeyidbp(cursor.getInt(cursor
								.getColumnIndex(KEY_ID)));
						lifeDetails.setDate(cursor.getString(cursor
								.getColumnIndex(LT_DATE)));
						lifeDetails.setTime(cursor.getString(cursor
								.getColumnIndex(LT_TIME)));
						lifeDetails.setPulse(cursor.getString(cursor
								.getColumnIndex(PULSE)));
						lifeDetails.setSystolic(cursor.getString(cursor
								.getColumnIndex(SYSTOLIC)));
						lifeDetails.setDiastolic(cursor.getString(cursor
								.getColumnIndex(DIASTOLIC)));
						lifeDetails.setPulseUnit(cursor.getString(cursor
								.getColumnIndex(PULSE_UNIT)));
						lifeDetails.setSystolicUnit(cursor.getString(cursor
								.getColumnIndex(SYSTOLIC_UNIT)));
						lifeDetails.setDiastolicUnit(cursor.getString(cursor
								.getColumnIndex(DIASTOLIC_UNIT)));
						lifeDetails.setDateTimeStamp(cursor.getString(cursor
								.getColumnIndex(LT_TIME_STAMP)));
						lifeDetails.setDeviceId(cursor.getString(cursor
								.getColumnIndex(LT_DEVICE_ID)));
						// Adding contact to list
						lifeList.add(lifeDetails);
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		return lifeList;
	}

	/**
	 * うそつきコード
	 * @param CurrentMonth
	 * @return
	 */
	public boolean getbpDetailsbyMonth(String CurrentMonth) {
		boolean checkValueExits = false;

		String selectQuery = "SELECT  * FROM " + BPTRACK_TABLE;
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst()) {
					do {
						checkValueExits = true;
						// Adding contact to list
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		return checkValueExits;
	}

	public ArrayList<Lifetrack_infobean> getAcivityDetails(String date) {
		ArrayList<Lifetrack_infobean> lifeList = new ArrayList<Lifetrack_infobean>();
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String sql = "SELECT * FROM " + ZLIFETRACK_TABLE + " WHERE LT_DATE = ?";
			String[] selectionArgs = new String[]{date};
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql, selectionArgs);
				if (cursor.moveToFirst()) {
					do {
						Lifetrack_infobean lifeDetails = new Lifetrack_infobean();
						lifeDetails.setDate(cursor.getString(cursor
								.getColumnIndex(LT_DATE)));
						lifeDetails.setTime(cursor.getString(cursor
								.getColumnIndex(LT_TIME)));
						lifeDetails.setSteps(cursor.getString(cursor
								.getColumnIndex(LT_STEPS)));
						lifeDetails.setCal(cursor.getString(cursor
								.getColumnIndex(LT_CAL)));
						lifeDetails.setDistance(cursor.getString(cursor
								.getColumnIndex(LT_MILES)));
						lifeDetails.setSleep(cursor.getString(cursor
								.getColumnIndex(LT_SLEEP)));
						lifeDetails.setStepsUnits(cursor.getString(cursor
								.getColumnIndex(LT_STEPS_UNITS)));
						lifeDetails.setCalorieUnits(cursor.getString(cursor
								.getColumnIndex(LT_CAL_UNITS)));
						lifeDetails.setDistanceUnit(cursor.getString(cursor
								.getColumnIndex(LT_MILES_UNITS)));
						lifeDetails.setHeartRate(cursor.getString(cursor
								.getColumnIndex(LT_HEART_RATE)));
						lifeDetails.setSleepUnit(cursor.getString(cursor
								.getColumnIndex(LT_SLEEP_UNITS)));
						lifeDetails.setDateTimeStamp(cursor.getString(cursor
								.getColumnIndex(LT_TIME_STAMP)));

						lifeDetails.setDeviceId(cursor.getString(cursor
								.getColumnIndex(LT_DEVICE_ID)));
						// Adding contact to list
						lifeList.add(lifeDetails);
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}

		return lifeList;

	}

	// retrieves all the entries
	// public Cursor

	public Lifetrack_infobean getLatestActivityDateTime() {
		Lifetrack_infobean lifeDetails = null;
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String sql = "SELECT * FROM " + ZLIFETRACK_TABLE + " WHERE " + KEY_ID  + " = ?";
			String[] selectionArgs = new String[]{"(SELECT MAX(_id))"};
	
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql, selectionArgs);
				//		Cursor cursor = db
				//				.rawQuery(
				//						"SELECT * from aandmedical_lifetrack Where _id = (SELECT MAX(_id))",
				//						null);
	
				if (cursor.moveToFirst()) {
					do {
						lifeDetails = new Lifetrack_infobean();
						lifeDetails.setDate(cursor.getString(cursor
								.getColumnIndex(LT_DATE)));
						lifeDetails.setTime(cursor.getString(cursor
								.getColumnIndex(LT_TIME)));
						lifeDetails.setSteps(cursor.getString(cursor
								.getColumnIndex(LT_STEPS)));
						lifeDetails.setCal(cursor.getString(cursor
								.getColumnIndex(LT_CAL)));
						lifeDetails.setDistance(cursor.getString(cursor
								.getColumnIndex(LT_MILES)));
						lifeDetails.setSleep(cursor.getString(cursor
								.getColumnIndex(LT_SLEEP)));
						lifeDetails.setStepsUnits(cursor.getString(cursor
								.getColumnIndex(LT_STEPS_UNITS)));
						lifeDetails.setCalorieUnits(cursor.getString(cursor
								.getColumnIndex(LT_CAL_UNITS)));
						lifeDetails.setDistanceUnit(cursor.getString(cursor
								.getColumnIndex(LT_MILES_UNITS)));
						lifeDetails.setSleepUnit(cursor.getString(cursor
								.getColumnIndex(LT_SLEEP_UNITS)));
						lifeDetails.setDateTimeStamp(cursor.getString(cursor
								.getColumnIndex(LT_TIME_STAMP)));
		
						lifeDetails.setDeviceId(cursor.getString(cursor
								.getColumnIndex(LT_DEVICE_ID)));
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}

		return lifeDetails;
	}

	public Lifetrack_infobean getLatestBPDateTime() {
		Lifetrack_infobean bloodPressureDetails = null;
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String sql = "SELECT * FROM " + BPTRACK_TABLE + " WHERE " + KEY_ID + " = ?";
			String[] selectionArgs = new String[]{"(SELECT MAX(_id))"};
	
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql, selectionArgs);
				// 現状動かせる仕組みがないのでコードは残しておく
				//		Cursor cursor = db
				//				.rawQuery(
				//						"SELECT * from aandmedical_bptrack Where _id = (SELECT MAX(_id))",
				//						null);
				
				if (cursor.moveToFirst()) {
					do {
						bloodPressureDetails = new Lifetrack_infobean();
						bloodPressureDetails.setDate(cursor.getString(cursor
								.getColumnIndex(LT_DATE)));
						bloodPressureDetails.setTime(cursor.getString(cursor
								.getColumnIndex(LT_TIME)));
						bloodPressureDetails.setPulse(cursor.getString(cursor
								.getColumnIndex(PULSE)));
						bloodPressureDetails.setSystolic(cursor.getString(cursor
								.getColumnIndex(SYSTOLIC)));
						bloodPressureDetails.setDiastolic(cursor.getString(cursor
								.getColumnIndex(DIASTOLIC)));
						bloodPressureDetails.setPulseUnit(cursor.getString(cursor
								.getColumnIndex(PULSE_UNIT)));
						bloodPressureDetails.setSystolicUnit(cursor.getString(cursor
								.getColumnIndex(SYSTOLIC_UNIT)));
						bloodPressureDetails.setDiastolicUnit(cursor.getString(cursor
								.getColumnIndex(DIASTOLIC_UNIT)));
						bloodPressureDetails.setDateTimeStamp(cursor.getString(cursor
								.getColumnIndex(LT_TIME_STAMP)));

						bloodPressureDetails.setDeviceId(cursor.getString(cursor
								.getColumnIndex(LT_DEVICE_ID)));
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}

		return bloodPressureDetails;
	}

	public Lifetrack_infobean getLatestWeightDateTime() {
		Lifetrack_infobean weightDetails = null;
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String sql = "SELECT * FROM " + ZWEIGHTTRACK_TABLE + "WHERE " + KEY_ID + " = ?";
			String[] selectionArgs = new String[]{"(SELECT MAX(_id))"};

			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql, selectionArgs);
				// 現状動かせる仕組みがないのでコードは残しておく
				//		Cursor cursor = db
				//				.rawQuery(
				//						"SELECT * from aandmedical_weighttrack Where _id = (SELECT MAX(_id))",
				//						null);

				if (cursor.moveToFirst()) {
					do {
						weightDetails = new Lifetrack_infobean();
						weightDetails.setDate(cursor.getString(cursor
								.getColumnIndex(LT_DATE)));
						weightDetails.setWeight(cursor.getString(cursor
								.getColumnIndex(WT_WEIGHT)));
						weightDetails.setTime(cursor.getString(cursor
								.getColumnIndex(LT_TIME)));
						weightDetails.setWeightUnit(cursor.getString(cursor
								.getColumnIndex(WT_WEIGHT_UNITS)));
						weightDetails.setDateTimeStamp(cursor.getString(cursor
								.getColumnIndex(LT_TIME_STAMP)));
						weightDetails.setDeviceId(cursor.getString(cursor
								.getColumnIndex(LT_DEVICE_ID)));

					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		return weightDetails;
	}

	public String getWeightDetails(String date) {
		ArrayList<Lifetrack_infobean> weightList = new ArrayList<Lifetrack_infobean>();
		String lastweight = "";
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String sql = "SELECT * FROM " + ZWEIGHTTRACK_TABLE + " WHERE " + LT_DATE + " = ?";
			String[] selectionArgs = new String[]{date};

			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql, selectionArgs);
				// 現状動かせる仕組みがないのでコードは残しておく
				//		Cursor cursor = db.rawQuery(
				//		"SELECT * from aandmedical_weighttrack Where "
				//		+ "(date= '" + date + "')", null);

				if (cursor.moveToFirst()) {
					do {
						Lifetrack_infobean weightDetails = new Lifetrack_infobean();
						weightDetails.setDate(cursor.getString(cursor
								.getColumnIndex(LT_DATE)));
						weightDetails.setWeight(cursor.getString(cursor
								.getColumnIndex(WT_WEIGHT)));
						weightDetails.setTime(cursor.getString(cursor
								.getColumnIndex(LT_TIME)));
						weightDetails.setWeightUnit(cursor.getString(cursor
								.getColumnIndex(WT_WEIGHT_UNITS)));
						weightDetails.setDateTimeStamp(cursor.getString(cursor
								.getColumnIndex(LT_TIME_STAMP)));
						weightDetails.setDeviceId(cursor.getString(cursor
								.getColumnIndex(LT_DEVICE_ID)));

						lastweight = cursor.getString(cursor.getColumnIndex(WT_WEIGHT));
						// Adding contact to list
						weightList.add(weightDetails);
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		return lastweight;
	}

	public ArrayList<Lifetrack_infobean> getlstWeightDetails(String date) {
		ArrayList<Lifetrack_infobean> weightList = new ArrayList<Lifetrack_infobean>();
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String sql = "SELECT * FROM " + ZWEIGHTTRACK_TABLE + " WHERE " + LT_DATE + " = ?";
			String[] selectionArgs = new String[]{date};
			
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql, selectionArgs);
				if (cursor.moveToFirst()) {
					do {
						Lifetrack_infobean weightDetails = new Lifetrack_infobean();
						weightDetails.setDate(cursor.getString(cursor
								.getColumnIndex(LT_DATE)));
						weightDetails.setWeight(cursor.getString(cursor
								.getColumnIndex(WT_WEIGHT)));
						weightDetails.setTime(cursor.getString(cursor
								.getColumnIndex(LT_TIME)));
						weightDetails.setWeightUnit(cursor.getString(cursor
								.getColumnIndex(WT_WEIGHT_UNITS)));
						weightDetails.setDateTimeStamp(cursor.getString(cursor
								.getColumnIndex(LT_TIME_STAMP)));
						weightDetails.setDeviceId(cursor.getString(cursor
								.getColumnIndex(LT_DEVICE_ID)));
		
						weightList.add(weightDetails);
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					 if (cursor != null) {
						 cursor.close();
					 }
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		return weightList;
	}

	public ArrayList<Lifetrack_infobean> getAllWeightDetails() {
		ArrayList<Lifetrack_infobean> weightList = new ArrayList<Lifetrack_infobean>();

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String sql = "SELECT * FROM " + ZWEIGHTTRACK_TABLE + " ORDER BY " + LT_DATE;
			
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql, null);
				if (cursor.moveToFirst()) {
					do {
						Lifetrack_infobean weightDetails = new Lifetrack_infobean();
						weightDetails.setKeyidweight(cursor.getInt(cursor
								.getColumnIndex(KEY_ID)));
						weightDetails.setDate(cursor.getString(cursor
								.getColumnIndex(LT_DATE)));
						weightDetails.setWeight(cursor.getString(cursor
								.getColumnIndex(WT_WEIGHT)));
						weightDetails.setTime(cursor.getString(cursor
								.getColumnIndex(LT_TIME)));
						weightDetails.setWeightUnit(cursor.getString(cursor
								.getColumnIndex(WT_WEIGHT_UNITS)));
						weightDetails.setDateTimeStamp(cursor.getString(cursor
								.getColumnIndex(LT_TIME_STAMP)));
						weightDetails.setDeviceId(cursor.getString(cursor
								.getColumnIndex(LT_DEVICE_ID)));
	
						weightList.add(weightDetails);
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		return weightList;
	}

	// ..............................Get blood pressure
	// details.....................

	public ArrayList<Lifetrack_infobean> getlstBloodDetails(String date) {
		ArrayList<Lifetrack_infobean> bloodPressureList = new ArrayList<Lifetrack_infobean>();

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String sql = "SELECT * FROM " + BPTRACK_TABLE + " WHERE " + LT_DATE + " = ? ";
			String[] selectionArgs = new String[]{date};
	
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql, selectionArgs);
				if (cursor.moveToFirst()) {
					do {
						Lifetrack_infobean bloodPressureDetails = new Lifetrack_infobean();
	
						bloodPressureDetails.setDate(cursor.getString(cursor
								.getColumnIndex(LT_DATE)));
						bloodPressureDetails.setTime(cursor.getString(cursor
								.getColumnIndex(LT_TIME)));
						bloodPressureDetails.setPulse(cursor.getString(cursor
								.getColumnIndex(PULSE)));
						bloodPressureDetails.setSystolic(cursor.getString(cursor
								.getColumnIndex(SYSTOLIC)));
						bloodPressureDetails.setDiastolic(cursor.getString(cursor
								.getColumnIndex(DIASTOLIC)));
						bloodPressureDetails.setDateTimeStamp(cursor.getString(cursor
								.getColumnIndex(LT_TIME_STAMP)));
						bloodPressureDetails.setDeviceId(cursor.getString(cursor
								.getColumnIndex(LT_DEVICE_ID)));
		
						bloodPressureList.add(bloodPressureDetails);
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		return bloodPressureList;
	}

	public ArrayList<Lifetrack_infobean> getAllBloodDetails() {
		ArrayList<Lifetrack_infobean> bloodPressureList = new ArrayList<Lifetrack_infobean>();

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String sql = "SELECT * FROM " + BPTRACK_TABLE;
			Cursor cursor = null;
			try {	
				cursor = db.rawQuery(sql, null);
				if (cursor.moveToFirst()) {
					do {
						Lifetrack_infobean bloodPressureDetails = new Lifetrack_infobean();
						bloodPressureDetails.setDate(cursor.getString(cursor
								.getColumnIndex(LT_DATE)));
						bloodPressureDetails.setTime(cursor.getString(cursor
								.getColumnIndex(LT_TIME)));
						bloodPressureDetails.setPulse(cursor.getString(cursor
								.getColumnIndex(PULSE)));
						bloodPressureDetails.setSystolic(cursor.getString(cursor
								.getColumnIndex(SYSTOLIC)));
						bloodPressureDetails.setDateTimeStamp(cursor.getString(cursor
								.getColumnIndex(LT_TIME_STAMP)));
						bloodPressureDetails.setDeviceId(cursor.getString(cursor
								.getColumnIndex(LT_DEVICE_ID)));
						// Adding contact to list
						bloodPressureList.add(bloodPressureDetails);
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		return bloodPressureList;
	}

	public ArrayList<Lifetrack_infobean> getAllActivityDetails() {
		ArrayList<Lifetrack_infobean> lifeTrackList = new ArrayList<Lifetrack_infobean>();

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String sql = "SELECT * FROM " + ZLIFETRACK_TABLE;
	
			Cursor cursor = null;
			try {
				
				cursor = db.rawQuery(sql, null);
				if (cursor.moveToFirst()) {
					do {
						Lifetrack_infobean lifeTrackDetails = new Lifetrack_infobean();
						lifeTrackDetails.setDate(cursor.getString(cursor
								.getColumnIndex(LT_DATE)));
						lifeTrackDetails.setTime(cursor.getString(cursor
								.getColumnIndex(LT_TIME)));
						lifeTrackDetails.setSteps(cursor.getString(cursor
								.getColumnIndex(LT_STEPS)));
						lifeTrackDetails.setCal(cursor.getString(cursor
								.getColumnIndex(LT_CAL)));
						lifeTrackDetails.setDistance(cursor.getString(cursor
								.getColumnIndex(LT_MILES)));
						lifeTrackDetails.setSleep(cursor.getString(cursor
								.getColumnIndex(LT_SLEEP)));
						lifeTrackDetails.setStepsUnits(cursor.getString(cursor
								.getColumnIndex(LT_STEPS_UNITS)));
						lifeTrackDetails.setCalorieUnits(cursor.getString(cursor
								.getColumnIndex(LT_CAL_UNITS)));
						lifeTrackDetails.setDistanceUnit(cursor.getString(cursor
								.getColumnIndex(LT_MILES_UNITS)));
						lifeTrackDetails.setSleepUnit(cursor.getString(cursor
								.getColumnIndex(LT_SLEEP_UNITS)));
		
						lifeTrackDetails.setHeartRate(cursor.getString(cursor
								.getColumnIndex(LT_HEART_RATE)));
						lifeTrackDetails.setDistanceInMiles(cursor.getString(cursor
								.getColumnIndex(LT_DISTANCE_MILES)));
						lifeTrackDetails.setDateTimeStamp(cursor.getString(cursor
								.getColumnIndex(LT_TIME_STAMP)));
						lifeTrackDetails.setDeviceId(cursor.getString(cursor
								.getColumnIndex(LT_DEVICE_ID)));
						// Adding contact to list
						lifeTrackList.add(lifeTrackDetails);
					} while (cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
					}
				} catch (SQLiteException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
			
		}

		return lifeTrackList;
	}

	// retrieves all the entries
	public Cursor getAllEntries_Weighttracker() {
		Cursor cursor = db.query(ZWEIGHTTRACK_TABLE, new String[] { KEY_ID,
				LT_DATE, WT_WEIGHT }, null, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}

	public void deletebloodpressureRow(int i) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			db.delete(BPTRACK_TABLE, KEY_ID + " = ?",
					new String[] { String.valueOf(i) });
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteweightscaleRow(int i) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			db.delete(ZWEIGHTTRACK_TABLE, KEY_ID + " = ?",
					new String[] { String.valueOf(i) });
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
	}

	public void deletereminder(int i) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			db.delete(REMINDER_TABLE, KEY_ID + " = ?",
					new String[] { String.valueOf(i) });
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
	}

	public void updatereminder(int id, String toggleValue) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			ContentValues cv;
			cv = new ContentValues();
			cv.put(REMINDER_SNOOZE, toggleValue);
			db.update(REMINDER_TABLE, cv, KEY_ID + " = '" + id + "'", null);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				db.close();
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}

	}

}
