package com.inocen.gojepang.activity.kanji_test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.inocen.gojepang.R;
import com.inocen.gojepang.activity.kanji.KanjiIntroActivity;
import com.inocen.gojepang.activity.kanji.KanjiNewCardFlipFragment;
import com.inocen.gojepang.activity.kanji.KanjiNewCardFragment;
import com.inocen.gojepang.activity.kanji.KanjiQuestionFragment;
import com.inocen.gojepang.activity.kanji.KanjiResultFragment;
import com.inocen.gojepang.activity.kanji.KanjiYomikataFlipFragment;
import com.inocen.gojepang.activity.kanji.KanjiYomikataFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class KanjiTestContainerActivity extends AppCompatActivity {

    String writerTemp = "";
    String prefixTreeJson = "vc";
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    SharedPreferences sharedPref;
    KanjiTestQuestionFragment kanjiTestQuestionFragment;
    KanjiTestResultFragment kanjiTestResultFragment;
    ArrayList<String> arrayListKanji = new ArrayList<>();
    ArrayList<String> arrayListEnglish= new ArrayList<>();
    ArrayList<String> arrayListIndonesia= new ArrayList<>();
    String stringKanji, stringEnglish, stringIndonesia;;
    int selectedKanjiLevel;
    int chapterTitle=1;
    boolean readJsonSuccess=true;
    int progressBarValue = 0, currentKanjiNodeNumber = 0, currentKanjiLeafNumber = 0,
            currentKanjiLeafCount = 0, totalKanji = 0, score = 0, cardCount = 0;

    final int KANJI = 0, INDONESIA = 1, ENGLISH = 2, ROMAJI_KUN = 3, ROMAJI_ON = 4, HIRAGANA_KUN = 5, HIRAGANA_ON = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_container);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Bundle bundleFromKanjiTestMenu = getIntent().getBundleExtra(getString(R.string.bundle_test_kanji_menu_adapter));
        selectedKanjiLevel = bundleFromKanjiTestMenu.getInt(getString(R.string.bundle_test_kanji_menu_adapter));

        loadQuestion();

        while(arrayListEnglish.size()>50){
            int random= ThreadLocalRandom.current().nextInt(arrayListEnglish.size());
            arrayListEnglish.remove(random);
            arrayListIndonesia.remove(random);
            arrayListKanji.remove(random);
        }
        totalKanji=arrayListEnglish.size();

        if(readJsonSuccess) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            kanjiTestQuestionFragment = new KanjiTestQuestionFragment();
            kanjiTestResultFragment = new KanjiTestResultFragment();
            fragmentTransaction.add(R.id.container, kanjiTestQuestionFragment);
            fragmentTransaction.add(R.id.container, kanjiTestResultFragment);
            fragmentTransaction.detach(kanjiTestResultFragment);
            fragmentTransaction.attach(kanjiTestQuestionFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            Log.e("GOJEPANG",""+arrayListEnglish.size());
        }
        else {
            Toast.makeText(KanjiTestContainerActivity.this, "Coming soon",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadQuestion() {
        switch (selectedKanjiLevel) {
            case R.string.kanjitestlevel1:
                prefixTreeJson = "vc01";
                loadFromJson(R.raw.kanji1);
                prefixTreeJson = "vc02";
                loadFromJson(R.raw.kanji2);
                prefixTreeJson = "vc03";
                loadFromJson(R.raw.kanji3);
                prefixTreeJson = "vc04";
                loadFromJson(R.raw.kanji4);
                prefixTreeJson = "vc05";
                loadFromJson(R.raw.kanji5);
                prefixTreeJson = "vc06";
                loadFromJson(R.raw.kanji6);
                prefixTreeJson = "vc07";
                loadFromJson(R.raw.kanji7);
                prefixTreeJson = "vc08";
                loadFromJson(R.raw.kanji8);
                prefixTreeJson = "vc09";
                loadFromJson(R.raw.kanji9);
                prefixTreeJson = "vc10";
                loadFromJson(R.raw.kanji10);
                break;
            case R.string.kanjitestlevel11:
                chapterTitle=2;
                readJsonSuccess=false;
                break;
            case R.string.kanjitestlevel21:
                chapterTitle=3;
                readJsonSuccess=false;
                break;
            case R.string.kanjitestlevel31:
                chapterTitle=4;
                readJsonSuccess=false;
                break;
            case R.string.kanjitestlevel41:
                chapterTitle=5;
                readJsonSuccess=false;
                break;
        }
    }

    private void loadFromJson(int rawKanjiJson) {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            br = new BufferedReader(new InputStreamReader(getResources().openRawResource(rawKanjiJson), "windows-31j"));
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
                parseWords(obj);
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

        //masuk ke node kanjis pada json
        JsonObject jsonObjectKanjis=jsonObject.getAsJsonObject("kanjis");
        int size = jsonObjectKanjis.size();

        //memasukkan data dari node ke arraylist
        try {
            for (int i = 1; i <= size; i++) {
                //dapetin format contoh vc0101
                JsonObject jsonObjectKanji;
                String tempPrefixTreeJson=prefixTreeJson;
                if (i < 10) {
                    tempPrefixTreeJson+="0"+i;
                } else {
                    tempPrefixTreeJson+=i;
                }
                jsonObjectKanji = jsonObjectKanjis.getAsJsonObject(tempPrefixTreeJson);

                int childSize= jsonObjectKanji.size();
                for(int j=0;j<childSize;j++){

                    //format contoh vc0101_0
                    String tempPrefixLeafJson=tempPrefixTreeJson+"_"+j;

                    JsonObject jsonObjectKanjiLeaf=jsonObjectKanji.getAsJsonObject(tempPrefixLeafJson);

                    stringKanji = jsonObjectKanjiLeaf.get("kanji").getAsString();
                    stringEnglish = jsonObjectKanjiLeaf.get("english").getAsString();
                    stringIndonesia = jsonObjectKanjiLeaf.get("indonesia").getAsString();

                    arrayListKanji.add(stringKanji);
                    arrayListEnglish.add(stringEnglish);
                    arrayListIndonesia.add(stringIndonesia);
//                    Log.e("GOEJAPNG", "prefix tree "+ tempPrefixLeafJson);
                }
            }
        } catch (NullPointerException e) {
            Toast.makeText(KanjiTestContainerActivity.this, "Coming soon",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            readJsonSuccess=false;
            finish();
        }
    }


    @Override
    public void onBackPressed() {

    }
}
