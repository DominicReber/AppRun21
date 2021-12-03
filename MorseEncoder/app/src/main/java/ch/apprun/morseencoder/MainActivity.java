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
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import ch.apprun.morseencoder.morse.MorseEncoder;
import ch.apprun.morseencoder.morse.Primitive;

public class MainActivity extends AppCompatActivity {
    private HandlerThread handlerThread = new HandlerThread("HandlerThread");
    private Handler threadHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handlerThread.start();
        threadHandler = new Handler(handlerThread.getLooper());
        setContentView(R.layout.activity_main);

        Button btnEncode = (Button) findViewById(R.id.btn_encode);

        btnEncode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textInput = (EditText) findViewById(R.id.text_input);
                String text = textInput.getText().toString().toUpperCase();

                try {
                    MorseEncoder morseEncoder = new MorseEncoder();
                    List<Primitive> code = morseEncoder.textToCode(text);

                    StringBuilder s = new StringBuilder();
                    for (Primitive p : code) {
                        s.append(p.getTextRepresentation());
                    }
                    System.out.println(s);

                    morsing(code);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        Button btnLogbuch = (Button) findViewById(R.id.btn_logbuch);

        btnLogbuch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textInput = (EditText) findViewById(R.id.logbuch_input);
                String text = textInput.getText().toString();
                LogbuchUtil logbuchUtil = new LogbuchUtil(getApplicationContext());
                logbuchUtil.log(text);
            }
        });
    }

    public void morsing(List<Primitive> code) {
        threadHandler.post(new morseLoopBlack(1));
        for (Primitive p : code) {
            if (p.isLightOn()) {
                threadHandler.post(new morseLoopWhite(p.getSignalLengthInDits()));
            } else {
                threadHandler.post(new morseLoopBlack(p.getSignalLengthInDits()));
            }
        }
        threadHandler.post(new morseLoopBlack(1));

//        for (int i = 0; i < textToMorse.length(); i++ ){
//            char morseSymbol = textToMorse.charAt(i);
//            if (morseSymbol == 'a') {
//                threadHandler.post(new morseLoopWhite());
//
//
//            } else if (morseSymbol == 'b') {
//                threadHandler.post(new morseLoopBlack());
//            }
//
//        }

    }

    /*class hideElements implements Runnable{
        @Override
        public void run() {
            Button start_button = findViewById(R.id.btn_start);
            EditText txt_toMorse = findViewById(R.id.txt_toMorse);
            start_button.setVisibility(start_button.GONE);
            txt_toMorse.setVisibility(txt_toMorse.GONE);
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }*/

    /*class showElements implements Runnable{
        @Override
        public void run() {
            Button start_button = findViewById(R.id.btn_start);
            EditText txt_toMorse = findViewById(R.id.txt_toMorse);
            start_button.setVisibility(start_button.VISIBLE);
            txt_toMorse.setVisibility(txt_toMorse.VISIBLE);
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }*/

    class morseLoopWhite implements Runnable {
        private int signalLengthInDits;

        public morseLoopWhite(int signalLengthInDits) {
            this.signalLengthInDits = signalLengthInDits;
        }

        @Override
        public void run() {
            View pageLayout = findViewById(R.id.page_layout);
            pageLayout.setBackgroundColor(Color.WHITE);
            SystemClock.sleep(500*signalLengthInDits);
        }
    }

    class morseLoopBlack implements Runnable {
        private int signalLengthInDits;

        public morseLoopBlack(int signalLengthInDits) {
            this.signalLengthInDits = signalLengthInDits;
        }

        @Override
        public void run() {
            View pageLayout = findViewById(R.id.page_layout);
            pageLayout.setBackgroundColor(Color.BLACK);
            SystemClock.sleep(500*signalLengthInDits);
        }
    }
}