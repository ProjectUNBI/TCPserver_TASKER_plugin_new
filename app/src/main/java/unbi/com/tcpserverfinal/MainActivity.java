package unbi.com.tcpserverfinal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.unbi.tcpserverfinal.R;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {
    private TextView tvClientMsg, tvServerIP, tvServerPort;
    private String Server_Name = "Unbi";
    Button clear;
    IntentFilter intentFilter = new IntentFilter();
    Switch switchmsgme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences spref = getSharedPreferences(Constants.USER_SPRF, MODE_PRIVATE);
        String userPrefGson=spref.getString(Constants.USER_DATA, null);
        UserPrefernece.getInstance().readfromGson(userPrefGson);
        setContentView(R.layout.activity_main);
        switchmsgme = (Switch) findViewById(R.id.switchmsg);
        if (UserPrefernece.getInstance().isBoolshowmsg()) {
            switchmsgme.setChecked(true);
        } else {
            switchmsgme.setChecked(false);
        }
        switchmsgme.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchmsgme.isChecked()) {
                    UserPrefernece.getInstance().setBoolshowmsg(true);
                } else {
                    UserPrefernece.getInstance().setBoolshowmsg(false);
                }
            }
        });
        tvClientMsg = (TextView) findViewById(R.id.textViewClientMessage);
        tvServerIP = (TextView) findViewById(R.id.textViewServerIP);
        tvServerPort = (TextView) findViewById(R.id.textViewServerPort);
        tvServerPort.setText(Integer.toString(UserPrefernece.getInstance().getIpPort().getPort()));
        getDeviceIpAddress();
        clear = (Button) findViewById(R.id.button1);
        clear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                tvClientMsg.setText("");
            }
        });
        startService(new Intent(this, MyTCPservice.class));
        intentFilter.addAction("SERVICE");
    }
    /**
     * Get ip address of the device
     */
    public void getDeviceIpAddress() {
        try {
            for (Enumeration<NetworkInterface> enumeration = NetworkInterface
                    .getNetworkInterfaces(); enumeration.hasMoreElements(); ) {
                NetworkInterface networkInterface = enumeration.nextElement();
                for (Enumeration<InetAddress> enumerationIpAddr = networkInterface
                        .getInetAddresses(); enumerationIpAddr
                             .hasMoreElements(); ) {
                    InetAddress inetAddress = enumerationIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress.getAddress().length == 4) {
                        tvServerIP.setText(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("ERROR:", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
//            Log.d("LOG HERE","SETTING");
            Intent intent = new Intent(getBaseContext(), setting.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        registerReceiver(broadcastReceiver, intentFilter);
        if (UserPrefernece.getInstance().isBoolshowmsg()) {
            switchmsgme.setChecked(true);
        } else {
            switchmsgme.setChecked(false);
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        /** Receives the broadcast that has been fired */
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == "SERVICE") {
                //HERE YOU WILL GET VALUES FROM BROADCAST THROUGH INTENT EDIT YOUR TEXTVIEW///////////
                String receivedValue = intent.getStringExtra("MSG");
                if (UserPrefernece.getInstance().isBoolshowmsg())
                    {tvClientMsg.append(receivedValue + "\n");}
            }
        }
    };

}