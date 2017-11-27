package jxpl.scnu.curb.data.retrofit;

import java.util.List;

import jxpl.scnu.curb.immediateInformation.ImmediateInformation;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by irijw on 2017/10/22.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public interface  GetInformationInterface {
    @GET("/showJson")
    Call<List<ImmediateInformation>> getCall();
}
