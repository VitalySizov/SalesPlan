package com.sizov.vitaly.salesplan;


public class Object {

    private int mId;
    private String mName;
    private String mAddress;
    private int mSalesPlan;
    private double[] mCurrentSales;


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
}
