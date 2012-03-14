package z.hol.android.mc.utils.contact;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import z.hol.android.mc.utils.Avatar;
import z.hol.android.mc.utils.CompareList;
import z.hol.android.mc.utils.SortCursor;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

import com.pinyin4android.PinyinUtil;

/**
 * contact tools class
 * warning: raw_contact_id , contact_id and _id some times , they are not the same one
 * @author holmes
 *
 */
public class ContactUtil {
	private static final String TAG = "ContactUtil";
	// 查询Data表的Uri
	public static final Uri URI = ContactsContract.Data.CONTENT_URI;
	public static final Uri CONTACT_URI = ContactsContract.Contacts.CONTENT_URI;
	public static final Uri RAW_URI = ContactsContract.RawContacts.CONTENT_URI;
	
	public static final String SORT_KEY_ALT = "sort_key_alt";
	public static final String SORT_KEY = "sort_key";
	public static final String VISIBLE = "1";
	public static final String INVISIBLE = "0";
	
	public static final String HAS_PHONE = "1"; 
	public static final String NOT_HAS_PHONE = "0";
	
	
	
	/**
	 * 查找通讯录
	 * 
	 * @param context
	 * @return people 联系人列表<ContactPerson>
	 */
	public static List<ContactPerson> searchContacts(Context context) {
		long myProfileId = -1;
		
		Long start = System.currentTimeMillis();
		ContentResolver cr = context.getContentResolver();
		List<ContactPerson> list = new ArrayList<ContactPerson>();
		Cursor cAll = null;
		Uri mUri = CONTACT_URI;
		//select contact_id, sum(version) as version from raw_contacts where deleted = 0 group by contact_id;
		String[] projection = new String[] { ContactsContract.Contacts._ID};
		//String selection = String.format("deleted = %d", 0);
		String selection = null;
		String[] selectionArgs = null;
		// 得到通讯录全部raw_id
		cAll = cr.query(mUri, projection, selection, selectionArgs, null);
		if (cAll != null) {
			// 遍历全部Raw_id
			while (cAll.moveToNext()) {
				int rawId = cAll.getInt(0);
				//int version = cAll.getInt(1);
				int version = 1;
//				int id = cAll.getInt(2);
				// 如果没有raw_id值
				if (rawId == 0) {
					continue;
				}
				ContactPerson p = searchPerson(context, rawId, version);	
				if (rawId == myProfileId)
					p.setProfile(true);
//				p.setId(id);
				// 添加一个名片到List中
				list.add(p);
				
			}
		}
		
		if (cAll != null){
			cAll.close();
		}
		cAll = null;
		Long ttt = System.currentTimeMillis() - start;
		System.out.println("timeout:" + String.valueOf(ttt) + "ms");
		return list;
	}
	
	
	private static final String[] SEARCH_PERSON_PROJECTION = new String[]{
		ContactsContract.Data._ID,	//0
		ContactsContract.Data.RAW_CONTACT_ID,	//1
		ContactsContract.Data.CONTACT_ID,	//2
		ContactsContract.Data.MIMETYPE,	//3
		ContactsContract.Data.DATA1,	//4
		ContactsContract.Data.DATA2,	//5
		ContactsContract.Data.DATA3,	//6
		ContactsContract.Data.DATA4,	//7
		ContactsContract.Data.DATA5,	//8
		ContactsContract.Data.DATA6,	//9
		ContactsContract.Data.DATA7,	//10
		ContactsContract.Data.DATA8,	//11
		ContactsContract.Data.DATA9,	//12
		ContactsContract.Data.DATA10,	//13
		ContactsContract.Data.DATA11,	//14
		ContactsContract.Data.DATA12,	//15
		ContactsContract.Data.DATA13,	//16
		ContactsContract.Data.DATA14,	//17
		ContactsContract.Data.DATA15,	//18
	};
	/**
	 * 获取一个特定联系人
	 * @param context
	 * @param id 特定联系人Id
	 * @param version 联系人的版本，如果为-1，则自动获取添加
	 * @param useRawId if true the selection with ContactsContract.Data.RAW_CONTACT_ID,else ContactsContract.Data.CONTACT_ID
	 * @return
	 */
	 
	public static ContactPerson searchPerson(Context context, long id, int version , boolean useRawId){
		ContactPerson p = new ContactPerson();
		if (version == -1){
			p.setVersion(findVersion(context, id));
		}else{
			p.setVersion(version);
		}
		p.setRawId((int) id);
		Cursor cUnite = null;
		Uri mUri = URI;
		
		String selection = null;
		if (useRawId)
		{
			selection = ContactsContract.Data.RAW_CONTACT_ID + "=" + id;
		}else{
			selection = ContactsContract.Data.CONTACT_ID + "=" + id;
		}
			
		cUnite = context.getContentResolver().query(mUri, SEARCH_PERSON_PROJECTION, selection, null, null);
		if (cUnite != null && !cUnite.isAfterLast()) {
			int idIndex = cUnite.getColumnIndex(ContactsContract.Data._ID);
			while (cUnite.moveToNext()) {
				//String mimitype = cUnite.getString(cUnite.getColumnIndex(ContactsContract.Data.MIMETYPE));
				String mimitype = cUnite.getString(3);
				// 查找电话号码
				if (mimitype
						.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
					PNumber num = new PNumber();
					num.setNumber(cUnite.getString(cUnite
							.getColumnIndex(PNumber.NUMBER)));
					num.setNumType(cUnite.getInt(cUnite
							.getColumnIndex(PNumber.TYPE)));
					num._id = cUnite.getLong(idIndex);
					p.addNumber(num);
					num = null;
					continue;
				}
				// 查找名称
				if (mimitype
						.equals(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
					PName name = new PName();
					name.setDisaplyName(cUnite.getString(cUnite
							.getColumnIndex(PName.DISPLAY_NAME)));
					name.setFirstName(cUnite.getString(cUnite
							.getColumnIndex(PName.GIVEN_NAME)));
					name.setLast_name(cUnite.getString(cUnite
							.getColumnIndex(PName.FAMILY_NAME)));
					name._id = cUnite.getLong(idIndex);
					p.setPName(name);
					name = null;
					continue;
				}
				// 查找Email
				if (mimitype
						.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
					PEmail email = new PEmail();
					email.setEmail(cUnite.getString(cUnite
							.getColumnIndex(PEmail.ADDRESS)));
					email.setEmailType(cUnite.getInt(cUnite
							.getColumnIndex(PEmail.TYPE)));
					email._id = cUnite.getLong(idIndex);
					p.addEmail(email);
					email = null;
					continue;
				}
				// 查找IM帐号
				if (mimitype.equals(ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)){
					PIm im = new PIm();
					im.setImData(cUnite.getString(cUnite.getColumnIndex(PIm.DATA)));
					im.setImProtocol(cUnite.getInt(cUnite.getColumnIndex(PIm.PROTOCOL)));
					im._id = cUnite.getLong(idIndex);
					p.addIM(im);
					im = null;
					continue;
				}
				// 查找个人网站
				if (mimitype
						.equals(ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)) {
					PWebsite web = new PWebsite();
					web.setWebUrl(cUnite.getString(cUnite
							.getColumnIndex(PWebsite.URL)));
					web.setWebType(cUnite.getInt(cUnite
							.getColumnIndex(PWebsite.TYPE)));
					web._id = cUnite.getLong(idIndex);
					p.addWebSite(web);
					web = null;
					continue;
				}
				// 查找公司组织
				if (mimitype
						.equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)) {
					POrganization org = new POrganization();
					org.setOrgCompany(cUnite.getString(cUnite
							.getColumnIndex(POrganization.COMPANY)));
					org.setOrgTitle(cUnite.getString(cUnite
							.getColumnIndex(POrganization.TITLE)));
					org.setOrgType(cUnite.getInt(cUnite
							.getColumnIndex(POrganization.TYPE)));
					org._id = cUnite.getLong(idIndex);
					p.addOrg(org);
					org = null;
					continue;
				}
				// 查找住址
				if (mimitype.equals(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)){
					PPostal postal = new PPostal();
					postal.setFullPostalAddress(cUnite.getString(cUnite.getColumnIndex(PPostal.FULL_ADDRESS)));
					postal.setPostalType(cUnite.getInt(cUnite.getColumnIndex(PPostal.TYPE)));
					postal.setPostalStreet(cUnite.getString(cUnite.getColumnIndex(PPostal.STREET)));
					postal.setPostalBox(cUnite.getString(cUnite.getColumnIndex(PPostal.POBOX)));
					postal.setNeighborhood(cUnite.getString(cUnite.getColumnIndex(PPostal.NEIGHBORHOOD)));
					postal.setPostalCity(cUnite.getString(cUnite.getColumnIndex(PPostal.CITY)));
					postal.setPostalState(cUnite.getString(cUnite.getColumnIndex(PPostal.REGION)));
					postal.setPostalZip(cUnite.getString(cUnite.getColumnIndex(PPostal.POSTCODE)));
					postal.setPostalCountry(cUnite.getString(cUnite.getColumnIndex(PPostal.COUNTRY)));					
					
					postal._id = cUnite.getLong(idIndex);
					p.addPostal(postal);
					postal = null;
					continue;
				}
				// 查找昵称
				if (mimitype
						.equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)) {
					PNickName nickname = new PNickName();
					nickname.setNickName(cUnite.getString(cUnite
							.getColumnIndex(PNickName.NAME)));
					nickname.setType(cUnite.getInt(cUnite
							.getColumnIndex(PNickName.TYPE)));
					nickname._id = cUnite.getLong(idIndex);
					p.setPNickname(nickname);
					//nickname = null;
					continue;
				}
				// 查找备注
				if (mimitype
						.equals(ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)) {
					PNote note = new PNote();
					note.setNote(cUnite.getString(cUnite
							.getColumnIndex(PNote.NOTE)));
					note._id = cUnite.getLong(idIndex);
					p.setPNote(note);
					//note = null;
					continue;
				}
				
				// 查找分组
				/*
				if (mimitype.equals(ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)){
					PGroup group = GroupsUtil.get(context).getGroup(cUnite.getInt(cUnite.getColumnIndex(PGroup.ID)));
					
					//moto bug
					if (!cUnite.isNull(idIndex)){
						group._id = cUnite.getLong(idIndex);
						p.setPGroup(group);
					}
					continue;
					
				}
				*/
				// 查找头像
				if (mimitype
						.equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)) {
					byte[] buff = cUnite.getBlob(cUnite
							.getColumnIndex(PPhoto.PHOTO));
					if (buff == null)
						continue;
					
					PPhoto photo = new PPhoto();
					photo.setRawPhoto(buff);
					photo._id = cUnite.getLong(idIndex);
					p.setPPhoto(photo);
					/*
					PPhoto photo = new PPhoto();
					ByteArrayInputStream ip = new ByteArrayInputStream(
							buff);
					Bitmap bm = BitmapFactory.decodeStream(ip);
					photo.setPhoto(bm);
					photo._id = cUnite.getLong(idIndex);
					p.setPPhoto(photo);
					try {
						ip.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
				}
				//end all
			}
		}
		if (cUnite != null){
			cUnite.close();
		}
		cUnite = null;
		return p;
	}
	
	/**
	 * use contact_id and like searchPerson(context, rawId, version, false)
	 * @param context
	 * @param rawId
	 * @param version
	 * @return
	 */
	public static ContactPerson searchPerson(Context context, long rawId, int version){
		return searchPerson(context, rawId, version, false);
	}
	
	/**
	 * 得到联系人email 列表
	 * @param context
	 * @param rawId 联系人raw id
	 * @return email 列表,如果没有返回null
	 */
	public static List<PEmail> getPersonEmailList(Context context, long rawId, boolean isRawId){
		List<PEmail> emailList = null;
		Uri uri = URI;
		String where = null;
		StringBuilder sb = new StringBuilder();
		if (isRawId){
			sb.append(ContactsContract.Data.RAW_CONTACT_ID + " = " + rawId);
		}else{
			sb.append(ContactsContract.Data.CONTACT_ID + " = " + rawId);
		}
		sb.append(" AND ");
		sb.append(ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "'");
		where = sb.toString();
		sb = null;
		final String[] projection = new String[]{
				Data._ID,		//0
				Data.MIMETYPE,  //1
				Data.DATA1, 	//data 2
				Data.DATA2, 	//type 3
				Data.DATA3,  	//4
				Data.DATA4,  	//5
				Data.DATA5		//protocl 6				
			};
		Cursor c = context.getContentResolver().query(uri, projection, where, null, null);
		if (c != null && !c.isAfterLast()){
			emailList = new ArrayList<PEmail>();
			while(c.moveToNext()){
				int type = c.getInt(3);
				String emailData = c.getString(2);
				PEmail email = new PEmail();
				email.setEmailType(type);
				email.setEmail(emailData);
				emailList.add(email);
			}
		}
		if (c != null) c.close();
		c = null;
		
		return emailList;
	}
	


	/**
	 * 通过号码得到一个联系人的简介
	 * @param context
	 * @param number 号码
	 * @return a simpleContactPerson with contact_id
	 */
	public static SimpleContactPerson getSimplePersonByNumber(Context context, String number){
		 SimpleContactPerson smpPerson = new SimpleContactPerson();
		 Uri lookUp =Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
		 Cursor c = context.getContentResolver().query(lookUp, new String[]{PhoneLookup._ID, PhoneLookup.DISPLAY_NAME, PhoneLookup.TYPE}, null, null, null);
		 if (c != null && !c.isAfterLast() ){
			 c.moveToFirst();
			 int contactId = c.getInt(0);
			 smpPerson.setRawId(contactId);
			 smpPerson.setDisplayName(c.getString(1));
			 smpPerson.setPhoneType(c.getInt(2));
			 Uri photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
			 Avatar avatar = new Avatar(photoUri);
			 smpPerson.setAvatar(avatar);
		 }else{
			 smpPerson.setDisplayName(number);
		 }
		 if (c != null){
			 c.close();
		 }
		 c = null;
		 smpPerson.setNumber(number);
		 return smpPerson;
		 
	}
	
	public static boolean isInContactByNumber(Context context, String number){
		 boolean isHas = false;
		 Uri lookUp =Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
		 Cursor c = context.getContentResolver().query(lookUp, new String[]{PhoneLookup._ID}, null, null, null);
		 if (c != null && !c.isAfterLast() ){
			isHas = true; 
		 }
		 if (c != null){
			 c.close();
		 }
		 c = null;
		 return isHas;
	}
	
	/**
	 * is number belong contact<br>
	 * it use to filter sms or call logs by a contact person
	 * @param context
	 * @param number phone number
	 * @param contactId contact id
	 * @return true is belong to this contact, false otherwise
	 */
	public static boolean isNumberBelongToContact(Context context, String number, long contactId){
		boolean isBelongTo = false;
		 Uri lookUp =Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
		 Cursor c = context.getContentResolver().query(lookUp, new String[]{PhoneLookup._ID}, null, null, null);
		 if (c != null && !c.isAfterLast() ){
			 while(c.moveToNext()){
				long id = c.getLong(0); 
				if (id == contactId){
					isBelongTo = true; 
					break;
				}
			 }
		 }
		 if (c != null){
			 c.close();
		 }
		 c = null;
		
		return isBelongTo;
	}
	
	/**
	 * 获取头像的bitmap
	 * @param context
	 * @param contactId 联系人的contactId
	 * @return
	 */
	public static Bitmap getPhotoByContactId(Context context, long contactId){
		 Bitmap photo = null;
		 Uri photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
		 InputStream photoStream = null;
		 try{
			 photoStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), photoUri);
			 
		 }catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
		}
		 if (photoStream != null){
			photo = BitmapFactory.decodeStream(photoStream);
			 try {
					photoStream.close();
					photoStream = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 }
		return photo;
	}
	
	public static Bitmap getPhotoByPhotoId(Context context, long photoId){
		Bitmap photo = null;
		String[] projection = new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO};
		String selection = ContactsContract.Data._ID + "=" + photoId;
		Cursor c = null;
		c = context.getContentResolver().query(URI, projection, selection, null, null);
		if (c != null && !c.isAfterLast()){
			c.moveToFirst();
			byte[] buff = c.getBlob(0);
			ByteArrayInputStream byteInput = new ByteArrayInputStream(buff);
			photo = BitmapFactory.decodeStream(byteInput);
			try {
				byteInput.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		if (c != null){
			c.close();
		}
		c = null;
		
		
		
		return photo;
	}
	
	private static final String[] RAW_PHOTO_PROJECTION = new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Photo.PHOTO};
	public static byte[] getRawPhoto(Context context, long rawId){
		byte[] rawPhoto = null;
		// mimitype = 'vnd' AND rawid = 123
		String where = String.format(" %s = '%s' AND %s = %d", ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE, ContactsContract.Data.RAW_CONTACT_ID, rawId);
		Cursor c = null;
		c = context.getContentResolver().query(URI, RAW_PHOTO_PROJECTION, where, null, null);
		if (c != null && !c.isAfterLast()){
			c.moveToFirst();
			if (!c.isNull(1)){
				rawPhoto = c.getBlob(1);
			}
		}
		if (c != null) c.close();
		
		return rawPhoto;
	}
	
	
	/**
	 * 获取原始的用于显示的联系人列表<br>
	 * 没有名字首字母头
	 * @param context
	 * @param onlyHasPhone
	 * @return
	 */
	public static ArrayList<SimpleContactPerson> getRawContactDisplayList(Context context, boolean onlyHasPhone){
		ArrayList<SimpleContactPerson> displayList = new ArrayList<SimpleContactPerson>();
		int sdkLevel = Integer.valueOf(android.os.Build.VERSION.SDK);
		
		if (sdkLevel >= 8){
			String perLetter = null;
			final String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, SORT_KEY, ContactsContract.Contacts.IN_VISIBLE_GROUP, ContactsContract.Contacts.PHOTO_ID};
			String selection = null; 
			//Filter phone
			if (onlyHasPhone){
				selection = String.format(ContactsContract.Contacts.IN_VISIBLE_GROUP + " = %s AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER + " = %s", VISIBLE, HAS_PHONE);
			}else{
				selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + "=" + VISIBLE; 
			}
			String order = SORT_KEY + " ASC";
			Cursor c = null;
			c = context.getContentResolver().query(CONTACT_URI, projection, selection, null, order);
			if (c != null){
				while(c.moveToNext()){
					SimpleContactPerson simplePerson = new SimpleContactPerson();
					long rawId = c.getLong(0);
					simplePerson.setRawId(rawId);
					simplePerson.setDisplayName(c.getString(1));
					String sortName = c.getString(2);
					if (sortName != null){
						sortName = sortName.toUpperCase();
						char firstChar = sortName.charAt(0);
						//perLetter = String.;
						if (firstChar >= '0' && firstChar <= '9'){
							if (perLetter == null){
								perLetter = "#";
							}
						}else{
							String currentLetter = String.valueOf(firstChar);
							if (!currentLetter.equals(perLetter)){
								perLetter = currentLetter;
							}
						}
						
					}else{
						if (perLetter == null){
							perLetter = "#";
						}
						
					}
					simplePerson.setSection(perLetter);
					simplePerson.setSortName(sortName);
					//simplePerson.setPhoto(ContactUtil.getPhotoByRawId(context, rawId));
					long photoId = c.isNull(4)? -1:c.getLong(4);
					simplePerson.setPhotoId(photoId);
					displayList.add(simplePerson);
				}
				c.close();
			}
			
			
		}else{
			//android sdk 2.1 or below
			String perLetter = null;
			final String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.IN_VISIBLE_GROUP, ContactsContract.Contacts.PHOTO_ID};
			String selection = null; 
			//Filter phone
			if (onlyHasPhone){
				selection = String.format(ContactsContract.Contacts.IN_VISIBLE_GROUP + " = %s AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER + " = %s", VISIBLE, HAS_PHONE);
			}else{
				selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + "=" + VISIBLE; 
			}
			String order = null;
			Cursor c = null;
			c = context.getContentResolver().query(CONTACT_URI, projection, selection, null, order);
			if (c != null){
				SortCursor sortC = new SortCursor(context, c, 1);
				while(sortC.moveToNext()){
					SimpleContactPerson simplePerson = new SimpleContactPerson();
					long rawId = sortC.getLong(0);
					String displayName = sortC.getString(1);
					simplePerson.setRawId(rawId);
					simplePerson.setDisplayName(displayName);
					String sortName = PinyinUtil.toPinyin(context, displayName.charAt(0));
					if (sortName != null){
						sortName = sortName.toUpperCase();
						char firstChar = sortName.charAt(0);
						//perLetter = String.;
						if (firstChar >= '0' && firstChar <= '9'){
							if (perLetter == null){
								perLetter = "#";
							}
						}else{
							String currentLetter = String.valueOf(firstChar);
							if (!currentLetter.equals(perLetter)){
								perLetter = currentLetter;
							}
						}
						
					}else{
						if (perLetter == null){
							perLetter = "#";
						}
						
					}
					simplePerson.setSection(perLetter);
					simplePerson.setSortName(sortName);
					//simplePerson.setPhoto(ContactUtil.getPhotoByRawId(context, rawId));
					long photoId = c.isNull(3)? -1:c.getLong(3);
					simplePerson.setPhotoId(photoId);
					displayList.add(simplePerson);
				}
				sortC.close();
				c.close();
			}
			
		}

		
		return displayList;
	}
	
	/**
	 * 获取用于显示的联系人列表 
	 * @param context
	 * @return
	 */
	public static ArrayList<SimpleContactPerson> getContactDisplayList(Context context, boolean onlyHasPhone){
		long start = System.currentTimeMillis();
		int sdkLevel = Integer.valueOf(android.os.Build.VERSION.SDK);
		ArrayList<SimpleContactPerson> displayList = new ArrayList<SimpleContactPerson>();
		boolean onlyVisible = false;
		int defaultGroupId = -1;

		if (sdkLevel >= 8){
			String perLetter = null;
			final String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, SORT_KEY, ContactsContract.Contacts.IN_VISIBLE_GROUP, ContactsContract.Contacts.PHOTO_ID};
			String selection = null; 
			//Filter phone
			if (onlyHasPhone){
				if (onlyVisible){
					selection = String.format(ContactsContract.Contacts.IN_VISIBLE_GROUP + " = %s AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER + " = %s", VISIBLE, HAS_PHONE);
				}else{
					selection = String.format(ContactsContract.Contacts.HAS_PHONE_NUMBER + " = %s", VISIBLE, HAS_PHONE);
				}
			}else{
				if (onlyVisible){
					selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + "=" + VISIBLE; 
				}
			}
			String order = SORT_KEY + " ASC";
			Cursor c = null;
			c = context.getContentResolver().query(CONTACT_URI, projection, selection, null, order);
			if (c != null){
				while(c.moveToNext()){
					SimpleContactPerson simplePerson = new SimpleContactPerson();
					long rawId = c.getLong(0);
					simplePerson.setRawId(rawId);
					simplePerson.setDisplayName(c.getString(1));
					String sortName = c.getString(2);
					if (sortName != null){
						sortName = sortName.toUpperCase();
						char firstChar = sortName.charAt(0);
						//perLetter = String.;
						if (firstChar >= '0' && firstChar <= '9'){
							if (perLetter == null){
								perLetter = "#";
								//displayList.add(new SimpleContactPerson(perLetter));
							}
						}else{
							String currentLetter = String.valueOf(firstChar);
							if (!currentLetter.equals(perLetter)){
								perLetter = currentLetter;
								//displayList.add(new SimpleContactPerson(perLetter));
							}
						}
						
					}else{
						if (perLetter == null){
							perLetter = "#";
							//displayList.add(new SimpleContactPerson(perLetter));
						}
						
					}

					simplePerson.setSection(perLetter);
					simplePerson.setSortName(sortName);
					//simplePerson.setPhoto(ContactUtil.getPhotoByRawId(context, rawId));
					long photoId = c.isNull(4)? -1:c.getLong(4);
					simplePerson.setPhotoId(photoId);
					displayList.add(simplePerson);
				}
				c.close();
			}
			
			
		}else{
			//android sdk 2.1 or below
			String perLetter = null;
			final String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.IN_VISIBLE_GROUP, ContactsContract.Contacts.PHOTO_ID};
			String selection = null; 
			//Filter phone
			if (onlyHasPhone){
				selection = String.format(ContactsContract.Contacts.IN_VISIBLE_GROUP + " = %s AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER + " = %s", VISIBLE, HAS_PHONE);
			}else{
				selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + "=" + VISIBLE; 
			}
			String order = null;
			Cursor c = null;
			c = context.getContentResolver().query(CONTACT_URI, projection, selection, null, order);
			if (c != null){
				SortCursor sortC = new SortCursor(context, c, 1);
				while(sortC.moveToNext()){
					SimpleContactPerson simplePerson = new SimpleContactPerson();
					long rawId = sortC.getLong(0);
					String displayName = sortC.getString(1);
					simplePerson.setRawId(rawId);
					simplePerson.setDisplayName(displayName);
					String sortName = PinyinUtil.toPinyin(context, displayName.charAt(0));
					if (sortName != null){
						sortName = sortName.toUpperCase();
						char firstChar = sortName.charAt(0);
						//perLetter = String.;
						if (firstChar >= '0' && firstChar <= '9'){
							if (perLetter == null){
								perLetter = "#";
								//displayList.add(new SimpleContactPerson(perLetter));
							}
						}else{
							String currentLetter = String.valueOf(firstChar);
							if (!currentLetter.equals(perLetter)){
								perLetter = currentLetter;
								//displayList.add(new SimpleContactPerson(perLetter));
							}
						}
						
					}else{
						if (perLetter == null){
							perLetter = "#";
							//displayList.add(new SimpleContactPerson(perLetter));
						}
						
					}

					simplePerson.setSection(perLetter);
					simplePerson.setSortName(sortName);
					//simplePerson.setPhoto(ContactUtil.getPhotoByRawId(context, rawId));
					long photoId = c.isNull(3)? -1:c.getLong(3);
					simplePerson.setPhotoId(photoId);
					displayList.add(simplePerson);
				}
				sortC.close();
				c.close();
			}
			
		}

		Log.i(TAG, "ContactDisplayList:" + (System.currentTimeMillis() - start));
		return displayList;
	}
	
	public static List<SimpleContactPerson> getContactDisplayListOnFristSearch(Context context, String startStr){
		List<SimpleContactPerson> displayList = new ArrayList<SimpleContactPerson>();
		String selection = String.format(ContactsContract.Contacts.DISPLAY_NAME + " LIKE '%%%s%%'", startStr);
		final String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.IN_VISIBLE_GROUP, ContactsContract.Contacts.PHOTO_ID};
		Cursor c = null;
		c = context.getContentResolver().query(CONTACT_URI, projection, selection, null, null);
		
		if (c != null){
			while(c.moveToNext()){
				SimpleContactPerson simplePerson = new SimpleContactPerson();
				long contactId = c.getLong(0);
				simplePerson.setRawId(contactId);
				simplePerson.setDisplayName(c.getString(1));
				long photoId = c.isNull(3)? -1:c.getLong(3);
				simplePerson.setPhotoId(photoId);
				displayList.add(simplePerson);
			}
		}
		if (c != null) c.close();
		c = null;
		return displayList;
	}
	

	
	public static ArrayList<SimpleContactPerson> getContactDisplayList(Context context){
		return getContactDisplayList(context, false);
	}
	
	/**
	 * 获取一个用与显示的联系人
	 * @param context
	 * @param contactId
	 * @return
	 */
	public static SimpleContactPerson getContactDisplay(Context context, long contactId){
		long start = System.currentTimeMillis();
		SimpleContactPerson simplePerson = new SimpleContactPerson();
		final String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.IN_VISIBLE_GROUP, ContactsContract.Contacts.PHOTO_ID};
		String selection = ContactsContract.Contacts._ID + "=" + contactId; 
		//Filter phone

		String order = null; //SORT_KEY + " ASC";
		Cursor c = null;
		c = context.getContentResolver().query(CONTACT_URI, projection, selection, null, order);
		if (c != null){
			while(c.moveToNext()){
				simplePerson.setRawId(contactId);
				simplePerson.setDisplayName(c.getString(1));
				//simplePerson.setPhoto(ContactUtil.getPhotoByRawId(context, rawId));
				 Uri photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
				 Avatar avatar = new Avatar(photoUri);
				simplePerson.setAvatar(avatar);
				long photoId = c.isNull(3)? -1:c.getLong(3);
				simplePerson.setPhotoId(photoId);
			}
			c.close();
		}
		c = null;
		Log.i(TAG, "ContactDisplayList:" + (System.currentTimeMillis() - start));
		return simplePerson;
	}
	
	/**
	 * convert raw_id to contact_id
	 * @param context
	 * @param rawId
	 * @return return contact_id , or -1 when there is no result
	 */
	public static long RawId2ContactId(Context context, long rawId){
		long contactId = -1;
		String[] projection = new String[]{ContactsContract.RawContacts.CONTACT_ID};
		String selection = ContactsContract.RawContacts._ID + "=" + rawId;
		Cursor c = null;
		c = context.getContentResolver().query(RAW_URI, projection, selection, null, null);
		if (c != null && !c.isAfterLast()){
			c.moveToFirst();
			if (c.isNull(0)){
				contactId = rawId;
			}else{
				contactId = c.getLong(0);
			}
		}
		if (c != null) c.close();
		c = null;
		
		return contactId;	
	}
	
	public static long ContactId2RawId(Context context, long contactId){
		long rawId = -1;
		String[] projection = new String[]{ContactsContract.RawContacts._ID};
		String selection = ContactsContract.RawContacts.CONTACT_ID + "=" + contactId;
		Cursor c = null;
		c = context.getContentResolver().query(RAW_URI, projection, selection, null, null);
		if (c != null && !c.isAfterLast()){
			c.moveToFirst();
			if (c.isNull(0)){
				rawId = contactId;
			}else{
				rawId = c.getLong(0);
			}
		}
		if (c != null) c.close();
		c = null;
		
		return rawId;	
	}
	
	
	
	/**
	 * 根据联系人raw_id，获取其Version值
	 * @param context
	 * @param rawId
	 * @return 返回相应的Version的值，如果不存在则返回 -1
	 */
	public static int findVersion(Context context, long rawId){
		int version = -1;
		int sumVersion = -1;
		
		//String[] projection = new String[] {"" + ContactsContract.RawContacts.VERSION + ""};
		//String selection = String.format("deleted = %d AND " + ContactsContract.RawContacts.CONTACT_ID + " = %d GROUP BY %s", 0, rawId, ContactsContract.RawContacts.CONTACT_ID);
		
		String[] projection = new String[]{ContactsContract.RawContacts.VERSION};
		String selection = ContactsContract.RawContacts._ID + "=" + String.valueOf(rawId);
		Cursor c = null;
		c = context.getContentResolver().query(RAW_URI, projection, selection, null, null);
		if ((c != null) && (!c.isAfterLast())){
			sumVersion = 0;
			while(c.moveToNext()){
				version = c.getInt(0);
				sumVersion += version;
			}
		}
		if (c != null){
			c.close();
		}
		c = null;
		return sumVersion;
		
	}
	/**
	 * 获取对比用的通讯录列表
	 * @param context
	 * @return List<CompareList>的通讯录列表
	 */
	public static List<CompareList> getRawIdList(Context context){
		//因为不需要将个人资料保存在本地系统数据库中，所以此数据无意义
		//但为了不修改代码，将其值设为0
		//long profilRawId = ContactUtil.checkHasPersonalRecord(context);
		long profileRawId = 0;
		
		List<CompareList> list = new ArrayList<CompareList>();
		long lastRawId = 0;
		int sumVersion = 0;
		
		
		//String[] projection = new String[] { ContactsContract.RawContacts.CONTACT_ID, "" + ContactsContract.RawContacts.VERSION + ""};
		//String selection = String.format("deleted = %d GROUP BY %s", 0, ContactsContract.RawContacts.CONTACT_ID);
		
		// modify, RawContacts.CONTACT_ID to _ID
		String[] projection = new String[]{ContactsContract.RawContacts._ID, ContactsContract.RawContacts.VERSION};
		//String selection =ContactsContract.RawContacts.DELETED + "=0";
		String selection = String.format("%s = 0 AND NOT %s = %d", ContactsContract.RawContacts.DELETED, ContactsContract.RawContacts._ID, profileRawId);
		String order = ContactsContract.RawContacts._ID + " ASC";
		Cursor c = null;
		c = context.getContentResolver().query(RAW_URI, projection, selection, null, order);
		if (c != null && !c.isAfterLast()){
			while(c.moveToNext()){
				long rawId = c.getLong(0);
				int version = c.getInt(1);
				if (rawId != lastRawId){
					sumVersion = version;
					CompareList compareList = new CompareList();
					//System.out.println("ComrawId:" + c.getInt(0));
					compareList.setLocalId((int) rawId);
					compareList.setVersion(sumVersion);
					list.add(compareList);	
					
					lastRawId = rawId;
					
				}else{
					int size = list.size();
					CompareList lastCompareList = list.get(size - 1);
					sumVersion += version;
					lastCompareList.setVersion(sumVersion);
					list.set(size - 1, lastCompareList);
				}

			
			}
		}
		if (c != null){
			c.close();
		}
		c = null;
		//System.out.println(list.toString());
		return list;
	}
	
	/**
	 * 获取联系人的总数
	 * @param context
	 * @return
	 */
	public static int getContactCount(Context context){
		int count = 0;
		//COUNT(_id)
		//String countStr = "max( * ) AS my_total";
		//String countStr = "_id";
		String[] projection = new String[]{ContactsContract.RawContacts._ID};
		String selection = String.format("%s = 0", ContactsContract.RawContacts.DELETED);
		Cursor c = null;
		c = context.getContentResolver().query(RAW_URI, projection, selection, null, null);
		if (c != null && c.moveToFirst()){
			//count = c.getInt(0);
			count = c.getCount();
		}
		if (c != null) c.close();
		return count;
	}
	
	/**
	 * 获得最新添加的联系人的Raw_id
	 * @param context
	 * @return 返回最新联系人的raw_id，如果出错或不存在则返回 -1
	 */
	public static int findLastRawId(Context context){
		int rawId = -1;
		String[] projection = new String[]{ContactsContract.RawContacts._ID};
		String orderBy = ContactsContract.RawContacts._ID + " DESC";
		Cursor c = null;
		c = context.getContentResolver().query(RAW_URI, projection, null, null, orderBy);
		if((c != null) && !c.isAfterLast()){
			c.moveToFirst();
			rawId = c.getInt(0);
		}
		if (c != null) c.close();

		c = null;
		return rawId;
	}
	
	/**
	 * 添加多个联系人
	 * @param context
	 * @param people 联系人列表<ContactPerson>
	 */
	public static void contactInsert(Context context, List<ContactPerson> people) {
		if (!people.isEmpty()) {
			//迭代通讯录
			Iterator<ContactPerson> iter = people.iterator();
			while (iter.hasNext()) {
				ContactPerson person = iter.next();
				insertPerson(context, person);
			}
		}
	}
	
	/**
	 * 添加一个联系人
	 * @param person
	 */
	public static void insertPerson(Context context, ContactPerson person){
		//AccountUtil accounts = new AccountUtil(context);
		
		//ContentProviderOperation
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		//RawContacts添加一条记录，以获得Raw_id
		ops.add(ContentProviderOperation.newInsert(
				ContactsContract.RawContacts.CONTENT_URI).withValue(
				ContactsContract.RawContacts.ACCOUNT_TYPE, null)
				.withValue(ContactsContract.RawContacts.ACCOUNT_NAME,
						null).build());
		//添加联系人姓名
		PName name = person.getPName();
		if (name != null) {
			ops.add(ContentProviderOperation.newInsert(URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE,
	                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
					.withValue(PName.DISPLAY_NAME,
							name.getDisaplyName()).withValue(
							PName.GIVEN_NAME, name.getFirstName())
					.withValue(PName.FAMILY_NAME, name.getLastName())
					.build());
		}
		//添加联系人电话号码
		List<PNumber> num_list = person.getPNumber();
		if (!num_list.isEmpty()) {
			Iterator<PNumber> nIter = num_list.iterator();
			while (nIter.hasNext()) {
				PNumber num = nIter.next();
				ops.add(ContentProviderOperation.newInsert(URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
						.withValue(PNumber.NUMBER, num.getNumber())
						.withValue(PNumber.TYPE, num.getNumType())
						.build());
			}

		}
		//添加联系人Email
		List<PEmail> email_lst = person.getPEmail();
		if(!email_lst.isEmpty()){
			Iterator<PEmail> eIter = email_lst.iterator();
			while(eIter.hasNext()){
				PEmail email = eIter.next();
				ops.add(ContentProviderOperation.newInsert(URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
						.withValue(PEmail.ADDRESS, email.getEmail())
						.withValue(PEmail.TYPE, email.getEmailType())
						.build());
			}
		}
		//添加联系人地址
		List<PPostal> postal_lst = person.getPPostal();
		if(!postal_lst.isEmpty()){
			Iterator<PPostal> pIter = postal_lst.iterator();
			while(pIter.hasNext()){
				PPostal postal = pIter.next();
				ops.add(ContentProviderOperation.newInsert(URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
						.withValue(PPostal.FULL_ADDRESS, postal.getFullPostalAddress())
						.build());
			}
		}
		//添加联系人IM工具账号
		List<PIm> pim_lst = person.getPIm();
		if(!pim_lst.isEmpty()){
			Iterator<PIm> iIter = pim_lst.iterator();
			while(iIter.hasNext()){
				PIm im = iIter.next();
				ops.add(ContentProviderOperation.newInsert(URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
						.withValue(PIm.DATA, im.getImData())
						.withValue(PIm.PROTOCOL, im.getImProtocol())
						.build());
			}
		}
		//添加联系人公司组织
		List<POrganization> org_lst = person.getPOrg();
		if(!org_lst.isEmpty()){
			Iterator<POrganization> oIter = org_lst.iterator();
			while(oIter.hasNext()){
				POrganization org = oIter.next();
				ops.add(ContentProviderOperation.newInsert(URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
						.withValue(POrganization.COMPANY, org.getOrgCompany())
						.withValue(POrganization.TITLE,org.getOrgTitle())
						.withValue(POrganization.TYPE, org.getOrgTitle())
						.build());
			}
		}
		//添加联系人个人网站 
		List<PWebsite> website_lst = person.getPWeb();
		if(!website_lst.isEmpty()){
			Iterator<PWebsite> wIter = website_lst.iterator();
			while(wIter.hasNext()){
				PWebsite website = wIter.next();
				ops.add(ContentProviderOperation.newInsert(URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
						.withValue(PWebsite.URL, website.getWebUrl())
						.withValue(PWebsite.TYPE, website.getWebType())
						.build());
			}
		}
		//添加联系人备注
		PNote note = person.getPNote();
		if(!note.isNull()){
			ops.add(ContentProviderOperation.newInsert(URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
					.withValue(PNote.NOTE, note.getNote())
					.build());
		}
		//添加分组
		//暂时不用
		/*
		PGroup group = person.getPGroup();
		if (!group.isNull()){
			ops.add(ContentProviderOperation.newInsert(URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)
					.withValue(PGroup.ID, group.getId())
					.build());
		}
		*/
		
		//添加联系人昵称
		PNickName nickname = person.getPNickname();
		if(!nickname.isNull()){
			ops.add(ContentProviderOperation.newInsert(URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)
					.withValue(PNickName.NAME, nickname.getNickName())
					.withValue(PNickName.TYPE, nickname.getType())
					.build());
		}
		//添加联系人图片 
		PPhoto photo = person.getPPhoto();
		if(!photo.isNull()){
			ops.add(ContentProviderOperation.newInsert(URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
					.withValue(PPhoto.PHOTO, photo.getRawPhoto())
					.build());
		}
		
		//开始批量添加
		try {
			context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 根据联系人"raw_id"删除一个联系人的全部详细信息，但不删除这个联系人
	 * @param context 
	 * @param person 联系人
	 * @throws RemoteException
	 * @throws OperationApplicationException
	 */
	private static void deleteAllItems(Context context, ContactPerson person) throws RemoteException, OperationApplicationException{
		String rawId = String.valueOf(person.getRawId());
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ops.add(ContentProviderOperation.newDelete(URI)
				.withSelection(ContactsContract.Data.RAW_CONTACT_ID + "=?", new String[]{rawId})
				.build());
		context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);	
	}
	/**
	 * 对于已经存在的联系人，根据这个联系人"raw_id"，添加这个联系人的详细信息
	 * @param context 
	 * @param person 联系人
	 */
	private static void insertItems(Context context,ContactPerson person){
		int rawId = person.getRawId();
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		//添加联系人姓名
		PName name = person.getPName();
		if (name != null) {
			ops.add(ContentProviderOperation.newInsert(URI)
					.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
					.withValue(ContactsContract.Data.MIMETYPE,
	                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
					.withValue(PName.DISPLAY_NAME,
							name.getDisaplyName()).withValue(
							PName.GIVEN_NAME, name.getFirstName())
					.withValue(PName.FAMILY_NAME, name.getLastName())
					.build());
		}
		//添加联系人电话号码
		List<PNumber> num_list = person.getPNumber();
		if (!num_list.isEmpty()) {
			Iterator<PNumber> nIter = num_list.iterator();
			while (nIter.hasNext()) {
				PNumber num = nIter.next();
				ops.add(ContentProviderOperation.newInsert(URI)
						.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
						.withValue(PNumber.NUMBER, num.getNumber())
						.withValue(PNumber.TYPE, num.getNumType())
						.build());
			}

		}
		//添加联系人Email
		List<PEmail> email_lst = person.getPEmail();
		if(!email_lst.isEmpty()){
			Iterator<PEmail> eIter = email_lst.iterator();
			while(eIter.hasNext()){
				PEmail email = eIter.next();
				ops.add(ContentProviderOperation.newInsert(URI)
						.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
						.withValue(PEmail.ADDRESS, email.getEmail())
						.withValue(PEmail.TYPE, email.getEmailType())
						.build());
			}
		}
		//添加联系人地址
		List<PPostal> postal_lst = person.getPPostal();
		if(!postal_lst.isEmpty()){
			Iterator<PPostal> pIter = postal_lst.iterator();
			while(pIter.hasNext()){
				PPostal postal = pIter.next();
				ops.add(ContentProviderOperation.newInsert(URI)
						.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
						.withValue(PPostal.FULL_ADDRESS, postal.getFullPostalAddress())
						.build());
			}
		}
		//添加联系人IM工具账号
		List<PIm> pim_lst = person.getPIm();
		if(!pim_lst.isEmpty()){
			Iterator<PIm> iIter = pim_lst.iterator();
			while(iIter.hasNext()){
				PIm im = iIter.next();
				ops.add(ContentProviderOperation.newInsert(URI)
						.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
						.withValue(PIm.DATA, im.getImData())
						.withValue(PIm.PROTOCOL, im.getImProtocol())
						.build());
			}
		}
		//添加联系人公司组织
		List<POrganization> org_lst = person.getPOrg();
		if(!org_lst.isEmpty()){
			Iterator<POrganization> oIter = org_lst.iterator();
			while(oIter.hasNext()){
				POrganization org = oIter.next();
				ops.add(ContentProviderOperation.newInsert(URI)
						.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
						.withValue(POrganization.COMPANY, org.getOrgCompany())
						.withValue(POrganization.TITLE,org.getOrgTitle())
						.withValue(POrganization.TYPE, org.getOrgTitle())
						.build());
			}
		}
		//添加联系人个人网站 
		List<PWebsite> website_lst = person.getPWeb();
		if(!website_lst.isEmpty()){
			Iterator<PWebsite> wIter = website_lst.iterator();
			while(wIter.hasNext()){
				PWebsite website = wIter.next();
				ops.add(ContentProviderOperation.newInsert(URI)
						.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
						.withValue(PWebsite.URL, website.getWebUrl())
						.withValue(PWebsite.TYPE, website.getWebType())
						.build());
			}
		}
		//添加联系人备注
		PNote note = person.getPNote();
		if(!note.isNull()){
			ops.add(ContentProviderOperation.newInsert(URI)
					.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
					.withValue(PNote.NOTE, note.getNote())
					.build());
		}
		//添加分组
		//暂时不用
		/*
		PGroup group = person.getPGroup();
		if (!group.isNull()){
			ops.add(ContentProviderOperation.newInsert(URI)
					.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)
					.withValue(PGroup.ID, group.getId())
					.build());
		}
		*/
		
		//添加联系人昵称
		PNickName nickname = person.getPNickname();
		if(!nickname.isNull()){
			ops.add(ContentProviderOperation.newInsert(URI)
					.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)
					.withValue(PNickName.NAME, nickname.getNickName())
					.withValue(PNickName.TYPE, nickname.getType())
					.build());
		}
		//添加联系人图片 
		PPhoto photo = person.getPPhoto();
		if(!photo.isNull()){
			ops.add(ContentProviderOperation.newInsert(URI)
					.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
					//.withValue(PPhoto.PHOTO, photo.getPhoto())
					.withValue(PPhoto.PHOTO, photo.getRawPhoto())
					.build());
		}
		
		//开始批量添加
		try {
			context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 完全删除一个联系人
	 * @param context
	 * @param person 要删除的联系人
	 * @throws RemoteException
	 * @throws OperationApplicationException
	 */
	public static void contactDelete(Context context, ContactPerson person) throws RemoteException, OperationApplicationException{
		int rawId = person.getRawId();
		contactDelete(context, rawId);
	}
	
	/**
	 * 完全删除一个联系人
	 * @param context
	 * @param rawId 要删除的联系人raw_id
	 * @throws RemoteException
	 * @throws OperationApplicationException
	 */
	public static void contactDelete(Context context, int rawId) throws RemoteException, OperationApplicationException{
		String sRawId = String.valueOf(rawId);
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ops.add(ContentProviderOperation.newDelete(RAW_URI)
				.withSelection(ContactsContract.RawContacts._ID + "=?", new String[]{sRawId})
				.build());
		context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		
	}
	
	/**
	 * 更新一个已存在的联系人
	 * @param context
	 * @param person
	 */
	public static void contactUpdate(Context context , ContactPerson person){
		try {
			deleteAllItems(context, person);
			insertItems(context, person);
			//System.out.println("update person:" + person.toString());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * 删除掉全部联系人，直删除<br>
	 * <b>不能随便用</b>
	 */
	public static void deleteAllContacts(Context context){
		context.getContentResolver().delete(RAW_URI, null, null);
	}
	
	/**
	 * get a profile record, just like get a contact.<br/>
	 * it is same to use <b>getPersonalRecord(context, id, version, true)</b>
	 * @param context
	 * @param id
	 * @param version
	 * @return
	 */
	public static ContactPerson getPersonalRecord(Context context, long id, int version){
		return getPersonalRecord(context, id, version, true);
	}
	
	/**
	 * get a profile record , just like get a contact
	 * @param context
	 * @param id
	 * @param version
	 * @param useRawId
	 * @return
	 */
	public static ContactPerson getPersonalRecord(Context context, long id, int version , boolean useRawId){
		ContactPerson p = new ContactPerson();
		if (version == -1){
			p.setVersion(findVersion(context, id));
		}else{
			p.setVersion(version);
		}
		p.setRawId((int) id);
		Cursor cUnite = null;
		Uri mUri = URI;
		
		String selection = null;
		if (useRawId)
		{
			selection = ContactsContract.Data.RAW_CONTACT_ID + "=" + id;
		}else{
			selection = ContactsContract.Data.CONTACT_ID + "=" + id;
		}
			
		cUnite = context.getContentResolver().query(mUri, null, selection, null, null);
		if (cUnite != null && !cUnite.isAfterLast()) {
			int idIndex = cUnite.getColumnIndex(ContactsContract.Data._ID);
			int remotedIndex = cUnite.getColumnIndex(DetailItemAbs.REMOTE_ID);
			while (cUnite.moveToNext()) {
				String mimitype = cUnite
						.getString(cUnite
								.getColumnIndex(ContactsContract.Data.MIMETYPE));
				// 查找电话号码
				if (mimitype
						.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
					PNumber num = new PNumber();
					num.setNumber(cUnite.getString(cUnite
							.getColumnIndex(PNumber.NUMBER)));
					num.setNumType(cUnite.getInt(cUnite
							.getColumnIndex(PNumber.TYPE)));
					num._id = cUnite.getLong(idIndex);
					num.remoteHash = getRemoteHash(cUnite, remotedIndex);
					p.addNumber(num);
					num = null;
					continue;
				}
				// 查找名称
				if (mimitype
						.equals(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
					PName name = new PName();
					name.setDisaplyName(cUnite.getString(cUnite
							.getColumnIndex(PName.DISPLAY_NAME)));
					name.setFirstName(cUnite.getString(cUnite
							.getColumnIndex(PName.GIVEN_NAME)));
					name.setLast_name(cUnite.getString(cUnite
							.getColumnIndex(PName.FAMILY_NAME)));
					name._id = cUnite.getLong(idIndex);
					name.remoteHash = getRemoteHash(cUnite, remotedIndex);
					p.setPName(name);
					//name = null;
					continue;
				}
				// 查找Email
				if (mimitype
						.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
					PEmail email = new PEmail();
					email.setEmail(cUnite.getString(cUnite
							.getColumnIndex(PEmail.ADDRESS)));
					email.setEmailType(cUnite.getInt(cUnite
							.getColumnIndex(PEmail.TYPE)));
					email._id = cUnite.getLong(idIndex);
					email.remoteHash = getRemoteHash(cUnite, remotedIndex);
					p.addEmail(email);
					email = null;
					continue;
				}
				// 查找IM帐号
				if (mimitype.equals(ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)){
					PIm im = new PIm();
					im.setImData(cUnite.getString(cUnite.getColumnIndex(PIm.DATA)));
					im.setImProtocol(cUnite.getInt(cUnite.getColumnIndex(PIm.PROTOCOL)));
					im._id = cUnite.getLong(idIndex);
					im.remoteHash = getRemoteHash(cUnite, remotedIndex);
					p.addIM(im);
					im = null;
					continue;
				}
				// 查找个人网站
				if (mimitype
						.equals(ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)) {
					PWebsite web = new PWebsite();
					web.setWebUrl(cUnite.getString(cUnite
							.getColumnIndex(PWebsite.URL)));
					web.setWebType(cUnite.getInt(cUnite
							.getColumnIndex(PWebsite.TYPE)));
					web._id = cUnite.getLong(idIndex);
					web.remoteHash = getRemoteHash(cUnite, remotedIndex);
					p.addWebSite(web);
					web = null;
					continue;
				}
				// 查找公司组织
				if (mimitype
						.equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)) {
					POrganization org = new POrganization();
					org.setOrgCompany(cUnite.getString(cUnite
							.getColumnIndex(POrganization.COMPANY)));
					org.setOrgTitle(cUnite.getString(cUnite
							.getColumnIndex(POrganization.TITLE)));
					org.setOrgType(cUnite.getInt(cUnite
							.getColumnIndex(POrganization.TYPE)));
					org._id = cUnite.getLong(idIndex);
					org.remoteHash = getRemoteHash(cUnite, remotedIndex);
					p.addOrg(org);
					org = null;
					continue;
				}
				// 查找住址
				if (mimitype.equals(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)){
					PPostal postal = new PPostal();
					postal.setFullPostalAddress(cUnite.getString(cUnite.getColumnIndex(PPostal.FULL_ADDRESS)));
					postal.setPostalType(cUnite.getInt(cUnite.getColumnIndex(PPostal.TYPE)));
					postal.setPostalStreet(cUnite.getString(cUnite.getColumnIndex(PPostal.STREET)));
					postal.setPostalBox(cUnite.getString(cUnite.getColumnIndex(PPostal.POBOX)));
					postal.setNeighborhood(cUnite.getString(cUnite.getColumnIndex(PPostal.NEIGHBORHOOD)));
					postal.setPostalCity(cUnite.getString(cUnite.getColumnIndex(PPostal.CITY)));
					postal.setPostalState(cUnite.getString(cUnite.getColumnIndex(PPostal.REGION)));
					postal.setPostalZip(cUnite.getString(cUnite.getColumnIndex(PPostal.POSTCODE)));
					postal.setPostalCountry(cUnite.getString(cUnite.getColumnIndex(PPostal.COUNTRY)));					
					
					postal._id = cUnite.getLong(idIndex);
					postal.remoteHash = getRemoteHash(cUnite, remotedIndex);
					p.addPostal(postal);
					postal = null;
					continue;
				}
				// 查找昵称
				if (mimitype
						.equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)) {
					PNickName nickname = new PNickName();
					nickname.setNickName(cUnite.getString(cUnite
							.getColumnIndex(PNickName.NAME)));
					nickname.setType(cUnite.getInt(cUnite
							.getColumnIndex(PNickName.TYPE)));
					nickname._id = cUnite.getLong(idIndex);
					nickname.remoteHash = getRemoteHash(cUnite, remotedIndex);
					p.setPNickname(nickname);
					//nickname = null;
					continue;
				}
				// 查找备注
				if (mimitype
						.equals(ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)) {
					PNote note = new PNote();
					note.setNote(cUnite.getString(cUnite
							.getColumnIndex(PNote.NOTE)));
					note._id = cUnite.getLong(idIndex);
					note.remoteHash = getRemoteHash(cUnite, remotedIndex);
					p.setPNote(note);
					//note = null;
					continue;
				}
				
				// 查找分组
				/*
				if (mimitype.equals(ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)){
					PGroup group = GroupsUtil.get(context).getGroup(cUnite.getInt(cUnite.getColumnIndex(PGroup.ID)));
					group._id = cUnite.getLong(idIndex);
					group.remoteHash = getRemoteHash(cUnite, remotedIndex);
					p.setPGroup(group);
					continue;
					
				}*/
				// 查找头像
				if (mimitype
						.equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)) {
					//byte[] buff = cUnite.getBlob(cUnite
					//		.getColumnIndex(PPhoto.PHOTO));
					//if (buff == null)
					//	continue;
					
					//String photoStr = new String(Base64.encodeBase64(buff));
					//p.setPhotoStr(photoStr);
					/*
					PPhoto photo = new PPhoto();
					photo.setRawPhoto(buff);
					photo._id = cUnite.getLong(idIndex);
					p.setPPhoto(photo);
					*/
					
					/*PPhoto photo = new PPhoto();
					ByteArrayInputStream ip = new ByteArrayInputStream(
							buff);
					Bitmap bm = BitmapFactory.decodeStream(ip);
					photo.setPhoto(bm);
					photo._id = cUnite.getLong(idIndex);
					photo.remoteHash = getRemoteHash(cUnite, remotedIndex);
					p.setPPhoto(photo);
					try {
						ip.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					photo = null;*/
				}
				//end all
			}
		}
		if (cUnite != null){
			cUnite.close();
		}
		cUnite = null;
		return p;
	}
	
	public static long getRemotedId(Cursor c, int idIndex){
		long remoteId = -1;
		if (!c.isNull(idIndex)){
			remoteId = Long.valueOf(c.getString(idIndex));
		}
		return remoteId;
	}
	
	public static String getRemoteHash(Cursor c, int hashIndex){
		String remoteHash = null;
		if (!c.isNull(hashIndex)){
			remoteHash = c.getString(hashIndex);
		}
		return remoteHash;
	}

	private static final HashMap<Integer, String> withOutMimeMap = new HashMap<Integer, String>();
	static {
		withOutMimeMap.put(2, ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE);
		withOutMimeMap.put(3, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
		withOutMimeMap.put(4, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
		withOutMimeMap.put(5, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE);
		withOutMimeMap.put(6, ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE);
	}

	
	/**
	 * check person record modify
	 * @return
	 */
	public static boolean isPersonalRecordModifyed(){
		return false;
	}
	

	
	/**
	 * get a remote hash list map
	 * @param context
	 * @param profileRawId
	 * @return HashMap<_id, remoteHash>
	 */
	public static HashMap<String, Long> getRemoteHashMap(Context context, long profileRawId){
		HashMap<String, Long> remoteMap = new HashMap<String, Long>();
		
		String[] projection = new String[] { 
				ContactsContract.Data._ID,	//0
				ContactsContract.Data.MIMETYPE,	//1 
				DetailItemAbs.REMOTE_ID	//2 
				};
		String selection = ContactsContract.Data.RAW_CONTACT_ID + "=" + profileRawId;
		Cursor c = null;
		c = context.getContentResolver().query(URI, projection, selection, null, null);
		if (c != null){
			while(c.moveToNext()){
				String mimetype = c.getString(1);
				if (!withOutMimeMap.containsValue(mimetype)){
					remoteMap.put(c.getString(2), c.getLong(0));
				}
			}
			c.close();
		}
		
		
		return remoteMap;
	}

	
	private static void insertPersonalRecordItem(Context context, long profileRawId, ContactPerson profile){
		long rawId = profileRawId;
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		//添加联系人姓名
		PName name = profile.getPName();
		if (name != null) {
			ops.add(ContentProviderOperation.newInsert(URI)
					.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
					.withValue(ContactsContract.Data.MIMETYPE,
	                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
					.withValue(PName.DISPLAY_NAME,
							name.getDisaplyName()).withValue(
							PName.GIVEN_NAME, name.getFirstName())
					.withValue(PName.FAMILY_NAME, name.getLastName())
					.withValue(DetailItemAbs.REMOTE_ID, name.remoteHash)
					.build());
		}
		//添加联系人电话号码
		List<PNumber> num_list = profile.getPNumber();
		if (!num_list.isEmpty()) {
			Iterator<PNumber> nIter = num_list.iterator();
			while (nIter.hasNext()) {
				PNumber num = nIter.next();
				ops.add(ContentProviderOperation.newInsert(URI)
						.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
						.withValue(PNumber.NUMBER, num.getNumber())
						.withValue(PNumber.TYPE, num.getNumType())
						.withValue(DetailItemAbs.REMOTE_ID, num.remoteHash)
						.build());
			}

		}
		//添加联系人Email
		List<PEmail> email_lst = profile.getPEmail();
		if(!email_lst.isEmpty()){
			Iterator<PEmail> eIter = email_lst.iterator();
			while(eIter.hasNext()){
				PEmail email = eIter.next();
				ops.add(ContentProviderOperation.newInsert(URI)
						.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
						.withValue(PEmail.ADDRESS, email.getEmail())
						.withValue(PEmail.TYPE, email.getEmailType())
						.withValue(DetailItemAbs.REMOTE_ID, email.remoteHash)
						.build());
			}
		}
		//添加联系人地址
		List<PPostal> postal_lst = profile.getPPostal();
		if(!postal_lst.isEmpty()){
			Iterator<PPostal> pIter = postal_lst.iterator();
			while(pIter.hasNext()){
				PPostal postal = pIter.next();
				ops.add(ContentProviderOperation.newInsert(URI)
						.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
						.withValue(PPostal.FULL_ADDRESS, postal.getFullPostalAddress())
						.withValue(DetailItemAbs.REMOTE_ID, postal.remoteHash)
						.build());
			}
		}
		//添加联系人IM工具账号
		List<PIm> pim_lst = profile.getPIm();
		if(!pim_lst.isEmpty()){
			Iterator<PIm> iIter = pim_lst.iterator();
			while(iIter.hasNext()){
				PIm im = iIter.next();
				ops.add(ContentProviderOperation.newInsert(URI)
						.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
						.withValue(PIm.DATA, im.getImData())
						.withValue(PIm.PROTOCOL, im.getImProtocol())
						.withValue(DetailItemAbs.REMOTE_ID, im.remoteHash)
						.build());
			}
		}
		//添加联系人公司组织
		List<POrganization> org_lst = profile.getPOrg();
		if(!org_lst.isEmpty()){
			Iterator<POrganization> oIter = org_lst.iterator();
			while(oIter.hasNext()){
				POrganization org = oIter.next();
				ops.add(ContentProviderOperation.newInsert(URI)
						.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
						.withValue(POrganization.COMPANY, org.getOrgCompany())
						.withValue(POrganization.TITLE,org.getOrgTitle())
						.withValue(POrganization.TYPE, org.getOrgTitle())
						.withValue(DetailItemAbs.REMOTE_ID, org.remoteHash)
						.build());
			}
		}
		//添加联系人个人网站 
		List<PWebsite> website_lst = profile.getPWeb();
		if(!website_lst.isEmpty()){
			Iterator<PWebsite> wIter = website_lst.iterator();
			while(wIter.hasNext()){
				PWebsite website = wIter.next();
				ops.add(ContentProviderOperation.newInsert(URI)
						.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
						.withValue(PWebsite.URL, website.getWebUrl())
						.withValue(PWebsite.TYPE, website.getWebType())
						.withValue(DetailItemAbs.REMOTE_ID, website.remoteHash)
						.build());
			}
		}
		//添加联系人备注
		String note = profile.getNote();
		if (note != null){
			ops.add(ContentProviderOperation.newInsert(URI)
					.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
					.withValue(PNote.NOTE, note)
					.build());
		}
		/*
		PNote note = profile.getPNote();
		if(!note.isNull()){
			ops.add(ContentProviderOperation.newInsert(URI)
					.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
					.withValue(PNote.NOTE, note.getNote())
					.withValue(DetailItemAbs.REMOTE_ID, note.remoteHash)
					.build());
		}*/
		//添加分组
		/*
		GroupsUtil g = GroupsUtil.get(context);
		ops.add(ContentProviderOperation
				.newInsert(URI)
				.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)
				.withValue(PGroup.ID, g.getAppGroupId()).build());
		*/
		//添加联系人昵称
		//PNickName nickname = profile.getPNickname();
		String nickname = profile.getNickName();
		if (nickname != null){
			ops.add(ContentProviderOperation.newInsert(URI)
					.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)
					.withValue(PNickName.NAME, nickname)
					.withValue(PNickName.TYPE, 1)
					.build());
		}
		
		/*
		if(!nickname.isNull()){
			ops.add(ContentProviderOperation.newInsert(URI)
					.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)
					.withValue(PNickName.NAME, nickname.getNickName())
					.withValue(PNickName.TYPE, nickname.getType())
					.withValue(DetailItemAbs.REMOTE_ID, nickname.remoteHash)
					.build());
		}*/
		//添加联系人图片 
		
		if (profile.hasPhotoStr()){
			//byte[] rawPhoto = Base64.decodeBase64(profile.getPhotoStr().getBytes());
			//ops.add(ContentProviderOperation.newInsert(URI)
			//		.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
			//		.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
			//		.withValue(PPhoto.PHOTO, rawPhoto)
			//		.build());
		}
		
		/*PPhoto photo = profile.getPPhoto();
		if(!photo.isNull()){
			ops.add(ContentProviderOperation.newInsert(URI)
					.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
					.withValue(PPhoto.PHOTO, photo.getPhoto())
					.withValue(DetailItemAbs.REMOTE_ID, photo.remoteHash)
					.build());
		}*/
		
		//开始批量添加
		try {
			context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    /**
     * Returns true if all the characters are meaningful as digits
     * in a phone number -- letters, digits, and a few punctuation marks.
     */
    public static boolean isPhoneNumber(CharSequence cons) {
        int len = cons.length();

        for (int i = 0; i < len; i++) {
            char c = cons.charAt(i);

            if ((c >= '0') && (c <= '9')) {
                continue;
            }
            if ((c == ' ') || (c == '-') || (c == '(') || (c == ')') || (c == '.') || (c == '+')
                    || (c == '#') || (c == '*')) {
                continue;
            }
            if ((c >= 'A') && (c <= 'Z')) {
                continue;
            }
            if ((c >= 'a') && (c <= 'z')) {
                continue;
            }

            return false;
        }

        return true;
    }
    
    /**
     * 返回联系人的电话列表
     * @param context
     * @param contactId
     * @return
     */
    public static List<PNumber> getContactPhoneList(Context context, long contactId){
    	List<PNumber> numberList = new ArrayList<PNumber>();
    	String[] numberProjection = new String[]{
    			ContactsContract.CommonDataKinds.Phone.NUMBER,
    			ContactsContract.CommonDataKinds.Phone.TYPE
    	};
    	String where = String.format("%s = %d AND %s = '%s'", ContactsContract.Data.CONTACT_ID, contactId, ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
    	Cursor c = null;
    	c = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, numberProjection, where, null, null);
    	if (c != null && !c.isAfterLast()){
    		while(c.moveToNext()){
    			PNumber number = new PNumber();
    			number.setNumber(c.getString(0));
    			number.setNumType(c.getInt(1));
    			numberList.add(number);
    		}
    	}
    	if (c != null) c.close();
    	return numberList;
	}

}
