package com.sizov.vitaly.salesplan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.sizov.vitaly.salesplan.database.ObjectDbHelper;

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

    private ObjectDbHelper objectDbHelper;

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

    // Viewing table items in log
    public void viewElementsDb() {

        objectDbHelper = new ObjectDbHelper(getContext());
        Cursor cursor = objectDbHelper.getAll();

        if (!cursor.moveToNext())  {
            Toast.makeText(getContext(), "When you first start the application you need the Internet", Toast.LENGTH_LONG).show();
        }

        if (cursor.moveToFirst()) {

            int idIndex = cursor.getColumnIndex(objectDbHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(objectDbHelper.KEY_NAME);
            int addressIndex = cursor.getColumnIndex(objectDbHelper.KEY_ADDRESS);
            int salesPlanIndex = cursor.getColumnIndex(objectDbHelper.KEY_SALESPLAN);
            int currentSalesIndex = cursor.getColumnIndex(objectDbHelper.KEY_CURRENTSALES);
            int totalCurrentSalesIndex = cursor.getColumnIndex(objectDbHelper.KEY_TOTALCURRENTSALES);
            do {
                Log.d(TAG,"ID = " + cursor.getInt(idIndex)
                        + " | NAME = " + cursor.getString(nameIndex)
                        + " | ADDRESS = " + cursor.getString(addressIndex)
                        + " | SALES PLAN = " + cursor.getString(salesPlanIndex)
                        + " | CURRENT SALES = " + cursor.getString(currentSalesIndex)
                        + " | TOTAL SALES = " + cursor.getString(totalCurrentSalesIndex));
            } while (cursor.moveToNext());
        } else
            Log.d(TAG, "0 rows");

        cursor.close();
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

    public void setupAdapter() {
        ObjectAdapter objectAdapter = new ObjectAdapter(mObjects, getContext());
        mRecyclerView.setAdapter(objectAdapter);
        objectAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_objects, container, false);

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

                    Snackbar snackbar = Snackbar
                            .make(v, "No internet connection!", Snackbar.LENGTH_LONG)
                            .setAction("OFFLINE MODE", new View.OnClickListener() {

                                // Transition to OFFLINE MODE
                                @Override
                                public void onClick(View view) {

                                    objectDbHelper = new ObjectDbHelper(getContext());
                                    Cursor cursor = objectDbHelper.getAll();

                                    if (!cursor.moveToNext())  {
                                        Toast.makeText(getContext(), "When you first start the application you need the Internet", Toast.LENGTH_LONG).show();
                                    }

                                    if (cursor.moveToFirst()) {

                                        mButtonRetry.setVisibility(View.INVISIBLE);
                                        mTextView.setVisibility(View.INVISIBLE);

                                        int idIndex = cursor.getColumnIndex(objectDbHelper.KEY_ID);
                                        int nameIndex = cursor.getColumnIndex(objectDbHelper.KEY_NAME);
                                        int addressIndex = cursor.getColumnIndex(objectDbHelper.KEY_ADDRESS);
                                        int salesPlanIndex = cursor.getColumnIndex(objectDbHelper.KEY_SALESPLAN);
                                        int currentSalesIndex = cursor.getColumnIndex(objectDbHelper.KEY_CURRENTSALES);
                                        int totalCurrentSalesIndex = cursor.getColumnIndex(objectDbHelper.KEY_TOTALCURRENTSALES);

                                        Log.d(TAG,"ID = " + cursor.getInt(idIndex)
                                                + " | NAME = " + cursor.getString(nameIndex)
                                                + " | ADDRESS = " + cursor.getString(addressIndex)
                                                + " | SALES PLAN = " + cursor.getString(salesPlanIndex)
                                                + " | CURRENT SALES = " + cursor.getString(currentSalesIndex)
                                                + " | TOTAL SALES = " + cursor.getString(totalCurrentSalesIndex));


                                        mObjects = new ArrayList<>();

                                        do {

                                            // Convert String to Double[]
                                            String numbers = cursor.getString(currentSalesIndex);
                                            String[] items = numbers.replaceAll("\\[", "").replaceAll("\\]", "").split(",");

                                            double[] result = new double[items.length];

                                            for (int i=0; i < items.length; i++) {

                                                try {
                                                    result[i] = Double.parseDouble(items[i]);
                                                } catch (NumberFormatException nfe) {
                                                    //Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            // Writing data from a database
                                            Object object = new Object();
                                            object.setId(cursor.getInt(idIndex));
                                            object.setName(cursor.getString(nameIndex));
                                            object.setAddress(cursor.getString(addressIndex));
                                            object.setSalesPlan(cursor.getInt(salesPlanIndex));
                                            object.setCurrentSales(result);
                                            object.setTotalCurrentSales(cursor.getDouble(totalCurrentSalesIndex));
                                            mObjects.add(object);

                                        } while (cursor.moveToNext());
                                    }

                                    cursor.close();

                                    if (mObjects != null) {

                                        // View ArrayList<Object>
                                        for (int i = 0; mObjects.size() > i; i++ ) {
                                            Log.i(TAG, "Id = " + mObjects.get(i).getId());
                                            Log.i(TAG, "Name = " + mObjects.get(i).getName());
                                            Log.i(TAG, "Address = " + mObjects.get(i).getAddress());
                                            Log.i(TAG, "CurrentSales = " + Arrays.toString(mObjects.get(i).getCurrentSales()));
                                            Log.i(TAG, "TotalCurrentSales = " + mObjects.get(i).getTotalCurrentSales());
                                            Log.i(TAG, "SalesPlan = " + mObjects.get(i).getSalesPlan());
                                            Log.i(TAG, "...............................................");
                                        }
                                    } else {

                                        Log.i(TAG, "No items in ArrayList<Object>");
                                    }

                                    setupAdapter();
                                }
                            });

                    snackbar.setActionTextColor(Color.RED);
                    snackbar.show();
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

            setupAdapter();

            mProgressBar.setVisibility(View.GONE);

            objectDbHelper = new ObjectDbHelper(getContext());
            SQLiteDatabase database = objectDbHelper.getWritableDatabase();

            database.delete(objectDbHelper.TABLE_OBJECTS, null, null);

            ContentValues contentValues = new ContentValues();

            // Writing to the database
            for (int i = 0; mObjects.size() > i; i++) {
                contentValues.put(objectDbHelper.KEY_ID, mObjects.get(i).getId());
                contentValues.put(objectDbHelper.KEY_NAME, mObjects.get(i).getName());
                contentValues.put(objectDbHelper.KEY_ADDRESS, mObjects.get(i).getAddress());
                contentValues.put(objectDbHelper.KEY_SALESPLAN, mObjects.get(i).getSalesPlan());
                contentValues.put(objectDbHelper.KEY_CURRENTSALES, Arrays.toString(mObjects.get(i).getCurrentSales()));
                contentValues.put(objectDbHelper.KEY_TOTALCURRENTSALES, mObjects.get(i).getTotalCurrentSales());

                database.insert(objectDbHelper.TABLE_OBJECTS, null, contentValues);
            }

            viewElementsDb();
        }
    }
}
