package com.yuhen.saleapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.yuhen.saleapp.shop.ShopActivity;


/**
 * 功能描述：首页
 * @author ps
 */
public class MainActivity extends Activity{
    
	private View mMyShopView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mMyShopView = findViewById(R.id.myShop);
		mMyShopView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ShopActivity.class);
				startActivity(intent);
			}
		});
	}
	
	
}
