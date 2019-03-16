package unbi.com.tcpserverfinal.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unbi.tcpserverfinal.R;

import unbi.com.tcpserverfinal.custumtasks.BaseTask;
import unbi.com.tcpserverfinal.intrfce.Listener;
import unbi.com.tcpserverfinal.singleton.AppEnvironment;
import unbi.com.tcpserverfinal.singleton.CustomActivity;

public class Custom_Fragment extends Fragment implements View.OnClickListener, Listener.Receiver {

    CardView card_findphone, card_copytext, card_sendclip;
    TextView tv_findphone_trig, tv_copytopClip_trig, tv_sendclip_trig;
    AlertDialog alertDialog;
    TextView msg;
    String receive;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_customtask, container, false);
        card_copytext = view.findViewById(R.id.card_copytext);
        card_sendclip = view.findViewById(R.id.card_sendclip);
        card_findphone = view.findViewById(R.id.card_findPhoneTrig);
        card_findphone.setOnClickListener(this);
        card_sendclip.setOnClickListener(this);
        card_copytext.setOnClickListener(this);
        tv_copytopClip_trig = view.findViewById(R.id.text_copytext_trig);
        tv_sendclip_trig = view.findViewById(R.id.text_sendclip_trig);
        tv_findphone_trig = view.findViewById(R.id.text_findphone_trig);
        updateview();
        return view;
    }

    private void updateview() {
        CustomActivity customActivity = CustomActivity.getInstance();
        if (customActivity.getFindPhone().getTriggerMsg() != null) {
            tv_findphone_trig.setText(customActivity.getFindPhone().getTriggerMsg());
        } else {
            tv_findphone_trig.setText("null");
        }
        if (customActivity.getCopyText().getTriggerMsg() != null) {
            tv_copytopClip_trig.setText(customActivity.getCopyText().getTriggerMsg());
        } else {
            tv_copytopClip_trig.setText("null");
        }
        if (customActivity.getSendClipboard().getTriggerMsg() != null) {
            tv_sendclip_trig.setText(customActivity.getSendClipboard().getTriggerMsg());
        } else {
            tv_sendclip_trig.setText("null");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_copytext:
                openDialog(CustomActivity.getInstance().getCopyText());
                break;
            case R.id.card_sendclip:
                openDialog(CustomActivity.getInstance().getSendClipboard());
                break;
            case R.id.card_findPhoneTrig:
                openDialog(CustomActivity.getInstance().getFindPhone());
                break;
        }
    }

    public void openDialog(final BaseTask baseTask) {
        AppEnvironment.getInstance().setTesting(true);
        alertDialog = new AlertDialog.Builder(getContext()).create();

        // Set Custom Title
        TextView title = new TextView(getContext());
        // Title Properties
        title.setText(baseTask.getTaskName());
        title.setPadding(10, 10, 10, 10);   // Set Position
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);

        // Set Message
        msg = new TextView(getContext());
        // Message Properties
        msg.setText("Waiting for a message.... \n Please send msg from other device.");
        msg.setGravity(Gravity.CENTER_HORIZONTAL);
        msg.setTextColor(Color.BLACK);
        alertDialog.setView(msg);

        // Set Button
        // you can more buttons
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
                AppEnvironment.getInstance().setTesting(false);
                baseTask.setActive(true);
                baseTask.setTriggerMsg(receive);
                alertDialog.dismiss();
                updateview();

            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Clear", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
                //Clear the previous.. i means active to null
                AppEnvironment.getInstance().setTesting(false);
                baseTask.setTriggerMsg("null");
                baseTask.setActive(false);
                alertDialog.dismiss();
                updateview();

            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AppEnvironment.getInstance().setTesting(false);
                alertDialog.dismiss();
                updateview();

                // Perform Action on Button
            }
        });

        new Dialog(getContext());
        alertDialog.show();

        // Set Properties for OK Button
        final Button okBT = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams neutralBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        neutralBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        okBT.setPadding(50, 10, 10, 10);   // Set Position
        okBT.setTextColor(Color.BLUE);
        okBT.setLayoutParams(neutralBtnLP);

        final Button clearBT = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams posiBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        posiBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        okBT.setPadding(50, 10, 10, 10);   // Set Position
        okBT.setTextColor(Color.GREEN);
        okBT.setLayoutParams(neutralBtnLP);


        final Button cancelBT = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        negBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        cancelBT.setTextColor(Color.RED);
        cancelBT.setLayoutParams(negBtnLP);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppEnvironment.getInstance().setTesting(false);
    }

    @Override
    public Object recieve(Object o) {
        receive = (String) o;
        if (receive == null) {
            return null;
        }
        msg.setText(receive);
        return null;
        //todo Listener
    }
}
