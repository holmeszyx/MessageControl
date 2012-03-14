package z.hol.android.mc.service;

import z.hol.android.mc.moniter.MessageDatabaseMoniter;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

public class MessageControlService extends Service{
	
	private MessageDatabaseMoniter messageDatabaseMoniter;
	
	private MyBinder mBinder = new MyBinder();

	public class MyBinder extends Binder{
		public MessageControlService getService(){
			return MessageControlService.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if (messageDatabaseMoniter == null){
			messageDatabaseMoniter = new MessageDatabaseMoniter(this, new Handler());
		}
		bindOb();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindOb();
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	private void bindOb(){
		getContentResolver().registerContentObserver(Uri.parse("content://sms/inbox"), true, messageDatabaseMoniter);
	}
	
	private void unbindOb(){
		if (messageDatabaseMoniter != null){
			getContentResolver().unregisterContentObserver(messageDatabaseMoniter);
		}
	}
	
}
