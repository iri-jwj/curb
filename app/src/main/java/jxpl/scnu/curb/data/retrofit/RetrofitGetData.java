package jxpl.scnu.curb.data.retrofit;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import jxpl.scnu.curb.immediateInformation.ImmediateInformation;
import jxpl.scnu.curb.smallData.SDAnswer;
import jxpl.scnu.curb.smallData.SDDetail;
import jxpl.scnu.curb.smallData.SDResult;
import jxpl.scnu.curb.smallData.SDSummary;
import jxpl.scnu.curb.smallData.SDSummaryCreate;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 建立网络连接、发送接收数据
 * 尽量不执行数据的处理操作*/
public class RetrofitGetData {
    private static Retrofit retrofit = new Retrofit.Builder().baseUrl("http://39.108.105.150:8080")
            .addConverterFactory(GsonConverterFactory.create()).build();
    private static ServerInterface serverInterface = retrofit.create(ServerInterface.class);

    public static List<ImmediateInformation> getDataFromWeb() {
        Call<List<ImmediateInformation>> immediateInformationCall = serverInterface.getInfoFromServer();
        Response<List<ImmediateInformation>> response;
        List<ImmediateInformation> immediateInformations = new ArrayList<>();
        try {
            response = immediateInformationCall.execute();
            if (response.isSuccessful()) {
                immediateInformations = response.body();
                Log.d("RESPONSE","SUCCEED"+immediateInformations.size());
                Log.d("RESPONSE",immediateInformations.get(1).getTitle());
            } else {
                Log.d("RESPONSE","FAIL");
            }
        } catch (IOException E) {
            E.printStackTrace();
        }
        return immediateInformations;
    }

    public static boolean postLogin(String account, String password) {
        boolean isLoginSucceed = false;
        Call<String> loginCall = serverInterface.postLogin(5, account, password);
        Response<String> response;
        try {
            response = loginCall.execute();
            String result = response.body();
            if (response.isSuccessful()) {
                if (result.equals("111")) {
                    Log.d("Retrofit", "postLogin: " + response.body().toString());
                    isLoginSucceed = true;
                } else
                    Log.d("Retrofit", "postLogin: " + "resultWrong");
            } else
                Log.d("Retrofit", "postLogin: " + "ResponseWrong" + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isLoginSucceed;
    }

    @Nullable
    public static List<SDDetail> postSmallDataDetail(String id) {
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

    @android.support.annotation.Nullable
    public static List<SDSummary> getSmallDataSummary(String time, int direction) {
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

    public static String postAnswer(String strEntity) {
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
    public static List<SDAnswer> getAnswers(String summaryId) {
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

    public static String postCreatedSD(String para_s) {
        RequestBody description =
                RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), para_s);
        Call<String> lc_stringCall = serverInterface.postCreatedSD(description);
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

    public static List<SDSummaryCreate> getCreatedSummaries() {
        Call<List<SDSummaryCreate>> lc_listCall = serverInterface.getCreatedSummaries();
        List<SDSummaryCreate> lc_sdSummaryCreates = null;
        Response<List<SDSummaryCreate>> lc_listResponse;
        try {
            lc_listResponse = lc_listCall.execute();
            if (lc_listResponse.isSuccessful() && !lc_listResponse.body().isEmpty()) {
                lc_sdSummaryCreates = new ArrayList<>(lc_listResponse.body());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lc_sdSummaryCreates;
    }

    public static List<SDDetail> getCreatedDetails(String id) {
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

    public static List<SDResult> getSDResult(String summaryId) {
        Call<List<SDResult>> lc_listCall = serverInterface.getSDResult(summaryId);
        List<SDResult> lc_sdResults = null;
        Response<List<SDResult>> lc_listResponse;
        try {
            lc_listResponse = lc_listCall.execute();
            if (lc_listResponse.isSuccessful() && lc_listResponse.body() != null)
                lc_sdResults = new ArrayList<>(lc_listResponse.body());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lc_sdResults;
    }
}