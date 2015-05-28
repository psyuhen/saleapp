/**
 * 
 */
package com.yuhen.saleapp.sell;

import android.os.Bundle;

import com.yuhen.saleapp.R;
import com.yuhen.saleapp.activity.BaseActivity;

/**
 * 销售管理
 * @author ps
 *
 */
public class SellMgrActivity extends BaseActivity {

	@Override
	public void initView() {
		super.initView();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sell);
		initView();
		head_center_text.setText("销售管理");
	}
}
