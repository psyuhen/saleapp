package com.yuhen.saleapp.login;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yuhen.saleapp.R;
import com.yuhen.saleapp.domain.User;
import com.yuhen.saleapp.util.HttpUtil;
import com.yuhen.saleapp.util.SecureUtil;
import com.yuhen.saleapp.util.TipFlag;
import com.yuhen.saleapp.util.Validator;

public class RegisterActivity extends Activity {
	
	private UserRegisterTask mAuthTask = null;

	private EditText mMobileView;
	private EditText mPasswordView;
	private EditText mShopNameView;
	private EditText mAddressView;
	
	private View mProgressView;
	private View mTlRegisterView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		mMobileView = (EditText) findViewById(R.id.et_mobile);
		mPasswordView = (EditText) findViewById(R.id.et_password);
		mShopNameView = (EditText) findViewById(R.id.et_shop_name);
		mAddressView = (EditText) findViewById(R.id.et_address);
		mProgressView = findViewById(R.id.register_progress);
		mTlRegisterView = findViewById(R.id.tl_register);
		
		Button btnRegister = (Button) findViewById(R.id.btn_register);
		btnRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptRegister();
			}
		});
	}
	
	//注册
	public void attemptRegister() {
		if (mAuthTask != null) {
			return;
		}
		// 清空错误
		mMobileView.setError(null);
		mPasswordView.setError(null);
		mShopNameView.setError(null);
		mAddressView.setError(null);
		
		String mobile = mMobileView.getText().toString();
		String password = mPasswordView.getText().toString();
		String shopName = mShopNameView.getText().toString();
		String address = mAddressView.getText().toString();

		boolean cancel = false;
		View focusView = null;
		
		// 手机号码为必输入的以及正确输入.
		if (TextUtils.isEmpty(mobile)) {
			mMobileView.setError(getString(R.string.error_field_required));
			focusView = mMobileView;
			cancel = true;
		} else if (!Validator.isMoblie(mobile)) {
			mMobileView.setError(getString(R.string.error_invalid_mobile));
			focusView = mMobileView;
			cancel = true;
		}else if (TextUtils.isEmpty(password)){// 检验密码.
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		}else if(!Validator.isPwdLength(password)){
			mPasswordView.setError(getString(R.string.error_length_password));
			focusView = mPasswordView;
			cancel = true;
		}else if(!Validator.isPassword(password)){
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
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
			// 有错误，不登录，焦点在错误的输入框中，并显示错误
			focusView.requestFocus();
		} else {
			//显示进度条，并发送登录请求，准备登录
			showProgress(true);
			mAuthTask = new UserRegisterTask(mobile, SecureUtil.shaEncode(password), shopName, address);
			mAuthTask.execute((Void) null);
		}
	}
	
	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			//登录框
			mTlRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
			mTlRegisterView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mTlRegisterView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
			//进度条
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mTlRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	/**
	 * 异步发送HTTP请求注册
	 */
	public class UserRegisterTask extends AsyncTask<Void, Void, Integer> {

		private final String mMobile;
		private final String mPassword;
		private final String mShopName;
		private final String mAddress;
		
		private String mTipInfo = "";

		UserRegisterTask(String mobile, String password, String shopName, String address) {
			mMobile = mobile;
			mPassword = password;
			mShopName = shopName;
			mAddress = address;
		}

		@Override
		protected Integer doInBackground(Void... params) {
			String url = HttpUtil.BASE_URL + "/user/register.do";
			
			// 创建 RestTemplate 实例
			RestTemplate restTemplate = new RestTemplate();

			User user = new User();
			user.setMobile(mMobile);
			user.setPassword(mPassword);
			user.setShop_name(mShopName);
			user.setAddress(mAddress);
			user.setUser_type("0");//卖家
			// 发送登录请求
			ResponseEntity<String> postForEntity = null;
			try{
				postForEntity = restTemplate.postForEntity(url, user, String.class);
				mTipInfo = postForEntity.getBody();
			}catch (Exception e) {
				e.printStackTrace();
				return TipFlag.NETWORK_ERROR;
			}
			
			if(HttpStatus.OK.equals(postForEntity.getStatusCode())){
				return TipFlag.LOGIN_SUCCESS;
			}
			
			return TipFlag.REGISTER_FAIL;
		}

		@Override
		protected void onPostExecute(final Integer success) {
			mAuthTask = null;
			showProgress(false);

			//成功登录
			if (success.intValue() == TipFlag.REGISTER_SUCCESS) {
				Toast.makeText(RegisterActivity.this, mTipInfo, Toast.LENGTH_LONG).show();
				//注册成功，返回登录页面
				Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
				intent.putExtra("mobile", mMobileView.getText().toString());
				intent.putExtra("password", mPasswordView.getText().toString());
				RegisterActivity.this.setResult(HttpStatus.OK.value(), intent);
				finish();
			} else if (success.intValue() == TipFlag.REGISTER_FAIL) {
				Toast.makeText(RegisterActivity.this, mTipInfo, Toast.LENGTH_LONG).show();
			} else if (success.intValue() == TipFlag.NETWORK_ERROR) {
				Toast.makeText(RegisterActivity.this, "服务器响应异常,请稍后再试!", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
