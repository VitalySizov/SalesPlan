package com.sizov.vitaly.salesplan;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ObjectLab {

    private static ObjectLab sObjectLab;

    private ArrayList<Object> mObjects;

    public static ObjectLab get(Context context) {
        if (sObjectLab == null) {
            sObjectLab = new ObjectLab(context);
        }
        return sObjectLab;
    }

    private ObjectLab(Context context) {
        mObjects = new ArrayList<>();
    }

    public List<Object> getObjects() {
        return mObjects;
    }
}
