package unbi.com.tcpserverfinal;

import android.content.SharedPreferences;

import com.google.gson.Gson;

public class UserPrefernece {
    private static UserPrefernece ourInstance = new UserPrefernece();

    public static UserPrefernece getInstance() {
        return ourInstance;
    }


    private IPobject ipPort;
    private boolean booltoast;
    private boolean boolshowmsg;
    private String message;

    private UserPrefernece() {

    }

    public void readfromGson(String userPrefGson) {
        if (userPrefGson == null) {
            this.ipPort = new IPobject();
            booltoast = true;
            boolshowmsg = true;
        } else {
            ourInstance = new Gson().fromJson(userPrefGson, UserPrefernece.class);
        }
    }

    public IPobject getIpPort() {
        return ipPort;
    }

    public boolean isBooltoast() {
        return booltoast;
    }

    public boolean isBoolshowmsg() {
        return boolshowmsg;
    }

    public void setIpPort(IPobject ipPort) {
        this.ipPort = ipPort;
    }

    public void setBooltoast(boolean booltoast) {
        this.booltoast = booltoast;
    }

    public void setBoolshowmsg(boolean boolshowmsg) {
        this.boolshowmsg = boolshowmsg;
    }

    public void saveinstance(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.USER_DATA, new Gson().toJson(this));
        editor.apply();
    }

    public void setMsg(String s) {
        this.message=s;
    }
    public String getMsg(){
        return this.message;
    }
}
