package a8wizard.com.myapplication.transactions;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import a8wizard.com.myapplication.MainActivity;
import a8wizard.com.myapplication.SQLHelper;
import a8wizard.com.myapplication.R;
import a8wizard.com.myapplication.Util;
import a8wizard.com.myapplication.history.HistoryFragment;

public class TransactionAdapter extends ArrayAdapter<TransactionItem> implements
        Filterable, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private Context context;
    private Calendar now;

    private ArrayList<TransactionItem> itemsArrayList;
    private ArrayList<TransactionItem> oriItemsArrayList;

    public Button bTimePicker;
    public Button bDatePicker;

    public long g;
    public String s;
    private Filter planetFilter;

    public TransactionAdapter(Context context, ArrayList<TransactionItem> itemsArrayList) {

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

    public TransactionItem getItem(int position) {
        return itemsArrayList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.rowhistory_layout, parent, false);
        RelativeLayout selector = (RelativeLayout) rowView.findViewById(R.id.row_layout);

        now = Calendar.getInstance();
        itemColorize(position, rowView);

        TextView time = (TextView) rowView.findViewById(R.id.textView1);
        final TextView description = (TextView) rowView.findViewById(R.id.textView2);
        TextView price = (TextView) rowView.findViewById(R.id.textView3);

        // 3. get
//        g = Util.getTimeStamp(,new SimpleDateFormat("hh:mm" ));
//        String s = new SimpleDateFormat("hh:mm").format(new Date(Long.parseLong(itemsArrayList.get(position).getTime())));
        time.setText(itemsArrayList.get(position).getTime());
        description.setText(itemsArrayList.get(position).getDescription());
        price.setText(Util.formatUSD(itemsArrayList.get(position).getPrice()));

        selector.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.alertdialog_detailhistory, null);
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setView(dialogView);
                alert.setCancelable(false);

                TextView dateTitle = (TextView) dialogView
                        .findViewById(R.id.alert_dialog_detail_history_date_title_text);
                TextView priceTitle = (TextView) dialogView
                        .findViewById(R.id.alert_dialog_detail_history_price_text);
                TextView dateBody = (TextView) dialogView
                        .findViewById(R.id.alert_dialog_detail_history_date_text);
                TextView timeBody = (TextView) dialogView
                        .findViewById(R.id.alert_dialog_detail_history_time_text);
                TextView descriptionBody = (TextView) dialogView
                        .findViewById(R.id.alert_dialog_detail_history_description_text);

                Button buttonCancel = (Button) dialogView.findViewById(R.id.alert_dialog_history_detail_cancel_button);
                Button buttonOk = (Button) dialogView.findViewById(R.id.alert_dialog_history_detail_ok_button);

                dateTitle.setText(itemsArrayList.get(position).getDate());
                priceTitle.setText(Util.formatUSD(itemsArrayList.get(position).getPrice()));
                dateBody.setText(itemsArrayList.get(position).getDate());
                timeBody.setText(itemsArrayList.get(position).getTime());
                descriptionBody.setText(itemsArrayList.get(position).getDescription());

                final AlertDialog show = alert.show();

                buttonCancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        show.dismiss();
                    }
                });

                buttonOk.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        show.dismiss();
                    }
                });

            }
        });

        selector.setOnLongClickListener(new OnLongClickListener() {

            public boolean onLongClick(View arg0) {
                // TODO Auto-generated method stub
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogview = inflater.inflate(
                        R.layout.alertdialog_delete_edit, null);
                final AlertDialog alert = new AlertDialog.Builder(context)
                        .create();

//                alert.setMessage("Ubah atau hapus transaksi?");
                alert.setView(dialogview);

                final Button bEdit = (Button) dialogview
                        .findViewById(R.id.edit_button);
                final Button bDelete = (Button) dialogview
                        .findViewById(R.id.delete_button);

                bEdit.setOnClickListener(new OnClickListener() {

                    public void onClick(View arg0) {
                        alert.dismiss();

                        final SQLHelper helper = new SQLHelper(
                                context);

                        LayoutInflater inflater = LayoutInflater.from(context);
                        View dialogview = inflater.inflate(R.layout.alertdialog_edit, null);
                        final AlertDialog alert = new AlertDialog.Builder(context).create();

                        bTimePicker = (Button) dialogview
                                .findViewById(R.id.button3);
                        bDatePicker = (Button) dialogview
                                .findViewById(R.id.delete_button);


                        final EditText ePrice = (EditText) dialogview
                                .findViewById(R.id.editText4);
                        final EditText eDescription = (EditText) dialogview
                                .findViewById(R.id.editText5);

                        bTimePicker.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showTimePicker();
                            }
                        });

                        bDatePicker.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showDatePicker();
                            }
                        });

                        final TransactionItem transaksi = helper
                                .getDetailTransactions(itemsArrayList
                                        .get(position).getIdTransaction());

                        bTimePicker.setText(itemsArrayList.get(position).getTime());
                        bDatePicker.setText(transaksi.getDate());
                        ePrice.setText(transaksi.getPrice());
                        eDescription.setText(transaksi.getDescription());

                        final double tempAmount = Double.parseDouble(transaksi
                                .getPrice());


                        final Button bEdit = (Button) dialogview
                                .findViewById(R.id.edit_button);
                        bEdit.setOnClickListener(new OnClickListener() {

                            public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                int idBudgetBefore = helper.getIdBudgetByDateTransaction(Util
                                        .getTimeStamp(transaksi.getDate()
                                                        + " " + transaksi.getTime(),
                                                new SimpleDateFormat("dd/MM/yyyy kk:mm:ss")));
                                int idBudgetAfter = helper.getIdBudgetByDateTransaction(Util
                                        .getTimeStamp(bDatePicker.getText()
                                                .toString()
                                                + " "
                                                + bTimePicker.getText()
                                                .toString(), new SimpleDateFormat("dd/MM/yyyy kk:mm:ss")));

                                if ((idBudgetBefore != idBudgetAfter)) {
                                    helper.updateBudgetSum(idBudgetBefore, Long
                                            .parseLong(transaksi.getPrice()));
                                    helper.updateBudgetDifference(
                                            idBudgetAfter, Long
                                                    .parseLong(ePrice.getText()
                                                            .toString()));
                                    if ((idBudgetBefore != -1)
                                            || (idBudgetAfter != -1)) {
                                        Toast.makeText(context,
                                                "Budget has been updated",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }

                                ArrayList<TransactionItem> listTransaksi = new ArrayList<TransactionItem>();
                                TransactionAdapter adapterTransaksi;

                                TransactionItem transaksiUpdate = new TransactionItem(
                                        itemsArrayList.get(position).getIdTransaction(),
                                        eDescription.getText().toString(),
                                        ePrice.getText().toString(),
                                        bTimePicker.getText().toString(),
                                        bDatePicker.getText().toString(),
                                        Util.getTimeStamp(bTimePicker.getText()
                                                .toString(), new SimpleDateFormat("HH:mm")));
                                helper.updateTransaction(transaksiUpdate);

                                // update amount budget yang transaksinya masih
                                // di tanggal budget tersebut

                                helper.updateBudgetByDate(
                                        bDatePicker.getText().toString()
                                                + " "
                                                + bTimePicker.getText()
                                                .toString(),
                                        tempAmount
                                                - Double.parseDouble(ePrice
                                                .getText().toString()));

                                listTransaksi = helper
                                        .getAllTransactionByTanggal(itemsArrayList
                                                .get(position).getDate());
                                adapterTransaksi = new TransactionAdapter(
                                        context, listTransaksi);

                                HistoryFragment.listItem
                                        .setAdapter(adapterTransaksi);
                                HistoryFragment.adapterStatus = 2;

                                alert.dismiss();

                            }
                        });

                        alert.setView(dialogview);

                        alert.show();
                    }
                });

                bDelete.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        alert.dismiss();

                        ArrayList<TransactionItem> listTransaksi = new ArrayList<TransactionItem>();
                        TransactionAdapter adapterTransaksi;

                        SQLHelper helper = new SQLHelper(context);

                        helper.deleteTransaction(itemsArrayList.get(position)
                                .getIdTransaction());
                        helper.updateBudgetByDate(
                                itemsArrayList.get(position).getDate() + " "
                                        + itemsArrayList.get(position).getTime(),
                                Double.parseDouble(itemsArrayList.get(position)
                                        .getPrice()));

                        listTransaksi = helper
                                .getAllTransactionByTanggal(itemsArrayList.get(
                                        position).getDate());
                        adapterTransaksi = new TransactionAdapter(context,
                                listTransaksi);

                        HistoryFragment.listItem.setAdapter(adapterTransaksi);
                        HistoryFragment.adapterStatus = 2;
                    }
                });

                alert.show();
                return false;
            }
        });

        return rowView;
    }

    public void showTimePicker() {
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );

        timePickerDialog.dismissOnPause(true);
        timePickerDialog.setAccentColor(ContextCompat.getColor(context, R.color.colorPrimary));
        timePickerDialog.show(((MainActivity) context).getFragmentManager(), "TimePicker Dialog");

    }

    public void showDatePicker() {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.setAccentColor(ContextCompat.getColor(context, R.color.colorPrimary));
        datePickerDialog.dismissOnPause(true);
        datePickerDialog.show(((MainActivity) context).getFragmentManager(), "DatePicker Dialog");

    }

    private void itemColorize(int position, View view) {
        if ((position % 2) == 0) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorItems));
        } else {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorItemSecond));
        }
    }

    @Override
    public Filter getFilter() {
        if (planetFilter == null)
            planetFilter = new PlanetFilter();

        return planetFilter;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;

        g = Util.getTimeStamp(hourString + ":" + minuteString, new SimpleDateFormat("HH:mm"));
        Toast.makeText(context, Long.toString(g), Toast.LENGTH_SHORT).show();
        s = new SimpleDateFormat("hh:mm").format(new Date(g));
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();


        bTimePicker.setText(s);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "." + (++monthOfYear) + "." + year;
        bDatePicker.setText(date);
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
                List<TransactionItem> nPlanetList = new ArrayList<TransactionItem>();

                for (int i = 0; i < itemsArrayList.size(); i++) {
                    if (itemsArrayList.get(i).getTime().toUpperCase()
                            .startsWith(constraint.toString().toUpperCase())
                            || itemsArrayList
                            .get(i)
                            .getDescription()
                            .toUpperCase()
                            .startsWith(
                                    constraint.toString().toUpperCase())
                            || itemsArrayList
                            .get(i)
                            .getPrice()
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

                itemsArrayList = (ArrayList<TransactionItem>) results.values;
                notifyDataSetChanged();
            }

        }

    }


}
