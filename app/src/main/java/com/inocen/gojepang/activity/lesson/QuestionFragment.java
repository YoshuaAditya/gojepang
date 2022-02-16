package com.inocen.gojepang.activity.lesson;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.inocen.gojepang.R;
import com.inocen.gojepang.activity.Constant;
import com.inocen.gojepang.activity.MenuActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class QuestionFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button buttonRow1, buttonRow2, buttonRow3, buttonRow4;
    ArrayList<Integer> numbers = new ArrayList<Integer>();
    ArrayList<Integer> randomQuestion = new ArrayList<Integer>();
    ArrayList<String> arrayListTempChar = new ArrayList<String>();
    private ImageView imageViewQuestionCard, imageViewOpenDrawer, imageViewRomaji, imageViewKana, imageViewKanji, imageViewClose;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private boolean drawerToggle = true;
    LessonContainerActivity lessonContainerActivity;
    private int cardCount = 0, totalCard = 0;
    ProgressBar progressBar;
    private boolean hasAnswered = false;
    private SharedPreferences.Editor editor;
    int ans1, ans2, ans3, ans4, score = 0;
    Handler handler=new Handler();

    private String mParam1;
    private String mParam2;

    public QuestionFragment() {
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
    public static QuestionFragment newInstance(String param1, String param2) {
        QuestionFragment fragment = new QuestionFragment();
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
        return inflater.inflate(R.layout.fragment_lesson_multiple_choices_question_layout, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getAndSetImageCard();
        progressBar = getActivity().findViewById(R.id.progressBar);
        progressBar.setProgress(lessonContainerActivity.progress * 100 / (totalCard * 2));
    }


    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        lessonContainerActivity = (LessonContainerActivity) getActivity();
        totalCard = lessonContainerActivity.arrayListEnglish.size();
        randomSeed();
        initializeOnClick();
    }

    private void getAndSetImageCard() {
        String resName = "vl";
        switch (lessonContainerActivity.selectedChapter) {
            case R.string.chaptertitle1:
                resName += "01";
                break;
            case R.string.chaptertitle2:
                resName += "02";
                break;
            case R.string.chaptertitle3:
                resName += "03";
                break;
            case R.string.chaptertitle4:
                resName += "04";
                break;
            case R.string.chaptertitle5:
                resName += "05";
                break;
            case R.string.chaptertitle6:
                resName += "06";
                break;
            case R.string.chaptertitle7:
                resName += "07";
                break;
            case R.string.chaptertitle8:
                resName += "08";
                break;
            case R.string.chaptertitle9:
                resName += "09";
                break;
            case R.string.chaptertitle10:
                resName += "10";
                break;
        }
        resName += "f";
        int getRandomNumber=randomQuestion.get(cardCount%4);
        if (getRandomNumber < 9) {
            resName += "0" + (getRandomNumber + 1);
        } else resName += (getRandomNumber + 1);
        resName += ".png";
        ZipResourceFile zipResourceFile;
        try {
            zipResourceFile = APKExpansionSupport.getAPKExpansionZipFile(lessonContainerActivity, Constant.buildVersion, 0);
            InputStream inputStream = zipResourceFile.getInputStream(resName);
            Bitmap bmp = BitmapFactory.decodeStream(inputStream);
            imageViewQuestionCard.setImageBitmap(bmp);

        } catch (IOException e) {
            e.printStackTrace();
        }
//        int resId=getResourceID(resName,"drawable",lessonContainerActivity);
//        imageButtonViewCard.setImageResource(resId);
        randomizeButtonText();
    }

    /**
     * buat random urutan soal
     */
    private void randomSeed() {
        randomQuestion.clear();
        while (randomQuestion.size() != lessonContainerActivity.newCardFragment.cardCount-cardCount) {
            int randomSeed= ThreadLocalRandom.current().nextInt(cardCount,lessonContainerActivity.newCardFragment.cardCount);
            if (!randomQuestion.contains(randomSeed)) {
                randomQuestion.add(randomSeed);
            }
        }
//        Log.e("GOJEPANG", "randomQuestion "+randomQuestion.toString());
    }

    /**
     * random jawaban setelah ambil yg benar, urutan dari randomSeed
     */
    private void randomizeButtonText() {
        //ini cuma pengingat buat balik ke urutan awal ke akhir, jangan lupa ganti yang bersangkutan
//        numbers.add(cardCount);
        numbers.add(randomQuestion.get(cardCount%4));
        while (numbers.size() != 4) {
            //random mengambil number dari cardccount newcardfragment supaya bertahap 4 kartu random, gk semua lnsg dirandom
            int random = new Random().nextInt(lessonContainerActivity.newCardFragment.cardCount);
            if (!numbers.contains(random)) {
                numbers.add(random);
            }
        }
        String defaultCharValue = getResources().getString(R.string.sharedPrefCharacter);
        String charPref = lessonContainerActivity.sharedPref.getString(getString(R.string.sharedPrefCharacter), defaultCharValue);
        switch (charPref) {
            case "alphabet":
                arrayListTempChar = lessonContainerActivity.arrayListRomaji;
                break;
            case "kana":
                arrayListTempChar = lessonContainerActivity.arrayListKana;
                break;
            case "kanji":
                arrayListTempChar = lessonContainerActivity.arrayListKanji;
                break;
        }
        int random = new Random().nextInt(numbers.size());
        ans1 = numbers.get(random);
        buttonRow1.setText(arrayListTempChar.get(ans1));
        numbers.remove(random);

        random = new Random().nextInt(numbers.size());
        ans2 = numbers.get(random);
        buttonRow2.setText(arrayListTempChar.get(ans2));
        numbers.remove(random);

        random = new Random().nextInt(numbers.size());
        ans3 = numbers.get(random);
        buttonRow3.setText(arrayListTempChar.get(ans3));
        numbers.remove(random);

        ans4 = numbers.get(0);
        buttonRow4.setText(arrayListTempChar.get(ans4));
        numbers.remove(0);

    }

    private void initializeOnClick() {
        imageViewQuestionCard = getActivity().findViewById(R.id.imageViewQuestion);

        imageViewRomaji = getActivity().findViewById(R.id.imageViewRomaji);
        imageViewRomaji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCharacterPreference("alphabet");
                changeAnswerButtonText();
                toggleDrawerCharacter();
            }
        });

        imageViewKana = getActivity().findViewById(R.id.imageViewKana);
        imageViewKana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCharacterPreference("kana");
                changeAnswerButtonText();
                toggleDrawerCharacter();
            }
        });

        imageViewKanji = getActivity().findViewById(R.id.imageViewKanji);
        imageViewKanji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCharacterPreference("kanji");
                changeAnswerButtonText();
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
                checkAnswer(ans1, v);
            }
        });

        buttonRow2 = getActivity().findViewById(R.id.buttonAnswer2);
        buttonRow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(ans2, v);
            }
        });

        buttonRow3 = getActivity().findViewById(R.id.buttonAnswer3);
        buttonRow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(ans3, v);
            }
        });

        buttonRow4 = getActivity().findViewById(R.id.buttonAnswer4);
        buttonRow4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(ans4, v);
            }
        });


        imageViewClose = getActivity().findViewById(R.id.imageViewClose);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                Intent i = new Intent(getActivity(), MenuActivity.class);
                startActivity(i);
                lessonContainerActivity.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                lessonContainerActivity.finish();
            }
        });

    }

    private void changeAnswerButtonText() {
        String defaultCharValue = getResources().getString(R.string.sharedPrefCharacter);
        String charPref = lessonContainerActivity.sharedPref.getString(getString(R.string.sharedPrefCharacter), defaultCharValue);
        switch (charPref) {
            case "alphabet":
                arrayListTempChar = lessonContainerActivity.arrayListRomaji;
                break;
            case "kana":
                arrayListTempChar = lessonContainerActivity.arrayListKana;
                break;
            case "kanji":
                arrayListTempChar = lessonContainerActivity.arrayListKanji;
                break;
        }
        buttonRow1.setText(arrayListTempChar.get(ans1));
        buttonRow2.setText(arrayListTempChar.get(ans2));
        buttonRow3.setText(arrayListTempChar.get(ans3));
        buttonRow4.setText(arrayListTempChar.get(ans4));
    }

    public void changeCharacterPreference(String characterPreference) {
        editor = lessonContainerActivity.sharedPref.edit();
        editor.putString(getString(R.string.sharedPrefCharacter), characterPreference);
        editor.apply();
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


    /**
     * @param answer dari tombol keberepa yang dipencet
     * @param v      diperlukan untuk mengeset warna tombol yang dipencet
     *               boolean hasAnswered digunakan untuk mencegah user memencet jawaban berkali-kali
     *               cek answer dari menyamakan kartu keberapa dengan tombol yang menyimpan index keberapa dia ambil textnya
     *               kalo kartu habis (!cardNotExhausted) saatnya ke layout skor
     *               setelah 4 pertanyaan pindah lagi ke NewCardFragment
     */
    public void checkAnswer(int answer, View v) {
        if (hasAnswered) return;
        int delayTime = 800;
        if (answer == randomQuestion.get(cardCount%4)) {
            v.setBackgroundColor(getResources().getColor(R.color.colorrightanswer));
            score++;
            //sfx benar
            final MediaPlayer mediaPlayer = MediaPlayer.create(lessonContainerActivity, R.raw.correct);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                }
            });
            mediaPlayer.start();
        } else {
            v.setBackgroundColor(getResources().getColor(R.color.colorwronganswer));
            if (randomQuestion.get(cardCount%4) == ans1) {
                buttonRow1.setBackgroundColor(getResources().getColor(R.color.colorrightanswer));
            } else if (randomQuestion.get(cardCount%4) == ans2) {
                buttonRow2.setBackgroundColor(getResources().getColor(R.color.colorrightanswer));
            } else if (randomQuestion.get(cardCount%4) == ans3) {
                buttonRow3.setBackgroundColor(getResources().getColor(R.color.colorrightanswer));
            } else {
                buttonRow4.setBackgroundColor(getResources().getColor(R.color.colorrightanswer));
            }
            //sfx salah
            final MediaPlayer mediaPlayer = MediaPlayer.create(lessonContainerActivity, R.raw.wrong);
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
        lessonContainerActivity.progress++;
        progressBar.setProgress(lessonContainerActivity.progress * 100 / (totalCard * 2));
        hasAnswered = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonRow1.setBackgroundColor(getResources().getColor(R.color.colorWhitePure));
                buttonRow2.setBackgroundColor(getResources().getColor(R.color.colorWhitePure));
                buttonRow3.setBackgroundColor(getResources().getColor(R.color.colorWhitePure));
                buttonRow4.setBackgroundColor(getResources().getColor(R.color.colorWhitePure));
                hasAnswered = false;
                boolean cardNotExhausted = cardCount < totalCard;
                if (!cardNotExhausted) {
                    score = score * 100 / totalCard;
                    hasAnswered = true;

                    lessonContainerActivity.score = score;
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.detach(lessonContainerActivity.questionFragment);
                    fragmentTransaction.attach(lessonContainerActivity.lessonResultFragment);
                    fragmentTransaction.commit();
                    return;
                }
                boolean cardMultiple4 = cardCount % 4 == 0;
                //alasan card multiple 4 biar gambar gk keliatan ganti pas ganti ke question fragment
                if (!cardMultiple4) {
                    getAndSetImageCard();
                }
                if (cardMultiple4 || !cardNotExhausted) {
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.detach(lessonContainerActivity.questionFragment);
                    fragmentTransaction.attach(lessonContainerActivity.newCardFragment);
                    fragmentTransaction.commit();
                    //karena gantinya dihalt, ganti gambar newcard fragment disini
                    lessonContainerActivity.newCardFragment.getAndSetImageCard();
                }
            }
//        }, 100);
        }, delayTime);
    }
}
