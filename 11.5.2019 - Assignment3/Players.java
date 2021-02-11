
public class Players {
	private int money;
	private String properties = "";
	public String getProperties() {
		return properties;
	}
	public void setProperties(String properties) {
		this.properties = properties;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public Players(int money) {
		this.money = money;
	}
}