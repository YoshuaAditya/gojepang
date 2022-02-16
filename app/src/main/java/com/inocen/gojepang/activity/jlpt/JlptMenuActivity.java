package com.inocen.gojepang.activity.jlpt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inocen.gojepang.R;
import com.inocen.gojepang.activity.MenuActivity;
import com.inocen.gojepang.activity.kanji_test.KanjiTestMenuAdapter;
import com.inocen.gojepang.databinding.KanjiTestHomeModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class JlptMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jlpt_menu);

        setClickListener();
    }

    private void setClickListener() {
        CircleImageView circleImageViewN5 = findViewById(R.id.imageViewN5);
        circleImageViewN5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(JlptMenuActivity.this, JlptCategoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.bundle_jlpt_menu_adapter), 0);
                i.putExtra(getString(R.string.bundle_jlpt_menu_adapter), bundle);
                startActivity(i);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }
        });
        CircleImageView circleImageViewN4 = findViewById(R.id.imageViewN4);
        circleImageViewN4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(JlptMenuActivity.this, "Coming soon",
                        Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(JlptMenuActivity.this, JlptCategoryActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt(getString(R.string.bundle_jlpt_menu_adapter), 1);
//                i.putExtra(getString(R.string.bundle_jlpt_menu_adapter), bundle);
//                startActivity(i);
//                finish();
            }
        });
        CircleImageView circleImageViewN3 = findViewById(R.id.imageViewN3);
        circleImageViewN3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(JlptMenuActivity.this, "Coming soon",
                        Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(JlptMenuActivity.this, JlptCategoryActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt(getString(R.string.bundle_jlpt_menu_adapter), 2);
//                i.putExtra(getString(R.string.bundle_jlpt_menu_adapter), bundle);
//                startActivity(i);
//                finish();
            }
        });
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
