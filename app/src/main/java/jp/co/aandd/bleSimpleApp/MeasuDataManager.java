package jp.co.aandd.bleSimpleApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jp.co.aandd.bleSimpleApp.entities.DataBase;
import jp.co.aandd.bleSimpleApp.entities.Lifetrack_infobean;
import jp.co.aandd.bleSimpleApp.utilities.ADSharedPreferences;
import jp.co.aandd.bleSimpleApp.utilities.ANDMedicalUtilities;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;


public class MeasuDataManager {
	
	/** Tips:省略文字について
	 * AM -> Activity Monitor (活動量計)
	 * BP -> Blood Presure (血圧計)
	 * WS -> Weight Scale (体重計)
	 * Disp -> Display
	 */
	
	public static final int MEASU_DATA_TYPE_UNKNOW = -1;
	// 活動量計
	public static final int MEASU_DATA_TYPE_AM = 0;
	// 血圧計
	public static final int MEASU_DATA_TYPE_BP = 1;
	// 体重計
	public static final int MEASU_DATA_TYPE_WS = 2;
	
	public static final String ACTION_AM_DATA_UPDATE = "com.andmedical.action_am_data_update";
	public static final String ACTION_BP_DATA_UPDATE = "com.andmedical.action_bp_data_update";
	public static final String ACTION_WS_DATA_UPDATE = "com.andmedical.action_ws_data_update";
	
	public static IntentFilter MeasuDataUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_AM_DATA_UPDATE);
		intentFilter.addAction(ACTION_BP_DATA_UPDATE);
		intentFilter.addAction(ACTION_WS_DATA_UPDATE);
		return intentFilter;
	}
	
	private int[] dataTypes = {
			MEASU_DATA_TYPE_AM,
			MEASU_DATA_TYPE_BP,
			MEASU_DATA_TYPE_WS
	};
	
	private Context mContext;
	private DataBase mDataBase;
	private ArrayList<Lifetrack_infobean> mAmDataList;
	private ArrayList<Lifetrack_infobean> mBpDataList;
	private ArrayList<Lifetrack_infobean> mWsDataList;
	private Lifetrack_infobean dispDataAM;
	private Lifetrack_infobean dispDataBP;
	private Lifetrack_infobean dispDataWS;
	
	public MeasuDataManager(Context context) {
		super();
		mContext = context;
		
		String userName = ADSharedPreferences.getString(ADSharedPreferences.KEY_LOGIN_USER_NAME, "");
		changeUser(userName);
	}

	private void changeUser(String userName) {
		mDataBase = new DataBase(mContext, userName);
	}

	public void syncAllMeasuDatas(boolean isSendBroadcast) {
		if(mDataBase == null) {
			return ;
		}
		
		if (ANDMedicalUtilities.APP_STAND_ALONE_MODE) {
			setDispMeasuData(MEASU_DATA_TYPE_AM, null);
			new SelectDataFromDatabase(MEASU_DATA_TYPE_AM, isSendBroadcast, null).execute();
			setDispMeasuData(MEASU_DATA_TYPE_BP, null);
			new SelectDataFromDatabase(MEASU_DATA_TYPE_BP, isSendBroadcast, null).execute();
			setDispMeasuData(MEASU_DATA_TYPE_WS, null);
			new SelectDataFromDatabase(MEASU_DATA_TYPE_WS, isSendBroadcast, null).execute();
		}
		else {
			// to Future
		}
	}
	
	public void syncMeasudata(int dataType, boolean isSendBroadcast) {
		syncMeasudata(dataType, isSendBroadcast, null);
	}
	
	public void syncMeasudata(int dataType, boolean isSendBroadcast,MeasureDataSyncListener listener) {
		if(mDataBase == null) {
			return ;
		}
		
		if (ANDMedicalUtilities.APP_STAND_ALONE_MODE) {
			new SelectDataFromDatabase(dataType, isSendBroadcast, listener).execute();
		}
		else {
			// to Future
		}
	}
	
	public static ArrayList<Lifetrack_infobean> sortDate(ArrayList<Lifetrack_infobean> list, boolean isAsc) {
		
		if(list == null || list.size() < 0) {
			return new ArrayList<Lifetrack_infobean>();
		}
		
		Collections.sort(list,(isAsc)? comparatorListAcs : comparatorListDesc);
		
		return list;
	}
	
	private static Comparator<Lifetrack_infobean> comparatorListAcs = new Comparator<Lifetrack_infobean>() {
		final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		@Override
		public int compare(Lifetrack_infobean lhs, Lifetrack_infobean rhs) {
			int j = 0;
			try {
				j = df.parse(lhs.getDate() + "T" + lhs.getTime())
						.compareTo(df.parse(rhs.getDate() + "T" + rhs.getTime()));
			}
			catch (ParseException e) {
				e.printStackTrace();
			}
			return j;
		}
	};
	
	private static Comparator<Lifetrack_infobean> comparatorListDesc = new Comparator<Lifetrack_infobean>() {
		final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		@Override
		public int compare(Lifetrack_infobean lhs, Lifetrack_infobean rhs) {
			int j = 0;
			try {
				j = df.parse(rhs.getDate() + "T" + rhs.getTime())
						.compareTo(df.parse(lhs.getDate() + "T" + lhs.getTime()));
			}
			catch (ParseException e) {
				e.printStackTrace();
			}
			return j;
		}
	};

	private class SelectDataFromDatabase extends AsyncTask<Void, Void, Void> {

		private int mSelectDataType = MEASU_DATA_TYPE_UNKNOW;
		private boolean mIsExecuteBroadcast = true;
		private ArrayList<Lifetrack_infobean> mList;
		private MeasureDataSyncListener mSyncListener;
		
		public SelectDataFromDatabase(int dataType, boolean isExecuteBroadcast, MeasureDataSyncListener listener) {
			super();
			mSelectDataType = dataType;
			mIsExecuteBroadcast = isExecuteBroadcast;
			mSyncListener = listener;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			if(mSyncListener != null) {
				mSyncListener.onSyncStart(mSelectDataType);
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			if(mSelectDataType == MEASU_DATA_TYPE_UNKNOW) {
				// to do nothing
			}
			else if(mSelectDataType == MEASU_DATA_TYPE_AM) {
				mList = sortDate(mDataBase.getAllActivityDetails(),true);
			}
			else if(mSelectDataType == MEASU_DATA_TYPE_BP) {
				mList = sortDate(mDataBase.getbpDetails(),true);
			}
			else if(mSelectDataType == MEASU_DATA_TYPE_WS) {
				mList = sortDate(mDataBase.getAllWeightDetails(),true);
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			String action = null;
			
			if(mSelectDataType == MEASU_DATA_TYPE_UNKNOW) {
				// to do nothing
				return ;
			}
			else if(mSelectDataType == MEASU_DATA_TYPE_AM) {
				setAmDataList(mList);
				action = ACTION_AM_DATA_UPDATE;
			}
			else if(mSelectDataType == MEASU_DATA_TYPE_BP) {
				setBpDataList(mList);
				action = ACTION_BP_DATA_UPDATE;
			}
			else if(mSelectDataType == MEASU_DATA_TYPE_WS) {
				setWsDataList(mList);
				action = ACTION_WS_DATA_UPDATE;
			}
			
			if(mList != null && mList.size() > 0) {
				// 初期値は最新のデータを表示
				setDispMeasuData(mSelectDataType, mList.get(mList.size()-1));
			}
			
			if(mIsExecuteBroadcast) {
				sendBroadcast(action);
			}
			
			if(mSyncListener != null) {
				mSyncListener.onSyncEnd(mSelectDataType, (mList != null));
			}
		}
	}
	
	public interface MeasureDataSyncListener {
		public void onSyncStart(int type);
		public void onSyncEnd(int type,boolean result);
	}
	
	private void sendBroadcast(String action) {
		if(action == null) {
			return;
		}
		Intent intent = new Intent(action);
		mContext.sendBroadcast(intent);
	}

	public ArrayList<Lifetrack_infobean> getBpDataList() {
		if(mBpDataList == null) {
			mBpDataList = new ArrayList<Lifetrack_infobean>();
		}
		return mBpDataList;
	}

	private void setBpDataList(ArrayList<Lifetrack_infobean> mBpDataList) {
		this.mBpDataList = mBpDataList;
	}

	public ArrayList<Lifetrack_infobean> getWsDataList() {
		if(mWsDataList == null) {
			mWsDataList = new ArrayList<Lifetrack_infobean>();
		}
		return mWsDataList;
	}

	private void setWsDataList(ArrayList<Lifetrack_infobean> mWsDataList) {
		this.mWsDataList = mWsDataList;
	}
	
	public ArrayList<Lifetrack_infobean> getAmDataList() {
		if(mAmDataList == null) {
			mAmDataList = new ArrayList<Lifetrack_infobean>();
		}
		return mAmDataList;
	}

	private void setAmDataList(ArrayList<Lifetrack_infobean> mAmDataList) {
		this.mAmDataList = mAmDataList;
	}
	
	/**
	 * Get Measure Data List
	 * @param dataType
	 * @return List
	 */
	private synchronized ArrayList<Lifetrack_infobean> getMeasuDataList(int dataType) {
		if(dataType == MEASU_DATA_TYPE_AM) {
			return getAmDataList();
		}
		else if(dataType == MEASU_DATA_TYPE_BP) {
			return getBpDataList();
		}
		else if(dataType == MEASU_DATA_TYPE_WS) {
			return getWsDataList();
		}
		
		return null;
	}
	
	/**
	 * Set Display MainDashboard Data
	 * @param dataType
	 * @param data
	 */
	private synchronized void setDispMeasuData(int dataType, Lifetrack_infobean data) {
		if(dataType == MEASU_DATA_TYPE_AM) {
			dispDataAM = data;
		}
		else if(dataType == MEASU_DATA_TYPE_BP) {
			dispDataBP = data;
		}
		else if(dataType == MEASU_DATA_TYPE_WS) {
			dispDataWS = data;
		}
	}
	
	/**
	 * Get Display MainDashboard Data
	 * @param dataType
	 * @return data
	 */
	private Lifetrack_infobean getDispMeasuData(int dataType) {
		if(dataType == MEASU_DATA_TYPE_AM) {
			return dispDataAM;
		}
		else if(dataType == MEASU_DATA_TYPE_BP) {
			return dispDataBP;
		}
		else if(dataType == MEASU_DATA_TYPE_WS) {
			return dispDataWS;
		}
		
		return null;
	}
	
	/**
	 * Get Current Display Data
	 * @param dataType
	 * @return
	 */
	public Lifetrack_infobean getCurrentDispData(int dataType) {
		
		if(dataType == MEASU_DATA_TYPE_UNKNOW) {
			return null;
		}
		
		ArrayList<Lifetrack_infobean> list = getMeasuDataList(dataType);
		
		if(list == null || list.isEmpty()) {
			return null;
		}
		
		Lifetrack_infobean data = getDispMeasuData(dataType);
		if(data != null) {
			return data;
		}
		else {
			// 初期値は最新のデータを表示
			data = list.get(list.size()-1);
			setDispMeasuData(dataType, data);
			return data;
		}
	}
	
	/**
	 * Datas is Exist FutureData
	 * @return true Exist Future Data. false Not Exist Future Data
	 */
	public boolean isExistFutureDatas() {
		
		boolean isShow = false;
		
		for(int index = 0 ; index < dataTypes.length ; index++) {
			int dataType = dataTypes[index];
			Lifetrack_infobean data = getFutureData(dataType);
			if(data != null) {
				isShow = true;
			}
		}
		
		return isShow;
	}
	
	/**
	 * Datas is Exist PastData
	 * @return true Exist Past Data. false Not Exist Past Data
	 */
	public boolean isExistPastDatas() {
		
		boolean isShow = false;
		
		for(int index = 0 ; index < dataTypes.length ; index++) {
			int dataType = dataTypes[index];
			Lifetrack_infobean data = getPastData(dataType);
			if(data != null) {
				isShow = true;
			}
		}
		
		return isShow;
	}
	
	/**
	 * Get Future Data(than CurrentDispData)
	 * @param dataType
	 * @return Future Data
	 */
	private Lifetrack_infobean getFutureData(int dataType) {
		Lifetrack_infobean currentData = getCurrentDispData(dataType);
		
		if(currentData == null) {
			return null;
		}
		
		List<Lifetrack_infobean> list = getMeasuDataList(dataType);
		if(list == null) {
			return null;
		}
		
		int index = list.indexOf(currentData);
		// to the future
		index++;
		if(list.size() <= index || !list.contains(currentData)) {
			return null;
		}

		return list.get(index);
	}
	
	/**
	 * Get Past Data(than CurrentDispData)
	 * @param dataType dataType
	 * @return Past Data
	 */
	private Lifetrack_infobean getPastData(int dataType) {
		
		Lifetrack_infobean currentData = getCurrentDispData(dataType);
		if(currentData == null) {
			return null;
		}
		
		List<Lifetrack_infobean> list = getMeasuDataList(dataType);
		if(list == null) {
			return null;
		}
		
		int index = list.indexOf(currentData);
		
		if(index == -1) {
			for(Lifetrack_infobean item : list) {
				if(item.toString().equals(currentData.toString())) {
					index = list.indexOf(item);
					break;
				}
			}
		}
		
		// to the Past
		index--;
		if(index < 0 || !list.contains(currentData)) {
			return null;
		}

		return list.get(index);
	}
	
	/**
	 * Move DisplayDatas Future
	 */
	public void moveDatasToTheFuture() {
		for(int index = 0 ; index < dataTypes.length ; index++) {
			int dataType = dataTypes[index];
			Lifetrack_infobean data = getFutureData(dataType);
			if(data != null) {
				setDispMeasuData(dataType, data);
			}
		}
	}
	
	/**
	 * Move DisplayDatas Past
	 */
	public void moveDatasToThePast() {
		for(int index = 0 ; index < dataTypes.length ; index++) {
			int dataType = dataTypes[index];
			Lifetrack_infobean data = getPastData(dataType);
			if(data != null) {
				setDispMeasuData(dataType, data);
			}
		}
	}
}
