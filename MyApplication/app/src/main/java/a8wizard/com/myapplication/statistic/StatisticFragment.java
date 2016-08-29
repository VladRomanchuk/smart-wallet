package a8wizard.com.myapplication.statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import a8wizard.com.myapplication.R;
import a8wizard.com.myapplication.SQLHelper;
import a8wizard.com.myapplication.history.HistoryItem;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;

public class StatisticFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "StatisticFragment";

    private static SQLHelper helper;
    private ArrayList<HistoryItem> historyItems = new ArrayList<HistoryItem>();

    private LinearLayout chart;
    private LinearLayout fragmentStatistic;
    private RelativeLayout dayLayout;
    private RelativeLayout monthlLayout;
    private RelativeLayout yearLayout;
    private TextView headerView;
    private Calendar calendar;
    private static ArrayList<HistoryItem> listHistory = new ArrayList<HistoryItem>();

    public static final int[] COLORS = new int[]{Color.parseColor("#f6a97a"), Color.parseColor("#fdd471")};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistic, container, false);

        if (savedInstanceState == null) {
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }

        calendar = Calendar.getInstance();
        helper = new SQLHelper(getActivity());

        defineLayout(view);
        defineView(view);
        setupLayout();

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
        dayLayout = (RelativeLayout) v.findViewById(R.id.dailyLayout);
        monthlLayout = (RelativeLayout) v.findViewById(R.id.monthlyLayout);
        yearLayout = (RelativeLayout) v.findViewById(R.id.yearlyLayout);
        fragmentStatistic = (LinearLayout) v.findViewById(R.id.fragmentStatistic);

    }

    @Override
    public void onClick(View view) {

    }

    public static class PlaceholderFragment extends Fragment {

        private ComboLineColumnChartView chart;
        private ComboLineColumnChartData data;

        private int maxNumberOfLines = 200;

        float[] randomNumbersTab = new float[maxNumberOfLines];

        private boolean hasAxes = true;
        private boolean hasPoints = true;
        private boolean hasLines = true;
        private boolean isCubic = false;
        private boolean hasLabels = false;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_combo_line_column_chart, container, false);

            chart = (ComboLineColumnChartView) rootView.findViewById(R.id.chart);
            listHistory = helper.getAllHistory();

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
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);
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
                    values.add(new PointValue(j, Float.parseFloat(listHistory.get(j).getTotal())));
                }

                Line line = new Line(values);
                line.setColor(Color.parseColor("#fee9b8"));
                line.setPointColor(ContextCompat.getColor(getActivity(), R.color.colorButtonTransactions));
                line.setCubic(isCubic);
                line.setHasLabels(hasLabels);
                line.setHasLines(hasLines);
                line.setHasPoints(hasPoints);
                lines.add(line);
            }

            LineChartData lineChartData = new LineChartData(lines);

            return lineChartData;

        }

        private ColumnChartData generateColumnData() {
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;

            for (int i = 0; i < listHistory.size(); ++i) {
                values = new ArrayList<SubcolumnValue>();
                if (columns.size() % 2 == 0) {
                    values.add(new SubcolumnValue(Float.parseFloat(listHistory.get(i).getTotal()), Color.parseColor("#fdd471")));

                } else {
                    values.add(new SubcolumnValue(Float.parseFloat(listHistory.get(i).getTotal()), Color.parseColor("#fee9b8")));

                }
                columns.add(new Column(values));
            }

            ColumnChartData columnChartData = new ColumnChartData(columns);
            return columnChartData;
        }

    }
}
