package com.inocen.gojepang.databinding;

public class KanjiHomeModel {
    private int kanjiLevel;
    private boolean lessonChapterLocker;
    private int lessonKanjiImageView;
    private int lessonLockerImageView;

    public KanjiHomeModel(int kanjiLevel, boolean lessonChapterLocker, int lessonKanjiImageView, int lessonLockerImageView) {
        this.kanjiLevel = kanjiLevel;
        this.lessonChapterLocker = lessonChapterLocker;
        this.lessonKanjiImageView = lessonKanjiImageView;
        this.lessonLockerImageView = lessonLockerImageView;
    }

    public int getKanjiLevel() {
        return kanjiLevel;
    }

    public void setKanjiLevel(int kanjiLevel) {
        this.kanjiLevel = kanjiLevel;
    }

    public boolean getLessonChapterLocker() {
        return lessonChapterLocker;
    }

    public void setLessonChapterLocker(boolean lessonChapterLocker) {
        this.lessonChapterLocker = lessonChapterLocker;
    }

    public int getLessonKanjiImageView() {
        return lessonKanjiImageView;
    }

    public void setLessonKanjiImageView(int lessonKanjiImageView) {
        this.lessonKanjiImageView = lessonKanjiImageView;
    }

    public int getLessonLockerImageView() {
        return lessonLockerImageView;
    }

    public void setLessonLockerImageView(int lessonLockerImageView) {
        this.lessonLockerImageView = lessonLockerImageView;
    }
}
