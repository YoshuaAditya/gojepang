package com.inocen.gojepang.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.preference.PreferenceManager;

import com.inocen.gojepang.R;
import com.inocen.gojepang.activity.kanji.KanjiIntroActivity;
import com.inocen.gojepang.activity.lesson.LessonIntroActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class CharacterSettingActivity extends AppCompatActivity implements View.OnClickListener{
    private CircleImageView circleImageViewRomaji;
    private CircleImageView circleImageViewKana;
    private CircleImageView circleImageViewKanji;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Bundle bundleFromLessonHome;
    private Bundle bundleFromKanjiHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_setting);
        circleImageViewRomaji = findViewById(R.id.circleImageViewRomaji);
        circleImageViewKana= findViewById(R.id.circleImageViewKana);
        circleImageViewKanji= findViewById(R.id.circleImageViewKanji);
        circleImageViewRomaji.setOnClickListener(this);
        circleImageViewKana.setOnClickListener(this);
        circleImageViewKanji.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String  characterPreference="alphabet";
        if (v== circleImageViewRomaji){
            characterPreference="alphabet";
        }
        else if (v==circleImageViewKana){
            characterPreference="kana";
        }
        else if (v==circleImageViewKanji){
            characterPreference="kanji";
        }
        sharedPref =  PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPref.edit();
        editor.putString(getString(R.string.sharedPrefCharacter), characterPreference);
        editor.apply();

        bundleFromLessonHome = getIntent().getBundleExtra(getString(R.string.bundle_intro));
        if(bundleFromLessonHome!=null)goToLessonIntroActivity();
        bundleFromKanjiHome = getIntent().getBundleExtra(getString(R.string.bundle_intro_kanji));
        if(bundleFromKanjiHome!=null)goToKanjiIntroActivity();

        finish();
    }

    private void goToLessonIntroActivity() {
        Intent intent=new Intent(CharacterSettingActivity.this, LessonIntroActivity.class);
        intent.putExtra(CharacterSettingActivity.this.getString(R.string.bundle_intro),bundleFromLessonHome);
        startActivity(intent);
    }
    private void goToKanjiIntroActivity() {
        Intent intent=new Intent(CharacterSettingActivity.this, KanjiIntroActivity.class);
        intent.putExtra(CharacterSettingActivity.this.getString(R.string.bundle_intro_kanji),bundleFromKanjiHome);
        startActivity(intent);
    }
}
