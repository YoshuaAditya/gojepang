package com.inocen.gojepang.databinding;

public class LessonHomeModel {
    private int lessonChapterNumber;
    private int lessonChapterTitle;
    private boolean lessonChapterLocker;
    private int lessonChapterImageView;
    private int lessonLockerImageView;

    public LessonHomeModel(int lessonChapterNumber, int lessonChapterTitle, boolean lessonChapterLocker, int lessonChapterImageView, int lessonLockerImageView) {
        this.lessonChapterNumber = lessonChapterNumber;
        this.lessonChapterTitle = lessonChapterTitle;
        this.lessonChapterLocker = lessonChapterLocker;
        this.lessonChapterImageView = lessonChapterImageView;
        this.lessonLockerImageView = lessonLockerImageView;
    }

    public int getLessonLockerImageView() {
        return lessonLockerImageView;
    }

    public void setLessonLockerImageView(int lessonLockerImageView) {
        this.lessonLockerImageView = lessonLockerImageView;
    }

    public int getLessonChapterNumber() {
        return lessonChapterNumber;
    }

    public void setLessonChapterNumber(int lessonChapterNumber) {
        this.lessonChapterNumber = lessonChapterNumber;
    }

    public int getLessonChapterTitle() {
        return lessonChapterTitle;
    }

    public void setLessonChapterTitle(int lessonChapterTitle) {
        this.lessonChapterTitle = lessonChapterTitle;
    }

    public boolean getLessonChapterLocker() {
        return lessonChapterLocker;
    }

    public void setLessonChapterLocker(boolean lessonChapterLocker) {
        this.lessonChapterLocker = lessonChapterLocker;
    }

    public int getLessonChapterImageView() {
        return lessonChapterImageView;
    }

    public void setLessonChapterImageView(int lessonChapterImageView) {
        this.lessonChapterImageView = lessonChapterImageView;
    }
}
