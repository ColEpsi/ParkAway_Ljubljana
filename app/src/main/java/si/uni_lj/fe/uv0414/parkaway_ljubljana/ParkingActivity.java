package si.uni_lj.fe.uv0414.parkaway_ljubljana;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ParkingActivity extends AppCompatActivity implements OnMapReadyCallback{

    ImageView slikaParkingaImageView;
    TextView imeParkiriscaTextView;
    TextView opisParkiriscaTextView;
    Button navodilaZaPotButton;
    String ime;
    private GoogleMap mMap;
    private Parking parking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.parking_activity_map);
        mapFragment.getMapAsync(this);

        // Pridobivanje podatkov iz putExtras v MainActivityju ob klicanju Intenta
        Intent i = getIntent();
        parking = (Parking) i.getParcelableExtra("PARKING");
        Log.i("TST", parking.getDescription());

        // Nastavi sliko parkinga ki ga pridobiš z intentom
        // Potrebno je še convertat iz Stringa ki ga dobimo iz JSONa v int id
        Resources res = getResources();
        String mDrawableName = parking.getPhoto();
        Log.i("PhotoName", mDrawableName);
        int resID = res.getIdentifier(mDrawableName, "drawable", getPackageName());
        slikaParkingaImageView = (ImageView) findViewById(R.id.parking_image_view);
        slikaParkingaImageView.setImageResource(resID);

        // TODO spremenljivka parking tipa Parking, parking.getParkingName() vstaviš v setText(...)
        imeParkiriscaTextView = (TextView) findViewById(R.id.parking_name_text_view);
        imeParkiriscaTextView.setText(parking.getParkingName());

        // parking.getDescription() vstaviš v opisParkiriscaTextView.setText(...)
        opisParkiriscaTextView = (TextView) findViewById(R.id.parking_description_text_view);
        opisParkiriscaTextView.setText(parking.getDescription());

        // Implicit intent za navodilo za pot do gledanega parkirišča
        navodilaZaPotButton = (Button) findViewById(R.id.directions_button);
        // ime parkirišča
        ime = imeParkiriscaTextView.getText().toString();
        navodilaZaPotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri intentUri = Uri.parse("google.navigation:q=" + ime + ",+Ljubljana&mode=d");
                Intent showDirections = new Intent(Intent.ACTION_VIEW, intentUri);
                showDirections.setPackage("com.google.android.apps.maps");
                try {
                    startActivity(showDirections);
                } catch(ActivityNotFoundException ex) {
                    try {
                        Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, intentUri);
                        startActivity(unrestrictedIntent);
                    } catch (ActivityNotFoundException innerEx) {
                        Toast.makeText(ParkingActivity.this, "Prosimo, namestite aplikacijo Google Maps.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().isZoomGesturesEnabled();
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(parking.getLatitude(), parking.getLongitude()))
                .anchor(0.5f, 0.5f)
                .title(parking.getParkingName())
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(parking.getLatitude(),
                        parking.getLongitude()),15));
    }
}