package si.uni_lj.fe.uv0414.parkaway_ljubljana;

import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button osvezi;
    public static TextView closest_name;
    public static ImageView closest_image;
    public static TextView closest_distance;
    public static TextView closest_num_slots;
    fetchData process = new fetchData();

    public static float device_lat;
    public static float device_lng;

    private LocationManager locationManager;
    private LocationListener locationListener;

    public static ArrayList<String[]> data;

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

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                device_lat = (float) location.getLatitude();
                device_lng = (float) location.getLongitude();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
            return;
        } else {
            configureButton();
        }
        process.execute();

        // Na tem mestu mamo upolrabnikovo lokacijo in podatke o vseh parkiriščih.


        osvezi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process.execute();
            }
        });

        closest_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent parkingInfoIntent = new Intent(MainActivity.this, ParkingActivity.class);
                // TODO s tem lahko preneseš Parking objekt (najbližji parking) v ParkingActivity.java, tako da ni potrebno tam potem še enkrat vseh data klicat itd.
                //parkingInfoIntent.putExtra("PARKING", parking);
                startActivity(parkingInfoIntent);
            }
        });

        // ustvari seznam najbližjih parkirišč TODO naj bo dinamično, TODO naj bo razvrščeno od najbližjega do najbolj oddaljenega
        ArrayList<Parking> parkings = new ArrayList<Parking>();

        //for (int i = 0; i < data.size(); i++) {
            //parkings.add(new Parking(data.get(i)[2], data.get(i)[0], data.get(i)[5], data.get(i)[6], data.get(i)[3]));
        //}

        parkings.add(new Parking(R.drawable.ilirija, "Parking Mirje", 13.4, 13.4, 10, "opis"));
        parkings.add(new Parking(R.drawable.ilirija, "Parking Tivoli", 123.3, 123.4, 100, "opis"));
        parkings.add(new Parking(R.drawable.ilirija, "Parking Mirje", 13.4, 13.4, 10, "opis"));
        parkings.add(new Parking(R.drawable.ilirija, "Parking Tivoli", 123.3, 123.4, 100, "opis"));
        parkings.add(new Parking(R.drawable.ilirija, "Parking Mirje", 13.4, 13.4, 10, "opis"));
        parkings.add(new Parking(R.drawable.ilirija, "Parking Tivoli", 123.3, 123.4, 100, "opis"));
        parkings.add(new Parking(R.drawable.ilirija, "Parking Mirje", 13.4, 13.4, 10, "opis"));

        ParkingAdapter adapter = new ParkingAdapter(this, parkings);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        Button map = (Button) findViewById(R.id.zemljevid_button);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showMapIntent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(showMapIntent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;
        }
    }

    private void configureButton() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 5, locationListener);
    }

    private double distanceTo(Parking parking) {
        double currentLat = device_lat;
        double currentLng = device_lng;
        double parkingLat = parking.getLatitude();
        double parkingLng = parking.getLongitude();
        final int R = 6371; // Radius of the earth

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
