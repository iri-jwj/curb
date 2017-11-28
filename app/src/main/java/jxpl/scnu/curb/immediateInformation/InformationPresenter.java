package jxpl.scnu.curb.immediateInformation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import jxpl.scnu.curb.data.repository.InformationDataSource;
import jxpl.scnu.curb.data.repository.InformationRepository;

import static com.google.common.base.Preconditions.checkNotNull;
import static jxpl.scnu.curb.immediateInformation.InformationFilter.ALL_INFORMATIONS;

/**
 * Created by irijw on 2017/9/25.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public class InformationPresenter implements InformationContract.Presenter {

    private InformationFilter currentFilter=ALL_INFORMATIONS;
    private final InformationRepository informationRepository;
    private final InformationContract.View informationView;
    private boolean firstLoad=true;
    public InformationPresenter(@NonNull InformationRepository informationRepository, InformationContract.View informationView){
        this.informationRepository=checkNotNull(informationRepository);
        this.informationView=checkNotNull(informationView);
        informationView.setPresenter(this);
    }

    @Override
    public void start(){
        loadInformation(false);
    }

    @Override
    public void getInformationFromRepository(boolean forceUpdate){
        final List<ImmediateInformation> informationToShow=new ArrayList<>();

        informationRepository.getInformations(new InformationDataSource.loadInformationCallback() {
            @Override
            public void getInformationsLoaded(List<ImmediateInformation> immediateInformationsFromCB) {
                for (ImmediateInformation i:
                        immediateInformationsFromCB) {
                    Log.d("getInfoFromR",i.getTitle());
                    switch (currentFilter){
                        case ALL_INFORMATIONS:
                            informationToShow.add(i);
                            Log.d("getInfoIsSucceed?","YES");
                            break;
                        case EDU_INFORMATIONS:
                            if(i.getType().equals("EDU_INFORMATIONS")){
                                informationToShow.add(i);
                            }
                            break;
                        case PRA_INFORMATIONS:
                            if(i.getType().equals("PRA_INFORMATIONS")){
                                informationToShow.add(i);
                            }
                            break;
                        case SCHOLAR_INFORMATIONS:
                            if(i.getType().equals("SCHOLAR_INFORMATIONS")){
                                informationToShow.add(i);
                            }
                            break;
                        case WORK_STUDY_INFORMATIONS:
                            if (i.getType().equals("WORK_STUDY_INFORMATIONS"))
                                informationToShow.add(i);
                            break;
                        default:informationToShow.add(i);
                            break;
                    }
                }
                processInfo(informationToShow);
            }

            @Override
            public void onDataNotAvailable() {
                if (!informationView.isActive())
                    return;
                informationView.showLoadingError();
                Log.d("getInfoFromR","failed");
            }
        });

    }

    @Override
    public void setFiltering(InformationFilter filtering){
        currentFilter=filtering;
    }

    @Override
    public InformationFilter getFiltering(){
        return currentFilter;
    }

    @Override
    public void loadInformation(boolean forceUpdate){
        loadInformation(forceUpdate||firstLoad,true);
        firstLoad=false;
    }

    private void loadInformation(boolean forceUpdate,final boolean showLoadingIndicator){
        if(showLoadingIndicator){
            informationView.setLoadingIndicator(true);
        }
        if (forceUpdate){
            informationRepository.refreshInformation();
        }

        getInformationFromRepository(forceUpdate);

        informationView.setLoadingIndicator(false);
    }

    private void processInfo(List<ImmediateInformation> immediateInformations){
        if (immediateInformations.isEmpty()){
            informationView.showNoInfo();
        }else{
            informationView.showInfo(immediateInformations);
        }
    }

    @Override
    public void openInformationDetails(@NonNull ImmediateInformation immediateInformation,@NonNull Context context){
        checkNotNull(immediateInformation);
        checkNotNull(context);
        informationView.showInformationDetailsUi(immediateInformation.getId(),context);
    }

}
