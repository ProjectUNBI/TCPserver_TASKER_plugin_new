package unbi.com.tcpserverfinal.utility;


import android.util.Base64;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import unbi.com.tcpserverfinal.singleton.UserPrefernece;

import static java.util.Arrays.copyOfRange;
import static unbi.com.tcpserverfinal.utility.Util.getRandomId;

public class AES_Util {
    public static String encrypt(String stringExtra) {
        if(!UserPrefernece.getInstance().isEncryptedTransmision()){
            return stringExtra;
        }
        if(UserPrefernece.getInstance().isValidChecker()){
            stringExtra=stringExtra+"=:="+getRandomId();
        }
        return encrypt(UserPrefernece.getInstance().getMykeybyte(),stringExtra);
    }

    public  static String decrypt(String string){
        return decrypt(UserPrefernece.getInstance().getMykeybyte(),string);
    }


    private static String encrypt(byte[] key, String value) {
        try {
            SecureRandom randomSecureRandom = new SecureRandom();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            byte[] iv_str = new byte[cipher.getBlockSize()];
            randomSecureRandom.nextBytes(iv_str);
            iv_str=randomSecureRandom.generateSeed(16);
            IvParameterSpec iv = new IvParameterSpec(iv_str);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            byte[] combined = new byte[iv_str.length + encrypted.length];

            for (int i = 0; i < combined.length; ++i)
            {

                combined[i] = i < iv_str.length ? iv_str[i] : encrypted[i - iv_str.length];
            }
            return Base64.encodeToString(combined, Base64.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static String decrypt(byte[] key, String encrypted) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            byte[] original = Base64.decode(encrypted,Base64.DEFAULT);
            byte[] ivbyte=copyOfRange(original,0,16);
            byte[] msgbytes=copyOfRange(original,16,original.length);
            IvParameterSpec iv = new IvParameterSpec(ivbyte);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] decrypted = cipher.doFinal(msgbytes);
            String s=new String(decrypted);
            return new String(decrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }


}
