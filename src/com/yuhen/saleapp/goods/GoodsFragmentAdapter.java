/**
 * 
 */
package com.yuhen.saleapp.goods;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author ps
 *
 */
public class GoodsFragmentAdapter extends FragmentPagerAdapter {
	private Context context;

	public GoodsFragmentAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public GoodsFragmentAdapter(FragmentManager fm,Context context) {
		super(fm);
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {
		if(position == 0){
			return new GoodsListFragment(context);
		}else if(position == 1){
			return new GoodsOutListFragment(context);
		}else if(position == 2){
			return new GoodsClassifyListFragment(context);
		}
		return null;
	}
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return 3;
	}

}
