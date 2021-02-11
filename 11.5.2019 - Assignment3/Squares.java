
public class Squares implements Comparable<Object>{
	protected String name;
	protected int cost;
	protected boolean status;
	protected String card;
	protected int id;
	protected String whose = "";
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public Squares(String name) {
		this.name = name;
	}
	public Squares(String name, int id) {
		this.name = name;
		this.id = id;
	}
	public Squares(String name, int cost, boolean status, int id, String whose) {
		this.name = name;
		this.cost = cost;
		this.status = status;
		this.id = id;
		this.whose = whose;
	}
	public String getWhose() {
		return whose;
	}
	public void setWhose(String whose) {
		this.whose = whose;
	}
	@Override
	public int compareTo(Object o) {
		return Integer.compare(this.id, ((Squares) o ).getId());
	}
}