package z.hol.android.mc.utils.calls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import z.hol.android.mc.utils.CompareList;
import z.hol.android.mc.utils.contact.ContactUtil;
import z.hol.android.mc.utils.contact.SimpleContactPerson;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

public class CallLogsUtil {
	public static final Uri URI = CallLog.Calls.CONTENT_URI;
	public static final String _ID = CallLog.Calls._ID;
	public static final String NUMBER = CallLog.Calls.NUMBER;
	public static final String NAME = CallLog.Calls.CACHED_NAME;
	public static final String LABEL = CallLog.Calls.CACHED_NUMBER_LABEL;
	public static final String DATE = CallLog.Calls.DATE;
	public static final String DURATION = CallLog.Calls.DURATION;
	public static final String TYPE = CallLog.Calls.TYPE;
	public static final String NEW = CallLog.Calls.NEW;
	public static final String NUM_TYPE = CallLog.Calls.CACHED_NUMBER_TYPE;
	
	public static final String TYPE_LABEL_DEFAULT = "default";

	public static final int TYPE_INCOMEING = CallLog.Calls.INCOMING_TYPE;
	public static final int TYPE_OUTGOING = CallLog.Calls.OUTGOING_TYPE;
	public static final int TYPE_MISSED = CallLog.Calls.MISSED_TYPE;

	private Context mContext = null;

	public Context getmContext() {
		return mContext;
	}

	public CallLogsUtil(Context mContext) {
		super();
		this.mContext = mContext;
	}

	/**
	 * 查找通话记录
	 * 
	 * @param ctxt
	 * @param type
	 *            all 0, in 1, out 2, miss 3
	 * @return
	 */
	public static List<CallLogsInfo> getCallLogs(Context ctxt, int type) {
		Cursor c = null;
		List<CallLogsInfo> list = new ArrayList<CallLogsInfo>();
		String[] projection = new String[] { _ID, NUMBER, DATE, DURATION, TYPE,
				NEW, NAME, NUM_TYPE, LABEL };

		if (!(type >= 0) && (type <= 3)) {
			throw new IllegalArgumentException("The type must between 0 and 3");
		}
		String selection = null;
		if (!(type == 0)) {
			selection = TYPE + "=" + String.valueOf(type);
		}

		c = ctxt.getContentResolver().query(URI, projection, selection, null,
				null);

		while (c.moveToNext()) {
			CallLogsInfo info = new CallLogsInfo();
			info.setId(c.getInt(0));
			info.setNumber(c.getString(1));
			info.setDate(c.getLong(2));
			info.setDuration(c.getLong(3));
			info.setType(c.getInt(4));
			info.setNew(c.getInt(5));
			info.setName(c.getString(6));
			info.setNumType(c.getInt(7));
			info.setNumLable(c.getString(8));

			list.add(info);
		}

		return list;
	}
	
	/**
	 * 得到新未知道的未接电话 
	 * @param context
	 * @return
	 */
	public static List<CallLogsInfo> getNewMissCallLogs(Context context){
		Cursor c = null;
		List<CallLogsInfo> list = new ArrayList<CallLogsInfo>();
		String[] projection = new String[] { _ID, NUMBER, DATE, DURATION, TYPE,
				NEW, NAME, NUM_TYPE, LABEL };
		int type = 3;
		String selection = String.format("%s = %d AND %s = %d", TYPE, 3, NEW, 1);
		

		c = context.getContentResolver().query(URI, projection, selection, null,
				null);

		while (c.moveToNext()) {
			CallLogsInfo info = new CallLogsInfo();
			info.setId(c.getInt(0));
			info.setNumber(c.getString(1));
			info.setDate(c.getLong(2));
			info.setDuration(c.getLong(3));
			info.setType(c.getInt(4));
			info.setNew(c.getInt(5));
			info.setName(c.getString(6));
			info.setNumType(c.getInt(7));
			info.setNumLable(c.getString(8));

			list.add(info);
		}

		return list;
	}
	
	
	
	/**
	 * get call logs which  number is in contact
	 * @param context
	 * @param type
	 * @return
	 */
	public static List<CallLogsInfo> getCallLogsInContact(Context context, int type) {
		Cursor c = null;
		List<CallLogsInfo> list = new ArrayList<CallLogsInfo>();
		String[] projection = new String[] { _ID, NUMBER, DATE, DURATION, TYPE,
				NEW, NAME, NUM_TYPE, LABEL };

		if (!(type >= 0) && (type <= 3)) {
			throw new IllegalArgumentException("The type must between 0 and 3");
		}
		String selection = null;
		if (!(type == 0)) {
			selection = TYPE + "=" + String.valueOf(type) + " AND " + NUM_TYPE + " <> 0";
		}else{
			selection = NUM_TYPE + " <> 0";
		}

		c = context.getContentResolver().query(URI, projection, selection, null,
				CallLog.Calls.DEFAULT_SORT_ORDER);

		while (c.moveToNext()) {
			/*
			if (!c.isNull(6)){
				if (ContactUtil.isInContactByNumber(ctxt, c.getString(1))){
					CallLogsInfo info = new CallLogsInfo();
					info.setId(c.getInt(0));
					info.setNumber(c.getString(1));
					info.setDate(c.getLong(2));
					info.setDuration(c.getLong(3));
					info.setType(c.getInt(4));
					info.setNew(c.getInt(5));
					info.setName(c.getString(6));
					info.setNumType(c.getInt(7));
					info.setNumLable(c.getString(8));
					list.add(info);
				}
			}
			*/
			CallLogsInfo info = new CallLogsInfo();
			info.setId(c.getInt(0));
			String number = c.getString(1);
			info.setNumber(number);
			info.setDate(c.getLong(2));
			info.setDuration(c.getLong(3));
			info.setType(c.getInt(4));
			info.setNew(c.getInt(5));
			if (!c.isNull(6)) info.setName(c.getString(6));
			info.setNumType(c.getInt(7));
			info.setNumLable(c.getString(8));
			SimpleContactPerson person = ContactUtil.getSimplePersonByNumber(context, number);
			info.setPerson(person);
			list.add(info);

		}

		return list;
	}
	
	public static List<CallLogsInfo> getCallLogsByContactId(Context context, int type, long contactId){
		
		SimpleContactPerson person = ContactUtil.getContactDisplay(context, contactId);
		Cursor c = null;
		List<CallLogsInfo> list = new ArrayList<CallLogsInfo>();
		String[] projection = new String[] { _ID, NUMBER, DATE, DURATION, TYPE,
				NEW, NAME, NUM_TYPE, LABEL };

		if (!(type >= 0) && (type <= 3)) {
			throw new IllegalArgumentException("The type must between 0 and 3");
		}
		String selection = null;
		if (!(type == 0)) {
			selection = TYPE + "=" + String.valueOf(type) + " AND " + NUM_TYPE + " <> 0" + " AND " + NAME + " = '" + person.getDisplayName() + "'";
		}else{
			selection = NUM_TYPE + " <> 0" + " AND " + NAME + " = '" + person.getDisplayName() + "'";
		}

		c = context.getContentResolver().query(URI, projection, selection, null,
				CallLog.Calls.DEFAULT_SORT_ORDER);

		while (c.moveToNext()) {
			
			CallLogsInfo info = new CallLogsInfo();
			info.setId(c.getInt(0));
			info.setNumber(c.getString(1));
			info.setDate(c.getLong(2));
			info.setDuration(c.getLong(3));
			info.setType(c.getInt(4));
			info.setNew(c.getInt(5));
			if (!c.isNull(6)) info.setName(c.getString(6));
			info.setNumType(c.getInt(7));
			info.setNumLable(c.getString(8));
			info.setPerson(person);
			list.add(info);

		}

		return list;
	}
	
	/**
	 * 获得一条通话记录
	 * @param context
	 * @param id
	 * @return
	 */
	public static CallLogsInfo getOneLogs(Context context, long id){
		CallLogsInfo logs = new CallLogsInfo();
		String[] projection = new String[] { _ID, NUMBER, DATE, DURATION, TYPE,
				NEW, NAME, NUM_TYPE, LABEL };
		String selection = _ID + "=" + id;
		Cursor c = null;
		c = context.getContentResolver().query(URI, projection, selection, null, null);
		if ((c != null) && (c.getCount() == 1)){
			c.moveToFirst();
			logs.setId(c.getInt(0));
			logs.setNumber(c.getString(1));
			logs.setDate(c.getLong(2));
			logs.setDuration(c.getLong(3));
			logs.setType(c.getInt(4));
			logs.setNew(c.getInt(5));
			logs.setName(c.getString(6));
			logs.setNumType(c.getInt(7));
			logs.setNumLable(c.getString(8));
		}
		if (c != null){
			c.close();
		}
		return logs;
		
	}

	/**
	 * 获得最新的通话记录ID
	 * 
	 * @param context
	 * @return 最新的ID，出错则返回 -1
	 */
	public static int findLastDataId(Context context) {
		int dataId = -1;
		String[] projection = new String[] { _ID };
		String order = _ID + " DESC";
		Cursor c = null;
		c = context.getContentResolver().query(URI, projection, null, null,
				order);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			dataId = c.getInt(0);
		}
		c.close();
		c = null;
		return dataId;
	}

	/**
	 * 插入多个通话记录
	 * 
	 * @param context
	 * @param list
	 */
	public static void insertCallLogs(Context context, List<CallLogsInfo> list) {
		Iterator<CallLogsInfo> iter = list.iterator();
		while (iter.hasNext()) {
			CallLogsInfo logsInfo = iter.next();
			insertCallLogs(context, logsInfo);
		}

	}

	/**
	 * 插入通话记录
	 * 
	 * @param context
	 * @param logsInfo
	 */
	public static void insertCallLogs(Context context, CallLogsInfo logsInfo) {
		try {
			ContentValues values = new ContentValues();
			values.put(NUMBER, logsInfo.getNumber());
			values.put(DATE, logsInfo.getDate());
			values.put(DURATION, logsInfo.getDuration());
			values.put(NAME, logsInfo.getName());
			values.put(TYPE, logsInfo.getType());
			values.put(NEW, logsInfo.getNew());
			values.put(NUM_TYPE, logsInfo.getNumType());
			values.put(LABEL, logsInfo.getNumLable());
			context.getContentResolver().insert(URI, values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 删除一条指定的通话记录
	 * 
	 * @param context
	 * @param id
	 */
	public static void deleteCallLogs(Context context, int id) {
		String where = _ID + "=" + id;
		context.getContentResolver().delete(URI, where, null);
	}
	
	/**
	 * 批量删除通话记录
	 * @param context
	 * @param ids
	 */
	public static void deleteCallLogs(Context context, long[] ids){
		if (ids.length <= 0){
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ids.length; i ++){
			if (i != 0){
				sb.append(" OR ");
			}
			sb.append(_ID);
			sb.append(" = ");
			sb.append(ids[i]);
		}
		String where = sb.toString();
		context.getContentResolver().delete(URI, where, null);
		
	}

	/**
	 * 删除全部记录
	 * 
	 * @param context
	 */
	public static void deleteAllCallLogs(Context context) {
		context.getContentResolver().delete(URI, null, null);
	}
	
	/**
	 * 通过联系人id 删除通话记录
	 * @param context
	 * @param contactId
	 */
	public static void deleteCallLogsByContactId(Context context, long contactId){
		SimpleContactPerson person = ContactUtil.getContactDisplay(context, contactId);
		String where = String.format("%s = '%s'", NAME, person.getDisplayName());
		context.getContentResolver().delete(URI, where, null);
	}

	/**
	 * 获取对比用通话记录ID
	 * 
	 * @param context
	 */
	public static List<CompareList> getRawLogsList(Context context) {
		List<CompareList> list = new ArrayList<CompareList>();
		String[] projcetion = new String[] { _ID };
		String order = _ID + " ASC";
		Cursor c = null;
		c = context.getContentResolver().query(URI, projcetion, null, null,
				order);
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
	 * 新版<br>
	 * 获取对比用通话记录ID
	 * 
	 * @param context
	 * @param lastUploadTimestamp
	 */
	public static List<CompareList> getRawLogsList(Context context, long lastUploadTimestamp) {
		List<CompareList> list = new ArrayList<CompareList>();
		String[] projcetion = new String[] { _ID, DATE };
		String order = DATE + " ASC";
		String where = DATE + " > " + lastUploadTimestamp;
		Cursor c = null;
		c = context.getContentResolver().query(URI, projcetion, where, null,
				order);
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
		return list;
	}
	
	public static String getTypeLabel(int type, String label){
		String disLabel = label == null ? TYPE_LABEL_DEFAULT : label;
		switch(type){
		case TYPE_INCOMEING:
			disLabel = "Incomeing";
			break;
		case TYPE_MISSED:
			disLabel = "Misseed";
			break;
		case TYPE_OUTGOING:
			disLabel = "Outgoing";
			break;
		}
		return disLabel;
		
	}

}
