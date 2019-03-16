package unbi.com.tcpserverfinal.services;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;

import unbi.com.tcpserverfinal.intrfce.MyTCPinterface;
import unbi.com.tcpserverfinal.objects.StringPair;
import unbi.com.tcpserverfinal.singleton.CustomActivity;
import unbi.com.tcpserverfinal.singleton.UserPrefernece;
import unbi.com.tcpserverfinal.utility.AES_Util;


/**
 * AsyncTask which handles the commiunication with clients
 */
public class ServerAsyncTask extends AsyncTask<Socket, Void, String> {
    private MyTCPinterface myTCPinter;
    private SocketAddress remote;

    ServerAsyncTask(MyTCPinterface myTCPinterface) {
        this.myTCPinter = myTCPinterface;
    }
    @Override
    protected String doInBackground(Socket... params) {
//            Log.d("LOG HERE","SERVER CONNECTED");
        //TODO Here is the server coonected
        String result = null;
        Socket mySocket = params[0];
        remote= mySocket.getRemoteSocketAddress();
        try {

            InputStream is = mySocket.getInputStream();
            PrintWriter out = new PrintWriter(mySocket.getOutputStream(),
                    true);

            //out.println("Welcome to \""+Server_Name+"\" Server");

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is));

            result = br.readLine();

            //mySocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mySocket.close();
        } catch (IOException e) {

//                Log.d("LOG HERE","CLOSE ERROR");
        }


//            Log.d("LOG HERE","SERVER CLOSE");
        return result;
    }


    @Override
    protected void onPostExecute(String s) {

        //TODO HERE IS THE MSG RECEIVED

        if (s != null) {
            if (!UserPrefernece.getInstance().isEncryptedTransmision()) {
                UserPrefernece.getInstance().setMsg(s);
                ArrayIntents(s);// here we do the activity
            } else {
                String decrypted = (AES_Util.decrypt(s));
                if (decrypted == null) {
                    myTCPinter.Toast("Decryption fail...");
                    return;
                }
                String[] array = decrypted.split("=:=");
                if (isValid(array[array.length - 1])) {
                    UserPrefernece.getInstance().setMsg(new Gson().toJson(new StringPair(s, decrypted, true)));
                    ArrayIntents(array);// here we do the activity
                } else {
                    UserPrefernece.getInstance().setMsg(new Gson().toJson(new StringPair(s, decrypted, false)));
                    myTCPinter.Toast("Verification fail...");
                    return;
                }
            }
            new Thread() {
                public void run() {
//                    Object result = null;
                    Looper l = Looper.getMainLooper();
                    Handler h = new Handler(l);
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            //update ui here
                            // display toast here
                            Intent i = new Intent();
                            i.setAction("SERVICE");
                            i.putExtra("MSG", UserPrefernece.getInstance().getMsg());
                            if (myTCPinter != null) {
                                myTCPinter.sendBroadCast(i);//this intent is for broadcarting in the MainActivity
                                myTCPinter.Toast(UserPrefernece.getInstance().getMsg());
                            }


                        }
                    });
                }

                ;
            }.start();


        } else {
            s = "null";
            UserPrefernece.getInstance().setMsg("null");
        }


    }

    private boolean isValid(String msgId) {
        if (!UserPrefernece.getInstance().isValidChecker()) {
            return true;
        }
        long MULTIPLYER = 10000;
        long CONSTANT = 600000 * MULTIPLYER;//maximum valid time 10 minute after that it will ce invalid
        long id;
        long currentmilli = System.currentTimeMillis() * MULTIPLYER;
        try {
            id = Math.abs(Long.parseLong(msgId));
        } catch (Exception e) {
            id = 0L;
        }
        for (String oldid : UserPrefernece.getInstance().getDonedIds()) {
            if (myParseLong(oldid) < currentmilli - CONSTANT) {
                UserPrefernece.getInstance().getDonedIds()
                        .remove(oldid);//
            }
            if (msgId.equals(oldid)) {
                return false;
            }
        }
        UserPrefernece.getInstance().getDonedIds().add(String.valueOf(id));
        return id > currentmilli - CONSTANT && id < currentmilli + CONSTANT;
    }

    private long myParseLong(String parsable) {
        try {
            return Math.abs(Long.parseLong(parsable));
        } catch (Exception e) {
            return 0L;
        }
    }

    private void ArrayIntents(String arg) {
        ArrayIntents(arg.split("=:="));
    }

    private void ArrayIntents(String[] arg) {
        List<String> items = Arrays.asList(arg);
        String par = items.get(0);
        boolean doperformed = CustomActivity.getInstance().perform(arg,remote);
        if (doperformed) {
            return;//return if the customactivity is performed
        }
        Intent intent = new Intent();
        intent.setAction("Intent.unbi.tcpserver.TCP_MSG");

        //items.remove(0);
        intent.putExtra("tcppar", par);
//        Log.d("tcppar",par);
        List<String> tcppar = Arrays.asList(par.split(" "));
        int i = 0;
        for (String nstr : tcppar) {
            i = i + 1;
//            Log.d("tcppar"+String.valueOf(i),nstr);
            intent.putExtra("tcppar" + String.valueOf(i), nstr);
        }

        int n = 0;
        for (String str : items) {
            n = n + 1;
            if (n > 1) {
//            Log.d("tcpcomm"+String.valueOf(n-1)+"=",str);//Igot some error in removing array object so i am doing like this
                intent.putExtra("tcpcomm" + String.valueOf(n - 1), str);
            }
        }
        intent.putExtra("tcpmsg", arg);
        if (myTCPinter != null) {
            myTCPinter.sendBroadCast(intent);//this intent is for sendng to
        }
    }


}
