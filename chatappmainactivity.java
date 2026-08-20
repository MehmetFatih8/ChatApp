package com.mfatih.mfatihchatapp;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity{
    TextView sohbetgecmisi;
    EditText gonderilecekmesaj;
    Button gonder;
    Button oturumukapat;

    private BufferedReader in;
    private PrintWriter out;
    private boolean done;

    boolean oturumukapattimmi = false;


    int butonatikladim = 0;

    public static Socket client;

    public static Socket getSocket(){
        return client;
    }

    public static synchronized void setSocket(Socket client){
        MainActivity.client = client;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //MainActivity client = new MainActivity();
        //client.run();


        sohbetgecmisi =  (TextView) findViewById(R.id.sohbetgecmisitextview);
        gonderilecekmesaj =  (EditText) findViewById(R.id.gonderilecekmesajedittext);
        gonder = (Button) findViewById(R.id.gonderbutonu);
        sohbetgecmisi.setMovementMethod(new ScrollingMovementMethod());
        oturumukapat = (Button) findViewById(R.id.oturumukapatbutonu);

        if (savedInstanceState == null || oturumukapattimmi) {


            new Thread(new Runnable() {
                public void run() {

                    String ip = null;
                    Log.i("Oturumu kapattım mı ", String.valueOf(oturumukapattimmi));

                    String inputLine = null;
                    try {


                        URL website = new URL("https://minecraftfatih.tr.gg/");
                        //URLConnection connection = website.openConnection();
                        BufferedReader webokuma = new BufferedReader(
                                new InputStreamReader(
                                        website.openConnection().getInputStream()));


                        inputLine = webokuma.readLine();

                        while (inputLine != null) {


                            if (inputLine.startsWith("<!--Hedefip = ")) {

                                break;

                            }
                            inputLine = webokuma.readLine();

                        }


                        String[] ayrilmisip = inputLine.split("Z");
                        ip = ayrilmisip[1];
                        webokuma.close();

                        if (ip != null) {
                            Log.i("Websiteden geldi ve değiştiriyorum -> ", "Yeni ip "+ip);
                            writeToFile(ip, getApplicationContext(), "svip.txt");
                        }

                        Log.i("Websiteden bulunan ip ", ip);
                    } catch (IOException e) {
                        if (ip == null) {

                            Log.i("Websiteden ip gelemedi değiştiriyorum", "change");
                            ip = readFromFile(getApplicationContext(), "svip.txt");

                        }
                    }

                    try {

                        int loginbasarili = 0;

                        Log.i("Socket zamanındaki ip ", ip);
                        client = new Socket(ip, 9999);
                        setSocket(client);

                        out = new PrintWriter(client.getOutputStream(), true);
                        in = new BufferedReader(new InputStreamReader(client.getInputStream()));




                    /*
                    String inMessage;
                    while ((inMessage = in.readLine()) != null) {


                        String finalInMessage = inMessage;
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                sohbetgecmisi.append(finalInMessage + "\n");

                            }
                        });


                    }*/

                        if (readFromFile(getApplicationContext(), "username.txt") != null && !Objects.equals(readFromFile(getApplicationContext(), "username.txt"), "") && readFromFile(getApplicationContext(), "password.txt") != null && !Objects.equals(readFromFile(getApplicationContext(), "password.txt"), "java.io.FileNotFoundException")) {


                            out.println("[/*&[logintry:" + readFromFile(getApplicationContext(), "username.txt") + ":" + readFromFile(getApplicationContext(), "password.txt"));
                            String inMessagelogin;
                            while ((inMessagelogin = in.readLine()) != null) {


                                if (inMessagelogin.equals("[/*&[loginbasarili")) {

                                    loginbasarili = 1;
                                    break;

                                }

                            }


                            Log.i("Daha önceki hesaptan giriş yaptım.", "Daha önceki hesaptan giriş yaptım.");

                        }

                        if (loginbasarili == 0) {
                            Intent intent = new Intent(MainActivity.this, loginsystem.class);
                            intent.putExtra("ip", "192.168.1.33");

                            String bastakikullaniciadi = readFromFile(getApplicationContext(), "username.txt");
                            String bastakisifre = readFromFile(getApplicationContext(), "password.txt");

                            startActivity(intent);
                            boolean farklimi = false;

                            while (!farklimi) {


                                if (!Objects.equals(bastakikullaniciadi, readFromFile(getApplicationContext(), "username.txt")) || !Objects.equals(bastakisifre, readFromFile(getApplicationContext(), "password.txt"))) {

                                    Log.i("Kullanıcı adı veya şifre değişti.", "Kullanıcı adı veya şifre değişti.");
                                    farklimi = true;

                                }

                            }


                        }

                        Log.i("Login sonrasındayım", "Login sonrasındayım");


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                gonderilecekmesaj.setEnabled(true);
                                gonder.setEnabled(true);
                                oturumukapat.setEnabled(true);


                            }
                        });


                        //in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                        String inMessage;
                        oturumukapattimmi = false;

                        while ((inMessage = in.readLine()) != null) {




                            String finalInMessage = inMessage;
                            runOnUiThread(() -> sohbetgecmisi.append(finalInMessage + "\n"));


                        }


                    } catch (IOException e) {

                        Log.e("Gelen mesajları okuma kısmında hata var.", e.toString());

                        if (oturumukapattimmi){

                            try {
                                in.close();
                            } catch (IOException ex) {
                                Log.e(ex.toString(),"hata");
                            }
                            out.close();


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                    startActivity(getIntent());
                                }
                            });


                        }

                        shutdown();
                    }


                }
            }).start();






        /*kullaniciadi.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER) ||
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {


                        baglanmak();

                        return true;
                    }
                return false;
            }
        });*/




        /*baglan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                baglanmak();


            }
        });*/


            gonderilecekmesaj.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN)
                        if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER) ||
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {


                            gondermek();

                            return true;
                        }
                    return false;
                }
            });

            gonder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    gondermek();

                }
            });

            oturumukapat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    writeToFile("", getApplicationContext(), "username.txt");
                    writeToFile("", getApplicationContext(), "password.txt");
                    oturumukapattimmi = true;
                    try {
                        client.close();
                    }catch (IOException e){
                        Log.e(e.toString(),"HATA VAR LA");
                    }


                }
            });

        // TODO: ALTIMDAKİ SAVED INSTANCE OLAN
        }

    }

    /*while (sohbetgecmisi.canScrollVertically(1)) {
        sohbetgecmisi.scrollBy(0, 10);
    }*/



    private void writeToFile(String data,Context context, String towhere) {

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(towhere, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.i("Yazdığım şey ", readFromFile(getApplicationContext(), towhere));
        } catch (IOException e) {
            Log.e("IO HATASI", "YAZAMADIM " + e.toString());
        }

    }

    private String readFromFile(Context context, String fromwhere) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fromwhere);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            return "java.io.FileNotFoundException";
            //Log.e("Dosya Bulunamadi", "Dosya yok " + e.toString());
        } catch (IOException e) {
            Log.e("IO HATASI", "OKUYAMIYORUM " + e.toString());
        }

        return ret;
    }


    public void gondermek(){



        new Thread(new Runnable() {
            @Override
            public void run() {

                if (!done){



                    String message = gonderilecekmesaj.getText().toString();
                    out.println(message);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            gonderilecekmesaj.setText("");
                            //gonderilecekmesaj.setText(readFromFile(getApplicationContext()));


                        }
                    });


                }

            }
        }).start();
    }

    /*@Override
    public void run() {

        try {
            Socket client = new Socket("192.168.1.33", 9999);
            //out = new PrintWriter(client.getOutputStream(), true);
            //in = new BufferedReader(new InputStreamReader(client.getInputStream()));


            InputHandler inHandler = new InputHandler();
            Thread t = new Thread(inHandler);
            t.start();

            String inMessage;
            while ((inMessage = in.readLine()) !=null) {

                Log.i(inMessage, inMessage);
                sohbetgecmisi.append(inMessage+"\n");

            }


        }
        catch (IOException e){
            shutdown();
        }


    }*/

    public void shutdown() {


        done = true;
        try {
            in.close();
            out.close();
            if (!client.isClosed()){

                client.close();

            }
        } catch (IOException e){
            // ignore
        }



    }

    /*class InputHandler {

        new Thread(new Runnable()





        //BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));


        gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        while (!done){

                            Log.i("mesaj göndermedeyim loo", "mesaj gönderiyom len");
                            String message = gonderilecekmesaj.getText().toString();
                            out.println(message);

                        }


                    }
                });



            }
        });








    }*/








}

