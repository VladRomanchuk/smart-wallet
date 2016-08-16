package a8wizard.com.myapplication.transactions;

public class TransactionItem {
	private int idTransaction;
	private String description;
	private String price;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	private String time;
	private String date;
	private long timeL;

	public TransactionItem(int idTransaction, String description, String price,
					 String time, String date, long timeL) {
		super();
		this.idTransaction = idTransaction;
		this.description = description;
		this.price = price;
		this.time = time;
		this.date = date;
		this.timeL = timeL;
	}

	public long getTimeL() {
		return timeL;
	}

	public void setTimeL(long timeL) {
		this.timeL = timeL;
	}

	public int getIdTransaction() {
		return idTransaction;
	}

	public void setIdTransaction(int idTransaction) {
		this.idTransaction = idTransaction;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
