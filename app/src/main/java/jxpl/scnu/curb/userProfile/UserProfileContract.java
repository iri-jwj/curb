package jxpl.scnu.curb.userProfile;

import android.graphics.Bitmap;
import android.net.Uri;

import jxpl.scnu.curb.BasePresenter;
import jxpl.scnu.curb.BaseView;

public interface UserProfileContract {
    interface View extends BaseView<Presenter> {
        /**
         * 显示用户的个人信息
         *
         * @param para_avatar    显示的bitmap 头像
         * @param nickname       显示的昵称
         * @param account        显示的用户账号
         * @param scholatAccount 显示的学者网账号
         */
        void showUserProfile(Bitmap para_avatar, String nickname, String account, String scholatAccount);

        /**
         * 更新头像
         *
         * @param para_avatar 更新的Bitmap头像
         */
        void refreshAvatar(Bitmap para_avatar);

        /**
         * 更新学者网信息
         *
         * @param scholatAccount 更新显示的学者网账号
         */
        void refreshScholat(String scholatAccount);

        void refreshAccount(String account);

        void showMessage(String message);

    }

    interface Presenter extends BasePresenter {

        /**
         * 更新用户昵称
         *
         * @param nickName 昵称
         */
        void updateProfile2Server(String nickName);

        /**
         * 更新头像
         *
         * @param para_bitmap 用于显示的bitmap
         */
        void updateAvatar(Bitmap para_bitmap);

        /**
         * @param para_account 更新的学者网账号
         */
        void updateScholatAccount(String para_account);

        /**
         * 设置学者网账号信息
         */
        void openScholatSetting();

        /**
         * 打开相册选择用户头像
         */
        void selectAvatarInGallery();

        /**
         * 用户登出逻辑
         */
        void logout();

        /**
         * 调用系统裁剪图片的功能
         */
        void cropImage(Uri para_uri);

        /*
        Uri getImageUri();*/

        void login();

    }

    interface UploadAvatarCallback {
        void onAvatarUploaded();

        void onUploadedFailed();
    }

    interface UploadScholatInfoCallback {
        void onScholatInfoUploaded();

        void onUploadedFailed();
    }

    interface UploadNicknameCallback {
        void onNicknameUploaded();

        void onUploadedFailed();
    }

}
