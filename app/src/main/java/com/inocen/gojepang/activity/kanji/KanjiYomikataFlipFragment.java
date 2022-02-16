package com.inocen.gojepang.activity.kanji;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.inocen.gojepang.BuildConfig;
import com.inocen.gojepang.R;
import com.inocen.gojepang.activity.Constant;
import com.inocen.gojepang.activity.MenuActivity;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class KanjiYomikataFlipFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ConstraintLayout constraintLayoutCard;
    private CircleImageView circleImageSound, circleImageViewInfo;
    private ImageView imageViewOpenDrawer, imageViewRomaji, imageViewKana,  imageViewClose;
    private TextView textViewKunyomi,textViewMeaning;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private boolean drawerToggle = true;
    KanjiContainerActivity kanjiContainerActivity;
    int nodeCount = 0;
    private ProgressBar progressBar;
    private String soundPath = "";
    private SharedPreferences.Editor editor;
    ArrayList<String> temp;
    private boolean hasAddedNextNode=false;

    public KanjiYomikataFlipFragment() {
        // Required empty public constructor
    }

    public static KanjiYomikataFlipFragment newInstance(String param1, String param2) {
        KanjiYomikataFlipFragment fragment = new KanjiYomikataFlipFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kanji_new_card_yomikata_flip_card_layout, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        kanjiContainerActivity = (KanjiContainerActivity) getActivity();
        textViewKunyomi= getActivity().findViewById(R.id.textViewKunyomi);
        textViewMeaning= getActivity().findViewById(R.id.textViewMeaning);
        initializeOnClick();
        getAndSetImageCard();
    }


    /**
     * ambil gambar dari expansion file, habis itu set di circle image view
     */
    void getAndSetImageCard() {
        String resName = "ka";
        //TODO UPDATE ini tiap nambah konten kanji
        switch (kanjiContainerActivity.selectedKanjiLevel) {
            case R.string.levelkanji1:
                resName += "01";
                break;
            case R.string.levelkanji2:
                resName += "02";
                break;
            case R.string.levelkanji3:
                resName += "03";
                break;
            case R.string.levelkanji4:
                resName += "04";
                break;
            case R.string.levelkanji5:
                resName += "05";
                break;
            case R.string.levelkanji6:
                resName += "06";
                break;
            case R.string.levelkanji7:
                resName += "07";
                break;
            case R.string.levelkanji8:
                resName += "08";
                break;
            case R.string.levelkanji9:
                resName += "09";
                break;
            case R.string.levelkanji10:
                resName += "10";
                break;
        }
        resName += "f";
        if (kanjiContainerActivity.progressBarValue < 10) {
            resName += "0" + (kanjiContainerActivity.progressBarValue );
        } else resName += (kanjiContainerActivity.progressBarValue );
        soundPath = resName.replace("f", "g");
        soundPath += ".mp3";
        resName += ".png";

        temp=KanjiIntroActivity.arrayListKanjiRoot.get(kanjiContainerActivity.currentKanjiNodeNumber)
                .get(kanjiContainerActivity.currentKanjiLeafNumber);
        String defaultCharValue = getResources().getString(R.string.sharedPrefCharacter);
        String charPref = kanjiContainerActivity.sharedPref.getString(getString(R.string.sharedPrefCharacter), defaultCharValue);
        switch (charPref) {
            case "alphabet":
                textViewKunyomi.setText(temp.get(kanjiContainerActivity.ROMAJI_KUN));
                break;
            case "kana":
                textViewKunyomi.setText(temp.get(kanjiContainerActivity.HIRAGANA_KUN));
                break;
            case "kanji":
                textViewKunyomi.setText(temp.get(kanjiContainerActivity.HIRAGANA_KUN));
                break;
        }
        String defaultLanguageValue = getResources().getString(R.string.sharedPrefLanguage);
        String languagePref = kanjiContainerActivity.sharedPref.getString(getString(R.string.sharedPrefLanguage), defaultLanguageValue);
        switch (languagePref) {
            case "en":
                textViewMeaning.setText(temp.get(kanjiContainerActivity.ENGLISH));
                break;
            case "in":
                textViewMeaning.setText(temp.get(kanjiContainerActivity.INDONESIA));
                break;
        }

    }

    /**
     * ada 2 sesi disini:
     * 1. Membuat gambar di ImageView menjadi transparan, lalu textview di tengah ImageView ditampilkan
     * 2. textview disembunyikan lagi, ganti kartu baru dengan memanggil getAndSetImageCard
     * Setiap 4 kartu atau kartu habis pindah ke QuestionFragment
     */
//TODO entah kenapa random while softlock kalo kanjis nya di json genap(4?) ganjil gk masalah, nanti perlu dicek knpnya
    private void initializeOnClick() {
        constraintLayoutCard = getActivity().findViewById(R.id.card);
        constraintLayoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kanjiContainerActivity.cardCount++;
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();

                if(kanjiContainerActivity.currentKanjiLeafNumber+1<kanjiContainerActivity.currentKanjiLeafCount) {
                    fragmentTransaction.detach(kanjiContainerActivity.kanjiYomikataFlipFragment);
                    fragmentTransaction.attach(kanjiContainerActivity.kanjiYomikataFragment);
                }
                else{
                    if(!hasAddedNextNode)nodeCount+=KanjiIntroActivity.arrayListKanjiRoot.get(kanjiContainerActivity.currentKanjiNodeNumber).size();
                    boolean cardNotExhausted = kanjiContainerActivity.progressBarValue < kanjiContainerActivity.totalKanji;
                    ArrayList<ArrayList<String>> arrayTemp=null;
                    if(cardNotExhausted) {
                        arrayTemp = KanjiIntroActivity.arrayListKanjiRoot.get(kanjiContainerActivity.currentKanjiNodeNumber + 1);
                    }
                    if(arrayTemp!=null && !hasAddedNextNode){
                        nodeCount+=arrayTemp.size();
                        hasAddedNextNode=true;
                    }
                    boolean nodeMultiple2 = kanjiContainerActivity.cardCount == nodeCount;
//                    Toast.makeText(kanjiContainerActivity,"a: "+nodeMultiple2+"b: "+kanjiContainerActivity.cardCount
//                            +"c: "+nodeCount,Toast.LENGTH_SHORT).show();
                    if(nodeMultiple2 || !cardNotExhausted){
                        fragmentTransaction.detach(kanjiContainerActivity.kanjiYomikataFlipFragment);
                        fragmentTransaction.attach(kanjiContainerActivity.kanjiQuestionFragment);
                        hasAddedNextNode=false;
                    }
                    else{
                        fragmentTransaction.detach(kanjiContainerActivity.kanjiYomikataFlipFragment);
                        fragmentTransaction.attach(kanjiContainerActivity.kanjiNewCardFragment);
                    }
                    kanjiContainerActivity.currentKanjiNodeNumber++;
                    kanjiContainerActivity.currentKanjiLeafNumber=0;
                }
                fragmentTransaction.commit();
            }
        });

        imageViewRomaji = getActivity().findViewById(R.id.imageViewRomaji);
        imageViewRomaji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawerCharacter();
                textViewKunyomi.setText(temp.get(kanjiContainerActivity.ROMAJI_KUN));
                changeCharacterPreference("alphabet");
            }
        });

        imageViewKana = getActivity().findViewById(R.id.imageViewKana);
        imageViewKana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawerCharacter();
                textViewKunyomi.setText(temp.get(kanjiContainerActivity.HIRAGANA_KUN));
                changeCharacterPreference("kana");
            }
        });

        imageViewOpenDrawer = getActivity().findViewById(R.id.imageViewOpenDrawer);
        imageViewOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawerCharacter();
            }
        });

        imageViewClose = getActivity().findViewById(R.id.imageViewClose);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MenuActivity.class);
                startActivity(i);
                kanjiContainerActivity.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                kanjiContainerActivity.finish();
            }
        });

        circleImageSound = getActivity().findViewById(R.id.circleImageViewAudio);
        circleImageSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZipResourceFile zipResourceFile;
                try {
                    zipResourceFile = APKExpansionSupport.getAPKExpansionZipFile(kanjiContainerActivity, Constant.buildVersion, 0);
                    AssetFileDescriptor afd = zipResourceFile.getAssetFileDescriptor(soundPath);
                    final MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mediaPlayer.release();
                        }
                    });
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
                    e.printStackTrace();
                    Toast.makeText(kanjiContainerActivity, "No sound files yet",
                            Toast.LENGTH_SHORT).show();
//                    Toast.makeText(kanjiContainerActivity, soundPath,
//                            Toast.LENGTH_SHORT).show();
                }
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

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        progressBar = getActivity().findViewById(R.id.progressBar);
        progressBar.setProgress(kanjiContainerActivity.progress*100/(kanjiContainerActivity.totalKanji*2));
    }

}