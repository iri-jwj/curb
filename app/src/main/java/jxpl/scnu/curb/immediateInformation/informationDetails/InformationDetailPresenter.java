package jxpl.scnu.curb.immediateInformation.informationDetails;

import android.support.annotation.NonNull;

import com.google.common.base.Strings;

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
    private String infoId;

    private final InformationRepository informationRepository;
    private final InformationDetailContract.View infoDetailView;

     InformationDetailPresenter(String infoId, @NonNull InformationRepository informationRepository,
                               @NonNull InformationDetailContract.View infoDetailView){
        this.infoId=infoId;
        this.informationRepository=checkNotNull(informationRepository);
        this.infoDetailView=checkNotNull(infoDetailView);
        infoDetailView.setPresenter(this);
    }
    @Override
    public void start() {
        openInfo();
    }

    @Override
    public void openInfo() {
         if (Strings.isNullOrEmpty(infoId)){
             infoDetailView.showMissingInfo();
             return;
         }

         infoDetailView.setLoadingIndicator(true);
         informationRepository.getInformation(new InformationDataSource.getInformationCallback() {
             @Override
             public void onInformationLoaded(ImmediateInformation immediateInformation) {
                 if (infoDetailView.isActive())
                     return;

                 infoDetailView.setLoadingIndicator(false);
                 if (immediateInformation==null)
                     infoDetailView.showMissingInfo();
                 else
                     showInfo(immediateInformation);
             }

             @Override
             public void onDataNotAvailable() {
                 if (infoDetailView.isActive())
                     return;
                 infoDetailView.setLoadingIndicator(false);
                 infoDetailView.showMissingInfo();
             }
         },infoId);
    }

    private void showInfo(ImmediateInformation immediateInformation){

         infoDetailView.showInfo(checkNotNull(immediateInformation));
    }
}
