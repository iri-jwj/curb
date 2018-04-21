package jxpl.scnu.curb.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.homePage.HomePageActivity;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();

            /*if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);

                //send the Registration Id to your server...

            } else*/


            if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                /*  processCustomMessage(context, bundle);*/

            } /*else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {

                //int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                *//*
             * JPushInterface.EXTRA_NOTIFICATION_TITLE 获取标题
             * JPushInterface.EXTRA_ALERT 对应 Portal 推送通知界面上的 “通知内容” 字段。
             * *//*

            } */ else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Intent i = new Intent(context, HomePageActivity.class);

                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
                Gson lc_gson = new Gson();
                Map<String, String> targetMap = lc_gson.fromJson(extras, Map.class);
                if (targetMap.get("target").equals("nav_info"))
                    i.putExtra("ItemToShow", R.id.nav_info);
                else if (targetMap.get("target").equals("nav_scholat"))
                    i.putExtra("ItemToShow", R.id.nav_scholat);

                //打开自定义的Activity
                //i.putExtras(bundle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);

            }
            //</editor-fold>
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  /*  //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        if (MainActivity.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (extraJson.length() > 0) {
                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {

                }

            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
        }
    }*/
}
