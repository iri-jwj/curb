package jxpl.scnu.curb.data.repository;

import java.util.List;

import jxpl.scnu.curb.scholat.ScholatHomework;

/**
 * Created by iri-jwj on 2018/3/27.
 *
 * @author iri-jwj
 * @version 1
 */

public interface ScholatDataSource {
    void saveHomeworkToLocal(List<ScholatHomework> para_homeworkList);

    void loadHomeworks(LoadHomeworkCallback para_loadHomeworkCallback, String userId);

    void refreshCache();

    interface LoadHomeworkCallback {
        void onHomeworkLoaded(List<ScholatHomework> para_homeworkList);

        void onDataNotAvailable();
    }
}
