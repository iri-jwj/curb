package jxpl.scnu.curb.data.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;
import java.util.UUID;

import jxpl.scnu.curb.data.repository.InformationDataSource;
import jxpl.scnu.curb.data.retrofit.RetrofitGetData;
import jxpl.scnu.curb.immediateInformation.ImmediateInformation;

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
                                String userId, String timestamp) {
        List<ImmediateInformation> immediateInformations = RetrofitGetData
                .getInformationInRetrofit(userId, timestamp);
        Log.d("RemoteDateSource", "getInformations: " +
                immediateInformations.isEmpty());
        if (!immediateInformations.isEmpty())
            callback.getInformationsLoaded(immediateInformations);
        else
            callback.onDataNotAvailable();
    }

    @Override
    public void addToReminder(ImmediateInformation immediateInformation) {

    }

    @Override
    public void postInformation(PostInformationCallback para_callback, String information,
                                String userId) {
        String result = RetrofitGetData.postCreateInformation(userId, information);
        if (result == null)
            para_callback.onPostFailed();
        else
            para_callback.onInformationPosted();
    }
}
