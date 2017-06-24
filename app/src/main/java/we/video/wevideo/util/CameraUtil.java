package we.video.wevideo.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import we.video.wevideo.cons.Constants;

public class CameraUtil {

    private Context mContext;
    private final String TAG = "CameraUtil";
    private File mTempFile;// 创建一个以当前时间为名称的文件
    public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public static final int PHOTO_REQUEST_CUT = 3;// 结果

    private boolean isCrop = true;
    private int aspectX = 1, aspectY = 1, outWidth = 150, outHeight = 150;

    private OnPhotoListener onPhotoListener;

    public interface OnPhotoListener {
        void getPhotoPath(String filePath);

        void getPhotoCropData(byte[] data);
    }

    public CameraUtil(Context context) {
        mContext = context;
    }

    public void setTempFile(File file) {
        mTempFile = file;
    }


    public File getTakePhotoFile() {
        return mTempFile;
    }

    public void setOnPhotoListener(OnPhotoListener onPhotoListener) {
        this.onPhotoListener = onPhotoListener;
    }

    public void setConfig(boolean isCrop, int aspectX, int aspectY, int outWidth, int outHeight) {
        this.isCrop = isCrop;
        this.aspectX = aspectX;
        this.aspectY = aspectY;
        this.outWidth = outWidth;
        this.outHeight = outHeight;
    }

    private void setUpTempFile() {
        if (null != mTempFile)
            FileUtils.deleteFile(mTempFile.getPath());
        mTempFile = new File(FileUtils.getFilePath(mContext, Constants.TEMP_PHOTO_PATH + getPhotoFileName()));
    }

    /**
     * 使用系统当前日期加以调整作为照片的名称
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    public void tackPhoto() {
        setUpTempFile();
        // 调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(mTempFile));
        ((Activity) mContext).startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
    }

    public void pickPhoto() {
        setUpTempFile();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        ((Activity) mContext).startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case CameraUtil.PHOTO_REQUEST_TAKEPHOTO:
                    if (isCrop) {
                        startPhotoZoom(Uri.fromFile(getTakePhotoFile()));
                    } else {
                        if (null != onPhotoListener) {
                            onPhotoListener.getPhotoPath(getUriToPath(Uri.fromFile(getTakePhotoFile())));
                        }
                    }
                    break;

                case CameraUtil.PHOTO_REQUEST_GALLERY:
                    if (data != null) {
                        if (isCrop) {
                            startPhotoZoom(data.getData());
                        } else {
                            if (null != onPhotoListener) {
                                onPhotoListener.getPhotoPath(getUriToPath(data.getData()));
                            }
                        }
                    }
                    break;

                case CameraUtil.PHOTO_REQUEST_CUT:
                    LogUtil.e(TAG, "裁剪结果" + data);
                    if (data != null) {
                        byte[] bytes = getDataToBytes(data);
                        if (null != bytes) {
                            if (null != onPhotoListener) {
                                FileUtils.createFile(mTempFile, bytes);
                                onPhotoListener.getPhotoCropData(bytes);
                                onPhotoListener.getPhotoPath(mTempFile.getPath());
                            }
                        }
                    }
                    break;
            }
    }

    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", outWidth);
        intent.putExtra("outputY", outHeight);
        intent.putExtra("return-data", true);
        ((Activity) mContext).startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private String getUriToPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = ((Activity) mContext).managedQuery(uri, proj, null, null, null);
        int actual_image_column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(actual_image_column_index);
    }

    // 将进行剪裁后的图片显示到UI界面上
    private byte[] getDataToBytes(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            // Constants.drawable = new BitmapDrawable(mIconBitmap);//太占用内存
            byte[] bytes = FileUtils.bitmap2Bytes((Bitmap) bundle.getParcelable("data"), 100);
            return bytes;
        } else {
            ToastUtil.getInstance().showToast("取消了选择");
        }
        return null;
    }

    public void clearTemp() {
        FileUtils.deleteFile(mTempFile.getPath());
    }
}
