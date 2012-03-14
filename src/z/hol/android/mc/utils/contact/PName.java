package z.hol.android.mc.utils.contact;

import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;

public class PName extends DetailItemAbs{
	public static final String DISPLAY_NAME = ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME;
	public static final String FAMILY_NAME = StructuredName.FAMILY_NAME;
	public static final String GIVEN_NAME = StructuredName.GIVEN_NAME;
	public static final String MIDDLE_NAME = StructuredName.MIDDLE_NAME;

	private String disaplyName;
	private String firstName;
	private String lastName;
	private String middleName;
	private transient boolean isNull ;
	
	
	public PName() {
		super();
		this.isNull = true;
	}
	
	public boolean isNull(){
		return (disaplyName == null && firstName == null && lastName == null && middleName == null) ? true : false;
	}
	
	public String getDisaplyName() {
		return disaplyName;
	}
	public void setDisaplyName(String disaplyName) {
		this.isNull = false;
		this.disaplyName = disaplyName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		isNull = false;
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLast_name(String lastName) {
		isNull = false;
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		isNull = false;
		this.middleName = middleName;
	}

	@Override
	public String toString() {
		return "PName [disaply_name=" + disaplyName + ", first_name="
				+ firstName + ", isNull=" + isNull + ", last_name="
				+ lastName + "]";
	}


	
    
	
}
