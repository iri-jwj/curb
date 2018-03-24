package jxpl.scnu.curb.homePage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.data.local.InformationLocalDataSource;
import jxpl.scnu.curb.data.local.SmallDataLocalDataSource;
import jxpl.scnu.curb.data.remote.InformationRemoteDataSource;
import jxpl.scnu.curb.data.remote.SDRemoteDataSource;
import jxpl.scnu.curb.data.repository.InformationRepository;
import jxpl.scnu.curb.data.repository.SmallDataRepository;
import jxpl.scnu.curb.immediateInformation.InformationFragment;
import jxpl.scnu.curb.immediateInformation.InformationPresenter;
import jxpl.scnu.curb.river.RiverFragment;
import jxpl.scnu.curb.smallData.SmallDataFragment;
import jxpl.scnu.curb.smallData.SmallDataPresenter;
import jxpl.scnu.curb.taskArrangement.ArrangeFragment;
import jxpl.scnu.curb.utils.ActivityUtil;

public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String bundleKey = "lastItem";
    private final String infoFilterKey = "currentFilter";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    private InformationPresenter informationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("");
            ab.setHomeAsUpIndicator(R.drawable.ic_ab_home);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        ActivityUtil.setContainerView(R.id.content_frame);
        ActivityUtil.setFragmentManagerInHome(getSupportFragmentManager());

        InformationFragment informationFragment = InformationFragment.newInstance();
        Log.d("HomePageCreateInfoFrag", "onCreate:infoFrag " + informationFragment.isAdded());
        ActivityUtil.addFragmentInHomePage(R.id.nav_info, informationFragment);
        informationPresenter = new InformationPresenter(InformationRepository.
                getInstance(InformationLocalDataSource.getInstance(HomePageActivity.this),
                        InformationRemoteDataSource.getInstance()), informationFragment);
        navView.setCheckedItem(R.id.nav_info);
        toolbarTitle.setText(R.string.information);
        ActivityUtil.setCurrentFragment(R.id.nav_info);
        navView.setNavigationItemSelectedListener(this);

        initFragment();//初始化其它fragments
    }

    private void initFragment() {
        SmallDataFragment lc_smallDataFragment = SmallDataFragment.newInstance();
        ActivityUtil.addFragmentInHomePage(R.id.nav_river, new RiverFragment());
        ActivityUtil.addFragmentInHomePage(R.id.nav_arrange, new ArrangeFragment());
        ActivityUtil.addFragmentInHomePage(R.id.nav_small_data, lc_smallDataFragment);

        SmallDataPresenter lc_smallDataPresenter = new SmallDataPresenter(lc_smallDataFragment
                , SmallDataRepository.getInstance(SDRemoteDataSource.getInstance()
                , SmallDataLocalDataSource.getInstance(this)), this);
    }

    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        ActivityUtil.setCurrentFragment(item.getItemId());
        toolbarTitle.setText(item.getTitle());
        item.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(bundleKey, ActivityUtil.getCurrentKey());
        bundle.putString(infoFilterKey, informationPresenter.getFiltering());
        super.onSaveInstanceState(bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        ActivityUtil.setCurrentFragment(savedInstanceState.getInt(bundleKey));
        informationPresenter.setFiltering(savedInstanceState.getString(infoFilterKey));
        informationPresenter.start();
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        Log.d("inHomePage", "onDestroy: ");
        ActivityUtil.removeFragment();
        super.onDestroy();
    }
}
