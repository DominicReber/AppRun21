package com.apprun;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.JsonWriter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.apprun.databinding.ActivityMainBinding;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private List<QRCodePair> qrCodePairs = new ArrayList<>();
    private int currentPairPosition;
    private String currentType;
    private int currentImageViewId;
    private int currentTextViewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Add new QR-Code Pair
            }
        });

        qrCodePairs.add(new QRCodePair());
        qrCodePairs.add(new QRCodePair());
        qrCodePairs.add(new QRCodePair());

        Button button = (Button) findViewById(R.id.button_1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeQrCodePicture(R.id.image_1, R.id.text_view_1, 0, "left");
            }
        });
        Button button2 = (Button) findViewById(R.id.button_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeQrCodePicture(R.id.image_2, R.id.text_view_2, 0, "right");
            }
        });

        Button button3 = (Button) findViewById(R.id.button_3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeQrCodePicture(R.id.image_3, R.id.text_view_3, 1, "left");
            }
        });
        Button button4 = (Button) findViewById(R.id.button_4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeQrCodePicture(R.id.image_4, R.id.text_view_4, 1, "right");
            }
        });

        Button button5 = (Button) findViewById(R.id.button_5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeQrCodePicture(R.id.image_5, R.id.text_view_5, 2, "left");
            }
        });
        Button button6 = (Button) findViewById(R.id.button_6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeQrCodePicture(R.id.image_6, R.id.text_view_6, 2, "right");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuItem menuItem = menu.add("Log");
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if ("Log".equals(item.getTitle())) {
                    log();
                    return true;
                }
                return false;
            }
        });
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

    @Override
    public boolean onSupportNavigateUp() {
        return true;
    }

    public void takeQrCodePicture(int imageViewId, int textViewId, int pairPosition, String type) {
        currentImageViewId = imageViewId;
        currentTextViewId = textViewId;
        this.currentPairPosition = pairPosition;
        currentType = type;
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(MyCaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setOrientationLocked(false);
        integrator.addExtra(Intents.Scan.BARCODE_IMAGE_ENABLED, true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentIntegrator.REQUEST_CODE
                && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String path = extras.getString(Intents.Scan.RESULT_BARCODE_IMAGE_PATH);

            // Ein Bitmap zur Darstellung erhalten wir so:
            // Bitmap bmp = BitmapFactory.decodeFile(path)

            String code = extras.getString(
                    Intents.Scan.RESULT);

            ImageView iv = findViewById(currentImageViewId);
            TextView tv = findViewById(currentTextViewId);
            iv.setImageURI(Uri.fromFile(new File(path)));
            tv.setText(code);
            switch (currentType) {
                case "left":
                    qrCodePairs.get(currentPairPosition).setFirstCodeImagePath(path);
                    qrCodePairs.get(currentPairPosition).setFirstCodeSolutionWord(code);
                    break;
                case "right":
                    qrCodePairs.get(currentPairPosition).setSecondCodeImagePath(path);
                    qrCodePairs.get(currentPairPosition).setSecondCodeSolutionWord(code);
                    break;
            }

        }
    }

    private void log() {
        Intent intent = new Intent("ch.apprun.intent.LOG");
        JSONObject log = new JSONObject();

        try {
            log.put("task", "Memory");
            log.put("solution", getDataForLogbuch());
        } catch (JSONException e) {
            System.err.println("Something went wrong while building the log!");
            System.err.println(e.getMessage());
        }

        intent.putExtra("ch.apprun.logmessage", log.toString());
        startActivity(intent);
    }

    /**
     * Takes the data from the scanned QR-Codes and transforms it into the following format:
     * [["word1","word2"],["word1","word2"],["word1","word2"],...]
     *
     * @return A JSONArray
     */
    private JSONArray getDataForLogbuch() {
        JSONArray solutionJson = new JSONArray();

        for (QRCodePair pair : qrCodePairs) {
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(pair.getFirstCodeSolutionWord());
            jsonArray.put(pair.getSecondCodeSolutionWord());

            solutionJson.put(jsonArray);
        }
        return solutionJson;
    }
}