package jxpl.scnu.curb.data.remote;

import android.content.Context;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxpl.scnu.curb.data.repository.ScholatDataSource;
import jxpl.scnu.curb.data.retrofit.Connect2Server;
import jxpl.scnu.curb.homePage.scholat.ScholatHomework;

import static com.google.common.base.Preconditions.checkNotNull;

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
        List<ScholatHomework> lc_homeworksTemp = Connect2Server.getConnect2Server(para_context)
                .getHomeworkFromServer(userId);
        if (lc_homeworksTemp != null && !lc_homeworksTemp.isEmpty()){
            List<ScholatHomework> lc_scholatHomeworks = new ArrayList<>();
            int i=0;
            int size = lc_homeworksTemp.size();
            while (i<size){
                int index = 0;
                for (int j=0;j<lc_homeworksTemp.size();j++){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
                    Date current = sdf.parse(checkNotNull(lc_homeworksTemp.get(j).getCreateTime()), new ParsePosition(0));
                    Date before = sdf.parse(checkNotNull(lc_homeworksTemp.get(index).getCreateTime()), new ParsePosition(0));
                    if (current.after(before))
                        index = j;
                }
                lc_scholatHomeworks.add(lc_homeworksTemp.get(index));
                lc_homeworksTemp.remove(index);
                i++;
            }
            para_loadHomeworkCallback.onHomeworkLoaded(lc_scholatHomeworks);
        }
        else
            para_loadHomeworkCallback.onDataNotAvailable();
    }

    @Override
    public void refreshCache() {

    }
}
