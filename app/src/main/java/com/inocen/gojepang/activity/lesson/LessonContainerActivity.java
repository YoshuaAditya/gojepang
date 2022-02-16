package com.inocen.gojepang.activity.lesson;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.inocen.gojepang.R;

import java.util.ArrayList;

public class LessonContainerActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    ArrayList<String> arrayListRomaji,arrayListKana,arrayListKanji
            ,arrayListEnglish, arrayListIndonesia;
    SharedPreferences sharedPref;
    QuestionFragment questionFragment;
    NewCardFragment newCardFragment;
    LessonResultFragment lessonResultFragment;
    int selectedChapter;
    String chapterTitle;
    int score=0, progress=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_container);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Bundle bundleFromLessonIntro = getIntent().getBundleExtra("bundle");
        arrayListRomaji=bundleFromLessonIntro.getStringArrayList(getString(R.string.array_list_romaji));
        arrayListKana=bundleFromLessonIntro.getStringArrayList(getString(R.string.array_list_kana));
        arrayListKanji=bundleFromLessonIntro.getStringArrayList(getString(R.string.array_list_kanji));
        arrayListEnglish=bundleFromLessonIntro.getStringArrayList(getString(R.string.array_list_english));
        arrayListIndonesia=bundleFromLessonIntro.getStringArrayList(getString(R.string.array_list_indonesia));
        selectedChapter=bundleFromLessonIntro.getInt(getString(R.string.lesson_intro_selected));
        chapterTitle=bundleFromLessonIntro.getString(getString(R.string.lesson_intro_chap_title));


//        Toast.makeText(LessonContainerActivity.this, arrayListRomaji.get(0)+arrayListRomaji.size(),
//                Toast.LENGTH_SHORT).show();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        newCardFragment = new NewCardFragment();
        questionFragment = new QuestionFragment();
        lessonResultFragment=new LessonResultFragment();
        fragmentTransaction.add(R.id.container, newCardFragment);
        fragmentTransaction.add(R.id.container, questionFragment);
        fragmentTransaction.add(R.id.container, lessonResultFragment);
        fragmentTransaction.detach(lessonResultFragment);
        fragmentTransaction.detach(questionFragment);
        fragmentTransaction.attach(newCardFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed(){

    }
}
