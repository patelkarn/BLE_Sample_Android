package jp.co.aandd.bleSimpleApp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
/*
 * Method for Extending Fragment Pager Adapter for View Pager
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
	private Context _context;
	public static int totalPage = 3;
	String mFrom;
	int datePos;

	public ViewPagerAdapter(Context context, FragmentManager fm, String from, int datePosition) {
		super(fm);
		_context = context;
		mFrom = from;
		datePos=datePosition;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {
		Fragment f = new Fragment();


		return f;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return totalPage;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.FragmentPagerAdapter#instantiateItem(android.view
	 * .ViewGroup, int)
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		return super.instantiateItem(container, position);
	}

}
