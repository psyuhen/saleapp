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
		head_center_text.setText("与买家聊天中");
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
