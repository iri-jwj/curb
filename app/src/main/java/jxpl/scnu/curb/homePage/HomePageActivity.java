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

public class HomePageActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.home_page_frame)
    FrameLayout homePageFrame;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.home_page_drawer)
    DrawerLayout homePageDrawer;
    private DrawerLayout drawerLayout;

    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(homePageDrawer);
        unbinder=ButterKnife.bind(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.home_page_drawer);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.home);
        }

        //菜单项目点击事件
        navView.setCheckedItem(R.id.nav_info);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_info:
                        break;
                    case R.id.nav_river:
                        break;
                    case R.id.nav_small_data:
                        break;
                    case R.id.nav_arrange:
                        break;
                }
                return false;
            }
        });


    }

    //工具栏点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    private void changeFragment(MenuItem item) {

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        unbinder.unbind();
    }
}
