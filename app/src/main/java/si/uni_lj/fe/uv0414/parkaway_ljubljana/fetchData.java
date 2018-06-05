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
    ArrayList<String[]> dataList = new ArrayList<String[]>();
    private static final String TAG = "SPOROCILO";
    JSONArray JA;
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://api.myjson.com/bins/1ejytu");

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
                String [] dataParsed = new String[7];
                JSONObject JO = (JSONObject) JA.get(i);
                dataParsed[0] = (String) JO.get("name");
                dataParsed[1] = (String) JO.get("description");
                dataParsed[2] = (String) JO.get("image_path");
                dataParsed[3] = (String) JO.get("num_slots");
                dataParsed[4] = (String) JO.get("price");
                dataParsed[5] = (String) JO.get("lat");
                dataParsed[6] = (String) JO.get("lng");

                dataList.add(dataParsed);
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
        Log.i(TAG, dataList.toString());
        MainActivity.closest_name.setText(this.dataList.get(1)[0]);
       // MainActivity.closest_image.setImageURI(this.dataList.get(0)[0]);
       // MainActivity.closest_distance.setText(this.dataList.get(0)[0]);
        MainActivity.closest_num_slots.setText(this.dataList.get(1)[3] + " mest");
    }
}
