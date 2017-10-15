package jxpl.scnu.curb.immediateInformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by irijw on 2017/9/25.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public class InformationPresenter implements InformationContract.Presenter {

    private InformationFilter currentFilter;

    @Override
    public void start(){
        loadInformation(false);
    }

    @Override
    public List<ImmediateInformation> getInformationFromRepository(){
        List<ImmediateInformation> ImmediateInformations;
        ImmediateInformations =new ArrayList<>();

        return ImmediateInformations;
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

    }

    @Override
    public void openInformationDetails(){

    }
}
