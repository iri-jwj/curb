package jxpl.scnu.curb.utils.notification;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;

import java.util.LinkedList;

import jxpl.scnu.curb.R;

import static com.google.common.base.Preconditions.checkNotNull;

@TargetApi(26)
public class NotificationChannelManage {
    private NotificationChannelGroup group;
    private LinkedList<NotificationChannel> m_channels;
    private volatile static NotificationChannelManage instance = null;

    private NotificationChannelManage(Context para_context) {
        CharSequence lc_charSequence = "curbNotification";
        group = new NotificationChannelGroup("jxpl.curb", lc_charSequence);
        m_channels = new LinkedList<>();
        initNotificationChannel(para_context);
    }

    public synchronized static NotificationChannelManage getInstance(Context para_context) {
        if (instance == null)
            instance = new NotificationChannelManage(para_context);
        return instance;
    }

    public synchronized NotificationChannelGroup getGroup() {
        return group;
    }

    private void initNotificationChannel(Context para_context) {
        String[] id = {para_context.getString(R.string.notification_info_channel_id),
                para_context.getString(R.string.notification_scholat_channel_id)};
        CharSequence[] name = {para_context.getString(R.string.notification_info_channel_name),
                para_context.getString(R.string.notification_scholat_channel_name)};

        String[] description = {para_context.getString(R.string.notification_info_channel_description),
                para_context.getString(R.string.notification_scholat_channel_description)};
        for (int i = 0; i < 2; i++) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(id[i], name[i], importance);
            channel.setDescription(description[i]);
            // Register the channel with the system
            NotificationManager notificationManager = (NotificationManager) para_context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            //设置图标小红点是否显示 和 颜色
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            //设置该Channel所属于的group
            channel.setGroup(group.getId());
            //长按图标时显示此渠道的通知
            channel.setShowBadge(true);
            checkNotNull(notificationManager).createNotificationChannel(channel);
            m_channels.add(channel);
        }
    }

    public LinkedList<NotificationChannel> getChannels() {
        return m_channels;
    }
}
