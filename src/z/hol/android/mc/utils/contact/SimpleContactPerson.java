package z.hol.android.mc.utils.contact;

import z.hol.android.mc.utils.Avatar;
import android.graphics.Bitmap;

public class SimpleContactPerson {
	
	public static SimpleContactPerson EMPTY = new SimpleContactPerson();
	
	public boolean bool1;
	public boolean bool2;

	private long rawId;
	private long photoId;
	private String displayName;
	private Bitmap photo;
	private String number;
	private String sortName;
	private String section;
	private boolean isHeadSection;
	private int groupId;
	private Avatar avatar;
	private int phoneType;
	
	public SimpleContactPerson(){
		this.rawId = -1;
		this.photoId = -1;
		this.photo = null;
		this.number = null;
		this.sortName = null;
		this.setSection(null);
		this.isHeadSection = false;
	}
	
	public SimpleContactPerson(String head){
		this.setSection(head);
		this.isHeadSection = true;
	}
	
	public long getRawId() {
		return rawId;
	}

	public void setRawId(long rawId) {
		this.rawId = rawId;
	}

	public void setPhotoId(long photoId) {
		this.photoId = photoId;
	}

	public long getPhotoId() {
		return photoId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getSection() {
		return section;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortName() {
		if (sortName != null){
			return sortName;
		}else{
			return displayName;
		}
	}

	public Bitmap getPhoto() {
		return photo;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}
	
	public void headSection(boolean isHead){
		this.isHeadSection = isHead;
	}
	
	public boolean isHeadSection(){
		return isHeadSection;
	}
	

	public boolean hasPhoto(){
		return (this.photo == null)?false:true;
	}
	
	public boolean existIn(){
		return (this.rawId == -1)?false:true;
	}

	public void setGroupId(int groupId){
		this.groupId = groupId;
	}
	
	public int getGroupId(){
		return groupId;
	}

	public Avatar getAvatar() {
		return avatar;
	}

	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

	public int getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(int phoneType) {
		this.phoneType = phoneType;
	}
	
	
	
}
