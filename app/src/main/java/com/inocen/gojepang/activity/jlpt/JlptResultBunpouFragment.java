package com.inocen.gojepang.activity.jlpt;

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


public class JlptResultBunpouFragment extends Fragment implements View.OnClickListener{
    private ImageView imageViewMedal;
    private TextView textViewResult, textViewPass, textViewScore, textViewScoreResult, textViewContinuation;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private boolean drawerToggle = true;
    JlptContainerBunpouActivity jlptContainerBunpouActivity;
    private int lastJlpt = 0;
    private ProgressBar progressBar;
    private String soundPath = "";
    private SharedPreferences.Editor editor;
    Handler handler;


    public JlptResultBunpouFragment() {
        // Required empty public constructor
    }

    public static JlptResultBunpouFragment newInstance(String param1, String param2) {
        JlptResultBunpouFragment fragment = new JlptResultBunpouFragment();
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
        jlptContainerBunpouActivity = (JlptContainerBunpouActivity) getActivity();
        imageViewMedal = jlptContainerBunpouActivity.findViewById(R.id.imageViewMedal);
        textViewResult = jlptContainerBunpouActivity.findViewById(R.id.textViewResult);
        textViewScore = jlptContainerBunpouActivity.findViewById(R.id.textViewScore);
        textViewScoreResult = jlptContainerBunpouActivity.findViewById(R.id.textViewScoreResult);
        textViewContinuation = jlptContainerBunpouActivity.findViewById(R.id.textViewContinuation);
        textViewPass = jlptContainerBunpouActivity.findViewById(R.id.textViewPass);
        setScore();
    }

    private void setScore() {

        boolean checkPass = jlptContainerBunpouActivity.score < 70;
//        if (false) { //TODO TESTING ganti ini kalo pingin test bener tanpa liat skor
        if(checkPass){
            imageViewMedal.setImageDrawable(getResources().getDrawable(R.drawable.ic_score_failed));
            textViewResult.setText(getString(R.string.toobad));
            textViewResult.setTextColor(Color.parseColor("#F0595D"));
            textViewPass.setText(getString(R.string.failedchapter));
            textViewContinuation.setText(getString(R.string.postponedchapter));
        }
        String score = jlptContainerBunpouActivity.score + "/100";
        textViewScoreResult.setText(score);

        //buat nambah angka bab di textview pass dan continuation
        String temPass = textViewPass.getText().toString() + " JLPT N";
        lastJlpt = 6 - jlptContainerBunpouActivity.chapterLevel;
        String temp4Pass = Integer.toString(lastJlpt);
        String textPass = temPass + temp4Pass;

        if (!checkPass) setNextProgressKanji();
//        if (true) setNextProgressKanji(); //TODO TESTING kalo males jawab bener

        //TODO ini bakal keluar n0 pas selesai n1, nanti fix
        String temContinuation = textViewContinuation.getText().toString()+ " JLPT N";
        String temp4Continuation = Integer.toString(lastJlpt - 1);
        String textContinuation = temContinuation + temp4Continuation;

        textViewPass.setText(textPass);
        textViewContinuation.setText(textContinuation);

//        handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(kanjiTestContainerActivity, ThankYouActivity.class));
//                kanjiTestContainerActivity.finish();
//            }
//        }, 5000);//TODO TESTING purpose, bisa berubah
    }

    /**
     * set value lastKanjiTest di lokal sharedPref dan database firebase, jika lastKanjiTest lebih kecil dengan chapter sekarang
     */
    private void setNextProgressKanji() {
        int defaultIntValue = -1;
        int lastKanjiPref = jlptContainerBunpouActivity.sharedPref.getInt(getString(R.string.result_last_jlpt), defaultIntValue);
        //kalo lebih besar berarti user cuma mengulang chapter sebelumnya
        if(lastKanjiPref> jlptContainerBunpouActivity.chapterLevel)return;
        //lokal
        SharedPreferences.Editor editor;
        editor = jlptContainerBunpouActivity.sharedPref.edit();
        editor.putInt(getString(R.string.result_last_jlpt), jlptContainerBunpouActivity.chapterLevel);
        editor.apply();

        //firebase
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("users/" + firebaseUser.getUid());
        databaseReference.child("lastJlpt").setValue(lastJlpt);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(jlptContainerBunpouActivity, ThankYouActivity.class));
//        handler.removeCallbacksAndMessages(null);
        jlptContainerBunpouActivity.finish();
    }
}
