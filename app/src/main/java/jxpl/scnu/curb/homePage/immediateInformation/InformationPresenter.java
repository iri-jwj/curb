package jxpl.scnu.curb.homePage.immediateInformation;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import jxpl.scnu.curb.data.repository.InformationDataSource;
import jxpl.scnu.curb.data.repository.InformationRepository;
import jxpl.scnu.curb.utils.SharedHelper;
import jxpl.scnu.curb.utils.XmlDataStorage;

import static com.google.common.base.Preconditions.checkNotNull;
import static jxpl.scnu.curb.homePage.immediateInformation.InformationFilter.ALL_INFORMATIONS;

/**
 * @author iri-jwj
 * @version 2
 * update: 3/24
 * <p>
 * update:3/29
 * 连接XmlDataStorage，获取userId
 * @see XmlDataStorage#getUserInfo()
 */

public class InformationPresenter implements InformationContract.Presenter, Serializable {

    private final InformationRepository informationRepository;
    private final InformationContract.View informationView;
    private final Activity m_activity;
    private InformationFilter currentFilter = ALL_INFORMATIONS;
    private boolean firstLoad;

    public InformationPresenter(@NonNull InformationRepository informationRepository,
                                InformationContract.View informationView,
                                Activity para_activity) {
        this.informationRepository = checkNotNull(informationRepository);
        m_activity = checkNotNull(para_activity);
        this.informationView = checkNotNull(informationView);
        informationView.setPresenter(this);
    }

    @Override
    public void start() {
        if (informationView != null)
            informationView.setPresenter(this);
        loadInformation(false);
    }

    @Override
    public void getInformationFromRepository() {
        SimpleDateFormat lc_simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss",
                Locale.CHINA);
        Date lc_date = new Date(System.currentTimeMillis());
        String timestamp = lc_simpleDateFormat.format(lc_date);
        if (!XmlDataStorage.isSharedHelperSet()) {
            SharedHelper lc_sharedHelper = SharedHelper.getInstance(m_activity);
            XmlDataStorage.setM_sharedHelper(lc_sharedHelper);
        }
        Map<String, String> lc_stringMap = XmlDataStorage.getUserInfo();
        informationRepository.getInformations(new InformationDataSource.LoadInformationCallback() {
            @Override
            public void getInformationsLoaded(List<ImmediateInformation> immediateInformationsFromCB) {
                List<ImmediateInformation> lc_informationListShown =
                        new LinkedList<>(informationView.getCurrentList());

                List<UUID> lc_uuids = new LinkedList<>();
                for (ImmediateInformation i : lc_informationListShown) {
                    lc_uuids.add(i.getId());
                }

                List<ImmediateInformation> lc_informations =
                        new LinkedList<>(immediateInformationsFromCB);

                for (ImmediateInformation i : immediateInformationsFromCB) {
                    if (lc_uuids.contains(i.getId()))
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
            }
        }, lc_stringMap.get(XmlDataStorage.USER_ID), timestamp, m_activity);
    }


    @Override
    public String getFiltering() {
        return currentFilter.toString();
    }


    @Override
    public void setFiltering(String filtering) {
        currentFilter = InformationFilter.valueOf(filtering);
    }


    @Override
    public void loadInformation(boolean forceUpdate) {
        if (!XmlDataStorage.isSharedHelperSet()) {
            SharedHelper lc_sharedHelper = SharedHelper.getInstance(m_activity);
            XmlDataStorage.setM_sharedHelper(lc_sharedHelper);
        }
        firstLoad = XmlDataStorage.getFirstRunState(XmlDataStorage.IS_INFO_FIRST_RUN);
        loadInformations(forceUpdate || firstLoad);
        if (firstLoad) {
            XmlDataStorage.setInfoFirstRun(false);
            firstLoad = false;
        }

    }

    /**
     * @param forceUpdate 判断是否强制刷新，即强制从网络获取最新信息；
     *                    若为强制刷新，则调用
     * @see InformationRepository#refreshInformation() 方法
     * 再调用 {@link #getInformationFromRepository()} 方法获取资讯
     */
    private void loadInformations(boolean forceUpdate) {
        informationView.setLoadingIndicator(true);
        if (forceUpdate) {
            informationRepository.refreshInformation();
        }
        getInformationFromRepository();
    }

    /**
     * @param immediateInformations 需要检查的List
     *                              若内容是空的，则调用view中的方法显示没有消息
     * @see InformationFragment#showNoInfo()
     * 若内容不为空，则显示List
     * @see InformationFragment#showInfo(List)
     * <p>
     * 3.24 添加判断： 当list和view中中list都为空时才显示 NoInfo
     */
    private void CheckIfInfoEmpty(final List<ImmediateInformation> immediateInformations) {
        Log.d("informationPresenter", "CheckIfInfoEmpty: " + immediateInformations.isEmpty());
        if (immediateInformations.isEmpty()) {
            if (informationView.isListShowing()) {
                informationView.showNoInfo();
            } else {
                informationView.showNoNewInfo();
            }
        } else {
            m_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    informationView.showInfo(immediateInformations);
                }
            });
        }
    }

    @Override
    public void openInformationDetails(@NonNull ImmediateInformation immediateInformation, @NonNull Context context) {
        checkNotNull(immediateInformation);
        checkNotNull(context);
        informationView.showInformationDetailsUi(immediateInformation.getId(), context);
    }

}
