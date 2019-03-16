package unbi.com.tcpserverfinal.custumtasks;

import android.util.Log;

import java.net.SocketAddress;

public class SendClipboard extends BaseTask{
    public SendClipboard() {
        this.TaskName="Send Clipboard Text";
    }

    public void perform(String[] arg, SocketAddress remote) {
        //todo send clipboard
        Log.d("TASK","SENDCLIP");

    }
}
