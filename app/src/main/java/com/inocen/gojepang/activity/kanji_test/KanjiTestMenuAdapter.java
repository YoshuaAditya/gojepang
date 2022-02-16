package com.inocen.gojepang.activity.kanji_test;

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
import com.inocen.gojepang.activity.kanji.KanjiIntroActivity;
import com.inocen.gojepang.databinding.KanjiHomeModel;
import com.inocen.gojepang.databinding.KanjiTestHomeModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class KanjiTestMenuAdapter extends RecyclerView.Adapter<KanjiTestMenuAdapter.KanjiTestMenuAdapterViewHolder> {
    private Context mCtx;
    private List<KanjiTestHomeModel> kanjiTestHomeModelList;
    private SharedPreferences sharedPref;

    public KanjiTestMenuAdapter(Context mCtx, List<KanjiTestHomeModel> kanjiTestHomeModelList) {
        this.mCtx = mCtx;
        this.kanjiTestHomeModelList = kanjiTestHomeModelList;
    }

    @NonNull
    @Override
    public KanjiTestMenuAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.kanji_test_cards, null);
        final KanjiTestMenuAdapterViewHolder holder = new KanjiTestMenuAdapterViewHolder(view);

        //set onClickListener tiap holder
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KanjiTestHomeModel kanjiTestHomeModel = kanjiTestHomeModelList.get(holder.getAdapterPosition());
                //cek apakah chapter yang dipilih sudah terbuka atau belum
                if (!kanjiTestHomeModel.isLessonChapterLocker()) {
                    Toast.makeText(mCtx, mCtx.getString(R.string.clear_previous_chapter_first),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                //tujuan pasang bundle untuk memberi value chapter apa yang diakses
                Bundle bundle = new Bundle();
                int title = kanjiTestHomeModelList.get(holder.getAdapterPosition()).getKanjiTestLevel();
                bundle.putInt(mCtx.getString(R.string.bundle_test_kanji_menu_adapter), title);

                //jika belum setting character preference, dibawa kesitu dulu
                sharedPref = PreferenceManager.getDefaultSharedPreferences(mCtx);

                Intent intent = new Intent(mCtx, KanjiTestContainerActivity.class);
                intent.putExtra(mCtx.getString(R.string.bundle_test_kanji_menu_adapter), bundle);
                mCtx.startActivity(intent);

                //finish activity ini
                if (mCtx instanceof Activity) {
                    ((Activity) mCtx).finish();
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull KanjiTestMenuAdapterViewHolder kanjiTestMenuAdapterViewHolder, int i) {
        KanjiTestHomeModel kanjiTestHomeModel = kanjiTestHomeModelList.get(i);
        kanjiTestMenuAdapterViewHolder.textViewKanjiLevel.setText(kanjiTestHomeModel.getKanjiTestLevel());
        kanjiTestMenuAdapterViewHolder.circleImageViewKanjiTest.setImageDrawable(mCtx.getResources().getDrawable(kanjiTestHomeModel.getLessonKanjiTestImageView(), null));
        kanjiTestMenuAdapterViewHolder.imageViewLocker.setImageDrawable(mCtx.getResources().getDrawable(kanjiTestHomeModel.getLessonLockerImageView(), null));
        if (kanjiTestHomeModel.isLessonChapterLocker()) {
            kanjiTestMenuAdapterViewHolder.imageViewLocker.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return kanjiTestHomeModelList.size();
    }

    class KanjiTestMenuAdapterViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageViewKanjiTest;
        ImageView imageViewLocker;
        TextView textViewKanjiLevel;

        public KanjiTestMenuAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageViewKanjiTest = itemView.findViewById(R.id.circleImageViewKanjiTest);
            imageViewLocker = itemView.findViewById(R.id.imageViewLocker);
            textViewKanjiLevel = itemView.findViewById(R.id.textViewKanjiLevel);
        }
    }
}
