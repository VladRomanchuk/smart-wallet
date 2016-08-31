package a8wizard.com.myapplication.intro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.paolorotolo.appintro.AppIntro;

import a8wizard.com.myapplication.MainActivity;
import a8wizard.com.myapplication.R;

public class IntroScreenActivity extends AppIntro {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            ed.commit();
        }



        new IntroFragment();
        addSlide(IntroFragment.newInstance(getResources().getString(R.string.title_intro_first), getResources().getString(R.string.intro_first_description), R.drawable.logo_intro_first));
        addSlide(IntroFragment.newInstance(getResources().getString(R.string.title_intro_second), getResources().getString(R.string.intro_second_description), R.drawable.logo_intro_second));
        addSlide(IntroFragment.newInstance(getResources().getString(R.string.title_intro_third), getResources().getString(R.string.intro_third_description), R.drawable.logo_intro_third));

        setGoBackLock(true);
        showSkipButton(true);
        setColorDoneText(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

        setBarColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));

        setSeparatorColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
        setIndicatorColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), ContextCompat.getColor(getApplicationContext(), R.color.colorIndicator));

    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        setFlowAnimation();


    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(IntroScreenActivity.this, MainActivity.class);
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        startActivity(intent);
        finish();
    }
}
