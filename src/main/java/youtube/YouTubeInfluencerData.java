package youtube;

public class YouTubeInfluencerData {

	int rank;
	Long seq;
	String siteName;
	String siteId;
	String siteType;
	int follower;
	int listed;
	int priority;
	String picture;
	String bio;
	String status;
	String url;
	String siteCategory;
	String views;
	String log; // Log를 조건에따라 남기기위해 만든 VO

	public YouTubeInfluencerData() {}

	public YouTubeInfluencerData(int rank, Long seq, String siteName, String siteId, String siteType, int follower,
			int listed, int priority, String picture, String bio, String status, String url, String siteCategory,
			String views, String log) {
		super();
		this.rank = rank;
		this.seq = seq;
		this.siteName = siteName;
		this.siteId = siteId;
		this.siteType = siteType;
		this.follower = follower;
		this.listed = listed;
		this.priority = priority;
		this.picture = picture;
		this.bio = bio;
		this.status = status;
		this.url = url;
		this.siteCategory = siteCategory;
		this.views = views;
		this.log = log;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public int getFollower() {
		return follower;
	}

	public void setFollower(int follower) {
		this.follower = follower;
	}

	public int getListed() {
		return listed;
	}

	public void setListed(int listed) {
		this.listed = listed;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSiteCategory() {
		return siteCategory;
	}

	public void setSiteCategory(String siteCategory) {
		this.siteCategory = siteCategory;
	}

	public String getViews() {
		return views;
	}

	public void setViews(String views) {
		this.views = views;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	@Override
	public String toString() {
		return "YouTubeInfluencerData [rank=" + rank + ", seq=" + seq + ", siteName=" + siteName + ", siteId=" + siteId
				+ ", siteType=" + siteType + ", follower=" + follower + ", listed=" + listed + ", priority=" + priority
				+ ", picture=" + picture + ", bio=" + bio + ", status=" + status + ", url=" + url + ", siteCategory="
				+ siteCategory + ", views=" + views + ", log=" + log + "]";
	}
	
}