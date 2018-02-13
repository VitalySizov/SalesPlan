package com.sizov.vitaly.salesplan;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectsFragment extends Fragment {

    private ArrayList<Object> mObjects;

    private static final String TAG = "ObjectsFragment";

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private Button mButtonRetry;
    private TextView mTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FetchData();
    }

    public static ObjectsFragment newInstance() {
        return new ObjectsFragment();
    }

    // Checking Internet connection
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void FetchData() {
        if (isOnline()) {
            FetchData process = new FetchData();
            process.execute();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_objects, container, false);

        mRecyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mProgressBar = (ProgressBar)v.findViewById(R.id.objects_fragment_progress_bar);

        mButtonRetry = (Button) v.findViewById(R.id.button_retry);
        mTextView = (TextView)v.findViewById(R.id.no_connection_text_view);

        // Hide or show elements in layout
        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mButtonRetry.setVisibility(View.VISIBLE);
            mTextView.setVisibility(View.VISIBLE);
        }

        mButtonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOnline()) {
                    mButtonRetry.setVisibility(View.GONE);
                    mTextView.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.VISIBLE);
                    FetchData();
                } else {
                    Toast.makeText(getContext(), "No connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        return v;
    }

    class FetchData extends AsyncTask<Void, Void, List<Object>> {

        private final static String TAG = "FetchData";

        private String data = "";

        @Override
        protected List<Object> doInBackground(Void... voids) {

            try {
                URL url = new URL("https://api.myjson.com/bins/nonl5");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";

                while (line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                }


                mObjects = new ArrayList<>();

                // Parsing and saving JSON arrays
                JSONArray jsonArray = new JSONArray(data);
                Gson gson = new Gson();

                for (int i=0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Object object = gson.fromJson(jsonObject.toString(), Object.class);
                    mObjects.add(object);
                }

                // Total amount of sale
                for(int i=0; i < mObjects.size(); i++) {

                    double sum = 0;
                    double[] array = mObjects.get(i).getCurrentSales();

                    for (int j=0; j< array.length; j++) {
                        sum = sum + array[j];
                        mObjects.get(i).setTotalCurrentSales(sum);
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Object> Data) {

            ObjectAdapter objectAdapter = new ObjectAdapter(mObjects, getContext());
            mRecyclerView.setAdapter(objectAdapter);
            objectAdapter.notifyDataSetChanged();

            mProgressBar.setVisibility(View.GONE);

            for (int i = 0; mObjects.size() > i; i++ ) {
                Log.i(TAG, "Id = " + mObjects.get(i).getId());
                Log.i(TAG, "Name = " + mObjects.get(i).getName());
                Log.i(TAG, "Address = " + mObjects.get(i).getAddress());
                Log.i(TAG, "CurrentSales = " + Arrays.toString(mObjects.get(i).getCurrentSales()));
                Log.i(TAG, "TotalCurrentSales = " + mObjects.get(i).getTotalCurrentSales());
                Log.i(TAG, "SalesPlan = " + mObjects.get(i).getSalesPlan());
                Log.i(TAG, "...............................................");
            }
        }
    }
}
