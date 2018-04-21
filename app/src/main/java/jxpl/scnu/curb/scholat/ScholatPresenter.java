package jxpl.scnu.curb.scholat;

import android.app.Activity;

import java.util.List;
import java.util.Map;

import jxpl.scnu.curb.data.repository.ScholatDataSource;
import jxpl.scnu.curb.data.repository.ScholatRepository;
import jxpl.scnu.curb.utils.SharedHelper;
import jxpl.scnu.curb.utils.XmlDataStorage;

/**
 * Created by iri-jwj on 2018/3/27.
 *
 * @author iri-jwj
 * @version 1
 */

public class ScholatPresenter implements ScholatContract.Presenter {
    private final ScholatContract.View m_view;
    private final ScholatRepository m_scholatRepository;
    private final Activity m_activity;
    private boolean isFirstLoaded;

    public ScholatPresenter(ScholatContract.View para_view,
                            ScholatRepository para_scholatRepository,
                            Activity para_activity) {
        m_view = para_view;
        m_scholatRepository = para_scholatRepository;
        m_activity = para_activity;
        m_view.setPresenter(this);
        m_view.setActivity(m_activity);

        if (!XmlDataStorage.isSharedHelperSet()) {
            SharedHelper lc_sharedHelper = SharedHelper.getInstance(para_activity);
            XmlDataStorage.setM_sharedHelper(lc_sharedHelper);
        }
        isFirstLoaded = XmlDataStorage.getFirstRunState(XmlDataStorage.IS_SCHOLAT_FIRST_RUN);
    }

    @Override
    public void start() {
        loadScholats(false);
    }

    @Override
    public void loadScholats(boolean forceUpdate) {
        innerLoadScholats(forceUpdate || isFirstLoaded);
    }

    private void innerLoadScholats(boolean forceUpdate) {
        if (forceUpdate) {
            m_scholatRepository.refreshCache();
            isFirstLoaded = false;
        }

        if (!XmlDataStorage.isSharedHelperSet()) {
            SharedHelper lc_sharedHelper = SharedHelper.getInstance(m_activity);
            XmlDataStorage.setM_sharedHelper(lc_sharedHelper);
        }
        Map<String, String> lc_stringMap = XmlDataStorage.getUserInfo();
        m_scholatRepository.loadHomeworks(new ScholatDataSource.LoadHomeworkCallback() {
            @Override
            public void onHomeworkLoaded(List<ScholatHomework> para_homeworkList) {
                m_view.showScholats(para_homeworkList);
            }

            @Override
            public void onDataNotAvailable() {
                m_view.showMessage("获取消息错误");
            }
        }, lc_stringMap.get(XmlDataStorage.USER_ID));
    }

    @Override
    public void addToReminder(ScholatHomework para_scholatHomework) {

    }
}
