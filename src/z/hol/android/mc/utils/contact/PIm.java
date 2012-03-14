package z.hol.android.mc.utils.contact;

import android.provider.ContactsContract.CommonDataKinds.Im;

public class PIm extends DetailItemAbs{
	public static final String FLAG = "IM";
	public static final String DATA = Im.DATA;
	public static final String PROTOCOL = Im.PROTOCOL;
	
	private String imData;
	private int imProtocol;

	
	public String getImData() {
		return imData;
	}


	public void setImData(String imData) {
		this.imData = imData;
	}


	public int getImProtocol() {
		return imProtocol;
	}


	public void setImProtocol(int imProtocol) {
		this.imProtocol = imProtocol;
	}


	@Override
	public String toString() {
		return "PIm [im_data=" + imData + ", im_protocol=" + imProtocol + "]";
	}
	
	
}
