package unbi.com.tcpserverfinal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.unbi.tcpserverfinal.R;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import unbi.com.tcpserverfinal.objects.Constants;
import unbi.com.tcpserverfinal.singleton.UserPrefernece;


public class setting extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TextView Port;
    private Switch toast, switch_Encrypt, switchVerification;
    private EditText editPassord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.unbi.tcpserverfinal.R.layout.setting);
        Port = (TextView) findViewById(R.id.port_setting);
        Port.setText(String.valueOf(UserPrefernece.getInstance().getIpPort().getPort()));
        toast = (Switch) findViewById(R.id.toast);
        switch_Encrypt = findViewById(R.id.switch_encrypt);
        switchVerification = findViewById(R.id.switch_verification);
        editPassord = findViewById(R.id.edit_password);
        editPassord.setText(UserPrefernece.getInstance().getPassword());
        switch_Encrypt.setChecked(UserPrefernece.getInstance().isEncryptedTransmision());
        switchVerification.setChecked(UserPrefernece.getInstance().isValidChecker());
        switch_Encrypt.setOnCheckedChangeListener(this);
        switchVerification.setOnCheckedChangeListener(this);

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
                try {
                    UserPrefernece.getInstance().setPassword(editPassord.getText().toString());
                } catch (UnsupportedEncodingException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                UserPrefernece.getInstance().saveinstance(getSharedPreferences(Constants.USER_SPRF, MODE_PRIVATE));
                Toast.makeText(this, "Force Stop the App in setting to make effect", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter a valid PORT", Toast.LENGTH_SHORT).show();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_encrypt:
                UserPrefernece.getInstance().setEncryptedTransmision(isChecked);
                break;
            case R.id.switch_verification:
                if (isChecked) {
                    Toast.makeText(this, "Please make sure the two device has same time", Toast.LENGTH_SHORT).show();
                }
                UserPrefernece.getInstance().setValidChecker(isChecked);
                break;

        }
    }
}

