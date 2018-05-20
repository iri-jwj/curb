package jxpl.scnu.curb.utils;

import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;

import jxpl.scnu.curb.R;
import jxpl.scnu.curb.data.local.InformationLocalDataSource;
import jxpl.scnu.curb.data.local.SmallDataLocalDataSource;
import jxpl.scnu.curb.data.remote.InformationRemoteDataSource;
import jxpl.scnu.curb.data.remote.SDRemoteDataSource;
import jxpl.scnu.curb.data.repository.InformationRepository;
import jxpl.scnu.curb.data.repository.ScholatRepository;
import jxpl.scnu.curb.data.repository.SmallDataRepository;
import jxpl.scnu.curb.homePage.immediateInformation.InformationFragment;
import jxpl.scnu.curb.homePage.immediateInformation.InformationPresenter;
import jxpl.scnu.curb.homePage.river.RiverFragment;
import jxpl.scnu.curb.homePage.scholat.ScholatFragment;
import jxpl.scnu.curb.homePage.scholat.ScholatPresenter;
import jxpl.scnu.curb.homePage.smallData.SmallDataFragment;
import jxpl.scnu.curb.homePage.smallData.SmallDataPresenter;

/**
 * created on ${date}
 *
 * @author iri-jwj
 * @version 1 init
 */
public class FragmentFactory {
    private static final FragmentFactory ourInstance = new FragmentFactory();
    private SparseBooleanArray isFragmentCreated = new SparseBooleanArray();

    private FragmentFactory() {
    }

    public static FragmentFactory getInstance() {
        return ourInstance;
    }

    /*    public Fragment getFragment(Context para_context){

        }*/
    public void showFragmentInHomePage(AppCompatActivity para_activity, int frameId, int fragmentId) {
        ActivityUtil.setFragmentManagerInHome(para_activity.getSupportFragmentManager());
        ActivityUtil.setContainerView(frameId);
        switch (fragmentId) {
            case R.id.nav_info:
                if (!isFragmentCreated.get(R.id.nav_info)) {
                    InformationFragment lc_informationFragment = InformationFragment.newInstance();
                    new InformationPresenter(InformationRepository.getInstance(InformationLocalDataSource.getInstance(para_activity),
                            InformationRemoteDataSource.getInstance(), para_activity), lc_informationFragment, para_activity);
                    ActivityUtil.addFragmentInHomePage(R.id.nav_info, lc_informationFragment);
                    ActivityUtil.setCurrentFragment(R.id.nav_info);
                    isFragmentCreated.put(R.id.nav_info, true);
                    break;
                }
                ActivityUtil.setCurrentFragment(R.id.nav_info);
                break;
            case R.id.nav_scholat:
                if (!isFragmentCreated.get(R.id.nav_scholat)) {
                    ScholatFragment lc_scholatFragment = ScholatFragment.newInstance();
                    new ScholatPresenter(lc_scholatFragment, ScholatRepository.getInstance(para_activity), para_activity);
                    ActivityUtil.addFragmentInHomePage(R.id.nav_scholat, lc_scholatFragment);
                    ActivityUtil.setCurrentFragment(R.id.nav_scholat);
                    isFragmentCreated.put(R.id.nav_scholat, true);
                    break;
                }
                ActivityUtil.setCurrentFragment(R.id.nav_scholat);
                break;
            case R.id.nav_small_data:
                if (!isFragmentCreated.get(R.id.nav_small_data)) {
                    SmallDataFragment lc_smallDataFragment = SmallDataFragment.newInstance();
                    new SmallDataPresenter(lc_smallDataFragment, SmallDataRepository.getInstance(SDRemoteDataSource.getInstance(),
                            SmallDataLocalDataSource.getInstance(para_activity), para_activity), para_activity);
                    ActivityUtil.addFragmentInHomePage(R.id.nav_small_data, lc_smallDataFragment);
                    ActivityUtil.setCurrentFragment(R.id.nav_small_data);
                    isFragmentCreated.put(R.id.nav_small_data, true);
                    break;
                }
                ActivityUtil.setCurrentFragment(R.id.nav_small_data);
                break;
            case R.id.nav_river:
                if (!isFragmentCreated.get(R.id.nav_river)) {
                    RiverFragment lc_riverFragment = RiverFragment.newInstance();
                    //todo 添加river的PRESENTER
                    ActivityUtil.addFragmentInHomePage(R.id.nav_river, lc_riverFragment);
                    ActivityUtil.setCurrentFragment(R.id.nav_river);
                    isFragmentCreated.put(R.id.nav_river, true);
                    break;
                }
                ActivityUtil.setCurrentFragment(R.id.nav_river);
                break;
        }
    }

}
