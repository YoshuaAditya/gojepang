package com.inocen.gojepang.activity.kanji;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.inocen.gojepang.R;
import com.inocen.gojepang.activity.MenuActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class KanjiNewCardFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageView imageViewCard;
    private ImageView imageViewOpenDrawer, imageViewRomaji, imageViewKana, imageViewClose;
    private TextView textViewCardMeaning;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private boolean drawerToggle = true;
    KanjiContainerActivity kanjiContainerActivity;
    private ProgressBar progressBar;
    private SharedPreferences.Editor editor;

    public KanjiNewCardFragment() {
        // Required empty public constructor
    }

    public static KanjiNewCardFragment newInstance(String param1, String param2) {
        KanjiNewCardFragment fragment = new KanjiNewCardFragment();
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
        return inflater.inflate(R.layout.fragment_kanji_new_card_layout, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        kanjiContainerActivity = (KanjiContainerActivity) getActivity();
        checkReplay();
        initializeOnClick();
        setTextKanji();
    }

    /**
     * kalo udah pernah selesai dengan nilai kkm, hilangkan kata new card
     */
    private void checkReplay() {
        int defaultIntValue = -1;
        int lastKanjiPref = kanjiContainerActivity.sharedPref.getInt(getString(R.string.result_last_kanji), defaultIntValue);
        String temp2Pass = ""+kanjiContainerActivity.chapterTitle;
        String tempArray[]=temp2Pass.split(" ");
        int lastKanji = Integer.decode(tempArray[1]);
        if(lastKanjiPref>=lastKanji){
            TextView textView=kanjiContainerActivity.findViewById(R.id.textViewNewCard);
            textView.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * ambil gambar dari expansion file, habis itu set di circle image view
     */
    void setTextKanji() {
        kanjiContainerActivity.currentKanjiLeafCount=KanjiIntroActivity.arrayListKanjiRoot.get(kanjiContainerActivity.currentKanjiNodeNumber).size();
        textViewCardMeaning.setText(KanjiIntroActivity.arrayListKanjiRoot.get(kanjiContainerActivity.currentKanjiNodeNumber)
                .get(kanjiContainerActivity.currentKanjiLeafNumber).get(kanjiContainerActivity.KANJI));

    }

    /**
     * ada 2 sesi disini:
     * 1. Membuat gambar di ImageView menjadi transparan, lalu textview di tengah ImageView ditampilkan
     * 2. textview disembunyikan lagi, ganti kartu baru dengan memanggil setTextKanji
     * Setiap 4 kartu atau kartu habis pindah ke QuestionFragment
     */
    private void cardHandlerLogic() {

    }

    private void initializeOnClick() {
        imageViewCard = getActivity().findViewById(R.id.card);
        imageViewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.detach(kanjiContainerActivity.kanjiNewCardFragment);
                fragmentTransaction.attach(kanjiContainerActivity.kanjiNewCardFlipFragment);
                fragmentTransaction.commit();
            }
        });

        imageViewRomaji = getActivity().findViewById(R.id.imageViewRomaji);
        imageViewRomaji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawerCharacter();
                changeCharacterPreference("alphabet");
            }
        });

        imageViewKana = getActivity().findViewById(R.id.imageViewKana);
        imageViewKana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawerCharacter();
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

        textViewCardMeaning=kanjiContainerActivity.findViewById(R.id.textViewCardMeaning);
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
        kanjiContainerActivity.progressBarValue++;
        kanjiContainerActivity.progress++;
        progressBar.setProgress(kanjiContainerActivity.progress*100/(kanjiContainerActivity.totalKanji*2));
    }

}
