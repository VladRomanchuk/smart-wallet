package a8wizard.com.myapplication.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import a8wizard.com.myapplication.R;

public class IntroScreenActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.into_page);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new IntroAdapter(getSupportFragmentManager()));
        mViewPager.setPageTransformer(false, new IntroPageTransformer());
    }
}
