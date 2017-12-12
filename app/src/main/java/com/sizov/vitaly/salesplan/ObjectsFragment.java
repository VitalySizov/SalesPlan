package com.sizov.vitaly.salesplan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ObjectsFragment extends Fragment {

    private static final String TAG = "ObjectsFragment";

    private Button mFetchButton;
    public static TextView mFetchText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public static ObjectsFragment newInstance() {
        return new ObjectsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_objects, container, false);

        mFetchButton = (Button)v.findViewById(R.id.fetch_button);
        mFetchText = (TextView)v.findViewById(R.id.fetch_text);

        mFetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FetchData process = new FetchData();
                process.execute();
            }
        });

        return v;

    }
}
