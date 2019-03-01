package com.example.android.popularmoviesstg2.tools;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.popularmoviesstg2.MainActivity;
import com.example.android.popularmoviesstg2.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Splash extends AppCompatActivity {

    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        countDownTimer = new CountDownTimer(2500, 200) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                fadeOut();
                finish();
            }
        };
        countDownTimer.start();
    }

    private void fadeOut(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
