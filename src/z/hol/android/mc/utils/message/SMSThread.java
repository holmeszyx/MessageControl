package z.hol.android.mc.utils.message;

import java.util.List;

import z.hol.android.mc.utils.Utils;
import z.hol.android.mc.utils.contact.SimpleContactPerson;
import android.net.Uri;

public class SMSThread {
	

	//public static final Uri CONTENT_URI = Uri.parse("content://sms/conversations");
	public static final Uri CONTENT_URI = Uri.parse("content://mms-sms/conversations").buildUpon().appendQueryParameter("simple", "true").build();;
	public static final Uri ADDRESS_URI = Uri.parse("content://sms/threadID/*");
	
	public static final String ADDRESS = "address";
	
	public static final String THREAD_ID = "thread_id";
	public static final String SIMPLE_PERSON_NUM = "simple_person";
	
	public static final String ID = "_id";
	public static final String DATE = "date";
	public static final String MSG_COUNT = "message_count";
	//TEXT
	public static final String RECIPIENT_IDS = "recipient_ids";
	//TEXT
	public static final String SNIPPET = "snippet";
	public static final String SNIPPET_CS = "snippet_cs";
	public static final String READ = "read";
	public static final String TYPE = "type";
	public static final String ERROR = "error";
	public static final String HAS_ATTACHMENT = "has_attachment";
	
	
	
	private int id;
	private long date;
	private int messageCount;
	private String recipient;
	private String snippet;
	private int snippetCs;
	private int read;
	private int type;
	private int error;
	private int hasAttachment;
	private String number;
	
	private SimpleContactPerson smpPerson;
	
	public SMSThread(){
		this.error = 0;
		this.hasAttachment = 0;
		this.snippetCs = 0;
		this.setSmpPerson(new SimpleContactPerson());
	}

	
	//---set and get---start
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public int getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public int getSnippetCs() {
		return snippetCs;
	}

	public void setSnippetCs(int snippetCs) {
		this.snippetCs = snippetCs;
	}

	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public int getHasAttachment() {
		return hasAttachment;
	}

	public void setHasAttachment(int hasAttachment) {
		this.hasAttachment = hasAttachment;
	}

	public void setSmpPerson(SimpleContactPerson smpPerson) {
		this.smpPerson = smpPerson;
	}


	public SimpleContactPerson getSmpPerson() {
		return smpPerson;
	}


	public String getNumber() {
		return number;
	}


	public void setNumber(String number) {
		this.number = number;
	}

	
	
	//---set and get---end

	/**
	 * 短信的接收者类
	 * @author holmes
	 *
	 */
	public static class Recipient{
		public long id;
		public String address;
		
		public Recipient(){
			this(0, null);
		}
		
		public Recipient(long id, String address){
			this.id = id;
			this.address = address;
		}
		
		/**
		 * 得到接收地址列表的sql where条件
		 * @param recipientList
		 * @return 类似返回 IN (1,2,3)，如果list为空则返回null
		 */
		public static String getRecipientWhereIn(List<Recipient> recipientList){
			if (Utils.isListEmpty(recipientList)){
				return null;
			}
			StringBuilder sb = new StringBuilder();
			sb.append("IN (");
			for (int i = 0; i < recipientList.size(); i ++){
				if (i > 0){
					sb.append(",");
				}
				sb.append(recipientList.get(i).address);
			}
			sb.append(")");
			return sb.toString();
		}
	}
	
	
	
}


