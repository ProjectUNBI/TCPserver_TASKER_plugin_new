package unbi.com.tcpserverfinal.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.unbi.tcpserverfinal.R;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Objects;

import unbi.com.tcpserverfinal.objects.Constants;
import unbi.com.tcpserverfinal.services.MyTCPservice;
import unbi.com.tcpserverfinal.fragment.Custom_Fragment;
import unbi.com.tcpserverfinal.objects.StringPair;
import unbi.com.tcpserverfinal.singleton.CustomActivity;
import unbi.com.tcpserverfinal.singleton.UserPrefernece;

public class MainActivity extends AppCompatActivity {
    private TextView tvClientMsg, tvServerIP, tvServerPort, tvDecrypted;
    ScrollView scrolldecrypted, scrollids;
    private String Server_Name = "Unbi";
    Button clear;
    IntentFilter intentFilter = new IntentFilter();
    Switch switchmsgme,switchcustomTask;
    Custom_Fragment custom_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences spref = getSharedPreferences(Constants.USER_SPRF, MODE_PRIVATE);
        String userPrefGson = spref.getString(Constants.USER_DATA, null);
        UserPrefernece.getInstance().readfromGson(userPrefGson);
        CustomActivity.getInstance().ReadFromSpref(this);
        setContentView(R.layout.activity_main);
        scrolldecrypted = findViewById(R.id.scrollView2);
        scrollids = findViewById(R.id.scrollView3);
        tvDecrypted = findViewById(R.id.textViewClientMessagedecrypted);
        if (UserPrefernece.getInstance().isEncryptedTransmision()) {
            scrolldecrypted.setVisibility(View.VISIBLE);
        } else {
            scrolldecrypted.setVisibility(View.GONE);
        }
        if (UserPrefernece.getInstance().isValidChecker()) {
            scrollids.setVisibility(View.VISIBLE);
        } else {
            scrollids.setVisibility(View.GONE);
        }
        switchmsgme = (Switch) findViewById(R.id.switchmsg);
        switchcustomTask = (Switch) findViewById(R.id.switchcustom);
        switchcustomTask.setChecked(CustomActivity.getInstance().isCustomActivity());
        switchmsgme.setChecked(UserPrefernece.getInstance().isBoolshowmsg());
        switchcustomTask.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchcustomTask.isChecked()) {
                    CustomActivity.getInstance().setCustomActivity(true,getApplicationContext());
                } else {
                    CustomActivity.getInstance().setCustomActivity(false,getApplicationContext());
                }
            }
        });
        switchmsgme.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchmsgme.isChecked()) {
                    UserPrefernece.getInstance().setBoolshowmsg(true);
                    UserPrefernece.getInstance().saveinstance(getSharedPreferences(Constants.USER_SPRF, MODE_PRIVATE));
                } else {
                    UserPrefernece.getInstance().setBoolshowmsg(false);
                    UserPrefernece.getInstance().saveinstance(getSharedPreferences(Constants.USER_SPRF, MODE_PRIVATE));
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
                tvDecrypted.setText("");
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
                        UserPrefernece.getInstance().setInetAdress(inetAddress.getHostAddress());

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
        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(getBaseContext(), setting.class);
                startActivity(intent);
                return true;
            case R.id.action_custotask:
                //todo start fragment  customtask
                FragmentManager fragmentManager=getSupportFragmentManager();
                fragmentManager.popBackStack();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                custom_fragment=new Custom_Fragment();
                CustomActivity.getInstance().setReceiver(custom_fragment);
                fragmentTransaction.add(R.id.activityfrag,custom_fragment,"custom");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        registerReceiver(broadcastReceiver, intentFilter);
        if (UserPrefernece.getInstance().isEncryptedTransmision()) {
            scrolldecrypted.setVisibility(View.VISIBLE);
        } else {
            scrolldecrypted.setVisibility(View.GONE);
        }
        if (UserPrefernece.getInstance().isValidChecker()) {
            scrollids.setVisibility(View.VISIBLE);
        } else {
            scrollids.setVisibility(View.GONE);
        }
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
            if (Objects.requireNonNull(intent.getAction()).equals("SERVICE")) {
                //HERE YOU WILL GET VALUES FROM BROADCAST THROUGH INTENT EDIT YOUR TEXTVIEW///////////
                String receivedValue = intent.getStringExtra("MSG");
                if (UserPrefernece.getInstance().isBoolshowmsg() && !UserPrefernece.getInstance().isEncryptedTransmision()) {
                    tvClientMsg.append(receivedValue + "\n");
                } else {
                    StringPair stringPair = new Gson().fromJson(receivedValue, StringPair.class);
                    if (stringPair == null) {
                        return;
                    }
                    tvClientMsg.append(stringPair.getRaw() + "\n");
                    tvDecrypted.append(stringPair.getDecrypted() + "\n");
                    ((TextView) findViewById(R.id.textViewClientMessesVerificID)).setText(
                            new Gson().toJson(UserPrefernece.getInstance().getDonedIds())
                    );
                }
            }
        }
    };

}