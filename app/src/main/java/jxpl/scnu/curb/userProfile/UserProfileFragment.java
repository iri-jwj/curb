package jxpl.scnu.curb.userProfile;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import jxpl.scnu.curb.R;


public class UserProfileFragment extends Fragment implements UserProfileContract.View {
    @BindView(R.id.avatar)
    CircleImageView m_Avatar;
    @BindView(R.id.nickname)
    TextView m_Nickname;
    @BindView(R.id.edit_nickname)
    EditText m_EditNickname;
    @BindView(R.id.costraint_nickname)
    ConstraintLayout m_ConstraintNickname;
    @BindView(R.id.account)
    TextView m_Account;
    @BindView(R.id.text_account)
    TextView m_TextAccount;
    @BindView(R.id.costraint_account)
    ConstraintLayout m_ConstraintAccount;
    @BindView(R.id.scholat)
    TextView m_Scholat;
    @BindView(R.id.text_scholat)
    TextView m_TextScholat;
    @BindView(R.id.costraint_scholat)
    ConstraintLayout m_ConstraintScholat;
    @BindView(R.id.logout)
    Button m_Logout;
    Unbinder unbinder;

    UserProfileContract.Presenter m_presenter;
    @BindView(R.id.fragment_profile_constrain)
    ConstraintLayout m_FragmentProfileConstrain;

    private final String TAG = "UserProfileFrag";

    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
        }*/
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        m_EditNickname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    m_FragmentProfileConstrain.requestFocus();
                    m_EditNickname.clearFocus();
                    String nickname = m_EditNickname.getText().toString();
                    m_presenter.updateProfile2Server(nickname);
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        m_presenter.start();
    }

    @Override
    public void showUserProfile(Bitmap para_avatar, String nickname, String account, String scholatAccount) {
        if (para_avatar != null)
            m_Avatar.setImageBitmap(para_avatar);
        else
            m_Avatar.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_no_avatar, null));
        if (!nickname.equals(""))
            m_EditNickname.setText(nickname);
        else
            m_EditNickname.setHint("还没有设置昵称哦");
        m_TextAccount.setText(account);
        if (!scholatAccount.equals(""))
            m_TextScholat.setText(scholatAccount);
        else
            m_TextScholat.setHint("还没有设置学者网账号哦");
    }

    @Override
    public void refreshAvatar(Bitmap para_avatar) {
        m_Avatar.setImageBitmap(para_avatar);
    }

    @Override
    public void refreshScholat(String scholatAccount) {
        m_TextScholat.setText(scholatAccount);
    }

    @Override
    public void setPresenter(UserProfileContract.Presenter presenter) {
        m_presenter = presenter;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.avatar)
    public void onM_AvatarClicked() {
        m_presenter.selectAvatarInGallery();
    }

    @OnClick(R.id.costraint_scholat)
    public void onM_LinearScholatClicked() {
        m_presenter.openScholatSetting();
    }

    @OnClick(R.id.logout)
    public void onM_LogoutClicked() {
        m_presenter.logout();
    }
}
