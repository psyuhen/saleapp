package com.yuhen.saleapp.order;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuhen.saleapp.R;
import com.yuhen.saleapp.domain.OrderInfo;

public class OrderItemAdapter extends BaseAdapter{
	private List<OrderInfo> items;
	private Context context;
	
	public OrderItemAdapter(Context context,List<OrderInfo> items) {
		this.context=context;
		this.items=items;
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		OrderInfo orderInfo = items.get(position);
		ViewHolder viewHolder;
		if (convertView==null) {
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.custom_in_order_list_view, null);
			viewHolder.ordertime_text= (TextView) convertView.findViewById(R.id.order_time);
			viewHolder.serialnumber_text=(TextView) convertView.findViewById(R.id.order_number);
			viewHolder.toperson_text=(TextView) convertView.findViewById(R.id.order_person);
			viewHolder.img_image=(ImageView) convertView.findViewById(R.id.order_image);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder=(ViewHolder) convertView.getTag();
		}
//		viewHolder.ordertime_text.setText(orderlist.get(position).getOrdertime()+"");
//		viewHolder.serialnumber_text.setText(orderlist.get(position).getSerialnumber());;
//		viewHolder.toperson_text.setText(orderlist.get(position).getToperson());;
		return convertView;
	}
	
	private class ViewHolder
	{
		private TextView toperson_text;
		private TextView ordertime_text;
		private TextView serialnumber_text;
		private ImageView img_image;
	}

}
