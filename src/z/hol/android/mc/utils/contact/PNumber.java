package z.hol.android.mc.utils.contact;

import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class PNumber extends DetailItemAbs{
	public static final String FLAG = "Phone";
	public static final String NUMBER  = ContactsContract.CommonDataKinds.Phone.NUMBER;
	public static final String TYPE = Phone.TYPE;
	
	private String number;
	private int numType;
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

	
	public int getNumType() {
		return numType;
	}
	public void setNumType(int numType) {
		this.numType = numType;
	}
	@Override
	public String toString() {
		return "PNumber [num_type=" + numType + ", number=" + number + "]";
	}
	
	public static int getRawType(String typeName){
		
		return 0;
	}
	
	public static String getTypeName(int type){
	
		return null;
	}
	
}
