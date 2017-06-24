package we.video.wevideo.cons;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Berfy on 2016/3/4.
 * 全局配置
 */
public class Constants {
    public static String XML_TEMP = "temp_weVideo";
    public static ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);
    public static ExecutorService EXECUTOR1 = Executors.newFixedThreadPool(100);

    public static ExecutorService EXECUTOR_PLAY = Executors.newFixedThreadPool(1);

    public static String TEMP_PATH = "WeVideo/";
    public static String TEMP_PHOTO_PATH = TEMP_PATH + "photo/";
    public static String TEMP_IMAGE_PATH = TEMP_PATH + "images/";

    public static String TEMP_ISLOGIN = "isLogin";
    public static String TEMP_ISPUSH = "isPush";
    public static String TEMP_ISLIULIANG = "isLiuliang";
}
