package a8wizard.com.myapplication.transactions;

public class TransactionItem {
	private int idTransaksi;
	private String deskripsi;
	private String harga;
	private String jam;
	private String tanggal;
	private long time;

	public TransactionItem(int idTransaksi, String deskripsi, String harga,
						   String jam, String tanggal, long time) {
		super();
		this.idTransaksi = idTransaksi;
		this.deskripsi = deskripsi;
		this.harga = harga;
		this.jam = jam;
		this.tanggal = tanggal;
		this.time = time;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getIdTransaksi() {
		return idTransaksi;
	}

	public void setIdTransaksi(int idTransaksi) {
		this.idTransaksi = idTransaksi;
	}

	public String getDeskripsi() {
		return deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

	public String getHarga() {
		return harga;
	}

	public void setHarga(String harga) {
		this.harga = harga;
	}

	public String getJam() {
		return jam;
	}

	public void setJam(String jam) {
		this.jam = jam;
	}

	public String getTanggal() {
		return tanggal;
	}

	public void setTanggal(String tanggal) {
		this.tanggal = tanggal;
	}

}
