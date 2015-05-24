/**
 * 
 */
package com.yuhen.saleapp.shop;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 下拉列表
 * @author ps
 *
 */
public class ListAdapter extends BaseAdapter {
	List<String> items;
	Activity context;
	
	public ListAdapter(Activity context, List<String> items) {
		super();
		this.items = items;
		this.context = context;
	}
	/** 
	 * 返回控制该Adapter将会包括多少个列表
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return items.size();
	}

	/**
	 * 决定第position处的列表内容
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public String getItem(int position) {
		return items.get(position);
	}

	/**
	 * 决定第position处的列表Id
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 决定第position处的列表组件
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String item = items.get(position);
		LinearLayout linearLayout = new LinearLayout(this.context);
		TextView textView = new TextView(this.context);
		textView.setText(item);
		linearLayout.addView(textView);
		return linearLayout;
	}

}
