package si.uni_lj.fe.uv0414.parkaway_ljubljana;

import android.content.Intent;
import android.location.LocationProvider;
import android.media.Image;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity implements DownloadCallback {
    private FusedLocationProviderClient client;

    private static final String TAG = "SPOROCILO";
    Button osvezi;
    public static TextView closest_name;
    public static ImageView closest_image;
    public static TextView closest_distance;
    public static TextView closest_num_slots;
    public static ListView listView;

    public static double device_lat;
    public static double device_lng;
    public static String jsonUrl = "https://api.myjson.com/bins/o2ndu";


    public static ArrayList<Parking> parkings = new ArrayList<Parking>();
    public static ArrayList<Parking> parkings_sorted = new ArrayList<Parking>();
    ArrayList<int[]> distance_ranking = new ArrayList<int[]>();

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        osvezi = (Button) findViewById(R.id.osvezi_button);
        closest_name = (TextView) findViewById(R.id.name_closest_text_view);
        closest_image = (ImageView) findViewById(R.id.closest_parking_image_view);
        closest_distance = (TextView) findViewById(R.id.distance_closest_text_view);
        closest_num_slots = (TextView) findViewById(R.id.nr_of_places_closest_text_view);
        listView = (ListView) findViewById(R.id.list);

        requestPermission();

        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    device_lat = location.getLatitude();
                    device_lng = location.getLongitude();
                }
            }
        });


        new DownloadJsonTask(this).execute(jsonUrl);
        //process.execute();


//        osvezi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                process.execute();
//            }
//        });

        // dummy parkings Luka
        // parkings.add(new Parking("R.drawable.ilirija", "Ilirija parking", 1.23, 199.0, 20, "Opis Ilirije parkinga. Cena: 4.2€/h."));
        // parkings.add(new Parking("R.drawable.langusova", "Parkirišče na Langusovi", 2.4, 100, 10, "Zelo luštno parkirišče. Cena: poceni."));
        // parkings.add(new Parking("R.drawable.mencingerjeva", "Mencingerjevo parkirišče", 4, -1.23, 1, "To parkirišče pripada Mencingerju. Stay away to not get him mad."));

        closest_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent parkingInfoIntent = new Intent(MainActivity.this, ParkingActivity.class);
                // prenašanja Parking objekta (najbližji parking) v ParkingActivity.java
                // da bi lahko prenašal object med activityji, ga moreš implementirat kot Parcelable.
                parkingInfoIntent.putExtra("PARKING", (Parcelable) parkings.get(distance_ranking.get(0)[1]));
                startActivity(parkingInfoIntent);
            }
        });


        Button map = (Button) findViewById(R.id.zemljevid_button);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showMapIntent = new Intent(MainActivity.this, MapsActivity.class);
                showMapIntent.putExtra("ArrayListOfParkings", parkings);
                startActivity(showMapIntent);
            }
        });
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }


    @Override
    public void updateFromDownload(Object result) throws JSONException {
        String json = (String) result;
        Log.i(TAG, json);

        //Parsamo String v objekte parking
        JSONArray JA = new JSONArray(json);

        for (int i = 0; i < JA.length(); i++){
            JSONObject JO = (JSONObject) JA.get(i);

            parkings.add(new Parking((String)JO.get("image_path"), (String)JO.get("name"), (double)JO.get("lat"),(double) JO.get("lng"), (int)JO.get("num_slots"), (String)JO.get("description"), 0 ));

        }

        //razvrstimo objekte po razdalji
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

        //naredimo ArrayList sortiranih parkingov
        for (int i = 1; i < distance_ranking.size(); i++){
            Log.i(TAG, String.valueOf(distance_ranking.get(i)[0]));
            parkings_sorted.add(parkings.get(distance_ranking.get(i)[1]));
            parkings_sorted.get(parkings_sorted.size() - 1).setDistance(distance_ranking.get(i)[0]);
        }

        //ocitno moramo podatke nastavit tukaj notri in ne v oncreate ker jih takrat se nima....
        // Če je razdalja večja od 999, pretvori v km.
        if (distance_ranking.get(0)[0] > 999) {
            int dist = distance_ranking.get(0)[0];
            dist = dist / 1000;
            closest_distance.setText(String.valueOf(dist + "km"));
        } else {
            closest_distance.setText(String.valueOf(distance_ranking.get(0)[0]) + "m");
        }
        closest_name.setText(parkings.get(distance_ranking.get(0)[1]).getParkingName());
        closest_num_slots.setText(String.valueOf(parkings.get(distance_ranking.get(0)[1]).getNumberOfPlaces())+ " mest");
        closest_image.setImageURI(Uri.parse("android.resource://si.uni_lj.fe.uv0414.parkaway_ljubljana/drawable/"+parkings.get(distance_ranking.get(0)[1]).getPhoto()));

        // adapter ne sprejme "parkings", ampak razvrščen seznam parkingov.
        // vrjetno najboljše da se nardi nov ArrayList<Parking> element, v katerega po velikosti s pomočjo distance_ranking.get... vstaviš parkinge.
        ParkingAdapter adapter = new ParkingAdapter(this, parkings_sorted);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        // on click metoda za listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ParkingActivity.class);
                // prenašanje Parking objekta (najbližji parking) v ParkingActivity.java
                // da bi lahko prenašal object med activityji, ga moreš implementirat kot Parcelable.
                intent.putExtra("PARKING", (Parcelable) parkings_sorted.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        return null;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {

    }

    @Override
    public void finishDownloading() {

    }
    private double distanceTo(Parking parking) {
        double currentLat = device_lat;
        double currentLng = device_lng;
        double parkingLat = parking.getLatitude();
        double parkingLng = parking.getLongitude();
        final int R = 6371; // Radius of the earth
        Log.i(TAG, device_lat + " " + device_lng);
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
