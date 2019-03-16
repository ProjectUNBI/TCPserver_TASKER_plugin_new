package unbi.com.tcpserverfinal.utility;

import java.util.Random;
import java.util.UUID;

public class Util {
    private static String getRandomUUID(String str){
       return UUID.nameUUIDFromBytes(str.getBytes()).toString();
    }

    public static String getRandomId(){
        return System.currentTimeMillis()+String.format("%04d", (new Random()).nextInt(10000));
    }
}
