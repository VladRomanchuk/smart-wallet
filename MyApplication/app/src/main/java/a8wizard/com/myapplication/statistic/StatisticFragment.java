package a8wizard.com.myapplication.statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import a8wizard.com.myapplication.R;
import a8wizard.com.myapplication.SQLHelper;
import a8wizard.com.myapplication.history.HistoryItem;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;

public class StatisticFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "StatisticFragment";

    private static SQLHelper helper;
    private ArrayList<HistoryItem> historyItems = new ArrayList<HistoryItem>();

    private LinearLayout fragmentStatistic;
    private RelativeLayout dayLayout;
    private RelativeLayout monthlLayout;
    private RelativeLayout yearLayout;
    private TextView headerView;
    private Calendar calendar;
    private static FrameLayout containerChart;
    private static ArrayList<HistoryItem> listHistory = new ArrayList<HistoryItem>();

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
        containerChart = (FrameLayout) v.findViewById(R.id.container);
        dayLayout = (RelativeLayout) v.findViewById(R.id.dailyLayout);
        monthlLayout = (RelativeLayout) v.findViewById(R.id.monthlyLayout);
        yearLayout = (RelativeLayout) v.findViewById(R.id.yearlyLayout);
        fragmentStatistic = (LinearLayout) v.findViewById(R.id.fragmentStatistic);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dailyLayout:
                showDayStatistic(view);
                break;
            case R.id.monthlyLayout:
                showMonthlyStatistic(view);
                break;
            case R.id.yearlyLayout:
                showYearlyStatistic(view);
                break;
        }

    }

    private void showYearlyStatistic(View view) {
        containerChart.removeAllViews();
        Animation slide = AnimationUtils.loadAnimation(getActivity(), R.anim.left_to_right);
        containerChart.startAnimation(slide);
        listHistory = helper.getAllYearlyHistory();
        Collections.sort(listHistory, new Comparator<HistoryItem>() {
            @Override
            public int compare(HistoryItem historyItem, HistoryItem t1) {
                return historyItem.getDate().compareTo(t1.getDate());
            }
        });

        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        headerView.setText(calendar.get(Calendar.YEAR) + "");
        dayLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        monthlLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        yearLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        historyItems = helper.getAllYearlyHistory();
    }

    private void showDayStatistic(View view) {
        containerChart.removeAllViews();
        Animation slide = AnimationUtils.loadAnimation(getActivity(), R.anim.left_to_right);
        containerChart.startAnimation(slide);
        listHistory = helper.getAllDay();
        Collections.sort(listHistory, new Comparator<HistoryItem>() {
            @Override
            public int compare(HistoryItem historyItem, HistoryItem t1) {
                return historyItem.getDate().compareTo(t1.getDate());
            }
        });
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        headerView.setVisibility(View.VISIBLE);
        headerView.setText(getResources().getString(R.string.title_statistic_day));
        dayLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        monthlLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        yearLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
    }

    private void showMonthlyStatistic(View view) {
        containerChart.removeAllViews();
        Animation slide = AnimationUtils.loadAnimation(getActivity(), R.anim.left_to_right);
        containerChart.startAnimation(slide);
        listHistory = helper.getAllMonthlyHistory();
        Collections.sort(listHistory, new Comparator<HistoryItem>() {
            @Override
            public int compare(HistoryItem historyItem, HistoryItem t1) {
                return historyItem.getDate().compareTo(t1.getDate());
            }
        });
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        headerView.setVisibility(View.VISIBLE);
        headerView.setText(new SimpleDateFormat("MMMM").format(calendar.getTime()) + ", " + calendar.get(Calendar.YEAR));
        dayLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        monthlLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        yearLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        historyItems = helper.getAllMonthlyHistory();
    }

    public static class PlaceholderFragment extends Fragment {

        private ComboLineColumnChartView chart;
        private ComboLineColumnChartData data;

        private int maxNumberOfLines = 200;
        public List<AxisValue> axisValuesForX;
        Axis axisX;
        Axis axisY;

        float[] randomNumbersTab = new float[maxNumberOfLines];

        private boolean hasAxes = true;
        private boolean hasPoints = true;
        private boolean hasLines = true;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_combo_line_column_chart, container, false);

            chart = (ComboLineColumnChartView) rootView.findViewById(R.id.chart);

            axisValuesForX = new ArrayList<AxisValue>();
            axisY = new Axis().setHasLines(true);

            containerChart.removeAllViews();
            generateValues();
            generateData();


            return rootView;
        }

        private void generateValues() {
            for (int i = 0; i < listHistory.size(); ++i) {
                randomNumbersTab[i] = Float.parseFloat(listHistory.get(i).getTotal());
            }
        }

        private void generateData() {
            data = new ComboLineColumnChartData(generateColumnData(), generateLineData());

            if (hasAxes) {
                if (listHistory.size() > 0) {
                    axisX.setInside(true);
                    axisX.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.black));
                    axisX.setTextSize(14);

                    data.setAxisXBottom(axisX);
                    data.setAxisYLeft(axisY);
                }

            } else {
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }

            chart.setComboLineColumnChartData(data);
        }

        private LineChartData generateLineData() {

            List<Line> lines = new ArrayList<Line>();
            for (int i = 0; i < 1; ++i) {
                List<PointValue> values = new ArrayList<PointValue>();

                for (int j = 0; j < listHistory.size(); ++j) {
                    axisValuesForX.add(new AxisValue(j, listHistory.get(j).getDate().toCharArray()));

                    axisX = new Axis(axisValuesForX);
                    values.add(new PointValue(j, Float.parseFloat(listHistory.get(j).getTotal())));
                }

                Line line = new Line(values);
                if (line.getValues().size() % 2 == 0) {
                    line.setColor(Color.parseColor("#fdd471"));
                } else {
                    line.setColor(Color.parseColor("#fee9b8"));
                }
                line.setAreaTransparency(255);
                line.setPointColor(Color.parseColor("#f17022"));
                line.setHasLines(hasLines);
                line.setFilled(true);
                line.setHasPoints(hasPoints);
                lines.add(line);
                chart.setInteractive(false);
            }

            return new LineChartData(lines);

        }

        private ColumnChartData generateColumnData() {

            List<Column> columns = new ArrayList<>();
            List<SubcolumnValue> values;
            for (int i = 0; i < listHistory.size(); ++i) {
                values = new ArrayList<>();
                if (columns.size() % 2 == 0) {
                    values.add(new SubcolumnValue(Float.parseFloat(listHistory.get(i).getTotal()), Color.parseColor("#00ffffff")));
                } else {
                    values.add(new SubcolumnValue(Float.parseFloat(listHistory.get(i).getTotal()), Color.parseColor("#00ffffff")));
                }
                columns.add(new Column(values));
            }

            return new ColumnChartData(columns);
        }
    }
}

