package com.inocen.gojepang.activity.card_list;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.inocen.gojepang.R;
import com.inocen.gojepang.activity.MenuActivity;
import com.inocen.gojepang.activity.kanji_test.KanjiTestContainerActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CardListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<CardModel> cardModelList = new ArrayList<>();
    ;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    CardAdapter cardAdapter;
    ImageView imageViewSetting, imageViewRomaji, imageViewKana, imageViewKanji;
    String writerTemp = "";
    String prefixTreeJson = "vc";
    String tempResChapter = "vl";
    boolean drawerToggle = true;
    int lastChapterPref = 0, lastKanjiPref = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        FirebaseApp.initializeApp(this);
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setItemViewCacheSize(30);

        getLessonProgress();
        getKanjiProgress();
        addCardLesson();
        addCardKanji();

        cardAdapter = new CardAdapter(this, cardModelList);
        cardAdapter.setHasStableIds(true);
        recyclerView.setAdapter(cardAdapter);

        TextView textView = findViewById(R.id.textViewYouHaveCollecting);
        String temp = textView.getText() + " " + cardModelList.size() + " " + getString(R.string.cards);
        textView.setText(temp);

        settingOnClick();

    }

    private void settingOnClick() {
        imageViewSetting = findViewById(R.id.imageViewSetting);
        imageViewSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawerCharacter();
            }
        });

        imageViewRomaji = findViewById(R.id.imageViewRomaji);
        imageViewRomaji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawerCharacter();
                toggleCardPreference("romaji");
            }
        });

        imageViewKana = findViewById(R.id.imageViewKana);
        imageViewKana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawerCharacter();
                toggleCardPreference("kana");
            }
        });

        imageViewKanji = findViewById(R.id.imageViewKanji);
        imageViewKanji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawerCharacter();
                toggleCardPreference("kanji");
            }
        });


        CircleImageView circleImageView = findViewById(R.id.circleImageViewBack);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = sharedPref.edit();
                editor.remove(getString(R.string.card_list_type_romaji));
                editor.apply();
                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                finish();
            }
        });
    }

    private void toggleCardPreference(String pref) {
        editor = sharedPref.edit();
        editor.putString(getString(R.string.card_list_type_romaji), pref);
        editor.apply();

        Intent intent = new Intent(CardListActivity.this, CardListActivity.class);
        startActivity(intent);
        finish();
    }

    public void toggleDrawerCharacter() {
        if (drawerToggle) {
            imageViewRomaji.setVisibility(View.VISIBLE);
            imageViewKana.setVisibility(View.VISIBLE);
            imageViewKanji.setVisibility(View.VISIBLE);
            drawerToggle = !drawerToggle;
        } else {
            imageViewRomaji.setVisibility(View.INVISIBLE);
            imageViewKana.setVisibility(View.INVISIBLE);
            imageViewKanji.setVisibility(View.INVISIBLE);
            drawerToggle = !drawerToggle;
        }
    }

    private void addCardLesson() {
        if (lastChapterPref == 0) return;
        prefixTreeJson = "vc01";
        tempResChapter = "vl01f";
        loadFromJsonLesson(R.raw.chapter1);

        if (lastChapterPref == 1) return;
        prefixTreeJson = "vc02";
        tempResChapter = "vl02f";
        loadFromJsonLesson(R.raw.chapter2);

        if (lastChapterPref == 2) return;
        prefixTreeJson = "vc03";
        tempResChapter = "vl03f";
        loadFromJsonLesson(R.raw.chapter3);

        if (lastChapterPref == 3) return;
        prefixTreeJson = "vc04";
        tempResChapter = "vl04f";
        loadFromJsonLesson(R.raw.chapter4);

        if (lastChapterPref == 4) return;
        prefixTreeJson = "vc05";
        tempResChapter = "vl05f";
        loadFromJsonLesson(R.raw.chapter5);

//        if(lastChapterPref==2)return;
        prefixTreeJson = "vc06";
        tempResChapter = "vl06f";
//                loadFromJsonLesson(R.raw.kanji6);

//        if(lastChapterPref==2)return;
        prefixTreeJson = "vc07";
        tempResChapter = "vl07f";
//                loadFromJsonLesson(R.raw.kanji7);

//        if(lastChapterPref==2)return;
        prefixTreeJson = "vc08";
        tempResChapter = "vl08f";
//                loadFromJsonLesson(R.raw.kanji8);

//        if(lastChapterPref==2)return;
        prefixTreeJson = "vc09";
        tempResChapter = "vl09f";
//                loadFromJsonLesson(R.raw.kanji9);

//        if(lastChapterPref==2)return;
        prefixTreeJson = "vc10";
        tempResChapter = "vl10f";
//                loadFromJsonLesson(R.raw.kanji10);
    }

    /**
     * membaca chapter1 dari json lalu mendapatkan variabel tipe JsonObject
     */
    private void loadFromJsonLesson(int rawChapterJson) {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            br = new BufferedReader(new InputStreamReader(getResources().openRawResource(rawChapterJson), "windows-31j"));
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
                parseWordsLesson(obj.getAsJsonObject("words"));
                br.close(); // stop reading
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param jsonObject jsonObject yang sudah masuk 1 nest, nest chapter1 lalu diparse untuk dimasukkan ke string array
     */
    private void parseWordsLesson(JsonObject jsonObject) {
        int size = jsonObject.size();

        //cek preference user dari bahasa dan character
        try {
            for (int i = 1; i <= size; i++) {
                JsonObject jsonObjectWords;
                String resName = tempResChapter;
                if (i < 10) {
                    jsonObjectWords = jsonObject.getAsJsonObject(prefixTreeJson + "0" + i);
                    resName += "0" + i + ".png";
                } else {
                    jsonObjectWords = jsonObject.getAsJsonObject(prefixTreeJson + i);
                    resName += i + ".png";
                }

                CardModel cardModel = new CardModel(cardModelList.size() + 1,
                        resName,
                        jsonObjectWords.get("kanji").getAsString(),
                        true,
                        jsonObjectWords.get("kana").getAsString(),
                        jsonObjectWords.get("romaji").getAsString(),
                        false,
                        "", "", "", ""
                );
                cardModelList.add(cardModel);
            }
        } catch (NullPointerException e) {
            Toast.makeText(CardListActivity.this, "Error in reading chapter JSON",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void addCardKanji() {
        if (lastKanjiPref == 0) return;
        prefixTreeJson = "vc01";
        loadFromJsonKanji(R.raw.kanji1);

        if (lastKanjiPref == 1) return;
        prefixTreeJson = "vc02";
        loadFromJsonKanji(R.raw.kanji2);

        if (lastKanjiPref == 2) return;
        prefixTreeJson = "vc03";
        loadFromJsonKanji(R.raw.kanji3);

        if (lastKanjiPref == 3) return;
        prefixTreeJson = "vc04";
        loadFromJsonKanji(R.raw.kanji4);

        if (lastKanjiPref == 4) return;
        prefixTreeJson = "vc05";
        loadFromJsonKanji(R.raw.kanji5);

        if (lastKanjiPref == 5) return;
        prefixTreeJson = "vc06";
        loadFromJsonKanji(R.raw.kanji6);

        if (lastKanjiPref == 6) return;
        prefixTreeJson = "vc07";
        loadFromJsonKanji(R.raw.kanji7);

        if (lastKanjiPref == 7) return;
        prefixTreeJson = "vc08";
        loadFromJsonKanji(R.raw.kanji8);

        if (lastKanjiPref == 8) return;
        prefixTreeJson = "vc09";
        loadFromJsonKanji(R.raw.kanji9);

        if (lastKanjiPref == 9) return;
        prefixTreeJson = "vc10";
        loadFromJsonKanji(R.raw.kanji10);

        //TODO UPDATE ini habis konten kanji dan lesson nambah
//        if (lastKanjiPref == 10) return;
//        prefixTreeJson = "vc11";
//        loadFromJsonKanji(R.raw.kanji11);
//
//        if (lastKanjiPref == 11) return;
//        prefixTreeJson = "vc12";
//        loadFromJsonKanji(R.raw.kanji12);
    }

    private void getLessonProgress() {
        //cek lokal dulu
        int defaultIntValue = -1;
        int tempChapterPref = sharedPref.getInt(getString(R.string.result_last_chapter), defaultIntValue);
        if (tempChapterPref >= lastChapterPref) {
            lastChapterPref = tempChapterPref;
            return;
        }

        //cek ke database firebase jika lokal blm ada
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("users/" + firebaseUser.getUid());
        databaseReference
                .addValueEventListener
                        (new ValueEventListener() {
                             @Override
                             public void onDataChange(DataSnapshot dataSnapshot) {
                                 if (dataSnapshot.exists()) {
                                     Long temp = (Long) dataSnapshot.child("lastChapter").getValue();
                                     int lastChapter = temp.intValue();
                                     lastChapterPref = lastChapter;
                                     SharedPreferences.Editor editor;
                                     editor = sharedPref.edit();
                                     editor.putInt(getString(R.string.result_last_chapter), lastChapter);
                                     editor.apply();
                                 }
                             }

                             @Override
                             public void onCancelled(DatabaseError databaseError) {
                                 System.out.println("Internet error");
//                                 System.out.println("Internet error: " + databaseError.getCode());
                             }
                         }
                        );
    }

    private void getKanjiProgress() {
        //cek lokal dulu
        int defaultIntValue = -1;
        int tempKanjiPref = sharedPref.getInt(getString(R.string.result_last_kanji), defaultIntValue);
        if (tempKanjiPref >= lastKanjiPref) {
            lastKanjiPref = tempKanjiPref;
            return;
        }

        //cek ke database firebase jika lokal blm ada
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("users/" + firebaseUser.getUid());
        databaseReference
                .addValueEventListener
                        (new ValueEventListener() {
                             @Override
                             public void onDataChange(DataSnapshot dataSnapshot) {
                                 if (dataSnapshot.exists()) {
                                     Long temp = (Long) dataSnapshot.child("lastKanji").getValue();
                                     int lastKanji = temp.intValue();
                                     lastKanjiPref = lastKanji;
                                     SharedPreferences.Editor editor;
                                     editor = sharedPref.edit();
                                     editor.putInt(getString(R.string.result_last_kanji), lastKanji);
                                     editor.apply();

                                 }
                             }

                             @Override
                             public void onCancelled(DatabaseError databaseError) {
                                 System.out.println("Internet error");
//                                 System.out.println("Internet error: " + databaseError.getCode());
                             }
                         }
                        );
    }

    private void loadFromJsonKanji(int rawKanjiJson) {
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
                parseWordsKanji(obj);
                br.close(); // stop reading
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param jsonObject jsonObject yang sudah masuk 1 nest, nest chapter1 lalu diparse untuk dimasukkan ke string array
     */
    private void parseWordsKanji(JsonObject jsonObject) {

        //masuk ke node kanjis pada json
        JsonObject jsonObjectKanjis = jsonObject.getAsJsonObject("kanjis");
        int size = jsonObjectKanjis.size();

        //memasukkan data dari node ke arraylist
        try {
            for (int i = 1; i <= size; i++) {
                //dapetin format contoh vc0101
                JsonObject jsonObjectKanji;
                String tempPrefixTreeJson = prefixTreeJson;
                if (i < 10) {
                    tempPrefixTreeJson += "0" + i;
                } else {
                    tempPrefixTreeJson += i;
                }
                jsonObjectKanji = jsonObjectKanjis.getAsJsonObject(tempPrefixTreeJson);

                int childSize = jsonObjectKanji.size();
                for (int j = 0; j < childSize; j++) {

                    //format contoh vc0101_0
                    String tempPrefixLeafJson = tempPrefixTreeJson + "_" + j;

                    JsonObject jsonObjectKanjiLeaf = jsonObjectKanji.getAsJsonObject(tempPrefixLeafJson);

                    CardModel cardModel = new CardModel(cardModelList.size() + 1,
                            "",
                            jsonObjectKanjiLeaf.get("kanji").getAsString(),
                            false,
                            "",
                            "",
                            true,
                            jsonObjectKanjiLeaf.get("hiraganakun").getAsString(),
                            jsonObjectKanjiLeaf.get("hiraganaon").getAsString(),
                            jsonObjectKanjiLeaf.get("romajikun").getAsString(),
                            jsonObjectKanjiLeaf.get("romajion").getAsString()
                    );
                    cardModelList.add(cardModel);
//                    Log.e("GOEJAPNG", "prefix tree "+ tempPrefixLeafJson);
                }
            }
        } catch (NullPointerException e) {
            Toast.makeText(CardListActivity.this, "Error in reading kanji JSON",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        editor = sharedPref.edit();
        editor.remove(getString(R.string.card_list_type_romaji));
        editor.apply();
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
        finish();
        super.onBackPressed();
    }
}
