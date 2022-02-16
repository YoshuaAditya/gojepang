package com.inocen.gojepang.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.inocen.gojepang.R;

public class AboutSponsorActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonAboutSponsor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_sponsor);
        buttonAboutSponsor=findViewById(R.id.buttonSeeWebsite);
        buttonAboutSponsor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==buttonAboutSponsor){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.naoyuki.co.id/"));
            startActivity(browserIntent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
        finish();
    }

}
