package si.uni_lj.fe.uv0414.parkaway_ljubljana;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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

public class fetchData extends AsyncTask<Void, Void, Void> {
    String data;
    ArrayList<Parking> parkings = new ArrayList<Parking>();
    ArrayList<int[]> distance_ranking = new ArrayList<int[]>();
    private static final String TAG = "SPOROCILO";
    JSONArray JA;
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://api.myjson.com/bins/o2ndu");

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

                parkings.add(new Parking((String)JO.get("image_path"), (String)JO.get("name"), (double)JO.get("lat"),(double) JO.get("lng"), (int)JO.get("num_slots"), (String)JO.get("description") ));

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Na tem mestu mamo upolrabnikovo lokacijo in podatke o vseh parkiriščih.

        //izracunamo razdalje in jih rangiramo
        int temp_min_distance = -1;
        for (int i = 0; i < parkings.size(); i++){
            int temp_min = 10000000;
            int temp_index = -1;
            for (int j = 0; j < parkings.size(); j++){
                int distance = (int) distanceTo(parkings.get(j));
                if (distance < temp_min && temp_min_distance < distance){
                    temp_min = distance;
                    temp_index = j;
                }
            }
            distance_ranking.add(new int[]{temp_min, temp_index});
            temp_min_distance = temp_min;
        }



        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
//        ParkingAdapter adapter = new ParkingAdapter(, parkings);
//        MainActivity.listView.setAdapter(adapter);
        Log.i(TAG, String.valueOf(distance_ranking.get(0)[0]));
        MainActivity.closest_distance.setText(String.valueOf(distance_ranking.get(0)[0]) + "m");
        MainActivity.closest_name.setText(parkings.get(distance_ranking.get(0)[1]).getParkingName());
        MainActivity.closest_num_slots.setText(String.valueOf(parkings.get(distance_ranking.get(0)[1]).getNumberOfPlaces())+ " mest");
        MainActivity.closest_image.setImageURI(Uri.parse("android.resource://si.uni_lj.fe.uv0414.parkaway_ljubljana/drawable/"+parkings.get(distance_ranking.get(0)[1]).getPhoto()));
        MainActivity.parkings = parkings;

    }
    private double distanceTo(Parking parking) {
        double currentLat = MainActivity.device_lat;
        double currentLng = MainActivity.device_lng;
        double parkingLat = parking.getLatitude();
        double parkingLng = parking.getLongitude();
        final int R = 6371; // Radius of the earth
        Log.i(TAG, MainActivity.device_lat + " " + MainActivity.device_lng);
        double latDistance = Math.toRadians(parkingLat - currentLat);
        double lonDistance = Math.toRadians(parkingLng - currentLng);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(currentLat)) * Math.cos(Math.toRadians(parkingLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        return distance;
    }
}
