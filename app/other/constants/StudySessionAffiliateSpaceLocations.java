package other.constants;

public enum StudySessionAffiliateSpaceLocations {
	
	TOP_RECTANGLE(1),
	TOP_RIGHT_SQUARE(2),
	BOTTOM_RECTANGE(3);
	
	private int intLocation;
	private StudySessionAffiliateSpaceLocations(int intLocation) {
		this.intLocation = intLocation;
	}
	
	public int getIntLocation() {
		return this.intLocation;
	}
}
