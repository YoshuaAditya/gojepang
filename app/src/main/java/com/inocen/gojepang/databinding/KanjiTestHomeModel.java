package com.inocen.gojepang.databinding;

public class KanjiTestHomeModel {
    private int kanjiTestLevel;
    private boolean lessonChapterLocker;
    private int lessonKanjiTestImageView;
    private int lessonLockerImageView;

    public KanjiTestHomeModel(int kanjiTestLevel, boolean lessonChapterLocker, int lessonKanjiTestImageView, int lessonLockerImageView) {
        this.kanjiTestLevel = kanjiTestLevel;
        this.lessonChapterLocker = lessonChapterLocker;
        this.lessonKanjiTestImageView = lessonKanjiTestImageView;
        this.lessonLockerImageView = lessonLockerImageView;
    }

    public int getKanjiTestLevel() {
        return kanjiTestLevel;
    }

    public void setKanjiTestLevel(int kanjiTestLevel) {
        this.kanjiTestLevel = kanjiTestLevel;
    }

    public boolean isLessonChapterLocker() {
        return lessonChapterLocker;
    }

    public void setLessonChapterLocker(boolean lessonChapterLocker) {
        this.lessonChapterLocker = lessonChapterLocker;
    }

    public int getLessonKanjiTestImageView() {
        return lessonKanjiTestImageView;
    }

    public void setLessonKanjiTestImageView(int lessonKanjiTestImageView) {
        this.lessonKanjiTestImageView = lessonKanjiTestImageView;
    }

    public int getLessonLockerImageView() {
        return lessonLockerImageView;
    }

    public void setLessonLockerImageView(int lessonLockerImageView) {
        this.lessonLockerImageView = lessonLockerImageView;
    }
}
