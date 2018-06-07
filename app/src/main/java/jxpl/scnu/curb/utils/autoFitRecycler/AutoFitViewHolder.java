package jxpl.scnu.curb.utils.autoFitRecycler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

public abstract class AutoFitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = "AutoFitViewHolder";
    private boolean isExpand = false;
    private ViewHolderClickListener m_listener;
    private View skeleton;
    private View details;
    private ImageView indicator = null;

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

    protected void bindExpandView(@NonNull View para_skeleton, @NonNull View para_details, @Nullable ImageView para_additional) {
        skeleton = para_skeleton;
        details = para_details;
        if (para_additional != null) {
            indicator = para_additional;
        }
    }

    public View getSkeleton() {
        return skeleton;
    }

    public View getDetails() {
        return details;
    }

    public ImageView getIndicator() {
        return indicator;
    }
}
