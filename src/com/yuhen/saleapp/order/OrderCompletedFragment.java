package com.yuhen.saleapp.order;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yuhen.saleapp.R;
import com.yuhen.saleapp.domain.OrderInfo;

public class OrderCompletedFragment extends Fragment {
	private ListView kjListView;
	private OrderItemAdapter orderItemAdapter;
	private List<OrderInfo> orderlist;
	private Context context;

	public OrderCompletedFragment(Context context) {
		this.context = context;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.view_completed_order,
				container, false);
		kjListView = (ListView) contentView.findViewById(R.id.lv_in_order_list);
		orderItemAdapter = new OrderItemAdapter(getActivity(), orderlist);
		kjListView.setAdapter(orderItemAdapter);
		return contentView;
	}
}
