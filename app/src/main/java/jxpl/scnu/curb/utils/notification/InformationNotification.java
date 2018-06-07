package jxpl.scnu.curb.utils.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import jxpl.scnu.curb.R;
import jxpl.scnu.curb.homePage.HomePageActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class InformationNotification {

    private static final String NOTIFICATION_TAG = "Information";

    public static void notify(final Context context,
                              final NotificationCompat.Builder para_builder) {
        // This image is used as the notification's large icon (thumbnail).
        //final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.curblogo);

        Intent lc_intent = new Intent(context, HomePageActivity.class);
        lc_intent.putExtra("ItemToShow", R.id.nav_info);

        para_builder
                // Set appropriate defaults for the notification light, sound,
                // and vibration.
                .setDefaults(Notification.DEFAULT_ALL)

                // Set required fields, including the small icon, the
                // notification title, and text.
                .setSmallIcon(R.drawable.curblogo)

                // All fields below this line are optional.

                // Use a default priority (recognized on devices running Android
                // 4.1 or later)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                // Provide a large icon, shown with the notification in the
                // notification drawer on devices running Android 3.0 or later.
                //.setLargeIcon(picture)

                // Set ticker text (preview) information for this notification.

                // Show a number. This is useful when stacking notifications of
                // a single type.
                //.setNumber(number)

                // If this notification relates to a past or upcoming event, you
                // should set the relevant time information using the setWhen
                // method below. If this call is omitted, the notification's
                // timestamp will by set to the time at which it was shown.
                // TODO: Call setWhen if this notification relates to a past or
                // upcoming event. The sole argument to this method should be
                // the notification timestamp in milliseconds.
                //.setWhen(...)

                // Set the pending intent to be initiated when the user touches
                // the notification.
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                11,
                                lc_intent,
                                PendingIntent.FLAG_UPDATE_CURRENT))

                // Example additional actions for this notification. These will
                // only show on devices running Android 4.1 or later, so you
                // should ensure that the activity in this notification's
                // content intent provides access to the same actions in
                // another way.
                /*  .addAction(
                          R.drawable.ic_action_stat_share,
                          res.getString(R.string.action_share),
                          PendingIntent.getActivity(
                                  context,
                                  0,
                                  Intent.createChooser(new Intent(Intent.ACTION_SEND)
                                          .setType("text/plain")
                                          .putExtra(Intent.EXTRA_TEXT, "Dummy text"), "Dummy title"),
                                  PendingIntent.FLAG_UPDATE_CURRENT))*/
                .setAutoCancel(true);

        notify(context, para_builder.build());
    }

    private static void notify(@NonNull final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        checkNotNull(nm).notify(NOTIFICATION_TAG, 0, notification);
    }

    /**
     * Cancels any notifications of this type previously shown using
     * {@link #notify(Context, String, int)}.
     */
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        checkNotNull(nm).cancel(NOTIFICATION_TAG, 0);
    }
}
