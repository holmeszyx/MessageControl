package z.hol.android.mc.utils.contact;

import java.util.ArrayList;
import java.util.List;

public class ContactPerson {
	public static final int UNKOWN = 0;
	public static final int INSERT = 10;
	public static final int UPDATE = 20;
	public static final int DELETE = 30;
	
	private PName mPName;
	private List<PNumber> mPNumber;
	private List<PEmail> mPEmail;
	private List<PPostal> mPPostal;
	private List<PIm> mPIm;
	private List<POrganization> mPOrg;
	private List<PWebsite> mPWeb;
	private transient boolean hasPhoto;
	private transient String remoteId;
	private transient PNickName mPNickname;	
	private transient PNote mPNote;
	private transient PGroup mPGroup;
	private transient PPhoto mPPhoto;
	private transient int version;
	private transient int privacyGroup;
//	private int id;
	private String note;
	private String nickName;
	private String photoStr;
	private transient String display_name;
	private transient int rawId;
	private transient boolean isProfile;
	private transient int control = UNKOWN;
	
	public ContactPerson() {
		this.mPName = new PName();
		this.mPNickname = new PNickName();
		this.mPNumber = new ArrayList<PNumber>();
		this.mPEmail = new ArrayList<PEmail>();
		this.mPPhoto = new PPhoto();
		this.mPPostal = new ArrayList<PPostal>();
		this.mPIm = new ArrayList<PIm>();
		this.mPOrg = new ArrayList<POrganization>();
		this.mPWeb = new ArrayList<PWebsite>();	
		this.mPGroup = new PGroup();
		this.mPNote = new PNote();
		this.hasPhoto = false;
		this.remoteId = null;
		this.rawId = -1;
		this.version = -1;
		this.privacyGroup = 1;
		this.setProfile(false);
		this.photoStr = null;
//		this.id = -1;
		
	}
	

	
	/**
	 * 取得用于显示的姓名
	 * @return 显示名
	 */
	public String getDisplayName(){
		String dn = "";
		if (mPName.getDisaplyName() != null){
			dn = mPName.getDisaplyName();
		}
		else if (mPNumber.size() != 0){
			dn = mPNumber.get(0).getNumber();
		}
		else if(mPEmail.size() != 0){
			dn = mPEmail.get(0).getEmail();
		}			
		return dn;
	}


	public boolean hasPhoto(){
		return this.hasPhoto;
	}
	public void setHasPhoto(boolean has){
		this.hasPhoto = has;
	}
	public void addNumber(PNumber num){
		this.mPNumber.add(num);
	}
	public void addEmail(PEmail email){
		this.mPEmail.add(email);
	}
	public void addIM(PIm im){
		this.mPIm.add(im);
	}
	public void addWebSite(PWebsite web){
		this.mPWeb.add(web);
	}
	public void addPostal(PPostal post){
		this.mPPostal.add(post);
	}
	public void addOrg(POrganization org){
		this.mPOrg.add(org);
	}
	
	//批量添加
	public void addNumbers(List<PNumber> nums){
		this.mPNumber.addAll(nums);
	}
	
	public void addEmails(List<PEmail> emails){
		this.mPEmail.addAll(emails);
	}
	
	public void addIMs(List<PIm> ims){
		this.mPIm.addAll(ims);
	}
	public void  addWebSites(List<PWebsite> webs){
		this.mPWeb.addAll(webs);
	}
	public void addPostals(List<PPostal> postals){
		this.mPPostal.addAll(postals);
	}
	public void addOrgs(List<POrganization> orgs){
		this.mPOrg.addAll(orgs);
	}
	
	
	
	//---get and set---start
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}
	public PName getPName() {
		return mPName;
	}
	public void setPName(PName mPName) {
		this.mPName = mPName;
	}
	public PNickName getPNickname() {
		return mPNickname;
	}
	public void setPNickname(PNickName mPNickname) {
		this.mPNickname = mPNickname;
	}
	public List<PNumber> getPNumber() {
		return mPNumber;
	}
	public void setPNumber(List<PNumber> mPNumber) {
		this.mPNumber = mPNumber;
	}
	public List<PEmail> getPEmail() {
		return mPEmail;
	}
	public void setPEmail(List<PEmail> mPEmail) {
		this.mPEmail = mPEmail;
	}
	public PPhoto getPPhoto() {
		return mPPhoto;
	}
	public void setPPhoto(PPhoto mPPhoto) {
		this.hasPhoto = true;
		this.mPPhoto = mPPhoto;
	}
	public List<PPostal> getPPostal() {
		return mPPostal;
	}
	public void setPPostal(List<PPostal> mPPostal) {
		this.mPPostal = mPPostal;
	}
	public List<PIm> getPIm() {
		return mPIm;
	}
	public void setPIm(List<PIm> mPIm) {
		this.mPIm = mPIm;
	}
	public List<POrganization> getPOrg() {
		return mPOrg;
	}
	public void setPOrg(List<POrganization> mPOrg) {
		this.mPOrg = mPOrg;
	}
	public List<PWebsite> getPWeb() {
		return mPWeb;
	}
	public void setPWeb(List<PWebsite> mPWeb) {
		this.mPWeb = mPWeb;
	}
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}
	public String getDisplay_name() {
		return display_name;
	}

	public void setRawId(int raw_id) {
		this.rawId = raw_id;
	}

	public int getRawId() {
		return rawId;
	}

	public void setPNote(PNote mPNote) {
		this.mPNote = mPNote;
		this.note = mPNote.getNote();
	}

	public PNote getPNote() {
		return mPNote;
	}
	
	public PGroup getPGroup() {
		return mPGroup;
	}



	public void setPGroup(PGroup pGroup) {
		this.mPGroup = pGroup;
	}



	public int getPrivacyGroup() {
		return privacyGroup;
	}



	public void setPrivacyGroup(int privacyGroup) {
		this.privacyGroup = privacyGroup;
	}



	public String getNote() {
		return note;
	}



	public void setNote(String note) {
		this.note = note;
		this.mPNote.setNote(note);
	}



	public String getNickName() {
		return nickName;
	}



	public void setNickName(String nickName) {
		this.nickName = nickName;
		this.mPNickname.setNickName(nickName);
	}

	


	public String getPhotoStr() {
		return photoStr;
	}



	public void setPhotoStr(String photoStr) {
		this.photoStr = photoStr;
	}



	public void setProfile(boolean isProfile) {
		this.isProfile = isProfile;
	}
	
	public boolean hasPhotoStr(){
		boolean has = false;
		if (photoStr != null && !photoStr.equals("")){
			has = true;
		}
		return has;
	}



	public boolean isProfile() {
		return isProfile;
	}
	
	public void setControl(int control){
		this.control = control;
	}
	
	public int getControl(){
		return this.control;
	}



	@Override
	public String toString() {
		return "ContactPerson [display_name=" + display_name + ", hasPhoto="
				+ hasPhoto + ", mPEmail=" + mPEmail + ", mPIm=" + mPIm
				+ ", mPName=" + mPName + ", mPNickname=" + mPNickname
				+ ", mPNote=" + mPNote + ", mPNumber=" + mPNumber + ", mPOrg="
				+ mPOrg + ", mPPhoto=" + mPPhoto + ", mPPostal=" + mPPostal
				+ ", mPWeb=" + mPWeb + ", rawId=" + rawId + ", remoteId="
				+ remoteId + ", version=" + version + "]";
	}








	
	
	
	
}
