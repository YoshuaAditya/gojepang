package com.inocen.gojepang.activity.lesson;

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
import com.inocen.gojepang.databinding.LessonHomeModel;

import java.util.ArrayList;
import java.util.List;

public class LessonHomeActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    LessonHomeAdapter adapter;
    List<LessonHomeModel> lessonHomeModelList;
    int lastChapter=0;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_home);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        FirebaseApp.initializeApp(this);

        lessonHomeModelList = new ArrayList<>();
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //add data for list
        //true berarti kunci kebuka, false kunci tutup
        lessonHomeModelList.add(new LessonHomeModel(R.string.chapter1, R.string.chaptertitle1,
                true, R.drawable.lesson_chapter_1, R.drawable.locker));
        lessonHomeModelList.add(new LessonHomeModel(R.string.chapter2, R.string.chaptertitle2,
                false, R.drawable.lesson_chapter_2, R.drawable.locker));
        lessonHomeModelList.add(new LessonHomeModel(R.string.chapter3, R.string.chaptertitle3,
                false, R.drawable.lesson_chapter_3, R.drawable.locker));
        lessonHomeModelList.add(new LessonHomeModel(R.string.chapter4, R.string.chaptertitle4,
                false, R.drawable.lesson_chapter_4, R.drawable.locker));
        lessonHomeModelList.add(new LessonHomeModel(R.string.chapter5, R.string.chaptertitle5,
                false, R.drawable.lesson_chapter_5, R.drawable.locker));
        lessonHomeModelList.add(new LessonHomeModel(R.string.chapter6, R.string.chaptertitle6,
                false, R.drawable.lesson_chapter_6, R.drawable.locker));
        lessonHomeModelList.add(new LessonHomeModel(R.string.chapter7, R.string.chaptertitle7,
                false, R.drawable.lesson_chapter_7, R.drawable.locker));
        lessonHomeModelList.add(new LessonHomeModel(R.string.chapter8, R.string.chaptertitle8,
                false, R.drawable.lesson_chapter_8, R.drawable.locker));
        lessonHomeModelList.add(new LessonHomeModel(R.string.chapter9, R.string.chaptertitle9,
                false, R.drawable.lesson_chapter_9, R.drawable.locker));
        lessonHomeModelList.add(new LessonHomeModel(R.string.chapter10, R.string.chaptertitle10,
                false, R.drawable.lesson_chapter_10, R.drawable.locker));

        setChapterProgress();

        adapter = new LessonHomeAdapter(this, lessonHomeModelList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
        finish();
    }

    /**
     * ambil variable lastChapter dari user untuk membuka chapter yang bersangkutan, coba cek di lokal dulu baru inet databae
     */
    private void setChapterProgress() {
        //cek lokal dulu
        int defaultIntValue = -1;
        int lastChapterPref = sharedPref.getInt(getString(R.string.result_last_chapter), defaultIntValue);
        if(lastChapterPref!=defaultIntValue){
            for (int i=0;i<=lastChapterPref;i++){
                lessonHomeModelList.get(i).setLessonChapterLocker(true);
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
                                     Long temp=(Long)dataSnapshot.child("lastChapter").getValue();
                                     lastChapter = temp.intValue();
                                     for (int i=0;i<=lastChapter;i++){
                                         lessonHomeModelList.get(i).setLessonChapterLocker(true);
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
        int latestChapter = lastChapter > lastChapterPref ? lastChapter : lastChapterPref;
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putInt(getString(R.string.result_last_chapter), latestChapter);
        editor.apply();
    }
}
