package jxpl.scnu.curb.userProfile;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.utils.ActivityUtil;

public class UserProfileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView m_ToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar m_Toolbar;
    @BindView(R.id.content_frame)
    FrameLayout m_ContentFrame;

    private UserProfileContract.Presenter m_userProfilePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        setSupportActionBar(m_Toolbar);
        ActionBar lc_actionBar = getSupportActionBar();
        if (lc_actionBar != null) {
            lc_actionBar.setTitle("");
            lc_actionBar.setDisplayHomeAsUpEnabled(false);
        }
        m_ToolbarTitle.setText("个人信息");

        UserProfileFragment lc_userProfileFragment = UserProfileFragment.newInstance();
        m_userProfilePresenter = new UserProfilePresenter(lc_userProfileFragment, this);

        ActivityUtil.setContainerViewNotHome(m_ContentFrame.getId());
        ActivityUtil.setFragmentManagerNotHome(getSupportFragmentManager());
        ActivityUtil.addFragmentNotInHomePage(lc_userProfileFragment);
        ActivityUtil.setCurrentFragmentNotInHome();

    }

    @Override
    protected void onDestroy() {
        ActivityUtil.removeFragmentNotHome();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.print("got PERMISSION\n");
                } else {
                    System.out.print("PERMISSION FAILED\n");
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case UserProfilePresenter.CHOOSE_PHOTO:
                    if (data != null) {
                        Uri lc_uri = data.getData();
                        m_userProfilePresenter.cropImage(lc_uri);
                    }
                    break;
                case UserProfilePresenter.CROP_PHOTO:
                    Bundle lc_bundle = data.getExtras();
                    Bitmap lc_bitmap = lc_bundle.getParcelable("data");
                    m_userProfilePresenter.updateAvatar(lc_bitmap);
                    break;
                case UserProfilePresenter.SET_SCHOLAT:
                    String scholatAccount = data.getStringExtra("result");
                    m_userProfilePresenter.updateScholatAccount(scholatAccount);
                    break;

            }
        }
    }

}
