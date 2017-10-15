package jxpl.scnu.curb.data.repository;

import android.support.annotation.NonNull;

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

    }
    interface getInformationCallback{

    }

    void getInformation(@NonNull getInformationCallback callback);
    void addToArrangement(ImmediateInformation immediateInformation);
}
