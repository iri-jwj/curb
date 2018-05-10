package jxpl.scnu.curb.utils.autoFitRecycler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

public abstract class AutoFitAdapter<T extends AutoFitViewHolder> extends RecyclerView.Adapter<T>
        implements ExpandReplicateListener, ViewHolderClickListener {

    protected LinkedList<Boolean> m_isExpand = new LinkedList<>();
    private View skeleton;
    private View details;
    private View additional;

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
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onViewRecycled(@NonNull T holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull T holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    protected void initOperatedView(@NonNull View para_skeleton, @NonNull View para_details, @Nullable View para_additional) {
        skeleton = para_skeleton;
        details = para_details;
        additional = para_additional;
    }


}
