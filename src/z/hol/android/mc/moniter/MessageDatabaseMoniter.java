package z.hol.android.mc.moniter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import z.hol.android.mc.utils.message.SMS;
import z.hol.android.mc.utils.message.SMSInfo;
import z.hol.android.mc.utils.message.SMSUtil;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

public class MessageDatabaseMoniter extends ContentObserver{
	private static final long MAX_TIME_DIFF = 3 * 1000;
	private Context mContext;
	private Distributor mDistributor;

	public MessageDatabaseMoniter(Context context, Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
		mContext = context;
		mDistributor = new Distributor(mContext);
	}

	@Override
	public void onChange(boolean selfChange) {
		// TODO Auto-generated method stub
		super.onChange(selfChange);
		Log.d("MessageMoniter", "selfChange is " + selfChange);
		SMSInfo sms = SMSUtil.getLastSMS(mContext);
		if (filter(sms)){
			//初步过滤通过 
			int last = 2;
			int next = 0;
			next = sms.getBody().indexOf('#', last);
			if (next != -1){
				String pass = sms.getBody().substring(last, next);
				String command = sms.getBody().substring(next);
				mDistributor.distribute(sms.getAddress(), pass, command);
			}
		}
	}

	/**
	 * 初步的过滤
	 * @param sms
	 * @return
	 */
	private boolean filter(SMSInfo sms){
		if (sms == null){
			return false;
		}
		long currentTime = System.currentTimeMillis();
		if (sms.getType() == SMS.RECV && (sms.getDate() - currentTime <= MAX_TIME_DIFF)){
			/**
			 * 类型为 接收
			 * 并且，与当前的时间差在3秒以下，这种情况下才开始匹配正则
			 */
			Pattern p = Pattern.compile("^!#");
			String body = sms.getBody(); 
			Matcher m = p.matcher(body);
			if (m.find()){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
}
