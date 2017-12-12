package com.sizov.vitaly.salesplan;

import android.os.AsyncTask;

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

public class FetchData extends AsyncTask<Void, Void, Void> {

    private String data = "";
    private String dataParsed = "";
    private String singleParsed = "";
    private String s = "";
    private ArrayList<Object> mObjects;

    @Override
    protected Void doInBackground(Void... voids) {

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

            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                mObjects = new ArrayList<>();
                Object object = new Object();
                object.setId(jsonObject.getInt("id"));
                object.setName((String) jsonObject.get("Name"));
                object.setAddress((String) jsonObject.get("Address"));
                object.setSalesPlan(jsonObject.getInt("Sales Plan"));


                JSONArray salesArr = (JSONArray) jsonObject.get("Current Sales");
                for(int j = 0; j < salesArr.length(); j++) {

                    double element = salesArr.getDouble(j);
                    double[] arr = new double[salesArr.length()];
                    arr[j] = element + 0;

                    object.setCurrentSales(arr);
                }


                mObjects.add(object);

                singleParsed = "ID: " + jsonObject.get("id") + "\n" + "Name: " + jsonObject.get("Name") + "\n" +
                "Address: " + jsonObject.get("Address") + "\n" +
                        "Current Sales: " + jsonObject.get("Current Sales") + "\n" +
                        "Sales Plan: " + jsonObject.get("Sales Plan") + "$" + "\n";

                dataParsed = dataParsed + singleParsed + "\n";
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
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        ObjectsFragment.mFetchText.setText(this.dataParsed);

    }
}
