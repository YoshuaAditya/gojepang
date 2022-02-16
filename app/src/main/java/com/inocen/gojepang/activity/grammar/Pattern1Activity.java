package com.inocen.gojepang.activity.grammar;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.inocen.gojepang.R;
import com.inocen.gojepang.activity.Constant;
import com.inocen.gojepang.activity.MenuActivity;

import java.io.IOException;

public class Pattern1Activity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageViewBack;
    private ImageView imageViewForward;
    private ImageView imageViewSetting;
    private ImageView imageViewHome;
    private ImageView imageViewKana;
    private ImageView imageViewKanji;
    private ImageView imageViewRomaji;
    private TextView textViewPatternExplanation;
    private TextView textViewSample1;
    private TextView textViewNote;
    private TextView textViewSample2;
    private TextView textViewGrammarPattern;
    private ImageView imageViewSoundPattern1;
    private ImageView imageViewSoundPattern2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern1);
        initialize();
    }
    public void initialize(){
        imageViewBack = findViewById(R.id.imageViewBack);
        imageViewForward = findViewById(R.id.imageViewForward);
        imageViewKana = findViewById(R.id.imageViewKana);
        imageViewRomaji = findViewById(R.id.imageViewRomaji);
        imageViewKanji = findViewById(R.id.imageViewKanji);
        imageViewSetting = findViewById(R.id.imageViewSetting);
        imageViewHome = findViewById(R.id.imageViewHome);
        textViewGrammarPattern = findViewById(R.id.textViewGrammarPattern);
        textViewNote = findViewById(R.id.textViewNote);
        textViewPatternExplanation = findViewById(R.id.textViewPatternExplanation);
        textViewSample1 = findViewById(R.id.textViewSample1);
        textViewSample2 = findViewById(R.id.textViewSample2);
        imageViewSoundPattern1 = findViewById(R.id.imageViewSoundPattern1);
        imageViewSoundPattern2 = findViewById(R.id.imageViewSoundPattern2);
        imageViewBack.setOnClickListener(this);
        imageViewForward.setOnClickListener(this);
        imageViewKana.setOnClickListener(this);
        imageViewRomaji.setOnClickListener(this);
        imageViewKanji.setOnClickListener(this);
        imageViewSetting.setOnClickListener(this);
        imageViewHome.setOnClickListener(this);
        imageViewSoundPattern1.setOnClickListener(this);
        imageViewSoundPattern2.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
        finish();
    }

    @Override
    public void onClick(final View v) {
        if (v==imageViewBack){
            Intent i = new Intent(this, MenuActivity.class);
            startActivity(i);
            //top to bottom transition
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            finish();
        }
        if (v==imageViewForward){
            Intent i = new Intent(this, Pattern2Activity.class);
            startActivity(i);
            //left to right transition
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            finish();
        }
        if (v==imageViewHome){
            Intent i = new Intent(this, MenuActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            finish();
        }
        if (v==imageViewSetting){
            allButtonVisible();
        }
        if (v==imageViewKana){
            changeKana();
        }
        if (v==imageViewRomaji){
            changeRomaji();
        }
        if (v==imageViewKanji){
            changeKanji();
        }
        if (v==imageViewSoundPattern1){
            ZipResourceFile zipResourceFile;
            try {
                zipResourceFile = APKExpansionSupport.getAPKExpansionZipFile(this, Constant.buildVersion, 0);
                AssetFileDescriptor afd = zipResourceFile.getAssetFileDescriptor("gr01g01.mp3");
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
            }
        }
        if (v==imageViewSoundPattern2){
            ZipResourceFile zipResourceFile;
            try {
                zipResourceFile = APKExpansionSupport.getAPKExpansionZipFile(this, Constant.buildVersion, 0);
                AssetFileDescriptor afd = zipResourceFile.getAssetFileDescriptor("gr01g02.mp3");
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
            }
        }
    }
    private void allButtonInvisible(){
        imageViewKana.setVisibility(View.INVISIBLE);
        imageViewRomaji.setVisibility(View.INVISIBLE);
        imageViewKanji.setVisibility(View.INVISIBLE);
    }
    private void allButtonVisible(){
        imageViewKana.setVisibility(View.VISIBLE);
        imageViewRomaji.setVisibility(View.VISIBLE);
        imageViewKanji.setVisibility(View.VISIBLE);
    }
    private void changeKana(){
        textViewPatternExplanation.setText(getText(R.string.pattern1explanationkana));
        textViewNote.setText(getText(R.string.pattern1notekana));
        textViewGrammarPattern.setText(getText(R.string.pattern1kana));
        textViewSample1.setText(getText(R.string.pattern1sample1kana));
        textViewSample2.setText(getText(R.string.pattern1sample2kana));
        allButtonInvisible();
    }
    private void changeRomaji(){
        textViewPatternExplanation.setText(getText(R.string.pattern1explanationromaji));
        textViewNote.setText(getText(R.string.pattern1noteromaji));
        textViewGrammarPattern.setText(getText(R.string.pattern1romaji));
        textViewSample1.setText(getText(R.string.pattern1sample1romaji));
        textViewSample2.setText(getText(R.string.pattern1sample2romaji));
        allButtonInvisible();
    }
    private void changeKanji(){
        textViewPatternExplanation.setText(getText(R.string.pattern1explanationkana));
        textViewNote.setText(getText(R.string.pattern1notekana));
        textViewGrammarPattern.setText(getText(R.string.pattern1kana));
        textViewSample1.setText(getText(R.string.pattern1sample1kanji));
        textViewSample2.setText(getText(R.string.pattern1sample2kanji));
        allButtonInvisible();
    }
}
