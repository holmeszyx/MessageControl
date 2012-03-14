package z.hol.android.mc.utils.contact;

import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;

public class PEmail  extends DetailItemAbs{
	public static final String FALG = "Email:";
	public static final String ADDRESS = ContactsContract.CommonDataKinds.Email.DATA;
	public static final String TYPE = Email.TYPE;
	
	private String email;
	private int emailType;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getEmailType() {
		return emailType;
	}
	public void setEmailType(int emailType) {
		this.emailType = emailType;
	}
	@Override
	public String toString() {
		return "PEmail [email=" + email + ", email_type=" + emailType + "]";
	}
	
	
	
}
