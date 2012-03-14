package z.hol.android.mc.utils.contact;

import android.provider.ContactsContract.CommonDataKinds.Nickname;

public class PNickName extends DetailItemAbs{
	public static final String NAME = Nickname.NAME;
	public static final String TYPE = Nickname.TYPE;
	

	private String nickName;
	private transient int type;
	
	public PNickName() {
		super();
	}
	public boolean isNull(){
		boolean isNull = true;
		if (nickName != null && !nickName.equals("")){
				isNull = false;
		}
		
		return isNull;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "PNickName [isNull=" + isNull() + ", nick_name=" + nickName
				+ ", type=" + type + "]";
	}
	
	
}
