/**
 * 
 */
package com.yuhen.saleapp.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.web.client.RestTemplate;

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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuhen.saleapp.R;
import com.yuhen.saleapp.activity.BaseActivity;
import com.yuhen.saleapp.domain.MerchInfo;
import com.yuhen.saleapp.domain.Store;
import com.yuhen.saleapp.domain.User;
import com.yuhen.saleapp.goods.GoodsActivity;
import com.yuhen.saleapp.goods.GoodsDetailActivity;
import com.yuhen.saleapp.menu.ActionItem;
import com.yuhen.saleapp.menu.TitlePopup;
import com.yuhen.saleapp.menu.TitlePopup.OnItemOnClickListener;
import com.yuhen.saleapp.session.SessionManager;
import com.yuhen.saleapp.util.HttpUtil;
import com.yuhen.saleapp.util.JsonUtil;
import com.yuhen.saleapp.util.TipFlag;

public class ShopActivity extends BaseActivity {
	
	private View imageView;
	private EditText mSearchView;
	private Drawable mIconSearchDefault; // 搜索文本框默认图标
    private Drawable mIconSearchClear; // 搜索文本框清除文本内容图标
    private ListView mListView;
    private View mShopLogoView;
    private View mShopAddressView;
    private View mShopTelephoneView;
    private View mShopFavNum;
    private View mShopFav;
    private ShopInfoTask mShopInfoTask = null;
    private Store storeInfo = null;
    
    //出售中，已下架，分类
    private View mSelling;
    private View mOutPublished;
    private View mClassify;
    
    //添加时间，销量，库存
    private View mAddTime;
    private View mSellVolumn;
    private View mInStock;
    
    private SessionManager sessionManager;
    private User user;
    private View selectOne;
    private View selectTwo;
    
	/**
	 * 右上角上下文菜单
	 */
	TitlePopup titlePopup;

	@Override
	public void initView() {
		super.initView();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);
		initView();
		head_center_text.setText("我的商店");
		head_right_text.setText("编辑");
		
		sessionManager = new SessionManager(getApplicationContext());
		user  = sessionManager.getUserDetails();
		
		/**  商家信息    **/
		mShopLogoView = findViewById(R.id.iv_shop_logo);
		mShopAddressView = findViewById(R.id.tv_shop_address);
		mShopTelephoneView = findViewById(R.id.tv_shop_telephone);
		
		/**  商家收藏信息    **/
		mShopFavNum = findViewById(R.id.tv_shop_fav_num);
		mShopFav = findViewById(R.id.iv_shop_fav);
		
		/**  菜单    **/
		head_right_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				titlePopup.show(view);//popwindow显示的位置
			}
		});
		
		/** 搜索   **/
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
		
		/** 列表显示 **/
		mListView = (ListView)findViewById(R.id.lv_merch_list);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MerchListAdapter adapter = (MerchListAdapter)mListView.getAdapter();
				MerchInfo item = adapter.getItem(position);
				
				Intent intent1 = new Intent(ShopActivity.this, GoodsDetailActivity.class);
				intent1.putExtra("from", "shop_home");
				intent1.putExtra("store_id", storeInfo.getStore_id());
				startActivity(intent1);
			}
		});
		
		/** 出售中，已下架，分类**/
		mSelling = findViewById(R.id.tv_selling);
		mOutPublished = findViewById(R.id.tv_offline);
		mClassify = findViewById(R.id.tv_classify);
		mSelling.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View [] tvs = {mSelling,mOutPublished,mClassify};
				clickEvent0(tvs, v);
				selectOne = v;
				queryMerch(getCondition(), getOrderBy());
			}
		});
		mOutPublished.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View [] tvs = {mSelling,mOutPublished,mClassify};
				clickEvent0(tvs, v);
				selectOne = v;
				queryMerch(getCondition(), getOrderBy());
			}
		});
		mClassify.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View [] tvs = {mSelling,mOutPublished,mClassify};
				clickEvent0(tvs, v);
				selectOne = v;
				queryMerch(getCondition(), getOrderBy());
			}
		});
		
		/** 添加时间，销量，库存 **/
		mAddTime = findViewById(R.id.tv_header_create_time);
		mSellVolumn = findViewById(R.id.tv_header_sale_volumn);
		mInStock = findViewById(R.id.tv_header_in_stock);
		mAddTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View [] tvs = {mAddTime,mSellVolumn,mInStock};
				clickEvent0(tvs, v);
				selectTwo = v;
				queryMerch(getCondition(), getOrderBy());
			}
		});
		mSellVolumn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View [] tvs = {mAddTime,mSellVolumn,mInStock};
				clickEvent0(tvs, v);
				selectTwo = v;
				queryMerch(getCondition(), getOrderBy());
			}
		});
		mInStock.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View [] tvs = {mAddTime,mSellVolumn,mInStock};
				clickEvent0(tvs, v);
				selectTwo = v;
				queryMerch(getCondition(), getOrderBy());
			}
		});
		
		/** 默认初始化赋值  **/
		selectOne = mSelling;
		selectTwo = mAddTime;
		
		/** 请求数据 **/
		mShopInfoTask = new ShopInfoTask();
		mShopInfoTask.execute((Void)null);
	}
	
	protected String getOrderBy(){
		if(selectTwo.getId() == mAddTime.getId()){
			return " create_time desc";
		}if(selectTwo.getId() == mSellVolumn.getId()){//TODO 销量还要查询订单表
			return " create_time desc";
		}if(selectTwo.getId() == mInStock.getId()){
			return " in_stock desc";
		}
		
		return "";
	}
	
	
	protected String getCondition(){
		if(selectOne.getId() == mSelling.getId()){
			return "0";
		}if(selectOne.getId() == mOutPublished.getId()){
			return "1";
		}if(selectOne.getId() == mClassify.getId()){//TODO 分类的再看吧
			return "";
		}
		
		return "";
	}
	
	protected void clickEvent0(View [] tvs,View v){
		int redColor = getResources().getColor(R.color.red);
		int grayColor = getResources().getColor(R.color.deepgray);
		
		for (View view : tvs) {
			TextView tv = (TextView)view;
			if(tv.getId() == v.getId()){
				tv.setTextColor(redColor);
			}else{
				tv.setTextColor(grayColor);
			}
		}
	}
	
	/**
	 * 根据出售中、下架、Order by查询商品信息
	 * @param outPublished
	 * @param orderBy
	 */
	protected void queryMerch(String outPublished, String orderBy){
		String url = HttpUtil.BASE_URL + "/merch/queryby.do";
		MerchInfo merchInfo = new MerchInfo();
		merchInfo.setUser_id(user.getUser_id());
		merchInfo.setOut_published(outPublished);
		merchInfo.setOrder_by_clause(orderBy);
		
		boolean success = false;
		try {
			String json = HttpUtil.getRequest(url, merchInfo);
			List<MerchInfo> list = JsonUtil.parse2ListMerchInfo(json);
			mListView.setAdapter(new MerchListAdapter(ShopActivity.this, list));//刷新ListView
			success = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		if(!success){
			Toast.makeText(ShopActivity.this, "服务器响应异常,请稍后再试!", Toast.LENGTH_LONG).show();
		}
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
				Intent intent1 = new Intent(ShopActivity.this, GoodsActivity.class);
				intent1.putExtra("from", "shop_home");
				intent1.putExtra("store_id", storeInfo.getStore_id());
				startActivityForResult(intent1, 100201);
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
			String url1 = HttpUtil.BASE_URL + "/store/querybyuser.do?user_id="+user.getUser_id();
			//查询商家收藏数
			String url2 = HttpUtil.BASE_URL + "/storeuf/count.do?store_id=";
			//根据用户查询商品列表
			String url3 = HttpUtil.BASE_URL + "/merch/querybyuserid.do?user_id="+user.getUser_id();
			
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
				
				if(storeInfo == null){
					//TODO 当还没有商家的时候，是否要跳转到创建商家页面中
					map.put("STATUS", TipFlag.NETWORK_ERROR);
					return map;
				}
				
				url2 += storeInfo.getStore_id();
				
				favoriteCount = restTemplate.getForObject(url2, Integer.class);
				result = restTemplate.getForObject(url3, String.class);
				JsonFactory factory = new JsonFactory();
				ObjectMapper mapper = new ObjectMapper(factory); 
				TypeReference<List<MerchInfo>> typeRef = new TypeReference<List<MerchInfo>>() {};
				//JavaType javaType = mapper.getTypeFactory().constructParametrizedType(ArrayList.class,ArrayList.class,MerchInfo.class);
				list = (List<MerchInfo>)mapper.readValue(result, typeRef);
				
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
