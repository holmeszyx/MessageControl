package z.hol.android.mc.utils.message;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import z.hol.android.mc.utils.CompareList;
import z.hol.android.mc.utils.contact.ContactUtil;
import z.hol.android.mc.utils.contact.SimpleContactPerson;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;

public class SMSUtil {
	public static final int TYPE_SEND = 2;
	public static final int TYPE_RECIVE = 1;

	/*
	 * 发送指定内容短信给指定号码 to :号码 , msg:短信内容 返回true成功,fasle失败
	 */
	public static boolean smsSender(Context context, String to, String msg, boolean record) {
		SmsManager smsManager = SmsManager.getDefault();
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent("z.hol.android.mc.sms.send"), 0);
		try {

				List<String> contents = smsManager.divideMessage(msg);
				for (String content : contents){
					smsManager.sendTextMessage(to, null, content, pendingIntent, null);
				}
			
				if (record){
					ContentValues values = new ContentValues(); 
					values.put("address", to); 
					values.put("body", msg); 
					context.getContentResolver().insert(Uri.parse("content://sms/sent"), values); 
				}
				return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * 获取最新短信ID
	 * 
	 * @param context
	 * @return
	 */
	public static int findLastSmsId(Context context) {
		int smsId = -1;
		String[] projection = new String[] { SMS.ID };
		String order = SMS.ID + " DESC";
		Cursor c = null;
		c = context.getContentResolver().query(SMS.CONTENT_URI, projection,
				null, null, order);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			smsId = c.getInt(0);
		}
		return smsId;
	}
	
	
	private static final String[] SmsProjection = new String[] {
				SMS.ID,	//0
				SMS.THREAD_ID,	//1 
				SMS.ADDRESS,	//2 
				SMS.PERSON,	//3 
				SMS.DATE,	//4 
				SMS.PROTORCOL,	//5
				SMS.READ,	//6
				SMS.STATUS,	//7 
				SMS.TYPE,	//8 
				SMS.REPLY_PATH,	//9 
				SMS.SUBJECT,	//10
				SMS.BODY,	//12 
				SMS.SERV_CENTER,	//13 
				SMS.LOCKED,	//14 
				SMS.ERRER_CODE,	//15 
				SMS.SEEN	//16 
			};
	
	private static final String[] SmsProjectionLite = new String[] {
			SMS.ID,		//0
			SMS.THREAD_ID,	//1 
			SMS.ADDRESS,	//2 
			SMS.DATE,	//3 
			SMS.PROTORCOL,	//4
			SMS.READ,	//5
			SMS.STATUS,	//6 
			SMS.TYPE,	//7 
			SMS.BODY,	//8 
			SMS.SERV_CENTER	//9 
			//SMS.SEEN	//10 
		};
	/**
	 * 查询短信
	 * 
	 * @param context
	 * @param type
	 *            信息类型， 接收 1 ，发送 2，全部 -1
	 * @param read
	 *            信息状态， 已读 1，未读 0，全部 -1
	 * @return List;
	 */
	public static List<SMSInfo> getSms(Context context, int type, int read) {
		List<SMSInfo> list = new ArrayList<SMSInfo>();
		Cursor c;
		String selection = null;
		String[] selectionArgs;

		// 判断要查的短信类型
		if (type != -1) {
			selection = SMS.TYPE + "=?";
			if (read != -1) {
				selection += " AND " + SMS.READ + "=?";
				selectionArgs = new String[] { String.valueOf(type),
						String.valueOf(read) };
			} else {
				selectionArgs = new String[] { String.valueOf(type) };
			}
		} else {
			if (read != -1) {
				selection = SMS.READ + "=?";
				selectionArgs = new String[] { String.valueOf(read) };
			} else {
				selection = null;
				selectionArgs = null;
			}

		}

		c = context.getContentResolver().query(SMS.CONTENT_URI, SmsProjectionLite,
				selection, selectionArgs, null);

		// 查找出一条短信
		if (c != null) {
			while (c.moveToNext()) {
				SMSInfo smsInfo = new SMSInfo();
				smsInfo.setId(c.getInt(0));
				smsInfo.setThreadId(c.getInt(1));
				smsInfo.setAddress(c.getString(2));
				smsInfo.setDate(c.getLong(3));
				smsInfo.setProtocol(c.getInt(4));
				smsInfo.setRead(c.getInt(5));
				smsInfo.setStatus(c.getInt(6));
				smsInfo.setType(c.getInt(7));;
				smsInfo.setBody(c.getString(8));
				smsInfo.setServCenter(c.getString(9));
				//smsInfo.setSeen(c.getInt(10));

				list.add(smsInfo);
			}
		}

		c.close();
		return list;
	}
	
	/**
	 * 得到最后一条短信
	 * @param context
	 * @return
	 */
	public static SMSInfo getLastSMS(Context context){
		SMSInfo smsInfo = null;
		String order = SMS.ID + " DESC";
		Cursor c = context.getContentResolver().query(SMS.CONTENT_URI, SmsProjectionLite, null, null, order);
		if (c != null && c.moveToFirst()){
			smsInfo = new SMSInfo();
			smsInfo.setId(c.getInt(0));
			smsInfo.setThreadId(c.getInt(1));
			smsInfo.setAddress(c.getString(2));
			smsInfo.setDate(c.getLong(3));
			smsInfo.setProtocol(c.getInt(4));
			smsInfo.setRead(c.getInt(5));
			smsInfo.setStatus(c.getInt(6));
			smsInfo.setType(c.getInt(7));;
			smsInfo.setBody(c.getString(8));
			smsInfo.setServCenter(c.getString(9));
			//smsInfo.setSeen(c.getInt(10));
		}
		if (c != null) c.close();
		
		return smsInfo;
	}
	
	/**
	 * 获取在联系人中的短信
	 * @param context context
	 * @param type 短信类型，收或发
	 * @param read 短信状态，是否已读等
	 * @return 短信列表
	 */
	public static List<SMSInfo> getSmsInContact(Context context, int type, int read) {
		List<SMSInfo> list = new ArrayList<SMSInfo>();
		Cursor c;
		String selection = null;
		String[] selectionArgs;

		// 判断要查的短信类型
		if (type != -1) {
			selection = SMS.TYPE + "=?";
			if (read != -1) {
				selection += " AND " + SMS.READ + "=?";
				selectionArgs = new String[] { String.valueOf(type),
						String.valueOf(read) };
			} else {
				selectionArgs = new String[] { String.valueOf(type) };
			}
		} else {
			if (read != -1) {
				selection = SMS.READ + "=?";
				selectionArgs = new String[] { String.valueOf(read) };
			} else {
				selection = null;
				selectionArgs = null;
			}

		}

		c = context.getContentResolver().query(SMS.CONTENT_URI, SmsProjectionLite,
				selection, selectionArgs, null);

		// 查找出一条短信
		if (c != null) {
			while (c.moveToNext()) {
				String address = c.getString(2);
				if (ContactUtil.isInContactByNumber(context, address)){
					SMSInfo smsInfo = new SMSInfo();
					smsInfo.setId(c.getInt(0));
					smsInfo.setThreadId(c.getInt(1));
					smsInfo.setAddress(c.getString(2));
					smsInfo.setDate(c.getLong(3));
					smsInfo.setProtocol(c.getInt(4));
					smsInfo.setRead(c.getInt(5));
					smsInfo.setStatus(c.getInt(6));
					smsInfo.setType(c.getInt(7));;
					smsInfo.setBody(c.getString(8));
					smsInfo.setServCenter(c.getString(9));
					//smsInfo.setSeen(c.getInt(10));					
					list.add(smsInfo);
				}
			}
		}

		if (c != null) c.close();
		c = null;
		return list;
	}
	
	/**
	 * 获取属于联系人的短信, 通过属于联系人的会话来获取
	 * @param context context
	 * @param threadList 属于联系人的短信会话
	 * @return 短信列表
	 */
	public static List<SMSInfo> getSmsInContact(Context context, List<SMSThread> threadList){
		
		List<SMSInfo> list = new ArrayList<SMSInfo>();
		Cursor c;
		String selection = null;
		String[] selectionArgs = null;

		// 判断要查的短信类型
		selection = SMS.THREAD_ID + " " + getSqlWhereByThreadList(threadList);

		c = context.getContentResolver().query(SMS.CONTENT_URI, SmsProjectionLite,
				selection, selectionArgs, null);

		// 查找出一条短信
		if (c != null) {
			while (c.moveToNext()) {
				SMSInfo smsInfo = new SMSInfo();
				smsInfo.setId(c.getInt(0));
				smsInfo.setThreadId(c.getInt(1));
				smsInfo.setAddress(c.getString(2));
				smsInfo.setDate(c.getLong(3));
				smsInfo.setProtocol(c.getInt(4));
				smsInfo.setRead(c.getInt(5));
				smsInfo.setStatus(c.getInt(6));
				smsInfo.setType(c.getInt(7));
				smsInfo.setBody(c.getString(8));
				smsInfo.setServCenter(c.getString(9));
				//smsInfo.setSeen(c.getInt(10));
				list.add(smsInfo);
			}
		}

		c.close();
		return list;
		
	}
	
	/**
	 * 直接通过联系人的短信短信会话列表来获取属于联系人的短信列表
	 * @param context
	 * @return 短信列表
	 */
	public static List<SMSInfo> getSmsInContact(Context context){
		return getSmsInContact(context, getThreadIdListInContact(context));
	}
	
	private static String getSqlWhereByThreadList(List<SMSThread> threadList){
		String where = null;
		StringBuilder sb = new StringBuilder();
		sb.append("IN (");
		for (int i = 0; i < threadList.size(); i++){
			if (i == threadList.size() - 1){
				sb.append(threadList.get(i).getId());
			}else{
				sb.append(threadList.get(i).getId());
				sb.append(",");
			}
		}
		sb.append(")");
		where = sb.toString();
		return where;
	}
	
	public static List<SMSInfo> getSmsByContactId(Context context, int type, int read, long contactId){
		List<SMSInfo> list = new ArrayList<SMSInfo>();
		Cursor c;
		String selection = null;
		String[] selectionArgs;

		// 判断要查的短信类型
		if (type != -1) {
			selection = SMS.TYPE + "=?";
			if (read != -1) {
				selection += " AND " + SMS.READ + "=?";
				selectionArgs = new String[] { String.valueOf(type),
						String.valueOf(read) };
			} else {
				selectionArgs = new String[] { String.valueOf(type) };
			}
		} else {
			if (read != -1) {
				selection = SMS.READ + "=?";
				selectionArgs = new String[] { String.valueOf(read) };
			} else {
				selection = null;
				selectionArgs = null;
			}

		}

		c = context.getContentResolver().query(SMS.CONTENT_URI, SmsProjectionLite,
				selection, selectionArgs, null);

		// 查找出一条短信
		if (c != null) {
			while (c.moveToNext()) {
				String address = c.getString(2);
				if (ContactUtil.isNumberBelongToContact(context, address, contactId)){
					SMSInfo smsInfo = new SMSInfo();
					smsInfo.setId(c.getInt(0));
					smsInfo.setThreadId(c.getInt(1));
					smsInfo.setAddress(c.getString(2));
					smsInfo.setDate(c.getLong(3));
					smsInfo.setProtocol(c.getInt(4));
					smsInfo.setRead(c.getInt(5));
					smsInfo.setStatus(c.getInt(6));
					smsInfo.setType(c.getInt(7));;
					smsInfo.setBody(c.getString(8));
					smsInfo.setServCenter(c.getString(9));
					//smsInfo.setSeen(c.getInt(10));
					list.add(smsInfo);
				}
			}
		}

		c.close();
		return list;
	}
	
	/**
	 * 根据回放ID得到 短信
	 * @param context
	 * @param threadId
	 * @return
	 */
	public static List<SMSInfo> getSMSByThreadId(Context context, long threadId){
		List<SMSInfo> list = new ArrayList<SMSInfo>();
		Cursor c;
		String selection = null;
		String order = SMS.DATE + " ASC";

		// 判断要查的短信类型
		selection = SMS.THREAD_ID + "=" + String.valueOf(threadId);
		//System.out.println(selection);
		c = context.getContentResolver().query(SMS.CONTENT_URI, SmsProjectionLite,
				selection, null, order);

		// 查找出一条短信
		if (c != null) {
			while (c.moveToNext()) {
				SMSInfo smsInfo = new SMSInfo();
				smsInfo.setId(c.getInt(0));
				smsInfo.setThreadId(c.getInt(1));
				smsInfo.setAddress(c.getString(2));
				smsInfo.setDate(c.getLong(3));
				smsInfo.setProtocol(c.getInt(4));
				smsInfo.setRead(c.getInt(5));
				smsInfo.setStatus(c.getInt(6));
				smsInfo.setType(c.getInt(7));;
				smsInfo.setBody(c.getString(8));
				smsInfo.setServCenter(c.getString(9));
				//smsInfo.setSeen(c.getInt(10));

				list.add(smsInfo);
			}
		}

		c.close();
		return list;
	}
	/**
	 * 获得一条特定的短信
	 * 
	 * @param context
	 * @param id
	 *            短信的ID
	 * @return SMSInfo
	 */
	public static SMSInfo getOneSMS(Context context, long id) {

		SMSInfo smsInfo = new SMSInfo();
		String selection = SMS.ID + "=" + String.valueOf(id);
		Cursor c = null;
		c = context.getContentResolver().query(SMS.CONTENT_URI, SmsProjectionLite,
				selection, null, null);
		if ((c != null) && (c.getCount() == 1)) {
			while (c.moveToNext()) {
				smsInfo.setId(c.getInt(0));
				smsInfo.setThreadId(c.getInt(1));
				smsInfo.setAddress(c.getString(2));
				smsInfo.setDate(c.getLong(3));
				smsInfo.setProtocol(c.getInt(4));
				smsInfo.setRead(c.getInt(5));
				smsInfo.setStatus(c.getInt(6));
				smsInfo.setType(c.getInt(7));;
				smsInfo.setBody(c.getString(8));
				smsInfo.setServCenter(c.getString(9));
				//smsInfo.setSeen(c.getInt(10));
			}
		}

		return smsInfo;

	}

	/**
	 * 获取未读短信
	 * 
	 * @param context
	 * @return
	 */
	public static List<SMSInfo> getUnreadSMS(Context context) {
		return getSms(context, -1, 0);

	}
	
	/**
	 * 得到未读短信数量
	 * @param context
	 * @return
	 */
	public static int getUnreadSMSCount(Context context){
		String[] projection = new String[]{SMS.ID};
		String where = SMS.READ + " = 0";
		Cursor  c = context.getContentResolver().query(SMS.CONTENT_URI, projection, where, null, null);
		int count = 0;
		if (c != null){
			count = c.getCount();
		}
		if (c != null) c.close();
		return count;
	}

	/**
	 * 获取发送的短信
	 * 
	 * @param context
	 * @return
	 */
	public static List<SMSInfo> getSendedSMS(Context context) {
		return getSms(context, 2, -1);
	}
	

	/**
	 * 获取接收的短信
	 * 
	 * @param context
	 * @return
	 */
	public static List<SMSInfo> getReceiveSMS(Context context) {
		return getSms(context, 1, -1);
	}

	/**
	 * 将特定短信标记为已读
	 * 
	 * @param cr
	 * @param id
	 * @return
	 */
	public static boolean makeAsRead(ContentResolver cr, int id) {
		try {
			ContentValues values = new ContentValues();
			values.put(SMS.READ, 1);
			//values.put("seen", 1);
			cr.update(SMS.CONTENT_URI, values, SMS.ID + "=?",
					new String[] { String.valueOf(id) });
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 批量插入短信
	 * 
	 * @param context
	 * @param smsInfos
	 */
	public static void insertSMS(Context context, List<SMSInfo> smsInfos) {
		if (smsInfos != null) {
			Iterator<SMSInfo> iter = smsInfos.iterator();
			while (iter.hasNext()) {
				SMSInfo smsInfo = new SMSInfo();
				smsInfo = iter.next();
				insertSMS(context, smsInfo);
			}

		}

	}

	/**
	 * 插入一条短信
	 * 
	 * @param context
	 * @param smsInfo
	 */
	public static void insertSMS(Context context, SMSInfo smsInfo) {
		try {
			ContentValues values = new ContentValues();
			values.put(SMS.ADDRESS, smsInfo.getAddress());
			values.put(SMS.DATE, smsInfo.getDate());
			values.put(SMS.STATUS, smsInfo.getStatus());
			values.put(SMS.TYPE, smsInfo.getType());
			values.put(SMS.READ, smsInfo.getRead());
			values.put(SMS.SUBJECT, smsInfo.getSubject());
			values.put(SMS.BODY, smsInfo.getBody());
			//values.put(SMS.SEEN, smsInfo.getSeen());
			context.getContentResolver().insert(SMS.CONTENT_URI, values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 删除一条特写短信
	 * 
	 * @param context
	 * @param id
	 */
	public static void deleteSMS(Context context, long id) {
		//String where = SMS.ID + "=" + id;
		//context.getContentResolver().delete(SMS.CONTENT_URI, where, null);
		Uri deleteUri = ContentUris.withAppendedId(SMS.CONTENT_URI, id);
		context.getContentResolver().delete(deleteUri, null, null);
	}
	
	/**
	 * 删除全部短信<br>
	 * <b>不能随便用</b>
	 * @param context
	 */
	public static void deleteAllSMS(Context context){
		context.getContentResolver().delete(SMS.CONTENT_URI, null, null);
	}
	
	/**
	 * 获取会话列表
	 * @param context
	 * @return
	 */
	public static List<SMSThread> getThreads(Context context) {
		List<SMSThread> list = new ArrayList<SMSThread>();
		String[] projection = new String[] { 
				SMSThread.ID, 
				SMSThread.DATE,
				SMSThread.MSG_COUNT, 
				SMSThread.RECIPIENT_IDS,
				SMSThread.SNIPPET, 
				SMSThread.SNIPPET_CS, 
				SMSThread.READ,
				SMSThread.TYPE, 
				SMSThread.ERROR, 
				SMSThread.HAS_ATTACHMENT 
				};
		String[] projectionAddress = new String[]{SMSThread.ADDRESS};
		Cursor c = null;
		c = context.getContentResolver().query(SMSThread.CONTENT_URI, projection, null, null, null);
		if (c != null){
			while(c.moveToNext()){
				//get number
				String recipientId = c.getString(3).trim();
				String[] recipientIds = recipientId.split(" ");
				if (recipientIds.length > 1) continue;		//群发问题,待解决
				
				SMSThread smsThread = new SMSThread();
						
				smsThread.setId(c.getInt(0));
				smsThread.setDate(c.getLong(1));
				smsThread.setMessageCount(c.getInt(2));
				
				Cursor addressCursor = null;	
				//Uri tempUri = Uri.withAppendedPath(SMSThread.ADDRESS_URI, "*");
				addressCursor = context.getContentResolver().query(SMSThread.ADDRESS_URI, projectionAddress, SMSThread.ID + "=" + recipientIds[0], null, null);
				addressCursor.moveToFirst();
				SimpleContactPerson smpPerson = ContactUtil.getSimplePersonByNumber(context, addressCursor.getString(0));
				smsThread.setRecipient(smpPerson.getDisplayName());
				addressCursor.close();
				addressCursor = null;
				
				smsThread.setSmpPerson(smpPerson);
				smsThread.setSnippet(c.getString(4));
				smsThread.setSnippetCs(c.getInt(5));
				smsThread.setRead(c.getInt(6));
				smsThread.setType(c.getInt(7));
				smsThread.setError(c.getInt(8));
				smsThread.setHasAttachment(c.getInt(9));
				list.add(smsThread);
			}
		}
		c.close();
		c = null;
		return list;
	}
	
	/**
	 * just thread id list in contact
	 * @param context
	 * @return
	 */
	public static List<SMSThread> getThreadIdListInContact(Context context){
		List<SMSThread> list = new ArrayList<SMSThread>();
		String[] projection = new String[] { SMSThread.ID, SMSThread.RECIPIENT_IDS};
		String[] projectionAdd = new String[]{SMSThread.ADDRESS};
		Cursor c = null;
		c = context.getContentResolver().query(SMSThread.CONTENT_URI, projection, null, null, null);
		if (c != null){
			while(c.moveToNext()){

				int threadId = c.getInt(0);
				//get number
				//bug 修复 11.10.28
				//当群发过短信后，这个ID会保存多个号码id，由空格分开。如 “7 9 10”。
				//会使后提取号码出错
				String RecipientId = c.getString(1);
				Cursor addressCursor = null;	
				//Uri tempUri = Uri.withAppendedPath(SMSThread.ADDRESS_URI, "*");
				addressCursor = context.getContentResolver().query(SMSThread.ADDRESS_URI, projectionAdd, SMSThread.ID + "=" + RecipientId, null, null);
				addressCursor.moveToFirst();
				boolean inContact = ContactUtil.isInContactByNumber(context, addressCursor.getString(0));
				addressCursor.close();
				addressCursor = null;
				
				if (!inContact) continue;
					
				SMSThread smsThread = new SMSThread();		
				smsThread.setId(threadId);
				list.add(smsThread);
			}
		}
		c.close();
		c = null;
		return list;
	}
	
	/**
	 * 获取短信接收者号码列表
	 * @param context
	 * @return
	 */
	public static List<SMSThread.Recipient> getRecipientList(Context context){
		List<SMSThread.Recipient> recipientList = new LinkedList<SMSThread.Recipient>();
		String[] projection = new String[]{
				SMSThread.ID,
				SMSThread.ADDRESS
		};
		Cursor c = null;
		c = context.getContentResolver().query(SMSThread.ADDRESS_URI, projection, null, null, null);
		if (c != null){
			while (c.moveToNext()){
				long id = c.getLong(0);
				String address = c.getString(1);
				SMSThread.Recipient recipient = new SMSThread.Recipient(id, address);
				recipientList.add(recipient);
			}
		}
		if (c != null) c.close();
		c = null;
		
		return recipientList;
	}
	
	/**
	 * 获取对话的电话号码查询条件
	 * @return
	 */
	public static String getThreadPhoneIdSelection(String[] phoneIds){
		StringBuilder sb = new StringBuilder();
		sb.append(SMSThread.ID).append(" IN ").append(" ( ");
		for (int i = 0; i < phoneIds.length; i ++){
			if (i != 0){
				sb.append(",");
			}
			sb.append(phoneIds[i]);
		}
		sb.append(" )");
		return sb.toString();
	}
	
	/**
	 * 删除会话
	 * @param context
	 * @param threadId
	 */
	public static void deleteThread(Context context, long threadId){
		//String where = SMSThread.ID + " = " + threadId;
		Uri deleteUri = ContentUris.withAppendedId(SMSThread.CONTENT_URI, threadId);
		context.getContentResolver().delete(deleteUri, null, null);
	}
	
	

	/**
	 * 获取对比用短信列表
	 * 
	 * @param context
	 */
	public static List<CompareList> getRawSMSList(Context context) {
		List<CompareList> list = new ArrayList<CompareList>();
		String[] projcetion = new String[] { SMS.ID };
		String order = SMS.ID + " ASC";
		Cursor c = null;
		c = context.getContentResolver().query(SMS.CONTENT_URI, projcetion,
				null, null, order);
		if (c != null) {
			while (c.moveToNext()) {
				CompareList compareList = new CompareList();
				compareList.setLocalId(c.getInt(0));
				list.add(compareList);
			}
		}
		c.close();
		c = null;
		return list;

	}
	
	/**
	 * 新版获取短信上传列表
	 * @param context
	 * @param lastUploadTimestamp 短信生成时间，只会获取在些时候之后生成的短信
	 * @return 一个以时间顺序排序的短信列表
	 */
	public static List<CompareList> getRawSMSList(Context context, long lastUploadTimestamp){
		List<CompareList> list = new ArrayList<CompareList>();
		String[] projcetion = new String[] { SMS.ID , SMS.DATE};
		String order = SMS.DATE + " ASC";
		String where = SMS.DATE + " > " + lastUploadTimestamp;
		Cursor c = null;
		c = context.getContentResolver().query(SMS.CONTENT_URI, projcetion,
				where, null, order);
		if (c != null) {
			while (c.moveToNext()) {
				CompareList compareList = new CompareList();
				compareList.setLocalId(c.getInt(0));
				compareList.setTimestamp(c.getLong(1));
				list.add(compareList);
			}
		}
		if (c != null)
			c.close();
		c = null;
		return list;
	}
	
	/**
	 * 获取联系人最后一条短信
	 * 注意：这是一个耗时的操作，尽量不要在UI线程中执行
	 * @param context
	 * @param contactId
	 * @return
	 */
	public static String getLastSMS(Context context, long contactId){
		List<SMSThread> threadList = getThreads(context);
		filterSmsThread(threadList, contactId);
		String lastSMS = null;
		if (threadList.size() > 0){
			lastSMS = threadList.get(0).getSnippet();
		}
		threadList.clear();
		return lastSMS;
	}
	
	/**
	 * 通过联系人过滤短信<br>
	 * 注意：这是一个耗时的操作，尽量不要在UI线程中执行
	 * @param threadList
	 * @param contactId
	 */
	public static void filterSmsThread(List<SMSThread> threadList, long contactId){
		Iterator<SMSThread> iter = threadList.iterator();
		while(iter.hasNext()){
			SMSThread threads = iter.next();
			if (threads.getSmpPerson().getRawId() != contactId){
				iter.remove();
			}
		}
	}
	
	/**
	 * 根据电话号码，拿取短信会话ID
	 * @param context
	 * @param phoneNumber
	 * @return 会话ID，如果没有会话则为 -1
	 */
	public static long getThreadIdByPhoneNumber(Context context, String phoneNumber){
		long threadId = -1;
		String[] addressProjection = new String[]{SMSThread.ID};
		String where = String.format("%s = '%s'", SMSThread.ADDRESS, phoneNumber); 
		
		String[] threadProjection = new String[]{SMSThread.ID};
		String threadWhere = null;
		
		Cursor c = null;
		c = context.getContentResolver().query(SMSThread.ADDRESS_URI, addressProjection, where, null, null);
		if (c != null && !c.isAfterLast()){
			c.moveToFirst();
			long addressId = c.getLong(0);
			threadWhere = String.format("%s = '%s'", SMSThread.RECIPIENT_IDS, addressId);
			Cursor threadCursor = context.getContentResolver().query(SMSThread.CONTENT_URI, threadProjection, threadWhere, null, null);
			if (threadCursor != null && !threadCursor.isAfterLast()){
				threadCursor.moveToFirst();
				threadId = threadCursor.getLong(0);
			}
			if (threadCursor != null) threadCursor.close();
			threadCursor = null;
			
		}
		
		if (c != null) c.close();
		c = null;
		
		return threadId;
	}
}
