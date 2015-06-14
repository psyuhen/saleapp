/**
 * 
 */
package com.yuhen.saleapp.shop;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yuhen.saleapp.R;

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
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(this.context).inflate(R.layout.custom_list_map_view, null);
			holder.map_key = (TextView)convertView.findViewById(R.id.map_key);
			holder.map_value = (TextView)convertView.findViewById(R.id.map_value);
			
			// 将holder绑定到convertView  
            convertView.setTag(holder);
		}else {  
            holder = (ViewHolder) convertView.getTag();  
        }
		
		holder.map_key.setText(item.get(this.fieldName));
		holder.map_value.setText(item.get(this.fieldName));
		
		return convertView;
	}

	/** 
     * ViewHolder类用以储存item中控件的引用 
     */  
    final class ViewHolder {  
        TextView map_key;  
        TextView map_value;  
    }  
}
