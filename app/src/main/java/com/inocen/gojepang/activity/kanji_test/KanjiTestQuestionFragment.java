package com.inocen.gojepang.activity.kanji_test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.inocen.gojepang.R;
import com.inocen.gojepang.activity.MenuActivity;
import com.inocen.gojepang.activity.kanji.KanjiContainerActivity;
import com.inocen.gojepang.activity.kanji.KanjiIntroActivity;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class KanjiTestQuestionFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button buttonRow1, buttonRow2, buttonRow3, buttonRow4;
    ArrayList<Integer> numbersNode = new ArrayList<Integer>();
    ArrayList<Integer> numbersLeaf = new ArrayList<Integer>();
    ArrayList<String> numberCheckContain = new ArrayList<String>();
    ArrayList<Integer> randomQuestionNode = new ArrayList<Integer>();
    ArrayList<Integer> randomQuestionLeaf = new ArrayList<Integer>();
    ArrayList<String> randomQuestionCheckContain = new ArrayList<String>();
    private Button buttonQuestionCard;
    private ImageView imageViewOpenDrawer, imageViewRomaji, imageViewKana,  imageViewClose;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private boolean drawerToggle = true;
    KanjiTestContainerActivity kanjiTestContainerActivity;
    private int kanjiStartCount = 0, kanjiNodeCount = 0, kanjiLeafCount = 0, cardPointer = 0, cardCount = 0;
    ProgressBar progressBar;
    private boolean hasAnswered = false;
    private SharedPreferences.Editor editor;
    int ansNode1, ansNode2, ansNode3, ansNode4,ansLeaf1, ansLeaf2, ansLeaf3, ansLeaf4, score = 0;
    Handler handler=new Handler();

    private String mParam1;
    private String mParam2;

    public KanjiTestQuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create wordsArray new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewCardFragment.
     */
    public static KanjiTestQuestionFragment newInstance(String param1, String param2) {
        KanjiTestQuestionFragment fragment = new KanjiTestQuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kanji_multiple_choices_question_layout, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        randomSeed();
        setKanjiText();
        progressBar = getActivity().findViewById(R.id.progressBar);
        progressBar.setProgress(cardCount * 100 / kanjiTestContainerActivity.totalKanji);
    }


    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        kanjiTestContainerActivity = (KanjiTestContainerActivity) getActivity();
        initializeOnClick();
    }

    private void setKanjiText() {
//        Log.e("GOJEPANG", " randomQuestionNodeSize "+randomQuestionNode.size()
//                +" arraylistkanji "+kanjiTestContainerActivity.arrayListKanji.size());
        String temp = kanjiTestContainerActivity.arrayListKanji.get(randomQuestionNode.get(cardCount));
        buttonQuestionCard.setText(temp);
        randomizeButtonText();
    }

    private void randomSeed() {
        randomQuestionNode.clear();
        while (randomQuestionNode.size()!=kanjiTestContainerActivity.totalKanji) {
            int random = ThreadLocalRandom.current().nextInt(kanjiTestContainerActivity.totalKanji);
            if (!randomQuestionNode.contains(random)) {
                randomQuestionNode.add(random);
            }
        }
//        Log.e("GOJEPANG", " randomQuestionCheckContain "+randomQuestionCheckContain.toString());
    }

    private void randomizeButtonText() {
        numbersNode.add(randomQuestionNode.get(cardCount));
        while (numbersNode.size()!=4) {
            int random = ThreadLocalRandom.current().nextInt(kanjiTestContainerActivity.totalKanji);
            if (!numbersNode.contains(random)) {
                numbersNode.add(random);
            }
        }

        String defaultLanguageValue = getResources().getString(R.string.sharedPrefLanguage);
        String languagePref = kanjiTestContainerActivity.sharedPref.getString(getString(R.string.sharedPrefLanguage), defaultLanguageValue);
        ArrayList<String> selectedLanguage = null;
        switch (languagePref) {
            case "en":
                selectedLanguage = kanjiTestContainerActivity.arrayListEnglish;
                break;
            case "in":
                selectedLanguage = kanjiTestContainerActivity.arrayListIndonesia;
                break;
        }

        int random = new Random().nextInt(numbersNode.size());
        ansNode1=numbersNode.get(random);
        buttonRow1.setText(selectedLanguage.get(ansNode1));
        numbersNode.remove(random);

        random = new Random().nextInt(numbersNode.size());
        ansNode2=numbersNode.get(random);
        buttonRow2.setText(selectedLanguage.get(ansNode2));
        numbersNode.remove(random);

        random = new Random().nextInt(numbersNode.size());
        ansNode3=numbersNode.get(random);
        buttonRow3.setText(selectedLanguage.get(ansNode3));
        numbersNode.remove(random);

        ansNode4=numbersNode.get(0);
        buttonRow4.setText(selectedLanguage.get(ansNode4));
        numbersNode.remove(0);

//        Log.e("GOJEPANG", "ans1 " + ans1 + " ans2 " + ans2 + " ans3 " + ans3 + " ans4 " + ans4
//                + " offset " + offset + " kanjiStartCount " + kanjiStartCount);
    }

    private void initializeOnClick() {
        buttonQuestionCard = getActivity().findViewById(R.id.buttonQuestion);

        imageViewRomaji = getActivity().findViewById(R.id.imageViewRomaji);
        imageViewRomaji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCharacterPreference("alphabet");
                toggleDrawerCharacter();
            }
        });

        imageViewKana = getActivity().findViewById(R.id.imageViewKana);
        imageViewKana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCharacterPreference("kana");
                toggleDrawerCharacter();
            }
        });

        imageViewOpenDrawer = getActivity().findViewById(R.id.imageViewOpenDrawer);
        imageViewOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawerCharacter();
            }
        });

        buttonRow1 = getActivity().findViewById(R.id.buttonAnswer1);
        buttonRow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(ansNode1,ansLeaf1, v);
            }
        });

        buttonRow2 = getActivity().findViewById(R.id.buttonAnswer2);
        buttonRow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(ansNode2,ansLeaf2, v);
            }
        });

        buttonRow3 = getActivity().findViewById(R.id.buttonAnswer3);
        buttonRow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(ansNode3,ansLeaf3, v);
            }
        });

        buttonRow4 = getActivity().findViewById(R.id.buttonAnswer4);
        buttonRow4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(ansNode4,ansLeaf4, v);
            }
        });


        imageViewClose = getActivity().findViewById(R.id.imageViewClose);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                Intent i = new Intent(getActivity(), MenuActivity.class);
                startActivity(i);
                kanjiTestContainerActivity.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                kanjiTestContainerActivity.finish();
            }
        });

    }

    public void changeCharacterPreference(String characterPreference) {
        editor = kanjiTestContainerActivity.sharedPref.edit();
        editor.putString(getString(R.string.sharedPrefCharacter), characterPreference);
        editor.apply();
    }

    public void toggleDrawerCharacter() {
        if (drawerToggle) {
            imageViewRomaji.setVisibility(View.VISIBLE);
            imageViewKana.setVisibility(View.VISIBLE);
            drawerToggle = !drawerToggle;
        } else {
            imageViewRomaji.setVisibility(View.INVISIBLE);
            imageViewKana.setVisibility(View.INVISIBLE);
            drawerToggle = !drawerToggle;
        }
    }


    /**
     * @param answerNode
     * @param answerLeaf dari tombol keberepa yang dipencet
     * @param v      diperlukan untuk mengeset warna tombol yang dipencet
     *               boolean hasAnswered digunakan untuk mencegah user memencet jawaban berkali-kali
     *               cek answer dari menyamakan kartu keberapa dengan tombol yang menyimpan index keberapa dia ambil textnya
     *               kalo kartu habis (!cardNotExhausted) saatnya ke layout skor
     *               setelah 4 pertanyaan pindah lagi ke NewCardFragment
     */
    public void checkAnswer(int answerNode,int answerLeaf, View v) {
        if (hasAnswered) return;
        int delayTime = 800;
        if (answerNode == randomQuestionNode.get(cardCount)) {
            v.setBackgroundColor(getResources().getColor(R.color.colorrightanswer));
            score++;
            //sfx benar
            final MediaPlayer mediaPlayer = MediaPlayer.create(kanjiTestContainerActivity, R.raw.correct);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                }
            });
            mediaPlayer.start();
        } else {
            v.setBackgroundColor(getResources().getColor(R.color.colorwronganswer));
            if (ansNode1 == randomQuestionNode.get(cardCount)) {
                buttonRow1.setBackgroundColor(getResources().getColor(R.color.colorrightanswer));
            } else if (ansNode2 == randomQuestionNode.get(cardCount)) {
                buttonRow2.setBackgroundColor(getResources().getColor(R.color.colorrightanswer));
            } else if (ansNode3 == randomQuestionNode.get(cardCount)) {
                buttonRow3.setBackgroundColor(getResources().getColor(R.color.colorrightanswer));
            } else {
                buttonRow4.setBackgroundColor(getResources().getColor(R.color.colorrightanswer));
            }
            //sfx salah
            final MediaPlayer mediaPlayer = MediaPlayer.create(kanjiTestContainerActivity, R.raw.wrong);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                }
            });
            mediaPlayer.start();
            delayTime = 1500;
        }
        cardCount++;
        progressBar.setProgress(cardCount * 100 / kanjiTestContainerActivity.totalKanji);
        hasAnswered = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonRow1.setBackgroundColor(getResources().getColor(R.color.colorWhitePure));
                buttonRow2.setBackgroundColor(getResources().getColor(R.color.colorWhitePure));
                buttonRow3.setBackgroundColor(getResources().getColor(R.color.colorWhitePure));
                buttonRow4.setBackgroundColor(getResources().getColor(R.color.colorWhitePure));
                hasAnswered = false;
                boolean cardNotExhausted = cardCount < kanjiTestContainerActivity.totalKanji;
                if (!cardNotExhausted) {
                    score = score * 100 / kanjiTestContainerActivity.totalKanji;
                    hasAnswered = true;

                    kanjiTestContainerActivity.score = score;
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.detach(kanjiTestContainerActivity.kanjiTestQuestionFragment);
                    fragmentTransaction.attach(kanjiTestContainerActivity.kanjiTestResultFragment);
                    fragmentTransaction.commit();
                    return;
                }
                //alasan card multiple 4 biar gambar gk keliatan ganti pas ganti ke question fragment
                else{
                    setKanjiText();
                }
            }
//        }, 100);
        }, delayTime);
    }
}
