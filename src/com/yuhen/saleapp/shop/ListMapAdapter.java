/**
 * 
 */
package com.yuhen.saleapp.shop;

import java.util.List;
import java.util.Map;

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
public class ListMapAdapter extends BaseAdapter {
	List<Map<String,String>> items;
	String fieldName;
	Activity context;
	
	public ListMapAdapter(Activity context, String fieldName, List<Map<String,String>> items) {
		super();
		this.items = items;
		this.fieldName = fieldName;
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
	public Map<String,String> getItem(int position) {
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
		Map<String,String> item = items.get(position);
		LinearLayout linearLayout = new LinearLayout(this.context);
		TextView textView = new TextView(this.context);
		textView.setText(item.get(this.fieldName));
		linearLayout.addView(textView);
		return linearLayout;
	}

}
