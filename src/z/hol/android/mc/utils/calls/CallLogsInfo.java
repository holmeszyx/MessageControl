package z.hol.android.mc.utils.calls;

import z.hol.android.mc.utils.contact.SimpleContactPerson;

public class CallLogsInfo {
	public static final int UNKOWN = 0;
	public static final int INSERT = 10;
	public static final int UPDATE = 20;
	public static final int DELETE = 30;
	
	public static final String NUMBER = "number";
	public static final String DATE = "date";
	public static final String DURATION = "duration";
	public static final String TYPE = "type";
	public static final String NEW = "new";
	public static final String CACHED_NAME = "name";
	public static final String NUMBER_TYPE = "num_type";
	public static final String CACHED_NUMBER_LABERL = "num_label";
	
	private transient int control = UNKOWN;
	
	private int id;
	private String number;
	private long date;
	private long duration;
	private int type;
	private int newCall;
	private String name;
	private int numType;
	private String numLable;
	private String remoteId;
	
	private SimpleContactPerson person;
	
	public CallLogsInfo() {
		super();
		this.remoteId = null;
	}
	public String getRemoteId() {
		return remoteId;
	}
	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getNew() {
		return newCall;
	}
	public void setNew(int newCall) {
		this.newCall = newCall;
	}
	public String getName() {
		return name;
	}
	public void setName(String cachedName) {
		name = cachedName;
	}
	public int getNumType() {
		return numType;
	}
	public void setNumType(int numType) {
		this.numType = numType;
	}
	public String getNumLable() {
		return numLable;
	}
	public void setNumLable(String numLable) {
		this.numLable = numLable;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	public int getControl() {
		return control;
	}
	public void setControl(int control) {
		this.control = control;
	}

	public SimpleContactPerson getPerson() {
		return person;
	}
	
	public void setPerson(SimpleContactPerson person) {
		this.person = person;
	}
	
	@Override
	public String toString() {
		return "CallLogsInfo [cached_name=" + name + ", date=" + date
				+ ", duration=" + duration + ", id=" + id + ", new_call="
				+ newCall + ", num_lable=" + numLable + ", num_type="
				+ numType + ", number=" + number + ", type=" + type + "]";
	}
	
	
	
	
}
