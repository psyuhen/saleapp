/**
 * 
 */
package com.yuhen.saleapp.goods;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.yuhen.saleapp.R;
import com.yuhen.saleapp.session.SessionManager;

/**
 * @author ps
 *
 */
public class GoodsActivity extends FragmentActivity {

	/**
	 * 具体内容
	 */
	private ViewPager mViewPager;
	public TextView head_left_text;
	public TextView head_center_text;
	public TextView head_right_text;
	/**
	 * 三个按钮组：出售中，已下架，分类
	 */
	private RadioGroup mRadioGroup;
	private GoodsFragmentAdapter goodsFragmentAdapter;
	
	protected SessionManager sessionManager ;
	
	public void initView() {
		head_left_text=(TextView) findViewById(R.id.head_left_text);
		head_center_text=(TextView) findViewById(R.id.head_center_text);
		head_right_text=(TextView) findViewById(R.id.head_right_text);
		
		if(head_left_text != null){
			head_left_text.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods);
		initView();
		head_center_text.setText("我的商品");
		sessionManager = new SessionManager(getApplicationContext());
		
		/** 选项卡  **/
		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkId) {
				switch (checkId) {
				case R.id.radio0:
					mViewPager.setCurrentItem(0);
					break;
				case R.id.radio1:
					mViewPager.setCurrentItem(1);
					break;
				case R.id.radio2:
					mViewPager.setCurrentItem(2);
					break;
				}

			}
		});
		
		/** 具体内容  **/
		mViewPager = (ViewPager) findViewById(R.id.vp_goods_list);
		goodsFragmentAdapter = new GoodsFragmentAdapter(getSupportFragmentManager(), GoodsActivity.this);
		mViewPager.setAdapter(goodsFragmentAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					mRadioGroup.check(R.id.radio0);
					break;
				case 1:
					mRadioGroup.check(R.id.radio1);
					break;
				case 2:
					mRadioGroup.check(R.id.radio2);
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
}
