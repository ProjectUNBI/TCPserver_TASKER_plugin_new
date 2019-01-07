package unbi.com.tcpserverfinal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Constants.BOOTCOMPLETE.equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, MyTCPservice.class);
            context.startService(pushIntent);
        }
        else if (Constants.INTENT_TCP_SENDMSG.equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, MyTCPservice.class);
            Bundle bundle = intent.getExtras();
            Bundle newbundle=new Bundle();
            newbundle.putInt("putInt#@###", Constants.SENDMSG);
            pushIntent.putExtras(newbundle);
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Object value = bundle.get(key);
                    pushIntent.putExtra(key,value.toString());

                }
            }
            context.startService(pushIntent);
        }
        else if(Constants.INTENT_TCP_AUTOREM.equals(intent.getAction())){
            Intent pushIntent = new Intent(context, MyTCPservice.class);
            Bundle bundle = intent.getExtras();
            Bundle newbundle=new Bundle();
            newbundle.putInt("putInt#@###", Constants.SEND_AUTOREM_MSG);
            pushIntent.putExtras(newbundle);
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Object value = bundle.get(key);
//                    Log.d("TAG EXTRA", String.format("%s %s (%s)", key,
//                            value.toString(), value.getClass().getName()));
                    pushIntent.putExtra(key,value.toString());

                }
            }
            context.startService(pushIntent);
        }
        else if(Constants.SEND_MSG_WITH_ID.equals(intent.getAction())){
            Intent pushIntent = new Intent(context, MyTCPservice.class);
            Bundle bundle = intent.getExtras();
            Bundle newbundle=new Bundle();
            newbundle.putInt("putInt#@###", Constants.SEND_MSG_ID);
            pushIntent.putExtras(newbundle);
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Object value = bundle.get(key);
//                    Log.d("TAG EXTRA", String.format("%s %s (%s)", key,
//                            value.toString(), value.getClass().getName()));
                    pushIntent.putExtra(key,value.toString());

                }
            }
            context.startService(pushIntent);
        }


    }
}

//VAriable are %address,%msg,%msgid