package jxpl.scnu.curb.smallData;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jxpl.scnu.curb.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class SmallDataFragment extends Fragment implements SmallDataInterface.View {

    private SmallDataInterface.Presenter presenter;
    public SmallDataFragment() {
        // Required empty public constructor
    }

    @NonNull
    public static SmallDataFragment newInstance() {
        return new SmallDataFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_small_data, container, false);
    }

    @Override
    public void setPresenter(SmallDataInterface.Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * 当用户点击了一个问卷，将当前显示的内容替换为问卷的详细内容
     */
    @Override
    public void changeToQuestionnaire() {

    }

    /**
     * 当用户点击了提交或返回键时，将当前的内容替换回问卷列表
     */
    @Override
    public void changeBack() {

    }

    /**
     * 将已填写的问卷更换一个颜色
     */
    @Override
    public void markAnswered() {

    }

    /**
     * 当出现了某些错误，如问卷未完成时调用这个方法
     *
     * @param error 错误信息
     */
    @Override
    public void showError(String error) {
        View view = checkNotNull(getView());
        Snackbar.make(view, error, Snackbar.LENGTH_LONG).show();
    }
}
