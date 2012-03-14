package z.hol.android.mc.controler;

import z.hol.android.mc.utils.Parser;
import android.content.Context;

public abstract class SimpleBaseControler extends Controler{

	public SimpleBaseControler(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean verify(String command) {
		// TODO Auto-generated method stub
		Parser parser = new Parser(command);
		return verify(parser);
	}	
	
	@Override
	public boolean verify(Parser parser) {
		// TODO Auto-generated method stub
		if (parser.getInstruction().equals(getInstruction())){
			return true;
		}
		return false;
	}

	@Override
	public void dispose(boolean hasPass, String from, String command) {
		// TODO Auto-generated method stub
		Parser parser = new Parser(command);
		dispose(hasPass, from, parser);
	}
	
	@Override
	public void dispose(boolean hasPass, String from, Parser parser) {
		// TODO Auto-generated method stub
		setFrom(from);
		if (parser.isAvaliable()){
			fillData(parser);
			if (sendCallBack()){
				// 接受本命令
				// 可以进行回复
				sendMsg();
			}
		}
	}

	/**
	 * 发送回复信息
	 */
	protected abstract boolean sendCallBack();
	
}
