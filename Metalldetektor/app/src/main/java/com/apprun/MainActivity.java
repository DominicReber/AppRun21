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

import org.json.JSONObject;

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

    protected void activateSensor() {
        if(sensorActive){
            sensorActive = false;
            ProgressBar pbar = findViewById(R.id.progressBar);
            pbar.setProgress(pbar.getProgress() - 5);

        }
        else if(!sensorActive){
            sensorActive = true;
            ProgressBar pbar = findViewById(R.id.progressBar);
            pbar.setProgress(pbar.getProgress()+5);


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void log(String solution) {
        Intent intent = new Intent("ch.apprun.intent.LOG");
        if (getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isEmpty()) {
            Toast.makeText(this, "Logbook App not Installed", Toast.LENGTH_LONG).show();
            return;
        }


// Achtung, je nach App wird etwas anderes eingetragen
        String logmessage = "{\n" +
                " \"task\": \"Metalldetektor\",\n" +
                " \"solution\": \"" + solution + "\"\n" +
                "}\n";
        intent.putExtra("ch.apprun.logmessage", logmessage);
        startActivity(intent);
    }


    // SensorEventListener:

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        synchronized (this) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                float[] mag = sensorEvent.values;
                double betrag = Math.sqrt(mag[0] * mag[0] + mag[1] * mag[1] + mag[2] *
                        mag[2]);
                ProgressBar pbar = findViewById(R.id.progressBar);
                double pbarPercentage = 1.6 * (int) betrag - 60;
                pbar.setProgress((int) pbarPercentage);
                updateProgressBarColor(pbar);
            }
        }
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
}