
public class Waiter {
	private String name;
	private float salary;
	private int tablesServed;
	private int totalOrder = 0;
	
	public int getTotalOrder() {
		return totalOrder;
	}
	public void setTotalOrder(int totalOrder) {
		this.totalOrder = totalOrder;
	}
	public int getTablesServed() {
		return tablesServed;
	}
	public void setTablesServed(int tablesServed) {
		this.tablesServed = tablesServed;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getSalary() {
		return salary;
	}
	public void setSalary(float salary) {
		this.salary = salary;
	}
}
