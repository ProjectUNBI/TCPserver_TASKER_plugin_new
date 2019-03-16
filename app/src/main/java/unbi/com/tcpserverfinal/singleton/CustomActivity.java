package unbi.com.tcpserverfinal.singleton;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.net.SocketAddress;

import unbi.com.tcpserverfinal.objects.Constants;
import unbi.com.tcpserverfinal.custumtasks.CopyText;
import unbi.com.tcpserverfinal.custumtasks.FindPhone;
import unbi.com.tcpserverfinal.custumtasks.SendClipboard;
import unbi.com.tcpserverfinal.intrfce.Listener;

import static android.content.Context.MODE_PRIVATE;
import static unbi.com.tcpserverfinal.objects.Constants.USER_CUSTOM;

public class CustomActivity {
    private static final CustomActivity ourInstance = new CustomActivity();

    public static CustomActivity getInstance() {
        return ourInstance;
    }

    private CustomActivity() {
    }

    private boolean isCustomActivity;
    private FindPhone findPhone = new FindPhone();
    private CopyText copyText = new CopyText();
    private SendClipboard sendClipboard = new SendClipboard();
    private Listener.Receiver receiver;

    public void setReceiver(Listener.Receiver receiver) {
        this.receiver = receiver;
    }

    public boolean isCustomActivity() {
        return isCustomActivity;
    }

    public void setCustomActivity(boolean customActivity, Context context) {
        SaveToSpref(context);
        isCustomActivity = customActivity;
    }

    public SendClipboard getSendClipboard() {
        return sendClipboard;
    }

    public void setSendClipboard(SendClipboard sendClipboard) {
        this.sendClipboard = sendClipboard;
    }

    public FindPhone getFindPhone() {
        return findPhone;
    }

    public void setFindPhone(FindPhone findPhone, Context context) {
        this.findPhone = findPhone;
        SaveToSpref(context);
    }

    public CopyText getCopyText() {
        return copyText;
    }

    public void setCopyText(CopyText copyText, Context context) {
        this.copyText = copyText;
        SaveToSpref(context);
    }

    public void ReadFromSpref(Context context) {
        SharedPreferences spref = context.getSharedPreferences(Constants.USER_SPRF, MODE_PRIVATE);
        String copytextGson = spref.getString(USER_CUSTOM+1, null);
        String findphoneGson = spref.getString(USER_CUSTOM+2, null);
        String sendClipGson = spref.getString(USER_CUSTOM+3, null);
        Gson gson=new Gson();
        CopyText copyText=gson.fromJson(copytextGson,CopyText.class);
        FindPhone findPhone=gson.fromJson(findphoneGson,FindPhone.class);
        SendClipboard sendClipboard=gson.fromJson(sendClipGson,SendClipboard.class);

        if (copyText != null) {
            this.copyText = copyText;
        }
        if (findPhone != null) {
            this.findPhone = findPhone;
        }
        if (sendClipboard != null) {
            this.sendClipboard = sendClipboard;
        }
    }

    private void SaveToSpref(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.USER_SPRF, MODE_PRIVATE).edit();
        editor.putString(Constants.USER_CUSTOM+1, new Gson().toJson(this.copyText));
        editor.putString(Constants.USER_CUSTOM+2, new Gson().toJson(this.findPhone));
        editor.putString(Constants.USER_CUSTOM+3, new Gson().toJson(this.sendClipboard));
        editor.apply();
    }


    public boolean perform(String[] arg, SocketAddress remote) {
        if (AppEnvironment.getInstance().isTesting()) {
            if (receiver != null) {
                receiver.recieve(arg[0]);
            }
            return true;
        }
        if (!isCustomActivity()) {
            return false;
        }
        if(arg[0].equals("null")){
            return false;
        }
        if (arg[0].equals(this.findPhone.getTriggerMsg())) {
            this.findPhone.perform(arg);
            return true;
        }
        if (arg[0].equals(this.copyText.getTriggerMsg())) {
            this.copyText.perform(arg);
            return true;
        }
        if (arg[0].equals(this.sendClipboard.getTriggerMsg())) {
            this.sendClipboard.perform(arg, remote);
            return true;
        }

        return false;
    }
}
