package com.yuhen.saleapp.order;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.yuhen.saleapp.R;
import com.yuhen.saleapp.domain.OrderInfo;

public class OrderInFragment extends Fragment {
	private ListView listView;
	private OrderItemAdapter orderItemAdapter;
	private List<OrderInfo> orderlist;
	private Context context;
	
	public OrderInFragment(Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.view_in_order, container,
				false);
		orderItemAdapter = new OrderItemAdapter(getActivity(), orderlist);
		listView = (ListView) contentView.findViewById(R.id.lv_in_order_list);
		listView.setAdapter(orderItemAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long len) {
//				Intent intent = new Intent(getActivity(), ChatActivity.class);
//				startActivity(intent);
			}
		});
		return contentView;
	}
}
