package jxpl.scnu.curb.data.repository;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jxpl.scnu.curb.data.local.SmallDataLocalDataSource;
import jxpl.scnu.curb.data.remote.SDRemoteDataSource;
import jxpl.scnu.curb.smallData.SDAnswer;
import jxpl.scnu.curb.smallData.SDDetail;
import jxpl.scnu.curb.smallData.SDSummary;
import jxpl.scnu.curb.smallData.SDSummaryCreate;
import jxpl.scnu.curb.utils.SharedHelper;
import jxpl.scnu.curb.utils.XmlDataStorage;


public class SmallDataRepository implements SmallDataDataSource {
    private static SmallDataRepository stc_smallDataRepository;
    private SDRemoteDataSource m_sdRemoteDataSource;
    private SmallDataLocalDataSource m_smallDataLocalDataSource;

    private Map<UUID, SDSummary> m_summaryMap = new LinkedHashMap<>();
    private boolean isSummaryCacheDirty = true;

    private Map<UUID, SDSummaryCreate> m_summaryCreateMap = new LinkedHashMap<>();
    private boolean summaryCreateCache;

    private SmallDataRepository(@NonNull SDRemoteDataSource para_sdRemoteDataSource,
                                @NonNull SmallDataLocalDataSource para_smallDataLocalDataSource,
                                @NonNull Context para_context) {
        m_sdRemoteDataSource = para_sdRemoteDataSource;
        m_smallDataLocalDataSource = para_smallDataLocalDataSource;
        if (!XmlDataStorage.isSharedHelperSet()) {
            XmlDataStorage.setM_sharedHelper(SharedHelper.getInstance(para_context));
        }
        isSummaryCacheDirty = XmlDataStorage.getFirstRunState(XmlDataStorage.IS_SD_FIRST_RUN);
    }

    public static SmallDataRepository getInstance(@NonNull SDRemoteDataSource para_sdRemoteDataSource,
                                                  @NonNull SmallDataLocalDataSource para_smallDataLocalDataSource,
                                                  @NonNull Context para_context) {
        if (stc_smallDataRepository == null)
            stc_smallDataRepository = new SmallDataRepository(para_sdRemoteDataSource,
                    para_smallDataLocalDataSource, para_context);
        return stc_smallDataRepository;
    }

    @Override
    public void getSummary(@NonNull final getSummaryCallback callback, String id) {
        UUID lc_uuid = UUID.fromString(id);
        if (m_summaryMap.containsKey(lc_uuid)) {
            callback.onSummaryGot(m_summaryMap.get(lc_uuid));
        } else {
            m_smallDataLocalDataSource.getSummary(new getSummaryCallback() {
                @Override
                public void onSummaryGot(SDSummary para_sdSummary) {
                    callback.onSummaryGot(para_sdSummary);
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            }, id);
        }
    }

    @Override
    public void loadDetails(@NonNull final loadDetailCallback callback, final String summaryId, final Context para_context) {
        final Thread lc_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                m_sdRemoteDataSource.loadDetails(new loadDetailCallback() {
                    @Override
                    public void onDetailLoaded(List<SDDetail> para_sdDetails) {
                        callback.onDetailLoaded(para_sdDetails);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                }, summaryId, para_context);
            }
        });

        m_smallDataLocalDataSource.loadDetails(new loadDetailCallback() {
            @Override
            public void onDetailLoaded(List<SDDetail> para_sdDetails) {
                callback.onDetailLoaded(para_sdDetails);
            }

            @Override
            public void onDataNotAvailable() {
                //todo 判断网络是否可用
                lc_thread.start();
            }
        }, summaryId, para_context);
    }

    /**
     * @param callback  获取的回调函数
     * @param time      执行获取操作的时间
     * @param direction 获取的方向，分为 0：不向网络获取 1：向网络获取最新的 2：向历史获取5个
     */
    @Override
    public void loadSummaries(@NonNull final loadSummaryCallback callback,
                              final String time, final int direction, Context para_context) {
        if (!isSummaryCacheDirty && !m_summaryMap.isEmpty()) {
            callback.onSummaryLoaded(new ArrayList<>(m_summaryMap.values()));
            return;
        }
        if (isSummaryCacheDirty)
            getSDSummariesFromWeb(callback, time, direction, para_context);
        else {
            m_smallDataLocalDataSource.loadSummaries(new loadSummaryCallback() {
                @Override
                public void onSummaryLoaded(List<SDSummary> sdSummaries) {
                    refreshSummaryCache(sdSummaries);
                    callback.onSummaryLoaded(sdSummaries);
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            }, time, direction, para_context);
        }
    }

    /**
     * 封装开辟线程获取summaries的方法
     *
     * @param para_loadSummaryCallback 回调参数，由调用方法传入
     * @param time                     时间参数，由调用方法传入
     * @param direction                选择参数，由调用方法传入
     */
    private void getSDSummariesFromWeb(@NonNull final loadSummaryCallback para_loadSummaryCallback,
                                       final String time, final int direction, final Context para_context) {
        final Thread lc_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                m_sdRemoteDataSource.loadSummaries(new loadSummaryCallback() {
                    @Override
                    public void onSummaryLoaded(List<SDSummary> sdSummaries) {
                        refreshSummaryCache(sdSummaries);
                        saveSummariesToLocal(sdSummaries);
                        para_loadSummaryCallback.onSummaryLoaded(sdSummaries);

                    }

                    @Override
                    public void onDataNotAvailable() {
                        para_loadSummaryCallback.onDataNotAvailable();
                    }
                }, time, direction, para_context);
            }
        });
        lc_thread.start();
    }

    /**
     * 更新缓存中的数据
     */
    private void refreshSummaryCache(List<SDSummary> para_sdSummaries) {
        if (!m_summaryMap.isEmpty())
            m_summaryMap.clear();
        for (SDSummary lc_sdSummary :
                para_sdSummaries) {
            m_summaryMap.put(lc_sdSummary.getId(), lc_sdSummary);
        }
        isSummaryCacheDirty = false;
    }

    private void refreshCreatedSummaryCache(List<SDSummaryCreate> para_sdSummaryCreates) {
        if (!m_summaryCreateMap.isEmpty())
            m_summaryCreateMap.clear();
        for (SDSummaryCreate lc_sdSummaryCreate :
                para_sdSummaryCreates) {
            m_summaryCreateMap.put(lc_sdSummaryCreate.getId(), lc_sdSummaryCreate);
        }
        summaryCreateCache = false;
    }

    @Override
    public void refreshSummaries() {
        isSummaryCacheDirty = true;
    }

    @Override
    public void refreshCreatedSummaries() {
        summaryCreateCache = true;
    }

    @Override
    public void getCreatedSummary(@NonNull final getCreatedSummaryCallback
                                          para_getCreatedSummaryCallback,
                                  final String id) {
        m_smallDataLocalDataSource.getCreatedSummary(new getCreatedSummaryCallback() {
            @Override
            public void onCreatedSummaryGot(SDSummaryCreate para_sdSummaryCreates) {
                para_getCreatedSummaryCallback.onCreatedSummaryGot(para_sdSummaryCreates);
            }

            @Override
            public void onDataNotAvailable() {
                m_sdRemoteDataSource.getCreatedSummary(new getCreatedSummaryCallback() {
                    @Override
                    public void onCreatedSummaryGot(SDSummaryCreate para_sdSummaryCreates) {
                        para_getCreatedSummaryCallback.onCreatedSummaryGot(para_sdSummaryCreates);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        para_getCreatedSummaryCallback.onDataNotAvailable();
                    }
                }, id);
            }
        }, id);
    }

    @Override
    public void loadCreatedSummaries(@NonNull final loadCreatedSummariesCallback
                                             para_loadCreatedSummariesCallback, final Context para_context) {
        if (!summaryCreateCache && !m_summaryCreateMap.isEmpty()) {
            para_loadCreatedSummariesCallback.onCreatedSummariesLoaded(
                    new ArrayList<>(m_summaryCreateMap.values()));
            return;
        }
        if (summaryCreateCache) {
            final Thread lc_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    m_sdRemoteDataSource.loadCreatedSummaries(new loadCreatedSummariesCallback() {
                        @Override
                        public void onCreatedSummariesLoaded(List<SDSummaryCreate>
                                                                     para_summaryCreates) {

                            refreshCreatedSummaryCache(para_summaryCreates);
                            para_loadCreatedSummariesCallback.
                                    onCreatedSummariesLoaded(para_summaryCreates);
                        }

                        @Override
                        public void onDataNotAvailable() {
                            para_loadCreatedSummariesCallback.onDataNotAvailable();
                        }
                    }, para_context);
                }
            });
            lc_thread.start();
        } else {
            m_smallDataLocalDataSource.loadCreatedSummaries(new loadCreatedSummariesCallback() {
                @Override
                public void onCreatedSummariesLoaded(List<SDSummaryCreate> sdSummaries) {
                    refreshCreatedSummaryCache(sdSummaries);
                    para_loadCreatedSummariesCallback.onCreatedSummariesLoaded(sdSummaries);
                }

                @Override
                public void onDataNotAvailable() {
                    para_loadCreatedSummariesCallback.onDataNotAvailable();
                }
            }, para_context);
        }
    }

    @Override
    public void loadCreatedDetails(@NonNull final loadCreatedDetailsCallback para_loadCreatedDetailsCallback,
                                   final String para_summaryId, final Context para_context) {
        final Thread lc_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                m_sdRemoteDataSource.loadCreatedDetails(new loadCreatedDetailsCallback() {
                    @Override
                    public void onCreatedDetailsLoaded(List<SDDetail> para_sdDetails) {
                        para_loadCreatedDetailsCallback.onCreatedDetailsLoaded(para_sdDetails);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        // todo 添加错误显示
                        para_loadCreatedDetailsCallback.onDataNotAvailable();
                    }
                }, para_summaryId, para_context);
            }
        });
        m_smallDataLocalDataSource.loadDetails(new loadDetailCallback() {
            @Override
            public void onDetailLoaded(List<SDDetail> para_sdDetails) {
                para_loadCreatedDetailsCallback.onCreatedDetailsLoaded(para_sdDetails);
            }

            @Override
            public void onDataNotAvailable() {
                //todo 判断网络是否可用
                lc_thread.start();
            }
        }, para_summaryId, para_context);
    }

    @Override
    public void loadAnswers(@NonNull final loadAnswersCallback para_loadAnswersCallback, final String summaryId, final Context para_context) {
        m_smallDataLocalDataSource.loadAnswers(new loadAnswersCallback() {
            @Override
            public void onAnswerLoaded(List<SDAnswer> para_sdAnswers) {
                para_loadAnswersCallback.onAnswerLoaded(para_sdAnswers);
            }

            @Override
            public void onDataNotAvailable() {
                final Thread lc_thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        m_sdRemoteDataSource.loadAnswers(new loadAnswersCallback() {
                            @Override
                            public void onAnswerLoaded(List<SDAnswer> para_sdAnswers) {
                                para_loadAnswersCallback.onAnswerLoaded(para_sdAnswers);
                            }

                            @Override
                            public void onDataNotAvailable() {
                                para_loadAnswersCallback.onDataNotAvailable();
                            }
                        }, summaryId, para_context);
                    }
                });
                lc_thread.start();
            }
        }, summaryId, para_context);
    }

    @Override
    public void saveAnswersToLocal(final List<SDAnswer> para_sdAnswers) {
        final Thread lc_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                m_smallDataLocalDataSource.saveAnswersToLocal(para_sdAnswers);
            }
        });
        lc_thread.start();
    }

    @Override
    public void markAnswered(String summaryId) {
        Log.d("sdr", "markAnswered:id: " + summaryId);
        m_smallDataLocalDataSource.markAnswered(summaryId);
        UUID lc_uuid = UUID.fromString(summaryId);
        SDSummary lc_sdSummary = m_summaryMap.get(lc_uuid);
        Log.d("sdr", "markAnswered:lc_sdSummary: " + (lc_sdSummary == null));
        lc_sdSummary.setHasFinish(true);
        m_summaryMap.put(lc_uuid, lc_sdSummary);
    }

    @Override
    public void saveSummariesToLocal(final List<SDSummary> sdSummaries) {
        final Thread lc_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                m_smallDataLocalDataSource.saveSummariesToLocal(sdSummaries);
            }
        });
        lc_thread.start();
    }

    @Override
    public void saveDetailsToLocal(final List<SDDetail> para_sdDetails) {
        final Thread lc_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                m_smallDataLocalDataSource.saveDetailsToLocal(para_sdDetails);
            }
        });
        lc_thread.start();
    }

    @Override
    public void saveCreatedSDToLocal(final SDSummaryCreate para_sdSummaryCreate,
                                     final List<SDDetail> para_sdDetails) {
        final Thread lc_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    m_smallDataLocalDataSource.saveCreatedSDToLocal(para_sdSummaryCreate, para_sdDetails);
                } catch (Exception para_e) {
                    para_e.printStackTrace();
                }
            }
        });
        lc_thread.start();
    }

    @Override
    public void saveCreatedSDToRemote(final String para_s, final File image, final Context para_context) {
        final Thread lc_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                m_sdRemoteDataSource.saveCreatedSDToRemote(para_s, image, para_context);
            }
        });
        lc_thread.start();

    }

    @Override
    public void commitAnswer(final String strEntity, final Context para_context) {
        final Thread lc_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                m_sdRemoteDataSource.commitAnswer(strEntity, para_context);
            }
        });
        lc_thread.start();
    }

    @Override
    public void loadResult(final loadResultCallback para_loadResultCallback,
                           final String summaryId, final Context para_context) {
        final Thread lc_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                m_sdRemoteDataSource.loadResult(para_loadResultCallback, summaryId, para_context);
            }
        });
        lc_thread.start();
    }
}
