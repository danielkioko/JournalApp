package com.danielkioko.peachnotes;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    IntroViewPagerAdapter introViewPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        List<ScreenItem> list = new ArrayList<>();
        list.add(new ScreenItem("Fresh Food", "Fresh Food", R.drawable.img_one));
        list.add(new ScreenItem("Fast Delivery", "Fresh Food", R.drawable.img_two));
        list.add(new ScreenItem("Easy Payment", "Fresh Food", R.drawable.img_three));

        viewPager = findViewById(R.id.screenViewPager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, list);
        viewPager.setAdapter(introViewPagerAdapter);

    }
}
