package com.inocen.gojepang.activity.kanji;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.inocen.gojepang.R;

import java.util.ArrayList;

public class KanjiContainerActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    SharedPreferences sharedPref;
    KanjiNewCardFragment kanjiNewCardFragment;
    KanjiNewCardFlipFragment kanjiNewCardFlipFragment;
    KanjiYomikataFragment kanjiYomikataFragment;
    KanjiYomikataFlipFragment kanjiYomikataFlipFragment;
    KanjiQuestionFragment kanjiQuestionFragment;
    KanjiResultFragment kanjiResultFragment;
    int selectedKanjiLevel;
    String chapterTitle;
    int progressBarValue=0,currentKanjiNodeNumber=0,currentKanjiLeafNumber=0,
            currentKanjiLeafCount=0, totalKanji=0, score=0, cardCount=0, progress=0;

    final int KANJI=0,INDONESIA=1,ENGLISH=2,ROMAJI_KUN=3,ROMAJI_ON=4,HIRAGANA_KUN=5,HIRAGANA_ON=6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_container);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Bundle bundleFromKanjiIntro = getIntent().getBundleExtra("bundle");
        totalKanji=bundleFromKanjiIntro.getInt(getString(R.string.kanji_intro_total_kanji));
        selectedKanjiLevel=bundleFromKanjiIntro.getInt(getString(R.string.kanji_intro_selected_level));
        chapterTitle=bundleFromKanjiIntro.getString(getString(R.string.kanji_intro_level_number));

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        kanjiNewCardFragment = new KanjiNewCardFragment();
        kanjiNewCardFlipFragment = new KanjiNewCardFlipFragment();
        kanjiYomikataFragment = new KanjiYomikataFragment();
        kanjiYomikataFlipFragment = new KanjiYomikataFlipFragment();
        kanjiQuestionFragment = new KanjiQuestionFragment();
        kanjiResultFragment = new KanjiResultFragment();
        fragmentTransaction.add(R.id.container, kanjiNewCardFragment);
        fragmentTransaction.add(R.id.container, kanjiNewCardFlipFragment);
        fragmentTransaction.add(R.id.container, kanjiYomikataFragment);
        fragmentTransaction.add(R.id.container, kanjiYomikataFlipFragment);
        fragmentTransaction.add(R.id.container, kanjiQuestionFragment);
        fragmentTransaction.add(R.id.container, kanjiResultFragment);
        fragmentTransaction.detach(kanjiResultFragment);
        fragmentTransaction.detach(kanjiQuestionFragment);
        fragmentTransaction.detach(kanjiYomikataFlipFragment);
        fragmentTransaction.detach(kanjiYomikataFragment);
        fragmentTransaction.detach(kanjiNewCardFlipFragment);
        fragmentTransaction.attach(kanjiNewCardFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed(){

    }
}
