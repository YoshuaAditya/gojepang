package com.inocen.gojepang.activity.kanji_test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inocen.gojepang.R;
import com.inocen.gojepang.activity.MenuActivity;
import com.inocen.gojepang.databinding.KanjiTestHomeModel;

import java.util.ArrayList;
import java.util.List;

public class KanjiTestMenuActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    KanjiTestMenuAdapter adapter;
    ImageView imageViewBack, imageViewHome;
    List<KanjiTestHomeModel> kanjiTestHomeModelList;
    int lastKanjiTest=0;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_test_menu);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        kanjiTestHomeModelList = new ArrayList<>();
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        //add data for list
        //true berarti kunci kebuka, false kunci tutup
        kanjiTestHomeModelList.add(new KanjiTestHomeModel(R.string.kanjitestlevel1, true, R.drawable.kanji_level1, R.drawable.locker));
        kanjiTestHomeModelList.add(new KanjiTestHomeModel(R.string.kanjitestlevel11, false, R.drawable.kanji_level11, R.drawable.locker));
        kanjiTestHomeModelList.add(new KanjiTestHomeModel(R.string.kanjitestlevel21, false, R.drawable.kanji_level21, R.drawable.locker));
        kanjiTestHomeModelList.add(new KanjiTestHomeModel(R.string.kanjitestlevel31, false, R.drawable.kanji_level31, R.drawable.locker));
        kanjiTestHomeModelList.add(new KanjiTestHomeModel(R.string.kanjitestlevel41, false, R.drawable.kanji_level41, R.drawable.locker));

        setKanjiTestProgress();

        adapter = new KanjiTestMenuAdapter(this, kanjiTestHomeModelList);
        recyclerView.setAdapter(adapter);

        imageViewHome=findViewById(R.id.imageViewHome);
        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                finish();
            }
        });
    }

    /**
     * ambil variable lastChapter dari user untuk membuka chapter yang bersangkutan, coba cek di lokal dulu baru inet databae
     */
    private void setKanjiTestProgress() {
        //cek lokal dulu
        int defaultIntValue = -1;
        int lastKanjiTestPref = sharedPref.getInt(getString(R.string.result_last_kanji_test), defaultIntValue);
        if(lastKanjiTestPref!=defaultIntValue){
            for (int i=0;i<=lastKanjiTestPref;i++){
                kanjiTestHomeModelList.get(i).setLessonChapterLocker(true);
            }
//            return;
        }

        //cek ke database firebase jika lokal blm ada
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("users/"+firebaseUser.getUid());
        databaseReference
                .addValueEventListener
                        (new ValueEventListener() {
                             @Override
                             public void onDataChange(DataSnapshot dataSnapshot) {
                                 if (dataSnapshot.exists()) {
                                     Long temp=(Long)dataSnapshot.child("lastKanjiTest").getValue();
                                     lastKanjiTest = temp.intValue();
                                     for (int i=0;i<=lastKanjiTest;i++){
                                         kanjiTestHomeModelList.get(i).setLessonChapterLocker(true);
                                     }
                                 }
                             }

                             @Override
                             public void onCancelled(DatabaseError databaseError) {
                                 System.out.println("Internet error" );
//                                 System.out.println("Internet error: " + databaseError.getCode());
                             }
                         }
                        );
        int latestKanjiTest = lastKanjiTest > lastKanjiTestPref ? lastKanjiTest : lastKanjiTestPref;
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putInt(getString(R.string.result_last_kanji_test), latestKanjiTest);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
        finish();
    }

}
