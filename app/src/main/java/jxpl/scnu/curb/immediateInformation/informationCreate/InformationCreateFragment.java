package jxpl.scnu.curb.immediateInformation.informationCreate;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import jxpl.scnu.curb.immediateInformation.ImmediateInformation;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * @author iri-jwj
 * @version 2
 *          last update 3/26
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

    @Override
    public void showPostResult(boolean result) {
        if (result) {
            Snackbar.make(checkNotNull(getView()), "发送成功", 1000);
            m_activity.finish();
        } else
            Snackbar.make(checkNotNull(getView()), "发送失败", 1000);
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
        String toShow = year + "-" + month + "-" + day;
        m_AddInfoEditDate.setText(toShow);
    }

    /**
     * 设置用户选择的小时、分钟
     *
     * @param hour   小时
     * @param minute 分钟
     */
    private void setTime(int hour, int minute) {
        String toShow = hour + ":" + minute;
        m_AddInfoEditTime.setText(toShow);
    }

    @OnClick(R.id.add_info_edit_date)
    public void onM_AddInfoEditDateClicked() {
        DatePicker lc_datePicker = new DatePicker(m_activity);
        lc_datePicker.setElevation(3);
        ConstraintLayout.LayoutParams lc_layoutParams = new ConstraintLayout
                .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lc_layoutParams.setMarginStart(16);
        m_InfoDetailConstraintLayout.addView(lc_datePicker, lc_layoutParams);
        lc_datePicker.setVisibility(View.VISIBLE);

        Calendar lc_calendar = Calendar.getInstance();
        int lc_currentYear = lc_calendar.get(Calendar.YEAR);
        int lc_currentMonth = lc_calendar.get(Calendar.MONTH);
        int lc_currentDay = lc_calendar.get(Calendar.DAY_OF_MONTH);
        lc_datePicker.init(lc_currentYear, lc_currentMonth, lc_currentDay,
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        setDate(year, monthOfYear, dayOfMonth);
                        view.setVisibility(View.GONE);
                    }
                });
    }

    @OnClick(R.id.add_info_edit_time)
    public void onM_AddInfoEditTimeClicked() {
        TimePicker lc_timePicker = new TimePicker(m_activity);
        lc_timePicker.setElevation(3);
        ConstraintLayout.LayoutParams lc_layoutParams = new ConstraintLayout
                .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        m_InfoDetailConstraintLayout.addView(lc_timePicker, lc_layoutParams);
        lc_timePicker.setVisibility(View.VISIBLE);

        lc_timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                setTime(hourOfDay, minute);
            }
        });
    }

    @OnClick(R.id.add_info_button_commit)
    public void onM_AddInfoButtonCommitClicked() {
        String title = m_AddInfoEditTitle.getText().toString();
        String time = m_AddInfoEditDate.getText().toString() + m_AddInfoEditTime.getText().toString();
        String content = m_AddInfoEditContent.getText().toString();
        String address = m_AddInfoEditAddress.getText().toString();
        String belong = (String) m_AddInfoSpinnerBelong.getSelectedItem();
        String unit = (String) m_AddInfoSpinnerUnit.getSelectedItem();
        SimpleDateFormat lc_simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss",
                Locale.CHINA);
        Date lc_date = new Date(System.currentTimeMillis());
        String createTime = lc_simpleDateFormat.format(lc_date);
        Log.d("InformationCreateFrag", "onM_AddInfoButtonCommitClicked: belong:" + belong);
        mPresenter.commitInformation(new ImmediateInformation(UUID.randomUUID(), title, content,
                belong,createTime,time,address));
    }
}
