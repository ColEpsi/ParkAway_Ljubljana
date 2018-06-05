package si.uni_lj.fe.uv0414.parkaway_ljubljana;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class ParkingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);

        // Nastavi sliko
        ImageView slikaParkingaImageView = (ImageView) findViewById(R.id.parking_image_view);
        //slikaParkingaImageView.setImageResource();

        TextView imeParkiriscaTextView = (TextView) findViewById(R.id.parking_name_text_view);
        //imeParkiriscaTextView.setText();

        TextView opisParkiriscaTextView = (TextView) findViewById(R.id.parking_description_text_view);
        //opisParkiriscaTextView.setText();
    }
}
