package com.inocen.gojepang.activity.jlpt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.inocen.gojepang.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class JlptCategoryActivity extends AppCompatActivity {

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jlpt_category);
        bundle=getIntent().getExtras();
        setClickListener();
    }

    private void setClickListener() {
        CircleImageView circleImageViewMojiGoi = findViewById(R.id.imageViewMojiGoi);
        circleImageViewMojiGoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(JlptCategoryActivity.this, JlptContainerActivity.class);
                i.putExtra(getString(R.string.bundle_jlpt_menu_adapter), bundle);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                startActivity(i);
                finish();
            }
        });
        CircleImageView circleImageViewBunpouDokkai = findViewById(R.id.imageViewBunpouDokkai);
        circleImageViewBunpouDokkai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO logic bunpou
                Intent i = new Intent(JlptCategoryActivity.this, JlptContainerBunpouActivity.class);
                i.putExtra(getString(R.string.bundle_jlpt_menu_adapter), bundle);
                startActivity(i);
                finish();
            }
        });
        CircleImageView circleImageViewChoukai = findViewById(R.id.imageViewChoukai);
        circleImageViewChoukai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO logic Choukai
                Intent i = new Intent(JlptCategoryActivity.this, JlptContainerChoukaiActivity.class);
                i.putExtra(getString(R.string.bundle_jlpt_menu_adapter), bundle);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, JlptMenuActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        finish();
    }

}
