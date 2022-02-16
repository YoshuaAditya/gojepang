package com.inocen.gojepang.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.inocen.gojepang.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inocen.gojepang.activity.card_list.CardListActivity;
import com.inocen.gojepang.activity.grammar.Pattern1Activity;
import com.inocen.gojepang.activity.jlpt.JlptMenuActivity;
import com.inocen.gojepang.activity.kanji.KanjiHomeActivity;
import com.inocen.gojepang.activity.kanji_test.KanjiTestMenuActivity;
import com.inocen.gojepang.activity.lesson.LessonHomeActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToogle;
    private NavigationView navigationView;
    private CircleImageView circleImageViewLesson, circleImageViewCard, circleImageViewKanji, circleImageViewKanjiTest, circleImageViewGrammar, circleImageViewDailyTalk, circleImageViewJlptTest, circleImageViewHiraganaChart, circleImageViewKatakanaChart, circleImageViewJapanInfo, circleImageViewJapaneseCourse, circleImageViewReferences, circleImageViewAboutDeveloper, circleImageViewAboutSponsor;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String testLocalFile = "";
    private String titleBar = "";
    private boolean firstTimeSetTitle = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initialize();
        mDrawerLayout = findViewById(R.id.drawer);
        mToogle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset == 0) {
                    // drawer closed
                    getSupportActionBar().setTitle(titleBar);
                } else if (slideOffset != 0) {
                    // started opening
                }
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.konnichiwa);
        navigationView = findViewById(R.id.menu_nav_view);
        navigationView.bringToFront();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        FirebaseApp.initializeApp(this);
        setUserData();
        navigationClickListener();
    }

    private void initialize() {
        circleImageViewLesson = findViewById(R.id.circleImageViewLesson);
        circleImageViewLesson.setOnClickListener(this);
        circleImageViewCard = findViewById(R.id.circleImageViewCard);
        circleImageViewCard.setOnClickListener(this);
        circleImageViewKanji = findViewById(R.id.circleImageViewKanji);
        circleImageViewKanji.setOnClickListener(this);
        circleImageViewKanjiTest = findViewById(R.id.circleImageViewKanjiTest);
        circleImageViewKanjiTest.setOnClickListener(this);
        circleImageViewGrammar = findViewById(R.id.circleImageViewGrammar);
        circleImageViewGrammar.setOnClickListener(this);
        circleImageViewDailyTalk = findViewById(R.id.circleImageViewDailyTalk);
        circleImageViewDailyTalk.setOnClickListener(this);
        circleImageViewJlptTest = findViewById(R.id.circleImageViewJlptTest);
        circleImageViewJlptTest.setOnClickListener(this);
        circleImageViewHiraganaChart = findViewById(R.id.circleImageViewHiraganaChart);
        circleImageViewHiraganaChart.setOnClickListener(this);
        circleImageViewKatakanaChart = findViewById(R.id.circleImageViewKatakanaChart);
        circleImageViewKatakanaChart.setOnClickListener(this);
        circleImageViewJapanInfo = findViewById(R.id.circleImageViewJapanInfo);
        circleImageViewJapanInfo.setOnClickListener(this);
        circleImageViewJapaneseCourse = findViewById(R.id.circleImageViewJapaneseCourseInIndonesia);
        circleImageViewJapaneseCourse.setOnClickListener(this);
        circleImageViewReferences = findViewById(R.id.circleImageViewReferences);
        circleImageViewReferences.setOnClickListener(this);
        circleImageViewAboutDeveloper = findViewById(R.id.circleImageViewAboutDeveloper);
        circleImageViewAboutDeveloper.setOnClickListener(this);
        circleImageViewAboutSponsor = findViewById(R.id.circleImageViewAboutSponsor);
        circleImageViewAboutSponsor.setOnClickListener(this);
    }

    /**
     * Get user data from database and set it into drawer profile
     */
    private void setUserData() {
        final TextView textViewUsername, textViewEmail;

        View headerView = navigationView.getHeaderView(0);
        textViewUsername = headerView.findViewById(R.id.textViewUsername);
        textViewEmail = headerView.findViewById(R.id.textViewEmail);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("users/" + firebaseUser.getUid());
        databaseReference
                .addValueEventListener
                        (new ValueEventListener() {
                             @Override
                             public void onDataChange(DataSnapshot dataSnapshot) {
                                 if (dataSnapshot.exists()) {
                                     if (firstTimeSetTitle) {
                                         textViewUsername.setText(dataSnapshot.child("username").getValue().toString());
                                         textViewEmail.setText(dataSnapshot.child("email").getValue().toString());
                                         titleBar = getSupportActionBar().getTitle() + ", " + dataSnapshot.child("username").getValue().toString() + "-san!";
                                         getSupportActionBar().setTitle(titleBar);
                                         firstTimeSetTitle = false;
                                     }
                                     //last chapter lesson progress
                                     Long temp = (Long) dataSnapshot.child("lastChapter").getValue();
                                     int lastChapterFirebase = temp.intValue();
                                     int defaultIntValue = -1;
                                     int lastChapterPref = sharedPref.getInt(getString(R.string.result_last_chapter), defaultIntValue);
                                     int latestChapter = lastChapterFirebase > lastChapterPref ? lastChapterFirebase : lastChapterPref;
                                     //last kanji progress
                                     temp = (Long) dataSnapshot.child("lastKanji").getValue();
                                     int lastKanjiFirebase = temp.intValue();
                                     int lastKanjiPref = sharedPref.getInt(getString(R.string.result_last_kanji), defaultIntValue);
                                     int latestKanji = lastKanjiFirebase > lastKanjiPref ? lastKanjiFirebase : lastKanjiPref;
                                     //last kanji test progress
                                     temp=(Long)dataSnapshot.child("lastKanjiTest").getValue();
                                     int lastKanjiTestFirebase = temp.intValue();
                                     int lastKanjiTestPref = sharedPref.getInt(getString(R.string.result_last_kanji_test), defaultIntValue);
                                     int latestKanjiTest = lastKanjiTestFirebase > lastKanjiTestPref ? lastKanjiTestFirebase : lastKanjiTestPref;
                                     //jlpt progress
                                     int latestJlpt;
                                     try {
                                         temp = (Long) dataSnapshot.child("lastJlpt").getValue();
                                         int lastJlptFirebase = temp.intValue();
                                         int lastJlptPref = sharedPref.getInt(getString(R.string.result_last_kanji_test), defaultIntValue);
                                         latestJlpt = lastKanjiTestFirebase > lastKanjiTestPref ? lastKanjiTestFirebase : lastKanjiTestPref;
                                     }
                                     catch (NullPointerException e){
                                         latestJlpt=0;
                                     }
                                     SharedPreferences.Editor editor;
                                     editor = sharedPref.edit();
                                     editor.putInt(getString(R.string.result_last_kanji), latestKanji);
                                     editor.putInt(getString(R.string.result_last_chapter), latestChapter);
                                     editor.putInt(getString(R.string.result_last_kanji_test), latestKanjiTest);
                                     editor.putInt(getString(R.string.result_last_jlpt), latestJlpt);
                                     editor.apply();

                                 }
                             }

                             @Override
                             public void onCancelled(DatabaseError databaseError) {
                                 System.out.println("The read failed: " + databaseError.getCode());
                             }
                         }
                        );
    }

    /**
     * gives onClick listener to items in the drawer
     */
    private void navigationClickListener() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.editprofile:
                                Toast.makeText(MenuActivity.this, getString(R.string.coming_soon),
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.editlanguage:
                                editor = sharedPref.edit();
                                editor.remove("FIRSTRUN");
                                editor.apply();

                                Intent intent = new Intent(MenuActivity.this, LanguageSettingActivity.class);
                                Bundle bundleToLanguage = new Bundle();
                                bundleToLanguage.putString("dummy", "dummy");
                                intent.putExtra("bundle", bundleToLanguage);
                                startActivity(intent);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                finish();
                                break;
                            case R.id.editcharacters:
                                startActivity(new Intent(MenuActivity.this, CharacterSettingActivity.class));
                                break;
                            case R.id.notification:
                                Toast.makeText(MenuActivity.this, getString(R.string.coming_soon),
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.changepassword:
                                Toast.makeText(MenuActivity.this, getString(R.string.coming_soon),
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.shareourapp:
                                Toast.makeText(MenuActivity.this, getString(R.string.coming_soon),
                                        Toast.LENGTH_SHORT).show();
////                                FirebaseStorage storage = FirebaseStorage.getInstance();
////                                StorageReference storageRef = storage.getReference();
////                                StorageReference pathReference = storageRef.child("test/test.txt");
//                                File localFile = null;
//                                localFile = new File (Environment.getExternalStorageDirectory()+"/Download/text.txt");
//                                if(localFile.exists()){
//                                        Toast.makeText(MenuActivity.this, "File exists",
//                                                Toast.LENGTH_SHORT).show();
//                                }
//                                else try{
//                                    localFile.createNewFile();
//                                }catch (IOException e) {
//                                    e.printStackTrace();
//                                }
////                                try {
////                                    localFile = File.createTempFile("test", "txt");
////                                } catch (IOException e) {
////                                    e.printStackTrace();
////                                }
////                                pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
////                                    @Override
////                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//////                                         Local temp file has been created
//////                                        Toast.makeText(MenuActivity.this, ""+taskSnapshot.getBytesTransferred(),
//////                                                Toast.LENGTH_SHORT).show();
////                                    }
////                                }).addOnFailureListener(new OnFailureListener() {
////                                    @Override
////                                    public void onFailure(@NonNull Exception exception) {
////                                        Toast.makeText(MenuActivity.this, exception.getMessage(),
////                                                Toast.LENGTH_SHORT).show();
////                                    }
////                                });
//                                testLocalFile=localFile.getAbsolutePath();
//                                Toast.makeText(MenuActivity.this,localFile.getAbsolutePath(),
//                                        Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.reviewus:
                                Toast.makeText(MenuActivity.this, getString(R.string.coming_soon),
                                        Toast.LENGTH_SHORT).show();
//                                if(testLocalFile.isEmpty()){
//                                    Toast.makeText(MenuActivity.this,"click share app first",
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                                else {
//                                    File file = new File(testLocalFile);
//                                    StringBuilder text = new StringBuilder();
//                                    try {
//                                        BufferedReader br = new BufferedReader(new FileReader(file));
//                                        String line;
//
//                                        while ((line = br.readLine()) != null) {
//                                            text.append(line);
//                                            text.append('\n');
//                                        }
//                                        br.close();
//                                    } catch (IOException e) {
//                                        //You'll need to add proper error handling here
//                                    }
//                                    TextView tv = findViewById(R.id.textViewLesson);
//                                    tv.setText(text.toString());
//                                }
                                break;
                            case R.id.logout:
                                FirebaseAuth.getInstance().signOut();
                                editor = sharedPref.edit();
                                editor.remove(getString(R.string.result_last_chapter));
                                editor.remove(getString(R.string.result_last_kanji));
                                editor.remove(getString(R.string.result_last_kanji_test));
                                editor.apply();
                                startActivity(new Intent(MenuActivity.this, LoginActivity.class));
                                finish();
                                break;

                        }

                        return true;
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getSupportActionBar().setTitle(R.string.setting);
        if (mToogle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        if (v == circleImageViewLesson) {
            Intent i = new Intent(this, LessonHomeActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            finish();
        } else if (v == circleImageViewCard) {
            Intent i = new Intent(this, CardListActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            finish();
        } else if (v == circleImageViewKanji) {
            Intent i = new Intent(this, KanjiHomeActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            finish();
        } else if (v == circleImageViewKanjiTest) {
            Intent i = new Intent(this, KanjiTestMenuActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            finish();
        } else if (v == circleImageViewGrammar) {
            Intent i = new Intent(this, Pattern1Activity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            finish();
        } else if (v == circleImageViewDailyTalk) {
            Toast.makeText(MenuActivity.this, getString(R.string.coming_soon),
                    Toast.LENGTH_SHORT).show();
        } else if (v == circleImageViewJlptTest) {
            Intent i = new Intent(this, JlptMenuActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            finish();
        } else if (v == circleImageViewHiraganaChart) {
            Toast.makeText(MenuActivity.this, getString(R.string.coming_soon),
                    Toast.LENGTH_SHORT).show();
        } else if (v == circleImageViewKatakanaChart) {
            Toast.makeText(MenuActivity.this, getString(R.string.coming_soon),
                    Toast.LENGTH_SHORT).show();
        } else if (v == circleImageViewJapanInfo) {
            Toast.makeText(MenuActivity.this, getString(R.string.coming_soon),
                    Toast.LENGTH_SHORT).show();
        } else if (v == circleImageViewJapaneseCourse) {
            Intent i = new Intent(this, JapaneseCourseActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            finish();
        } else if (v == circleImageViewReferences) {
            Toast.makeText(MenuActivity.this, getString(R.string.coming_soon),
                    Toast.LENGTH_SHORT).show();
        } else if (v == circleImageViewAboutDeveloper) {
            Intent i = new Intent(this, AboutDeveloperActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            finish();
        } else if (v == circleImageViewAboutSponsor) {
            Intent i = new Intent(this, AboutSponsorActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            finish();
        }
    }
}
