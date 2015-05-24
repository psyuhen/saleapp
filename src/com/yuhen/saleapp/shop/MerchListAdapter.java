/**
 * 
 */
package com.yuhen.saleapp.shop;

import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuhen.saleapp.R;
import com.yuhen.saleapp.domain.MerchInfo;
import com.yuhen.saleapp.util.DateUtil;

/**
 * @author ps
 * 
 */
public class MerchListAdapter extends BaseAdapter {
	List<MerchInfo> items;
	Activity context;

	public MerchListAdapter(Activity context, List<MerchInfo> items) {
		super();
		this.context = context;
		this.items = items;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return items.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public MerchInfo getItem(int position) {
		return items.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MerchInfo item = items.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(this.context).inflate(R.layout.custom_list_view, null);
			holder.photo = (ImageView)convertView.findViewById(R.id.iv_merch_photo);
			holder.title = (TextView)convertView.findViewById(R.id.tv_title);
			holder.price = (TextView)convertView.findViewById(R.id.tv_price);
			holder.sale_volumn = (TextView)convertView.findViewById(R.id.tv_sales_volumn);
			holder.in_stock = (TextView)convertView.findViewById(R.id.tv_in_stock);
			holder.create_time = (TextView)convertView.findViewById(R.id.tv_create_time);
			
			// 将holder绑定到convertView  
            convertView.setTag(holder);
		}else {  
            holder = (ViewHolder) convertView.getTag();  
        }  
		holder.photo.setImageResource(item.getImage_resource_id());
		holder.title.setText(item.getName());
		holder.price.setText("￥ "+item.getPrice()+"");
		holder.sale_volumn.setText("总销量 " + item.getSales_volume()+"件");
		holder.in_stock.setText("库存 " + item.getIn_stock()+"件");
		holder.create_time.setText(DateFormatUtils.format(DateUtil.parseDate(item.getCreate_time(), new String[]{"yyyyMMddHHmmss"}), "MM-dd"));
		
		return convertView;
	}

	/** 
     * ViewHolder类用以储存item中控件的引用 
     */  
    final class ViewHolder {  
        ImageView photo;  
        TextView title;  
        TextView price;  
        TextView sale_volumn;  
        TextView in_stock;  
        TextView create_time;  
    }  
}
