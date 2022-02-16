package com.inocen.gojepang.activity.jlpt;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class JlptContainerBunpouActivity extends AppCompatActivity {

    String writerTemp = "";
    String prefixTreeJson = "";
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    SharedPreferences sharedPref;
    JlptQuestionBunpouFragment jlptQuestionBunpouFragment;
    JlptResultBunpouFragment jlptResultBunpouFragment;
    ArrayList<Soal> arrayListSoalAkhir = new ArrayList<>();
    ArrayList<Soal> arrayListSoalSemua = new ArrayList<>();
    ArrayList<Soal> arrayListMondai1= new ArrayList<>();
    ArrayList<Soal> arrayListMondai2= new ArrayList<>();
    ArrayList<Soal> arrayListMondai3= new ArrayList<>();
    ArrayList<Soal> arrayListMondai4= new ArrayList<>();
    ArrayList<Soal> arrayListMondai5= new ArrayList<>();
    ArrayList<Soal> arrayListMondai6= new ArrayList<>();
    final int jumlahSoal = 21;
    int selectedJlptLevel;
    int chapterLevel = 1, mondai1 = 0, mondai2 = 0, mondai3 = 0, mondai4 = 0, mondai5 = 0, mondai6 = 0;
    boolean readJsonSuccess = true;
    int totalKanji = 0, score = 0, cardCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_container);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Bundle bundleFromJlptMenu = getIntent().getBundleExtra(getString(R.string.bundle_jlpt_menu_adapter));
        selectedJlptLevel = bundleFromJlptMenu.getInt(getString(R.string.bundle_jlpt_menu_adapter));

        loadQuestion();

        pisahMondai("1",arrayListMondai1);
        pisahMondai("2",arrayListMondai2);
        pisahMondai("3",arrayListMondai3);
        pisahMondai("4",arrayListMondai4);
        pisahMondai("5",arrayListMondai5);
        pisahMondai("6",arrayListMondai6);
        while (mondai1 < 5) {
            int random = ThreadLocalRandom.current().nextInt(arrayListMondai1.size());
            arrayListSoalAkhir.add(arrayListMondai1.remove(random));
            mondai1++;
        }
        while (mondai2 < 4) {
            int random = ThreadLocalRandom.current().nextInt(arrayListMondai2.size());
            arrayListSoalAkhir.add(arrayListMondai2.remove(random));
            mondai2++;
        }
        while (mondai3 < 5) {
            int random = ThreadLocalRandom.current().nextInt(arrayListMondai3.size());
            arrayListSoalAkhir.add(arrayListMondai3.remove(random));
            mondai3++;
        }
        while (mondai4 < 3) {
            int random = ThreadLocalRandom.current().nextInt(arrayListMondai4.size());
            arrayListSoalAkhir.add(arrayListMondai4.remove(random));
            mondai4++;
        }
        while (mondai5 < 2) {
            int random = ThreadLocalRandom.current().nextInt(arrayListMondai5.size());
            arrayListSoalAkhir.add(arrayListMondai5.remove(random));
            mondai5++;
        }
        while (mondai6 < 2) {
            int random = ThreadLocalRandom.current().nextInt(arrayListMondai6.size());
            arrayListSoalAkhir.add(arrayListMondai6.remove(random));
            mondai6++;
        }

        Collections.sort(arrayListSoalAkhir);
        totalKanji = arrayListSoalAkhir.size();

        if (readJsonSuccess) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            jlptQuestionBunpouFragment = new JlptQuestionBunpouFragment();
            jlptResultBunpouFragment = new JlptResultBunpouFragment();
            fragmentTransaction.add(R.id.container, jlptQuestionBunpouFragment);
            fragmentTransaction.add(R.id.container, jlptResultBunpouFragment);
            fragmentTransaction.detach(jlptResultBunpouFragment);
            fragmentTransaction.attach(jlptQuestionBunpouFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
//            Log.e("GOJEPANG", "" + arrayListSoal.size());
        } else {
            Toast.makeText(JlptContainerBunpouActivity.this, "Coming soon",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void pisahMondai(String i, ArrayList<Soal> arrayListMondai) {
        for(int j=0;j<arrayListSoalSemua.size();j++){
            String mondai=arrayListSoalSemua.get(j).mondai;
            if(mondai.equals(i))
                arrayListMondai.add(arrayListSoalSemua.remove(j));
        }
    }

    private void loadQuestion() {
        switch (selectedJlptLevel) {
            case 0:
                chapterLevel = 1;
                loadFromJson(R.raw.jlptn5bunpou);
                break;
            case R.string.kanjitestlevel11:
                chapterLevel = 2;
                readJsonSuccess = false;
                break;
            case R.string.kanjitestlevel21:
                chapterLevel = 3;
                readJsonSuccess = false;
                break;
            case R.string.kanjitestlevel31:
                chapterLevel = 4;
                readJsonSuccess = false;
                break;
            case R.string.kanjitestlevel41:
                chapterLevel = 5;
                readJsonSuccess = false;
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
        int size = jsonObject.size();

        //memasukkan data dari node ke arraylist
        try {
            for (int i = 1; i <= size; i++) {
                //dapetin format contoh vc0101
                JsonObject jsonObjectKanji;
                String tempPrefixTreeJson = prefixTreeJson + i;
                jsonObjectKanji = jsonObject.getAsJsonObject(tempPrefixTreeJson);

                Soal soal = new Soal(
                        jsonObjectKanji.get("Soal").getAsString(),
                        jsonObjectKanji.get("ExtraSoal").getAsString(),
                        jsonObjectKanji.get("A").getAsString(),
                        jsonObjectKanji.get("B").getAsString(),
                        jsonObjectKanji.get("C").getAsString(),
                        jsonObjectKanji.get("D").getAsString(),
                        jsonObjectKanji.get("Jawaban").getAsString(),
                        jsonObjectKanji.get("Mondai").getAsString()
                );
                arrayListSoalSemua.add(soal);
//                    Log.e("GOEJAPNG", "prefix tree "+ tempPrefixLeafJson);

            }
        } catch (NullPointerException e) {
            Toast.makeText(JlptContainerBunpouActivity.this, "Coming soon",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            readJsonSuccess = false;
            finish();
        }
    }


    @Override
    public void onBackPressed() {

    }
}
