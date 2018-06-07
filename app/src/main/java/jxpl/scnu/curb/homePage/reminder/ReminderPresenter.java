package jxpl.scnu.curb.homePage.reminder;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * created on 2018/6/4
 *
 * @author iri-jwj
 * @version 1 init
 */
public class ReminderPresenter implements ReminderContract.Presenter {
    private static String CALENDARS_NAME = "curb_events";
    private static String CALENDARS_ACCOUNT_NAME = "curb";
    private static String CALENDARS_ACCOUNT_TYPE = "scnu.curb";
    private static String CALENDARS_DISPLAY_NAME = "curb_events";
    private final Activity m_activity;
    private ReminderContract.View m_view;

    public ReminderPresenter(ReminderContract.View para_view, Activity para_activity) {
        m_view = para_view;
        m_view.setPresenter(this);
        m_activity = para_activity;

        if (ContextCompat.checkSelfPermission(m_activity, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(m_activity, new String[]{Manifest.permission.READ_CALENDAR}, 1);

        if (ContextCompat.checkSelfPermission(m_activity, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(m_activity, new String[]{Manifest.permission.WRITE_CALENDAR}, 2);
    }

    private static int checkCalendarAccount(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED)
            return -1;
        String selection = CalendarContract.Calendars.ACCOUNT_NAME + " like ? and " + CalendarContract.Calendars.ACCOUNT_TYPE + " like ? ";
        String[] selectionArgs = {CALENDARS_ACCOUNT_NAME, CALENDARS_ACCOUNT_TYPE};
        Cursor userCursor = context.getContentResolver().query(CalendarContract.Calendars.CONTENT_URI, new String[]{CalendarContract.Calendars._ID},
                selection, selectionArgs, null);
        try {
            if (userCursor == null)//查询返回空值
                return -1;
            int count = userCursor.getCount();
            if (count > 0) {//存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }

    private static long addCalendarAccount(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED)
            return -1;
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME);

        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = CalendarContract.Calendars.CONTENT_URI;
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
                .build();

        Uri result = context.getContentResolver().insert(calendarUri, value);
        return result == null ? -1 : ContentUris.parseId(result);
    }

    private static int checkAndAddCalendarAccount(Context context) {
        int oldId = checkCalendarAccount(context);
        if (oldId >= 0) {
            return oldId;
        } else {
            long addId = addCalendarAccount(context);
            if (addId >= 0) {
                return checkCalendarAccount(context);
            } else {
                return -1;
            }
        }
    }

    public static void addCalendarEvent(Context context, String title, String description, long beginTime) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // 获取日历账户的id
        int calId = checkAndAddCalendarAccount(context);
        if (calId < 0) {
            return;
        }

        ContentValues event = new ContentValues();
        event.put("title", title);
        event.put("description", description);
        // 插入账户的id
        event.put("calendar_id", calId);

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(beginTime);//设置开始时间
        long start = mCalendar.getTime().getTime();
        mCalendar.setTimeInMillis(start + 3600000);//设置终止时间
        long end = mCalendar.getTime().getTime();

        event.put(CalendarContract.Events.DTSTART, start);
        event.put(CalendarContract.Events.DTEND, end);
        event.put(CalendarContract.Events.HAS_ALARM, 1);//设置有闹钟提醒
        event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");  //这个是时区，必须有，
        //添加事件
        Uri newEvent = context.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, event);
        if (newEvent == null) {
            // 添加日历事件失败直接返回
            return;
        }
        //事件提醒的设定
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent));
        // 提前10分钟有提醒
        values.put(CalendarContract.Reminders.MINUTES, 15);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        Uri uri = context.getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, values);
        if (uri == null) {
            // 添加闹钟提醒失败直接返回
            return;
        }
    }

    public static void deleteCalendarEvent(Context context, String title) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED)
            return;

        Cursor eventCursor = context.getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);
        try {
            if (eventCursor == null)//查询返回空值
                return;
            if (eventCursor.getCount() > 0) {
                //遍历所有事件，找到title跟需要查询的title一样的项
                for (eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext()) {
                    String eventTitle = eventCursor.getString(eventCursor.getColumnIndex("title"));
                    if (!TextUtils.isEmpty(title) && title.equals(eventTitle)) {
                        int id = eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID));//取得id
                        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id);
                        int rows = context.getContentResolver().delete(deleteUri, null, null);
                        if (rows == -1) {
                            //事件删除失败
                            return;
                        }
                    }
                }
            }
        } finally {
            if (eventCursor != null) {
                eventCursor.close();
            }
        }
    }

    @Override
    public void start() {
        List<Reminder> lc_reminders = getRemindersFromCalendar();
        if (lc_reminders != null)
            m_view.showReminders(lc_reminders);
        else
            m_view.showNoReminder();
    }

    @Override
    public void deleteReminder(String title) {
        deleteCalendarEvent(m_activity, title);
    }

    @Nullable
    private List<Reminder> getRemindersFromCalendar() {
        int _id = checkAndAddCalendarAccount(m_activity);
        if (_id < 0) {
            m_view.showError("日历账户出错");
        }
        List<Reminder> lc_reminders = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(m_activity, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            m_view.showError("未获取日历读写权限");
            return null;
        }
        ContentResolver lc_contentResolver = m_activity.getContentResolver();
        String projection[] = {
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.EVENT_LOCATION,
                CalendarContract.Events.DESCRIPTION
        };
        String selection = CalendarContract.Events._ID + " like ?";
        String[] selectionArgs = {_id + ""};
        String orderby = CalendarContract.Events.DTSTART + " DESC";
        Cursor lc_cursor = lc_contentResolver.query(CalendarContract.Events.CONTENT_URI, projection, selection, selectionArgs, orderby);
        if (lc_cursor != null && lc_cursor.getCount() != 0) {
            while (lc_cursor.moveToNext()) {
                String id = lc_cursor.getString(lc_cursor.getColumnIndex(CalendarContract.Events._ID));
                String title = lc_cursor.getString(lc_cursor.getColumnIndex(CalendarContract.Events.TITLE));
                String time = lc_cursor.getString(lc_cursor.getColumnIndex(CalendarContract.Events.DTSTART));
                Log.d("presenter", "getRemindersFromCalendar: "+time);
                String address = lc_cursor.getString(lc_cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION));
                String content = lc_cursor.getString(lc_cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION));
                lc_reminders.add(new Reminder(id, time, address, content, title));
            }
        }
        if (lc_cursor != null)
            lc_cursor.close();
        return lc_reminders.size() == 0 ? null : lc_reminders;
    }
}
