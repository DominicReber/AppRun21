package ch.apprun.pixelmaler;

import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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
    public void log(List<Pixel> pixels) {

        Intent intent = new Intent("ch.apprun.intent.LOG");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Used to call startActivity() from outside of an activity context
        JSONObject log = new JSONObject();

        try {
            log.put("task", "Pixelmaler");
            log.put("pixels", getDataForLogbuch(pixels));
        } catch (JSONException e) {
            System.err.println("Something went wrong while building the log!");
            System.err.println(e.getMessage());
        }

        intent.putExtra("ch.apprun.logmessage", log.toString());
        context.startActivity(intent);
    }

    /**
     * Takes the colored Pixels and transforms them into the correct format for the Logbuch App:
     * {"task": "Pixelmaler", "pixels": [ {"y":"0","x":"0","color":"#ffea00FF"}, ...]}
     *
     * @return A JSONArray
     */
    private JSONArray getDataForLogbuch(List<Pixel> pixels) throws JSONException {
        JSONArray solutionJson = new JSONArray();
        for (Pixel pixel : pixels) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("y", pixel.getY());
            jsonObject.put("x", pixel.getX());
            jsonObject.put("color", pixel.getColor());
            solutionJson.put(jsonObject);
        }
        return solutionJson;
    }
}
