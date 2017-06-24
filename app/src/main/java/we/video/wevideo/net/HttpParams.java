package we.video.wevideo.net;

import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import we.video.wevideo.util.LogUtil;

/**
 * Created by Berfy on 2016/3/4.
 * 参数
 */
public class HttpParams {

    private List<BasicNameValuePair> mData = new ArrayList<BasicNameValuePair>();
    private HashMap<String, File> mFiles = new HashMap<String, File>();
    private StringBuffer sb;

    public void put(String key, String value) {
        // TODO Auto-generated method stub
        BasicNameValuePair pair = new BasicNameValuePair(key, value);
        if (!mData.contains(pair)) {
            mData.add(pair);
        }
    }

    public void put(String key, File file) {
        // TODO Auto-generated method stub
        if (!mFiles.containsKey(key)) {
            LogUtil.e("参数文件", key);
            mFiles.put(key, file);
        }
    }

    public List<BasicNameValuePair> getParamsList() {
        return mData;
    }

    public HashMap<String, File> getFileList() {
        return mFiles;
    }

    @Override
    public String toString() {
        if (null == sb) {
            sb = new StringBuffer();
        }
        sb.append("?");
        for (BasicNameValuePair pair : mData) {
            sb.append(pair.getName());
            sb.append("=");
            sb.append(pair.getValue());
            sb.append("&");
        }
        return sb.substring(0, sb.length() - 1);
    }
}
