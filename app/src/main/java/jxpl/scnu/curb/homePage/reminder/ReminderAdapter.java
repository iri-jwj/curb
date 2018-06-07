package jxpl.scnu.curb.homePage.reminder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jxpl.scnu.curb.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * created on 2018/6/4
 *
 * @author iri-jwj
 * @version 1 init
 */
public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    private List<Reminder> m_reminders;
    private ReminderContract.Presenter m_presenter;
    private Context m_context;

    public ReminderAdapter(Context para_context) {
        m_reminders = new ArrayList<>();
        m_context = para_context;
    }

    public void setReminders(@NonNull List<Reminder> para_reminders) {
        checkNotNull(para_reminders);
        m_reminders = para_reminders;
        notifyDataSetChanged();
    }

    private void deleteReminder(final int position) {
        final String title = m_reminders.get(position).getTitle();
        m_reminders.remove(position);
        notifyItemRemoved(position);

        new Thread(new Runnable() {
            @Override
            public void run() {
                m_presenter.deleteReminder(title);
            }
        }).start();
    }

    public void setPresenter(ReminderContract.Presenter para_presenter) {
        m_presenter = para_presenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View lc_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder, parent, false);
        return new ViewHolder(lc_view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.m_ReminderItemTitle.setText(m_reminders.get(position).getTitle());
        holder.m_ReminderItemTime.setText(m_reminders.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return m_reminders == null ? 0 : m_reminders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        @BindView(R.id.reminder_item_title)
        TextView m_ReminderItemTitle;
        @BindView(R.id.reminder_item_time)
        TextView m_ReminderItemTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            PopupMenu lc_popupMenu = new PopupMenu(m_context, v);
            lc_popupMenu.inflate(R.menu.reminder_long_click);
            lc_popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.reminder_menu_delete) {
                        final int position = ViewHolder.this.getAdapterPosition();
                        deleteReminder(position);
                    }
                    return true;
                }
            });
            lc_popupMenu.show();
            return true;
        }
    }
}
