package io.alstonlin.hackprinceton;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

public class VisualizeFragment extends Fragment {

    private static final String ARG_ACTIVITY = "activity";
    private static final String[] COLUMN_LABELS = {"Cals", "Coles.", "Fat", "Protien", "Carbs", "Sugar", "Sodium"};
    private static final int[] COLUMN_COLORS = {Color.parseColor("#E53935"), Color.parseColor("#FFEB3B"), Color.parseColor("#795548"),
            Color.parseColor("#4CAF50"), Color.parseColor("#03A9F4"), Color.parseColor("#FF9800"), Color.parseColor("#9C27B0")};

    // BAD pratice, but hackathon so who cares
    public static VisualizeFragment instance;

    private MainActivity activity;
    private String[] dayLabels;
    private float[][] data;
    private int[] dayData;
    private ColumnChartView chartTop;
    private LineChartView chartBottom;

    private LineChartData lineData;
    private ColumnChartData columnData;

    /**
     * Use this Factory method to create the Fragment instead of the constructor.
     * @param activity The Activity this Fragment will be attached to
     * @return The new Fragment instance
     */
    public static VisualizeFragment newInstance(MainActivity activity) {
        final VisualizeFragment fragment = new VisualizeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ACTIVITY, activity);
        fragment.setArguments(args);
        fragment.activity = activity;
        instance = fragment;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generateData();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_visualize, container, false);
        chartTop = (ColumnChartView) v.findViewById(R.id.chart_top);
        chartBottom = (LineChartView) v.findViewById(R.id.chart_bottom);
        setupColumns();
        setupLineData();
        return v;
    }

    public void notifyDataChanged(){
        generateData();
        setupLineData();
    }

    public void generateData(){
        data = new float[7][7];
        dayData = new int[7];
        dayLabels = new String[7];

        for (int i = 0; i < data.length; i++){
            for (int j = 0; j < data[0].length; j++){
                data[i][j] = 0;
            }
            DateTime date = DateTime.now().minusDays(i);
            dayLabels[i] = date.toString("MM/dd");
        }

        for (Food f : activity.getFoods()){
            int days = Days.daysBetween(new DateTime(f.getCreatedAt()), DateTime.now()).getDays();
            if (days < 7){
                data[days][0] += 100 * f.getCalories() / (double)Food.HEALTHY_CALORIES;
                data[days][1] += 100 * f.getColesterol() / (double)Food.HEALTHY_COLESTEROL;
                data[days][2] += 100 * f.getFat() / (double)Food.HEALTHY_FAT;
                data[days][3] += 100 * f.getProtien() / (double)Food.HEALTHY_PROTIEN;
                data[days][4] += 100 * f.getCarbs() / (double)Food.HEALTHY_CARBS;
                data[days][5] += 100 * f.getSugar() / (double)Food.HEALTHY_SUGAR;
                data[days][6] += 100 * f.getSodium() / (double)Food.HEALTHY_SODIUM;
            }
        }

        for (int i = 0; i < data.length; i++){
            dayData[i] = getHealthValue(data[i]);
        }
    }

    private int getHealthValue(float[] dayData){
        float sum = 0;
        for (float aDayData : dayData) {
            sum += aDayData;
        }
        if (sum == 0) return 2;
        return 100 - (int)Math.sqrt(Math.abs(sum - dayData.length * 100));
    }

    private void setupColumns() {
        List<AxisValue> axisValues = new ArrayList<>();
        List<Column> columns = new ArrayList<>();
        for (int i = 0; i < COLUMN_LABELS.length; ++i) {
            List<SubcolumnValue> values = new ArrayList<>();
            values.add(new SubcolumnValue(0, COLUMN_COLORS[i]));
            axisValues.add(new AxisValue(i).setLabel(COLUMN_LABELS[i]));
            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }
        columnData = new ColumnChartData(columns);
        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(4));
        chartTop.setColumnChartData(columnData);
        chartTop.setZoomEnabled(false);
    }

    private void setupLineData() {
        int numValues = 7;
        List<AxisValue> axisValues = new ArrayList<>();
        List<PointValue> values = new ArrayList<>();
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, dayData[numValues - i - 1]));
            axisValues.add(new AxisValue(i).setLabel(dayLabels[numValues - i - 1]));
        }

        Line line = new Line(values);
        line.setColor(Color.BLACK).setCubic(false);
        List<Line> lines = new ArrayList<>();
        lines.add(line);

        lineData = new LineChartData(lines);
        lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

        chartBottom.setLineChartData(lineData);
        chartBottom.setViewportCalculationEnabled(false);
        Viewport v = new Viewport(0, 110, 6, 0);
        chartBottom.setMaximumViewport(v);
        chartBottom.setCurrentViewport(v);
        chartBottom.setZoomEnabled(false);
        chartBottom.setOnValueTouchListener(new ValueTouchListener());
        chartBottom.setValueSelectionEnabled(true);
    }

    private void generateColumnData(int index) {
        chartTop.cancelDataAnimation();
        List<Column> columns = columnData.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            ArrayList<SubcolumnValue> values = new ArrayList<>();
            values.add(new SubcolumnValue(index == -1 ? 0 : data[data.length - index - 1][i], COLUMN_COLORS[i]));
            column.setValues(values);
        }
        chartTop.startDataAnimation(300);
    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {
        @Override
        public void onValueDeselected() {
            generateColumnData(-1);
        }

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            generateColumnData(pointIndex);
        }
    }


}
