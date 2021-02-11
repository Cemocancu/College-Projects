
public class Tables {
	private int id;
	private int capacity;
	private boolean status;
	private int totalOrderCount = 0;
	private String whoCreated;
	private String whoOperates;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public int getTotalOrderCount() {
		return totalOrderCount;
	}
	public void setTotalOrderCount(int totalOrderCount) {
		this.totalOrderCount = totalOrderCount;
	}
	public String getWhoCreated() {
		return whoCreated;
	}
	public void setWhoCreated(String whoCreated) {
		this.whoCreated = whoCreated;
	}
	public String getWhoOperates() {
		return whoOperates;
	}
	public void setWhoOperates(String whoOperates) {
		this.whoOperates = whoOperates;
	}
}
