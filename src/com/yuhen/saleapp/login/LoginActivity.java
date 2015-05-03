package com.yuhen.saleapp.login;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yuhen.saleapp.MainActivity;
import com.yuhen.saleapp.R;
import com.yuhen.saleapp.domain.User;
import com.yuhen.saleapp.util.HttpUtil;
import com.yuhen.saleapp.util.SecureUtil;
import com.yuhen.saleapp.util.TipFlag;
import com.yuhen.saleapp.util.Validator;

/**
 * 登录功能
 * @author ps
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;
	
	// UI references.
	private AutoCompleteTextView mMobileView;
	private EditText mPasswordView;
	private View mProgressView;
	private View mLoginFormView;
	private View mLoginTitleView;
	private View mRegisterTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// 初始化登录页面
		mMobileView = (AutoCompleteTextView) findViewById(R.id.mobile);
		populateAutoComplete();

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		//登录按钮事件
		Button mMobileSignInButton = (Button) findViewById(R.id.mobile_sign_in_button);
		mMobileSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});

		mRegisterTextView = findViewById(R.id.register_text);
		mRegisterTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivityForResult(intent, HttpStatus.OK.value());
			}
		});
		
		mLoginFormView = findViewById(R.id.login_form);
		mLoginTitleView = findViewById(R.id.login_title);
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
	
	private void populateAutoComplete() {
		if (VERSION.SDK_INT >= 14) {
			// Use ContactsContract.Profile (API 14+)
			getLoaderManager().initLoader(0, null, this);
		} else if (VERSION.SDK_INT >= 8) {
			// Use AccountManager (API 8+)
			new SetupMobileAutoCompleteTask().execute(null, null);
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
			mLoginTitleView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginTitleView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginTitleView.setVisibility(show ? View.GONE
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
			mLoginTitleView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		//获取联系人的手机号码
		return new CursorLoader(this,
				// Retrieve data rows for the device user's 'profile' contact.
				Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
						ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
				ProfileQuery.PROJECTION,

				// Select only mobile number.
				ContactsContract.Contacts.Data.MIMETYPE + " = ?",
				new String[] { ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE },

				// Show primary mobile number first. Note that there won't be
				// a primary mobile number if the user hasn't specified one.
				ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		//遍历查询结果，获取该联系人的多个号码
		List<String> mobiles = new ArrayList<String>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			mobiles.add(cursor.getString(ProfileQuery.NUMBER));
			cursor.moveToNext();
		}

		addMobilesToAutoComplete(mobiles);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursorLoader) {

	}

	private interface ProfileQuery {
		String[] PROJECTION = { ContactsContract.CommonDataKinds.Phone.NUMBER,
				ContactsContract.CommonDataKinds.Phone.IS_PRIMARY, };

		int NUMBER = 0;
		int IS_PRIMARY = 1;
	}

	/**
	 * 异步获取用户的手机号码，并在手机号码输入框中显示出来
	 */
	class SetupMobileAutoCompleteTask extends
			AsyncTask<Void, Void, List<String>> {

		@Override
		protected List<String> doInBackground(Void... voids) {
			ArrayList<String> moblieCollection = new ArrayList<String>();

			// Get all mobiles from the user's contacts and copy them to a list.
			ContentResolver cr = getContentResolver();
			Cursor mobileCur = cr.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					null, null, null);
			while (mobileCur.moveToNext()) {
				String mobile = mobileCur
						.getString(mobileCur
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				moblieCollection.add(mobile);
			}
			mobileCur.close();

			return moblieCollection;
		}

		@Override
		protected void onPostExecute(List<String> moblileCollection) {
			addMobilesToAutoComplete(moblileCollection);
		}
	}

	private void addMobilesToAutoComplete(List<String> mobileCollection) {
		// Create adapter to tell the AutoCompleteTextView what to show in its
		// dropdown list.
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				LoginActivity.this,
				android.R.layout.simple_dropdown_item_1line,
				mobileCollection);

		mMobileView.setAdapter(adapter);
	}

	/**
	 * 异步发送HTTP请求登录
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

		private final String mMobile;
		private final String mPassword;

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
