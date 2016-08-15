package a8wizard.com.myapplication.history;

public class HistoryItem {
	private String tanggal;
	private String total;
	private String jumlah;

	public HistoryItem(String tanggal, String total, String jumlah) {
		super();
		this.tanggal = tanggal;
		this.total = total;
		this.jumlah = jumlah;
	}

	public String getTanggal() {
		return tanggal;
	}

	public void setTanggal(String tanggal) {
		this.tanggal = tanggal;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getJumlah() {
		return jumlah;
	}

	public void setJumlah(String jumlah) {
		this.jumlah = jumlah;
	}

}
