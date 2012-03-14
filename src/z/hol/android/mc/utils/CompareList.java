package z.hol.android.mc.utils;


/**
 * 用于对比同步数据，以确定各个同步数据的操作
 * @author holmes
 *
 */
public class CompareList {
	public static final String NULL_REMOTE_ID = null;
	
	private int localId;
	private String remoteId;
	private int version;
	private long timestamp;
	
	
	public CompareList() {
		super();
		// TODO Auto-generated constructor stub
		this.localId = -1;
		this.remoteId = NULL_REMOTE_ID;
		this.version = -1;
	}


	public int getLocalId() {
		return localId;
	}


	public void setLocalId(int localId) {
		this.localId = localId;
	}


	public String getRemoteId() {
		return remoteId;
	}


	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}


	public int getVersion() {
		return version;
	}


	public void setVersion(int version) {
		this.version = version;
	}


	public long getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}


	@Override
	public String toString() {
		return "CompareList [localId=" + localId + ", remoteId=" + remoteId
				+ ", version=" + version + "]";
	}
	
	
	
	
}
