package com.inocen.gojepang.activity.jlpt;

public class Soal implements Comparable<Soal> {

    String soal;
    String extraSoal;
    String a;
    String b;
    String c;
    String d;
    String jawaban;
    String mondai;

    public Soal(String soal, String extraSoal, String a, String b, String c, String d, String jawaban, String mondai) {
        this.soal = soal;
        this.extraSoal = extraSoal;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.jawaban = jawaban;
        this.mondai = mondai;
    }

    @Override
    public int compareTo(Soal o) {
        return this.mondai.compareTo(o.mondai);
    }
}
