package jxpl.scnu.curb.immediateInformation.informationDetails;

import android.support.annotation.NonNull;
import android.util.Log;

import jxpl.scnu.curb.data.repository.InformationDataSource;
import jxpl.scnu.curb.data.repository.InformationRepository;
import jxpl.scnu.curb.immediateInformation.ImmediateInformation;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by irijw on 2017/9/8.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public class InformationDetailPresenter implements InformationDetailContract.Presenter {
    private final InformationRepository informationRepository;
    private final InformationDetailContract.View infoDetailView;
    private int infoId;
    private String infoTitle = "";

    InformationDetailPresenter(int infoId, @NonNull InformationRepository informationRepository,
                               @NonNull InformationDetailContract.View infoDetailView) {
        this.infoId = infoId;
        this.informationRepository = checkNotNull(informationRepository);
        this.infoDetailView = checkNotNull(infoDetailView);
        infoDetailView.setPresenter(this);
    }

    @Override
    public void start() {
        openInfo();
    }

    @Override
    public void openInfo() {
        if (infoId == 0) {
            infoDetailView.showMissingInfo();
            Log.d("detailPresenter", "openInfo: checkInfoIdIfNull" + infoId);
            return;
        }
        infoDetailView.setLoadingIndicator(true);
        informationRepository.getInformation(new InformationDataSource.getInformationCallback() {
            @Override
            public void onInformationLoaded(ImmediateInformation immediateInformation) {
                if (!infoDetailView.isActive())
                    return;
                infoDetailView.setLoadingIndicator(false);
                if (immediateInformation == null) {
                    infoDetailView.showMissingInfo();
                    Log.d("detailPresenter", "onInformationLoaded: null");
                }
                else {
                    showInfo(immediateInformation);
                    infoTitle = immediateInformation.getTitle();
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (!infoDetailView.isActive())
                    return;
                infoDetailView.setLoadingIndicator(false);
                infoDetailView.showMissingInfo();
                Log.d("detailPresenter", "onDataNotAvailable: getInfoFail");
            }
        }, infoId);
    }


    String getInfoTitle() {
        return infoTitle;
    }

    private void showInfo(ImmediateInformation immediateInformation) {
        infoDetailView.showInfo(checkNotNull(immediateInformation));
    }
}
