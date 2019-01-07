package unbi.com.tcpserverfinal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;


/**
 * AsyncTask which handles the commiunication with clients
 */
public class ServerAsyncTask extends AsyncTask<Socket, Void, String> {
    MyTCPinterface myTCPinter;

    public ServerAsyncTask(MyTCPinterface myTCPinterface) {
        this.myTCPinter = myTCPinterface;
    }

    @Override
    protected String doInBackground(Socket... params) {
//            Log.d("LOG HERE","SERVER CONNECTED");
        //TODO Here is the server coonected
        String result = null;
        Socket mySocket = params[0];
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
            UserPrefernece.getInstance().setMsg(s);
        } else {
            s = "null";
            UserPrefernece.getInstance().setMsg("null");
        }
        ArrayIntents(s);
        new Thread() {
            public void run() {
                Object result = null;
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
                            myTCPinter.sendBroadCast(i);//this intent is for broadcarting in the MaonActivity
                            myTCPinter.Toast(UserPrefernece.getInstance().getMsg());
                        }


                    }
                });
            }

            ;
        }.start();


    }

    private void ArrayIntents(String arg) {
        List<String> items = Arrays.asList(UserPrefernece.getInstance().getMsg().split("=:="));
        Intent intent = new Intent();
        intent.setAction("Intent.unbi.tcpserver.TCP_MSG");

        String par = items.get(0);
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
