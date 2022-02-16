package com.inocen.gojepang.activity.kanji;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inocen.gojepang.R;
import com.inocen.gojepang.activity.MenuActivity;
import com.inocen.gojepang.databinding.KanjiHomeModel;

import java.util.ArrayList;
import java.util.List;

public class KanjiHomeActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    KanjiHomeAdapter kanjiHomeAdapter;
    List<KanjiHomeModel> kanjiHomeModelList;
    int lastKanji=0;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_home);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        FirebaseApp.initializeApp(this);

        kanjiHomeModelList = new ArrayList<>();
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        //add data for list
        //true berarti kunci kebuka, false kunci tutup
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji1, true, R.drawable.ic_kanji1, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji2, false, R.drawable.ic_kanji2, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji3, false, R.drawable.ic_kanji3, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji4, false, R.drawable.ic_kanji4, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji5, false, R.drawable.ic_kanji5, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji6, false, R.drawable.ic_kanji6, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji7, false, R.drawable.ic_kanji7, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji8, false, R.drawable.ic_kanji8, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji9, false, R.drawable.ic_kanji9, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji10, false, R.drawable.ic_kanji10, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji11, false, R.drawable.ic_kanji11, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji12, false, R.drawable.ic_kanji12, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji13, false, R.drawable.ic_kanji13, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji14, false, R.drawable.ic_kanji14, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji15, false, R.drawable.ic_kanji15, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji16, false, R.drawable.ic_kanji16, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji17, false, R.drawable.ic_kanji17, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji18, false, R.drawable.ic_kanji18, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji19, false, R.drawable.ic_kanji19, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji20, false, R.drawable.ic_kanji20, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji21, false, R.drawable.ic_kanji21, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji22, false, R.drawable.ic_kanji22, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji23, false, R.drawable.ic_kanji23, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji24, false, R.drawable.ic_kanji24, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji25, false, R.drawable.ic_kanji25, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji26, false, R.drawable.ic_kanji26, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji27, false, R.drawable.ic_kanji27, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji28, false, R.drawable.ic_kanji28, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji29, false, R.drawable.ic_kanji29, R.drawable.locker));
        kanjiHomeModelList.add(new KanjiHomeModel(R.string.levelkanji30, false, R.drawable.ic_kanji30, R.drawable.locker));

        setKanjiProgress();

        kanjiHomeAdapter = new KanjiHomeAdapter(this, kanjiHomeModelList);
        recyclerView.setAdapter(kanjiHomeAdapter);
    }

    /**
     * ambil variable lastKanji dari user untuk membuka kanji yang bersangkutan, coba cek di lokal dulu baru inet databae
     */
    private void setKanjiProgress() {
        //cek lokal dulu
        int defaultIntValue = -1;
        int lastKanjiPref = sharedPref.getInt(getString(R.string.result_last_kanji), defaultIntValue);
        if(lastKanjiPref!=defaultIntValue){
            for (int i=0;i<=lastKanjiPref;i++){
                kanjiHomeModelList.get(i).setLessonChapterLocker(true);
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
                                     Long temp=(Long)dataSnapshot.child("lastKanji").getValue();
                                     lastKanji = temp.intValue();
                                     for (int i=0;i<=lastKanji;i++){
                                         kanjiHomeModelList.get(i).setLessonChapterLocker(true);
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
        int latestKanji = lastKanji > lastKanjiPref ? lastKanji : lastKanjiPref;
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putInt(getString(R.string.result_last_kanji), latestKanji);
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
