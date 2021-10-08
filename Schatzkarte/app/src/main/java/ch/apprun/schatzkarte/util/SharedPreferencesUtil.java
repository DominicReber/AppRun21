package ch.apprun.schatzkarte.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class helps to save certain properties in the shared preferences
 *
 * @author Kai Moser
 */
public class SharedPreferencesUtil {
    private static final String PROPERTY_NAME_LATITUDE = "latitude";
    private static final String PROPERTY_NAME_LONGITUDE = "longitude";

    private final SharedPreferences sharedPref;

    /**
     * @param context Context/Activity
     */
    public SharedPreferencesUtil(Context context) {
        sharedPref = context.getSharedPreferences("schatzkarte", Context.MODE_PRIVATE);
    }

    /**
     * Saves the given latitude in shared preferences
     *
     * @param latitude Latitude to be saved in shared preferences
     */
    public void setLatitude(double latitude) {
        sharedPref.edit()
                .putString(PROPERTY_NAME_LATITUDE, String.valueOf(latitude))
                .apply();
    }

    /**
     * Retrieves Latitude from shared preferences and returns it
     *
     * @return Latitude from shared preferences
     */
    public double getLatitude() {
        String value = sharedPref.getString(PROPERTY_NAME_LATITUDE, null);
        return Double.parseDouble(value);
    }

    /**
     * Saves the given Longitude in shared preferences
     *
     * @param longitude Longitude to be saved in shared preferences
     */
    public void setLongitude(double longitude) {
        sharedPref.edit()
                .putString(PROPERTY_NAME_LONGITUDE, String.valueOf(longitude))
                .apply();
    }

    /**
     * Retrieves Longitude from shared preferences and returns it
     *
     * @return Longitude from shared preferences
     */
    public double getLongitude() {
        String value = sharedPref.getString(PROPERTY_NAME_LONGITUDE, null);
        return Double.parseDouble(value);
    }
}
