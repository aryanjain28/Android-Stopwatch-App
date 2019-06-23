package com.example.stopwatch;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView displayMinutes;
    TextView displaySeconds;
    String secondsString, minutesString;
    int secondsInteger, minutesInteger;
    Thread thread;

    static int flag = 0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            display();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displaySeconds = findViewById(R.id.displaySeconds);
        displayMinutes = findViewById(R.id.displayMinutes);
    }

    public void startButton(View view) {

        flag = 0;

        Runnable r = new Runnable() {
            @Override
            public void run() {

                while (flag == 0) {

                    synchronized (this) {
                        try {
                            wait(1000);

                            secondsString = displaySeconds.getText().toString();
                            minutesString = displayMinutes.getText().toString();

                            secondsInteger = Integer.parseInt(secondsString);
                            minutesInteger = Integer.parseInt(minutesString);

                            if (secondsInteger >= 59) {
                                minutesInteger += 1;
                                secondsInteger = -1;
                            }

                            if(minutesInteger > 59){
                                minutesInteger = -1;
                            }

                            secondsInteger += 1;

                        } catch (Exception e) {}

                        handler.sendEmptyMessage(0);
                    }
                }
            }

        };

        thread = new Thread(r);
        thread.start();
    }

    public void pauseButton(View view){

        stopThread();
        flag = 1;
    }

    public void resetButton(View view){
        stopThread();

        secondsInteger = 0;
        minutesInteger = 0;

        display();
    }

    public void stopThread(){
        if(thread!=null){
            thread.interrupt();
            thread = null;
        }
    }

    public void display(){
        if(secondsInteger  < 10)
            displaySeconds.setText("0"+secondsInteger);
        else
            displaySeconds.setText(""+secondsInteger);

        if(minutesInteger < 10)
            displayMinutes.setText("0"+minutesInteger);
        else
            displayMinutes.setText(""+minutesString);
    }
}
