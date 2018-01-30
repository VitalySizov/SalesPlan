package com.sizov.vitaly.salesplan;

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
import android.widget.ProgressBar;

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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FetchData process = new FetchData();
        process.execute();
    }

    public static ObjectsFragment newInstance() {
        return new ObjectsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_objects, container, false);

        mRecyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mProgressBar = (ProgressBar)v.findViewById(R.id.objects_fragment_progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

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
                URL url = new URL("https://api.myjson.com/bins/zf4u7");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";

                while (line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                }

                mObjects = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    Object object = new Object();
                    object.setId(jsonObject.getInt("id"));
                    object.setName((String) jsonObject.get("Name"));
                    object.setAddress((String) jsonObject.get("Address"));
                    object.setSalesPlan(jsonObject.getInt("Sales Plan"));

                    JSONArray salesArr = (JSONArray) jsonObject.get("Current Sales");
                    double[] arr = new double[salesArr.length()];

                    double sum = 0;

                    for(int j = 0; j < salesArr.length(); j++) {
                        double element = salesArr.getDouble(j);
                        arr[j] =+ element;
                        sum = sum + element;
                        object.setCurrentSales(arr);
                        object.setTotalCurrentSales(sum);
                    }

                    mObjects.add(object);
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
