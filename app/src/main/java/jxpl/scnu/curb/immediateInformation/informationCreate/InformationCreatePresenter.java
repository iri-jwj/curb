package jxpl.scnu.curb.immediateInformation.informationCreate;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import jxpl.scnu.curb.data.repository.InformationDataSource;
import jxpl.scnu.curb.data.repository.InformationRepository;
import jxpl.scnu.curb.immediateInformation.ImmediateInformation;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * @author iri-jwj
 * @version 2
 * last update 2018/3/25
 * 将显示Information详细信息的功能改为了新建Information的功能
 */
public class InformationCreatePresenter implements InformationCreateContract.Presenter {
    private final InformationRepository informationRepository;
    private final InformationCreateContract.View infoDetailView;

    InformationCreatePresenter(@NonNull InformationRepository informationRepository,
                               @NonNull InformationCreateContract.View infoDetailView,
                               @NonNull Activity para_activity) {
        this.informationRepository = checkNotNull(informationRepository);
        this.infoDetailView = checkNotNull(infoDetailView);
        infoDetailView.setActivity(para_activity);
        infoDetailView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void commitInformation(ImmediateInformation para_information) {
        Gson lc_gson = new Gson();
        infoDetailView.setLoadingIndicator(true);
        String information = lc_gson.toJson(para_information);
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
        }, information, "123");
        //todo 替换这里的userid
    }

}
