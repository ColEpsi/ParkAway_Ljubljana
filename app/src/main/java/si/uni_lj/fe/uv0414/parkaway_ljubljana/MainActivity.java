package si.uni_lj.fe.uv0414.parkaway_ljubljana;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
