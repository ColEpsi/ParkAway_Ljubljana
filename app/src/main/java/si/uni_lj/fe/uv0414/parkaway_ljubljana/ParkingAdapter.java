package si.uni_lj.fe.uv0414.parkaway_ljubljana;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import si.uni_lj.fe.uv0414.parkaway_ljubljana.Parking;
import si.uni_lj.fe.uv0414.parkaway_ljubljana.R;

public class ParkingAdapter extends ArrayAdapter<Parking> {

    public ParkingAdapter(Context context, ArrayList<Parking> parkings) {
        super(context, 0, parkings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.parking_item, parent, false
            );
        }
        // Get the Parking object located at this position in the list
        Parking currentParking = getItem(position);
        // Find the TextView in the parking_item.xml layout with the ID distance_text_view
        TextView distanceTextView = (TextView) listItemView.findViewById(R.id.distance_text_view);
        // Get the distance to the parking in the list
        // Treba še tam napisat funkcijo ki zračuna razdaljo med trenutno lokacijo in lokacijo tega parkinga
        if (currentParking != null) {
            distanceTextView.setText(Integer.toString(currentParking.getDistance()));
        }

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.others_text_view);
        nameTextView.setText(currentParking.getParkingName());

        return listItemView;
    }
}
