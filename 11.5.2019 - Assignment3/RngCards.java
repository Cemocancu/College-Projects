
public class RngCards extends Squares{
	private String card;
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public RngCards(String name, String card) {
		super(name);
		this.card = card;
	}
}