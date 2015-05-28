/**
 * 
 */
package com.yuhen.saleapp.domain;

/**
 * @author ps
 * 
 */
public class ChatInfo {
	private String name;
	private String text;
	private String date;
	private String sendToUser;
	private int msgType;
	private boolean isComing = true;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isComing() {
		return isComing;
	}

	public void setComing(boolean isComing) {
		this.isComing = isComing;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSendToUser() {
		return sendToUser;
	}

	public void setSendToUser(String sendToUser) {
		this.sendToUser = sendToUser;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public ChatInfo(String name, String text, String date, String sendToUser,
			int msgType, boolean isComing) {
		super();
		this.name = name;
		this.text = text;
		this.date = date;
		this.sendToUser = sendToUser;
		this.msgType = msgType;
		this.isComing = isComing;
	}

	public ChatInfo() {
		super();
	}
}
