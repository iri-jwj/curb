package jxpl.scnu.curb.data.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import jxpl.scnu.curb.data.repository.InformationDataSource;
import jxpl.scnu.curb.data.retrofit.RetrofitGetData;
import jxpl.scnu.curb.immediateInformation.ImmediateInformation;

/**
 * Created by irijw on 2017/10/15.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public class InformationRemoteDataSource implements InformationDataSource {
    private static InformationRemoteDataSource INSTANCE;
    private InformationRemoteDataSource() {    }


    @Override
    public void refreshInformation() {

    }

    public static InformationRemoteDataSource getInstance(){
        if (INSTANCE==null)
            INSTANCE=new InformationRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void saveInfoFromWeb(List<ImmediateInformation> immediateInformations)  {

    }

    @Override
    public void getInformation(@NonNull getInformationCallback callback,@NonNull String id) {

    }

    @Override
    public void getInformations(@NonNull loadInformationCallback callback) {
        List<ImmediateInformation> immediateInformations=RetrofitGetData.getDataFromWeb();
        Log.d("RemoteDateSource", "getInformations: " +
                immediateInformations.isEmpty());
        if (!immediateInformations.isEmpty())
            callback.getInformationsLoaded(immediateInformations);
        else
            callback.onDataNotAvailable();
    }

    @Override
    public void addToArrangement(ImmediateInformation immediateInformation) {

    }
}
