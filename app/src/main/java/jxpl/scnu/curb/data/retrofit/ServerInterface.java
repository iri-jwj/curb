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
 * @author iri-jwj
 * @version 2
 * last update 3/25
 * 获取information的接口添加参数 userId:String , timestamp:String
 */

public interface ServerInterface {
    //获取information
    @GET("/showUnfinishHomework/{userid}")
    Call<List<ImmediateInformation>> getInfoFromServer(@Path("userid") int id);

    //登陆
//    @POST("/logintest")
//    Call<String> postLogin(@Query("id") int id, @Query("userName") String userName,
//                           @Query("password") String password);

    /**
     * 获取最新的资讯（information）的接口
     *
     * @param userId    用户的账户|标识符
     * @param timestamp 用户发起请求的时间
     * @return 返回获得的List
     */
    @POST("curb/news/lately")
    Call<List<ImmediateInformation>> postInformation(@Query("userId") String userId,
                                                     @Query("timestamp") String timestamp);

    /**
     * 发送用户创建的information至服务端
     *
     * @param information 需要发送的数据体？
     * @return 返回结果
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("curb/information/send")
    Call<String> postCreateInformation(@Part("createdInformation") RequestBody information,
                                       @Query("userId") String userId);

    /**
     * 获取用户点击的summary的详细信息
     * @param id 用户点击的summaryId
     * @return 服务器返回对应id的问题详情
     */
    @POST("curb/smalldata/questiondetail")
    Call<List<SDDetail>> postSmallDataDetail(@Query("sd_id") String id);

    /**
     * 获取smallData概要的接口
     * @param time 发起获取的时间
     * @param direction 向前获取还是向后
     * @return 返回获得的smallData
     */
    @POST("curb/smalldata/summary")
    Call<List<SDSummary>> getSmallDataSummary(@Query("timestamp") String time
            , @Query("direction") int direction);

    /**
     * 发送用户完成问卷的答案
     * @param body 用户的答案转化为json
     * @return 返回结果
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("curb/smalldata/userans")
    Call<String> postAnswer(@Part("description") RequestBody body);

    /**
     * 获取用户的答题结果
     * @param id summary id
     * @return 返回结果
     */
    @GET("curb/smalldata/userans")
    Call<List<SDAnswer>> getAnswers(@Query("sd_id") String id);

    /**
     * 发送用户创建的smallData
     * @param para_requestBody 用户编写的题目、问题等
     * @param file 用户选择的图片文件
     * @return 返回结果
     */
    @Multipart
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/smallData/uploadCreatedSD")
    Call<String> postCreatedSD(@Part("description") RequestBody para_requestBody
            , @Part MultipartBody.Part file);

    /**
     * 获取用户自身创建的summary
     * @return 返回List
     */
    @GET("/smallData/getCreatedSummaries")
    Call<List<SDSummaryCreate>> getCreatedSummaries();

    /**
     * 返回用户自身创建的summary对应的详情
     * @param id 问卷id
     * @return 返回list
     */
    @GET("/smallData/getCreatedDetails")
    Call<List<SDDetail>> getCreatedDetails(@Query("st_id") String id);

    /**
     * 返回用户完成问卷之后，问卷的结果信息
     * @param summaryId 即问卷的id
     * @return 返回结果信息
     */
    @GET("curb/smalldata/questionresult")
    Call<List<SDResult>> getSDResult(@Query("st_id") String summaryId);

    /**
     * lfumin
     * 2018-03-23
     * 注册接口
     */
    @POST("curb/account/register")
    Call<String> postRegister(@Query("id") String id, @Query("account") String userName,
                              @Query("password") String password);

    /**
     * lfumin
     * 2018-03-23
     *
     * @param userName
     * @param password
     * @return
     * 用户的登录入口
     */
    @POST("/curb/account/login")
    Call<String> postLogin(@Query("userName") String userName,
                           @Query("password") String password);
}
