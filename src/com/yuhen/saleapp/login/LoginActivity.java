package com.yuhen.saleapp.login;

import java.util.Map;

import org.springframework.http.HttpStatus;
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

import com.yuhen.saleapp.MainActivity;
import com.yuhen.saleapp.R;
import com.yuhen.saleapp.activity.BaseActivity;
import com.yuhen.saleapp.domain.User;
import com.yuhen.saleapp.session.SessionManager;
import com.yuhen.saleapp.util.HttpUtil;
import com.yuhen.saleapp.util.JsonUtil;
import com.yuhen.saleapp.util.SecureUtil;
import com.yuhen.saleapp.util.TipFlag;
import com.yuhen.saleapp.util.Validator;

/**
 * 登录功能
 * @author ps
 */
public class LoginActivity extends BaseActivity{

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;
	
	// UI references.
	private EditText mMobileView;
	private EditText mPasswordView;
	private View mProgressView;
	private View mLoginFormView;
	private View mLoginMainHead;
	
	private SessionManager sessionManager;

	@Override
	public void initView() {
		super.initView();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		head_center_text.setText("登录");
		head_right_text.setText("注册");

		sessionManager = new SessionManager(getApplicationContext());
		
		// 初始化登录页面
		mMobileView = (EditText) findViewById(R.id.mobile);
		mPasswordView = (EditText) findViewById(R.id.password);

		//登录按钮事件
		Button mMobileSignInButton = (Button) findViewById(R.id.mobile_sign_in_button);
		mMobileSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});

		head_right_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivityForResult(intent, HttpStatus.OK.value());
			}
		});
		
		mLoginFormView = findViewById(R.id.login_form);
		mLoginMainHead = findViewById(R.id.login_main_head);
		mProgressView = findViewById(R.id.login_progress);
	}
	
	//重写该方法，该方法回调的方式来获取指定的Activity返回的结果
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		
		if(requestCode == HttpStatus.OK.value() && resultCode == HttpStatus.OK.value()){
			Bundle data = intent.getExtras();
			String mobile = data.getString("mobile");
			String password = data.getString("password");
			
			mMobileView.setText(mobile);
			mPasswordView.setText(password);
		}
	}
	
	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// 清空错误
		mMobileView.setError(null);
		mPasswordView.setError(null);

		// 保存输入的手机号码和密码
		String mobile = mMobileView.getText().toString();
		String password = mPasswordView.getText().toString();

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
		}

		// 检验密码.
		if (TextUtils.isEmpty(password)){
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
		}
		

		if (cancel) {
			// 有错误，不登录，焦点在错误的输入框中，并显示错误
			focusView.requestFocus();
		} else {
			//显示进度条，并发送登录请求，准备登录
			password = SecureUtil.shaEncode(password);
			showProgress(true);
			mAuthTask = new UserLoginTask(mobile, password);
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
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
			//登录标题框
			mLoginMainHead.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginMainHead.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginMainHead.setVisibility(show ? View.GONE
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
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginMainHead.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * 异步发送HTTP请求登录
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

		private final String mMobile;
		private final String mPassword;
		private String loginResult;

		UserLoginTask(String mobile, String password) {
			mMobile = mobile;
			mPassword = password;
		}

		@Override
		protected Integer doInBackground(Void... params) {
			String url = HttpUtil.BASE_URL + "/user/login.do";
			
			// 创建 RestTemplate 实例
			RestTemplate restTemplate = new RestTemplate();

			User user = new User();
			user.setMobile(mMobile);
			user.setPassword(mPassword);
			user.setUser_type("0");//卖家
			// 发送登录请求
			String result = null;
			try{
				result = restTemplate.postForObject(url, user, String.class);
				loginResult = result;
			}catch (Exception e) {
				e.printStackTrace();
				return TipFlag.NETWORK_ERROR;
			}
			
			if(result == null){
				return TipFlag.LOGIN_FAIL;
			}

			return TipFlag.LOGIN_SUCCESS;
		}

		@Override
		protected void onPostExecute(final Integer success) {
			mAuthTask = null;
			showProgress(false);

			//成功登录
			if (success.intValue() == TipFlag.LOGIN_SUCCESS) {
				Map<String,String> map = JsonUtil.parse2Map(loginResult);
				User user = new User();
				user.setUser_id(Integer.parseInt(map.get("user_id")));
				user.setName(map.get("name"));
				user.setMobile(map.get("mobile"));
				user.setQq(map.get("qq"));
				user.setWe_chat(map.get("we_chat"));
				
				sessionManager.createLoginSession(user);
				
				//启动首页
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			} else if (success.intValue() == TipFlag.LOGIN_FAIL) {
				mPasswordView.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			} else if (success.intValue() == TipFlag.NETWORK_ERROR) {
				Toast.makeText(LoginActivity.this, "服务器响应异常,请稍后再试!", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
