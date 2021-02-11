
public class Orders extends Tables {
	private String order = "";
	private float totalCost;
	private String itemCount = "";




	public float getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(float totalCost) {
		this.totalCost = totalCost;
	}

	public String getOrder() {
		return order;
	}

	public String getItemCount() {
		return itemCount;
	}

	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
