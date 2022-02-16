package com.inocen.gojepang.activity.card_list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.inocen.gojepang.R;
import com.inocen.gojepang.activity.CharacterSettingActivity;
import com.inocen.gojepang.activity.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder>{
    private Context mCtx;
    private List<CardModel> cardModelList;
    private SharedPreferences sharedPref;
    private int imageSizeDp=400;

    public CardAdapter(Context mCtx, List<CardModel> cardModelList) {
        this.mCtx = mCtx;
        this.cardModelList = cardModelList;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(mCtx);
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.card_list_cards, null);

        final CardViewHolder holder = new CardViewHolder(view);

        //set onClickListener tiap holder
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //animasi uy
                v.startAnimation(AnimationUtils.loadAnimation(mCtx, R.anim.test));

                CardModel cardModel=cardModelList.get(holder.getAdapterPosition());
                if(cardModel.isLesson){
                    switch (cardModel.flipCard){
                        case 0:
                            holder.imageViewCard.setVisibility(View.INVISIBLE);
                            String temp=cardModel.cardNumber+". Romaji";
                            holder.textViewNumber.setText(temp);
                            holder.textViewKanji.setText(cardModel.romaji);
                            holder.textViewKanji.setVisibility(View.VISIBLE);
                            cardModel.flipCard++;
                            break;
                        case 1:
                            String temp1=cardModel.cardNumber+". Kana";
                            holder.textViewNumber.setText(temp1);
                            holder.textViewKanji.setText(cardModel.kana);
                            cardModel.flipCard++;
                            break;
                        case 2:
                            String temp2=cardModel.cardNumber+". Kanji";
                            holder.textViewNumber.setText(temp2);
                            holder.textViewKanji.setText(cardModel.kanji);
                            cardModel.flipCard++;
                            break;
                        case 3:
                            String temp3=cardModel.cardNumber+".";
                            holder.textViewNumber.setText(temp3);
                            holder.imageViewCard.setVisibility(View.VISIBLE);
                            holder.textViewKanji.setVisibility(View.INVISIBLE);
                            cardModel.flipCard=0;
                            break;
                    }
                }
                else{
                    switch (cardModel.flipCard){
                        case 0:
                            String temp=cardModel.cardNumber+". Romaji";
                            holder.textViewNumber.setText(temp);
                            holder.textViewKanji.setVisibility(View.INVISIBLE);
                            holder.textViewKunyomi.setText(cardModel.romajikun);
                            holder.textViewOnyomi.setText(cardModel.romajion);
                            holder.textViewKunyomi.setVisibility(View.VISIBLE);
                            holder.textViewOnyomi.setVisibility(View.VISIBLE);
                            cardModel.flipCard++;
                            break;
                        case 1:
                            String temp1=cardModel.cardNumber+". Kana";
                            holder.textViewNumber.setText(temp1);
                            holder.textViewKunyomi.setText(cardModel.hiraganakun);
                            holder.textViewOnyomi.setText(cardModel.hiraganaon);
                            cardModel.flipCard++;
                            break;
                        case 2:
                            String temp3=cardModel.cardNumber+".";
                            holder.textViewNumber.setText(temp3);
                            holder.textViewKanji.setVisibility(View.VISIBLE);
                            holder.textViewKunyomi.setVisibility(View.INVISIBLE);
                            holder.textViewOnyomi.setVisibility(View.INVISIBLE);
                            cardModel.flipCard=0;
                    }
                }
            }
        });

        String defaultCharValue = "xx";
        String charPref = sharedPref.getString(mCtx.getString(R.string.card_list_type_romaji), defaultCharValue);

        CardModel cardModel=cardModelList.get(i);
        String cardNumber=cardModel.cardNumber+".";
        holder.textViewNumber.setText(cardNumber);
        if(cardModel.isKanji){
            holder.textViewKanji.setText(cardModel.kanji);
            switch (charPref) {
                case "romaji":
                    holder.textViewKanji.setVisibility(View.INVISIBLE);
                    String temp=cardModel.cardNumber+". Romaji";
                    holder.textViewNumber.setText(temp);
                    holder.textViewKanji.setVisibility(View.INVISIBLE);
                    holder.textViewKunyomi.setText(cardModel.romajikun);
                    holder.textViewOnyomi.setText(cardModel.romajion);
                    holder.textViewKunyomi.setVisibility(View.VISIBLE);
                    holder.textViewOnyomi.setVisibility(View.VISIBLE);
                    cardModel.flipCard=1;
                    break;
                case "kana":
                    holder.textViewKanji.setVisibility(View.INVISIBLE);
                    String temp1=cardModel.cardNumber+". Kana";
                    holder.textViewNumber.setText(temp1);
                    holder.textViewKunyomi.setText(cardModel.hiraganakun);
                    holder.textViewOnyomi.setText(cardModel.hiraganaon);
                    cardModel.flipCard=2;
                    break;
                case "kanji":
                    holder.textViewKanji.setVisibility(View.INVISIBLE);
                    String temp3=cardModel.cardNumber+". Kana";
                    holder.textViewNumber.setText(temp3);
                    holder.textViewKunyomi.setText(cardModel.hiraganakun);
                    holder.textViewOnyomi.setText(cardModel.hiraganaon);
                    cardModel.flipCard=2;
                    break;
            }
        }
        if(cardModel.isLesson){
            ZipResourceFile zipResourceFile;
            try {
                zipResourceFile = APKExpansionSupport.getAPKExpansionZipFile(mCtx, Constant.buildVersion, 0);
                InputStream inputStream = zipResourceFile.getInputStream(cardModel.resourcePath);
                Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                holder.imageViewCard.setImageBitmap(bmp);
//                holder.imageViewCard.setImageBitmap(Bitmap.createScaledBitmap(bmp, imageSizeDp, imageSizeDp, false));
            } catch (IOException e) {
                e.printStackTrace();
            }
            switch (charPref) {
                case "romaji":
                    holder.imageViewCard.setVisibility(View.INVISIBLE);
                    String temp=cardModel.cardNumber+". Romaji";
                    holder.textViewNumber.setText(temp);
                    holder.textViewKanji.setText(cardModel.romaji);
                    holder.textViewKanji.setVisibility(View.VISIBLE);
                    cardModel.flipCard=1;
                    break;
                case "kana":
                    holder.imageViewCard.setVisibility(View.INVISIBLE);
                    String temp1=cardModel.cardNumber+". Kana";
                    holder.textViewNumber.setText(temp1);
                    holder.textViewKanji.setText(cardModel.kana);
                    cardModel.flipCard=2;
                    break;
                case "kanji":
                    holder.imageViewCard.setVisibility(View.INVISIBLE);
                    String temp2=cardModel.cardNumber+". Kanji";
                    holder.textViewNumber.setText(temp2);
                    holder.textViewKanji.setText(cardModel.kanji);
                    cardModel.flipCard=3;
                    break;
            }
        }

        //dynamic ukuran item recycler view
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mCtx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //if you need three fix imageview in width
        int devicewidth = displaymetrics.widthPixels / 5 * 2;
        holder.cardView.getLayoutParams().width = devicewidth;
        holder.imageViewCard.getLayoutParams().width = devicewidth;
        //if you need same height as width you can set devicewidth in holder.image_view.getLayoutParams().height
        holder.cardView.getLayoutParams().height = devicewidth;
        holder.imageViewCard.getLayoutParams().height = devicewidth;

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder cardViewHolder, int i) {
        CardModel cardModel = cardModelList.get(i);


    }

    @Override
    public int getItemCount() {
        return cardModelList.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView imageViewCard;
        TextView textViewNumber;
        TextView textViewKanji;
        TextView textViewKunyomi;
        TextView textViewOnyomi;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.card_view);
            imageViewCard= itemView.findViewById(R.id.imageViewCard);
            textViewNumber= itemView.findViewById(R.id.textViewNumber);
            textViewKanji= itemView.findViewById(R.id.textViewKanji);
            textViewKunyomi= itemView.findViewById(R.id.textViewKunyomi);
            textViewOnyomi= itemView.findViewById(R.id.textViewOnyomi);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
