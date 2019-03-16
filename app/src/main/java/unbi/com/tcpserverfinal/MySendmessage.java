package unbi.com.tcpserverfinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.apache.commons.codec.binary.Base64;

import unbi.com.tcpserverfinal.objects.Constants;
import unbi.com.tcpserverfinal.objects.IPobject;
import unbi.com.tcpserverfinal.objects.mydoNetwork;
import unbi.com.tcpserverfinal.singleton.UserPrefernece;
import unbi.com.tcpserverfinal.utility.AES_Util;

public class MySendmessage {
    private final String TAG = "MYTCPservice";
    private IPobject ipPort;
    private String msg;
    Intent myintent;


    public MySendmessage(Intent intent) {
        if (intent != null) {
            this.myintent = intent;
            this.msg = AES_Util.encrypt((intent.getStringExtra(Constants.MESSAGE)));
            this.ipPort = new IPobject(intent.getStringExtra(Constants.ADDRESS));
        }
    }

    public void sendMsg(mydoNetwork stuff) {
        Bundle bundle = myintent.getExtras();
        if (bundle == null) {
            return;
        } else {
            Log.e(TAG, "Intent bundle is null");
        }
        switch (bundle.getInt(Constants.BUNDLE_FROM_INTENT, Constants.SENDMSG)) {
            case Constants.SEND_AUTOREM_MSG:
                this.msg = getautoremmsg();
                break;
            case Constants.SEND_MSG_ID:
                this.msg = this.msg + "=:=" + this.myintent.getStringExtra(Constants.MSG_ID);
                this.msg = doBaseEncode(this.msg);
                break;
            case Constants.SENDMSG:
                this.msg = doBaseEncode(this.msg);
                break;
        }

        //starting thread
        stuff.setIpadress(this.ipPort.getIp());
        stuff.setIpport(this.ipPort.getPort());
        stuff.setMessage(this.msg);
        new Thread(stuff).start();
    }

    private String getautoremmsg() {
        this.msg = this.myintent.getStringExtra("msgid") + "=:=" + this.msg;
        int length = this.msg.length();
        length = 327 + length;
        String newkey = "qwertyuiop[asdfghjklzxcvbnmasdfghjklxcvbnsdfghsdfgh45678sdfghxcvbsdfghasdfghsdfghxdfghedrtedfgdfghdfghxcvbdfghjdfgh45678dfghjdcvbsdfghzxcver";

        return "POST / HTTP/1.1\r\nUser-Agent: Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.215 Safari/535.1\r\nAccept: application/json,text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5\r\nContent-Type: application/json\r\nContent-Length: "
                + String.valueOf(length)
                + "\r\nHost: "+ UserPrefernece.getInstance().getInetAdress()+":1818\r\nConnection: Keep-Alive\r\n\r\n{\"communication_base_params\":{\"type\":\"Message\",\"fallback\":false,\"via\":\"Wifi\"},\"sender\":\""
                + newkey +
                "\",\"ttl\":-12114,\"collapseKey\":\"\",\"password\":\"\",\"message\":\""
                + this.msg
                + "\",\"target\":\"\",\"files\":\"\",\"version\":\"1.63\"}";
    }


    private String doBaseEncode(String msg) {
        byte[] encodedBytes = Base64.encodeBase64(msg.getBytes());
//        System.out.println("encodedBytes " + new String(encodedBytes));
//        byte[] decodedBytes = Base64.decodeBase64(encodedBytes);
//        System.out.println("decodedBytes " + new String(decodedBytes));
        return new String(encodedBytes);

    }

}

