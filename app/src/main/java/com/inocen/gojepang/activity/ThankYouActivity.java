package com.inocen.gojepang.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.inocen.gojepang.R;

public class ThankYouActivity extends AppCompatActivity  {

    Button websiteStart;
    ImageView imageViewHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        websiteStart=findViewById(R.id.buttonSeeWebsite);
        imageViewHome=findViewById(R.id.imageViewHome);
        initOnClick();
    }

    private void initOnClick() {
        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ThankYouActivity.this, MenuActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                finish();
            }
        });
        websiteStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThankYouActivity.this, AboutSponsorActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ThankYouActivity.this, MenuActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
        finish();
    }
}
