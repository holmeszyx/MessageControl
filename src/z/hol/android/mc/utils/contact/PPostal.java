package z.hol.android.mc.utils.contact;

import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;

public class PPostal extends DetailItemAbs{
	public static String FLAG = "ADDs";
	
	public static String FULL_ADDRESS = StructuredPostal.FORMATTED_ADDRESS;
	public static String TYPE = StructuredPostal.TYPE;
	public static String STREET = StructuredPostal.STREET;
	public static String POBOX = StructuredPostal.POBOX;
	public static String NEIGHBORHOOD = StructuredPostal.NEIGHBORHOOD;
	public static String CITY = StructuredPostal.CITY;
	public static String REGION = StructuredPostal.REGION;
	public static String POSTCODE = StructuredPostal.POSTCODE;
	public static String COUNTRY = StructuredPostal.COUNTRY;
	
	private String fullPostalAddress;
	private int postalType;
	private String postalStreet;
	private String postalCity;
	private String postalState;
	private String postalZip;
	private String postalCountry;
	private String postalBox;
	private String neighborhood;
	
	
	
	
	public String getFullPostalAddress() {
		return fullPostalAddress;
	}




	public void setFullPostalAddress(String fullPostalAddress) {
		this.fullPostalAddress = fullPostalAddress;
	}




	public int getPostalType() {
		return postalType;
	}




	public void setPostalType(int postalType) {
		this.postalType = postalType;
	}




	public String getPostalStreet() {
		return postalStreet;
	}




	public void setPostalStreet(String postalStreet) {
		this.postalStreet = postalStreet;
	}




	public String getPostalCity() {
		return postalCity;
	}




	public void setPostalCity(String postalCity) {
		this.postalCity = postalCity;
	}




	public String getPostalState() {
		return postalState;
	}




	public void setPostalState(String postalState) {
		this.postalState = postalState;
	}




	public String getPostalZip() {
		return postalZip;
	}




	public void setPostalZip(String postalZip) {
		this.postalZip = postalZip;
	}




	public String getPostalCountry() {
		return postalCountry;
	}




	public void setPostalCountry(String postalCountry) {
		this.postalCountry = postalCountry;
	}




	public String getPostalBox() {
		return postalBox;
	}




	public void setPostalBox(String postalBox) {
		this.postalBox = postalBox;
	}




	public String getNeighborhood() {
		return neighborhood;
	}




	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}




	@Override
	public String toString() {
		return "PPostal [full_postal_address=" + fullPostalAddress
				+ ", postal_city=" + postalCity + ", postal_state="
				+ postalState + ", postal_street=" + postalStreet
				+ ", postal_type=" + postalType + ", postal_zip=" + postalZip
				+ "]";
	}
	
	
}
