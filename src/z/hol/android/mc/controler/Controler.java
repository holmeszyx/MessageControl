package z.hol.android.mc.controler;

import z.hol.android.mc.utils.Parser;
import z.hol.android.mc.utils.message.SMSUtil;
import android.content.Context;
import android.text.TextUtils;

/**
 * 控制器
 * @author holmes
 *
 */
public abstract class Controler {
	private Context mContext;
	
	private String mInstruction;	// 指令
	private String mVerifyInstruction;
	private String mInstructionMask;	// 指令辅码
	private String[] mTags;		// 目标
	private String[] mQuerys;	// 搜索
	private String mFrom;
	
	protected String msg;	// 保存一些文字
	
	public Controler(Context context){
		mContext = context;
	}
	
	/**
	 * 得到一个默认的指令
	 * @return
	 */
	public abstract String getDefaultInstruction();
	
	/**
	 * 得到指令
	 * @return
	 */
	public String getInstruction(){
		return mInstruction;
	}
	
	/**
	 * 设置指令
	 * @param inst
	 */
	public void setInstruction(String inst){
		mInstruction = inst;
	}
	
	/*
	 * 得到指令辅码
	 */
	public String getInstructionMask(){
		return mInstructionMask;
	}
	
	public void setVerifyInstruction(String verifyInst){
		mVerifyInstruction = verifyInst;
	}
	
	public String getVerifyInstruction(){
		return mVerifyInstruction;
	}
	
	/**
	 * 设置指令辅码
	 * @param mask
	 */
	public void setInstructionMask(String mask){
		mInstructionMask = mask;
	}
	
	/**
	 * 设置目标集
	 * @param tags
	 */
	public void setTags(String[] tags){
		mTags = tags;
	}
	
	/**
	 * 得到目标集
	 * @return
	 */
	public String[] getTags(){
		return mTags;
	}
	
	/**
	 * 得到来源
	 * @return
	 */
	public String getFrom(){
		return mFrom;
	}
	
	/**
	 * 设置来源
	 * @param from
	 */
	public void setFrom(String from){
		mFrom = from;
	}
	
	/**
	 * 得到搜索条件集
	 * @return
	 */
	public String[] getQuerys(){
		return mQuerys;
	}
	
	/**
	 * 设置搜索条件集
	 * @param querys
	 */
	public void setQuerys(String[] querys){
		mQuerys = querys;
	}
	
	public Context getContext(){
		return mContext;
	}
	
	protected void fillData(Parser parser){
		setVerifyInstruction(parser.getInstruction());
		setInstructionMask(parser.getInstructionMask());
		setTags(parser.getTags());
		setQuerys(parser.getQuerys());
	}
	
	/**
	 * 发送回复信息
	 */
	public void sendMsg(){
		boolean sendToTags = false;
		if (mTags != null && mTags.length > 0){
			// 有短信发送的目标
			sendToTags = true;
		}
		
		if (!sendToTags){
			if (TextUtils.isEmpty(getFrom())){
				return;
			}else{
				sendMsg(getFrom());
			}
		}else{
			for (String tag : getTags()){
				sendMsg(tag);
			}
		}
		
	}
	
	public void sendMsg(String to){
		if (TextUtils.isEmpty(msg)){
			return;
		}
		SMSUtil.smsSender(getContext(), to, msg, false);
	}
	
	/**
	 * 处理一条短信命令
	 * @param hasPass 是否有口令
	 * @param from	短信来源
	 * @param command	指令，除去识别码和密码剩余的内容
	 */
	public abstract void dispose(boolean hasPass, String from, String command);
	
	/**
	 * 处理一条短信命令
	 * @param hasPass 是否有口令
	 * @param from	短信来源
	 * @param parser
	 */
	public abstract void dispose(boolean hasPass, String from, Parser parser);
	
	/**
	 * 验证是否是本类的命令
	 * @param command
	 * @return
	 */
	public abstract boolean verify(String command);
	
	/**
	 * 验证是否是本类的命令
	 * @param parser
	 * @return
	 */
	public abstract boolean verify(Parser parser);
	
}
