package com.inocen.gojepang.activity.lesson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inocen.gojepang.R;
import com.inocen.gojepang.activity.CharacterSettingActivity;
import com.inocen.gojepang.databinding.LessonHomeModel;

import java.util.List;

public class LessonHomeAdapter extends RecyclerView.Adapter<LessonHomeAdapter.LessonViewHolder> {
    private Context mCtx;
    private List<LessonHomeModel> lessonHomeModeList;
    private SharedPreferences sharedPref;

    public LessonHomeAdapter(Context mCtx, List<LessonHomeModel> lessonHomeModeList) {
        this.mCtx = mCtx;
        this.lessonHomeModeList = lessonHomeModeList;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.lesson_cards, null);
        final LessonViewHolder holder = new LessonViewHolder(view);

        //set onClickListener tiap holder
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LessonHomeModel lessonHomeModel = lessonHomeModeList.get(holder.getAdapterPosition());
                //cek apakah chapter yang dipilih sudah terbuka atau belum
                if (!lessonHomeModel.getLessonChapterLocker()) {
                    Toast.makeText(mCtx, mCtx.getString(R.string.clear_previous_chapter_first),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                //tujuan pasang bundle untuk memberi value chapter apa yang diakses
                Bundle bundle = new Bundle();
                int title = lessonHomeModeList.get(holder.getAdapterPosition()).getLessonChapterTitle();
                bundle.putInt(mCtx.getString(R.string.chapter_title), title);

                //jika belum setting character preference, dibawa kesitu dulu
                sharedPref = PreferenceManager.getDefaultSharedPreferences(mCtx);
                String defaultCharacterValue = mCtx.getResources().getString(R.string.sharedPrefCharacter);
                String characterPref = sharedPref.getString(mCtx.getString(R.string.sharedPrefCharacter), defaultCharacterValue);
                if (characterPref.equals(defaultCharacterValue)) {
                    Intent intent = new Intent(mCtx, CharacterSettingActivity.class);
                    intent.putExtra(mCtx.getString(R.string.bundle_intro), bundle);
                    mCtx.startActivity(intent);
                } else {
                    Intent intent = new Intent(mCtx, LessonIntroActivity.class);
                    intent.putExtra(mCtx.getString(R.string.bundle_intro), bundle);
                    mCtx.startActivity(intent);
                }
                //finish activity ini
                if (mCtx instanceof Activity) {
                    ((Activity) mCtx).finish();
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder lessonViewHolder, int i) {
        LessonHomeModel lessonHomeModel = lessonHomeModeList.get(i);
        lessonViewHolder.textViewChapterNumber.setText(lessonHomeModel.getLessonChapterNumber());
        lessonViewHolder.textViewChapterTitle.setText(lessonHomeModel.getLessonChapterTitle());
        lessonViewHolder.imageViewChapter.setImageDrawable(mCtx.getResources().getDrawable(lessonHomeModel.getLessonChapterImageView(), null));
        if (lessonHomeModel.getLessonChapterLocker()) {
            lessonViewHolder.imageViewLocker.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return lessonHomeModeList.size();
    }

    class LessonViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewChapter;
        ImageView imageViewLocker;
        TextView textViewChapterNumber;
        TextView textViewChapterTitle;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewChapter = itemView.findViewById(R.id.circleImageViewKanji);
            imageViewLocker = itemView.findViewById(R.id.imageViewLocker);
            textViewChapterNumber = itemView.findViewById(R.id.textViewKanjiLevel);
            textViewChapterTitle = itemView.findViewById(R.id.textViewChapterTitle);
        }
    }
}
