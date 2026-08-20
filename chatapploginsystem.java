package com.mfatih.mfatihchatapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class loginsystem extends AppCompatActivity {

    private EditText kullaniciadi;
    private PrintWriter outlogin;
    private BufferedReader inlogin;
    private EditText sifre;
    private Button girisyap;
    private Button kayitol;
    private TextView girisyapabilmedurumu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);




        kullaniciadi =  (EditText) findViewById(R.id.kullaniciadiedittext);
        sifre = (EditText) findViewById(R.id.sifreedittext);
        girisyap = (Button) findViewById(R.id.girisyapbutonu);
        kayitol = (Button) findViewById(R.id.kayitolbutonu);
        girisyapabilmedurumu = (TextView) findViewById(R.id.girisyapabilmedurumu);

        if (savedInstanceState == null) {

            String serverip = getIntent().getExtras().getString("ip");
            Log.i("GELEN IP ", serverip);

            new Thread(new Runnable() {
                @Override
                public void run() {


                    try {

                        //Socket client = new Socket("192.168.1.33", 9999);
                        //outlogin = new PrintWriter(MainActivity.getSocket().getOutputStream(), true);
                        //inlogin = new BufferedReader(new InputStreamReader(MainActivity.getSocket().getInputStream()));


                        String inMessage;
                        while ((inMessage = new BufferedReader(new InputStreamReader(MainActivity.getSocket().getInputStream())).readLine()) != null) {



                            if (inMessage.equals("[/*&[loginbasarisiz")) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        girisyapabilmedurumu.setText("Kullanıcı adı veya şifre hatalı...");

                                    }
                                });

                            }

                            if (inMessage.equals("[/*&[loginbasarili")) {

                            /*try {
                                outlogin.close();
                                inlogin.close();
                            }
                            catch (IOException e){
                                Log.i("LOGIN SONRASI KAPATAMADIM", e.toString());
                            }*/

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        girisyapabilmedurumu.setText("Başarıyla giriş yaptınız...");

                                        Log.i("Başarıyla giriş yapıldı!", ":)");

                                        dosyayazdirma(kullaniciadi.getText().toString(), getApplicationContext(), "username.txt");
                                        dosyayazdirma(sifre.getText().toString(), getApplicationContext(), "password.txt");

                                        kullaniciadi.setEnabled(false);
                                        sifre.setEnabled(false);

                                        // TODO : TEKRAR TEKRAR KULLANABILMEM ICIN BOSLUK YAZDIRIYORUM
                                        //dosyayazdirma("", getApplicationContext(), "username.txt");
                                        //Log.i("Boşluk yazdırdım", "");
                                        finish();

                                    }
                                });

                                break;

                            }

                            if (inMessage.equals("[/*&[loginbasarisiz")) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        girisyapabilmedurumu.setText("Kullanıcı adı veya şifre hatalı!...");

                                    }
                                });


                            }
                            if (inMessage.equals("[/*&[signupbasarili")) {

                            /*try {
                                outlogin.close();
                                inlogin.close();
                            }
                            catch (IOException e){
                                Log.i("SIGNUP SONRASI KAPATAMADIM", e.toString());
                            }*/


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        girisyapabilmedurumu.setText("Başarıyla kayıt oldunuz...");

                                        dosyayazdirma(kullaniciadi.getText().toString(), getApplicationContext(), "username.txt");
                                        dosyayazdirma(sifre.getText().toString(), getApplicationContext(), "password.txt");

                                        kullaniciadi.setEnabled(false);
                                        sifre.setEnabled(false);


                                        // TODO : TEKRAR TEKRAR KULLANABILMEM ICIN BOSLUK YAZDIRIYORUM
                                        //dosyayazdirma("", getApplicationContext(), "username.txt");
                                        //Log.i("Boşluk yazdırdım", "");

                                        finish();


                                    }
                                });

                                break;


                            }

                            if (inMessage.equals("[/*&[signupbasarisiz")) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        girisyapabilmedurumu.setText("Bu kullanıcı adı zaten kullanımda...");

                                    }
                                });

                            }


                        }

                    } catch (IOException e) {
                        Log.i("HATA", e.toString());

                    }


                }

            }).start();
        }


        girisyap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                girisyap.setEnabled(false);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        girisyap.setEnabled(true);
                    }
                },2000);

                String kullaniciaditext = kullaniciadi.getText().toString();
                String sifreedittext = sifre.getText().toString();

                int usernamekontrol = kullaniciaditext.indexOf(",");
                int passwordkontrol = sifreedittext.indexOf(",");

                if (!kullaniciaditext.equals("[/*&[") && !sifreedittext.equals("[/*&[")) {

                    if (kullaniciaditext.length() > 4 && sifreedittext.length() > 4) {

                        if (kullaniciaditext.length() <= 12 && sifreedittext.length() <= 12) {

                            if (usernamekontrol == -1 && passwordkontrol == -1) {


                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try {
                                            new PrintWriter(MainActivity.getSocket().getOutputStream(), true).println("[/*&[logintry:" + kullaniciaditext + ":" + sifreedittext);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }

                                    }
                                }).start();
                            }
                            else {
                                girisyapabilmedurumu.setText("Kullanıcı adında veya şifrende virgül bulunamaz!");
                            }
                        }
                        else {
                            girisyapabilmedurumu.setText("Kullanıcı adın ve şifrenin uzunluğu 12 yi geçemez!");
                        }

                    }
                    else {
                        girisyapabilmedurumu.setText("Kullanıcı adın ve şifren 4 harften uzun olmalı!");
                    }

                }
                else {
                    girisyapabilmedurumu.setText("Bu özel karakterleri böyle kullanamazsın!");
                }



            }
        });

        kayitol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                kayitol.setEnabled(false);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        kayitol.setEnabled(true);
                    }
                },2000);


                String kullaniciaditext = kullaniciadi.getText().toString();
                String sifreedittext = sifre.getText().toString();

                int usernamekontrol = kullaniciaditext.indexOf(",");
                int passwordkontrol = sifreedittext.indexOf(",");

                if (!kullaniciaditext.equals("") || !sifreedittext.equals("")) {

                    if (kullaniciaditext.length() > 4 && sifreedittext.length() > 4) {

                        if (usernamekontrol == -1 && passwordkontrol == -1) {


                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        new PrintWriter(MainActivity.getSocket().getOutputStream(), true).println("[/*&[signuptry:"+kullaniciaditext+":"+sifreedittext);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }

                                }
                            }).start();
                        }

                        else {
                            girisyapabilmedurumu.setText("Kullanıcı adında veya şifrende virgül bulunamaz!");
                        }

                    }
                    else {
                        girisyapabilmedurumu.setText("Kullanıcı adın ve şifren 4 harften uzun olmalı!");
                    }

                }
                else {

                    girisyapabilmedurumu.setText("Kullanıcı adın ve şifre kısmını doldurmalısın.................................");

                }

            }
        });



    }


    private void dosyayazdirma(String data, Context context, String towhere) {

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(towhere, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.i("Yazdığım şey ", data);
        } catch (IOException e) {
            Log.e("IO HATASI", "YAZAMADIM " + e.toString());
        }

    }

}
