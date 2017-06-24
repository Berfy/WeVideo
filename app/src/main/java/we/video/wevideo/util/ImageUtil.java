package we.video.wevideo.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import we.video.wevideo.R;
import we.video.wevideo.cons.Constants;

/**
 * Created by Administrator on 2016/4/18.
 */
public class ImageUtil {

    private static ImageUtil imageUtil;
    private Context context;

    public static ImageUtil getInstances() {
        if (null == imageUtil) {
            try {
                throw new NullPointerException(
                        "ImageUtil，请在Application中调用ImageUtil.init(Context context)方法");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return imageUtil;
    }

    public static ImageUtil init(Context context) {
        if (null == imageUtil) {
            imageUtil = new ImageUtil(context);
        }
        return imageUtil;
    }

    public ImageUtil(Context context) {
        this.context = context;
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheSize(50 * 1024 * 1024)
                // 加密
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())//将保存的时候的URI名称用HASHCODE加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的File数量
                .discCache(new UnlimitedDiscCache(new File(FileUtils.getFilePath(context, Constants.TEMP_IMAGE_PATH))))// 自定义缓存路径
                .build();
        ImageLoader.getInstance().init(configuration);
    }

    public DisplayImageOptions getDefault() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .showImageOnLoading(R.mipmap.loading)
                .showImageOnFail(R.mipmap.loading)
                .showImageForEmptyUri(R.mipmap.loading)
                .build();
    }

    public void displayDefault(ImageView view, String url) {
        ImageLoader.getInstance().displayImage(url, view, getDefault());
    }

    /**
     * 使用此加载框架的imageloader加载的图片，设置了缓存后，下次使用，手工从缓存取出来用，这时特别要注意，不能直接使用：
     * imageLoader.getMemoryCache().get(uri)来获取，因为在加载过程中，key是经过运算的，而不单单是uri,而是：
     * String memoryCacheKey = MemoryCacheUtil.generateKey(uri, targetSize);
     *
     * @return
     */
    public static Bitmap getBitmapFromCache(String uri, ImageLoader imageLoader) {//这里的uri一般就是图片网址
        List<String> memCacheKeyNameList = MemoryCacheUtil.findCacheKeysForImageUri(uri, imageLoader.getMemoryCache());
        if (memCacheKeyNameList != null && memCacheKeyNameList.size() > 0) {
            for (String each : memCacheKeyNameList) {
            }
            return imageLoader.getMemoryCache().get(memCacheKeyNameList.get(0));
        }

        return null;
    }


    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "DownloadJianXi");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + appDir.getPath())));
    }


}
