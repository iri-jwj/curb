package jxpl.scnu.curb.homePage;

import android.content.Intent;
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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
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
import jxpl.scnu.curb.userProfile.UserProfileActivity;
import jxpl.scnu.curb.utils.ActivityUtil;

/**
 * @author iri-jwj
 * @version 2
 * update 4.9
 * 添加了极光推送的初始化代码= =
 */
public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String bundleKey = "lastItem";
    private final String infoFilterKey = "currentFilter";
    private final static String ITEM_TO_SHOW = "ItemToShow";
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
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setTitle("");
            ab.setHomeAsUpIndicator(R.drawable.ic_ab_home);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        Intent lc_intent = getIntent();
        initFragment();//初始化其它fragments
        ActivityUtil.setContainerView(R.id.content_frame);
        ActivityUtil.setFragmentManagerInHome(getSupportFragmentManager());

        InformationFragment informationFragment = InformationFragment.newInstance();
        Log.d("HomePageCreateInfoFrag", "onCreate:infoFrag " + informationFragment.isAdded());
        ActivityUtil.addFragmentInHomePage(R.id.nav_info, informationFragment);
        informationPresenter = new InformationPresenter(InformationRepository.
                getInstance(InformationLocalDataSource.getInstance(HomePageActivity.this),
                        InformationRemoteDataSource.getInstance(), this), informationFragment, this);

        int itemToShow = R.id.nav_info;
        String title = getString(R.string.information);

        if (lc_intent.getStringExtra("FcmDataTarget") != null && !lc_intent.getStringExtra("FcmDataTarget").isEmpty()) {
            String[] target = lc_intent.getStringExtra("FcmDataTarget").split("-");
            if (target[1].equals("nav_scholat")) {
                itemToShow = R.id.nav_scholat;
            }
        } else {
            //判断Intent中是否存在intemToShow这一项
            int itemTemp = lc_intent.getIntExtra(ITEM_TO_SHOW, 0);
            if (itemTemp != 0) {
                itemToShow = itemTemp;
            }
        }

        navView.setCheckedItem(itemToShow);
        if (itemToShow == R.id.nav_scholat)
            title = getString(R.string.scholat);
        toolbarTitle.setText(title);
        ActivityUtil.setCurrentFragment(itemToShow);
        navView.setNavigationItemSelectedListener(this);

        //设置NavigationView中头像的点击事件
        LinearLayout lc_linearLayout = (LinearLayout) navView.getHeaderView(0);
        for (int i = 0; i < lc_linearLayout.getChildCount(); i++) {
            View lc_view = lc_linearLayout.getChildAt(i);
            if (lc_view.getId() == R.id.nav_header_imageView) {
                lc_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent lc_intent = new Intent(HomePageActivity.this, UserProfileActivity.class);
                        HomePageActivity.this.startActivity(lc_intent);
                    }
                });
                break;
            }
        }
    }

    /**
     * 初始化Homepage中寄存的3个Fragment
     */
    private void initFragment() {

        SmallDataFragment lc_smallDataFragment = SmallDataFragment.newInstance();
        ActivityUtil.addFragmentInHomePage(R.id.nav_river, new RiverFragment());
        //ActivityUtil.addFragmentInHomePage(R.id.nav_arrange, new ArrangeFragment());
        ActivityUtil.addFragmentInHomePage(R.id.nav_small_data, lc_smallDataFragment);

        new SmallDataPresenter(lc_smallDataFragment
                , SmallDataRepository.getInstance(SDRemoteDataSource.getInstance()
                , SmallDataLocalDataSource.getInstance(this), this), this);
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int itemToShow = intent.getIntExtra(ITEM_TO_SHOW, 0);
        if (itemToShow != 0) {
            navView.setCheckedItem(itemToShow);
            String title = "";
            if (itemToShow == R.id.nav_info)
                title = getString(R.string.information);
            if (itemToShow == R.id.nav_scholat)
                title = getString(R.string.scholat);
            toolbarTitle.setText(title);
            ActivityUtil.setCurrentFragment(itemToShow);
        }
    }

    @Override
    protected void onDestroy() {
        Log.d("inHomePage", "onDestroy: ");
        ActivityUtil.removeFragment();
        super.onDestroy();
    }
}
