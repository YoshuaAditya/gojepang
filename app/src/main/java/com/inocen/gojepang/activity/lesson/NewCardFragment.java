package com.inocen.gojepang.activity.lesson;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;

public class NewCardFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageButton imageButtonViewCard;
    private CircleImageView circleImageSound, circleImageViewInfo;
    private ImageView imageViewOpenDrawer, imageViewRomaji, imageViewKana, imageViewKanji, imageViewClose;
    private TextView textViewCardMeaning, textViewCardInfo;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private boolean drawerToggle = true;
    LessonContainerActivity lessonContainerActivity;
    int cardCount = 0, totalCard = 0;
    private ProgressBar progressBar;
    private String soundPath = "";
    private SharedPreferences.Editor editor;

    public NewCardFragment() {
        // Required empty public constructor
    }

    public static NewCardFragment newInstance(String param1, String param2) {
        NewCardFragment fragment = new NewCardFragment();
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
        return inflater.inflate(R.layout.fragment_lesson_new_card_layout, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        textViewCardMeaning = getActivity().findViewById(R.id.textViewCardMeaning);
        textViewCardInfo = getActivity().findViewById(R.id.textViewCardInfo);
        lessonContainerActivity = (LessonContainerActivity) getActivity();
        totalCard = lessonContainerActivity.arrayListEnglish.size();
        checkReplay();
        initializeOnClick();
        getAndSetImageCard();

        boolean tutorialComplete=lessonContainerActivity.sharedPref.getBoolean(getString(R.string.tutorial),false);
        if(!tutorialComplete)showTutorial();
    }

    private void checkReplay() {
        int defaultIntValue = -1;
        int lastChapterPref = lessonContainerActivity.sharedPref.getInt(getString(R.string.result_last_chapter), defaultIntValue);
        String temp2Pass = ""+lessonContainerActivity.chapterTitle;
        String temp3Pass = temp2Pass.substring(temp2Pass.length() - 1);
        int lastChapter = Integer.decode(temp3Pass);
        if(lastChapterPref>=lastChapter){
            TextView textView=lessonContainerActivity.findViewById(R.id.textViewNewCard);
            textView.setVisibility(View.INVISIBLE);
        }
    }

    private void showTutorial(){
        new GuideView.Builder(getContext())
                .setTitle(getString(R.string.titletutorialsound))
                .setContentText(getString(R.string.contenttutorialsound1)+"\n"+getString(R.string.contenttutorialclick))
                .setGravity(Gravity.auto) //optional
                .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                .setTargetView(circleImageSound)
                .setContentTextSize(16)//optional
                .setTitleTextSize(20)//optional
                .build()
                .show();
        new GuideView.Builder(getContext())
                .setTitle(getString(R.string.titletutorialinfo))
                .setContentText(getString(R.string.contenttutorialinfo1)+"\n"+getString(R.string.contenttutorialclick))
                .setGravity(Gravity.auto) //optional
                .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                .setTargetView(circleImageViewInfo)
                .setContentTextSize(16)//optional
                .setTitleTextSize(20)//optional
                .build()
                .show();
        //tutorial sekali saja, ditaruh ke shared pref
        editor = lessonContainerActivity.sharedPref.edit();
        editor.putBoolean(getString(R.string.tutorial), true);
        editor.apply();
    }


    /**
     * ambil gambar dari expansion file, habis itu set di circle image view
     */
    void getAndSetImageCard() {
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
        if (cardCount < 9) {
            resName += "0" + (cardCount + 1);
        } else resName += (cardCount + 1);
        soundPath = resName.replace("f", "g");
        soundPath += ".mp3";
        resName += ".png";
        ZipResourceFile zipResourceFile;
        try {
            zipResourceFile = APKExpansionSupport.getAPKExpansionZipFile(lessonContainerActivity, Constant.buildVersion, 0);
//            Log.e("gojepang",resName);
            InputStream inputStream = zipResourceFile.getInputStream(resName);
            Bitmap bmp = BitmapFactory.decodeStream(inputStream);
            imageButtonViewCard.setImageBitmap(bmp);

        } catch (IOException e) {
            e.printStackTrace();
        }
//        int resId=getResourceID(resName,"drawable",lessonContainerActivity);
//        imageButtonViewCard.setImageResource(resId);
    }

    /**
     * ada 2 sesi disini:
     * 1. Membuat gambar di ImageView menjadi transparan, lalu textview di tengah ImageView ditampilkan
     * 2. textview disembunyikan lagi, ganti kartu baru dengan memanggil getAndSetImageCard
     * Setiap 4 kartu atau kartu habis pindah ke QuestionFragment
     */
    private void cardHandlerLogic() {
        if (textViewCardMeaning.getVisibility() == View.INVISIBLE) {
            //munculin text buat arti cardnya, set text sesuai gambar
            imageButtonViewCard.setImageResource(android.R.color.transparent);
            String defaultCharValue = getResources().getString(R.string.sharedPrefCharacter);
            String charPref = lessonContainerActivity.sharedPref.getString(getString(R.string.sharedPrefCharacter), defaultCharValue);
            switch (charPref) {
                case "alphabet":
                    textViewCardMeaning.setText(lessonContainerActivity.arrayListRomaji.get(cardCount));
                    break;
                case "kana":
                    textViewCardMeaning.setText(lessonContainerActivity.arrayListKana.get(cardCount));
                    break;
                case "kanji":
                    textViewCardMeaning.setText(lessonContainerActivity.arrayListKanji.get(cardCount));
                    break;
            }
            textViewCardMeaning.setVisibility(View.VISIBLE);
        } else {
            //text hilangin, gambar diupdate ke card berikutnya
            textViewCardMeaning.setVisibility(View.INVISIBLE);
            cardCount++;
            lessonContainerActivity.progress++;
            progressBar.setProgress(lessonContainerActivity.progress * 100 / (totalCard * 2));
            textViewCardInfo.setVisibility(View.INVISIBLE);
            boolean cardNotExhausted = cardCount < totalCard;
            boolean cardMultiple4 = cardCount % 4 == 0;
            //alasan card multiple 4 biar gambar gk keliatan ganti pas ganti ke question fragment
            if (!cardMultiple4 && cardNotExhausted) {
                getAndSetImageCard();
            }
            if (cardMultiple4 || !cardNotExhausted) {
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.detach(lessonContainerActivity.newCardFragment);
                fragmentTransaction.attach(lessonContainerActivity.questionFragment);
                fragmentTransaction.commit();
            }
        }
    }

    private void initializeOnClick() {
        imageButtonViewCard = getActivity().findViewById(R.id.imageButtonViewCard);
        imageButtonViewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardHandlerLogic();
            }
        });

        circleImageViewInfo = getActivity().findViewById(R.id.circleImageViewInfo);
        circleImageViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String defaultLanguageValue = getResources().getString(R.string.sharedPrefLanguage);
                String languagePref = lessonContainerActivity.sharedPref.getString(getString(R.string.sharedPrefLanguage), defaultLanguageValue);
                switch (languagePref) {
                    case "en":
                        textViewCardInfo.setText(lessonContainerActivity.arrayListEnglish.get(cardCount));
                        break;
                    case "in":
                        textViewCardInfo.setText(lessonContainerActivity.arrayListIndonesia.get(cardCount));
                        break;
                }
                textViewCardInfo.setVisibility(View.VISIBLE);
            }
        });

        imageViewRomaji = getActivity().findViewById(R.id.imageViewRomaji);
        imageViewRomaji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewCardMeaning.setText(lessonContainerActivity.arrayListRomaji.get(cardCount));
                toggleDrawerCharacter();
                changeCharacterPreference("alphabet");
            }
        });

        imageViewKana = getActivity().findViewById(R.id.imageViewKana);
        imageViewKana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewCardMeaning.setText(lessonContainerActivity.arrayListKana.get(cardCount));
                toggleDrawerCharacter();
                changeCharacterPreference("kana");
            }
        });

        imageViewKanji = getActivity().findViewById(R.id.imageViewKanji);
        imageViewKanji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewCardMeaning.setText(lessonContainerActivity.arrayListKanji.get(cardCount));
                toggleDrawerCharacter();
                changeCharacterPreference("kanji");
            }
        });

        imageViewOpenDrawer = getActivity().findViewById(R.id.imageViewOpenDrawer);
        imageViewOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawerCharacter();
            }
        });

        circleImageSound = getActivity().findViewById(R.id.circleImageViewAudio);
        circleImageSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ZipResourceFile zipResourceFile;
                try {
                    zipResourceFile = APKExpansionSupport.getAPKExpansionZipFile(lessonContainerActivity, Constant.buildVersion, 0);
                    AssetFileDescriptor afd = zipResourceFile.getAssetFileDescriptor(soundPath);
//                                    InputStream inputStream=zipResourceFile.getInputStream("vl01g.mp3");
//                                    FileInputStream fileInputStream=new FileInputStream(String.valueOf(inputStream));
                    final MediaPlayer mediaPlayer = new MediaPlayer();
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
                    Toast.makeText(lessonContainerActivity, "No sound files yet",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageViewClose = getActivity().findViewById(R.id.imageViewClose);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MenuActivity.class);
                startActivity(i);
                lessonContainerActivity.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                lessonContainerActivity.finish();
            }
        });

        progressBar = getActivity().findViewById(R.id.progressBar);

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

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        progressBar = getActivity().findViewById(R.id.progressBar);
        progressBar.setProgress(lessonContainerActivity.progress * 100 / (totalCard * 2));
    }
}
