package unbi.com.tcpserverfinal.custumtasks;

import android.util.Base64;
import android.util.Log;

public class CopyText extends BaseTask {
    public CopyText() {
        this.TaskName="Copy to Clipboard";
    }

    public void perform(String[] arg) {
        //todo perform copy to clipboard
        Log.d("TASK","COPYTEXT");
    }
}
