package com.sizov.vitaly.salesplan;


import android.os.Parcel;
import android.os.Parcelable;

public class Object implements Parcelable{

    private int mId;
    private String mName;
    private String mAddress;
    private int mSalesPlan;
    private double[] mCurrentSales;
    private double mTotalCurrentSales;
    private int mPercentProgress;

    public Object(int id, String name, String address, int salesPlan, double[] currentSales, double totalCurrentSales, int percentProgress) {
        mId = id;
        mName = name;
        mAddress = address;
        mSalesPlan = salesPlan;
        mCurrentSales = currentSales;
        mTotalCurrentSales = totalCurrentSales;
        mPercentProgress = percentProgress;
    }

    protected Object(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mAddress = in.readString();
        mSalesPlan = in.readInt();
        mCurrentSales = in.createDoubleArray();
        mTotalCurrentSales = in.readDouble();
        mPercentProgress = in.readInt();
    }

    public static final Creator<Object> CREATOR = new Creator<Object>() {
        @Override
        public Object createFromParcel(Parcel in) {
            return new Object(in);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[size];
        }
    };


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public int getSalesPlan() {
        return mSalesPlan;
    }

    public void setSalesPlan(int salesPlan) {
        mSalesPlan = salesPlan;
    }

    public double[] getCurrentSales() {
        return mCurrentSales;
    }

    public void setCurrentSales(double[] currentSales) {
        mCurrentSales = currentSales;
    }

    public double getTotalCurrentSales() {
        return mTotalCurrentSales;
    }

    public void setTotalCurrentSales(double totalCurrentSales) {
        mTotalCurrentSales = totalCurrentSales;
    }

    public int getPercentProgress() {
        return mPercentProgress;
    }

    public void setPercentProgress(int percentProgress) {
        mPercentProgress = percentProgress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mName);
        parcel.writeString(mAddress);
        parcel.writeInt(mSalesPlan);
        parcel.writeDoubleArray(mCurrentSales);
        parcel.writeDouble(mTotalCurrentSales);
    }
}
