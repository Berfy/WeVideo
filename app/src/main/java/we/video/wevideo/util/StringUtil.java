package we.video.wevideo.util;

import java.text.DecimalFormat;

/**
 * Created by Berfy on 2016/6/3.
 */
public class StringUtil {

    /**
     * 设置保留1个小数位，四舍五入
     */
    public static String fomatScale(float num) {
        String[] nums = (num + "").split("\\.");
        DecimalFormat fnum = new DecimalFormat("##0.0");
        String result = fnum.format(num);
        return result;
    }
}
