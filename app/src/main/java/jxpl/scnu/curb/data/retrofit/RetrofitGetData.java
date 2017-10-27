package jxpl.scnu.curb.data.retrofit;

import java.util.ArrayList;
import java.util.List;

import jxpl.scnu.curb.immediateInformation.ImmediateInformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by irijw on 2017/10/24.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public class RetrofitGetData {
    private static Retrofit retrofit;
    private static List<ImmediateInformation> immediateInformations=new ArrayList<>();

    public static List<ImmediateInformation> getDataFromWeb(){
        retrofit=new Retrofit.Builder().baseUrl("")
                .addConverterFactory(GsonConverterFactory.create()).build();
        GetInformationInterface getInformationInterface=retrofit.create(GetInformationInterface.class);
        Call<List<ImmediateInformation>> immediateInformationCall=getInformationInterface.getCall();

        immediateInformationCall.enqueue(new Callback<List<ImmediateInformation>>() {
            @Override
            public void onResponse(Call<List<ImmediateInformation>> call, Response<List<ImmediateInformation>> response) {
                if (response.isSuccessful()){
                    immediateInformations=response.body();
                }
            }
            @Override
            public void onFailure(Call<List<ImmediateInformation>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
        return immediateInformations;
    }
}
