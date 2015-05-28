/**
 * 
 */
package com.yuhen.saleapp.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yuhen.saleapp.R;
import com.yuhen.saleapp.domain.Store;
import com.yuhen.saleapp.domain.User;
import com.yuhen.saleapp.login.LoginActivity;
import com.yuhen.saleapp.session.SessionManager;
import com.yuhen.saleapp.util.HttpUtil;
import com.yuhen.saleapp.util.JsonUtil;
import com.yuhen.saleapp.util.TipFlag;

/**
 * 商家信息
 * @author ps
 *
 */
public class ShopDetailActivity extends Activity {
	private Spinner spinner;
	private EditText mShopNameView;
	private EditText mAddressView;
	private Button btnShopDetail;
	private Button btnShopDetailLogout;
	private View btnShopDetailBack;
	
	private ShopDetailTask detailTask;
	private String storeId;
	private SessionManager sessionManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_detail);
		
		sessionManager = new SessionManager(getApplicationContext());
		
		spinner = (Spinner)findViewById(R.id.s_shop_classify);
		mShopNameView = (EditText) findViewById(R.id.et_shop_name);
		mAddressView = (EditText) findViewById(R.id.et_address);
		btnShopDetail = (Button) findViewById(R.id.btn_shop_detail);
		btnShopDetailLogout = (Button) findViewById(R.id.btn_shop_detail_logout);
		btnShopDetailBack = findViewById(R.id.shop_detail_back);
		
		//取到注册成功后的用户信息
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		String from = data.getString("from");
		
		if("shop_home".equals(from)){
			btnShopDetailLogout.setVisibility(View.GONE);
			storeId = data.getInt("store_id")+"";
		}
		//提交
		btnShopDetail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				attemptSubmit();
			}
		});
		//退出登录
		btnShopDetailLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(ShopDetailActivity.this)
						.setTitle("超家伙(卖家版)")
						.setMessage("您确认要退出当前账户吗?")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										//跳转到登录页面
										Intent intent = new Intent(ShopDetailActivity.this, LoginActivity.class);
										startActivity(intent);
										// 点击“确认”后的操作
										ShopDetailActivity.this.finish();
										
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// 点击“返回”后的操作,这里不设置没有任何操作
									}
								}).show();
			}
		});
		
		//返回按钮
		btnShopDetailBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShopDetailActivity.this, ShopActivity.class);  
                intent.putExtra("address", mAddressView.getText().toString());  
                setResult(100200, intent); 
				finish();
			}
		});
		
		if (detailTask == null) {
			detailTask = new ShopDetailTask("init");
			detailTask.execute((Void)null);
		}
	}
	
	//提交修改
	public void attemptSubmit(){
		if (detailTask != null) {
			return;
		}
		
		Map<String,String> map = (Map<String,String>)spinner.getSelectedItem();
		String classifyType = map.get("classify_id");
		String shopName = mShopNameView.getText().toString();
		String address = mAddressView.getText().toString();
		
		boolean cancel = false;
		View focusView = null;
		
		if (TextUtils.isEmpty(classifyType)) {
			((TextView)(spinner.getSelectedView())).setError(getString(R.string.error_field_required));
			focusView = spinner;
			cancel = true;
		}else if (TextUtils.isEmpty(shopName)) {
			mShopNameView.setError(getString(R.string.error_field_required));
			focusView = mShopNameView;
			cancel = true;
		}else if (TextUtils.isEmpty(address)) {
			mAddressView.setError(getString(R.string.error_field_required));
			focusView = mAddressView;
			cancel = true;
		}
		
		if (cancel) {
			// 有错误，不提交
			focusView.requestFocus();
		}else {
			detailTask = new ShopDetailTask("submit");
			detailTask.setClassify_id(classifyType);
			detailTask.setAddress(address);
			detailTask.setShopName(shopName);
			detailTask.execute((Void)null);
		}
	}
	
	/**
	 * 异步发送HTTP请求数据
	 */
	public class ShopDetailTask extends AsyncTask<Void, Void, Map<String,Object>> {
		private String oper;
		private String classify_id;
		private String shopName;
		private String address;
		
		ShopDetailTask(String oper) {
			this.oper = oper;
		}

		@Override
		protected Map<String,Object> doInBackground(Void... params) {
			//根据分类类型查询分类,0为商家
			String url1 = HttpUtil.BASE_URL + "/classify/querybytype.do?classify_type=0";
			//查询商家信息
			String url2 = HttpUtil.BASE_URL + "/store/querybyid.do?store_id="+ShopDetailActivity.this.storeId;
			//更新商家信息
			String url3 = HttpUtil.BASE_URL + "/store/modify.do";
			
			// 创建 RestTemplate 实例
			RestTemplate restTemplate = new RestTemplate();

			// 发送登录请求
			String result = null;
			List<Map<String,String>> list = null;
			Map<String,Object> map = new HashMap<String,Object>();
			Store storeInfo = null;
			try{
				if("init".equals(oper)){
					result = restTemplate.getForObject(url1, String.class);
					list = JsonUtil.parse2ListMap(result);
					
					storeInfo = restTemplate.getForObject(url2, Store.class);
					
					map.put("STORE_INFO", storeInfo);
					map.put("CLASSIFY_LIST", list);
				}else if("submit".equals(oper)){
					User user = sessionManager.getUserDetails();
					storeInfo = new Store();
					storeInfo.setUser_id(user.getUser_id());
					storeInfo.setClassify_id(Integer.parseInt(classify_id));
					storeInfo.setAddress(address);
					storeInfo.setName(shopName);
					storeInfo.setStore_id(Integer.parseInt(ShopDetailActivity.this.storeId));
					result = restTemplate.postForObject(url3, storeInfo, String.class);
					map.put("SUBMIT_RESULT", result);
				}
				
				map.put("STATUS", TipFlag.SUCCESS);
			}catch (Exception e) {
				e.printStackTrace();
				map.put("STATUS", TipFlag.NETWORK_ERROR);
				return map;
			}

			return map;
		}

		@Override
		protected void onPostExecute(final Map<String,Object> map) {
			detailTask = null;
			
			if(TipFlag.SUCCESS == Integer.parseInt(map.get("STATUS")+"")){
				
				if("init".equals(oper)){
					List<Map<String,String>> list = (List<Map<String,String>>)map.get("CLASSIFY_LIST");
					
					DropDownListAdapter adapter = new DropDownListAdapter(ShopDetailActivity.this, "name",list);
					spinner.setAdapter(adapter);
					
					Store storeInfo = (Store)map.get("STORE_INFO");
					if(storeInfo != null){
						int position = (int)adapter.getSelection("classify_id", storeInfo.getClassify_id()+"");
						spinner.setSelection(position, true);
						mShopNameView.setText(storeInfo.getName());
						mAddressView.setText(storeInfo.getAddress());
					}
				}else if("submit".equals(oper)){
					Object result = map.get("SUBMIT_RESULT");
					if(result == null){
						Toast.makeText(ShopDetailActivity.this, "提交失败！", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(ShopDetailActivity.this, result+"", Toast.LENGTH_LONG).show();
					}
				}
				
			}else if(TipFlag.NETWORK_ERROR == Integer.parseInt(map.get("STATUS")+"")){
				Toast.makeText(ShopDetailActivity.this, "服务器响应异常,请稍后再试!", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			detailTask = null;
		}

		public void setOper(String oper) {
			this.oper = oper;
		}

		public void setClassify_id(String classify_id) {
			this.classify_id = classify_id;
		}

		public void setShopName(String shopName) {
			this.shopName = shopName;
		}

		public void setAddress(String address) {
			this.address = address;
		}
	}
}
