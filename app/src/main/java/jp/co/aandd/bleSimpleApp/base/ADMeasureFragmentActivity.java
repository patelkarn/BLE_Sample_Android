package jp.co.aandd.bleSimpleApp.base;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

import jp.co.aandd.bleSimpleApp.entities.Lifetrack_infobean;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;


/**
 * 測定値ダッシュボード 基底クラス
 */
public abstract class ADMeasureFragmentActivity extends FragmentActivity {
	
	/**
	 * Get Measure Datas
	 * @return Measure Datas
	 */
	abstract protected ArrayList<Lifetrack_infobean> getMeasureDataList();

	/**
	 * Display ChartType
	 * Default Dayliy Graph
	 */
	protected int chartType = CHART_TYPE_DAILY;
	protected static final int CHART_TYPE = 100;
	protected static final int CHART_TYPE_DAILY = CHART_TYPE + 1;
	protected static final int CHART_TYPE_WEEKLY = CHART_TYPE + 2;
	protected static final int CHART_TYPE_MONTHLY = CHART_TYPE+ 3;
	
	/**
	 * Display ViewPosition
	 * Default GraphView
	 */
	protected int currentTabPosition = TAB_POSITION_READING;
	protected static final int TAB_POSITION = 200;
	protected static final int TAB_POSITION_READING = TAB_POSITION + 1;
	protected static final int TAB_POSITION_HISTORY = TAB_POSITION + 2;
	
	/**
	 * Measure Datas
	 * Separate Each ChartType
	 */
	protected HashMap<String, ArrayList<Lifetrack_infobean>> dayHashMap = new HashMap<String, ArrayList<Lifetrack_infobean>>();
	protected HashMap<String, ArrayList<Lifetrack_infobean>> weekHashMap = new HashMap<String, ArrayList<Lifetrack_infobean>>();
	protected HashMap<String, ArrayList<Lifetrack_infobean>> monthHashMap = new HashMap<String, ArrayList<Lifetrack_infobean>>();
	
	/**
	 * Selected MeasureData Map Key
	 */
	protected String displayDayKey;
	protected String displayWeekKey;
	protected String displayMonthKey;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupDatas();
	}
	
	@Override
	public void onBackPressed() {
		
		if(currentTabPosition == TAB_POSITION_HISTORY) {
			// If Display ListView
			// Show GraphView
			selectTab(TAB_POSITION_READING);
			return;
		}
		
		super.onBackPressed();
		finish();
	}
	
	/**
	 * Regist MeasureData Refresh Receiver
	 * @param filter
	 */
	protected void registMeasureDataRefreshReceiver(IntentFilter filter) {
		registerReceiver(mMeasudataUpdateReceiver, filter);
	}
	
	/**
	 * Unregist MeasureData Refresh Receiver
	 */
	protected void unregistMeasureDataRefreshReceive() {
		unregisterReceiver(mMeasudataUpdateReceiver);
	}
	
	/**
	 * MeasureData Refresh Receiver
	 */
	private final BroadcastReceiver mMeasudataUpdateReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			updateMeasuDatas(context, intent);
		}
	};
	
	/**
	 * Update Measure Datas
	 * @param context
	 * @param intent
	 */
	protected void updateMeasuDatas(Context context, Intent intent) {
		
		reloadDatas();
		
		new Handler(getMainLooper()).post(new Runnable() {
			
			@Override
			public void run() {
				onChangeGraphView(getSelectedDateList(),null);
			}
		});
	}
	
	/**
	 * Refresh Arrows Visisble Status
	 */
	protected void refreshArrows() {
		
		onChangeRightArrowVisible(View.INVISIBLE);
		onChangeLeftArrowVisible(View.INVISIBLE);
		
		HashMap<String, ArrayList<Lifetrack_infobean>> map;
		String disKey;
		
		if (chartType == CHART_TYPE_DAILY) {
			map = dayHashMap;
			disKey = getDisplayDayKey();
		}
		else if (chartType == CHART_TYPE_WEEKLY) {
			map = weekHashMap;
			disKey = getDisplayWeekKey();
		} 
		else if (chartType == CHART_TYPE_MONTHLY) {
			map = monthHashMap;
			disKey = getDisplayMonthKey();
		}
		else {
			// to do Nothing
			return ;
		}

		ArrayList<String> keys = getSortDateKeys(map);
		
		if(keys.contains(disKey)) {
			int index = keys.indexOf(disKey);
			
			if(keys.size() <= 1) {
				onChangeRightArrowVisible(View.INVISIBLE);
				onChangeLeftArrowVisible(View.INVISIBLE);
			}
			else if(index == 0) {
				onChangeRightArrowVisible(View.VISIBLE);
				onChangeLeftArrowVisible(View.INVISIBLE);
			}
			else if(index == (map.size() -1)) {
				onChangeRightArrowVisible(View.INVISIBLE);
				onChangeLeftArrowVisible(View.VISIBLE);
			}
			else {
				onChangeRightArrowVisible(View.VISIBLE);
				onChangeLeftArrowVisible(View.VISIBLE);
			}
		}
		else {
			onChangeRightArrowVisible(View.INVISIBLE);
			onChangeLeftArrowVisible(View.INVISIBLE);
		}
	}
	
	/**
	 * Select Tab( Visible View Type )
	 * @param tabpositionTye
	 */
	protected void selectTab(int tabpositionTye) {

		if(tabpositionTye == TAB_POSITION_READING) {
			
			currentTabPosition = TAB_POSITION_READING;
			
			onChangeGraphViewVisible(View.VISIBLE);
			onChangeListViewVisible(View.GONE);
			
			new Handler(getMainLooper()).post(new Runnable() {
				
				@Override
				public void run() {
					onRequestRefreshGraphView();
				}
			});
		}
		else if(tabpositionTye == TAB_POSITION_HISTORY) {
			
			currentTabPosition = TAB_POSITION_HISTORY;
			
			onChangeGraphViewVisible(View.GONE);
			onChangeListViewVisible(View.VISIBLE);

			onRequestRefreshListView();
		}
	}
	
	/**
	 * Selected Display Measure Data
	 * @param date
	 * @param time
	 * @return true Selecte Data
	 * @return false Not Selecte Data
	 */
	protected boolean selectMeasuData(String date, String time) {
		
		boolean isSelectData = false;
		
		String dateArray[] = date.split("-");
		if(dateArray.length != 3) {
			return isSelectData;
		}
		
		String dayKey = generateDateKey(dateArray, chartType);
		// Keyが存在しているか確認。
		ArrayList<String> keys = getSortDateKeys(dayHashMap);
		int index = keys.indexOf(dayKey);
		
		if(index >= 0) {
			// 存在している場合、
			// Time単位で存在しているか確認。
			displayDayKey = dayKey;
			ArrayList<Lifetrack_infobean> list = dayHashMap.get(dayKey);
			if(list == null) {
				return isSelectData;
			}
			
			for(Lifetrack_infobean item : list) {
				if(item.getTime().equals(time)) {
					onChangeGraphView(getSelectedDateList(), item);
					isSelectData = true;
					break;
				}
			}
		}
		
		return isSelectData;
	}
	
	/**
	 * Selected Latest Data
	 */
	protected void selectedLatestData() {
		HashMap<String, ArrayList<Lifetrack_infobean>> dataMap = getSelectedDataMap();
		ArrayList<Lifetrack_infobean> list = getMapValueWithIndex(dataMap, dataMap.size()-1);
		if(list != null) {
			Lifetrack_infobean item = list.get(list.size()-1);
			setDisplayValue(item);
		}
		else {
			onRequestDisplayErrorValue();
		}
	}
	
	/**
	 * Get Map Value From Map Key Index
	 * @param map
	 * @param index
	 * @return
	 */
	private ArrayList<Lifetrack_infobean> getMapValueWithIndex(HashMap<String, ArrayList<Lifetrack_infobean>> map,int index) {
		ArrayList<String> keys = getSortDateKeys(map);
		if(keys.size() <= index || map.isEmpty()) {
			return null;
		}
		String key = keys.get(index);
		ArrayList<Lifetrack_infobean> list = map.get(key);
		return list;
	}
	
	/**
	 * Get Sorted Map Keys
	 * @param map
	 * @return
	 */
	private ArrayList<String> getSortDateKeys(HashMap<String, ArrayList<Lifetrack_infobean>> map) {
		ArrayList<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys, new Comparator<String>() {
			@Override
			public int compare(String lhs,String rhs) {
				return lhs.compareTo(rhs);
			}
		});
		return keys;
	}
	
	/**
	 * Get DataMap From Current Chart-Type
	 * @return
	 */
	private HashMap<String, ArrayList<Lifetrack_infobean>> getSelectedDataMap() {
		
		if (chartType == CHART_TYPE_DAILY) {
			return dayHashMap;
		}
		else if (chartType == CHART_TYPE_WEEKLY) {
			return weekHashMap;
		} 
		else if (chartType == CHART_TYPE_MONTHLY) {
			return monthHashMap;
		}
		
		return null;
	}
	
	/**
	 * Get Selected Map Key From Current Chart-Type
	 * @return
	 */
	private String getSelectedDisplayMapKey() {

		if (chartType == CHART_TYPE_DAILY) {
			return getDisplayDayKey();
		}
		else if (chartType == CHART_TYPE_WEEKLY) {
			return getDisplayWeekKey();
		} 
		else if (chartType == CHART_TYPE_MONTHLY) {
			return getDisplayMonthKey();
		}
		return null;
	}
	
	/**
	 * Get Selected Map Values From Current Chart-Type
	 * @return
	 */
	private ArrayList<Lifetrack_infobean> getSelectedDateList() {
		ArrayList<Lifetrack_infobean> list = null;
		
		String key = getSelectedDisplayMapKey();
		HashMap<String, ArrayList<Lifetrack_infobean>> dataMap = getSelectedDataMap();
		
		if(key == null || dataMap == null) return null;
		
		list = dataMap.get(key);
		if(list == null) {
			list = getMapValueWithIndex(dataMap, dataMap.size()-1);
		}
		
		return list;
	}
	
	/**
	 * Get Next Data-Map Key From Current Chart-Type
	 * @param isPast is Get Past Data
	 * @return
	 */
	private String getNextDisplayKey(boolean isPast) {
		
		String key = getSelectedDisplayMapKey();
		HashMap<String, ArrayList<Lifetrack_infobean>> dataMap = getSelectedDataMap();
		
		if(key == null || dataMap == null) return null;

		ArrayList<String> keys = getSortDateKeys(dataMap);
		
		if(!keys.contains(key)) {
			// to do Nothing
		}
		
		int index = keys.indexOf(key);
		
		if(isPast) {
			if(index != 0) {
				index--;
			}
		}
		else {
			if(index != (dataMap.size() -1)) {
				index++;
			}
		}
		
		return keys.get(index);
	}
	
	/**
	 * Initialized Data-Maps
	 */
	private void setupDatas() {
		// 初期値 : 日
		chartType = CHART_TYPE_DAILY;
		
		reloadDatas();
		
		// 初期値：最新日時
		displayDayKey = getInitializedDisplayMapKey(dayHashMap);
		displayWeekKey = getInitializedDisplayMapKey(weekHashMap);
		displayMonthKey = getInitializedDisplayMapKey(monthHashMap);
	}
	
	/**
	 * Get Initialized Display Map Key
	 * @param map
	 * @return
	 */
	private String getInitializedDisplayMapKey(HashMap<String, ArrayList<Lifetrack_infobean>> map) {
		ArrayList<String> keys = getSortDateKeys(map);
		String key = null ;
		if(keys != null) {
			key = (keys.isEmpty())? null : keys.get(keys.size() - 1);
		}
		return key;
	}
	
	/**
	 * Reload Data-Maps
	 */
	protected void reloadDatas() {
		
		dayHashMap = new HashMap<String, ArrayList<Lifetrack_infobean>>(); // yyyy/mm/dd : Array
		weekHashMap = new HashMap<String, ArrayList<Lifetrack_infobean>>();// yyyy/mm/dd(SUN) : Array
		monthHashMap = new HashMap<String, ArrayList<Lifetrack_infobean>>();// yyyy/mm : Array
		
		ArrayList<Lifetrack_infobean> dataList = getMeasureDataList();
		if (dataList == null) {
			return ;
		}
		
		for(Lifetrack_infobean item : dataList) {
			
			if(item.getDate() == null) {
				continue;
			}
			
			String dateArray[] = item.getDate().split("-");
			
			if(dateArray.length != 3) {
				continue;
			}
			
			String dayKey = generateDateKey(dateArray, CHART_TYPE_DAILY);
			putItemForMap(dayKey, item, dayHashMap);
			
			String weekKey = generateDateKey(dateArray, CHART_TYPE_WEEKLY);
			putItemForMap(weekKey, item, weekHashMap);
			
			String monthKey = generateDateKey(dateArray, CHART_TYPE_MONTHLY);
			putItemForMap(monthKey, item, monthHashMap);
			
		}
	}
	
	/**
	 * Put Map Key and Value
	 * @param key
	 * @param item
	 * @param map
	 */
	private void putItemForMap(String key, Lifetrack_infobean item, HashMap<String, ArrayList<Lifetrack_infobean>> map) {
		ArrayList<Lifetrack_infobean> list = map.get(key);
		if(list == null) {
			list = new ArrayList<Lifetrack_infobean>();
			map.put(key, list);
		}
		list.add(item);
	}
	
	/**
	 * Generate Data-Maps Key From Date String(yyyy-mm-dd)
	 * @param dateArray
	 * @param chartType
	 * @return
	 */
	private String generateDateKey(String[] dateArray,int chartType) {
		
		if(dateArray.length != 3) {
			return "";
		}
		
		int y = Integer.valueOf(dateArray[0]);
		int m = Integer.valueOf(dateArray[1]);
		int d = Integer.valueOf(dateArray[2]);
		StringBuilder builder = new StringBuilder("");
		
		if(chartType == CHART_TYPE_DAILY) {
			builder.append(String.format("%02d", y));
			builder.append("-");
			builder.append(String.format("%02d", m));
			builder.append("-");
			builder.append(String.format("%02d", d));
		}
		else if(chartType == CHART_TYPE_WEEKLY) {
			// Tips: WeekTypeについて
			// Weekの場合、年跨ぎで一週間を表示しなくてはならない。
			// そのため、日曜を基準に一週間を算出する。
			Calendar calendar = Calendar.getInstance(Locale.getDefault());
			calendar.clear();
			calendar.set(y, m, d);
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			calendar.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - dayOfWeek);
			
			y = calendar.get(Calendar.YEAR);
			m = calendar.get(Calendar.MONDAY);
			d = calendar.get(Calendar.DAY_OF_MONTH);

			builder.append(String.format("%02d", y));
			builder.append("-");
			builder.append(String.format("%02d", m));
			builder.append("-");
			builder.append(String.format("%02d", d));
		}
		else if(chartType == CHART_TYPE_MONTHLY) {
			builder.append(String.format("%02d", y));
			builder.append("-");
			builder.append(String.format("%02d", m));
		}
		
		return builder.toString();
	}
	
	/**
	 * Set 
	 * @param type
	 */
	protected void changeGraphType(int type) {
		
		if (type == CHART_TYPE_DAILY) {
			chartType = CHART_TYPE_DAILY;
		}
		else if (type == CHART_TYPE_WEEKLY) {
			chartType = CHART_TYPE_WEEKLY;
		}
		else if (type == CHART_TYPE_MONTHLY) {
			chartType = CHART_TYPE_MONTHLY;
		}
		
		onChangeGraphType(chartType);
		
		new Handler(getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				onChangeGraphView(getSelectedDateList(),null);
			}
		});
	}
	
	/**
	 * Get Selected Day Map Key
	 * @return
	 */
	private String getDisplayDayKey() {
		if(displayDayKey == null) {
			displayDayKey = "";
		}
		return displayDayKey;
	}
	
	/**
	 * Get Selected Week Map Key
	 * @return
	 */
	private String getDisplayWeekKey() {
		if(displayWeekKey == null) {
			displayWeekKey = "";
		}
		return displayWeekKey;
	}
	
	/**
	 * Get Selected Month Map Key
	 * @return
	 */
	private String getDisplayMonthKey() {
		if(displayMonthKey == null) {
			displayMonthKey = "";
		}
		return displayMonthKey;
	}
	
	/**
	 * Data Move Past Button Click Listener
	 */
	protected OnClickListener onLeftArrowClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			String pastKey = getNextDisplayKey(true);
			
			if (chartType == CHART_TYPE_DAILY) {
				displayDayKey = pastKey;
			} else if (chartType == CHART_TYPE_WEEKLY) {
				displayWeekKey = pastKey;
			} else if (chartType == CHART_TYPE_MONTHLY) {
				displayMonthKey = pastKey;
			}
			
			new Handler(getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					onChangeGraphView(getSelectedDateList(),null);
				}
			});
		}
	};
	
	/**
	 * Data Move Future Button Click Listener
	 */
	protected OnClickListener onRightArrowClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			String futureKey = getNextDisplayKey(false);

			if (chartType == CHART_TYPE_DAILY) {
				displayDayKey = futureKey;
			} else if (chartType == CHART_TYPE_WEEKLY) {
				displayWeekKey = futureKey;
			} else if (chartType == CHART_TYPE_MONTHLY) {
				displayMonthKey = futureKey;
			}
			
			new Handler(getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					onChangeGraphView(getSelectedDateList(),null);
				}
			});
		}
	};
	
	
	/** Tips
	 * 本来は、Abstruct で宣言する必要はないが、
	 * 実装しておくべき or 実装しているであろう機能が
	 * 多いため、Abstruct で宣言する。
	 * 必要ない or 未使用であっても、 OverRideするように。
	 */
	
	/**
	 * Change LeftArrow Visible Property
	 * @param visible
	 */
	abstract protected void onChangeLeftArrowVisible(int visible);
	
	/**
	 * Change RightArrow Visible Property
	 * @param visible
	 */
	abstract protected void onChangeRightArrowVisible(int visible);
	
	/**
	 * Change GraphView Visible Property
	 * @param visible
	 */
	abstract protected void onChangeGraphViewVisible(int visible);
	
	/**
	 * Change ListView Visible Property
	 * @param visible
	 */
	abstract protected void onChangeListViewVisible(int visible);
	
	/**
	 * Change Graph Type
	 * @param type CHART_TYPE
	 */
	abstract protected void onChangeGraphType(int type);
	
	/**
	 * Change GraphView List items
	 * @param list
	 * @param selectData
	 */
	abstract protected void onChangeGraphView(ArrayList<Lifetrack_infobean> list,Lifetrack_infobean selectData);
	
	/**
	 * Request Display Error Value
	 */
	abstract protected void onRequestDisplayErrorValue();
	
	/**
	 * Request ListView Refresh
	 */
	abstract protected void onRequestRefreshListView();
	
	/**
	 * Request ListView Refresh
	 */
	abstract protected void onRequestRefreshGraphView();
	
	/**
	 * Set Display Value
	 * @param data
	 */
	abstract protected void setDisplayValue(Lifetrack_infobean data);
}
