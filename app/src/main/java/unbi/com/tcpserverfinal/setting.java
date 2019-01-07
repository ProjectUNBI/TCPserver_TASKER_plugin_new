package unbi.com.tcpserverfinal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.unbi.tcpserverfinal.R;



public class setting extends AppCompatActivity implements View.OnClickListener {

    private TextView Port;
    private Switch toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.unbi.tcpserverfinal.R.layout.setting);
        Port = (TextView) findViewById(R.id.port_setting);
        Port.setText(String.valueOf(UserPrefernece.getInstance().getIpPort().getPort()));
        toast = (Switch) findViewById(R.id.toast);
        if (UserPrefernece.getInstance().isBooltoast()) {
            toast.setChecked(true);
        } else {
            toast.setChecked(false);
        }
        toast.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case com.unbi.tcpserverfinal.R.id.toast: {
                boolean on = toast.isChecked();
//                NotificationManager noti = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                noti.cancel(001);


                if (on) {
                    UserPrefernece.getInstance().setBooltoast(true);
//                    Log.d("Toast","ON");

                } else {
                    UserPrefernece.getInstance().setBooltoast(false);
//                    Log.d("Toast","OFF");
                }
                UserPrefernece.getInstance().saveinstance(getSharedPreferences(Constants.USER_SPRF, MODE_PRIVATE));
                break;
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            Log.d(this.getClass().getName(), "back button pressed");
            String content = Port.getText().toString();
            if (Integer.valueOf(content) < 65535 && Integer.valueOf(content) > 0) {
                UserPrefernece.getInstance().getIpPort().setPort(Integer.valueOf(content));
                UserPrefernece.getInstance().saveinstance(getSharedPreferences(Constants.USER_SPRF, MODE_PRIVATE));
                Toast.makeText(this, "Force Stop the App in setting to make effect", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter a valid PORT", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onKeyDown(keyCode, event);
    }


}

