package z.hol.android.mc.controler;

import z.hol.android.mc.utils.message.SMSUtil;
import android.content.Context;
import android.text.TextUtils;

public class MessageControler extends SimpleBaseControler{
	public static final String DEFAULT = "dx";
	
	
	public MessageControler(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setInstruction(DEFAULT);
	}

	@Override
	public String getDefaultInstruction() {
		// TODO Auto-generated method stub
		return DEFAULT;
	}
	
	@Override
	protected boolean sendCallBack(){
		if (getVerifyInstruction().equals(getInstruction())){
			if (TextUtils.isEmpty(getInstructionMask())){
				int count = SMSUtil.getUnreadSMSCount(getContext());
				msg = String.format("There ara %d unread messages", count);
			}else{
				
			}
			return true;
		}
		return false;
	}

}
