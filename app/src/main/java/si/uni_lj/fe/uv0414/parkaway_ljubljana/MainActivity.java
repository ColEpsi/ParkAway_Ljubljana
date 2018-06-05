package si.uni_lj.fe.uv0414.parkaway_ljubljana;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button osvezi;
    public static TextView closest_name;
    public static ImageView closest_image;
    public static TextView closest_distance;
    public static TextView closest_num_slots;
    fetchData process = new fetchData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        osvezi = (Button) findViewById(R.id.osvezi_button);
        closest_name = (TextView) findViewById(R.id.name_closest_text_view);
        closest_image = (ImageView) findViewById(R.id.closest_parking_image_view);
        closest_distance = (TextView) findViewById(R.id.distance_closest_text_view);
        closest_num_slots = (TextView) findViewById(R.id.nr_of_places_closest_text_view);


        process.execute();

        osvezi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process.execute();
            }
        });

        ImageView closest = (ImageView) findViewById(R.id.closest_parking_image_view);

        closest.setOnClickListener(new View.OnClickListener() {
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
}
