package jxpl.scnu.curb.homePage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        unbinder=ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.home);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        homePageDrawer.setStatusBarBackground(R.color.toolsBar);

        //菜单项目点击事件
        setupDrawerContent(navView);
//        informationFragment=(InformationFragment) getSupportFragmentManager().findFragmentById(R.id.home_page_frame) ;
//        if (informationFragment == null){
//            informationFragment = InformationFragment.newInstance();
//            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(), informationFragment, R.id.home_page_frame);
//        }
        ActivityUtil.setContainerView(R.id.home_page_frame);
        ActivityUtil.setFragmentManager(getSupportFragmentManager());
        if(informationFragment==null){
            informationFragment=InformationFragment.newInstance();
            Log.d("HomePageCreateInfoFrag", "onCreate:infoFrag "+informationFragment.isAdded());
            ActivityUtil.addFragment(R.id.nav_info,informationFragment);
        }
        ActivityUtil.setCurrentFragment(R.id.nav_info);
        informationPresenter = new InformationPresenter(InformationRepository.getInstance(InformationLocalDataSource.getInstace(HomePageActivity.this),
                InformationRemoteDataSource.getInstance()), informationFragment);



        if (savedInstanceState != null) {
            InformationFilter currentFiltering =
                    (InformationFilter) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            informationPresenter.setFiltering(currentFiltering);
        }
    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.nav_info:
//                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                        transaction.replace(R.id.home_page_frame,informationFragment);
//                        transaction.commit();
//                        break;
//                    case R.id.nav_river:
//                        break;
//                    case R.id.nav_small_data:
//                        break;
//                    case R.id.nav_arrange:
//                        break;
//                    case R.id.nav_share:
//                        break;
//                    case R.id.nav_concerned_topic:
//                        break;
//                    case R.id.nav_remind:
//                        break;
//                    case R.id.nav_setting:
//                        break;
//                }
                ActivityUtil.setCurrentFragment(item.getItemId());
                item.setChecked(true);
                homePageDrawer.closeDrawers();
                return true;
            }
        });
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    System.out.print("got PERMISSION\n");
                }else{
                    System.out.print("PERMISSION FAILED\n");
                }
                break;
            default:
        }
    }
}