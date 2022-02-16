package com.inocen.gojepang.activity.kanji;

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

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class KanjiQuestionFragment extends Fragment {
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
    KanjiContainerActivity kanjiContainerActivity;
    private int kanjiStartCount = 0, kanjiNodeCount = 0, kanjiLeafCount = 0, cardPointer = 0, cardCount = 0;
    ProgressBar progressBar;
    private boolean hasAnswered = false;
    private SharedPreferences.Editor editor;
    int ansNode1, ansNode2, ansNode3, ansNode4,ansLeaf1, ansLeaf2, ansLeaf3, ansLeaf4, score = 0;
    Handler handler=new Handler();

    private String mParam1;
    private String mParam2;

    public KanjiQuestionFragment() {
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
    public static KanjiQuestionFragment newInstance(String param1, String param2) {
        KanjiQuestionFragment fragment = new KanjiQuestionFragment();
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
        progressBar.setProgress(kanjiContainerActivity.progress * 100 / (kanjiContainerActivity.totalKanji*2));
    }


    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        kanjiContainerActivity = (KanjiContainerActivity) getActivity();
        initializeOnClick();
    }

    private void setKanjiText() {
        String temp = KanjiIntroActivity.arrayListKanjiRoot.get(randomQuestionNode.get(cardPointer))
                .get(randomQuestionLeaf.get(cardPointer)).get(kanjiContainerActivity.KANJI);
        buttonQuestionCard.setText(temp);
        randomizeButtonText();
    }

    private void randomSeed() {
        randomQuestionNode.clear();
        randomQuestionLeaf.clear();
        randomQuestionCheckContain.clear();
        int rootSize=KanjiIntroActivity.arrayListKanjiRoot.size();
        while (randomQuestionLeaf.size() != kanjiContainerActivity.progressBarValue-cardCount) {
            int maxNode= kanjiStartCount+2<rootSize?kanjiStartCount+2:kanjiStartCount+1;
            int randomNodeSeed= ThreadLocalRandom.current().nextInt(kanjiNodeCount,maxNode);
            int maxLeaf= KanjiIntroActivity.arrayListKanjiRoot.get(randomNodeSeed).size();
            int randomLeafSeed= ThreadLocalRandom.current().nextInt(maxLeaf);
            String checkContain = randomNodeSeed+","+randomLeafSeed;
            if (!randomQuestionCheckContain.contains(checkContain)) {
                randomQuestionNode.add(randomNodeSeed);
                randomQuestionLeaf.add(randomLeafSeed);
                randomQuestionCheckContain.add(checkContain);
            }
        }
//        Log.e("GOJEPANG", " randomQuestionCheckContain "+randomQuestionCheckContain.toString());
    }

    private void randomizeButtonText() {
        numbersNode.add(randomQuestionNode.get(cardPointer));
        numbersLeaf.add(randomQuestionLeaf.get(cardPointer));
        numberCheckContain.clear();
        numberCheckContain.add(randomQuestionNode.get(cardPointer)+","+randomQuestionLeaf.get(cardPointer));
        int rootSize=KanjiIntroActivity.arrayListKanjiRoot.size();
        while (numbersLeaf.size()!=4) {
            //random mengambil number dari cardccount newcardfragment supaya bertahap 2 kartu random, gk semua lnsg dirandom
            int maxNode= kanjiStartCount+2<rootSize?kanjiStartCount+2:kanjiStartCount;
            int randomNode = new Random().nextInt(maxNode);
            int maxLeaf= KanjiIntroActivity.arrayListKanjiRoot.get(randomNode).size();
            int randomLeaf = new Random().nextInt(maxLeaf);
            String checkContain = randomNode+","+randomLeaf;
            if (!numberCheckContain.contains(checkContain)) {
                numbersNode.add(randomNode);
                numbersLeaf.add(randomLeaf);
                numberCheckContain.add(checkContain);
            }
        }

//        tempNode = KanjiIntroActivity.arrayListKanjiRoot.get(kanjiStartCount);
//        try{
//            tempNodeNext = KanjiIntroActivity.arrayListKanjiRoot.get(kanjiStartCount + 1);
//        } catch (IndexOutOfBoundsException e){
//
//        }

        //testing buat bug ambil text jawaban
//        int id=KanjiIntroActivity.arrayListKanjiRoot.size();
//        ArrayList<ArrayList<String>> tempPreviousNode=KanjiIntroActivity.arrayListKanjiRoot.get(id);
//        ArrayList<ArrayList<String>> tempPreviousNodeNext=KanjiIntroActivity.arrayListKanjiRoot.get(id-1);

        String defaultLanguageValue = getResources().getString(R.string.sharedPrefLanguage);
        String languagePref = kanjiContainerActivity.sharedPref.getString(getString(R.string.sharedPrefLanguage), defaultLanguageValue);
        int selectedLanguage = 0;
        switch (languagePref) {
            case "en":
                selectedLanguage = kanjiContainerActivity.ENGLISH;
                break;
            case "in":
                selectedLanguage = kanjiContainerActivity.INDONESIA;
                break;
        }

        int random = new Random().nextInt(numbersNode.size());
        ansNode1=numbersNode.get(random);
        ansLeaf1=numbersLeaf.get(random);
        buttonRow1.setText(KanjiIntroActivity.arrayListKanjiRoot.get(ansNode1)
                .get(ansLeaf1).get(selectedLanguage));
        numbersNode.remove(random);
        numbersLeaf.remove(random);

        random = new Random().nextInt(numbersNode.size());
        ansNode2=numbersNode.get(random);
        ansLeaf2=numbersLeaf.get(random);
        buttonRow2.setText(KanjiIntroActivity.arrayListKanjiRoot.get(ansNode2)
                .get(ansLeaf2).get(selectedLanguage));
        numbersNode.remove(random);
        numbersLeaf.remove(random);

        random = new Random().nextInt(numbersNode.size());
        ansNode3=numbersNode.get(random);
        ansLeaf3=numbersLeaf.get(random);
        buttonRow3.setText(KanjiIntroActivity.arrayListKanjiRoot.get(ansNode3)
                .get(ansLeaf3).get(selectedLanguage));
        numbersNode.remove(random);
        numbersLeaf.remove(random);

        ansNode4=numbersNode.get(0);
        ansLeaf4=numbersLeaf.get(0);
        buttonRow4.setText(KanjiIntroActivity.arrayListKanjiRoot.get(ansNode4)
                .get(ansLeaf4).get(selectedLanguage));
        numbersNode.remove(0);
        numbersLeaf.remove(0);

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
                kanjiContainerActivity.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                kanjiContainerActivity.finish();
            }
        });

    }

    public void changeCharacterPreference(String characterPreference) {
        editor = kanjiContainerActivity.sharedPref.edit();
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
        if (answerNode == randomQuestionNode.get(cardPointer) && answerLeaf== randomQuestionLeaf.get(cardPointer)) {
            v.setBackgroundColor(getResources().getColor(R.color.colorrightanswer));
            score++;
            //sfx benar
            final MediaPlayer mediaPlayer = MediaPlayer.create(kanjiContainerActivity, R.raw.correct);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                }
            });
            mediaPlayer.start();
        } else {
            v.setBackgroundColor(getResources().getColor(R.color.colorwronganswer));
            if (ansNode1 == randomQuestionNode.get(cardPointer)&& ansLeaf1== randomQuestionLeaf.get(cardPointer)) {
                buttonRow1.setBackgroundColor(getResources().getColor(R.color.colorrightanswer));
            } else if (ansNode2 == randomQuestionNode.get(cardPointer)&& ansLeaf2== randomQuestionLeaf.get(cardPointer)) {
                buttonRow2.setBackgroundColor(getResources().getColor(R.color.colorrightanswer));
            } else if (ansNode3 == randomQuestionNode.get(cardPointer)&& ansLeaf3== randomQuestionLeaf.get(cardPointer)) {
                buttonRow3.setBackgroundColor(getResources().getColor(R.color.colorrightanswer));
            } else {
                buttonRow4.setBackgroundColor(getResources().getColor(R.color.colorrightanswer));
            }
            //sfx salah
            final MediaPlayer mediaPlayer = MediaPlayer.create(kanjiContainerActivity, R.raw.wrong);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                }
            });
            mediaPlayer.start();
            delayTime = 1500;
        }
        kanjiContainerActivity.progress++;
        progressBar.setProgress(kanjiContainerActivity.progress * 100 / (kanjiContainerActivity.totalKanji*2));
        cardCount++;
        cardPointer++;
        kanjiLeafCount++;
        if (KanjiIntroActivity.arrayListKanjiRoot.get(kanjiNodeCount).size() == kanjiLeafCount) {
            kanjiNodeCount++;
            kanjiLeafCount = 0;
        }
        hasAnswered = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonRow1.setBackgroundColor(getResources().getColor(R.color.colorWhitePure));
                buttonRow2.setBackgroundColor(getResources().getColor(R.color.colorWhitePure));
                buttonRow3.setBackgroundColor(getResources().getColor(R.color.colorWhitePure));
                buttonRow4.setBackgroundColor(getResources().getColor(R.color.colorWhitePure));
                hasAnswered = false;
                boolean cardNotExhausted = cardCount < kanjiContainerActivity.totalKanji;
                if (!cardNotExhausted) {
                    score = score * 100 / kanjiContainerActivity.totalKanji;
                    hasAnswered = true;

                    kanjiContainerActivity.score = score;
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.detach(kanjiContainerActivity.kanjiQuestionFragment);
                    fragmentTransaction.attach(kanjiContainerActivity.kanjiResultFragment);
                    fragmentTransaction.commit();
                    return;
                }
                boolean lastLeafNode = kanjiLeafCount == 0 && kanjiNodeCount % 2 == 0;
                //alasan card multiple 4 biar gambar gk keliatan ganti pas ganti ke question fragment
                if (!lastLeafNode) {
                    setKanjiText();
                }
                if (lastLeafNode || !cardNotExhausted) {
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.detach(kanjiContainerActivity.kanjiQuestionFragment);
                    fragmentTransaction.attach(kanjiContainerActivity.kanjiNewCardFragment);
                    fragmentTransaction.commit();

                    kanjiStartCount = kanjiNodeCount;
                    cardPointer=0;
                    //karena gantinya dihalt, ganti gambar newcard fragment disini
                    kanjiContainerActivity.kanjiNewCardFragment.setTextKanji();
                }
            }
//        }, 100);
        }, delayTime);
    }
}
