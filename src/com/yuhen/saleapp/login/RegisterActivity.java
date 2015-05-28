package com.yuhen.saleapp.login;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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
import com.yuhen.saleapp.activity.BaseActivity;
import com.yuhen.saleapp.domain.User;
import com.yuhen.saleapp.shop.ShopDetailActivity;
import com.yuhen.saleapp.util.HttpUtil;
import com.yuhen.saleapp.util.SecureUtil;
import com.yuhen.saleapp.util.TipFlag;
import com.yuhen.saleapp.util.Validator;

public class RegisterActivity extends BaseActivity {
	
	private UserRegisterTask mAuthTask = null;

	private EditText mMobileView;
	private EditText mPasswordView;
	private EditText mComfirmPasswordView;
	
	private View mProgressView;
	private View mTlRegisterView;
//	private View mRegisterBackView;
//	private View mRegisterTitleView;
	
	@Override
	public void initView() {
		super.initView();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initView();
		head_center_text.setText("注册");
		head_right_text.setText("");
		
		mMobileView = (EditText) findViewById(R.id.et_mobile);
		mPasswordView = (EditText) findViewById(R.id.et_password);
		mComfirmPasswordView = (EditText) findViewById(R.id.et_comfirm_password);
		mProgressView = findViewById(R.id.register_progress);
		mTlRegisterView = findViewById(R.id.sv_register);
//		mRegisterBackView = findViewById(R.id.register_back);
//		mRegisterTitleView = findViewById(R.id.register_title);
		
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
		mComfirmPasswordView.setError(null);
		
		String mobile = mMobileView.getText().toString();
		String password = mPasswordView.getText().toString();
		String comfirmPassword = mComfirmPasswordView.getText().toString();

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
		}else if(!password.equals(comfirmPassword)){
			mComfirmPasswordView.setError(getString(R.string.error_comfirm_password));
			focusView = mComfirmPasswordView;
			cancel = true;
		}
		
		if (cancel) {
			// 有错误，不登录，焦点在错误的输入框中，并显示错误
			focusView.requestFocus();
		} else {
			//显示进度条，并发送登录请求，准备登录
			showProgress(true);
			mAuthTask = new UserRegisterTask(mobile, SecureUtil.shaEncode(password));
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
//			mRegisterTitleView.setVisibility(show ? View.GONE : View.VISIBLE);
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mTlRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
//			mRegisterTitleView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	/**
	 * 异步发送HTTP请求注册
	 */
	public class UserRegisterTask extends AsyncTask<Void, Void, Integer> {

		private final String mMobile;
		private final String mPassword;
		
		private String mTipInfo = "";

		UserRegisterTask(String mobile, String password) {
			mMobile = mobile;
			mPassword = password;
		}

		@Override
		protected Integer doInBackground(Void... params) {
			String url = HttpUtil.BASE_URL + "/user/register.do";
			
			// 创建 RestTemplate 实例
			RestTemplate restTemplate = new RestTemplate();

			User user = new User();
			user.setMobile(mMobile);
			user.setPassword(mPassword);
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
				Intent intent = new Intent(RegisterActivity.this, ShopDetailActivity.class);
				intent.putExtra("mobile", mMobileView.getText().toString());
				intent.putExtra("password", mPasswordView.getText().toString());
				intent.putExtra("from", "register");
//				RegisterActivity.this.setResult(HttpStatus.OK.value(), intent);
				startActivity(intent);
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
