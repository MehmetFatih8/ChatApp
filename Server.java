
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server implements Runnable {





    private ArrayList<ConnectionHandler> connections;
    private ArrayList<String> nicknames;
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool;
    private String path = getClass().getResource("log.txt").getPath();
    private String userdatabase = getClass().getResource("userdatabase.txt").getPath();
    private File fileokuma = new File(path);
    private File fileyazma = new File(path);
    private File filedogrulamalogin = new File(userdatabase);
    private File filedogrulamasignup = new File(userdatabase);
    private File filedogrulamasignupyazma = new File(userdatabase);
    private int guestnumber = 100;





    public Server() {

        connections = new ArrayList<>();
        nicknames = new ArrayList<>();
        done = false;



    }
    

    @Override
    public void run() {


        try {
            ServerSocket server = new ServerSocket(9999);
            pool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add(handler);
                nicknames.add("guest"+guestnumber);
                guestnumber++;
                pool.execute(handler);




            }

        }
        catch (Exception e) {
            try {
                e.printStackTrace();
                shutdown();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }


    }




    public void broadcast(String message) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileyazma, true));
        writer.append(message +"\n");
        writer.close();


        for (ConnectionHandler ch : connections) {

            if(ch != null) {

                ch.sendMessage(message);


            }

        }

    }


    public void shutdown() throws IOException {
        done = true;



        if (!server.isClosed()) {

            server.close();

        }

        for (ConnectionHandler ch : connections){

            ch.shutdown();



        }


    }

    class ConnectionHandler implements Runnable{

        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private String nickname;
        private String log;
        private int logintamamlandi = 0;
        public ConnectionHandler(Socket client) {

            this.client = client;


        }


        @Override
        public void run() {

            try {

                for (int u = 0; u < connections.size(); u++){
                    System.out.println(u+". bağlantı: "+connections.get(u) + " --- " + connections.get(u).client.getInetAddress().getHostAddress()+":"+connections.get(u).client.getPort());
                    System.out.println(u+". bağlantının adı: "+nicknames.get(u) + " --- " + connections.get(u).client.getInetAddress().getHostAddress()+":"+connections.get(u).client.getPort());
                }
                out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);

                in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                String ipveport = client.getInetAddress().getHostAddress()+":"+client.getPort();

                System.out.println("Gelen kişi: "+ipveport);


                while (logintamamlandi == 0) {

                    String messagelogin;


                    messagelogin = in.readLine();

                    if (messagelogin == null){
                        throw new IOException();

                    }

                    else if (!messagelogin.equals("")) {

                        if (messagelogin.startsWith("[/*&[logintry")){

                            String[] logindeneme = messagelogin.split(":");

                            //TODO DOĞRULAMA LOGIN SISTEMI IVIR ZIVIR

                            int girisbasarili = 0;
                            BufferedReader logindogrulama;
                            try {
                                logindogrulama = new BufferedReader(new FileReader(filedogrulamalogin));
                                String line = logindogrulama.readLine();

                                while (line != null) {

                                    String[] eslestirmelogin = line.split(",");


                                    if (logindeneme[1].equals(eslestirmelogin[0]) && logindeneme[2].equals(eslestirmelogin[1])) {

                                        //System.out.println(client.getInetAddress().getHostAddress() + ":" + client.getPort() + " giriş yaptı");
                                        girisbasarili = 1;

                                    }

                                    line = logindogrulama.readLine();

                                }
                                logindogrulama.close();
                            } catch (FileNotFoundException ex) {
                                throw new RuntimeException(ex);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

                            if (girisbasarili == 0) {

                                out.println("[/*&[loginbasarisiz");
                                System.out.println(client.getInetAddress().getHostAddress()+":"+client.getPort() + " giriş yapamadı!");

                            }

                            if (girisbasarili == 1){

                                out.println("[/*&[loginbasarili");


                                for (int i = 0; i < connections.size(); i++) {

                                    if ((connections.get(i).client.getInetAddress().getHostAddress() + ":" + connections.get(i).client.getPort()).equals(client.getInetAddress().getHostAddress()+":"+client.getPort())) {


                                        nicknames.set(i , logindeneme[1]);
                                        nickname = nicknames.get(i);
                                        System.out.println(client.getInetAddress().getHostAddress()+":"+client.getPort() + " " + nickname + " adı ile giriş yaptı!");



                                    }


                                }
                                logintamamlandi = 1;
                                break;


                            }


                        }


                        if (messagelogin.startsWith("[/*&[signuptry")){

                            String[] signupdeneme = messagelogin.split(":");

                            // TODO KAYIT IVIR ZIVIR

                            int kaydauygun = 1;

                            BufferedReader signupdogrulama;
                            try {
                                signupdogrulama = new BufferedReader(new FileReader(filedogrulamasignup));
                                String line = signupdogrulama.readLine();

                                while (line != null) {
                                    String[] eslestirmesignup = line.split(",");

                                    if (signupdeneme[1].equals(eslestirmesignup[0])) {
                                        kaydauygun = 0;

                                    }

                                    line = signupdogrulama.readLine();

                                }
                                signupdogrulama.close();


                            } catch (IOException exx) {
                                throw new RuntimeException(exx);
                            }

                            if (kaydauygun == 0) {

                                System.out.println(client.getInetAddress().getHostAddress()+":"+client.getPort()+" "+signupdeneme[1]+" ismiyle kayıt olamadı!");
                                out.println("[/*&[signupbasarisiz");


                            }

                            if (kaydauygun == 1){

                                try {
                                    FileWriter writer = new FileWriter(filedogrulamasignup, true);
                                    writer.write(signupdeneme[1] + "," + signupdeneme[2] + "\n");
                                    writer.close();
                                    System.out.println(client.getInetAddress().getHostAddress()+":"+client.getPort()+" "+signupdeneme[1]+" ismiyle kayıt oldu!");


                                } catch (IOException exx) {
                                    throw new RuntimeException(exx);
                                }


                                out.println("[/*&[signupbasarili");

                                for (int i = 0; i < connections.size(); i++) {

                                    if ((connections.get(i).client.getInetAddress().getHostAddress() + ":" + connections.get(i).client.getPort()).equals(client.getInetAddress().getHostAddress()+":"+client.getPort())) {

                                        nicknames.set(i , signupdeneme[1]);
                                        nickname = nicknames.get(i);


                                    }


                                }



                                logintamamlandi = 1;
                                break;


                            }







                        }





                    }


                }







                BufferedReader reader = new BufferedReader(new FileReader(fileokuma));
                String line = reader.readLine();
                log = "";


                while (line != null) {

                    log = log + line + "\n";
                    line = reader.readLine();

                }
                reader.close();
                log = log.substring(0, log.length() - 1);


                // TODO println demememin sebebi logun sonunda /n vardı zaten

                out.println(log);

                broadcast(nickname + ": bağlandı!");

                String message;
                boolean isimizbittimi = false;
                while (!isimizbittimi) {

                    message = in.readLine();

                    if (message == null){
                        isimizbittimi = true;
                        throw new IOException();
                    }

                    else if (!message.equals("")){
                        System.out.println(nickname + ": " + message);

                        broadcast(nickname + ": " + message);
                    }
                    /*String message;
                    while (!(message = in.readLine()).equals("")) {

                        System.out.println(nickname + ": " + message);

                        broadcast(nickname + ": " + message);
                        break;


                    }*/
                }


            }
            catch (IOException e){
                System.out.println(client.getInetAddress().getHostAddress()+":"+client.getPort()+" kişisi ayrılmalı...");
                shutdown();

            }



        }

        public void sendMessage(String message){

            out.println(message);



        }


        public void shutdown() {

            for (int i = 0; i < connections.size(); i++) {

                if ((connections.get(i).client.getInetAddress().getHostAddress() + ":" + connections.get(i).client.getPort()).equals(client.getInetAddress().getHostAddress() + ":" + client.getPort())) {

                    System.out.println("Ayrılan: "+connections.get(i).client.getInetAddress().getHostAddress() + ":" + connections.get(i).client.getPort());
                    connections.remove(i);

                    String gidenkullaniciadi = nicknames.get(i);
                    nicknames.remove(i);

                    if (logintamamlandi == 1) {
                        try {
                            broadcast(gidenkullaniciadi + " ayrıldı!");
                        } catch (IOException g) {
                            g.printStackTrace();
                        }
                    }

                }
            }

            try{
                in.close();
                out.close();
                if (!client.isClosed()){
                    client.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }


        }

    }


    public static void main(String[] args) {



        Server server = new Server();
        server.run();
    }

}