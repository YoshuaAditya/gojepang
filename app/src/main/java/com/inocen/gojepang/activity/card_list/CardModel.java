package com.inocen.gojepang.activity.card_list;

public class CardModel {

    int cardNumber;
    String resourcePath;
    String kanji;
    boolean isLesson;
    String kana;
    String romaji;
    boolean isKanji;
    String hiraganakun;
    String hiraganaon;
    String romajikun;
    String romajion;
    int flipCard=0;

    public CardModel(int cardNumber, String resourcePath, String kanji,
                     boolean isLesson, String kana,String romaji,
                     boolean isKanji, String hiraganakun, String hiraganaon, String romajikun, String romajion) {
        this.cardNumber=cardNumber;
        this.resourcePath=resourcePath;
        this.kanji=kanji;
        this.isLesson=isLesson;
        this.kana=kana;
        this.romaji=romaji;
        this.isKanji=isKanji;
        this.hiraganakun=hiraganakun;
        this.hiraganaon=hiraganaon;
        this.romajikun=romajikun;
        this.romajion=romajion;
    }
}
