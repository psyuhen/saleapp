/**
 * 
 */
package com.yuhen.saleapp.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yuhen.saleapp.R;

/**
 * @author ps
 *
 */
public class QQLoginActivity  extends Activity implements OnClickListener{
	TextView openidTextView;
    TextView nicknameTextView;
    Button loginButton;
    ImageView userlogo;
	
    public static QQAuth mQQAuth;
    public static String openidString;
    public static String nicknameString;
    public static String TAG="QQLoginActivity";
    Bitmap bitmap = null;
	
	public static Tencent mTencent;
	public static String mAppid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_qq_login);
		//用来登录的Button
        loginButton=(Button)findViewById(R.id.login);
        loginButton.setOnClickListener(this);
        //用来显示OpenID的textView
        openidTextView=(TextView)findViewById(R.id.user_openid);
        //用来显示昵称的textview
        nicknameTextView=(TextView)findViewById(R.id.user_nickname);
       //用来显示头像的Imageview
        userlogo=(ImageView)findViewById(R.id.user_logo);
		
		
//		setTitle(null);
//		setLeftButton("返回", new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
	}
	public void onClick(View v) {
        switch (v.getId()) {
        case R.id.login:
        	try{
        		LoginQQ();
        	}catch (Exception e) {
        		Log.e(TAG, "login error==========",e);
			}
            
            break;
 
        default:
            break;
        }
    }
	
	//这里是调用QQ登录的关键代码
    public void LoginQQ() {
        //这里的APP_ID请换成你应用申请的APP_ID，我这里使用的是DEMO中官方提供的测试APP_ID 222222
        mAppid = "1104500255";
//        mAppid = "222222";
        //第一个参数就是上面所说的申请的APPID，第二个是全局的Context上下文，这句话实现了调用QQ登录
        mTencent = Tencent.createInstance(mAppid,QQLoginActivity.this);
        /**通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，第二个参数SCOPO 是一个String类型的字符串，表示一些权限 
        官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。例如：SCOPE = “get_user_info,add_t”；所有权限用“all”  
        第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类 */
        mTencent.login(QQLoginActivity.this,"all", new BaseUiListener());
             
    }
    /**当自定义的监听器实现IUiListener接口后，必须要实现接口的三个方法，
     * onComplete  onCancel onError 
     *分别表示第三方登录成功，取消 ，错误。*/
    private class BaseUiListener implements IUiListener {
 
        public void onCancel() {
             
        }
        public void onComplete(Object response) {
            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_LONG).show();
            try {
                //获得的数据是JSON格式的，获得你想获得的内容
                //如果你不知道你能获得什么，看一下下面的LOG
                Log.e(TAG, "-------------"+response.toString());
                openidString = ((JSONObject) response).getString("openid");
                openidTextView.setText(openidString);
                Log.e(TAG, "-------------"+openidString);
                //access_token= ((JSONObject) response).getString("access_token");              //expires_in = ((JSONObject) response).getString("expires_in");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /**到此已经获得OpneID以及其他你想获得的内容了
            QQ登录成功了，我们还想获取一些QQ的基本信息，比如昵称，头像什么的，这个时候怎么办？ 
            sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息，我么可以通过这个类拿到这些信息 
            如何得到这个UserInfo类呢？  */
            QQToken qqToken = mTencent.getQQToken();
            UserInfo info = new UserInfo(getApplicationContext(), qqToken);
            //这样我们就拿到这个类了，之后的操作就跟上面的一样了，同样是解析JSON   
            info.getUserInfo(new IUiListener() {
            	 
                public void onComplete(final Object response) {
                    Log.e(TAG, "---------------111111");
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                    Log.e(TAG, "-----111---"+response.toString());
                    /**由于图片需要下载所以这里使用了线程，如果是想获得其他文字信息直接
                     * 在mHandler里进行操作
                     * 
                     */
                    new Thread(){
 
                        @Override
                        public void run() {
                            JSONObject json = (JSONObject)response;
                            try {
                                bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Message msg = new Message();
                            msg.obj = bitmap;
                            msg.what = 1;
                            mHandler.sendMessage(msg);
                        }                       
                    }.start();
                }               
                public void onCancel() {
                    Log.e(TAG, "--------------111112");
                }
                public void onError(UiError arg0) {
                    Log.e(TAG, "-111113"+":"+arg0);
                }
                 
            });
             
        }
 
        public void onError(UiError arg0) {
             
        }           
         
    }
    Handler mHandler = new Handler() {
 
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                if (response.has("nickname")) {
                    try {
                        nicknameString=response.getString("nickname");
                         
                        nicknameTextView.setText(nicknameString);
                        Log.e(TAG, "--"+nicknameString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else if(msg.what == 1){
                Bitmap bitmap = (Bitmap)msg.obj;
                userlogo.setImageBitmap(bitmap);
                 
            }
        }
 
    };
}
