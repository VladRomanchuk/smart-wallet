package a8wizard.com.myapplication.transactions;

public class TransactionItem {
	private int idTransaction;
	private String description;
	private String price;
	private String time;
	private String date;
	private long dateCurrent;

	public TransactionItem(int idTransaction, String description, String price,
					 String time, String date, long dateCurrent) {
		super();
		this.idTransaction = idTransaction;
		this.description = description;
		this.price = price;
		this.time = time;
		this.date = date;
		this.dateCurrent = dateCurrent;
	}


	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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
