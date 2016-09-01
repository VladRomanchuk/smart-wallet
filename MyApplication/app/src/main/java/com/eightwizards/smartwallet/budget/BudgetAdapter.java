package com.eightwizards.smartwallet.budget;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.eightwizards.smartwallet.R;
import com.eightwizards.smartwallet.Util;
import com.eightwizards.smartwallet.BudgetItem;

public class BudgetAdapter extends ArrayAdapter<BudgetItem> implements Filterable {

    private Context context;
    public static String css = null;
    private ArrayList<BudgetItem> itemsArrayList;
    private ArrayList<BudgetItem> oriItemsArrayList;

    private Filter planetFilter;

    public BudgetAdapter(Context context, ArrayList<BudgetItem> itemsArrayList) {

        super(context, R.layout.rowbudgethistory_layout, itemsArrayList);

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

    public BudgetItem getItem(int position) {
        return itemsArrayList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.rowbudgethistory_layout, parent,
                false);


        TextView tId = (TextView) rowView.findViewById(R.id.textView1);
        TextView tTglMulai = (TextView) rowView.findViewById(R.id.textView2);
        TextView tTglAkhir = (TextView) rowView.findViewById(R.id.textView3);
        TextView tKategori = (TextView) rowView.findViewById(R.id.textView4);
        TextView tAmount = (TextView) rowView.findViewById(R.id.textView5);
        TextView tLeft = (TextView) rowView.findViewById(R.id.textView6);


        tId.setText((Integer.toString(itemsArrayList.get(position).getIdBudget())));
        tTglMulai.setText(itemsArrayList.get(position).getStartDate());
        tTglAkhir.setText(itemsArrayList.get(position).getEndDate());
        tKategori.setText((itemsArrayList.get(position).getCategory()));
        tAmount.setText(Util.formatUSD(itemsArrayList.get(position).getAmount()));
        tLeft.setText(Util.formatUSD(itemsArrayList.get(position).getLeft()));


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
                List<BudgetItem> nPlanetList = new ArrayList<BudgetItem>();

                for (int i = 0; i < itemsArrayList.size(); i++) {
                    if (Integer.toString(itemsArrayList.get(i).getIdBudget())
                            .toUpperCase()
                            .startsWith(constraint.toString().toUpperCase())
                            || itemsArrayList
                            .get(i)
                            .getStartDate()
                            .toUpperCase()
                            .startsWith(
                                    constraint.toString().toUpperCase())
                            || itemsArrayList
                            .get(i)
                            .getEndDate()
                            .toUpperCase()
                            .startsWith(
                                    constraint.toString().toUpperCase())
                            || itemsArrayList
                            .get(i)
                            .getCategory()
                            .toUpperCase()
                            .startsWith(
                                    constraint.toString().toUpperCase())
                            || itemsArrayList
                            .get(i)
                            .getAmount()
                            .toUpperCase()
                            .startsWith(
                                    constraint.toString().toUpperCase())
                            || itemsArrayList
                            .get(i)
                            .getLeft()
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
                                      Filter.FilterResults results) {

            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {

                itemsArrayList = (ArrayList<BudgetItem>) results.values;
                notifyDataSetChanged();
            }

        }

    }
}
