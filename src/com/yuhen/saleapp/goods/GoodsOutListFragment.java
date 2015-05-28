/**
 * 
 */
package com.yuhen.saleapp.goods;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.yuhen.saleapp.R;
import com.yuhen.saleapp.domain.MerchInfo;
import com.yuhen.saleapp.domain.User;
import com.yuhen.saleapp.util.HttpUtil;
import com.yuhen.saleapp.util.JsonUtil;

/**
 * 
 * @author ps
 * 
 */
public class GoodsOutListFragment extends Fragment {

	private ListView listView;
	private GoodsOutListAdapter goodsListAdapter;
	private List<MerchInfo> items;
	private Context context;
	
	public GoodsOutListFragment(Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.view_goods_out_lists,
				container, false);
		listView = (ListView) contentView.findViewById(R.id.lv_goods_out_list);
		
		String jsonResult = "";
		try {
			GoodsActivity goodsActivity = (GoodsActivity)context;
			User user = goodsActivity.sessionManager.getUserDetails();
			MerchInfo requestParams = new MerchInfo();
			requestParams.setUser_id(user.getUser_id());
			requestParams.setOut_published("1");//已下架
			requestParams.setOrder_by_clause("create_time desc");
			jsonResult = HttpUtil.getRequest(HttpUtil.BASE_URL + "/merch/queryby.do", requestParams);
			if(jsonResult != null){
				items = JsonUtil.parse2ListMerchInfo(jsonResult);
				goodsListAdapter = new GoodsOutListAdapter(getActivity(), items);
				listView.setAdapter(goodsListAdapter);
			}else{
				Toast.makeText(context, "服务器响应异常,请稍后再试!", Toast.LENGTH_LONG).show();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		return contentView;
	}
}
