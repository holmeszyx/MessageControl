package z.hol.android.mc.utils.contact;

import android.provider.ContactsContract;

public class PGroup extends DetailItemAbs{
	public static String ID = ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID;
	
	private int id;
	private String title;
	private String note;
	private boolean visible;
	
	public PGroup(){
		this.id = -1;
		this.title = null;
		this.note = null;
		this.visible = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
	}
	
	public boolean isVisible(){
		return visible;
	}

	public boolean isNull() {
		return (id == -1)?true:false;
	}
	
	
	
}
