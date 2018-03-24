package jxpl.scnu.curb.data.retrofit;

import java.util.List;

import jxpl.scnu.curb.immediateInformation.ImmediateInformation;
import jxpl.scnu.curb.smallData.SDAnswer;
import jxpl.scnu.curb.smallData.SDDetail;
import jxpl.scnu.curb.smallData.SDResult;
import jxpl.scnu.curb.smallData.SDSummary;
import jxpl.scnu.curb.smallData.SDSummaryCreate;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by irijw on 2017/10/22.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public interface ServerInterface {
    //获取information
    @GET("/showUnfinishHomework/{userid}")
    Call<List<ImmediateInformation>> getInfoFromServer(@Path("userid") int id);

    //登陆
    @POST("/logintest")
    Call<String> postLogin(@Query("id") int id, @Query("userName") String userName,
                           @Query("password") String password);


    @POST("curb/smalldata/questiondetail")
    Call<List<SDDetail>> postSmallDataDetail(@Query("sd_id") String id);

    @POST("curb/smalldata/summary")
    Call<List<SDSummary>> getSmallDataSummary(@Query("timestamp") String time
            , @Query("direction") int direction);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("curb/smalldata/userans")
    Call<String> postAnswer(@Part("description") RequestBody body);

    @GET("curb/smalldata/userans")
    Call<List<SDAnswer>> getAnswers(@Query("sd_id") String id);


    @Multipart
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/smallData/uploadCreatedSD")
    Call<String> postCreatedSD(@Part("description") RequestBody para_requestBody
            , @Part MultipartBody.Part file);

    @GET("/smallData/getCreatedSummaries")
    Call<List<SDSummaryCreate>> getCreatedSummaries();

    @GET("/smallData/getCreatedDetails")
    Call<List<SDDetail>> getCreatedDetails(@Query("st_id") String id);

    @GET("curb/smalldata/questionresult")
    Call<List<SDResult>> getSDResult(@Query("st_id") String summaryId);
}
