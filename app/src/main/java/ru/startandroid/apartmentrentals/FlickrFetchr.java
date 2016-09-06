package ru.startandroid.apartmentrentals;

import android.net.Uri;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FlickrFetchr {

    private static final String TAG = "FlickrFetchr";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        /*URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }*/

        HttpClient httpclient = new DefaultHttpClient();
        // make GET request to the given URL
        HttpGet httpget = new HttpGet(urlSpec);
        httpget.setHeader("Accept", "application/json");
        httpget.setHeader("Content-Type", "application/json");
        //httpget.setHeader("Content-Type", "charset=utf-8");
        HttpResponse httpResponse = httpclient.execute(httpget);

        // receive response as inputStream
        InputStream in = httpResponse.getEntity().getContent();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int bytesRead = 0;
        byte[] buffer = new byte[1024];
        while ((bytesRead = in.read(buffer)) > 0) {
            out.write(buffer, 0, bytesRead);
        }
        out.close();
        in.close();
        return out.toByteArray();
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<ApartmentItem> fetchItems() {
        List<ApartmentItem> items = new ArrayList<>();
        try {
            Log.i(TAG, "fetchItems");
            String urlFirst = Uri.parse("https://ak.api.onliner.by/apartments")
                    .buildUpon()
                    .appendQueryParameter("page", "1")
                    .build().toString();
            Log.i(TAG, "Received URL: " + urlFirst);
            String jsonFirst = getUrlString(urlFirst);

            JSONObject firstJSONObject = new JSONObject(jsonFirst);
            String lastPage = firstJSONObject.getJSONObject("page").getString("last");
            String url = Uri.parse("https://ak.api.onliner.by/apartments")
                    .buildUpon()
                    .appendQueryParameter("page", lastPage)
                    .build().toString();

            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (JSONException je){
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        return items;
    }

    private void parseItems(List<ApartmentItem> items, JSONObject jsonBody) throws JSONException{

        JSONArray apartmentsJSONArray = jsonBody.getJSONArray("apartments");
        for(int i = apartmentsJSONArray.length() - 1; i >= 0; i--){
            JSONObject apartmJSONObject = apartmentsJSONArray.getJSONObject(i);
            ApartmentItem item = new ApartmentItem();
            item.setId(apartmJSONObject.getString("id"));

            JSONObject priceJSONObject = apartmJSONObject.getJSONObject("price");
            JSONObject convertedJSONObject = priceJSONObject.getJSONObject("converted");
            JSONObject BYRJSONObject = convertedJSONObject.getJSONObject("BYR");
            item.setCurrencyBYR(BYRJSONObject.getString("currency"));
            item.setAmountBYR(BYRJSONObject.getString("amount"));

            JSONObject BYNJSONObject = convertedJSONObject.getJSONObject("BYN");
            item.setCurrencyBYN(BYNJSONObject.getString("currency"));
            item.setAmountBYN(BYNJSONObject.getString("amount"));

            JSONObject USDJSONObject = convertedJSONObject.getJSONObject("USD");
            item.setCurrencyUSD(USDJSONObject.getString("currency"));
            item.setAmountUSD(USDJSONObject.getString("amount"));

            item.setRentType(apartmJSONObject.getString("rent_type"));

            JSONObject locationJSONObject = apartmJSONObject.getJSONObject("location");
            item.setAddress(locationJSONObject.getString("address"));

            item.setPhotoUrl(apartmJSONObject.getString("photo"));
            item.setWebUrl(apartmJSONObject.getString("url"));

            items.add(item);
        }

    }

    public List<ApartmentItem> fetchOldItems(int i) {
        List<ApartmentItem> items = new ArrayList<>();
        try {
            Log.i(TAG, "fetchOldItems");
            String urlFirst = Uri.parse("https://ak.api.onliner.by/apartments")
                    .buildUpon()
                    .appendQueryParameter("page", "1")
                    .build().toString();
            Log.i(TAG, "Received URL: " + urlFirst);
            String jsonFirst = getUrlString(urlFirst);

            JSONObject firstJSONObject = new JSONObject(jsonFirst);
            String lastPage = firstJSONObject.getJSONObject("page").getString("last");
            int prev = Integer.parseInt(lastPage) - i;
            String url = Uri.parse("https://ak.api.onliner.by/apartments")
                    .buildUpon()
                    .appendQueryParameter("page", String.valueOf(prev))
                    .build().toString();
            Log.d("ZHEKA", " fetchOldItems - " + url);
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (JSONException je){
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        return items;
    }
}

