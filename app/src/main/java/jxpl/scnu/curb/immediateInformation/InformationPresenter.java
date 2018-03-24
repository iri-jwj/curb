package jxpl.scnu.curb.immediateInformation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jxpl.scnu.curb.data.repository.InformationDataSource;
import jxpl.scnu.curb.data.repository.InformationRepository;

import static com.google.common.base.Preconditions.checkNotNull;
import static jxpl.scnu.curb.immediateInformation.InformationFilter.ALL_INFORMATIONS;

/**
 *@author iri-jwj
 * @version 2
 * last update: 3.24
 */

public class InformationPresenter implements InformationContract.Presenter, Serializable {

    private final InformationRepository informationRepository;
    private final InformationContract.View informationView;
    private InformationFilter currentFilter = ALL_INFORMATIONS;
    private boolean firstLoad = true;

    public InformationPresenter(@NonNull InformationRepository informationRepository,
                                InformationContract.View informationView) {
        this.informationRepository = checkNotNull(informationRepository);
        this.informationView = checkNotNull(informationView);
        informationView.setPresenter(this);
    }

    @Override
    public void start() {
        if (informationView != null)
            informationView.setPresenter(this);
        loadInformation(false);
    }

    /**
     * {void getInformationFromRepository()}
     * 从Repository中获取资讯信息
     * 3.24更新：取消了分类机制，直接显示所有信息;
     * 将在View中Adapter中对新资讯与旧资讯的相同判断逻辑放到此处
     */
    @Override
    public void getInformationFromRepository() {

        informationRepository.getInformations(new InformationDataSource.loadInformationCallback() {
            @Override
            public void getInformationsLoaded(List<ImmediateInformation> immediateInformationsFromCB) {
                /*for (ImmediateInformation i :
                        immediateInformationsFromCB) {
                    Log.d("getInfoFromR", i.toString());
                    switch (currentFilter) {
                        case ALL_INFORMATIONS:
                            informationToShow.add(i);
                            Log.d("getInfoIsSucceed?", "YES");
                            break;
                        case EDU_INFORMATIONS:
                            if (i.getType().equals("EDU_INFORMATIONS")) {
                                informationToShow.add(i);
                            }
                            break;
                        case PRA_INFORMATIONS:
                            if (i.getType().equals("PRA_INFORMATIONS")) {
                                informationToShow.add(i);
                            }
                            break;
                        case SCHOLAR_INFORMATIONS:
                            if (i.getType().equals("SCHOLAR_INFORMATIONS")) {
                                informationToShow.add(i);
                            }
                            break;
                        case WORK_STUDY_INFORMATIONS:
                            if (i.getType().equals("WORK_STUDY_INFORMATIONS"))
                                informationToShow.add(i);
                            break;
                        default:
                            informationToShow.add(i);
                            break;
                    }
                }*/
                List<ImmediateInformation> lc_informationListShown =
                        new LinkedList<>(informationView.getCurrentList());

                List<ImmediateInformation> lc_informations =
                        new LinkedList<>(immediateInformationsFromCB);

                for (ImmediateInformation i :
                        immediateInformationsFromCB) {
                    if (lc_informationListShown.contains(i))
                        lc_informations.remove(i);
                }
                if (!lc_informations.isEmpty()) {
                    lc_informations.addAll(lc_informationListShown);
                    CheckIfInfoEmpty(lc_informations);
                } else
                    informationView.showNoNewInfo();

                informationView.setLoadingIndicator(false);
            }

            @Override
            public void onDataNotAvailable() {
                informationView.setLoadingIndicator(false);
                if (!informationView.isActive())
                    return;
                informationView.showLoadingError();
                Log.d("getInfoFromR", "failed");
            }
        });

    }

    /**
     *@deprecated
     */
    @Override
    public String getFiltering() {
        return currentFilter.toString();
    }

    /**
     * @deprecated
     * @param filtering 筛选的目的信息
     */
    @Override
    public void setFiltering(String filtering) {
        currentFilter = InformationFilter.valueOf(filtering);
    }

    /**
     * @param forceUpdate 判断是否强制刷新，即强制从网络获取最新信息
     * 判断是否是第一次刷新，然后调用真正的装载信息的方法{@link #loadInformations(boolean)}
     */
    @Override
    public void loadInformation(boolean forceUpdate) {
        loadInformations(forceUpdate || firstLoad);
        firstLoad = false;
    }

    /**
     *
     * @param forceUpdate 判断是否强制刷新，即强制从网络获取最新信息；
     *  若为强制刷新，则调用
     *  @see InformationRepository#refreshInformation() 方法
     *  再调用 {@link #getInformationFromRepository()} 方法获取资讯
     */
    private void loadInformations(boolean forceUpdate) {
        informationView.setLoadingIndicator(true);
        if (forceUpdate) {
            informationRepository.refreshInformation();
        }
        getInformationFromRepository();
    }

    /**
     *
     * @param immediateInformations 需要检查的List
     * 若内容是空的，则调用view中的方法显示没有消息
     * @see InformationFragment#showNoInfo()
     * 若内容不为空，则显示List
     * @see InformationFragment#showInfo(List)
     *
     * 3.24 添加判断： 当list和view中中list都为空时才显示 NoInfo
     */
    private void CheckIfInfoEmpty(List<ImmediateInformation> immediateInformations) {
        Log.d("informationPresenter", "CheckIfInfoEmpty: " + immediateInformations.isEmpty());
        if (immediateInformations.isEmpty()) {
            if (informationView.isListShowing()) {
                informationView.showNoInfo();
            } else {
                informationView.showNoNewInfo();
            }
        } else {
            informationView.showInfo(immediateInformations);
        }
    }

    /**
     * @deprecated
     * @param immediateInformation 需要显示详细的information
     * @param context 应用上下文
     */
    @Override
    public void openInformationDetails(@NonNull ImmediateInformation immediateInformation, @NonNull Context context) {
        checkNotNull(immediateInformation);
        checkNotNull(context);
        informationView.showInformationDetailsUi(immediateInformation.getHomeworkId(), context);
    }

}
