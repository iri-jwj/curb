package jxpl.scnu.curb.data.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import jxpl.scnu.curb.data.repository.InformationDataSource;
import jxpl.scnu.curb.data.retrofit.Connect2Server;
import jxpl.scnu.curb.homePage.immediateInformation.ImmediateInformation;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author iri-jwj
 * @version 1
 */

public class InformationRemoteDataSource implements InformationDataSource {
    private static InformationRemoteDataSource INSTANCE;

    private InformationRemoteDataSource() {
    }

    public static InformationRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new InformationRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void refreshInformation() {

    }

    @Override
    public void saveInfoFromWeb(List<ImmediateInformation> immediateInformations) {

    }

    @Override
    public void getInformation(@NonNull GetInformationCallback callback, UUID id) {

    }

    @Override
    public void getInformations(@NonNull LoadInformationCallback callback,
                                String userId, String timestamp, Context para_context) {
        List<ImmediateInformation> immediateInformationsTemp = Connect2Server.getConnect2Server(para_context)
                .getInformationInRetrofit(userId, timestamp);
        if ( immediateInformationsTemp!= null && !immediateInformationsTemp.isEmpty()) {
            List<ImmediateInformation> lc_immediateInformations = new ArrayList<>();
            int i = 0;
            int size = immediateInformationsTemp.size();
            while (i < size) {
                int index = 0;
                for (int j = 0; j < immediateInformationsTemp.size(); j++) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
                    Date current = sdf.parse(checkNotNull(immediateInformationsTemp.get(j).getCreateTime()), new ParsePosition(0));
                    Date before = sdf.parse(checkNotNull(immediateInformationsTemp.get(index).getCreateTime()), new ParsePosition(0));
                    if (current.before(before))
                        index = j;
                }
                lc_immediateInformations.add(immediateInformationsTemp.get(index));
                immediateInformationsTemp.remove(index);
                i++;
            }
            callback.getInformationsLoaded(lc_immediateInformations);
        }
        else
            callback.onDataNotAvailable();
    }

    @Override
    public void addToReminder(ImmediateInformation immediateInformation) {

    }

    @Override
    public void postInformation(PostInformationCallback para_callback, String information,
                                String userId, Context para_context) {
        String result = Connect2Server.getConnect2Server(para_context)
                .postCreateInformation(userId, information);
        if (result == null)
            para_callback.onPostFailed();
        else
            para_callback.onInformationPosted();
    }
}
