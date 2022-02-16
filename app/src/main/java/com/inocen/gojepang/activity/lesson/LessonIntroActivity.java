package com.inocen.gojepang.activity.lesson;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.inocen.gojepang.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.inocen.gojepang.activity.MenuActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

public class LessonIntroActivity extends AppCompatActivity {

    ListView listView;
    String writerTemp = "";
    int selectedChapter;
    String[] wordsArray;
    String prefixTreeJson = "vc";
    String chapterTitle="";
    Button start;
    ArrayList<String> arrayListRomaji,arrayListKana,arrayListKanji
            ,arrayListEnglish, arrayListIndonesia;
    boolean readJsonSuccess=true;
    private SharedPreferences sharedPref;
    private TextView textViewChapterNumber, textViewChapterTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_intro);

        Bundle bundleFromLessonHome = getIntent().getBundleExtra(getString(R.string.bundle_intro));
        selectedChapter = bundleFromLessonHome.getInt(getString(R.string.chapter_title));
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        getKotobaList();
        setKotobaList();

        start = findViewById(R.id.buttonLessonStart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LessonIntroActivity.this, LessonContainerActivity.class);
                Bundle bundleToLessonContainer = new Bundle();
                bundleToLessonContainer.putStringArrayList(getString(R.string.array_list_english),arrayListEnglish);
                bundleToLessonContainer.putStringArrayList(getString(R.string.array_list_indonesia),arrayListIndonesia);
                bundleToLessonContainer.putStringArrayList(getString(R.string.array_list_romaji),arrayListRomaji);
                bundleToLessonContainer.putStringArrayList(getString(R.string.array_list_kana),arrayListKana);
                bundleToLessonContainer.putStringArrayList(getString(R.string.array_list_kanji),arrayListKanji);
                bundleToLessonContainer.putInt(getString(R.string.lesson_intro_selected),selectedChapter);
                bundleToLessonContainer.putString(getString(R.string.lesson_intro_chap_title),chapterTitle);
                intent.putExtra("bundle",bundleToLessonContainer);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();
            }
        });
    }

    /**
     * memasukkan data string array ke listview di activity ini
     */
    private void setKotobaList() {
        listView = findViewById(R.id.listView);
        //add data for list
        if(readJsonSuccess) {
            LessonIntroAdapter adapter = new LessonIntroAdapter(this, wordsArray);
            listView.setAdapter(adapter);
        }
    }

    /**
     * membaca chapter1 dari json lalu mendapatkan variabel tipe JsonObject
     */
    private void getKotobaList() {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            //TODO UPDATE ini tiap nambah konten lesson
            int chapterId=R.raw.chapter1;
            chapterTitle="Chapter ";
            //switch buat menentukan file json mana yang dibaca dan kode wordId nantinya di json
            switch (selectedChapter) {
                case R.string.chaptertitle1:
                    prefixTreeJson += "01";
                    chapterId=R.raw.chapter1;
                    chapterTitle+=1;
                    break;
                case R.string.chaptertitle2:
                    prefixTreeJson += "02";
                    chapterId=R.raw.chapter2;
                    chapterTitle+=2;
                    break;
                case R.string.chaptertitle3:
                    prefixTreeJson += "03";
                    chapterId=R.raw.chapter3;
                    chapterTitle+=3;
                    break;
                case R.string.chaptertitle4:
                    prefixTreeJson += "04";
                    chapterId=R.raw.chapter4;
                    chapterTitle+=4;
                    break;
                case R.string.chaptertitle5:
                    prefixTreeJson += "05";
                    chapterId=R.raw.chapter5;
                    chapterTitle+=5;
                    break;
                case R.string.chaptertitle6:
                    prefixTreeJson += "06";
//                    chapterId=R.raw.chapter6;
                    chapterTitle+=6;
                    break;
                case R.string.chaptertitle7:
                    prefixTreeJson += "07";
//                    chapterId=R.raw.chapter7;
                    chapterTitle+=7;
                    break;
                case R.string.chaptertitle8:
                    prefixTreeJson += "08";
//                    chapterId=R.raw.chapter8;
                    chapterTitle+=8;
                    break;
                case R.string.chaptertitle9:
                    prefixTreeJson += "09";
//                    chapterId=R.raw.chapter9;
                    chapterTitle+=9;
                    break;
                case R.string.chaptertitle10:
                    prefixTreeJson += "10";
//                    chapterId=R.raw.chapter10;
                    chapterTitle+=10;
                    break;
            }
            textViewChapterTitle= findViewById(R.id.textViewChapterTitle);
            textViewChapterTitle.setText(selectedChapter);
            textViewChapterNumber= findViewById(R.id.textViewKanjiLevel);
            textViewChapterNumber.setText(chapterTitle);
            br = new BufferedReader(new InputStreamReader(getResources().openRawResource(chapterId), "windows-31j"));
            int n;
            while ((n = br.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writerTemp = writer.toString();
                JsonObject obj = new JsonParser().parse(writerTemp).getAsJsonObject();
                parseWords(obj.getAsJsonObject("words"));
                br.close(); // stop reading
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param jsonObject jsonObject yang sudah masuk 1 nest, nest chapter1 lalu diparse untuk dimasukkan ke string array
     */
    private void parseWords(JsonObject jsonObject) {
        int size = jsonObject.size();
        wordsArray = new String[size];

        String defaultLanguageValue = getResources().getString(R.string.sharedPrefLanguage);
        String languagePref = sharedPref.getString(getString(R.string.sharedPrefLanguage), defaultLanguageValue);
        String defaultCharValue = getResources().getString(R.string.sharedPrefCharacter);
        String charPref = sharedPref.getString(getString(R.string.sharedPrefCharacter), defaultCharValue);

        arrayListEnglish=new ArrayList<>();
        arrayListIndonesia=new ArrayList<>();
        arrayListRomaji=new ArrayList<>();
        arrayListKana=new ArrayList<>();
        arrayListKanji=new ArrayList<>();

        //cek preference user dari bahasa dan character
        try {
            for (int i = 1; i <= size; i++) {
                JsonObject jsonObjectWords;
                if (i < 10) {
                    jsonObjectWords = jsonObject.getAsJsonObject(prefixTreeJson + "0" + i);
                } else {
                    jsonObjectWords = jsonObject.getAsJsonObject(prefixTreeJson + i);
                }

                //TODO kode try cuma mencegah json yang blm ada, klo lengkap benere gk usah, jgn lupa hilangin cek null di setKotoba

                String tempRomaji=jsonObjectWords.get("romaji").getAsString();
                String tempKana=jsonObjectWords.get("kana").getAsString();
                String tempKanji=jsonObjectWords.get("kanji").getAsString();
                String tempEnglish=jsonObjectWords.get("english").getAsString();
                String tempIndonesia=jsonObjectWords.get("indonesia").getAsString();
                String temp="";
                //switch buat menentukan setting apa yang dipilih user untuk menampilkan sesuai setting lewat sharedPreference
                switch (charPref){
                    case "alphabet":
                        temp+=tempRomaji;
                        break;
                    case "kana":
                        temp+=tempKana;
                        break;
                    case "kanji":
                        temp+=tempKanji;
                        break;
                }
                temp+=" : ";
                switch (languagePref){
                    case "en":
                        temp+=tempEnglish;
                        break;
                    case "in":
                        temp+=tempIndonesia;
                        break;
                }
                arrayListRomaji.add(tempRomaji);
                arrayListKana.add(tempKana);
                arrayListKanji.add(tempKanji);
                arrayListEnglish.add(tempEnglish);
                arrayListIndonesia.add(tempIndonesia);
                wordsArray[i - 1] = temp;
            }
        } catch (NullPointerException e) {
            readJsonSuccess=false;
            Toast.makeText(LessonIntroActivity.this, "Coming soon",
                    Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, LessonHomeActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, LessonHomeActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
        finish();
    }
}
