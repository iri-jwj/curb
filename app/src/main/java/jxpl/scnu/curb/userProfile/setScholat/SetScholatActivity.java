package jxpl.scnu.curb.userProfile.setScholat;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.utils.SharedHelper;
import jxpl.scnu.curb.utils.XmlDataStorage;

public class SetScholatActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView m_ToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar m_Toolbar;
    @BindView(R.id.confirm)
    Button m_Confirm;
    @BindView(R.id.scholat_account)
    EditText m_ScholatAccount;
    @BindView(R.id.scholat_account_text)
    TextView m_ScholatAccountText;
    @BindView(R.id.constraint_set_scholat_account)
    ConstraintLayout m_ConstraintSetScholatAccount;
    @BindView(R.id.scholat_psw)
    EditText m_ScholatPsw;
    @BindView(R.id.scholat_psw_text)
    TextView m_ScholatPswText;
    @BindView(R.id.constraint_set_scholat_psw)
    ConstraintLayout m_ConstraintSetScholatPsw;

    private String account;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_scholat);
        ButterKnife.bind(this);

        setSupportActionBar(m_Toolbar);
        ActionBar lc_actionBar = getSupportActionBar();
        if (lc_actionBar != null) {
            lc_actionBar.setTitle("");
            lc_actionBar.setDisplayHomeAsUpEnabled(false);
        }
        m_ToolbarTitle.setText("设置学者网账号");
        m_Confirm.setClickable(false);

        if (!XmlDataStorage.isSharedHelperSet())
            XmlDataStorage.setM_sharedHelper(SharedHelper.getInstance(this));
        Map scholat = XmlDataStorage.getScholat();
        account = (String) scholat.get(XmlDataStorage.SCHOLAT_ACCOUNT);
        password = (String) scholat.get(XmlDataStorage.SCHOLAT_PSW);

        if (account.equals("") || password.equals("")) {
            m_ScholatAccount.setHint("未设置学者网账号密码");
            account = "";
            password = "";
        } else {
            m_ScholatAccount.setText(account);
            m_ScholatPsw.setText(password);
        }

        m_ScholatAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                account = s.toString();
                if (!password.equals(""))
                    m_Confirm.setClickable(true);
            }
        });

        m_ScholatPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password = s.toString();
                if (!account.equals(""))
                    m_Confirm.setClickable(true);
            }
        });
    }

    @OnClick(R.id.confirm)
    public void onViewClicked() {
        if (!account.equals("") && !password.equals("")) {
            XmlDataStorage.saveScholat(account, password);
            Intent lc_intent = new Intent();
            lc_intent.putExtra("result", account);
            SetScholatActivity.this.setResult(RESULT_OK, lc_intent);
            SetScholatActivity.this.finish();
        }
    }
}
