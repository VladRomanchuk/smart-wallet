package com.eightwizards.smartwallet.history;

import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.eightwizards.smartwallet.R;
import com.eightwizards.smartwallet.SQLHelper;
import com.eightwizards.smartwallet.Util;
import com.eightwizards.smartwallet.transactions.TransactionAdapter;
import com.eightwizards.smartwallet.transactions.TransactionItem;

public class HistoryAdapter extends ArrayAdapter<HistoryItem> implements Filterable {

    private Context context;
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
    public View getView(final int position, final View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.rowhistory_layout, parent, false);
        final RelativeLayout selector = (RelativeLayout) rowView.findViewById(R.id.row_layout);

        itemColorize(position, rowView);

        TextView tanggal = (TextView) rowView.findViewById(R.id.textView1);
        TextView total = (TextView) rowView.findViewById(R.id.textView2);
        TextView jumlah = (TextView) rowView.findViewById(R.id.textView3);


        tanggal.setText((itemsArrayList.get(position).getDate()));
        total.setText(itemsArrayList.get(position).getSum());
        jumlah.setText(Util.formatUSD(itemsArrayList.get(position).getTotal()));

        selector.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ArrayList<TransactionItem> listTransaction = new ArrayList<TransactionItem>();

                SQLHelper helper = new SQLHelper(context);
                HistoryFragment.backButton.setVisibility(View.VISIBLE);
                listTransaction = helper.getAllTransactionByTanggal(itemsArrayList.get(position).getDate());
                Animation itemClick = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                HistoryFragment.listItem.setAnimation(itemClick);
                HistoryFragment.transactionAdapter = new TransactionAdapter(context, listTransaction);



                HistoryFragment.listItem.setAdapter(HistoryFragment.transactionAdapter);
                HistoryFragment.adapterStatus = 2;

                HistoryFragment.dateTitle.setText(R.string.time_history_row);
                HistoryFragment.transactionTitle.setText(R.string.description_history_row);
                HistoryFragment.totalTitle.setText(R.string.bill_history_row);


            }
        });

        selector.setOnLongClickListener(new OnLongClickListener() {

            public boolean onLongClick(View arg0) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogview = inflater.inflate(R.layout.alert_delete, null);
                final AlertDialog alert = new AlertDialog.Builder(context).create();
                final Button bCancel = (Button) dialogview.findViewById(R.id.cancel_button);
                final Button bOk = (Button) dialogview.findViewById(R.id.ok_button);

                bOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SQLHelper helper = new SQLHelper(context);
                        SQLiteDatabase db = helper.getReadableDatabase();

                        helper.onCreate(db);
                        helper.updateBudgetByDateHistory(itemsArrayList.get(position).getDate());
                        helper.deleteHistory(itemsArrayList.get(position).getDate());

                        ArrayList<HistoryItem> listHistory = new ArrayList<HistoryItem>();
                        HistoryAdapter adapterHistory;

                        listHistory = helper.getAllHistory();
                        adapterHistory = new HistoryAdapter(context, listHistory);

                        HistoryFragment.listItem.setAdapter(adapterHistory);
                        HistoryFragment.adapterStatus = 1;
                        alert.dismiss();

                    }
                });

                bCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                    }
                });
                alert.setView(dialogview);
                alert.show();

                return true;
            }
        });

        return rowView;
    }

    private void itemColorize(int position, View view) {
        if ((position % 2) == 0) {
            view.setBackground(ContextCompat.getDrawable(context, R.drawable.item_ripple));
        } else {
            view.setBackground(ContextCompat.getDrawable(context, R.drawable.item_ripple_second));
        }
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

            if (constraint == null || constraint.length() == 0) {
                results.values = oriItemsArrayList;
                results.count = oriItemsArrayList.size();
            } else {
                List<HistoryItem> nPlanetList = new ArrayList<HistoryItem>();

                for (int i = 0; i < itemsArrayList.size(); i++) {
                    if (itemsArrayList.get(i).getDate()
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
                            .getTotal()
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
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (results.count == 0)
                notifyDataSetInvalidated();
            else
                itemsArrayList = (ArrayList<HistoryItem>) results.values;
            notifyDataSetChanged();

        }

    }


}
