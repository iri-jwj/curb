package jxpl.scnu.curb.utils.autoFitRecycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.LinkedList;

public abstract class AutoFitAdapter<T extends AutoFitViewHolder> extends RecyclerView.Adapter<T>
        implements ExpandReplicateListener, ViewHolderClickListener {
    protected LinkedList<Boolean> m_isExpand = new LinkedList<>();

    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull T holder, int position) {

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull T holder) {
        super.onViewDetachedFromWindow(holder);

        /*int position = holder.getAdapterPosition();
        m_isExpand.set(position,false);*/
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
