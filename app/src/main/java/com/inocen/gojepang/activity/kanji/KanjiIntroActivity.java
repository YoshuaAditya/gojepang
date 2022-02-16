package com.inocen.gojepang.activity.kanji;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.inocen.gojepang.R;
import com.inocen.gojepang.activity.MenuActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

public class KanjiIntroActivity extends AppCompatActivity {

    String writerTemp = "";
    int selectedKanji;
    String prefixTreeJson = "vc";
    String levelKanji = "";
    Button start;
    String stringRomajiKun, stringRomajiOn,
            stringHiraganaKun, stringHiraganaOn,
            stringKanji, stringEnglish, stringIndonesia;
    static ArrayList<ArrayList<ArrayList<String>>> arrayListKanjiRoot;
    ArrayList<ArrayList<String>> arrayListKanjiNode;
    ArrayList<String> arrayListKanjiLeaf;
    private int totalKanji=0;
    private SharedPreferences sharedPref;
    private TextView textViewIntroSpeech, textViewKanjiLevel, textViewChapterTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_intro);
        Bundle bundleFromKanjiHome = getIntent().getBundleExtra(getString(R.string.bundle_intro_kanji));
        selectedKanji = bundleFromKanjiHome.getInt(getString(R.string.kanji_level));
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        getKotobaList();

        start = findViewById(R.id.buttonLessonStart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KanjiIntroActivity.this, KanjiContainerActivity.class);
                Bundle bundleToKanjiContainer = new Bundle();
                bundleToKanjiContainer.putInt(getString(R.string.kanji_intro_total_kanji),totalKanji);
                bundleToKanjiContainer.putInt(getString(R.string.kanji_intro_selected_level),selectedKanji);
                bundleToKanjiContainer.putString(getString(R.string.kanji_intro_level_number),levelKanji);
                intent.putExtra("bundle", bundleToKanjiContainer);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * memasukkan data string array ke listview di activity ini
     */

    /**
     * membaca chapter1 dari json lalu mendapatkan variabel tipe JsonObject
     */
    private void getKotobaList() {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            int kanjiId = R.raw.kanji1;
            levelKanji = "Level ";
            //TODO UPDATE ini tiap nambah konten kanji
            //switch buat menentukan file json mana yang dibaca dan kode wordId nantinya di json
            switch (selectedKanji) {
                case R.string.levelkanji1:
                    prefixTreeJson += "01";
                    kanjiId = R.raw.kanji1;
                    levelKanji += 1;
                    break;
                case R.string.levelkanji2:
                    prefixTreeJson += "02";
                    kanjiId=R.raw.kanji2;
                    levelKanji += 2;
                    break;
                case R.string.levelkanji3:
                    prefixTreeJson += "03";
                    kanjiId=R.raw.kanji3;
                    levelKanji += 3;
                    break;
                case R.string.levelkanji4:
                    prefixTreeJson += "04";
                    kanjiId=R.raw.kanji4;
                    levelKanji += 4;
                    break;
                case R.string.levelkanji5:
                    prefixTreeJson += "05";
                    kanjiId=R.raw.kanji5;
                    levelKanji += 5;
                    break;
                case R.string.levelkanji6:
                    prefixTreeJson += "06";
                    kanjiId=R.raw.kanji6;
                    levelKanji += 6;
                    break;
                case R.string.levelkanji7:
                    prefixTreeJson += "07";
                    kanjiId=R.raw.kanji7;
                    levelKanji += 7;
                    break;
                case R.string.levelkanji8:
                    prefixTreeJson += "08";
                    kanjiId=R.raw.kanji8;
                    levelKanji += 8;
                    break;
                case R.string.levelkanji9:
                    prefixTreeJson += "09";
                    kanjiId=R.raw.kanji9;
                    levelKanji += 9;
                    break;
                case R.string.levelkanji10:
                    prefixTreeJson += "10";
                    kanjiId=R.raw.kanji10;
                    levelKanji += 10;
                    break;
                case R.string.levelkanji11:
                    prefixTreeJson += "11";
//                    kanjiId=R.raw.kanji11;
                    levelKanji += 11;
                    break;
                case R.string.levelkanji12:
                    prefixTreeJson += "12";
//                    kanjiId=R.raw.kanji12;
                    levelKanji += 12;
                    break;
                case R.string.levelkanji13:
                    prefixTreeJson += "13";
//                    kanjiId=R.raw.kanji13;
                    levelKanji += 13;
                    break;
                case R.string.levelkanji14:
                    prefixTreeJson += "14";
//                    kanjiId=R.raw.kanji14;
                    levelKanji += 14;
                    break;
            }
            textViewIntroSpeech = findViewById(R.id.textViewIntroSpeech);
            textViewChapterTitle = findViewById(R.id.textViewChapterTitle);
            textViewChapterTitle.setText(levelKanji);
            textViewKanjiLevel = findViewById(R.id.textViewKanjiLevel);
//            textViewKanjiLevel.setText(selectedKanji);
            br = new BufferedReader(new InputStreamReader(getResources().openRawResource(kanjiId), "windows-31j"));
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
        String temp="";
        String defaultLanguageValue = getResources().getString(R.string.sharedPrefLanguage);
        String languagePref = sharedPref.getString(getString(R.string.sharedPrefLanguage), defaultLanguageValue);
        //set intro speech sesuai bahasa yang dipilih
        switch (languagePref) {
            case "en":
                temp=jsonObject.get("explanationEnglish").getAsString();
                break;
            case "in":
                temp=jsonObject.get("explanationIndonesia").getAsString();
                break;
        }
        textViewIntroSpeech.setText(temp);

        //masuk ke node kanjis pada json
        JsonObject jsonObjectKanjis=jsonObject.getAsJsonObject("kanjis");
        int size = jsonObjectKanjis.size();
        arrayListKanjiRoot= new ArrayList<>();

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
                arrayListKanjiNode=new ArrayList<>();
                for(int j=0;j<childSize;j++){

                    //format contoh vc0101_0
                    String tempPrefixLeafJson=tempPrefixTreeJson+"_"+j;

                    JsonObject jsonObjectKanjiLeaf=jsonObjectKanji.getAsJsonObject(tempPrefixLeafJson);

                    stringKanji = jsonObjectKanjiLeaf.get("kanji").getAsString();
                    stringEnglish = jsonObjectKanjiLeaf.get("english").getAsString();
                    stringIndonesia = jsonObjectKanjiLeaf.get("indonesia").getAsString();
                    stringRomajiKun= jsonObjectKanjiLeaf.get("romajikun").getAsString();
                    stringRomajiOn= jsonObjectKanjiLeaf.get("romajion").getAsString();
                    stringHiraganaKun= jsonObjectKanjiLeaf.get("hiraganakun").getAsString();
                    stringHiraganaOn= jsonObjectKanjiLeaf.get("hiraganaon").getAsString();

                    arrayListKanjiLeaf =new ArrayList<>();
                    arrayListKanjiLeaf.add(stringKanji);
                    arrayListKanjiLeaf.add(stringIndonesia);
                    arrayListKanjiLeaf.add(stringEnglish);
                    arrayListKanjiLeaf.add(stringRomajiKun);
                    arrayListKanjiLeaf.add(stringRomajiOn);
                    arrayListKanjiLeaf.add(stringHiraganaKun);
                    arrayListKanjiLeaf.add(stringHiraganaOn);

                    totalKanji++;
                    arrayListKanjiNode.add(arrayListKanjiLeaf);
                }
                arrayListKanjiRoot.add(arrayListKanjiNode);
            }
            //skip intro kalo udah level 2 keatas
            if(!levelKanji.equals("Level 1")){
                Intent intent = new Intent(KanjiIntroActivity.this, KanjiContainerActivity.class);
                Bundle bundleToKanjiContainer = new Bundle();
                bundleToKanjiContainer.putInt(getString(R.string.kanji_intro_total_kanji),totalKanji);
                bundleToKanjiContainer.putInt(getString(R.string.kanji_intro_selected_level),selectedKanji);
                bundleToKanjiContainer.putString(getString(R.string.kanji_intro_level_number),levelKanji);
                intent.putExtra("bundle", bundleToKanjiContainer);
                startActivity(intent);
                finish();
            }
        } catch (NullPointerException e) {
            Toast.makeText(KanjiIntroActivity.this, "Coming soon",
                    Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, KanjiHomeActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            finish();
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, KanjiHomeActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
        finish();
    }
}
