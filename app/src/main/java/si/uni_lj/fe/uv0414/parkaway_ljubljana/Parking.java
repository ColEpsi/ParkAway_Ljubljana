package si.uni_lj.fe.uv0414.parkaway_ljubljana;

/** Class, ki določa lastnosti parkinga: geografski položaj, število parkirnih mest, ime parkirišča, sliko parkirišča.
 *  Morebitne težave/potrebni popravki tipa spremenljivke za latitude in longitude
 */

public class Parking {
    private int mPhoto;
    private String mParkingName;
    private double mLatitude;
    private double mLongitude;
    private int mNumberOfPlaces;

    public Parking(int photo, String parkingName, double latitude, double longitude, int numberOfPlaces) {
       mPhoto = photo;
       mParkingName = parkingName;
       mLatitude = latitude;
       mLongitude = longitude;
       mNumberOfPlaces = numberOfPlaces;
    }

    public int getPhoto() {
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

    // Potrebni setterji? Zaenkrat mislim da ne, da se bo itak določlo ob klicu konstruktorja lastnosti posameznega parkplaca.
}
