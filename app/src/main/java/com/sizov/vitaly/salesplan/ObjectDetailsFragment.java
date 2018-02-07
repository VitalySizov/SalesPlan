package com.sizov.vitaly.salesplan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;


public class ObjectDetailsFragment extends Fragment {


    private final static String TAG = "ObjectDetailsFragment";

    private BarChart mChartCurrentSales;

    private TextView mNameTextView;
    private TextView mTotalCurrentSalesTextView;

    private String mName;
    private double[] mCurrentSales;
    private double mTotalCurrentSales;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get intent from first activity and extract data
        Intent intent = getActivity().getIntent();
        mName = intent.getStringExtra("EXTRA_OBJECT_NAME");
        mCurrentSales = intent.getDoubleArrayExtra("EXTRA_CURRENT_SALES");
        mTotalCurrentSales = intent.getDoubleExtra("EXTRA_TOTAL_CURRENT_SALES", mTotalCurrentSales);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_object_details, container, false);


        mNameTextView = (TextView) view.findViewById(R.id.name_text);
        mNameTextView.setText(mName);

        mTotalCurrentSalesTextView = (TextView) view.findViewById(R.id.total_current_sales_text_view);
        mTotalCurrentSalesTextView.setText(String.format("%(.2f", mTotalCurrentSales) + "$");

        mChartCurrentSales = (BarChart) view.findViewById(R.id.chart_sales);


        setSettingChart();

        return view;
    }


    public void setSettingChart() {

        mChartCurrentSales.setEnabled(true);
        mChartCurrentSales.setScaleEnabled(false);
        mChartCurrentSales.getDescription().setText("");

        mChartCurrentSales.setDrawBorders(true);
        mChartCurrentSales.setBorderColor(Color.GRAY);

        final ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; mCurrentSales.length > i; i++) {
            values.add(new BarEntry(i, (float) mCurrentSales[i]));
        }

        BarDataSet set = new BarDataSet(values, "The current sales on hours");
        int colorPrimaryTins = Color.parseColor("#6573c3");
        set.setColor(colorPrimaryTins);


        final ArrayList<String> xLabel = new ArrayList<>();
        for (int i=1; i <= mCurrentSales.length; i++) {
            xLabel.add(8 + i + ":00");
        }

        
        XAxis xAxis = mChartCurrentSales.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabel.get((int)value);
            }
        });

        BarData data = new BarData(set);

        Legend legend = mChartCurrentSales.getLegend();
        legend.setTextSize(15f);
        legend.setFormSize(15f);

        data.setValueTextSize(10f);

        mChartCurrentSales.animateXY(2500, 2500);
        mChartCurrentSales.setData(data);
    }
}

