package jxpl.scnu.curb.data.remote;

import android.content.Context;

import java.util.List;

import jxpl.scnu.curb.data.repository.ScholatDataSource;
import jxpl.scnu.curb.data.retrofit.Connect2Server;
import jxpl.scnu.curb.homePage.scholat.ScholatHomework;

/**
 * Created by iri-jwj on 2018/3/27.
 *
 * @author iri-jwj
 * @version 1
 */

public class ScholatRemoteDataSource implements ScholatDataSource {
    @Override
    public void saveHomeworkToLocal(List<ScholatHomework> para_homeworkList) {

    }

    @Override
    public void loadHomeworks(LoadHomeworkCallback para_loadHomeworkCallback, String userId, Context para_context) {
        List<ScholatHomework> lc_homeworks = Connect2Server.getConnect2Server(para_context)
                .getHomeworkFromServer(userId);
        if (lc_homeworks != null)
            para_loadHomeworkCallback.onHomeworkLoaded(lc_homeworks);
        else
            para_loadHomeworkCallback.onDataNotAvailable();
    }

    @Override
    public void refreshCache() {

    }
}
