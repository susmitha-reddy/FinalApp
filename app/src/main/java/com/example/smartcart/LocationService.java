package com.example.smartcart;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class LocationService extends AsyncTask<String, Void, String> {

    public interface AsyncResponse{
        void processFinish(ArrayList<Store> output);
    }

    public AsyncResponse delegate = null;



    protected String doInBackground(String... urls) {
        String radius = urls[0];
        String lat = urls[1];
        String lng = urls[2];
        String result= "";
        HttpURLConnection urlConnection = null;
        try {
            URL url = buildurl(radius,lat,lng);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader isw = new InputStreamReader(in);
            int data = isw.read();
            while (data != -1) {
                result += (char) data;
                data = isw.read();

            }
            Log.v("Dismay",result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return result;
    }

    protected void onPostExecute(String result) {
        JSONObject reader;
        ArrayList<Store> list = new ArrayList<>();

        {
            try {
                reader = new JSONObject(result);
                JSONObject response = reader.getJSONObject("response");
                JSONArray venues = response.getJSONArray("venues");
                for (int i=0;i < venues.length();i++){
                    JSONObject store = venues.getJSONObject(i);
                    JSONObject location = store.getJSONObject("location");
                    Store s = new Store(store.getString("name"),location.getString("postalCode"),location.getDouble("distance"), location.getString("formattedAddress"),location.getDouble("lat"),
                            location.getDouble("lng"));
                    list.add(s);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        delegate.processFinish(list);
    }

    public URL buildurl(String radius, String lat, String lng) throws MalformedURLException {
        URL url = new URL("https://api.foursquare.com/v2/venues/search?client_id=SR4VUPWVMDSST2FBC3N0WLRCHXBF4KMTCOIRLFB1LPLMWT0T&client_secret=S0LQW3NRAEPZ4RO0YHS2JZ0WNJGJKS0KBIANTYYXT02QTQDB&" +
                "v=20180323&ll=" + lat +"," + lng+"&"+
                "radius="+ radius +"000&"+
                "categoryId=52f2ab2ebcbc57f1066b8b46");
        Log.d("URL",String.valueOf(url));

        return url;

    }

}

