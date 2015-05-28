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
import com.yuhen.saleapp.domain.ClassifyInfo;
import com.yuhen.saleapp.util.HttpUtil;
import com.yuhen.saleapp.util.JsonUtil;

/**
 * 
 * @author ps
 * 
 */
public class GoodsClassifyListFragment extends Fragment {

	private ListView listView;
	private GoodsClassifyListAdapter goodsListAdapter;
	private List<ClassifyInfo> items;
	private Context context;
	
	public GoodsClassifyListFragment(Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.view_goods_classify_lists,
				container, false);
		listView = (ListView) contentView.findViewById(R.id.lv_goods_classify_list);
		
		
		String jsonResult = "";
		try {
			jsonResult = HttpUtil.getRequest(HttpUtil.BASE_URL + "/classify/countclassifymerch.do");
			if(jsonResult != null){
				items = JsonUtil.parse2ListClassifyInfo(jsonResult);
				goodsListAdapter = new GoodsClassifyListAdapter(getActivity(), items);
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
