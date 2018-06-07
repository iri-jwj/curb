package jxpl.scnu.curb.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.data.retrofit.Connect2Server;
import jxpl.scnu.curb.userProfile.UserProfilePresenter;
import jxpl.scnu.curb.utils.SharedHelper;
import jxpl.scnu.curb.utils.XmlDataStorage;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.login_progress)
    ProgressBar loginProgress;
    @BindView(R.id.login_account)
    AutoCompleteTextView loginAccount;
    @BindView(R.id.password)
    EditText password;
    //    @BindView(R.id.sign_in_button)
//    Button SignInButton;
    @BindView(R.id.email_login_form)
    LinearLayout emailLoginForm;
    @BindView(R.id.login_form)
    ScrollView loginForm;
    @BindView(R.id.login_linear)
    LinearLayout loginLinear;

    /**
     * 2018-03-24
     * lifumin
     * 添加一个注册的textview，有点击事件
     */
    @BindView(R.id.registerTextView)
    TextView registerTextView;
    @BindView(R.id.signin_image)
    ImageView signinLogo;


    private String userId;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        // Set up the login form.
        //populateAutoComplete();
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        /*
         * 2018-03-24
         * lifumin
         * 用logo代替了上面的登录按钮
         */
        signinLogo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("lifumin", "onClick:点击了登录logo ");
                attemptLogin();
            }
        });
        /*
         * 2018-03-24
         * @author lifumin
         * 给注册的textview添加按钮事件，跳转到注册页面
         * 从注册页面回到此页面时，会回调onActivityResult(int requestCode, int resultCode, Intent data)函数
         */
        registerTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(registerIntent, 1);
            }
        });
    }

    /**
     * 2018-03-24
     * lifumin
     *
     * @param requestCode *
     * @param resultCode  *
     * @param data        从注册页面返回的时候，获取注册页面传过来的值并填充在账号和密码的edittext上面
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    loginAccount.setText(data.getStringExtra("return_account"));
                    password.setText(data.getStringExtra("return_password"));
                    userId = data.getStringExtra("return_id");
                }
                break;
            default:
                break;
        }
    }


    private void attemptLogin() {
        // Reset errors.
        loginAccount.setError(null);
        password.setError(null);

        // Store values at the time of the login attempt.
        String accountText = loginAccount.getText().toString();
        String passwordText = password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordText) && !isPasswordValid(passwordText)) {
            password.setError(getString(R.string.error_invalid_password));
            focusView = password;
            cancel = true;
        }

        // Check for a valid loginAccount address.
        if (TextUtils.isEmpty(accountText)) {
            loginAccount.setError(getString(R.string.error_field_required));
            focusView = loginAccount;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            login(accountText, passwordText);
        }
    }

    private void login(final String account, final String password) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgress(true);
                    }
                });
                String id = Connect2Server.getConnect2Server(LoginActivity.this)
                        .postLogin(account, password);

                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgress(false);
                    }
                });
                if (!id.equals("failed") && !id.equals("")) {
                    if (!XmlDataStorage.isSharedHelperSet()) {
                        XmlDataStorage.setM_sharedHelper(SharedHelper.getInstance(LoginActivity.this));
                    }
                    userId = id;
                    XmlDataStorage.saveUserInfo(userId, password, account);
                    JPushInterface.init(LoginActivity.this);
                    JPushInterface.setDebugMode(true);
                    JPushInterface.deleteAlias(LoginActivity.this, 3);
                    JPushInterface.setAlias(LoginActivity.this, 2, userId);
                    Intent intent = new Intent();
                    intent.putExtra("result", account);
                    LoginActivity.this.setResult(UserProfilePresenter.USER_LOGIN, intent);
                    LoginActivity.this.finish();
                } else {
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        thread.start();
        /*Intent intent = new Intent(LoginActivity.this,
                HomePageActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();*/
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6 && password.length() < 20;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        loginForm.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });
        loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        loginProgress.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}

