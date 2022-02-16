package com.inocen.gojepang.activity.jlpt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.inocen.gojepang.R;
import com.inocen.gojepang.activity.Constant;
import com.inocen.gojepang.activity.MenuActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class JlptQuestionChoukaiFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button buttonRow1, buttonRow2, buttonRow3, buttonRow4;
    ArrayList<Integer> randomQuestionNode = new ArrayList<Integer>();
    private ImageView imageViewJlptQuestion;
    private ImageView imageViewSound, imageViewClose;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private boolean drawerToggle = true;
    JlptContainerChoukaiActivity jlptContainerChoukaiActivity;
    private int kanjiStartCount = 0, kanjiNodeCount = 0, kanjiLeafCount = 0, cardPointer = 0, cardCount = 0;
    ProgressBar progressBar;
    private boolean hasAnswered = false;
    private SharedPreferences.Editor editor;
    String ansNode1, ansNode2, ansNode3, ansNode4, ansLeaf1, ansLeaf2, ansLeaf3, ansLeaf4;
    private int score = 0, currentQuestionNumber = 0;
    Handler handler = new Handler();
    MediaPlayer mediaPlayer=new MediaPlayer();

    private String mParam1;
    private String mParam2;

    public JlptQuestionChoukaiFragment() {
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
    public static JlptQuestionChoukaiFragment newInstance(String param1, String param2) {
        JlptQuestionChoukaiFragment fragment = new JlptQuestionChoukaiFragment();
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
        return inflater.inflate(R.layout.fragment_jlpt_choukai, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setImageQuestion();
        progressBar = getActivity().findViewById(R.id.progressBar);
        progressBar.setProgress(cardCount * 100 / jlptContainerChoukaiActivity.totalKanji);
    }


    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        jlptContainerChoukaiActivity = (JlptContainerChoukaiActivity) getActivity();
        initializeOnClick();
    }


    private void setImageQuestion() {
        imageViewJlptQuestion.setVisibility(View.VISIBLE);
        currentQuestionNumber = cardCount;
        String gambar = jlptContainerChoukaiActivity.arrayListSoalAkhir.get(currentQuestionNumber).soal + ".jpg";
        final String mp3 = jlptContainerChoukaiActivity.arrayListSoalAkhir.get(currentQuestionNumber).extraSoal + ".mp3";
//        Log.e("GOJEPANG", gambar
//                + " "+mp3);
        if (gambar.equals(".jpg")){
            imageViewJlptQuestion.setVisibility(View.INVISIBLE);
        }
        else {
            ZipResourceFile zipResourceFile;
            try {
                zipResourceFile = APKExpansionSupport.getAPKExpansionZipFile(jlptContainerChoukaiActivity, Constant.buildVersion, 0);
                InputStream inputStream = zipResourceFile.getInputStream(gambar);
                Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                imageViewJlptQuestion.setImageBitmap(bmp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageViewSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ZipResourceFile zipResourceFile;
                try {
                    zipResourceFile = APKExpansionSupport.getAPKExpansionZipFile(jlptContainerChoukaiActivity, Constant.buildVersion, 0);
                    AssetFileDescriptor afd = zipResourceFile.getAssetFileDescriptor(mp3);
                    mediaPlayer=new MediaPlayer();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mediaPlayer.release();
                            v.setEnabled(true);
                        }
                    });
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    v.setEnabled(false);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
                    e.printStackTrace();
                    Toast.makeText(jlptContainerChoukaiActivity, "No sound files yet",
                            Toast.LENGTH_SHORT).show();
                }
            }});
        randomizeButtonText();
    }

    private void randomSeed() {
//        randomQuestionNode.clear();
//        while (randomQuestionNode.size()!=jlptContainerChoukaiActivity.totalKanji) {
//            int random = ThreadLocalRandom.current().nextInt(jlptContainerChoukaiActivity.totalKanji);
//            if (!randomQuestionNode.contains(random)) {
//                randomQuestionNode.add(random);
//            }
//        }
//        Log.e("GOJEPANG", " randomQuestionCheckContain "+randomQuestionCheckContain.toString());
    }

    private void randomizeButtonText() {
        ArrayList<String> abcd = new ArrayList<>();
        buttonRow4.setVisibility(View.VISIBLE);
        int nomor = currentQuestionNumber;
        abcd.add(jlptContainerChoukaiActivity.arrayListSoalAkhir.get(nomor).a);
        abcd.add(jlptContainerChoukaiActivity.arrayListSoalAkhir.get(nomor).b);
        abcd.add(jlptContainerChoukaiActivity.arrayListSoalAkhir.get(nomor).c);
        if(!jlptContainerChoukaiActivity.arrayListSoalAkhir.get(nomor).d.isEmpty())
            abcd.add(jlptContainerChoukaiActivity.arrayListSoalAkhir.get(nomor).d);

        if(jlptContainerChoukaiActivity.arrayListSoalAkhir.get(nomor).a.equals("1")){
            ansNode1 = abcd.remove(0);
            buttonRow1.setText(ansNode1);

            ansNode2 = abcd.remove(0);
            buttonRow2.setText(ansNode2);

            ansNode3 = abcd.remove(0);
            buttonRow3.setText(ansNode3);
        }
        else {
            int random = new Random().nextInt(abcd.size());
            ansNode1 = abcd.remove(random);
            buttonRow1.setText(ansNode1);

            random = new Random().nextInt(abcd.size());
            ansNode2 = abcd.remove(random);
            buttonRow2.setText(ansNode2);

            random = new Random().nextInt(abcd.size());
            ansNode3 = abcd.remove(random);
            buttonRow3.setText(ansNode3);
        }

        if(!jlptContainerChoukaiActivity.arrayListSoalAkhir.get(nomor).d.isEmpty()) {
            ansNode4 = abcd.remove(0);
            buttonRow4.setText(ansNode4);
        }
        else buttonRow4.setVisibility(View.INVISIBLE);

//        Log.e("GOJEPANG", "ans1 " + ans1 + " ans2 " + ans2 + " ans3 " + ans3 + " ans4 " + ans4
//                + " offset " + offset + " kanjiStartCount " + kanjiStartCount);
    }

    private void initializeOnClick() {
        imageViewJlptQuestion = getActivity().findViewById(R.id.imageViewQuestion);
        imageViewSound= getActivity().findViewById(R.id.imageViewSound);

        buttonRow1 = getActivity().findViewById(R.id.buttonAnswer1);
        buttonRow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(ansNode1, v);
            }
        });

        buttonRow2 = getActivity().findViewById(R.id.buttonAnswer2);
        buttonRow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(ansNode2, v);
            }
        });

        buttonRow3 = getActivity().findViewById(R.id.buttonAnswer3);
        buttonRow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(ansNode3, v);
            }
        });

        buttonRow4 = getActivity().findViewById(R.id.buttonAnswer4);
        buttonRow4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(ansNode4, v);
            }
        });


        imageViewClose = getActivity().findViewById(R.id.imageViewClose);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                mediaPlayer.release();
                Intent i = new Intent(getActivity(), MenuActivity.class);
                startActivity(i);
                jlptContainerChoukaiActivity.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                jlptContainerChoukaiActivity.finish();
            }
        });

    }

    public void changeCharacterPreference(String characterPreference) {
        editor = jlptContainerChoukaiActivity.sharedPref.edit();
        editor.putString(getString(R.string.sharedPrefCharacter), characterPreference);
        editor.apply();
    }


    /**
     * @param answerNode
     * @param v          diperlukan untuk mengeset warna tombol yang dipencet
     *                   boolean hasAnswered digunakan untuk mencegah user memencet jawaban berkali-kali
     *                   cek answer dari menyamakan kartu keberapa dengan tombol yang menyimpan index keberapa dia ambil textnya
     *                   kalo kartu habis (!cardNotExhausted) saatnya ke layout skor
     *                   setelah 4 pertanyaan pindah lagi ke NewCardFragment
     */
    public void checkAnswer(String answerNode, View v) {
        mediaPlayer.release();
        imageViewSound.setEnabled(true);
        if (hasAnswered) return;
        int delayTime = 800;
        if (answerNode.equals(jlptContainerChoukaiActivity.arrayListSoalAkhir.get(currentQuestionNumber).jawaban)) {
            v.setBackgroundColor(getResources().getColor(R.color.colorrightanswer));
            score++;
            //sfx benar
            final MediaPlayer mediaPlayer = MediaPlayer.create(jlptContainerChoukaiActivity, R.raw.correct);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                }
            });
            mediaPlayer.start();
        } else {
            v.setBackgroundColor(getResources().getColor(R.color.colorwronganswer));
            if (ansNode1.equals(jlptContainerChoukaiActivity.arrayListSoalAkhir.get(currentQuestionNumber).jawaban)) {
                buttonRow1.setBackgroundColor(getResources().getColor(R.color.colorrightanswer));
            } else if (ansNode2.equals(jlptContainerChoukaiActivity.arrayListSoalAkhir.get(currentQuestionNumber).jawaban)) {
                buttonRow2.setBackgroundColor(getResources().getColor(R.color.colorrightanswer));
            } else if (ansNode3.equals(jlptContainerChoukaiActivity.arrayListSoalAkhir.get(currentQuestionNumber).jawaban)) {
                buttonRow3.setBackgroundColor(getResources().getColor(R.color.colorrightanswer));
            } else {
                buttonRow4.setBackgroundColor(getResources().getColor(R.color.colorrightanswer));
            }
            //sfx salah
            final MediaPlayer mediaPlayer = MediaPlayer.create(jlptContainerChoukaiActivity, R.raw.wrong);
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
        progressBar.setProgress(cardCount * 100 / jlptContainerChoukaiActivity.totalKanji);
        hasAnswered = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonRow1.setBackgroundColor(getResources().getColor(R.color.colorWhitePure));
                buttonRow2.setBackgroundColor(getResources().getColor(R.color.colorWhitePure));
                buttonRow3.setBackgroundColor(getResources().getColor(R.color.colorWhitePure));
                buttonRow4.setBackgroundColor(getResources().getColor(R.color.colorWhitePure));
                hasAnswered = false;
                boolean cardNotExhausted = cardCount < jlptContainerChoukaiActivity.totalKanji;
                if (!cardNotExhausted) {
                    score = score * 100 / jlptContainerChoukaiActivity.totalKanji;
                    hasAnswered = true;

                    jlptContainerChoukaiActivity.score = score;
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.detach(jlptContainerChoukaiActivity.jlptQuestionChoukaiFragment);
                    fragmentTransaction.attach(jlptContainerChoukaiActivity.jlptResultChoukaiFragment);
                    fragmentTransaction.commit();
                    return;
                }
                else {
                    setImageQuestion();
                }
            }
//        }, 100);
        }, delayTime);
    }
}
