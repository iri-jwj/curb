package jxpl.scnu.curb.data.retrofit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;

import jxpl.scnu.curb.immediateInformation.ImmediateInformation;
import jxpl.scnu.curb.scholat.ScholatHomework;
import jxpl.scnu.curb.smallData.SDAnswer;
import jxpl.scnu.curb.smallData.SDDetail;
import jxpl.scnu.curb.smallData.SDResult;
import jxpl.scnu.curb.smallData.SDSummary;
import jxpl.scnu.curb.smallData.SDSummaryCreate;
import jxpl.scnu.curb.userProfile.UserProfileContract;
import jxpl.scnu.curb.utils.MyHostnameVerifier;
import jxpl.scnu.curb.utils.MyTrustManage;
import jxpl.scnu.curb.utils.SSLFactory;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 建立网络连接、发送接收数据
 * 尽量不执行数据的处理操作
 *
 * @author iri-jwj
 * @version 2
 * last update 2018/3/25
 * 更新获取information的代码，添加参数 timestamp:String ,userId:String
 */
public class Connect2Server {
    private static ServerInterface serverInterface;
    private static volatile Connect2Server stc_connect2Server = null;

    private Connect2Server(Context para_context) throws Exception {
        Gson lc_gson = new GsonBuilder().setLenient().create();
        Map certificate = SSLFactory.getSSLCertifcation(para_context);
        OkHttpClient lc_okHttpClient = null;
        if (certificate.size() != 0) {
            lc_okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory((SSLSocketFactory) certificate.get("ssl"),
                            (X509TrustManager) certificate.get("x509"))
                    .hostnameVerifier(new MyHostnameVerifier()).build();
        } else {
            throw new Exception("未成功生成认证信息");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://39.108.105.150")
                .addConverterFactory(GsonConverterFactory.create(lc_gson))
                .client(lc_okHttpClient)
                .build();
        serverInterface = retrofit.create(ServerInterface.class);
    }

    public static synchronized Connect2Server getConnect2Server(Context para_context) {
        if (stc_connect2Server == null)
            try {
                stc_connect2Server = new Connect2Server(para_context);
            } catch (Exception para_e) {
                para_e.printStackTrace();
            }
        return stc_connect2Server;
    }

    public List<ImmediateInformation> getInformationInRetrofit(@NonNull String userId,
                                                                      @NonNull String timestamp) {
        Call<List<ImmediateInformation>> immediateInformationCall =
                serverInterface.postInformation(userId, timestamp);
        Response<List<ImmediateInformation>> response;
        List<ImmediateInformation> immediateInformations = new ArrayList<>();
        try {
            response = immediateInformationCall.execute();
            if (response.isSuccessful()) {
                immediateInformations = response.body();
            }
        } catch (IOException E) {
            E.printStackTrace();
        }
        return immediateInformations;
    }

    public List<ScholatHomework> getHomeworkFromServer(String userId) {
        List<ScholatHomework> lc_homeworks = new LinkedList<>();

        Call<List<ScholatHomework>> lc_homeworkCall = serverInterface.getHomework(userId);
        Response<List<ScholatHomework>> lc_response;
        try {
            lc_response = lc_homeworkCall.execute();
            if (lc_response.isSuccessful() && lc_response.body() != null) {
                Log.d("retrofit", "getHomeworkFromServer: success:");
                lc_homeworks.addAll(checkNotNull(lc_response.body()));
                return lc_homeworks;
            } else {
                String message = lc_response.message();
                Log.d("retrofit", "getHomeworkFromServer: message:" + message);
                String error = lc_response.errorBody().string();
                Log.d("retrofit", "getHomeworkFromServer: message:" + error);
                return null;
            }
        } catch (IOException para_e) {
            para_e.printStackTrace();
        }
        return null;
    }

    public String postCreateInformation(@NonNull String userId, @NonNull String information) {
        RequestBody lc_requestBody = RequestBody
                .create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), information);

        Call<String> lc_stringCall = serverInterface.postCreateInformation(lc_requestBody, userId);
        Response<String> lc_response = null;
        try {
            lc_response = lc_stringCall.execute();
        } catch (IOException para_e) {
            para_e.printStackTrace();
        }
        if (lc_response != null && lc_response.isSuccessful() && lc_response.body() != null)
            return lc_response.body();
        else return null;
    }

    public String postLogin(String account, String password) {
        String userId = "";
        Call<String> loginCall = serverInterface.postLogin(account, password);
        Response<String> response;
        try {
            response = loginCall.execute();
            String result = response.body();
            if (response.isSuccessful()) {
                if (result != null) {
                    Log.d("Retrofit", "postLogin: " + response.body().toString());
                    userId = result;
                } else
                    Log.d("Retrofit", "postLogin: " + "resultWrong");
            } else
                Log.d("Retrofit", "postLogin: " + "ResponseWrong" + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userId;
    }

    /**
     * 2018-03-24
     * lifumin
     *
     * @param registerId
     * @param account
     * @param password
     * @return 注册后台逻辑的实现
     */
    public boolean postRegister(String registerId, String account, String password) {
        boolean isRegisterSucceed = false;

        Call<String> registerCall = serverInterface.postRegister(registerId, account, password);
        Response<String> response;

        try {
            response = registerCall.execute();
            String result = response.body();
            if (response.isSuccessful()) {
                if (result.equals("111")) {
                    isRegisterSucceed = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isRegisterSucceed;
    }

    @Nullable
    public List<SDDetail> postSmallDataDetail(String id) {
        Call<List<SDDetail>> sdDetailCall = serverInterface.postSmallDataDetail(id);
        List<SDDetail> SDDetailList = new ArrayList<>();
        Response<List<SDDetail>> response;
        try {
            response = sdDetailCall.execute();
            if (response.isSuccessful()) {
                SDDetailList = response.body();
            } else
                return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SDDetailList;
    }


    public List<SDSummary> getSmallDataSummary(String time, int direction) {
        Call<List<SDSummary>> sdSummaryCall = serverInterface.getSmallDataSummary(time, direction);
        List<SDSummary> sdSummaryList = new ArrayList<>();
        Response<List<SDSummary>> response;
        try {
            response = sdSummaryCall.execute();
            if (response.isSuccessful()) {
                sdSummaryList = response.body();
            } else
                return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sdSummaryList;
    }

    public String postAnswer(String strEntity) {
        RequestBody description =
                RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
        Call<String> postAnswerCall = serverInterface.postAnswer(description);
        Response<String> response;
        String postResult = null;
        try {
            response = postAnswerCall.execute();
            if (response.isSuccessful()) {
                postResult = response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return postResult;
    }

    @android.support.annotation.Nullable
    public List<SDAnswer> getAnswers(String summaryId) {
        Call<List<SDAnswer>> lc_listCall = serverInterface.getAnswers(summaryId);
        List<SDAnswer> lc_sdAnswers = new ArrayList<>();
        Response<List<SDAnswer>> lc_listResponse;
        try {
            lc_listResponse = lc_listCall.execute();
            if (lc_listResponse.isSuccessful())
                lc_sdAnswers = lc_listResponse.body();
            else return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lc_sdAnswers;
    }

    public String postCreatedSD(String para_s, final File para_file) {
        RequestBody description =
                RequestBody.create(okhttp3.MediaType
                        .parse("application/json;charset=UTF-8"), para_s);

        RequestBody lc_requestFile =
                RequestBody.create(okhttp3.MediaType.parse("multipart/form-data"), para_file);
        MultipartBody.Part lc_part = MultipartBody.Part
                .createFormData("image", para_file.getName(), lc_requestFile);

        Call<String> lc_stringCall = serverInterface.postCreatedSD(description, lc_part);
        Response<String> lc_stringResponse;
        String postResult = null;
        try {
            lc_stringResponse = lc_stringCall.execute();
            if (lc_stringResponse.isSuccessful())
                postResult = lc_stringResponse.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return postResult;
    }

    public List<SDSummaryCreate> getCreatedSummaries() {
        Call<List<SDSummaryCreate>> lc_listCall = serverInterface.getCreatedSummaries();
        List<SDSummaryCreate> lc_sdSummaryCreates = new ArrayList<>();
        Response<List<SDSummaryCreate>> lc_listResponse;
        try {
            lc_listResponse = lc_listCall.execute();
            if (lc_listResponse.isSuccessful() && lc_listResponse.body() != null) {
                lc_sdSummaryCreates.addAll(checkNotNull(lc_listResponse.body()));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lc_sdSummaryCreates;
    }

    public List<SDDetail> getCreatedDetails(String id) {
        Call<List<SDDetail>> lc_listCall = serverInterface.getCreatedDetails(id);
        List<SDDetail> lc_sdDetails = null;

        Response<List<SDDetail>> lc_listResponse;
        try {
            lc_listResponse = lc_listCall.execute();
            if (lc_listResponse.isSuccessful() && lc_listResponse.body() != null)
                lc_sdDetails = new ArrayList<>(lc_listResponse.body());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lc_sdDetails;
    }

    public List<SDResult> getSDResult(String summaryId) {
        Call<List<SDResult>> lc_listCall = serverInterface.getSDResult(summaryId);
        List<SDResult> lc_sdResults = new ArrayList<>();
        Response<List<SDResult>> lc_listResponse;
        try {
            lc_listResponse = lc_listCall.execute();
            if (lc_listResponse.isSuccessful() && lc_listResponse.body() != null)
                lc_sdResults.addAll(checkNotNull(lc_listResponse.body()));
            Log.d("retrofit", "getSDResult: " + lc_sdResults.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lc_sdResults;
    }

    public void uploadAvatar(final String userId, final File para_avatar, final UserProfileContract.UploadAvatarCallback para_callback) {
        RequestBody lc_requestBody = RequestBody.create(MediaType.parse("image/jpg"), para_avatar);
        MultipartBody.Builder lc_builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        lc_builder.addFormDataPart("imgfile", para_avatar.getName(), lc_requestBody);
        //MultipartBody.Part upload = MultipartBody.Part.createFormData("imgfile",para_avatar.getName(),lc_requestBody);

        //RequestBody id  = RequestBody.create(MediaType.parse("multipart/form-data"),userId);
        Call<String> lc_call = serverInterface.postAvatar(lc_builder.build().part(0), userId);
        lc_call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                if (result != null && result.equals("success"))
                    para_callback.onAvatarUploaded();
                else
                    para_callback.onUploadedFailed();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                para_callback.onUploadedFailed();
                Log.d("retrofit", "onFailure: " + t.getStackTrace().toString());
            }
        });
    }

    public void uploadScholatInfo(final String id, final String scholatAccount, final String scholatPsw,
                                  final UserProfileContract.UploadScholatInfoCallback para_callback) {
        Call<String> lc_call = serverInterface.postScholatInfo(id, scholatAccount, scholatPsw);

        lc_call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                if (result != null && result.equals("success"))
                    para_callback.onScholatInfoUploaded();
                else
                    para_callback.onUploadedFailed();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                para_callback.onUploadedFailed();
            }
        });
    }

    public void uploadNickname(final String id, final String nickname,
                               final UserProfileContract.UploadNicknameCallback para_callback) {
        Call<String> lc_call = serverInterface.postNickname(id, nickname);
        lc_call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                if (result != null && result.equals("success"))
                    para_callback.onNicknameUploaded();
                else
                    para_callback.onUploadedFailed();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                para_callback.onUploadedFailed();
                Log.d("retrofit", "onFailure: " + t.getStackTrace().toString());
            }
        });
    }
}