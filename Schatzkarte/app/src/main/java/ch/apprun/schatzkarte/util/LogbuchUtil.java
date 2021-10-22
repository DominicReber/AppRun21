package ch.apprun.schatzkarte.util;

import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.apprun.schatzkarte.model.Coordinate;

/**
 * @author Kai Moser
 */
public class LogbuchUtil {
    private Context context;

    /**
     * @param context Context/Activity
     */
    public LogbuchUtil(Context context) {
        this.context = context;
    }

    /**
     * Creates a new entry in the Logbuch App containing the saved coordinates
     */
    public void log() {
        Intent intent = new Intent("ch.apprun.intent.LOG");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Used to call startActivity() from outside of an activity context
        JSONObject log = new JSONObject();

        try {
            log.put("task", "Schatzkarte");
            log.put("points", getDataForLogbuch());
        } catch (JSONException e) {
            System.err.println("Something went wrong while building the log!");
            System.err.println(e.getMessage());
        }

        intent.putExtra("ch.apprun.logmessage", log.toString());
        context.startActivity(intent);
    }

    /**
     * Takes the saved coordinates and transforms them into the correct format for the Logbuch App:
     * [{"lat": ..., "lon": ...}, {"lat": ..., "lon": ...}, ...]
     *
     * @return A JSONArray
     */
    private JSONArray getDataForLogbuch() throws JSONException {
        JSONArray solutionJson = new JSONArray();
        SharedPreferencesUtil spUtil = new SharedPreferencesUtil(context);
        for (Coordinate coordinate : spUtil.getAllCoordinates()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("lat", coordinate.getLatitude());
            jsonObject.put("lon", coordinate.getLongitude());
            solutionJson.put(jsonObject);
        }
        return solutionJson;
    }
}

