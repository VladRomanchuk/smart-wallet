package com.eightwizards.smartwallet.history;

public class HistoryItem {
	private String date;
	private String total;
	private String sum;

	public HistoryItem(String date, String total, String sum) {
		super();
		this.date = date;
		this.total = total;
		this.sum = sum;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

}
