package com.yuhen.saleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.yuhen.saleapp.activity.BaseActivity;
import com.yuhen.saleapp.goods.GoodsActivity;
import com.yuhen.saleapp.order.OrderActivity;
import com.yuhen.saleapp.sell.SellMgrActivity;
import com.yuhen.saleapp.shop.ShopActivity;


/**
 * 功能描述：首页
 * @author ps
 */
public class MainActivity extends BaseActivity{
    
	private View mMyOrderView;
	private View mMyShopView;
	private View mMyGoodsView;
	private View mSellMgrView;
	
	@Override
	public void initView() {
		super.initView();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		head_left_text.setText("退出");
		head_center_text.setText("首页");
		
		mMyOrderView = findViewById(R.id.queryOrder);
		mMyOrderView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, OrderActivity.class);
				startActivity(intent);
			}
		});
		
		mMyGoodsView = findViewById(R.id.editGoods);
		mMyGoodsView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, GoodsActivity.class);
				startActivity(intent);
			}
		});
		
		mMyShopView = findViewById(R.id.myShop);
		mMyShopView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ShopActivity.class);
				startActivity(intent);
			}
		});
		
		mSellMgrView = findViewById(R.id.marketMgr);
		mSellMgrView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SellMgrActivity.class);
				startActivity(intent);
			}
		});
	}
	
	
}
