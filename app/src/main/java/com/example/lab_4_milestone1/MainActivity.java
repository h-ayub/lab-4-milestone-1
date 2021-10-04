package com.example.lab_4_milestone1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button startButton;
    private TextView textView;
    private volatile boolean stopThread = false;

    class ExampleRunnable implements Runnable {

        @Override
        public void run() {
            mockFileDownloader();
        }
    }

    public void mockFileDownloader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText("DOWNLOADING...");
                textView.setVisibility(View.VISIBLE);
            }
        });

        for (int dlProgress = 0; dlProgress <= 100; dlProgress += 10) {
            if (stopThread) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startButton.setText("Start");
                        textView.setVisibility(View.INVISIBLE);
                        textView.setText("");
                    }
                });
                return;
            }

            String message = "Download Progress: " + dlProgress + "%";
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText(message);
                }
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText("Start");
            }
        });
    }

    public void startDownload(View view) {
        stopThread = false;
        ExampleRunnable runnable = new ExampleRunnable();
        new Thread(runnable).start();
    }

    public void stopDownload(View view) {
        stopThread = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.start);
        textView = findViewById(R.id.textView);
    }
}