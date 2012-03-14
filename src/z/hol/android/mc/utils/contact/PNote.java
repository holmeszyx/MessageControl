package z.hol.android.mc.utils.contact;

import android.provider.ContactsContract.CommonDataKinds.Note;

public class PNote extends DetailItemAbs{
	public static final String NOTE = Note.NOTE;
	
	private String note;
	public PNote(){
		super();

	}
	public boolean isNull(){
		boolean isNull = true;
		if (note != null && !note.equals("")){
				isNull = false;
		}
		return isNull;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String mNote) {		
		note = mNote;
	}
	@Override
	public String toString() {
		return "PNote [isNull=" + isNull() + ", m_note=" + note + "]";
	}
	
	
}
