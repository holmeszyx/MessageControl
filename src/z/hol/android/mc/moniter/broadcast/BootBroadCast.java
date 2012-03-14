package z.hol.android.mc.moniter.broadcast;

import z.hol.android.mc.service.MessageControlService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootBroadCast extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
			//start service
			SharedPreferences setting = context.getSharedPreferences("mc_setting", 0);
			boolean autoStart = setting.getBoolean("auto_boot", false);
			if (autoStart){				
				//自动启动
				Intent iService = new Intent();
				iService.setClass(context, MessageControlService.class);
				context.startService(iService);
			}else{
				// 不自动启动
			}
		}
	}

}
