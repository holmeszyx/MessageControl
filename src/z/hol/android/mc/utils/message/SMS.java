package z.hol.android.mc.utils.message;

import android.content.ContentUris;
import android.net.Uri;

public class SMS {
	public static final Uri CONTENT_URI = Uri.parse("content://sms");
	public static final Uri ALL_CONTENT_URI = Uri.parse("content://mms-sms/");
	public static final Uri THREADS_CONTENT_URI = Uri.withAppendedPath( ALL_CONTENT_URI, "conversations");
	
	public static final String ID = "_id";
	public static final String THREAD_ID = "thread_id";
	public static final String ADDRESS = "address";
	public static final String PERSON = "person";
	public static final String DATE = "date";
	public static final String PROTORCOL = "protocol";;
	public static final String READ = "read";
	public static final String BODY = "body";
	public static final String STATUS = "status";
	public static final String TYPE = "type";
	public static final String REPLY_PATH = "reply_path_present";
	public static final String SUBJECT = "subject";
	public static final String SERV_CENTER = "service_center";
	public static final String LOCKED = "locked";
	public static final String ERRER_CODE = "error_code";	//don't use this
	public static final String SEEN = "seen";
	
	public static final int SEND = 2;
	public static final int RECV = 1;
	
	public static final Uri createrUri(long threadId){
		return ContentUris.withAppendedId(THREADS_CONTENT_URI, threadId);
	}
}
