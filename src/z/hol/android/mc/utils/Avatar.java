package z.hol.android.mc.utils;

import java.io.IOException;
import java.io.InputStream;

import z.hol.android.mc.utils.contact.ContactUtil;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

public class Avatar {
	
	public boolean checked = false;
	public boolean bool1 = false;
	
	private boolean mUserPhotoUri;
	private long mId;
	private Uri mPhotoUri;
	private Bitmap mPhoto;
	
	
	
	public Avatar(Uri photoUri){
		mUserPhotoUri = true;
		this.mPhotoUri = photoUri;
	}
	
	public Avatar(long photoId){
		mUserPhotoUri = false;
		this.mId = photoId;
	}
	
	public void setAvatar(Bitmap avatar){
		mPhoto = avatar;
		checked = true;
	}
	
	public Bitmap getAvatar(Context context){
		if (!checked){
			if (isUserPhotoUri()){
				InputStream photoStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), mPhotoUri);
				 if (photoStream != null){
					Bitmap photo = BitmapFactory.decodeStream(photoStream);
					setAvatar(photo);
					 try {	
							photoStream.close();
							photoStream = null;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				 }
			}else{
				Bitmap photo = ContactUtil.getPhotoByPhotoId(context, mId);
				setAvatar(photo);
			}
			checked = true;
		}
		return mPhoto;
	}
	
	public boolean isUserPhotoUri(){
		return mUserPhotoUri;
	}

}
