package ch.apprun.morseencoder;

import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Helps with the Logbuch integration
 *
 * @author Kai Moser
 */
public class LogbuchUtil {

    private final Context context;

    public LogbuchUtil(Context context) {
        this.context = context;
    }

    /**
     * Creates a new entry in the Logbuch
     *
     * @param word solution word
     */
    public void log(String word) {
        Intent intent = new Intent("ch.apprun.intent.LOG");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Used to call startActivity() from outside of an activity context
        JSONObject log = new JSONObject();

        try {
            log.put("task", "Morseencoder");
            log.put("solution", word);
        } catch (JSONException e) {
            System.err.println("Something went wrong while building the log!");
            System.err.println(e.getMessage());
        }

        intent.putExtra("ch.apprun.logmessage", log.toString());
        context.startActivity(intent);
    }
}
