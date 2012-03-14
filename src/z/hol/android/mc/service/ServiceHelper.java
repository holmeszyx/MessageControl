package z.hol.android.mc.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class ServiceHelper{
	private static final String TAG = "ServiceHelper";
	private Context mContext;
	private OnBindDoneCallBack onBindDone;
	private boolean mIsBinded = false;
	
	
	private MessageControlService serviceInstance;
	private ServiceConnection mServiceConn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			serviceInstance = ((MessageControlService.MyBinder)service).getService();
			setBinded(true);
			if (onBindDone != null){
				onBindDone.onDone();
			}
			//System.out.println("get instance");
		}
	};
	
	public ServiceHelper(Context context){
		this(context, null);
	}
	
	public ServiceHelper(Context context, OnBindDoneCallBack onBindDone){
		this.mContext = context;
		this.onBindDone = onBindDone;
	}
	
	public void bindService(){
		mContext.bindService(new Intent(mContext, MessageControlService.class), mServiceConn, Context.BIND_AUTO_CREATE);
		Log.d(TAG, "bind");
	}
	
	public void unbindService(){
		if (isBinded()){
			mContext.unbindService(mServiceConn);
			setBinded(false);
		}
		Log.d(TAG, "unbind");
	}
	
	public void startService(){
		Intent serviceIntent =  new Intent(mContext, MessageControlService.class);
		mContext.startService(serviceIntent);
	}
	
	public void stopService(){
		Intent serviceIntent =  new Intent(mContext, MessageControlService.class);
		mContext.stopService(serviceIntent);
	}
	
	public void setOnBindDoneCallBack(OnBindDoneCallBack onBindDone){
		this.onBindDone = onBindDone;
	}
	
	public void removeOnBindDoneCallBack(){
		this.onBindDone = null;
	}
	
	public MessageControlService serviceInstance(){
		return this.serviceInstance;
	}
	
	public synchronized boolean isBinded(){
		return mIsBinded;
	}
	
	private synchronized void setBinded(boolean binded){
		mIsBinded = binded;
	}
	
	/**
	 * 绑定完成后，也就是当可以得到一个service的时候，会进行回调
	 * @author holmes
	 *
	 */
	public interface OnBindDoneCallBack{
		
		/**
		 * serivce绑定后执行
		 */
		public void onDone();
	}
	
}
