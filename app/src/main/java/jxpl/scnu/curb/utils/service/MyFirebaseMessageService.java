package jxpl.scnu.curb.utils.service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.LinkedList;
import java.util.Map;

import jxpl.scnu.curb.R;
import jxpl.scnu.curb.utils.notification.InformationNotification;
import jxpl.scnu.curb.utils.notification.NotificationChannelManage;
import jxpl.scnu.curb.utils.notification.ScholatNotification;

import static com.google.common.base.Preconditions.checkNotNull;

public class MyFirebaseMessageService extends FirebaseMessagingService {
    public MyFirebaseMessageService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage para_remoteMessage) {
        super.onMessageReceived(para_remoteMessage);
        int chooseInfoOrScholat = 0;
        if (para_remoteMessage.getData().size() > 0) {
            Map lc_dataMap = para_remoteMessage.getData();
            String lc_target = (String) lc_dataMap.get("target");
            String[] lc_targetSplited = lc_target.split("-");
            if (lc_targetSplited[2].equals("nav_info"))
                chooseInfoOrScholat = 1;
            else
                chooseInfoOrScholat = 2;
        }
        if (para_remoteMessage.getNotification() != null) {
            sendNotification(para_remoteMessage.getNotification(), chooseInfoOrScholat);
        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String para_s) {
        super.onMessageSent(para_s);
    }

    @Override
    public void onSendError(String para_s, Exception para_e) {
        super.onSendError(para_s, para_e);
    }

    private void sendNotification(RemoteMessage.Notification para_notification, int chooseInfoOrScholat) {
        String[] channelsId = {getResources().getString(R.string.notification_info_channel_id),
                getResources().getString(R.string.notification_scholat_channel_id)};
        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannelManage lc_notificationChannelManage = NotificationChannelManage.getInstance(this);
            LinkedList<NotificationChannel> lc_channels = lc_notificationChannelManage.getChannels();
            if (lc_channels.get(1).getId().equals(channelsId[chooseInfoOrScholat])) {
                builder = new NotificationCompat.Builder(this, lc_channels.get(1).getId());
            } else
                builder = new NotificationCompat.Builder(this, lc_channels.get(2).getId());
        } else {
            builder = new NotificationCompat.Builder(this);
        }

        checkNotNull(builder)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(para_notification.getTitle())
                .setContentText(para_notification.getBody());

        if (chooseInfoOrScholat == 1)
            InformationNotification.notify(this, builder);
        else
            ScholatNotification.notify(this, builder);
    }
}
