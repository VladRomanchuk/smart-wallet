package a8wizard.com.myapplication.history;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import a8wizard.com.myapplication.R;
import a8wizard.com.myapplication.SQLHelper;
import a8wizard.com.myapplication.Util;
import a8wizard.com.myapplication.transactions.TransactionAdapter;
import a8wizard.com.myapplication.transactions.TransactionItem;

public class HistoryAdapter extends ArrayAdapter<HistoryItem> implements Filterable {

    private Context context;
    public static String css = null;
    private ArrayList<HistoryItem> itemsArrayList;
    private ArrayList<HistoryItem> oriItemsArrayList;

    private Filter planetFilter;

    public HistoryAdapter(Context context, ArrayList<HistoryItem> itemsArrayList) {

        super(context, R.layout.rowhistory_layout, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
        this.oriItemsArrayList = itemsArrayList;
    }

    public void resetData() {
        this.itemsArrayList = this.oriItemsArrayList;
    }

    public int getCount() {
        return itemsArrayList.size();
    }

    public HistoryItem getItem(int position) {
        return itemsArrayList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.rowhistory_layout, parent,
                false);
        RelativeLayout selector = (RelativeLayout) rowView
                .findViewById(R.id.relativeLayout1);

        TextView tanggal = (TextView) rowView.findViewById(R.id.textView1);
        TextView total = (TextView) rowView.findViewById(R.id.textView2);
        TextView jumlah = (TextView) rowView.findViewById(R.id.textView3);

        tanggal.setText((itemsArrayList.get(position).getTanggal()));
        total.setText(Util.formatUSD(itemsArrayList.get(position).getTotal()));
        jumlah.setText(itemsArrayList.get(position).getJumlah());

        selector.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                ArrayList<TransactionItem> listTransaksi = new ArrayList<TransactionItem>();

                SQLHelper helper = new SQLHelper(context);

                listTransaksi = helper.getAllTransactionByTanggal(itemsArrayList
                        .get(position).getTanggal());
                HistoryFragment.transactionAdapter = new TransactionAdapter(
                        context, listTransaksi);

                HistoryFragment.listItem
                        .setAdapter(HistoryFragment.transactionAdapter);
                HistoryFragment.backButton.setVisibility(View.VISIBLE);
                HistoryFragment.adapterStatus = 2;

                HistoryFragment.timeTitle.setText("Jam");
                HistoryFragment.descriptionTitle.setText("Deskripsi");
                HistoryFragment.billTitle.setText("Harga");
            }
        });

        selector.setOnLongClickListener(new OnLongClickListener() {

            public boolean onLongClick(View arg0) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Deletion");

                builder.setIcon(android.R.drawable.ic_delete);
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub

                                SQLHelper helper = new SQLHelper(
                                        context);
                                SQLiteDatabase db = helper
                                        .getReadableDatabase();
                                helper.onCreate(db);

                                helper.updateBudgetByDateHistory(itemsArrayList.get(
                                        position).getTanggal());
                                helper.deleteHistory(itemsArrayList.get(
                                        position).getTanggal());


                                // /
                                ArrayList<HistoryItem> listHistory = new ArrayList<HistoryItem>();
                                HistoryAdapter adapterHistory;

                                listHistory = helper.getAllHistory();
                                adapterHistory = new HistoryAdapter(context,
                                        listHistory);

                                HistoryFragment.listItem.setAdapter(adapterHistory);
                                HistoryFragment.adapterStatus = 1;

                            }

                        });

                builder.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub

                            }

                        });

                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }
        });

        return rowView;
    }


    @Override
    public Filter getFilter() {
        if (planetFilter == null)
            planetFilter = new PlanetFilter();

        return planetFilter;
    }

    private class PlanetFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = oriItemsArrayList;
                results.count = oriItemsArrayList.size();
            } else {
                // We perform filtering operation
                List<HistoryItem> nPlanetList = new ArrayList<HistoryItem>();

                for (int i = 0; i < itemsArrayList.size(); i++) {
                    if (itemsArrayList.get(i).getTanggal()
                            .toUpperCase()
                            .startsWith(constraint.toString().toUpperCase())
                            || itemsArrayList
                            .get(i)
                            .getTotal()
                            .toUpperCase()
                            .startsWith(
                                    constraint.toString().toUpperCase())
                            || itemsArrayList
                            .get(i)
                            .getJumlah()
                            .toUpperCase()
                            .startsWith(
                                    constraint.toString().toUpperCase())) {
                        nPlanetList.add(itemsArrayList.get(i));

                    }
                }

                results.values = nPlanetList;
                results.count = nPlanetList.size();

            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            // Now we have to inform the historyAdapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {

                itemsArrayList = (ArrayList<HistoryItem>) results.values;
                notifyDataSetChanged();
            }

        }

    }


}
