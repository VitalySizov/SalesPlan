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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Arrays;


public class ObjectDetailsFragment extends Fragment {


    private final static String TAG = "ObjectDetailsFragment";

    private LineChart mChartCurrentSales;

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

        mChartCurrentSales = (LineChart) view.findViewById(R.id.chart_sales);


        setSettingChart();

        return view;
    }


    public void setSettingChart() {

        mChartCurrentSales.setEnabled(true);
        mChartCurrentSales.setScaleEnabled(false);

        mChartCurrentSales.setDrawBorders(true);
        mChartCurrentSales.setBorderColor(Color.GRAY);

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; mCurrentSales.length > i; i++) {
            values.add(new Entry(i, (float) mCurrentSales[i]));
        }

        LineDataSet set = new LineDataSet(values, "Data Set 1");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        LineData dataSet = new LineData(dataSets);
        mChartCurrentSales.setData(dataSet);
    }
}

