package jxpl.scnu.curb.homePage.immediateInformation.informationCreate;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.homePage.immediateInformation.ImmediateInformation;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * @author iri-jwj
 * @version 2
 * last update 3/26
 */
public class InformationCreateFragment extends Fragment implements InformationCreateContract.View {

    Unbinder unbinder;
    @BindView(R.id.add_info_text_title)
    TextView m_AddInfoTextTitle;
    @BindView(R.id.add_info_edit_title)
    EditText m_AddInfoEditTitle;
    @BindView(R.id.add_info_text_content)
    TextView m_AddInfoTextContent;
    @BindView(R.id.add_info_edit_content)
    EditText m_AddInfoEditContent;
    @BindView(R.id.add_info_text_time)
    TextView m_AddInfoTextTime;
    @BindView(R.id.add_info_edit_date)
    EditText m_AddInfoEditDate;
    @BindView(R.id.add_info_edit_time)
    EditText m_AddInfoEditTime;
    @BindView(R.id.add_info_text_address)
    TextView m_AddInfoTextAddress;
    @BindView(R.id.add_info_edit_address)
    EditText m_AddInfoEditAddress;
    @BindView(R.id.add_info_text_belong)
    TextView m_AddInfoTextBelong;
    @BindView(R.id.add_info_spinner_belong)
    Spinner m_AddInfoSpinnerBelong;
    @BindView(R.id.add_info_text_unit)
    TextView m_AddInfoTextUnit;
    @BindView(R.id.add_info_spinner_unit)
    Spinner m_AddInfoSpinnerUnit;
    @BindView(R.id.add_info_button_commit)
    Button m_AddInfoButtonCommit;
    @BindView(R.id.info_detail_constraintLayout)
    ConstraintLayout m_InfoDetailConstraintLayout;
    @BindView(R.id.progressBar)
    ProgressBar m_ProgressBar;
    private InformationCreateContract.Presenter mPresenter;
    private Activity m_activity;

    private String year;
    private String month;
    private String day;
    private String hours;
    private String minutes;

    public InformationCreateFragment() {
        // Required empty public constructor
    }

    public static InformationCreateFragment newInstance() {
        InformationCreateFragment lc_informationCreateFragment = new InformationCreateFragment();
        return lc_informationCreateFragment;
    }

    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_information_create, container, false);
        unbinder = ButterKnife.bind(this, view);

        m_AddInfoEditTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showTimePicker();
                }
            }
        });
        m_AddInfoEditDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    showDatePicker();
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void setPresenter(@NonNull InformationCreateContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        m_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (active) {
                    m_activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    m_activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_ProgressBar.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    m_activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    checkNotNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_ProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

    }

    @Override
    public void showPostResult(final boolean result) {
        m_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (result) {
                    Snackbar.make(checkNotNull(getView()), "发送成功", 1000).show();
                    m_activity = null;
                    Handler lc_handler = new Handler();
                    lc_handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().finish();
                        }
                    }, 1300);

                } else
                    Snackbar.make(checkNotNull(getView()), "发送失败", 1000).show();
            }
        });

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setActivity(Activity para_activity) {
        m_activity = para_activity;
    }

    /**
     * 设置用户选择的年，月，日
     *
     * @param year  年
     * @param month 月
     * @param day   日
     */
    private void setDate(int year, int month, int day) {
        this.year = year + "";
        this.month = month + "";
        this.day = day + "";
    }

    /**
     * 设置用户选择的小时、分钟
     *
     * @param hour   小时
     * @param minute 分钟
     */
    private void setTime(int hour, int minute) {
        hours = hour + "";
        minutes = minute + "";
    }

    @OnClick(R.id.add_info_edit_date)
    public void onM_AddInfoEditDateClicked() {
        showDatePicker();
    }

    @OnClick(R.id.add_info_edit_time)
    public void onM_AddInfoEditTimeClicked() {
        showTimePicker();
    }

    @OnClick(R.id.add_info_button_commit)
    public void onM_AddInfoButtonCommitClicked() {
        String title = m_AddInfoEditTitle.getText().toString();
        String time = m_AddInfoEditDate.getText().toString() + m_AddInfoEditTime.getText().toString();
        String content = m_AddInfoEditContent.getText().toString();
        String address = m_AddInfoEditAddress.getText().toString();
        String belong = (String) m_AddInfoSpinnerBelong.getSelectedItem();
        String unit = (String) m_AddInfoSpinnerUnit.getSelectedItem();
        SimpleDateFormat lc_simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",
                Locale.CHINA);
        Date lc_date = new Date(System.currentTimeMillis());
        String createTime = lc_simpleDateFormat.format(lc_date);
        Log.d("InformationCreateFrag", "onM_AddInfoButtonCommitClicked: belong:" + belong);
        mPresenter.commitInformation(new ImmediateInformation(UUID.randomUUID(), title, content,
                belong, createTime, time, address));
    }

    /**
     * 当用户点击了目标控件时，显示TimePicker
     */
    private void showTimePicker() {
        AlertDialog.Builder lc_builder = new AlertDialog.Builder(m_activity);
        lc_builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_AddInfoEditTime.setText(hours + ":" + minutes);
            }
        });
        lc_builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog lc_alertDialog = lc_builder.create();

        Calendar lc_calendar = Calendar.getInstance();
        int lc_hour = lc_calendar.get(Calendar.HOUR);
        int lc_minutes = lc_calendar.get(Calendar.MINUTE);

        TimePicker lc_timePicker = new TimePicker(m_activity);
        lc_timePicker.setElevation(3);
        lc_timePicker.setIs24HourView(true);
        if (Build.VERSION.SDK_INT > 23) {
            lc_timePicker.setHour(lc_hour);
            lc_timePicker.setMinute(lc_minutes);
        } else {
            lc_timePicker.setCurrentHour(lc_hour);
            lc_timePicker.setCurrentMinute(lc_minutes);
        }
        setTime(lc_hour, lc_minutes);
        lc_timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                setTime(hourOfDay, minute);
            }
        });
        LinearLayout lc_linearLayout = new LinearLayout(m_activity);
        LinearLayout.LayoutParams lc_layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lc_layoutParams.gravity = Gravity.CENTER;
        lc_linearLayout.setLayoutParams(lc_layoutParams);
        lc_linearLayout.setOrientation(LinearLayout.VERTICAL);
        lc_linearLayout.addView(lc_timePicker);

        lc_alertDialog.setView(lc_linearLayout);
        lc_alertDialog.show();
    }

    private void showDatePicker() {
        AlertDialog.Builder lc_builder = new AlertDialog.Builder(m_activity);
        lc_builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_AddInfoEditDate.setText(year + "年" + month + "月" + day + "日");
            }
        });
        lc_builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog lc_alertDialog = lc_builder.create();
        DatePicker lc_datePicker = new DatePicker(m_activity);
        lc_datePicker.setElevation(3);
        Calendar lc_calendar = Calendar.getInstance();
        int lc_currentYear = lc_calendar.get(Calendar.YEAR);
        int lc_currentMonth = lc_calendar.get(Calendar.MONTH) + 1;
        int lc_currentDay = lc_calendar.get(Calendar.DAY_OF_MONTH);
        setDate(lc_currentYear, lc_currentMonth, lc_currentDay);
        lc_datePicker.init(lc_currentYear, lc_currentMonth, lc_currentDay,
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        setDate(year, monthOfYear + 1, dayOfMonth);
                    }
                });

        LinearLayout lc_linearLayout = new LinearLayout(m_activity);
        LinearLayout.LayoutParams lc_layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lc_layoutParams.gravity = Gravity.CENTER;
        lc_linearLayout.setLayoutParams(lc_layoutParams);
        lc_linearLayout.setOrientation(LinearLayout.VERTICAL);
        lc_linearLayout.addView(lc_datePicker);

        lc_alertDialog.setView(lc_linearLayout);
        lc_alertDialog.show();
    }
}
