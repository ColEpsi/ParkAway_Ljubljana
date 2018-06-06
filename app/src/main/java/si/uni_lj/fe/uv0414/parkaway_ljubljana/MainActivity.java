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
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient client;

    private static final String TAG = "SPOROCILO";
    Button osvezi;
    public static TextView closest_name;
    public static ImageView closest_image;
    public static TextView closest_distance;
    public static TextView closest_num_slots;
    public static ListView listView;
    fetchData process = new fetchData();

    public static double device_lat;
    public static double device_lng;


    public static ArrayList<Parking> parkings;

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



        process.execute();


//        osvezi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                process.execute();
//            }
//        });

        closest_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent parkingInfoIntent = new Intent(MainActivity.this, ParkingActivity.class);
                startActivity(parkingInfoIntent);
            }
        });


        Button map = (Button) findViewById(R.id.zemljevid_button);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showMapIntent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(showMapIntent);
            }
        });
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }


}
