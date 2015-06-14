/**
 * 
 */
package com.yuhen.saleapp.chat;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
  

public abstract class ChatAsyncQueryHandler extends Handler {  
  
    private static final String TAG = "ChatAsyncQueryHandler";  
  
    private ChatDatabase mDatabase;  
  
    private static Looper mLooper = null;  
    private Handler mWorkerHandler = null;  
  
    private static final int EVENT_QUERY = 0;  
    private static final int EVENT_INSERT = 1;  
    private static final int EVENT_UPDATE = 2;  
    private static final int EVENT_DELECT = 3;  
  
  
    public ChatAsyncQueryHandler(ChatDatabase database) {  
        mDatabase = database;  
        synchronized (ChatAsyncQueryHandler.class) {  
            if (mLooper == null) {  
                HandlerThread thread = new HandlerThread("ChatAsyncQueryHandler");  
                thread.start();  
                mLooper = thread.getLooper();  
            }  
        }  
        mWorkerHandler = new WorkerHandler(mLooper);  
    }  
  
    protected static final class WorkerArgs {  
  
        public Handler handler;  
        public String[] projection;  
        public String selection;  
        public ContentValues values;  
  
        public Object result;  
    }  
  
    /** 
     * 查询。通过两个字段，查询ID,DATA,TIME,TYPE,SELF,CONTENT 
     *  
     * @param token 
     * @param localName 
     * @param remoteName 
     */  
    public void startQuery(int token, String localName, String remoteName) {  
        Log.d(TAG, "startQuery, token=" + token + ",localName=" + localName + ",remoteName=" + remoteName);  
        Message msg = mWorkerHandler.obtainMessage(token);  
        msg.arg1 = EVENT_QUERY;  
        WorkerArgs args = new WorkerArgs();  
        args.handler = this;  
        args.projection = new String[] {"id","chat_date","chat_time","chat_type",
        		"from_user","from_user_name","to_user","to_user_name","chat_content"};  
        args.selection = "from_user =\"" + localName + "\" AND to_user =\"" + remoteName + "\"";  
        msg.obj = args;  
        mWorkerHandler.sendMessage(msg);  
    }  
  
    public void startInsert(int token, ContentValues values) {  
        Log.d(TAG, "startInsert, values=" + values);  
        Message msg = mWorkerHandler.obtainMessage(token);  
        msg.arg1 = EVENT_INSERT;  
        WorkerArgs args = new WorkerArgs();  
        args.handler = this;  
        args.values = values;  
        msg.obj = args;  
        mWorkerHandler.sendMessage(msg);  
    }  
  
    protected class WorkerHandler extends Handler {  
  
        public WorkerHandler(Looper looper) {  
            super(looper);  
        }  
  
        @Override  
        public void handleMessage(Message msg) {  
            WorkerArgs args = (WorkerArgs) msg.obj;  
            int token = msg.what;  
            int event = msg.arg1;  
  
            switch (event) {  
            case EVENT_QUERY:  
                args.result = mDatabase.query(args.projection, args.selection);  
                break;  
            case EVENT_INSERT:  
                mDatabase.insert(args.values);  
                break;  
            case EVENT_UPDATE:  
                break;  
            case EVENT_DELECT:  
                break;  
            }  
  
            Message reply = args.handler.obtainMessage(token);  
            reply.obj = args;  
            reply.arg1 = event;  
            reply.sendToTarget();  
        }  
  
    }  
  
    protected void onQueryCompleted(int token, Cursor cursor) {  
        // empty  
    }  
  
    protected void onInsertCompleted(int token) {  
        // empty  
    }  
  
    @Override  
    public void handleMessage(Message msg) {  
        WorkerArgs args = (WorkerArgs) msg.obj;  
        int token = msg.what;  
        int event = msg.arg1;  
        switch (event) {  
        case EVENT_QUERY:  
            onQueryCompleted(token, (Cursor) args.result);  
            break;  
        case EVENT_INSERT:  
            onInsertCompleted(token);  
            break;  
        case EVENT_UPDATE:  
            break;  
        case EVENT_DELECT:  
            break;  
        }  
    }  
}  