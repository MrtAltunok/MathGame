package com.example.quizgame;

public class SoruModal {
    private String Soru;
    private String Cevap;

    public SoruModal(String soru, String cevap) {
        this.Soru = soru;
        this.Cevap = cevap;
    }

    public String getSoru() {
        return Soru;
    }

    public void setSoru(String soru) {
        Soru = soru;
    }

    public String getCevap() {
        return Cevap;
    }

    public void setCevap(String cevap) {
        Cevap = cevap;
    }
}
