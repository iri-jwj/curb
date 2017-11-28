package jxpl.scnu.curb.data.repository;

import android.support.annotation.NonNull;

import java.util.List;

import jxpl.scnu.curb.immediateInformation.ImmediateInformation;

/**
 * Created by irijw on 2017/10/15.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public interface InformationDataSource {
    interface loadInformationCallback{
        void getInformationsLoaded(List<ImmediateInformation> immediateInformations);
        void onDataNotAvailable();
    }
    interface getInformationCallback{
        void onInformationLoaded(ImmediateInformation immediateInformation);
        void onDataNotAvailable();
    }

    void getInformation(@NonNull getInformationCallback callback, @NonNull String id);
    void getInformations(@NonNull loadInformationCallback callback);
    void saveInfoFromWeb(List<ImmediateInformation> immediateInformations);
    void refreshInformation();
    void addToArrangement(ImmediateInformation immediateInformation);

}
