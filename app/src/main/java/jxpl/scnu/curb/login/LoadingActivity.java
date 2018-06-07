package jxpl.scnu.curb.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.homePage.HomePageActivity;
import jxpl.scnu.curb.utils.SharedHelper;
import jxpl.scnu.curb.utils.XmlDataStorage;


public class LoadingActivity extends AppCompatActivity {
    private static final int AUTO_HIDE_DELAY_MILLIS = 1250;
    @BindView(R.id.loading_image)
    ImageView loadingImage;
    @BindView(R.id.loading_group)
    ConstraintLayout m_LoadingGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ButterKnife.bind(this);
        //设置全屏展示
        m_LoadingGroup.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);

        if (!XmlDataStorage.isSharedHelperSet()) {
            SharedHelper lc_sharedHelper = SharedHelper.getInstance(this);
            XmlDataStorage.setM_sharedHelper(lc_sharedHelper);
        }
        final Map lc_map = XmlDataStorage.getUserInfo();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!lc_map.get(XmlDataStorage.USER_ID).equals("")) {
                    JPushInterface.setDebugMode(true);
                    JPushInterface.init(LoadingActivity.this);
                    JPushInterface.setAlias(LoadingActivity.this, 1, (String) lc_map.get(XmlDataStorage.USER_ID));
                    Intent intent = new Intent(LoadingActivity.this,
                            HomePageActivity.class);
                    startActivity(intent);
                    LoadingActivity.this.finish();
                } else {
                    XmlDataStorage.setInfoFirstRun(true);
                    XmlDataStorage.setRiverFirstRun(true);
                    XmlDataStorage.setScholatFirstRun(true);
                    XmlDataStorage.setSDFirstRun(true);
                    String id = UUID.randomUUID().toString();
                    String userAccount = "curb_" + id.substring(0,8);
                    XmlDataStorage.saveUserInfo(id, "00000000", userAccount);
                    JPushInterface.init(LoadingActivity.this);
                    JPushInterface.setAlias(LoadingActivity.this, 1, id);
                    Intent intent = new Intent(LoadingActivity.this,
                            HomePageActivity.class);
                    startActivity(intent);
                    LoadingActivity.this.finish();
                }
            }
        }, AUTO_HIDE_DELAY_MILLIS);
    }
}
