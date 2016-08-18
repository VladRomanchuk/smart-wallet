package a8wizard.com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);


        TimerTask task = new TimerTask(){
            @Override
            public void run() {

                Intent intentMain = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intentMain);

                SplashActivity.this.finish();
            }
        };

        new Timer().schedule(task, 3000);

    }

}
