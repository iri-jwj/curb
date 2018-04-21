package jxpl.scnu.curb.utils.autoFitRecycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class AutoFitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private boolean isExpand = false;
    private ViewHolderClickListener m_listener;

    public AutoFitViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean para_expand) {
        isExpand = para_expand;
    }

    @Override
    public void onClick(View v) {
        if (m_listener != null)
            m_listener.onViewHolderClick(getAdapterPosition());
    }

    public void setListener(ViewHolderClickListener para_listener) {
        m_listener = para_listener;
    }
}
