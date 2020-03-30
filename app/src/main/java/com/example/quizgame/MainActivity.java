package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    TextView textsoruNumarasi, textDerece, textSoruYaz;
    EditText cevap;
    Button btnCevapla;
    ProgressBar progress;
    ArrayList<SoruModal> soruListesi;

    int soruSayac = 0;
    int derece = 0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textsoruNumarasi = findViewById(R.id.textsoruNumarasi);
        textSoruYaz = findViewById(R.id.textSoruYaz);
        textDerece = findViewById(R.id.textDerece);

        cevap = findViewById(R.id.edittextCevap);
        btnCevapla = findViewById(R.id.btnCevapla);
        progress = findViewById(R.id.progress);


        soruListesi = new ArrayList<>();

        setSoruListesi();
        setData();

        btnCevapla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soruCevapla();
            }
        });
    }

    public void soruCevapla() {
        String cevapbin = cevap.getText().toString().trim();
        if (cevapbin.equals(soruListesi.get(soruSayac).getCevap())) {
            derece++;
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Güzel")
                    .setConfirmText("Tamam")
                    .setContentText("Cevabınız doğru..")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            soruSayac++;
                            setData();
                            cevap.setText("");
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
        } else {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Üzgünüm")
                    .setConfirmText("Tamam")
                    .setContentText("Cevabınız hatalıdır..")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            soruSayac++;
                            setData();
                            cevap.setText("");
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
        }

        int x = ((soruSayac + 1) - 100) / soruListesi.size();
        progress.setProgress(x);
    }


    public void setData() {
        if (soruListesi.size() > soruSayac) {
            textSoruYaz.setText(soruListesi.get(soruSayac).getSoru());
            textsoruNumarasi.setText("Soru numarası :" + (soruSayac + 1));
            textDerece.setText("Skor :" + derece + "/" + soruListesi.size());
        } else {
            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Bütün soruları cevapladınız.")
                    .setConfirmText("Tekrarla")
                    .setContentText("Skorunuz : " + derece + "/" + soruListesi.size())
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            soruSayac = 0;
                            derece = 0;
                            progress.setProgress(0);
                            setSoruListesi();
                            cevap.setText("");
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    })
                    .setCancelText("Çıkış")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            finish();
                        }
                    })
                    .show();
        }
    }

    public void setSoruListesi() {
        for (int i = 0; i < 15; i++) {
            int ilkSayi = sayiUret();
            int ikinciSayi = sayiUret();
            soruUret(ilkSayi, ikinciSayi);
        }
    }


    public int sayiUret() {
        Random rdm = new Random();
        int sayi = 0;
        while (true) {
            sayi = rdm.nextInt(50);
            if (sayi != 0) break;
        }
        return sayi;
    }

    public int uret() {
        Random rdm = new Random();
        int sayi = 0;
        while (true) {
            sayi = rdm.nextInt(4);
            if (sayi != 0) break;
        }
        return sayi;
    }

    public void soruUret(int ilkSayi, int ikinciSayi) {
        Random rdm = new Random();

        int a = rdm.nextInt(4);
        char[] islemTipi = {'-', '+', '/', '*'};

        if (islemTipi[a] == '/') {
            if (bolmeKontrol(ilkSayi, ikinciSayi) == false) {
                int _ilk = sayiUret();
                int _son = sayiUret();
                soruUret(_ilk, _son);
            }
            else{
                git(ilkSayi, ikinciSayi, islemTipi[a]);
            }
        }else {
            git(ilkSayi, ikinciSayi, islemTipi[a]);
        }


    }

    private void git(int ilkSayi, int ikinciSayi, char islemTipi ) {
        String islem;
        int cevaps;
        if (ikinciSayi > ilkSayi) {
            islem = ikinciSayi + "" + islemTipi + "" + ilkSayi;
            cevaps = islemYap(ikinciSayi, ilkSayi, islemTipi);

        } else {
            islem = ilkSayi + "" + islemTipi + "" + ikinciSayi;
            cevaps = islemYap(ilkSayi, ikinciSayi,islemTipi);
        }

        soruListesi.add(new SoruModal(islem + " = ? ", String.valueOf(cevaps)));
    }

    public boolean bolmeKontrol(int a, int b) {
        if (a < b) {
            bolmeKontrol(b, a);
        } else {
            if (a % b == 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public int islemYap(int a, int b, char islemTipi) {
        switch (islemTipi) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '/':
                    return a / b;
            case '*':
                return a * b;
        }
        return 0;
    }
}
