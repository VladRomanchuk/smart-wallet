package a8wizard.com.myapplication.statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import a8wizard.com.myapplication.R;
import a8wizard.com.myapplication.SQLHelper;
import a8wizard.com.myapplication.history.HistoryItem;

public class StatisticFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "StatisticFragment";

    private SQLHelper helper;
    private ArrayList<HistoryItem> historyItems = new ArrayList<HistoryItem>();

    private LinearLayout chart;
    private LinearLayout fragmentStatistic;
    private RelativeLayout dayLayout;
    private RelativeLayout monthlLayout;
    private RelativeLayout yearLayout;
    private TextView headerView;

    private XYSeries incomeSeries;
    private XYMultipleSeriesDataset dataSet;
    private XYSeriesRenderer incomeRenderer;
    private XYMultipleSeriesRenderer multiRenderer;
    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistic, container, false);

        calendar = Calendar.getInstance();
        helper = new SQLHelper(getActivity());

        defineLayout(view);
        defineView(view);
        setupLayout();
        showDayStatistic(view);


        return view;
    }

    private void defineView(View view) {
        headerView = (TextView) view.findViewById(R.id.headerStatistic);
    }

    private void setupLayout() {
        dayLayout.setOnClickListener(this);
        monthlLayout.setOnClickListener(this);
        yearLayout.setOnClickListener(this);
    }

    private void defineLayout(View v) {
        chart = (LinearLayout) v.findViewById(R.id.chart);
        dayLayout = (RelativeLayout) v.findViewById(R.id.dailyLayout);
        monthlLayout = (RelativeLayout) v.findViewById(R.id.monthlyLayout);
        yearLayout = (RelativeLayout) v.findViewById(R.id.yearlyLayout);
        fragmentStatistic = (LinearLayout) v.findViewById(R.id.fragmentStatistic);

    }


    private View createGraph() {
        incomeSeries = new XYSeries("Price");

        // Adding data to Income and Expense Series
        for (int i = 0; i < historyItems.size(); i++) {
            incomeSeries.add(i,
                    Double.parseDouble(historyItems.get(i).getTotal()));

        }

        dataSet = new XYMultipleSeriesDataset();
        dataSet.addSeries(incomeSeries);

        // Creating XYSeriesRenderer to customize incomeSeries
        incomeRenderer = new XYSeriesRenderer();
        incomeRenderer.setColor(Color.rgb(130, 130, 230));
        incomeRenderer.setFillPoints(true);
        incomeRenderer.setLineWidth(2);
        incomeRenderer.setDisplayChartValues(true);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setChartTitle("Cost Tracker Chart");
        multiRenderer.setXTitle("Year");
        multiRenderer.setYTitle("Amount");
        multiRenderer.setZoomButtonsVisible(true);
        for (int i = 0; i < historyItems.size(); i++) {
            multiRenderer.addXTextLabel(i, historyItems.get(i).getDate());
        }

        multiRenderer.addSeriesRenderer(incomeRenderer);
        GraphicalView chartView = ChartFactory.getLineChartView(getActivity(),
                dataSet, multiRenderer);

        return chartView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.dailyLayout:
                showDayStatistic(view);
                headerView.setVisibility(View.VISIBLE);
                headerView.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + ", " + new SimpleDateFormat("MMMM").format(calendar.getTime()));
                break;

            case R.id.monthlyLayout:
                headerView.setVisibility(View.VISIBLE);
                headerView.setText(new SimpleDateFormat("MMMM").format(calendar.getTime()) + ", " + calendar.get(Calendar.YEAR));
                dayLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                monthlLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                yearLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                removeView();
                historyItems = helper.getAllMonthlyHistory();
                addView();
                break;

            case R.id.yearlyLayout:
                dayLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                monthlLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                yearLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                removeView();
                historyItems = helper.getAllYearlyHistory();
                addView();
                break;
        }
    }

    private void removeView() {
        chart.removeAllViews();
    }

    private void addView() {
        chart.addView(createGraph());
    }

    private void showDayStatistic(View view) {
        headerView.setVisibility(View.VISIBLE);
        headerView.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + ", " + new SimpleDateFormat("MMMM").format(calendar.getTime()));

        dayLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        monthlLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        yearLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

        removeView();
        historyItems = helper.getAllHistory();
        addView();
    }
}
