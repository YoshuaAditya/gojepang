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

public class Pattern3Part2Activity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageViewBack;
    private ImageView imageViewForward;
    private ImageView imageViewHome;
    private ImageView imageViewKana;
    private ImageView imageViewKanji;
    private ImageView imageViewRomaji;
    private ImageView imageViewSetting;
    private TextView textViewSample1;
    private TextView textViewGrammarPattern;
    private ImageView imageViewSoundPattern1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern3_part2);
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
        textViewSample1 = findViewById(R.id.textViewSample1);
        textViewGrammarPattern = findViewById(R.id.textViewGrammarPattern);
        imageViewSoundPattern1 = findViewById(R.id.imageViewSoundPattern1);
        imageViewBack.setOnClickListener(this);
        imageViewForward.setOnClickListener(this);
        imageViewKana.setOnClickListener(this);
        imageViewRomaji.setOnClickListener(this);
        imageViewKanji.setOnClickListener(this);
        imageViewSetting.setOnClickListener(this);
        imageViewHome.setOnClickListener(this);
        imageViewSoundPattern1.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter, R.anim.exit);
        finish();
    }

    @Override
    public void onClick(final View v) {
        if (v==imageViewBack){
            Intent i = new Intent(this, Pattern3Activity.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            finish();
        }
        if (v==imageViewForward){
            Intent i = new Intent(this, Pattern4Activity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            finish();
        }
        if (v==imageViewHome){
            Intent i = new Intent(this, MenuActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
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
                AssetFileDescriptor afd = zipResourceFile.getAssetFileDescriptor("gr01g08.mp3");
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
        textViewGrammarPattern.setText(getText(R.string.pattern3kana));
        textViewSample1.setText(getText(R.string.pattern3sample4kana));
        allButtonInvisible();
    }
    private void changeRomaji(){
        textViewGrammarPattern.setText(getText(R.string.pattern3romaji));
        textViewSample1.setText(getText(R.string.pattern3sample4romaji));
        allButtonInvisible();
    }
    private void changeKanji(){
        textViewGrammarPattern.setText(getText(R.string.pattern3kana));
        textViewSample1.setText(getText(R.string.pattern3sample4kanji));
        allButtonInvisible();
    }
}
