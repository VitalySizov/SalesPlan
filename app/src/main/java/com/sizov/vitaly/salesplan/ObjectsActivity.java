package com.sizov.vitaly.salesplan;

import android.support.v4.app.Fragment;

public class ObjectsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return ObjectsFragment.newInstance();
    }
}

