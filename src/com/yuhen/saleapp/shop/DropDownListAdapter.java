/**
 * 
 */
package com.yuhen.saleapp.shop;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/**
 * 下拉列表adapter
 * @author ps
 *
 */
public class DropDownListAdapter extends BaseAdapter implements SpinnerAdapter {
	
	List<Map<String,String>> items;
	String fieldName;
	Activity context;
	
	
	public DropDownListAdapter(Activity context, String fieldName, List<Map<String,String>> items) {
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
	public Object getItem(int position) {
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
	 * 获取下拉的index
	 * @param name
	 * @param value
	 * @return
	 */
	public long getSelection(String name,String value){
		for (int i = 0; i < items.size(); i++) {
			Map<String,String> map = items.get(i);
			if(StringUtils.trimToEmpty(map.get(name)).equals(value)){
				return i;
			}
		}
		return 0;
	}

	/**
	 * 决定第position处的列表组件
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView text;
        if (convertView != null){
            text = (TextView) convertView;
        } else {
        	convertView = LayoutInflater.from(this.context).inflate(android.R.layout.simple_spinner_dropdown_item, null);
            text = (TextView) convertView;
        }
        text.setTextColor(Color.BLACK);
        text.setText(items.get(position).get(this.fieldName));
        return text;
	}

}
