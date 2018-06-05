package si.uni_lj.fe.uv0414.parkaway_ljubljana;

import android.content.Intent;
import android.media.Image;
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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO dobi trenutno lokacijo uporabnika in katero parkirišče mu je najbližje;
        // TODO hkrati izračunaj tudi razdalje do ostalih parkirišč in primerno inflateaj spodi seznam po razdalji

        // Zgornji del začetnega zaslona, slika in podatki o najbližjem parkirišču
        ImageView closest = (ImageView) findViewById(R.id.closest_parking_image_view);
        // TODO nastavi sliko trenutnega najbližjega
        // closest.setImageResource();
        closest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent parkingInfoIntent = new Intent(MainActivity.this, ParkingActivity.class);
                startActivity(parkingInfoIntent);
            }
        });
        TextView razdaljaDoNajblizjegaTextView = (TextView) findViewById(R.id.distance_closest_text_view);
        //razdaljaDoNajblizjegaTextView.setText();
        TextView najblizjiTextView = (TextView) findViewById(R.id.name_closest_text_view);
        //najblizjiTextView.setText();
        TextView steviloMestNajblizjegaTextView = (TextView) findViewById(R.id.nr_of_places_closest_text_view);
        //steviloMestNajblizjegaTextView.setText();

        // ustvari seznam najbližjih parkirišč TODO naj bo dinamično, TODO naj bo razvrščeno od najbližjega do najbolj oddaljenega
        ArrayList<Parking> parkings = new ArrayList<Parking>();
        parkings.add(new Parking(R.drawable.ilirija, "Parking Mirje", 13.4, 13.4, 10));
        parkings.add(new Parking(R.drawable.ilirija, "Parking Tivoli", 123.3, 123.4, 100));
        parkings.add(new Parking(R.drawable.ilirija, "Parking Mirje", 13.4, 13.4, 10));
        parkings.add(new Parking(R.drawable.ilirija, "Parking Tivoli", 123.3, 123.4, 100));
        parkings.add(new Parking(R.drawable.ilirija, "Parking Mirje", 13.4, 13.4, 10));
        parkings.add(new Parking(R.drawable.ilirija, "Parking Tivoli", 123.3, 123.4, 100));
        parkings.add(new Parking(R.drawable.ilirija, "Parking Mirje", 13.4, 13.4, 10));
        parkings.add(new Parking(R.drawable.ilirija, "Parking Tivoli", 123.3, 123.4, 100));
        parkings.add(new Parking(R.drawable.ilirija, "Parking Mirje", 13.4, 13.4, 10));
        parkings.add(new Parking(R.drawable.ilirija, "Parking Tivoli", 123.3, 123.4, 100));
        parkings.add(new Parking(R.drawable.ilirija, "Parking Mirje", 13.4, 13.4, 10));
        parkings.add(new Parking(R.drawable.ilirija, "Parking Tivoli", 123.3, 123.4, 100));

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
}
