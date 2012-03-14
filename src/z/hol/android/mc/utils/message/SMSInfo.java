package z.hol.android.mc.utils.message;


public class SMSInfo {
	/**
	 * {
	    "id": "str(24)",
	    "thread_id": "str(24)",
	    "address": "10655350",
	    "date": 1312439508377,
	    "read": 1,
	    "status": -1,
	    "type": 1,
	    "protocol": 0,
	    "body": "军歌嘹亮，个性首选，在红色八月，联通炫铃为您准备了军港之夜、打起手鼓唱起歌、十五的月亮，拨打10150按0试听下载"
	 */
	
	public static final int UNKOWN = 0;
	public static final int INSERT = 10;
	public static final int UPDATE = 20;
	public static final int DELETE = 30;
	
	private transient int control = UNKOWN;
	
	private int _id;
	private int threadId;
	private String address;
	private int person;
	private long date;
	private int protocol;
	private int read;
	private int status;
	private int type;
	private int replyPath;
	private String subject;
	private String body;
	private String servCenter;
	private int locked;
	private int errorCode;
	private int seen;
	private String remoteId;
	private String remoteThreadId;
	private boolean needShowTime;
	
	
	
	public SMSInfo() {
		this.read = 0;
		this.status = -1;
		this.locked = 0;
		this.errorCode = 0;
		this.seen = 0;
		this.protocol = -10;
		this.remoteId = null;
		this.setNeedShowTime(false);
	}
	//---get and set---start
	

	public String getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		_id = id;
	}
	public int getThreadId() {
		return threadId;
	}
	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getPerson() {
		return person;
	}
	public void setPerson(int person) {
		this.person = person;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public int getProtocol() {
		return protocol;
	}
	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}
	public int getRead() {
		return read;
	}
	public void setRead(int read) {
		this.read = read;
		if (read == 1){
			this.seen = 1;
		}
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getReplyPath() {
		return replyPath;
	}
	public void setReplyPath(int replyPath) {
		this.replyPath = replyPath;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getServCenter() {
		return servCenter;
	}
	public void setServCenter(String servCenter) {
		this.servCenter = servCenter;
	}
	public int getLocked() {
		return locked;
	}
	public void setLocked(int locked) {
		this.locked = locked;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public int getSeen() {
		return seen;
	}
	public void setSeen(int seen) {
		this.seen = seen;
	}
	

	public int getControl() {
		return control;
	}


	public void setControl(int control) {
		this.control = control;
	}
	
	public String getRemoteThreadId(){
		return remoteThreadId;
	}

	public void setRemoteThreadId(String threadId){
		this.remoteThreadId = threadId;
	}

	public void setNeedShowTime(boolean needShowTime) {
		this.needShowTime = needShowTime;
	}

	public boolean isNeedShowTime() {
		return needShowTime;
	}


	//---get and set---end
	@Override
	public String toString() {
		return "SMSInfo [_id=" + _id + ", address=" + address + ", body="
				+ body + ", date=" + date + ", error_code=" + errorCode
				+ ", locked=" + locked + ", person=" + person + ", protocol="
				+ protocol + ", read=" + read + ", reply_path=" + replyPath
				+ ", seen=" + seen + ", serv_center=" + servCenter
				+ ", status=" + status + ", subject=" + subject
				+ ", thread_id=" + threadId + ", type=" + type + "]";
	}
	
	
	
}
