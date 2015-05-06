/**
 * 
 */
package com.yuhen.saleapp.shop;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
import android.widget.EditText;
import android.widget.Toast;

import com.yuhen.saleapp.R;
import com.yuhen.saleapp.menu.ActionItem;
import com.yuhen.saleapp.menu.TitlePopup;
import com.yuhen.saleapp.menu.TitlePopup.OnItemOnClickListener;

public class ShopActivity extends Activity {
	
	private View imageView;
	private EditText mSearchView;
	private Drawable mIconSearchDefault; // 搜索文本框默认图标
    private Drawable mIconSearchClear; // 搜索文本框清除文本内容图标
	/**
	 * 右上角上下文菜单
	 */
	TitlePopup titlePopup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);
		
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
				Toast.makeText(ShopActivity.this, "开始进行【点击主页】的操作\n可以弹出对话框,或者跳转等", 1).show();
				break;
			case R.string.edit_my_merch:
				Toast.makeText(ShopActivity.this, "开始进行【人员定位】的操作\n可以弹出对话框,或者跳转等", 1).show();
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
