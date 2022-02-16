package com.inocen.gojepang.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.inocen.gojepang.R;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class LanguageSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView circleImageViewEnglish;
    private CircleImageView circleImageViewIndonesia;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_setting);
        circleImageViewEnglish = findViewById(R.id.circleImageViewEnglish);
        circleImageViewIndonesia = findViewById(R.id.circleImageViewIndonesia);
        circleImageViewEnglish.setOnClickListener(this);
        circleImageViewIndonesia.setOnClickListener(this);
        //untuk run sekali aja pas diawal
        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);
        if (isFirstRun)
        {
            // Code to run once
            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("FIRSTRUN", false);
            editor.apply();
        }else{
            sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            String defaultLanguageValue = "en";
            String languagePref = sharedPref.getString(getString(R.string.sharedPrefLanguage), defaultLanguageValue);
            Resources res = LanguageSettingActivity.this.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            conf.setLocale(new Locale(languagePref)); // API 17+ only.
            res.updateConfiguration(conf, dm);

            goToWelcomeScreenActivity();
        }
    }
    public void goToWelcomeScreenActivity(){
        Intent i = new Intent(this, WelcomeScreenActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(LanguageSettingActivity.this, MenuActivity.class);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View v) {
        String language_code="en";
        if (v==circleImageViewEnglish){
            language_code="en";
        }
        else if (v==circleImageViewIndonesia){
            language_code="in";
        }

        Resources res = LanguageSettingActivity.this.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(language_code)); // API 17+ only.
        res.updateConfiguration(conf, dm);

        sharedPref =  PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPref.edit();
        editor.putString(getString(R.string.sharedPrefLanguage), language_code);
        editor.apply();

        Bundle bundleFromMenu = getIntent().getBundleExtra("bundle");
        if(bundleFromMenu!=null){
            Intent i = new Intent(LanguageSettingActivity.this, MenuActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            finish();
            return;
        }
        goToWelcomeScreenActivity();
    }
}
