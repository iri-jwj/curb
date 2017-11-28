package jxpl.scnu.curb.homePage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.data.local.InformationLocalDataSource;
import jxpl.scnu.curb.data.remote.InformationRemoteDataSource;
import jxpl.scnu.curb.data.repository.InformationRepository;
import jxpl.scnu.curb.immediateInformation.InformationFragment;
import jxpl.scnu.curb.immediateInformation.InformationPresenter;
import jxpl.scnu.curb.river.RiverFragment;
import jxpl.scnu.curb.smallData.SmallDataFragment;
import jxpl.scnu.curb.taskArrangement.ArrangeFragment;
import jxpl.scnu.curb.utils.ActivityUtil;

public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        ab.setHomeAsUpIndicator(R.drawable.home);
        ab.setDisplayHomeAsUpEnabled(true);



        ActivityUtil.setContainerView(R.id.content_frame);
        ActivityUtil.setFragmentManager(getSupportFragmentManager());
        ActivityUtil.addFragment(R.id.nav_river, new RiverFragment());
        ActivityUtil.addFragment(R.id.nav_arrange, new ArrangeFragment());
        ActivityUtil.addFragment(R.id.nav_small_data, new SmallDataFragment());

        InformationFragment informationFragment = InformationFragment.newInstance();
        Log.d("HomePageCreateInfoFrag", "onCreate:infoFrag " + informationFragment.isAdded());
        ActivityUtil.addFragment(R.id.nav_info, informationFragment);
        ActivityUtil.setCurrentFragment(R.id.nav_info);
        InformationPresenter informationPresenter = new InformationPresenter(InformationRepository.getInstance(InformationLocalDataSource.getInstace(HomePageActivity.this),
                InformationRemoteDataSource.getInstance()), informationFragment);
        navView.setCheckedItem(R.id.nav_info);
        toolbarTitle.setText(R.string.information);
        navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
}
