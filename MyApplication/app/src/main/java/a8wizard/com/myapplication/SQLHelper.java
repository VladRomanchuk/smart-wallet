package a8wizard.com.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import a8wizard.com.myapplication.statistic.BudgetItem;
import a8wizard.com.myapplication.history.HistoryItem;
import a8wizard.com.myapplication.transactions.TransactionItem;

public class SQLHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME    = "smartWallet";
    public  static final String TBL_TRANSACTIONS = "tbl_transactions";
    public  static final String TBL_BUDGET       = "tbl_budget";

    private Context context;

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String tbl_budget = "CREATE TABLE IF not exists tbl_budget ("
                + " idBudget integer PRIMARY KEY AUTOINCREMENT,"
                + " startDate date NOT NULL," + " endDate date NOT NULL,"
                + " category text NOT NULL," + " amount text NOT NULL,"
                + " left text NOT NULL, "
                + " timeStartDate timestamp NOT NULL,"
                + " timeEndDate timestamp NOT NULL)";

        String tbl_transactions = "CREATE TABLE IF not exists tbl_transactions ("
                + " idTransactions integer PRIMARY KEY AUTOINCREMENT,"
                + " description text NOT NULL," + " price text NOT NULL,"
                + " clock text NOT NULL," + " data date NOT NULL,"
                + " time timestamp NOT NULL)";


        db.execSQL(tbl_budget);
        db.execSQL(tbl_transactions);

    }

    public BudgetItem getDetailBudget(int idBudget) {
        String query = "select * from tbl_budget where idBudget=" + idBudget;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        BudgetItem budget = null;
        if (cursor.moveToFirst()) {
            do {
                budget = new BudgetItem(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4),
                        cursor.getString(5),
                        Long.parseLong(cursor.getString(6)),
                        Long.parseLong(cursor.getString(7)));

            } while (cursor.moveToNext());
        }

        return budget;

    }

    public void updateBudgetByDate(String tgl, double amount) {
        ArrayList<BudgetItem> budgets = getAllBudget();

        long tglTime = Util.getTimeStamp(tgl, new SimpleDateFormat(
                "dd/MM/yyyy kk:mm:ss"));

        for (int i = 0; i < budgets.size(); i++) {
            if (Util.checkTransactionDateInBudget(tglTime, budgets.get(i))) {
                if (amount < 0) {
                    amount = Math.abs(amount);

                    updateBudgetDifference(budgets.get(i).getIdBudget(), amount);

                } else {
                    amount = Math.abs(amount);
                    updateBudgetSum(budgets.get(i).getIdBudget(), amount);

                }

                Toast.makeText(context, "BudgetItem has been updated",
                        Toast.LENGTH_SHORT).show();
                break;
            }

        }
    }

    public int getIdBudgetByDateTransaction(long tglTime) {
        ArrayList<BudgetItem> budgets = getAllBudget();

        for (int i = 0; i < budgets.size(); i++) {
            if (Util.checkTransactionDateInBudget(tglTime, budgets.get(i))) {
                return budgets.get(i).getIdBudget();
            }

        }

        return -1;
    }

    public void updateBudgetByDateHistory(String tgl) {

        ArrayList<TransactionItem> trans = getAllTransactionByTanggal(tgl);
		/*
		 * Toast.makeText(context, tgl + Integer.toString(trans.size()),
		 * Toast.LENGTH_SHORT) .show();
		 */
        for (int i = 0; i < trans.size(); i++) {
            updateBudgetByDate(trans.get(i).getTanggal() + " "
                            + trans.get(i).getJam(),
                    Double.parseDouble(trans.get(i).getHarga()));

        }
        if (trans.size() > 0)
            Toast.makeText(context, "BudgetItem has been updated", Toast.LENGTH_SHORT)
                    .show();

    }

    public BudgetItem getDetailLastBudget() {
        String query = "select * from tbl_budget order by idBudget desc limit 1";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        BudgetItem budget = null;
        if (cursor.moveToFirst()) {
            do {
                budget = new BudgetItem(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4),
                        cursor.getString(5),
                        Long.parseLong(cursor.getString(6)),
                        Long.parseLong(cursor.getString(7)));

            } while (cursor.moveToNext());
        }

        return budget;

    }

    public void addBudget(BudgetItem budget) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // values.put("idTransaksi", "");
        values.put("startDate", budget.getStartDate());
        values.put("endDate", budget.getEndDate());
        values.put("category", budget.getCategory());
        values.put("amount", budget.getAmount());
        values.put("left", budget.getLeft());
        values.put("timeStartDate", budget.getTimeStartDate());
        values.put("timeEndDate", budget.getTimeEndDate());
        db.insert(TBL_BUDGET, null, values);
        Toast.makeText(context, "BudgetItem has been set", Toast.LENGTH_SHORT)
                .show();
        if(Util.checkBudget(context))
            Util.updateWidget(context);

        db.close();

    }

    public ArrayList<BudgetItem> getAllBudget() {
        ArrayList<BudgetItem> budgets = new ArrayList<BudgetItem>();
        // 1. build the query
        // String query = "select * from tbl_friendlist";

        String query = "select * from tbl_budget order by idBudget asc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        BudgetItem budget = null;
        if (cursor.moveToFirst()) {
            do {
                budget = new BudgetItem(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4),
                        cursor.getString(5),
                        Long.parseLong(cursor.getString(6)),
                        Long.parseLong(cursor.getString(7)));
                budgets.add(budget);

            } while (cursor.moveToNext());
        }

        return budgets;
    }

    public ArrayList<BudgetItem> getAllWeeklyBudget() {
        ArrayList<BudgetItem> budgets = new ArrayList<BudgetItem>();
        // 1. build the query
        // String query = "select * from tbl_friendlist";

        String query = "select * from tbl_budget where category='Weekly' order by idBudget asc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        BudgetItem budget = null;
        if (cursor.moveToFirst()) {
            do {
                budget = new BudgetItem(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4),
                        cursor.getString(5),
                        Long.parseLong(cursor.getString(6)),
                        Long.parseLong(cursor.getString(7)));
                budgets.add(budget);

            } while (cursor.moveToNext());
        }

        return budgets;
    }

    public ArrayList<BudgetItem> getAllMonthlyBudget() {
        ArrayList<BudgetItem> budgets = new ArrayList<BudgetItem>();
        // 1. build the query
        // String query = "select * from tbl_friendlist";

        String query = "select * from tbl_budget where category='Monthly' order by idBudget asc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        BudgetItem budget = null;
        if (cursor.moveToFirst()) {
            do {
                budget = new BudgetItem(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4),
                        cursor.getString(5),
                        Long.parseLong(cursor.getString(6)),
                        Long.parseLong(cursor.getString(7)));
                budgets.add(budget);

            } while (cursor.moveToNext());
        }

        return budgets;
    }

    public void deleteBudget(int idBudget) {
        SQLiteDatabase db = this.getWritableDatabase();
        Toast.makeText(context, "Last budget has been deleted", Toast.LENGTH_SHORT)
                .show();
        db.execSQL("delete from tbl_budget where idBudget='" + idBudget + "'");
        if(Util.checkBudget(context))
            Util.updateWidget(context);

    }

    public void updateBudgetDifference(int idBudget, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();



        db.execSQL("update tbl_budget set left=left-" + amount
                + " where idBudget=" + idBudget + "");
        if(Util.checkBudget(context))
            Util.updateWidget(context);
    }



    public void updateBudgetSum(int idBudget, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update tbl_budget set left=left+" + amount
                + " where idBudget=" + idBudget + "");
        if(Util.checkBudget(context))
            Util.updateWidget(context);
    }

    public TransactionItem getDetailTransactions(int idTransaksi) {

        // 1. build the query
        // String query = "select * from tbl_friendlist";

        String query = "select * from tbl_transactions where idTransactions="
                + idTransaksi;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        TransactionItem trans = null;
        if (cursor.moveToFirst()) {
            do {
                trans = new TransactionItem(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4),
                        Long.parseLong(cursor.getString(5)));

            } while (cursor.moveToNext());
        }

        return trans;
    }

    public ArrayList<HistoryItem> getAllHistory() {
        ArrayList<HistoryItem> history = new ArrayList<HistoryItem>();

        String query = "select distinct data, sum(price) total, count(*) sum from tbl_transactions GROUP BY data order by time asc";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        HistoryItem hist = null;
        if (cursor.moveToFirst()) {
            do {
                hist = new HistoryItem(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2));
                history.add(hist);

            } while (cursor.moveToNext());
        }

        return history;
    }

    public ArrayList<HistoryItem> getAllMonthlyHistory() {
        ArrayList<HistoryItem> history = new ArrayList<HistoryItem>();
        // 1. build the query
        // String query = "select * from tbl_friendlist";

        //*** FYI ***
        //sum means sum
        String query = "select distinct substr(data,4) data, sum(price) total, count(*) sum from tbl_transactions GROUP BY data order by time asc";
        // String query =
        // "select distinct strftime('%d-%m-%Y', data / 1000, 'unixepoch') data, sum(price) total, count(*) sum from tbl_transaksi GROUP BY data order by data asc";
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        HistoryItem hist = null;
        if (cursor.moveToFirst()) {
            do {
                hist = new HistoryItem(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2));
                history.add(hist);

            } while (cursor.moveToNext());
        }

        return history;
    }

    public ArrayList<HistoryItem> getAllYearlyHistory() {
        ArrayList<HistoryItem> history = new ArrayList<HistoryItem>();
        // 1. build the query
        // String query = "select * from tbl_friendlist";


        String query = "select distinct substr(data,7,4) data, sum(price) total, count(*) sum from tbl_transactions GROUP BY data order by time asc";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        HistoryItem hist = null;
        if (cursor.moveToFirst()) {
            do {
                hist = new HistoryItem(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2));
                history.add(hist);

            } while (cursor.moveToNext());
        }

        return history;
    }

    public void addTransaction(TransactionItem transactionItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // values.put("idTransaksi", "");
        values.put("description", transactionItem.getDeskripsi());
        values.put("price", transactionItem.getHarga());
        values.put("clock", transactionItem.getJam());
        values.put("data", transactionItem.getTanggal());
        values.put("time", transactionItem.getTime());
        db.insert(TBL_TRANSACTIONS, null, values);
        Toast.makeText(context, "TransactionItem has been added",
                Toast.LENGTH_SHORT).show();

        db.close();
    }

    public ArrayList<TransactionItem> getAllTransactionByTanggal(String data) {
        ArrayList<TransactionItem> transaction = new ArrayList<TransactionItem>();

        // 1. build the query
        // String query = "select * from tbl_friendlist";

        String query = "select * from tbl_transactions where data='" + data
                + "' order by clock asc";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        TransactionItem trans = null;
        if (cursor.moveToFirst()) {
            do {
                trans = new TransactionItem(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4),
                        Long.parseLong(cursor.getString(5)));

                transaction.add(trans);
            } while (cursor.moveToNext());
        }

        return transaction;
    }

    public void deleteTransaction(int idTransaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from tbl_transactions where idTransactions=" + idTransaction);
        Toast.makeText(context, "TransactionItem has been deleted",
                Toast.LENGTH_SHORT).show();

    }

    public void deleteHistory(String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from tbl_transactions where data='" + data + "'");
        Toast.makeText(context, "HistoryItem has been deleted", Toast.LENGTH_SHORT)
                .show();
    }

    public void updateTransaction(TransactionItem transactionItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update tbl_transactions set clock='" + transactionItem.getJam()
                + "',data='" + transactionItem.getTanggal() + "',price='"
                + transactionItem.getHarga() + "',description='"
                + transactionItem.getDeskripsi() + "',time=" + transactionItem.getTime()
                + " where idTransactions='" + transactionItem.getIdTransaksi() + "'");
        Toast.makeText(context, "TransactionItem has been edited", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        this.onCreate(db);
    }

}
