package jxpl.scnu.curb.data.repository;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jxpl.scnu.curb.data.local.InformationLocalDataSource;
import jxpl.scnu.curb.data.remote.InformationRemoteDataSource;
import jxpl.scnu.curb.immediateInformation.ImmediateInformation;
import jxpl.scnu.curb.utils.SharedHelper;
import jxpl.scnu.curb.utils.XmlDataStorage;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author iri-jwj
 * @version 2
 * last update 3.25
 * 更改了 {@link #getInformations(LoadInformationCallback, String, String)}
 * {@link #getInformationFromRemote(LoadInformationCallback, String, String)}
 * 的参数，新增用户id和时间的参数
 */

public class InformationRepository implements InformationDataSource {
    private static InformationRepository INSTANCE = null;
    private InformationLocalDataSource informationLocalDataSource;
    private InformationRemoteDataSource informationRemoteDataSource;

    private Map<UUID, ImmediateInformation> cachedInfo = new LinkedHashMap<>();
    private boolean cachedIsDirty;

    private InformationRepository(@NonNull InformationLocalDataSource informationLocalDataSource,
                                  @NonNull InformationRemoteDataSource informationRemoteDataSource,
                                  Context para_context) {
        this.informationLocalDataSource = checkNotNull(informationLocalDataSource);
        this.informationRemoteDataSource = checkNotNull(informationRemoteDataSource);
        if (!XmlDataStorage.isSharedHelperSet()) {
            XmlDataStorage.setM_sharedHelper(SharedHelper.getInstance(para_context));
        }
        cachedIsDirty = XmlDataStorage.getFirstRunState(XmlDataStorage.IS_INFO_FIRST_RUN);
    }

    public static InformationRepository getInstance(InformationLocalDataSource informationLocalDataSource,
                                                    InformationRemoteDataSource informationRemoteDataSource, Context para_context) {
        if (INSTANCE == null)
            INSTANCE = new InformationRepository(informationLocalDataSource, informationRemoteDataSource, para_context);
        return INSTANCE;
    }

//    public static void destoryInstance(){INSTANCE=null;}


    @Override
    public void getInformation(@NonNull final GetInformationCallback callback, UUID id) {
        checkNotNull(callback);
        //首先在缓存中查找是否存在，若不存在，再去数据库中查询
        if (cachedInfo.containsKey(id)) {
            callback.onInformationLoaded(cachedInfo.get(id));
            return;
        }
        informationLocalDataSource.getInformation(new GetInformationCallback() {
            @Override
            public void onInformationLoaded(ImmediateInformation immediateInformation) {
                callback.onInformationLoaded(immediateInformation);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        }, id);
    }


    @Override
    public void getInformations(@NonNull final LoadInformationCallback callback, final String userId,
                                final String timestamp) {
        checkNotNull(callback);

        if (!cachedInfo.isEmpty() && !cachedIsDirty) {
            callback.getInformationsLoaded(new LinkedList<>(cachedInfo.values()));
            return;
        }
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                getInformationFromRemote(callback, userId, timestamp);
            }
        });

        if (cachedIsDirty) {
            thread.start();
        } else {
            Log.d("InformationRepository", "getInformations-localdatasource: ");
            informationLocalDataSource.getInformations(new LoadInformationCallback() {
                @Override
                public void getInformationsLoaded(List<ImmediateInformation> immediateInformations) {
                    refreshCached(immediateInformations);
                    callback.getInformationsLoaded(immediateInformations);
                }

                @Override
                public void onDataNotAvailable() {
                    thread.start();
                }
            }, userId, timestamp);
        }
    }

    /**
     * 封装了从服务器获取最新information的代码
     *
     * @param callback  装载information的回调函数，
     *                  由{@link #getInformations(LoadInformationCallback, String, String)}直接传入
     * @param userId    用户Id
     * @param timestamp 用户执行获取操作的时间
     */
    private void getInformationFromRemote(@NonNull final LoadInformationCallback callback, String userId,
                                          String timestamp) {
        informationRemoteDataSource.getInformations(new LoadInformationCallback() {
            @Override
            public void getInformationsLoaded(List<ImmediateInformation> immediateInformations) {
                refreshCached(immediateInformations);
                saveInfoFromWeb(immediateInformations);
                callback.getInformationsLoaded(immediateInformations);
            }

            @Override
            public void onDataNotAvailable() {
                Log.d("InformationRepository", "onDataNotAvailable: ");
                callback.onDataNotAvailable();
            }
        }, userId, timestamp);
    }

    /**
     * 刷新当前的map缓存 {@link #cachedInfo}
     *
     * @param immediateInformations 用于刷新的Information List
     */
    private void refreshCached(List<ImmediateInformation> immediateInformations) {
        checkNotNull(immediateInformations);
        if (!cachedInfo.isEmpty())
            cachedInfo.clear();
        for (ImmediateInformation i :
                immediateInformations) {
            cachedInfo.put(i.getId(), i);
            Log.d("InformationRepository", "refreshCached: " + i.getTitle());
        }
        cachedIsDirty = false;
    }

    @Override
    public void refreshInformation() {
        cachedIsDirty = true;
    }

    @Override
    public void addToReminder(ImmediateInformation immediateInformation) {

    }

    @Override
    public void saveInfoFromWeb(List<ImmediateInformation> immediateInformations) {
        informationLocalDataSource.saveInfoFromWeb(immediateInformations);
    }

    @Override
    public void postInformation(final PostInformationCallback para_callback, final String information,
                                final String userId) {
        final Thread lc_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                informationRemoteDataSource.postInformation(para_callback, information, userId);
            }
        });
        lc_thread.start();
    }
}