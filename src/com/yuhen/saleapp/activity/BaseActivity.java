/**
 * 
 */
package com.yuhen.saleapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yuhen.saleapp.R;

public class BaseActivity extends Activity {
	public TextView head_left_text;
	public TextView head_center_text;
	public TextView head_right_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

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
}
