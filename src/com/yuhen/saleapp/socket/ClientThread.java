/**
 * 
 */
package com.yuhen.saleapp.socket;

import com.bgpublish.socket.FileSerial;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 客户端线程
 * @author ps
 *
 */
public class ClientThread implements Runnable {
	private Client client;
	//定义向UI发送信息的handler对象
	private Handler handler;
	//定义接收UI信息的handler对象
	private Handler revHandler;
	
	public ClientThread(Handler handler) {
		this.handler = handler;
		this.client = new Client();
	}
	public ClientThread(Client client,Handler handler) {
		this.handler = handler;
		this.client = client;
	}

	public Handler getRevHandler() {
		return revHandler;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		new Thread(){
			public void run() {
				FileSerial receive = client.receive();
				while(receive != null){
					Message msg = new Message();
					msg.what = 0x123;
					msg.obj = receive;
					handler.sendMessage(msg);
				}
			};
		}.start();
		
		//为当前线程初始化Looper
		Looper.prepare();
		revHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 0x345){
					client.send((FileSerial)msg.obj);
				}
				
			}
		};
		
		Looper.loop();
	}

}
