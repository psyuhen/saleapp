package com.yuhen.saleapp.chat;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.bgpublish.socket.FileSerial;
import com.yuhen.saleapp.R;
import com.yuhen.saleapp.activity.BaseActivity;
import com.yuhen.saleapp.domain.ChatInfo;
import com.yuhen.saleapp.domain.User;
import com.yuhen.saleapp.session.SessionManager;
import com.yuhen.saleapp.socket.Client;
import com.yuhen.saleapp.socket.ClientThread;

public class ChatActivity extends BaseActivity {
	private ListView mMsgListView;
	private List<ChatInfo> msgList;
	private ChatMsgViewAdapter mAapter;
	private ClientThread clientThread;
	private Client client;
	private Handler handler;
	private Button btnSendMsg;
	private EditText etInputMsg;
	
	private SessionManager sessionManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		initView();
		initData();
		head_center_text.setText("与买家聊天中");
		sessionManager = new SessionManager(getApplicationContext());
		final User user  = sessionManager.getUserDetails();
		
		client = new Client();
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 0x123){
					//TODO 刷新聊天窗口信息，未完成
					FileSerial fileSerial = (FileSerial)msg.obj;
					
					ChatInfo msgItem = new ChatInfo();
					msgItem.setComing(true);
					msgItem.setName("科比");
					msgItem.setSendToUser("詹姆斯");
					msgItem.setText(fileSerial.getFileName());
					msgList.add(msgItem);
					
					mAapter=new ChatMsgViewAdapter(ChatActivity.this, msgList);
					mMsgListView.setAdapter(mAapter);
				}
			}
		};
		clientThread = new ClientThread(client,handler);
		
		new Thread(clientThread).start();
		
		btnSendMsg = (Button)findViewById(R.id.chat_send_message_btn);
		etInputMsg = (EditText)findViewById(R.id.send_edit_msg);
		btnSendMsg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.what = 0x345;
				FileSerial fileSerial = new FileSerial();
				fileSerial.setType(FileSerial.TYPE_TEXT);
				fileSerial.setFromUser(user.getMobile());
//				fileSerial.setToUser(toUser);
				fileSerial.setFileName(etInputMsg.getText().toString());
				
				msg.obj = fileSerial;
				clientThread.getRevHandler().sendMessage(msg);
				etInputMsg.setText("");
			}
		});
	}
	
	/**
	 * 获取离线信息
	 */
	public void getOfflineMsg(){
		
	}

	private void initData() {
		msgList = new ArrayList<ChatInfo>();
		for (int i = 0; i < 10; i++) {
			ChatInfo msgItem = new ChatInfo();
			msgItem.setDate("2009-09-09");
			if (i % 2 == 0) {
				msgItem.setComing(true);
				msgItem.setName("科比");
				msgItem.setSendToUser("詹姆斯");
				msgItem.setText("亲，不知道你喜欢这一款不喜欢啊！");
			}
			if (i % 2 == 1) {
				msgItem.setComing(false);
				msgItem.setName("詹姆斯");
				msgItem.setSendToUser("科比");
				msgItem.setText("我喜欢这一款！我想买这一款，不知道还有没有别的什么优惠!请在给我推荐几款比较好的东西！");
			}
			msgList.add(msgItem);

		}
	}

	@Override
	public void initView() {
		super.initView();
		mMsgListView = (ListView) findViewById(R.id.chat_msg_listview);
	}
}
