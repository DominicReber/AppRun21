package ch.apprun.morseencoder;

import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import ch.apprun.morseencoder.morse.MorseEncoder;
import ch.apprun.morseencoder.morse.Primitive;
import android.os.Handler;
import android.os.HandlerThread;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Handler threadHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HandlerThread handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();
        threadHandler = new Handler(handlerThread.getLooper());
        setContentView(R.layout.activity_main);

        Button btnEncode = (Button) findViewById(R.id.btn_encode);
        btnEncode.setOnClickListener(view -> {
            EditText textInput = (EditText) findViewById(R.id.text_input);
            String text = textInput.getText().toString().toUpperCase();

            try {
                MorseEncoder morseEncoder = new MorseEncoder();
                morse(morseEncoder.textToCode(text));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button btnLogbuch = (Button) findViewById(R.id.btn_logbuch);
        btnLogbuch.setOnClickListener(view -> {
            EditText textInput = (EditText) findViewById(R.id.logbuch_input);
            String text = textInput.getText().toString();
            if (!text.isEmpty()) {
                LogbuchUtil logbuchUtil = new LogbuchUtil(getApplicationContext());
                logbuchUtil.log(text);
            }
        });
    }

    private void morse(List<Primitive> code) {
        View pageLayout = findViewById(R.id.page_layout);
        threadHandler.post(new MorseLoop(Color.BLACK,1, pageLayout));
        for (Primitive p : code) {
            int color = p.isLightOn() ? Color.WHITE : Color.BLACK;
            threadHandler.post(new MorseLoop(color, p.getSignalLengthInDits(), pageLayout));
        }
        threadHandler.post(new MorseLoop(Color.BLACK,1, pageLayout));
    }
}