package jxpl.scnu.curb.homePage;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.data.local.InformationLocalDataSource;
import jxpl.scnu.curb.data.local.SmallDataLocalDataSource;
import jxpl.scnu.curb.data.remote.InformationRemoteDataSource;
import jxpl.scnu.curb.data.remote.SDRemoteDataSource;
import jxpl.scnu.curb.data.repository.InformationRepository;
import jxpl.scnu.curb.data.repository.ScholatRepository;
import jxpl.scnu.curb.data.repository.SmallDataRepository;
import jxpl.scnu.curb.immediateInformation.InformationFragment;
import jxpl.scnu.curb.immediateInformation.InformationPresenter;
import jxpl.scnu.curb.river.RiverFragment;
import jxpl.scnu.curb.scholat.ScholatFragment;
import jxpl.scnu.curb.scholat.ScholatPresenter;
import jxpl.scnu.curb.smallData.SmallDataFragment;
import jxpl.scnu.curb.smallData.SmallDataPresenter;
import jxpl.scnu.curb.userProfile.UserProfileActivity;
import jxpl.scnu.curb.userProfile.setScholat.SetScholatActivity;
import jxpl.scnu.curb.utils.ActivityUtil;
import jxpl.scnu.curb.utils.SharedHelper;
import jxpl.scnu.curb.utils.XmlDataStorage;

/**
 * @author iri-jwj
 * @version 2
 * update 4.9
 * 添加了极光推送的初始化代码= =
 */
public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String ITEM_TO_SHOW = "ItemToShow";
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

    private CircleImageView m_avatar;
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

        Intent lc_intent = getIntent();
        initFragment();//初始化其它fragments
        ActivityUtil.setContainerView(R.id.content_frame);
        ActivityUtil.setFragmentManagerInHome(getSupportFragmentManager());

        InformationFragment informationFragment = InformationFragment.newInstance();
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
                m_avatar = (CircleImageView) lc_view;
                if (!XmlDataStorage.isSharedHelperSet()) {
                    XmlDataStorage.setM_sharedHelper(SharedHelper.getInstance(HomePageActivity.this));
                }
                String path = XmlDataStorage.getAvatarPath();

                if (!path.equals("")) {
                    Bitmap lc_bitmap = BitmapFactory.decodeFile(path);
                    m_avatar.setImageBitmap(lc_bitmap);
                }

                lc_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent lc_intent = new Intent(HomePageActivity.this, UserProfileActivity.class);
                        HomePageActivity.this.startActivity(lc_intent);
                    }
                });
            } else {
                if (lc_view.getId() == R.id.nav_header_account) {
                    TextView lc_account = (TextView) lc_view;
                    Map userInfo = XmlDataStorage.getUserInfo();
                    lc_account.setText((String) userInfo.get(XmlDataStorage.USER_ACCOUNT));
                }
            }
        }
    }

    /**
     * 初始化Homepage中寄存的3个Fragment
     */
    private void initFragment() {

        SmallDataFragment lc_smallDataFragment = SmallDataFragment.newInstance();
        ScholatFragment lc_scholatFragment = ScholatFragment.newInstance();

        ActivityUtil.addFragmentInHomePage(R.id.nav_river, new RiverFragment());
        //ActivityUtil.addFragmentInHomePage(R.id.nav_arrange, new ArrangeFragment());
        ActivityUtil.addFragmentInHomePage(R.id.nav_small_data, lc_smallDataFragment);
        ActivityUtil.addFragmentInHomePage(R.id.nav_scholat, lc_scholatFragment);

        new ScholatPresenter(lc_scholatFragment, ScholatRepository.getInstance(this), this);

        new SmallDataPresenter(lc_smallDataFragment
                , SmallDataRepository.getInstance(SDRemoteDataSource.getInstance()
                , SmallDataLocalDataSource.getInstance(this), this), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String path = XmlDataStorage.getAvatarPath();
        if (!path.equals("")) {
            Bitmap lc_bitmap = BitmapFactory.decodeFile(path);
            m_avatar.setImageBitmap(lc_bitmap);
        }
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
        if (item.getItemId() == R.id.nav_scholat) {
            if (!XmlDataStorage.isSharedHelperSet())
                XmlDataStorage.setM_sharedHelper(SharedHelper.getInstance(HomePageActivity.this));
            Map scholatInfo = XmlDataStorage.getScholat();
            String scholatAccount = (String) scholatInfo.get(XmlDataStorage.SCHOLAT_ACCOUNT);
            if (!scholatAccount.equals("")) {
                ActivityUtil.setCurrentFragment(item.getItemId());
                toolbarTitle.setText(item.getTitle());
                item.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                AlertDialog.Builder lc_builder = new AlertDialog.Builder(HomePageActivity.this);
                lc_builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                lc_builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent lc_intent = new Intent(HomePageActivity.this, SetScholatActivity.class);
                        HomePageActivity.this.startActivity(lc_intent);
                        dialog.dismiss();
                    }
                });
                lc_builder.setMessage("你还没有添加学者网账号哦~");
                lc_builder.setTitle("出错啦");
                AlertDialog lc_alertDialog = lc_builder.create();
                lc_alertDialog.show();
            }
        } else {
            ActivityUtil.setCurrentFragment(item.getItemId());
            toolbarTitle.setText(item.getTitle());
            item.setChecked(true);
            drawerLayout.closeDrawer(GravityCompat.START);
        }
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
        ActivityUtil.removeFragment();
        super.onDestroy();
    }
}
