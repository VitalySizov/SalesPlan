package com.sizov.vitaly.salesplan;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class ObjectAdapter extends RecyclerView.Adapter<ObjectAdapter.ViewHolder> {

    private List<Object> mObjectList;
    private Context mContext;

    private static final String TAG = "ObjectAdapter";

    public ObjectAdapter(List<Object> objects, Context context) {

        this.mObjectList = objects;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_objects, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ObjectAdapter.ViewHolder holder, final int position) {

        final Object object = mObjectList.get(position);
        holder.mName.setText(object.getName());
        holder.mAddress.setText(object.getAddress());
        holder.mTotalCurrentSales.setText("Total current sales: " + String.format(Locale.getDefault(),
                "%(.2f", object.getTotalCurrentSales()) + "\u0024");
        holder.mSalesPlan.setText("Sales plan: " + String.format(Locale.getDefault(),"%d",
                object.getSalesPlan()) + "\u0024");
        holder.mPercentProgressBar.setProgress(object.getPercentProgress());
        holder.mPercentProgressTextView.setText(String.valueOf(object.getPercentProgress() + "\u0025"));

        // Transition to ObjectDetailsActivity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setObjectInView(object);
            }
        });
    }

    // Data transfer to the second activity
    private void setObjectInView(Object object) {

        Intent intent = new Intent(mContext, ObjectDetailsActivity.class);
        intent.putExtra("EXTRA_OBJECT_NAME", object.getName());
        intent.putExtra("EXTRA_CURRENT_SALES", object.getCurrentSales());
        intent.putExtra("EXTRA_TOTAL_CURRENT_SALES", object.getTotalCurrentSales());
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (mObjectList != null) {
            return mObjectList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mName;
        public TextView mAddress;
        public TextView mTotalCurrentSales;
        public TextView mSalesPlan;
        public ProgressBar mPercentProgressBar;
        public TextView mPercentProgressTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.name_text_view);
            mAddress = (TextView) itemView.findViewById(R.id.address_text_view);
            mTotalCurrentSales = (TextView) itemView.findViewById(R.id.total_current_sales_text_view);
            mSalesPlan = (TextView) itemView.findViewById(R.id.sales_plan_text_view);
            mPercentProgressBar = (ProgressBar)itemView.findViewById(R.id.percent_progress_bar);
            mPercentProgressTextView = (TextView)itemView.findViewById(R.id.percent_progress_text_view);
        }
    }
}
