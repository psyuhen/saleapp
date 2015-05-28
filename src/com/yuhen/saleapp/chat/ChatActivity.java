package com.yuhen.saleapp.chat;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

import com.yuhen.saleapp.R;
import com.yuhen.saleapp.activity.BaseActivity;
import com.yuhen.saleapp.domain.ChatInfo;

public class ChatActivity extends BaseActivity {
	private ListView mMsgListView;
	private List<ChatInfo> msgList;
	private ChatMsgViewAdapter mAapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		initView();
		initData();
		head_center_text.setText("�����������");
		mAapter=new ChatMsgViewAdapter(ChatActivity.this, msgList);
		mMsgListView.setAdapter(mAapter);

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
