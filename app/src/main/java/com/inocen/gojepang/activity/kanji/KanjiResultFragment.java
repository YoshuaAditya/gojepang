package com.inocen.gojepang.activity.kanji;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inocen.gojepang.R;
import com.inocen.gojepang.activity.ThankYouActivity;


public class KanjiResultFragment extends Fragment implements View.OnClickListener{
    private ImageView imageViewMedal;
    private TextView textViewResult, textViewPass, textViewScore, textViewScoreResult, textViewContinuation;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private boolean drawerToggle = true;
    KanjiContainerActivity kanjiContainerActivity;
    private int  lastKanji = 0;
    private ProgressBar progressBar;
    private String soundPath = "";
    private SharedPreferences.Editor editor;
    Handler handler;


    public KanjiResultFragment() {
        // Required empty public constructor
    }

    public static KanjiResultFragment newInstance(String param1, String param2) {
        KanjiResultFragment fragment = new KanjiResultFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.setOnClickListener(this);
        return inflater.inflate(R.layout.fragment_lesson_result, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        kanjiContainerActivity = (KanjiContainerActivity) getActivity();
        imageViewMedal = kanjiContainerActivity.findViewById(R.id.imageViewMedal);
        textViewResult = kanjiContainerActivity.findViewById(R.id.textViewResult);
        textViewScore = kanjiContainerActivity.findViewById(R.id.textViewScore);
        textViewScoreResult = kanjiContainerActivity.findViewById(R.id.textViewScoreResult);
        textViewContinuation = kanjiContainerActivity.findViewById(R.id.textViewContinuation);
        textViewPass = kanjiContainerActivity.findViewById(R.id.textViewPass);
        setScore();
    }

    private void setScore() {

        boolean checkPass = kanjiContainerActivity.score < 70;
//        if (false) { //TODO TESTING ganti ini kalo pingin test bener tanpa liat skor
        if(checkPass){
            imageViewMedal.setImageDrawable(getResources().getDrawable(R.drawable.ic_score_failed));
            textViewResult.setText(getString(R.string.toobad));
            textViewResult.setTextColor(Color.parseColor("#F0595D"));
            textViewPass.setText(getString(R.string.failedchapter));
            textViewContinuation.setText(getString(R.string.postponedchapter));
        }
        String score = kanjiContainerActivity.score + "/100";
        textViewScoreResult.setText(score);

        //buat nambah angka bab di textview pass dan continuation
        String temPass = textViewPass.getText().toString();
        String temp2Pass = ""+kanjiContainerActivity.chapterTitle;
        String tempArray[]=temp2Pass.split(" ");
        lastKanji = Integer.decode(tempArray[1]);
        String temp4Pass = Integer.toString(lastKanji);
        String textPass = temPass + " " + temp4Pass;

        if (!checkPass) setNextProgressKanji();
//        if (true) setNextProgressKanji(); //TODO TESTING kalo males jawab bener

        String temContinuation = textViewContinuation.getText().toString();
        String temp4Continuation = Integer.toString(lastKanji + 1);
        String textContinuation = temContinuation + " " + temp4Continuation;

        textViewPass.setText(textPass);
        textViewContinuation.setText(textContinuation);

//        handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(kanjiContainerActivity, ThankYouActivity.class));
//                kanjiContainerActivity.finish();
//            }
//        }, 5000);//TODO TESTING purpose, bisa berubah
    }

    /**
     * set value lastKanji di lokal sharedPref dan database firebase, jika lastKanji lebih kecil dengan chapter sekarang
     */
    private void setNextProgressKanji() {
        int defaultIntValue = -1;
        int lastKanjiPref = kanjiContainerActivity.sharedPref.getInt(getString(R.string.result_last_kanji), defaultIntValue);
        //kalo lebih besar berarti user cuma mengulang chapter sebelumnya
        if(lastKanjiPref>lastKanji)return;
        //lokal
        SharedPreferences.Editor editor;
        editor = kanjiContainerActivity.sharedPref.edit();
        editor.putInt(getString(R.string.result_last_kanji), lastKanji);
        editor.apply();

        //firebase
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("users/" + firebaseUser.getUid());
        databaseReference.child("lastKanji").setValue(lastKanji);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(kanjiContainerActivity, ThankYouActivity.class));
//        handler.removeCallbacksAndMessages(null);
        kanjiContainerActivity.finish();
    }

}
