package jxpl.scnu.curb.immediateInformation.informationCreate;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.util.Map;

import jxpl.scnu.curb.data.repository.InformationDataSource;
import jxpl.scnu.curb.data.repository.InformationRepository;
import jxpl.scnu.curb.immediateInformation.ImmediateInformation;
import jxpl.scnu.curb.utils.SharedHelper;
import jxpl.scnu.curb.utils.XmlDataStorage;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * @author iri-jwj
 * @version 2
 * update 2018/3/25
 * 将显示Information详细信息的功能改为了新建Information的功能
 * update 3/29
 * 链接XmlDataStorage，获取userId
 * @see XmlDataStorage#getUserInfo()
 */
public class InformationCreatePresenter implements InformationCreateContract.Presenter {
    private final InformationRepository informationRepository;
    private final InformationCreateContract.View infoDetailView;
    private final Activity m_activity;

    InformationCreatePresenter(@NonNull InformationRepository informationRepository,
                               @NonNull InformationCreateContract.View infoDetailView,
                               @NonNull Activity para_activity) {
        this.informationRepository = checkNotNull(informationRepository);
        this.infoDetailView = checkNotNull(infoDetailView);
        m_activity = para_activity;
        infoDetailView.setActivity(m_activity);
        infoDetailView.setPresenter(this);
    }

    @Override
    public void start() {
        //doNothing
    }

    @Override
    public void commitInformation(ImmediateInformation para_information) {
        Gson lc_gson = new Gson();
        infoDetailView.setLoadingIndicator(true);
        String information = lc_gson.toJson(para_information);
        if (!XmlDataStorage.isSharedHelperSet()) {
            SharedHelper lc_sharedHelper = SharedHelper.getInstance(m_activity);
            XmlDataStorage.setM_sharedHelper(lc_sharedHelper);
        }
        Map<String, String> lc_stringMap = XmlDataStorage.getUserInfo();
        informationRepository.postInformation(new InformationDataSource.PostInformationCallback() {

            @Override
            public void onInformationPosted() {
                infoDetailView.setLoadingIndicator(false);
                infoDetailView.showPostResult(true);
            }

            @Override
            public void onPostFailed() {
                infoDetailView.setLoadingIndicator(false);
                infoDetailView.showPostResult(false);
            }
        }, information, lc_stringMap.get(XmlDataStorage.USER_ID));
    }

}
