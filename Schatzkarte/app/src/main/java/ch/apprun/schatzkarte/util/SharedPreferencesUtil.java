package ch.apprun.schatzkarte.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.apprun.schatzkarte.model.Coordinate;

/**
 * This class helps to save certain properties in the SharedPreferences
 *
 * @author Kai Moser
 */
public class SharedPreferencesUtil {
    private List<String> uuidList;

    private final SharedPreferences sharedPref;

    /**
     * @param context Context/Activity
     */
    public SharedPreferencesUtil(Context context) {
        sharedPref = context.getSharedPreferences("schatzkarte", Context.MODE_PRIVATE);
        uuidList = new ArrayList<>();
    }

    /**
     * Saves saves a new coordinate in the SharedPreferences and allocates them with a random UUID. The UUID
     * is returned and can later be used to retrieve this coordinate.
     *
     * @param coordinate coordinate to be saved
     * @return random uuid allocated to the saved coordinates
     */
    public String addCoordinate(Coordinate coordinate) {
        String uuid = UUID.randomUUID().toString();
        sharedPref.edit().putString(uuid, coordinate.getLatitude() + "," + coordinate.getLongitude()).apply();
        uuidList.add(uuid);
        return uuid;
    }

    /**
     * Retrieves the coordinate allocated to the given uuid from the SharedPreferences.
     *
     * @param uuid identifier of the coordinate to look for
     * @return Retrieved coordinate (null if there is no coordinate allocated to the given uuid)
     */
    public Coordinate getCoordinate(String uuid) {
        String value = sharedPref.getString(uuid, null);
        if (value != null)
            return new Coordinate(getLatitude(value), getLongitude(value));
        return null;
    }

    /**
     * Retrieves all coordinates from the SharedPreferences and returns them as a list
     *
     * @return List of all coordinates in SharedPreferences
     */
    public List<Coordinate> getAllCoordinates() {
        List<Coordinate> coordinateList = new ArrayList<>();
        for (String uuid : uuidList) {
            coordinateList.add(getCoordinate(uuid));
        }
        return coordinateList;
    }

    /**
     * @param coordinate String: [latitude],[longitude]
     * @return Latitude as a double
     */
    private double getLatitude(String coordinate) {
        if (coordinate != null)
            return Double.parseDouble(coordinate.split(",")[0]);
        return 0;
    }

    /**
     * @param coordinate [latitude],[longitude]
     * @return Longitude as a double
     */
    private double getLongitude(String coordinate) {
        if (coordinate != null)
            return Double.parseDouble(coordinate.split(",")[1]);
        return 0;
    }
}
