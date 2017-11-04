package jxpl.scnu.curb.homePage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.data.local.InformationLocalDataSource;
import jxpl.scnu.curb.data.remote.InformationRemoteDataSource;
import jxpl.scnu.curb.data.repository.InformationRepository;
import jxpl.scnu.curb.immediateInformation.InformationFilter;
import jxpl.scnu.curb.immediateInformation.InformationFragment;
import jxpl.scnu.curb.immediateInformation.InformationPresenter;
import jxpl.scnu.curb.utils.ActivityUtil;

public class HomePageActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.home_page_frame)
    FrameLayout homePageFrame;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.home_page_drawer)
    DrawerLayout homePageDrawer;

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    private InformationFragment informationFragment;
    private InformationPresenter informationPresenter;

    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(homePageDrawer);
        unbinder=ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.home);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        informationFragment=(InformationFragment) getSupportFragmentManager().findFragmentById(R.id.home_page_frame) ;

        //菜单项目点击事件
        navView.setCheckedItem(R.id.nav_info);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                changeFragment(item);
                return false;
            }
        });

        if (savedInstanceState != null) {
            InformationFilter currentFiltering =
                    (InformationFilter) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            informationPresenter.setFiltering(currentFiltering);
        }

    }

    //工具栏点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                homePageDrawer.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeFragment(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_info:
                if(informationFragment==null)
                    informationFragment=InformationFragment.newInstance();
                ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),informationFragment,R.id.home_page_frame);
                informationPresenter=new InformationPresenter(InformationRepository.getInstance(InformationLocalDataSource.getInstace(this),
                        InformationRemoteDataSource.getInstance()),informationFragment);
                break;
            case R.id.nav_river:
                break;
            case R.id.nav_small_data:
                break;
            case R.id.nav_arrange:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_concerned_topic:
                break;
            case R.id.nav_remind:
                break;
            case R.id.nav_setting:
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putSerializable(CURRENT_FILTERING_KEY,informationPresenter.getFiltering());
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        unbinder.unbind();
    }
}