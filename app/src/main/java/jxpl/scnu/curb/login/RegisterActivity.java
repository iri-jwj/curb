package jxpl.scnu.curb.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.data.retrofit.Connect2Server;
import jxpl.scnu.curb.utils.SharedHelper;
import jxpl.scnu.curb.utils.XmlDataStorage;


public class RegisterActivity extends AppCompatActivity {


    @BindView(R.id.signup_password)
    EditText password_et;
    @BindView(R.id.signup_button)
    Button register_button;
    @BindView(R.id.signup_account)
    AutoCompleteTextView account_et;

    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        // Set up the login form.
        // mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
//        populateAutoComplete();

        //mPasswordView = (EditText) findViewById(R.id.password);
        password_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        password_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        register_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private void attemptLogin() {
        // Reset errors.
        account_et.setError(null);
        password_et.setError(null);

        // Store values at the time of the login attempt.
        String account = account_et.getText().toString();
        String password = password_et.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            password_et.setError(getString(R.string.error_invalid_password));
            focusView = password_et;
            cancel = true;
        }
        /*
         * 检查账号是否合法
         */
        if (TextUtils.isEmpty(account)) {
            account_et.setError(getString(R.string.error_field_required));
            focusView = account_et;
            cancel = true;
        }
        if (account.equals("default")) {
            account_et.setError("can't use this account");
            focusView = account_et;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            register(account, password);
        }
    }

    /**
     * 2018-03-24
     * lifumin
     *
     * @param account  账号
     * @param password 密码
     *                 注册逻辑（仿照login的逻辑）
     */
    public void register(final String account, final String password) {
        final Thread registerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgress(true);
                    }
                });
                if (XmlDataStorage.isSharedHelperSet())
                    XmlDataStorage.setM_sharedHelper(SharedHelper.getInstance(RegisterActivity.this));
                //生成一个UUid类型
                String registerId = XmlDataStorage.getUserInfo().get(XmlDataStorage.USER_ID);
                boolean registerResult = Connect2Server.getConnect2Server(RegisterActivity.this).postRegister(registerId, account, password);
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgress(false);
                    }
                });

                if (registerResult) {

                    /*
                     * 2018-03-24
                     * lifumin
                     * 返回数据给login页面
                     * start
                     */
                    Intent intent = new Intent();
                    intent.putExtra("return_id", registerId);
                    intent.putExtra("return_account", account);
                    intent.putExtra("return_password", password);
                    setResult(RESULT_OK, intent);
                    /*
                     * end
                     */
                    RegisterActivity.this.finish();//本活动销毁后会返回上一个活动，也就是登录界面
                } else {
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "111", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        registerThread.start();

    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        //TODO： 还未实现检查两次确认密码的一致性

        return password.length() > 6 && password.length() < 20;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

    }

}

