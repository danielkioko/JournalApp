package com.danielkioko.peachnotes.Intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.danielkioko.peachnotes.Notes.HomeActivity;
import com.danielkioko.peachnotes.R;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    IntroViewPagerAdapter introViewPagerAdapter;
    private ViewPager viewPager;
    TabLayout tabLayout;
    Button nextBtn, getStarted;
    int position = 0;
    Animation btnAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (restorePrefData()) {
            goToHome();
            finish();
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_intro);

        tabLayout = findViewById(R.id.tabLayout);
        nextBtn = findViewById(R.id.btn_next);
        getStarted = findViewById(R.id.btn_get_started);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);

        final List<ScreenItem> list = new ArrayList<>();
        list.add(new ScreenItem("Fresh Food", "Fresh Food", R.drawable.img_one));
        list.add(new ScreenItem("Fast Delivery", "Fresh Food", R.drawable.img_two));
        list.add(new ScreenItem("Easy Payment", "Fresh Food", R.drawable.img_three));

        viewPager = findViewById(R.id.screenViewPager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, list);
        viewPager.setAdapter(introViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = viewPager.getCurrentItem();

                if (position < list.size()) {
                    position++;
                    viewPager.setCurrentItem(position);
                }

                if (position == list.size() - 1) {
                    loadScreen();
                }

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == list.size() - 1) {
                    loadScreen();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHome();
                savePrefsData();
                finish();
            }
        });

    }

    private boolean restorePrefData() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("userPref", MODE_PRIVATE);
        Boolean isActivityOpenedBefore = preferences.getBoolean("isIntroOpened", false);

        return isActivityOpenedBefore;
    }

    private void savePrefsData() {

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("userPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.commit();

    }

    private void loadScreen() {

        nextBtn.setVisibility(View.INVISIBLE);
        getStarted.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.INVISIBLE);
        getStarted.setAnimation(btnAnim);

    }

    public void goToHome() {
        Intent intent = new Intent(IntroActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
