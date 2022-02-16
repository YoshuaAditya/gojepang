package com.inocen.gojepang.activity.kanji;

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
import com.inocen.gojepang.databinding.KanjiHomeModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class KanjiHomeAdapter extends RecyclerView.Adapter<KanjiHomeAdapter.KanjiHomeViewHolder>{
    private Context mCtx;
    private List<KanjiHomeModel> kanjiHomeModelList;
    private SharedPreferences sharedPref;

    public KanjiHomeAdapter(Context mCtx, List<KanjiHomeModel> kanjiHomeModelList) {
        this.mCtx = mCtx;
        this.kanjiHomeModelList = kanjiHomeModelList;
    }

    @NonNull
    @Override
    public KanjiHomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.kanji_cards, null);
        final KanjiHomeViewHolder holder = new KanjiHomeViewHolder(view);

        //set onClickListener tiap holder
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KanjiHomeModel kanjiHomeModel = kanjiHomeModelList.get(holder.getAdapterPosition());
                //cek apakah chapter yang dipilih sudah terbuka atau belum
                if (!kanjiHomeModel.getLessonChapterLocker()) {
                    Toast.makeText(mCtx, mCtx.getString(R.string.clear_previous_chapter_first),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                //tujuan pasang bundle untuk memberi value chapter apa yang diakses
                Bundle bundle = new Bundle();
                int title = kanjiHomeModelList.get(holder.getAdapterPosition()).getKanjiLevel();
                bundle.putInt(mCtx.getString(R.string.kanji_level), title);

                //jika belum setting character preference, dibawa kesitu dulu
                sharedPref = PreferenceManager.getDefaultSharedPreferences(mCtx);
                String defaultCharacterValue = mCtx.getResources().getString(R.string.sharedPrefCharacter);
                String characterPref = sharedPref.getString(mCtx.getString(R.string.sharedPrefCharacter), defaultCharacterValue);
                if (characterPref.equals(defaultCharacterValue)) {
                    Intent intent = new Intent(mCtx, CharacterSettingActivity.class);
                    intent.putExtra(mCtx.getString(R.string.bundle_intro_kanji), bundle);
                    mCtx.startActivity(intent);
                } else {
                    Intent intent = new Intent(mCtx, KanjiIntroActivity.class);
                    intent.putExtra(mCtx.getString(R.string.bundle_intro_kanji), bundle);
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
    public void onBindViewHolder(@NonNull KanjiHomeViewHolder kanjiHomeViewHolder, int i) {
        KanjiHomeModel kanjiHomeModel = kanjiHomeModelList.get(i);
        kanjiHomeViewHolder.circleImageViewKanji.setImageDrawable(mCtx.getResources().getDrawable(kanjiHomeModel.getLessonKanjiImageView(), null));
        kanjiHomeViewHolder.textViewKanjiLevel.setText(kanjiHomeModel.getKanjiLevel());
        if (kanjiHomeModel.getLessonChapterLocker()) {
            kanjiHomeViewHolder.imageViewLocker.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return kanjiHomeModelList.size();
    }

    class KanjiHomeViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageViewKanji;
        ImageView imageViewLocker;
        TextView textViewKanjiLevel;
        public KanjiHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageViewKanji = itemView.findViewById(R.id.circleImageViewKanji);
            imageViewLocker = itemView.findViewById(R.id.imageViewLocker);
            textViewKanjiLevel = itemView.findViewById(R.id.textViewKanjiLevel);

        }
    }
}
