package unbi.com.tcpserverfinal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class mydoNetwork implements Runnable {
    public PrintWriter out;
    public BufferedReader in;
    private int ipport = 8080;
    private String ipadress = "192.168.1.1";
    private String message = "null";


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("handleMessage", (msg.getData().getString("msg")));
        }

    };

    public void mkmsg(String str) {
        //handler junk, because thread can't update screen!
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("msg", str);
        msg.setData(b);
        handler.sendMessage(msg);
    }



    public void run() {


        mkmsg("host is " + ipadress + "\n");
        mkmsg(" Port is " + ipport + "\n");
        try {
            InetAddress serverAddr = InetAddress.getByName(ipadress);
            mkmsg("Attempt Connecting..." + ipadress + "\n");
            Socket socket = new Socket(serverAddr, ipport);


            //made connection, setup the read (in) and write (out)
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //now send a message to the server and then read back the response.
            try {
                //write a message to the server
                mkmsg("Attempting to send message ...\n");
                out.println(message);
                mkmsg("Message sent...\n");

                //read back a message from the server.
                mkmsg("Attempting to receive a message ...\n");
                String str = in.readLine();
                mkmsg("received a message:\n" + str + "\n");

                mkmsg("We are done, closing connection\n");
                out.flush();
            } catch (Exception e) {
                mkmsg("Error happened sending/receiving\n");

            } finally {
                in.close();
                out.close();
                socket.close();
            }

        } catch (Exception e) {
            mkmsg("Unable to connect...\n");
        }
    }


    public void setIpport(int ipport) {
        this.ipport = ipport;
    }


    public void setIpadress(String ipadress) {
        this.ipadress = ipadress;
    }


    public void setMessage(String message) {
        this.message = message;
    }
}
