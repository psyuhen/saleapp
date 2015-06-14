/**
 * 
 */
package com.yuhen.saleapp.goods;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.yuhen.saleapp.R;
import com.yuhen.saleapp.activity.BaseActivity;
import com.yuhen.saleapp.shop.ListMapAdapter;
import com.yuhen.saleapp.util.HttpUtil;
import com.yuhen.saleapp.util.JsonUtil;

/**
 * @author ps
 *
 */
public class ClassifyActivity extends BaseActivity {
	
	private ListView listView; 
	
	@Override
	public void initView() {
		super.initView();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classify);
		initView();
		head_center_text.setText("选择分类");
		
		listView = (ListView)findViewById(R.id.classify_list);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ListMapAdapter adapter = (ListMapAdapter)listView.getAdapter();
				Map<String, String> items = adapter.getItem(position);
				
				Intent intent = new Intent(ClassifyActivity.this, GoodsDetailActivity.class);  
                intent.putExtra("classify_id", items.get("classify_id"));  
                intent.putExtra("name", items.get("name"));  
                setResult(R.id.classify_list, intent);
				finish();
			}
		});
		
		String json = null;
		try {
			json = HttpUtil.getRequest(HttpUtil.BASE_URL + "/classify/querybytype.do?classify_type=1");
			if(json != null){
				List<Map<String, String>> items = JsonUtil.parse2ListMap(json);
				listView.setAdapter(new ListMapAdapter(ClassifyActivity.this, "name", items));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		if(json == null){
			Toast.makeText(ClassifyActivity.this, "服务器响应异常,请稍后再试!", Toast.LENGTH_LONG).show();
		}
	}
}
