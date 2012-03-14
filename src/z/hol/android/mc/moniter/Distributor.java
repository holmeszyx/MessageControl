package z.hol.android.mc.moniter;

import java.util.ArrayList;
import java.util.List;

import z.hol.android.mc.controler.CallLogControler;
import z.hol.android.mc.controler.Controler;
import z.hol.android.mc.controler.MessageControler;
import z.hol.android.mc.utils.Parser;
import z.hol.android.mc.utils.SettingManager;
import android.content.Context;
import android.text.TextUtils;

public class Distributor {

	private String mPass;
	private String mCommand;
	private Context mContext;
	
	private List<Controler> controlerList = new ArrayList<Controler>();
	
	public Distributor(Context context){
		this(context, null, null);
	}
	
	public Distributor(Context context, String pass, String command){
		mPass = pass;
		mCommand = command;
		mContext = context;
		fillControler();
	}
	
	private void fillControler(){
		controlerList.add(new MessageControler(mContext));
		controlerList.add(new CallLogControler(mContext));
	}
	
	public void distribute(String from, String pass, String command){
		if (verifyPass(pass)){
			for (int i = 0; i < controlerList.size(); i ++){
				Controler controler = controlerList.get(i);
				Parser parser = new Parser(command);
				if (controler.verify(parser)){
					controler.dispose(true, from, parser);
					break;
				}
			}
		}
	}
	
	public boolean verifyPass(String pass){
		if (TextUtils.isEmpty(pass)){
			return false;
		}
		SettingManager setting = SettingManager.get(mContext);
		String p = setting.getPass();
		return pass.trim().equals(p);
	}
}
