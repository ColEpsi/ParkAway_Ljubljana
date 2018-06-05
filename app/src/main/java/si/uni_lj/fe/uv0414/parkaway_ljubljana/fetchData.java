package si.uni_lj.fe.uv0414.parkaway_ljubljana;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class fetchData extends AsyncTask<Void, Void, Void> {
    String data;
    ArrayList<Parking> parkings = new ArrayList<Parking>();
    private static final String TAG = "SPOROCILO";
    JSONArray JA;
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://api.myjson.com/bins/isare");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null){
                line = bufferedReader.readLine();
                data += line;
            }
            data = data.substring(4);
            JA = new JSONArray(data);

            for (int i = 0; i < JA.length(); i++){
                JSONObject JO = (JSONObject) JA.get(i);

                parkings.add(new Parking((int)JO.get("image_path"), (String)JO.get("name"), (double)JO.get("lat"),(double) JO.get("lng"), (int)JO.get("num_slots")));

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

        MainActivity.parkings = parkings;
    }
}
