package z.hol.android.mc.utils.contact;

import android.provider.ContactsContract.CommonDataKinds.Organization;

public class POrganization extends DetailItemAbs{
	public static final String FLAG = "Orgs";
	public static final String COMPANY  = Organization.COMPANY;
	public static final String TITLE = Organization.TITLE;
	public static final String TYPE = Organization.TYPE;
	
	private String orgCompany;
	private String orgTitle;
	private int orgType;

	
	public String getOrgCompany() {
		return orgCompany;
	}


	public void setOrgCompany(String orgCompany) {
		this.orgCompany = orgCompany;
	}


	public String getOrgTitle() {
		return orgTitle;
	}


	public void setOrgTitle(String orgTitle) {
		this.orgTitle = orgTitle;
	}


	public int getOrgType() {
		return orgType;
	}


	public void setOrgType(int orgType) {
		this.orgType = orgType;
	}


	@Override
	public String toString() {
		return "POrganization [org_company=" + orgCompany + ", org_title="
				+ orgTitle + ", org_type=" + orgType + "]";
	}
	
	
}
