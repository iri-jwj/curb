package jxpl.scnu.curb.data.repository;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jxpl.scnu.curb.data.local.InformationLocalDataSource;
import jxpl.scnu.curb.data.remote.InformationRemoteDataSource;
import jxpl.scnu.curb.immediateInformation.ImmediateInformation;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by irijw on 2017/10/13.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public class InformationRepository implements InformationDataSource{
    private static InformationRepository INSTANCE = null;
    private InformationLocalDataSource informationLocalDataSource;
    private InformationRemoteDataSource informationRemoteDataSource;

    private Map<String ,ImmediateInformation> cachedInfo;
    private boolean cachedIsDirty=false;
    private InformationRepository(@NonNull InformationLocalDataSource informationLocalDataSource,
                                  @NonNull InformationRemoteDataSource informationRemoteDataSource) {
        this.informationLocalDataSource = checkNotNull(informationLocalDataSource);
        this.informationRemoteDataSource = checkNotNull(informationRemoteDataSource);
    }

    public static InformationRepository getInstance(InformationLocalDataSource informationLocalDataSource,
                                                     InformationRemoteDataSource informationRemoteDataSource){
        if(INSTANCE==null)
            INSTANCE=new InformationRepository(informationLocalDataSource,informationRemoteDataSource);
        return INSTANCE;
    }

    public static void destoryInstance(){INSTANCE=null;}

    @Override
    public void getInformation(@NonNull final getInformationCallback callback,@NonNull String id) {
        checkNotNull(callback);
        //首先在缓存中查找是否存在，若不存在，再去数据库中查询
        if (cachedInfo.containsKey(id)){
            callback.onInformationLoaded(cachedInfo.get(id));
            return;
        }
        informationLocalDataSource.getInformation(new getInformationCallback() {
            @Override
            public void onInformationLoaded(ImmediateInformation immediateInformation) {
                callback.onInformationLoaded(immediateInformation);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        },id);
    }

    @Override
    public void getInformations(@NonNull final loadInformationCallback callback) {
        checkNotNull(callback);

        if (cachedInfo!=null&&!cachedIsDirty){
            callback.getInformationsLoaded(new ArrayList<>(cachedInfo.values()));
            return;
        }

        if (cachedIsDirty)
            getInformationFromRemote(callback);
        else{
            informationLocalDataSource.getInformations(new loadInformationCallback(){
                @Override
                public void getInformationsLoaded(List<ImmediateInformation> immediateInformations) {
                    refreshCached(immediateInformations);
                    callback.getInformationsLoaded(immediateInformations);
                }

                @Override
                public void onDataNotAvailable() {
                    getInformationFromRemote(callback);
                }
            });
        }
    }

    private void getInformationFromRemote(@NonNull final loadInformationCallback callback){
        informationRemoteDataSource.getInformations(new loadInformationCallback() {
            @Override
            public void getInformationsLoaded(List<ImmediateInformation> immediateInformations) {
                refreshCached(immediateInformations);
                saveInfoFromWeb(immediateInformations);
                callback.getInformationsLoaded(immediateInformations);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }
    private void refreshCached( List<ImmediateInformation> immediateInformations){
        checkNotNull(immediateInformations);
        if (cachedInfo!=null)
            cachedInfo.clear();
        else
            cachedInfo=new LinkedHashMap<>();
        for (ImmediateInformation i:
                immediateInformations) {
            cachedInfo.put(i.getId(),i);
        }
        cachedIsDirty=false;
    }

    @Override
    public void refreshInformation() {
        cachedIsDirty=true;
    }

    @Override
    public void addToArrangement(ImmediateInformation immediateInformation) {

    }

    @Override
    public void saveInfoFromWeb(List<ImmediateInformation> immediateInformations) {
        informationLocalDataSource.saveInfoFromWeb(immediateInformations);
    }
}