package com.yuhen.saleapp.order;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class OrderFragmentAdapter extends FragmentPagerAdapter{
	private int pageCount=2;  //ҳ����
	private Context context;
	/*
	 * ���캯��
	 */
	public OrderFragmentAdapter(FragmentManager fm) {
		super(fm);
	}
	public OrderFragmentAdapter(FragmentManager fm,Context context) {
		super(fm);
		this.context = context;
	}
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 *��ȡÿһ��ҳ�������
	 */
	@Override
	public Fragment getItem(int position) {
		switch (position) {
		//���ڽ��еĶ���
		case 0:
			return new OrderInFragment(context);
		//����ɵĶ���
		case 1:
			return new OrderCompletedFragment(context);
		}
		return null;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return super.getPageTitle(position);
	}
	/**
	 * destory��ǰ��pager
	 */
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
	/**
	 * ��ȡ����
	 */
	@Override
	public int getCount() {
		return pageCount;
	}

}
