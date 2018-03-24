package jxpl.scnu.curb.smallData.createSmallData;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import jxpl.scnu.curb.R;

public class SmallDataCreateActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView m_ToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar m_Toolbar;
    @BindView(R.id.content_frame)
    FrameLayout m_ContentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_data_create);
        ButterKnife.bind(this);
    }


}
