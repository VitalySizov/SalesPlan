package com.sizov.vitaly.salesplan;

import android.support.v4.app.Fragment;


public class ObjectDetailsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ObjectDetailsFragment();
    }
}
