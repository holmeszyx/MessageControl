package z.hol.android.mc.controler;

import java.util.List;

import z.hol.android.mc.utils.Utils;
import z.hol.android.mc.utils.calls.CallLogsInfo;
import z.hol.android.mc.utils.calls.CallLogsUtil;
import android.content.Context;
import android.text.TextUtils;

public class CallLogControler extends SimpleBaseControler{
	public static final String DEFAULT = "jl";
	

	public CallLogControler(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setInstruction(DEFAULT);
	}
	
	@Override
	public boolean verify(String command) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean sendCallBack() {
		// TODO Auto-generated method stub
		if (getVerifyInstruction().equals(getInstruction())){
			List<CallLogsInfo> callList = CallLogsUtil.getNewMissCallLogs(getContext());
			if (!callList.isEmpty()){
				if (callList.size() > 10){
					msg = String.format("Your hava %d miss calls.", callList.size());
				}else{
					msg = formatCallList(callList);
				}
			}else{
				msg = "no miss call.";
			}
			callList.clear();
			return true;
		}
		return false;
	}

	@Override
	public String getDefaultInstruction() {
		// TODO Auto-generated method stub
		return DEFAULT;
	}
	
	private String formatCallList(List<CallLogsInfo> callList){
		StringBuilder sb = new StringBuilder();
		sb.append("Miss call:\n");
		for (int i = 0; i < callList.size(); i ++){
			CallLogsInfo call = callList.get(i);
			sb.append(formatCallLog(call));
		}
		return sb.toString();
	}
	
	private String formatCallLog(CallLogsInfo call){
		StringBuilder sb = new StringBuilder();
		if (!TextUtils.isEmpty(call.getName())){
			sb.append(call.getName()).append(" ");
		}
		sb.append(call.getNumber()).append("@");
		sb.append(Utils.timestamp2TimeNoYear(call.getDate()));
		return sb.toString();
	}

}
