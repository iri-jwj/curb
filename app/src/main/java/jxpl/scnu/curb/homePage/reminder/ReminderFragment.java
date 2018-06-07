package jxpl.scnu.curb.homePage.reminder;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jxpl.scnu.curb.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReminderFragment extends Fragment implements ReminderContract.View {

    @BindView(R.id.reminder_no_reminder)
    TextView m_ReminderNoReminder;
    @BindView(R.id.reminder_list)
    RecyclerView m_ReminderList;
    @BindView(R.id.reminder_linear)
    LinearLayout m_ReminderLinear;
    Unbinder unbinder;

    private ReminderContract.Presenter m_presenter;
    private ReminderAdapter m_reminderAdapter;

    public ReminderFragment() {
        // Required empty public constructor
    }

    public static ReminderFragment newInstance() {
        ReminderFragment fragment = new ReminderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_reminderAdapter = new ReminderAdapter(getContext());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            m_presenter.start();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);
        LinearLayoutManager lc_linearLayoutManager = new LinearLayoutManager(this.getContext());

        unbinder = ButterKnife.bind(this, view);
        m_ReminderList.setLayoutManager(lc_linearLayoutManager);
        m_ReminderList.setAdapter(m_reminderAdapter);
        m_ReminderList.addItemDecoration(new DividerItemDecoration(
                checkNotNull(getContext()), DividerItemDecoration.VERTICAL));
        m_ReminderList.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        m_presenter.start();
        m_reminderAdapter.setPresenter(m_presenter);
    }


    @Override
    public void setPresenter(ReminderContract.Presenter presenter) {
        m_presenter = presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showNoReminder() {
        m_ReminderNoReminder.setVisibility(View.VISIBLE);
        m_ReminderList.setVisibility(View.GONE);
    }

    @Override
    public void showReminders(List<Reminder> para_reminders) {
        if (m_ReminderNoReminder.getVisibility() == View.VISIBLE)
            m_ReminderNoReminder.setVisibility(View.GONE);
        m_ReminderList.setVisibility(View.VISIBLE);
        m_reminderAdapter.setReminders(para_reminders);
    }

    @Override
    public void showError(String msg) {
        Snackbar.make(checkNotNull(getView()), msg, 1250).show();
    }
}
