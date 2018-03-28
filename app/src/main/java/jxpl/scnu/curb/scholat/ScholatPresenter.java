package jxpl.scnu.curb.scholat;

import android.app.Activity;

import java.util.List;

import jxpl.scnu.curb.data.repository.ScholatDataSource;
import jxpl.scnu.curb.data.repository.ScholatRepository;

/**
 * Created by iri-jwj on 2018/3/27.
 *
 * @author iri-jwj
 * @version 1
 */

public class ScholatPresenter implements ScholatContract.Presenter {
    private final ScholatContract.View m_view;
    private final ScholatRepository m_scholatRepository;

    private boolean isFirstLoaded = true;

    public ScholatPresenter(ScholatContract.View para_view,
                            ScholatRepository para_scholatRepository,
                            Activity para_activity) {
        m_view = para_view;
        m_scholatRepository = para_scholatRepository;
        m_view.setPresenter(this);
        m_view.setActivity(para_activity);
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

        m_scholatRepository.loadHomeworks(new ScholatDataSource.LoadHomeworkCallback() {
            @Override
            public void onHomeworkLoaded(List<ScholatHomework> para_homeworkList) {

            }

            @Override
            public void onDataNotAvailable() {

            }
        }, "23");
        //todo replace userId
    }

    @Override
    public void addToReminder(ScholatHomework para_scholatHomework) {

    }
}
