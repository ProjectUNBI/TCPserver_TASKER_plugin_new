package unbi.com.tcpserverfinal.singleton;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import unbi.com.tcpserverfinal.objects.Constants;
import unbi.com.tcpserverfinal.objects.IPobject;
import unbi.com.tcpserverfinal.utility.PasswordUtil;

public class UserPrefernece {
    private static UserPrefernece ourInstance = new UserPrefernece();

    public static UserPrefernece getInstance() {
        return ourInstance;
    }


    private IPobject ipPort=new IPobject();
    private boolean booltoast;
    private boolean boolshowmsg;
    private String message="";
    private String inetAdress="127.0.0.1";
    private byte[] mykeybyte;
    private String password;
    private boolean validChecker;
    private boolean encryptedTransmision;
    private ArrayList<String>DonedIds=new ArrayList<>();
    private UserPrefernece() {

    }

    public void readfromGson(String userPrefGson) {
        if (userPrefGson == null) {
            this.ipPort = new IPobject();
            booltoast = true;
            boolshowmsg = true;
            validChecker=false;
            encryptedTransmision=false;
            try {
                setPassword(PasswordUtil.randomPassword());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
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

    public String getInetAdress() {
        return inetAdress;
    }

    public void setInetAdress(String inetAdress) {
        this.inetAdress = inetAdress;
    }


    public boolean isValidChecker() {
        return validChecker;
    }

    public void setValidChecker(boolean validChecker) {
        this.validChecker = validChecker;
    }

    public boolean isEncryptedTransmision() {
        return encryptedTransmision;
    }

    public void setEncryptedTransmision(boolean encryptedTransmision) {
        this.encryptedTransmision = encryptedTransmision;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if(password==null||password.equals("")){return;}
        byte[] key = (password).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        this.mykeybyte= Arrays.copyOf(key, 16);

        this.password = password;
    }

    public byte[] getMykeybyte() {
        return mykeybyte;
    }

    public ArrayList<String> getDonedIds() {
        return DonedIds;
    }

    public void setDonedIds(ArrayList<String> donedIds) {
        DonedIds = donedIds;
    }
}
