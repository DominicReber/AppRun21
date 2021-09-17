package com.apprun;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.JsonWriter;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.apprun.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private SensorManager sensorManager = null;
    boolean sensorActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // Register magnetic sensor
        sensorManager.registerListener((SensorEventListener) this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
         * Wenn der magnetFieldSensor gefunden wurde, melden wir uns für Updates
         * an.
         */

    }

    @Override
    protected void onPause() {
        super.onPause();
        /*
         * Um Resourcen zu sparen melden wir uns beim verlassen der Activity
         * wieder vom Sensor Manager ab.
         */

    }

    // SensorEventListener:

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        synchronized (this) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                float[] mag = sensorEvent.values;
                double betrag = Math.sqrt(mag[0] * mag[0] + mag[1] * mag[1] + mag[2] * mag[2]);
                ProgressBar pbar = findViewById(R.id.progressBar);
                double pbarPercentage = 1.6 * (int) betrag - 60;
                pbar.setProgress((int) pbarPercentage);
                updateStrengthTextView(betrag);
                updateProgressBarColor(pbar);
            }
        }
    }

    /**
     * Updates the TextView with the current magnetic strength
     *
     * @param strength strength of magnetic field
     */
    private void updateStrengthTextView(double strength) {
        long roundedStrength = Math.round(strength);
        TextView textView = (TextView) findViewById(R.id.strengthTextView);
        textView.setText(String.format("Stärke: %s", roundedStrength));
    }

    /**
     * Changes the color of the given ProgressBar based on the progress
     * - green: 0% - 29%
     * - yellow: 30% - 79%
     * - red: 80% - 100%
     *
     * @param pbar ProgressBar
     */
    private void updateProgressBarColor(ProgressBar pbar) {
        int percentage = pbar.getProgress();
        if (percentage < 30) {
            pbar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        } else if (percentage < 80) {
            pbar.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
        } else {
            pbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add("Log");
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setCaptureActivity(OurCaptureActivity.class);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setOrientationLocked(false);
                integrator.addExtra(Intents.Scan.BARCODE_IMAGE_ENABLED, true);
                integrator.initiateScan();
                return false;
            }
        });
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == IntentIntegrator.REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = intent.getExtras();
            log(extras.getString(Intents.Scan.RESULT));
        }
    }

    private void log(String qrCode) {
        Intent intent = new Intent("ch.apprun.intent.LOG");
        JSONObject log = new JSONObject();

        try {
            log.put("task", "MetallDetektor");
            log.put("solution", qrCode);
        } catch (JSONException e) {
        }

        intent.putExtra("ch.apprun.logmessage", log.toString());
        startActivity(intent);
    }
}