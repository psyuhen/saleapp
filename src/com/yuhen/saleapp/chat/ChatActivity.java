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
		head_center_text.setText("�����������");
		sessionManager = new SessionManager(getApplicationContext());
		final User user  = sessionManager.getUserDetails();
		
		client = new Client();
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 0x123){
					//TODO ˢ�����촰����Ϣ��δ���
					FileSerial fileSerial = (FileSerial)msg.obj;
					
					ChatInfo msgItem = new ChatInfo();
					msgItem.setComing(true);
					msgItem.setName("�Ʊ�");
					msgItem.setSendToUser("ղķ˹");
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
	 * ��ȡ������Ϣ
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
				msgItem.setName("�Ʊ�");
				msgItem.setSendToUser("ղķ˹");
				msgItem.setText("�ף���֪����ϲ����һ�ϲ������");
			}
			if (i % 2 == 1) {
				msgItem.setComing(false);
				msgItem.setName("ղķ˹");
				msgItem.setSendToUser("�Ʊ�");
				msgItem.setText("��ϲ����һ���������һ���֪������û�б��ʲô�Ż�!���ڸ����Ƽ�����ȽϺõĶ�����");
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
