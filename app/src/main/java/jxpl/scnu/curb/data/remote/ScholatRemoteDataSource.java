package jxpl.scnu.curb.data.remote;

import java.util.List;

import jxpl.scnu.curb.data.repository.ScholatDataSource;
import jxpl.scnu.curb.data.retrofit.RetrofitGetData;
import jxpl.scnu.curb.scholat.ScholatHomework;

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
    public void loadHomeworks(LoadHomeworkCallback para_loadHomeworkCallback, String userId) {
        List<ScholatHomework> lc_homeworks = RetrofitGetData.getHomeworkFromServer(userId);
        if (lc_homeworks != null)
            para_loadHomeworkCallback.onHomeworkLoaded(lc_homeworks);
        else
            para_loadHomeworkCallback.onDataNotAvailable();
    }

    @Override
    public void refreshCache() {

    }
}
