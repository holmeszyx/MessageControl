package z.hol.android.mc.utils.contact;

import android.provider.ContactsContract.CommonDataKinds.Website;

public class PWebsite extends DetailItemAbs{
	public static final String URL = Website.URL;
	public static final String TYPE = Website.TYPE;
	
	private String webUrl;
	private int webType;

	
	
	public String getWebUrl() {
		return webUrl;
	}



	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}



	public int getWebType() {
		return webType;
	}



	public void setWebType(int webType) {
		this.webType = webType;
	}



	@Override
	public String toString() {
		return "PWebsite [web_type=" + webType + ", web_url=" + webUrl + "]";
	}
	
	
}
