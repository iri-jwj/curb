package jxpl.scnu.curb.data.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxpl.scnu.curb.data.local.ScholatLocalDataSource;
import jxpl.scnu.curb.data.remote.ScholatRemoteDataSource;
import jxpl.scnu.curb.homePage.scholat.ScholatHomework;
import jxpl.scnu.curb.utils.SharedHelper;
import jxpl.scnu.curb.utils.XmlDataStorage;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by iri-jwj on 2018/3/28.
 *
 * @author iri-jwj
 * @version 1
 */

public class ScholatRepository implements ScholatDataSource {
    private volatile static ScholatRepository instance = null;
    private final ScholatLocalDataSource m_scholatLocalDataSource;
    private final ScholatRemoteDataSource m_scholatRemoteDataSource;

    private Map<String, ScholatHomework> m_homeworkCache = new LinkedHashMap<>();
    private boolean isCacheDirty;

    private ScholatRepository(@NonNull ScholatLocalDataSource para_scholatLocalDataSource,
                              @NonNull ScholatRemoteDataSource para_scholatRemoteDataSource,
                              @NonNull Context para_context) {
        m_scholatLocalDataSource = checkNotNull(para_scholatLocalDataSource);
        m_scholatRemoteDataSource = checkNotNull(para_scholatRemoteDataSource);
        if (!XmlDataStorage.isSharedHelperSet())
            XmlDataStorage.setM_sharedHelper(SharedHelper.getInstance(para_context));
        isCacheDirty = XmlDataStorage.getFirstRunState(XmlDataStorage.IS_SCHOLAT_FIRST_RUN);
    }

    public static ScholatRepository getInstance(Context para_context) {
        if (instance == null)
            instance = new ScholatRepository(new ScholatLocalDataSource(para_context), new ScholatRemoteDataSource(), para_context);
        return instance;
    }

    @Override
    public void saveHomeworkToLocal(final List<ScholatHomework> para_homeworkList) {
        final Thread lc_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                m_scholatLocalDataSource.saveHomeworkToLocal(para_homeworkList);
            }
        });
        lc_thread.start();
    }

    @Override
    public void loadHomeworks(final LoadHomeworkCallback para_loadHomeworkCallback,
                              final String userId, final Context para_context) {
        if (!m_homeworkCache.isEmpty() && !isCacheDirty) {
            para_loadHomeworkCallback.onHomeworkLoaded(new LinkedList<>(m_homeworkCache.values()));
            return;
        }
        final Thread lc_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                getHomeworkFromServer(para_loadHomeworkCallback, userId, para_context);
            }
        });

        if (isCacheDirty) {
            lc_thread.start();
        } else {
            m_scholatLocalDataSource.loadHomeworks(new LoadHomeworkCallback() {
                @Override
                public void onHomeworkLoaded(List<ScholatHomework> para_homeworkList) {
                    refreshMap(para_homeworkList);
                    para_loadHomeworkCallback.onHomeworkLoaded(para_homeworkList);
                }

                @Override
                public void onDataNotAvailable() {
                    lc_thread.start();
                }
            }, userId, para_context);
        }
    }

    private void getHomeworkFromServer(@NonNull final LoadHomeworkCallback para_loadHomeworkCallback,
                                       final String userId, Context para_context) {
        m_scholatRemoteDataSource.loadHomeworks(new LoadHomeworkCallback() {
            @Override
            public void onHomeworkLoaded(List<ScholatHomework> para_homeworkList) {
                refreshMap(para_homeworkList);
                saveHomeworkToLocal(para_homeworkList);
                para_loadHomeworkCallback.onHomeworkLoaded(para_homeworkList);
            }

            @Override
            public void onDataNotAvailable() {
                para_loadHomeworkCallback.onDataNotAvailable();
            }
        }, userId, para_context);
    }

    private void refreshMap(List<ScholatHomework> para_homeworks) {
        for (ScholatHomework homework :
                para_homeworks) {
            m_homeworkCache.put(homework.getHomeworkId(), homework);
        }
        isCacheDirty = false;
    }

    @Override
    public void refreshCache() {
        isCacheDirty = true;
    }
}
