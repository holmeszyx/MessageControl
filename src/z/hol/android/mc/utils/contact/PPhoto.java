package z.hol.android.mc.utils.contact;

import android.graphics.Bitmap;
import android.provider.ContactsContract.CommonDataKinds.Photo;

public class PPhoto extends DetailItemAbs{
	public static final String PHOTO = Photo.PHOTO;
	
	private Bitmap photo;
	private byte[] rawPhoto;
	private boolean isNull;
	
	public PPhoto(){
		super();
		this.isNull = true;
	}
	public boolean isNull(){
		return this.isNull;
	}
	public Bitmap getPhoto() {
		return photo;
	}

	public void setPhoto(Bitmap photo) {
		this.isNull = false;
		this.photo = photo;
	}
	
	public byte[] getRawPhoto() {
		return rawPhoto;
	}
	
	public void setRawPhoto(byte[] rawPhoto) {
		isNull = false;
		this.rawPhoto = rawPhoto;
	}
	
	@Override
	public String toString() {
		return "PPhoto [isNull=" + isNull + ", m_photo=" + photo + "]";
	}
	
	
}
