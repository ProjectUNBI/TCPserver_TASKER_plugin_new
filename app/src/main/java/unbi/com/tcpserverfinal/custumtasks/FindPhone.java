package unbi.com.tcpserverfinal.custumtasks;

import android.util.Log;

public class FindPhone extends BaseTask {
    private String Ringtone;
    private boolean flashlight;

    public FindPhone() {
        this.TaskName="Find Phone";
    }

    public String getRingtone() {
        return Ringtone;
    }

    public void setRingtone(String ringtone) {
        Ringtone = ringtone;
    }


    public boolean isFlashlight() {
        return flashlight;
    }

    public void setFlashlight(boolean flashlight) {
        this.flashlight = flashlight;
    }


    public void perform(String[] arg) {
        //todo vibrate the phone
        Log.d("TASK","FINDPHONe");

    }
}
