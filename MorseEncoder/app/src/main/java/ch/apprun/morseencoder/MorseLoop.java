package ch.apprun.morseencoder;

import android.os.SystemClock;
import android.view.View;

public class MorseLoop implements Runnable {
    private static final long DIT_DURATION_MS = 500;

    private final int color;
    private final int signalLengthInDits;
    private final View view;

    /**
     * @param color              Color ID of android.graphics.Color
     * @param signalLengthInDits Length of the signal in Dits
     * @param view               View whose color needs to change
     */
    public MorseLoop(int color, int signalLengthInDits, View view) {
        this.color = color;
        this.signalLengthInDits = signalLengthInDits;
        this.view = view;
    }

    @Override
    public void run() {
        view.setBackgroundColor(color);
        SystemClock.sleep(DIT_DURATION_MS * signalLengthInDits);
    }
}
