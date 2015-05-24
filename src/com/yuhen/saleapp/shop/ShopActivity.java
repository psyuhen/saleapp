/**
 * 
 */
package com.yuhen.saleapp.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuhen.saleapp.R;
import com.yuhen.saleapp.domain.MerchInfo;
import com.yuhen.saleapp.domain.Store;
import com.yuhen.saleapp.login.LoginActivity;
import com.yuhen.saleapp.menu.ActionItem;
import com.yuhen.saleapp.menu.TitlePopup;
import com.yuhen.saleapp.menu.TitlePopup.OnItemOnClickListener;
import com.yuhen.saleapp.util.HttpUtil;
import com.yuhen.saleapp.util.TipFlag;

public class ShopActivity extends Activity {
	
	private View imageView;
	private EditText mSearchView;
	private Drawable mIconSearchDefault; // 搜索文本框默认图标
    private Drawable mIconSearchClear; // 搜索文本框清除文本内容图标
    private View mShopBackView;
    private ListView mListView;
    private View mShopLogoView;
    private View mShopAddressView;
    private View mShopTelephoneView;
    private View mShopFavNum;
    private View mShopFav;
    private ShopInfoTask mShopInfoTask = null;
    private Store storeInfo = null;
    
	/**
	 * 右上角上下文菜单
	 */
	TitlePopup titlePopup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);
		
		/**  商家信息    **/
		mShopLogoView = findViewById(R.id.iv_shop_logo);
		mShopAddressView = findViewById(R.id.tv_shop_address);
		mShopTelephoneView = findViewById(R.id.tv_shop_telephone);
		
		/**  商家收藏信息    **/
		mShopFavNum = findViewById(R.id.tv_shop_fav_num);
		mShopFav = findViewById(R.id.iv_shop_fav);
		
		/**  菜单    **/
		imageView = findViewById(R.id.imgMenu);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				titlePopup.show(view);//popwindow显示的位置
			}
		});
		
		final Resources res = getResources();
        mIconSearchDefault = res.getDrawable(R.drawable.txt_search_default);
        mIconSearchClear = res.getDrawable(R.drawable.txt_search_clear);
		
		mSearchView = (EditText)findViewById(R.id.et_search);
		mSearchView.addTextChangedListener(new TextWatcher() {
			//缓存上一次文本框内是否为空
	        private boolean isnull = true;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (TextUtils.isEmpty(s)) {
	                if (!isnull) {
	                    mSearchView.setCompoundDrawablesWithIntrinsicBounds(null,
	                            null, mIconSearchDefault, null);
	                    isnull = true;
	                }
	            } else {
	                if (isnull) {
	                    mSearchView.setCompoundDrawablesWithIntrinsicBounds(null,
	                            null, mIconSearchClear, null);
	                    isnull = false;
	                }
	            }
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mSearchView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 switch (event.getAction()) {
		            case MotionEvent.ACTION_UP:
		                int curX = (int) event.getX();
		                if (curX > v.getWidth() - 78
		                        && !TextUtils.isEmpty(mSearchView.getText().toString())) {
		                    mSearchView.setText("");
		                    int cacheInputType = mSearchView.getInputType();
		                    mSearchView.setInputType(InputType.TYPE_NULL);
		                    mSearchView.onTouchEvent(event);
		                    mSearchView.setInputType(cacheInputType);
		                    return true;
		                }
		                break;
		            }
				return false;
			}
		});
		
		initMenu();
		
		/** 返回按钮 **/
		mShopBackView = findViewById(R.id.shop_back);
		mShopBackView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		/** 列表显示 **/
		mListView = (ListView)findViewById(R.id.lv_merch_list);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//TODO 显示商品详细信息
			}
		});
		
		/** 请求数据 **/
		mShopInfoTask = new ShopInfoTask();
		mShopInfoTask.execute((Void)null);
	}
	
	/**
	 * 初始化 右上角 popwindow的显示 文字和图标
	 */
	public void initMenu(){
		//实例化一个对象
		titlePopup = new TitlePopup(ShopActivity.this);
		titlePopup.setMenuItemOnClickListener(menuItemOnClickListener);
		//这个加入的顺序和在右上角显示的顺序是一样的
		titlePopup.addAction(new ActionItem(this, R.string.edit_my_shop, R.drawable.pop9));
		titlePopup.addAction(new ActionItem(this, R.string.edit_my_merch, R.drawable.pop7));
	
	}
	
	/**
	 * 点击右上角popwindow 触发的事件
	 */
	OnItemOnClickListener menuItemOnClickListener = new OnItemOnClickListener() {
		@Override
		public void onItemClick(ActionItem item, int position) {
			switch (item.getTitleId()) {
			case R.string.edit_my_shop:
				//跳转到商家信息页面
				Intent intent = new Intent(ShopActivity.this, ShopDetailActivity.class);
				intent.putExtra("from", "shop_home");
				intent.putExtra("store_id", storeInfo.getStore_id());
				startActivityForResult(intent, 100200);
				break;
			case R.string.edit_my_merch:
				Toast.makeText(ShopActivity.this, "开始进行【人员定位】的操作\n可以弹出对话框,或者跳转等", 1).show();
				//TODO 编辑商品信息
				break;
			default:
				break;
			}
		}
	};
	
	//重写该方法，该方法回调的方式来获取指定的Activity返回的结果
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		
		if(requestCode == 100200 && resultCode == 100200){
			Bundle data = intent.getExtras();
			String address = data.getString("address");
			
			((TextView)mShopAddressView).setText(address);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * 异步发送HTTP请求数据
	 */
	public class ShopInfoTask extends AsyncTask<Void, Void, Map<String,Object>> {
		ShopInfoTask() {
		}

		@Override
		protected Map<String,Object> doInBackground(Void... params) {
			//根据用户查询商家信息
			String url1 = HttpUtil.BASE_URL + "/store/querybyuser.do?user_id=1";
			//查询商家收藏数
			String url2 = HttpUtil.BASE_URL + "/storeuf/count.do?store_id=1";
			//根据用户查询商品列表
			String url3 = HttpUtil.BASE_URL + "/merch/querybyuserid.do?user_id=1";
			
			// 创建 RestTemplate 实例
			RestTemplate restTemplate = new RestTemplate();

			// 发送登录请求
			String result = null;
			Store storeInfo = null;
			int favoriteCount = 0;
			List<MerchInfo> list = null;
			Map<String,Object> map = new HashMap<String,Object>();
			try{
				storeInfo = restTemplate.getForObject(url1, Store.class);
				favoriteCount = restTemplate.getForObject(url2, Integer.class);
				result = restTemplate.getForObject(url3, String.class);
				ObjectMapper mapper = new ObjectMapper(); 
				JavaType javaType = mapper.getTypeFactory().constructParametrizedType(ArrayList.class,ArrayList.class,MerchInfo.class);
				list = (List<MerchInfo>)mapper.readValue(result, javaType);
				
				map.put("STORE_INFO", storeInfo);
				map.put("FAVORITE_COUNT", favoriteCount);
				map.put("MERCH_LIST", list);
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
			if(TipFlag.SUCCESS == Integer.parseInt(map.get("STATUS")+"")){
				Store storeInfo = (Store)map.get("STORE_INFO");
				int favoriteCount = Integer.parseInt(map.get("FAVORITE_COUNT")+"");
				List<MerchInfo> list = (List<MerchInfo>)map.get("MERCH_LIST");
				
				//TODO 图片可能保存到7牛上，要从7牛中下载
				if(storeInfo != null){
//					((ImageView)mShopLogoView).setImageResource(storeInfo.getLogo());
					((TextView)mShopAddressView).setText(storeInfo.getAddress());
					((TextView)mShopTelephoneView).setText(storeInfo.getPhone());
					ShopActivity.this.storeInfo = storeInfo;
				}
				
				((TextView)mShopFavNum).setText("收藏数:"+favoriteCount+"");
				if(favoriteCount == 0){
					((ImageView)mShopFav).setImageResource(R.drawable.xin);
				}else{
					((ImageView)mShopFav).setImageResource(R.drawable.xin_red);
				}
				
				mListView.setAdapter(new MerchListAdapter(ShopActivity.this, list));
			}else if(TipFlag.NETWORK_ERROR == Integer.parseInt(map.get("STATUS")+"")){
				Toast.makeText(ShopActivity.this, "服务器响应异常,请稍后再试!", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			
		}
	}
}
