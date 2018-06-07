package si.uni_lj.fe.uv0414.parkaway_ljubljana;

import android.os.Parcel;
import android.os.Parcelable;

/** Class, ki določa lastnosti parkinga: geografski položaj, število parkirnih mest, ime parkirišča, sliko parkirišča.
 *  Morebitne težave/potrebni popravki tipa spremenljivke za latitude in longitude
 */

public class Parking implements Parcelable {
    private String mPhoto;
    private String mParkingName;
    private double mLatitude;
    private double mLongitude;
    private int mNumberOfPlaces;
    private String mDescription;
    private int mDistance;

    // konstruktor za parking, ki je najbližji
    /*public Parking(String photo, String parkingName, double latitude, double longitude, int numberOfPlaces, String description) {
       mPhoto = photo;
       mParkingName = parkingName;
       mLatitude = latitude;
       mLongitude = longitude;
       mNumberOfPlaces = numberOfPlaces;
       mDescription = description;
    }*/

    // konstruktor za seznam parkingov, ki jim podamo še razdaljo od trenutnega najbližjega parkinga
    public Parking(String photo, String parkingName, double latitude, double longitude, int numberOfPlaces, String description, int distance) {
        mPhoto = photo;
        mParkingName = parkingName;
        mLatitude = latitude;
        mLongitude = longitude;
        mNumberOfPlaces = numberOfPlaces;
        mDescription = description;
        mDistance = distance;
    }

    public String getPhoto() {
       return mPhoto;
    }

    // getter, ki vrne ime parkirišča
    public String getParkingName() {
       return mParkingName;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    // getter, ki vrne število parkirnih mest na parkirišču
    public int getNumberOfPlaces() {
       return mNumberOfPlaces;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getDistance() { return mDistance; }

    // Potrebni setterji? Zaenkrat mislim da ne, da se bo itak določlo ob klicu konstruktorja lastnosti posameznega parkplaca.
    public void setDistance (int distance) {
        mDistance = distance;
    }

    // Parcelable del
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mPhoto);
        out.writeString(mParkingName);
        out.writeDouble(mLatitude);
        out.writeDouble(mLongitude);
        out.writeInt(mNumberOfPlaces);
        out.writeString(mDescription);
        out.writeInt(mDistance);
        // vrjetno ni potrebno dodat še mDistance, ker le-tega ne podajamo med intenti
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Parking> CREATOR = new Parcelable.Creator<Parking>() {
        public Parking createFromParcel(Parcel in) {
            return new Parking(in);
        }

        public Parking[] newArray(int size) {
            return new Parking[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Parking(Parcel in) {
        mPhoto = in.readString();
        mParkingName = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mNumberOfPlaces = in.readInt();
        mDescription = in.readString();
        mDistance = in.readInt();
    }
}
