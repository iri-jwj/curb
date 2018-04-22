package jxpl.scnu.curb.userProfile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import jxpl.scnu.curb.data.retrofit.RetrofitGetData;
import jxpl.scnu.curb.userProfile.setScholat.SetScholatActivity;
import jxpl.scnu.curb.utils.SharedHelper;
import jxpl.scnu.curb.utils.XmlDataStorage;

public class UserProfilePresenter implements UserProfileContract.Presenter {
    private UserProfileContract.View m_profileView;
    private Activity m_activity;

    public static final int CROP_PHOTO = 3;
    public static final int CHOOSE_PHOTO = 2;
    public static final int SET_SCHOLAT = 4;

    private final String TAG = "ProfilePresenter";
    private boolean firstOpen = true;

    public UserProfilePresenter(UserProfileContract.View para_profileView, Activity para_activity) {
        m_profileView = para_profileView;
        m_activity = para_activity;
        m_profileView.setPresenter(this);
    }

    private void updateProfile2Server(File para_avatar) {
        Map userInfo = XmlDataStorage.getUserInfo();
        RetrofitGetData.uploadAvatar((String) userInfo.get(XmlDataStorage.USER_ID), para_avatar,
                new UserProfileContract.UploadAvatarCallback() {
                    @Override
                    public void onAvatarUploaded() {
                        m_activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                m_profileView.showMessage("更新头像成功");
                            }
                        });
                    }

                    @Override
                    public void onUploadedFailed() {
                        m_activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                m_profileView.showMessage("更新头像失败");
                            }
                        });
                    }
                });
        Log.d(TAG, "updateProfile2Server: ");
    }

    private void updateScholatInfo2Server() {
        Map scholatInfo = XmlDataStorage.getScholat();
        Map userInfo = XmlDataStorage.getUserInfo();
        RetrofitGetData.uploadScholatInfo((String) userInfo.get(XmlDataStorage.USER_ID),
                (String) scholatInfo.get(XmlDataStorage.SCHOLAT_ACCOUNT),
                (String) scholatInfo.get(XmlDataStorage.SCHOLAT_PSW),
                new UserProfileContract.UploadScholatInfoCallback() {
                    @Override
                    public void onScholatInfoUploaded() {
                        m_activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                m_profileView.showMessage("头像上传成功");
                            }
                        });
                    }

                    @Override
                    public void onUploadedFailed() {
                        m_activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                m_profileView.showMessage("头像上传失败");
                            }
                        });
                    }
                });
        Log.d(TAG, "updateScholatInfo2Server: ");
    }

    @Override
    public void updateProfile2Server(final String nickname) {
        if (!XmlDataStorage.isSharedHelperSet())
            XmlDataStorage.setM_sharedHelper(SharedHelper.getInstance(m_activity));
        String originNickname = XmlDataStorage.getNickname();
        if (!originNickname.equals(nickname)) {
            XmlDataStorage.saveNickname(nickname);
            Map userInfo = XmlDataStorage.getUserInfo();
            RetrofitGetData.uploadNickname((String) userInfo.get(XmlDataStorage.USER_ID), nickname,
                    new UserProfileContract.UploadNicknameCallback() {
                        @Override
                        public void onNicknameUploaded() {
                            m_activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    m_profileView.showMessage("更新昵称成功");
                                }
                            });
                        }

                        @Override
                        public void onUploadedFailed() {
                            m_activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    m_profileView.showMessage("更新昵称失败");
                                }
                            });
                        }
                    });
            Log.d(TAG, "updateProfile2Server: ");
        }

    }

    @Override
    public void openScholatSetting() {
        Intent lc_intent = new Intent(m_activity, SetScholatActivity.class);
        m_activity.startActivityForResult(lc_intent, SET_SCHOLAT);
    }

    @Override
    public void selectAvatarInGallery() {
        if (firstOpen && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                && ContextCompat.checkSelfPermission(m_activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(m_activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        firstOpen = false;
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        m_activity.startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void logout() {

    }

    @Override
    public void start() {
        loadProfile();
    }

    private void loadProfile() {
        if (!XmlDataStorage.isSharedHelperSet()) {
            SharedHelper lc_sharedHelper = SharedHelper.getInstance(m_activity);
            XmlDataStorage.setM_sharedHelper(lc_sharedHelper);
        }
        String avatarPath = XmlDataStorage.getAvatarPath();
        Bitmap lc_avatar = null;
        if (!avatarPath.equals(""))
            lc_avatar = BitmapFactory.decodeFile(avatarPath);
        String nickname = XmlDataStorage.getNickname();
        Map scholat = XmlDataStorage.getScholat();
        String scholatAccount = (String) scholat.get(XmlDataStorage.SCHOLAT_ACCOUNT);
        Map user = XmlDataStorage.getUserInfo();
        String userAccount = (String) user.get(XmlDataStorage.USER_ACCOUNT);

        m_profileView.showUserProfile(lc_avatar, nickname, userAccount, scholatAccount);
    }

    @Override
    public void cropImage(Uri para_uri) {
        para_uri = Uri.parse(getPath(m_activity, para_uri));

        File lc_fileDir = new File(m_activity.getFilesDir(), "avatar");
        lc_fileDir.mkdirs();
        File avatar = new File(lc_fileDir, "avatar_temp.jpg");
        try {
            avatar.createNewFile();
        } catch (IOException para_e) {
            para_e.printStackTrace();
        }
        Intent lc_intent = new Intent("com.android.camera.action.CROP");
        Uri m_imageUri;
        if (Build.VERSION.SDK_INT > 23) {
            m_imageUri = FileProvider.getUriForFile(m_activity, "jxpl.scnu.curb.getimage", avatar);
            m_activity.grantUriPermission(m_activity.getPackageName(), m_imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            m_activity.grantUriPermission(m_activity.getPackageName(), m_imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            lc_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            lc_intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            m_imageUri = Uri.fromFile(avatar);
        }

        lc_intent.setDataAndType(para_uri, "image/*");
        lc_intent.putExtra("crop", "true");
        lc_intent.putExtra("circleCrop", true);
        lc_intent.putExtra("aspectX", 1);
        lc_intent.putExtra("aspectY", 1);
        lc_intent.putExtra("scale", true);
        lc_intent.putExtra("return-data", true);
        lc_intent.putExtra("outputX", 200);
        lc_intent.putExtra("outputY", 200);
        lc_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //lc_intent.putExtra(MediaStore.EXTRA_OUTPUT, m_imageUri);
        lc_intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        lc_intent.putExtra("noFaceDetection", false);

        m_activity.startActivityForResult(lc_intent, CROP_PHOTO);
    }

    @Override
    public void updateAvatar(Bitmap para_bitmap) {
        if (para_bitmap != null) {
            //更新个人信息界面的显示
            m_profileView.refreshAvatar(para_bitmap);

            //创建文件存储头像
            File lc_fileDir = new File(m_activity.getFilesDir(), "avatar");
            File avatar = new File(lc_fileDir, "avatar.jpg");
            if (avatar.exists())
                avatar.delete();
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(avatar));
                para_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //存储头像路径
            if (!XmlDataStorage.isSharedHelperSet()) {
                SharedHelper lc_sharedHelper = SharedHelper.getInstance(m_activity);
                XmlDataStorage.setM_sharedHelper(lc_sharedHelper);
            }
            XmlDataStorage.saveAvatarPath(avatar.getPath());
            //将更新推送到服务器
            updateProfile2Server(avatar);
        }
    }

    @Override
    public void updateScholatAccount(String para_account) {
        m_profileView.refreshScholat(para_account);
        updateScholatInfo2Server();
    }

    /**
     * @param context 上下文对象
     * @param uri     当前相册照片的Uri
     * @return 解析后的Uri对应的String
     */
    @SuppressLint("NewApi")
    private String getPath(final Context context, final Uri uri) {
        String pathHead = "file:///";
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return pathHead + Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);

                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return pathHead + getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return pathHead + getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return pathHead + getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return pathHead + uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
