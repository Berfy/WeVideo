package we.video.wevideo.net;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.message.BasicNameValuePair;

import java.util.HashMap;
import java.util.Map;

import we.video.wevideo.util.LogUtil;

public class VolleyHttp {

    private static VolleyHttp mVolleyHttp;
    private RequestQueue mRequestQueue;
    private AsyncHttpClient mAsyncHttpClient;

    public static VolleyHttp init(Context context) {
        if (null == mVolleyHttp) {
            mVolleyHttp = new VolleyHttp(context);
        }
        return mVolleyHttp;
    }

    public static VolleyHttp getInstances() {
        if (null == mVolleyHttp) {
            try {
                throw new NullPointerException(
                        "未初始化VolleyHttp，请在Application中调用VolleyHttp.init(Context context)方法");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return mVolleyHttp;
    }

    private VolleyHttp(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mAsyncHttpClient = new AsyncHttpClient();
    }

    public RequestQueue getRequestQueue() {
        if (null == mRequestQueue) {
            try {
                throw new NullPointerException(
                        "未初始化VolleyHttp，请在Application中调用VolleyHttp.init(Context context)方法");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return mRequestQueue;
    }

    private boolean isNull() {
        if (null == mRequestQueue) {
            try {
                throw new NullPointerException(
                        "未初始化VolleyHttp，请在Application中调用VolleyHttp.init(Context context)方法");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    // public <T> void get(String url, final HttpParams httpParams,
    // final ResponseHandler<T> responseHandler) {
    // StringRequest jr = new StringRequest(Request.Method.GET, url,
    // new Listener<String>() {
    //
    // @Override
    // public void onResponse(String arg0) {
    // LogUtil.e("Volley返回值", arg0);
    // responseHandler.finish(arg0);
    // }
    //
    // }, new Response.ErrorListener() {
    //
    // @Override
    // public void onErrorResponse(VolleyError arg0) {
    // LogUtil.e("Volley错误" + "", arg0.getMessage());
    // responseHandler.error(arg0);
    // }
    // }) {
    // @Override
    // protected Map<String, String> getParams() throws AuthFailureError {
    // // TODO Auto-generated method stub
    // Map<String, String> map = new HashMap<String, String>();
    // for (BasicNameValuePair pair : httpParams.getParamsList()) {
    // map.put(pair.getName(), pair.getValue());
    // }
    // return map;
    // }
    // };
    // mRequestQueue.add(jr);
    // }

    public <T> void post(final String url, final HttpParams httpParams,
                         final ResponseHandler<T> responseHandler) {
        if (!isNull()) {
            LogUtil.i("Volley传参post", url + httpParams.toString());
            responseHandler.start();
            StringRequest jr = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String result) {
                            LogUtil.e("Volley返回值 (" + url + ")", result);
                            responseHandler.finish(result);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LogUtil.i("Volley错误 (" + url + ")", error.getMessage());
                    responseHandler.error(error);
                }
            }) {
                @Override
                protected Map<String, String> getParams()
                        throws AuthFailureError {
                    // TODO Auto-generated method stub
                    Map<String, String> map = new HashMap<String, String>();
                    for (BasicNameValuePair basicNameValuePair : httpParams
                            .getParamsList()) {
                        map.put(basicNameValuePair.getName(),
                                basicNameValuePair.getValue());
                    }
                    return map;
                }
            };
            jr.setRetryPolicy(new DefaultRetryPolicy(
                    15000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mRequestQueue.add(jr);
        }
    }

    public void get(final String url, final HttpParams httpParams,
                    final VolleyCallBack callBack) {
        if (!isNull()) {
            LogUtil.i("Volley传参GET", url + httpParams.toString());
            StringRequest jr = new StringRequest(Request.Method.GET, url + httpParams.toString(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            LogUtil.i("Volley返回值GET (" + url + ")", response);
                            callBack.finish(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LogUtil.i("Volley错误GET (" + url + ")", error.getMessage());
                    callBack.error(error);
                }
            });
            jr.setRetryPolicy(new DefaultRetryPolicy(
                    8000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mRequestQueue.add(jr);
        }
    }

    public <T> void postFile(Context context, final String url, final HttpParams httpParams,
                             final ResponseHandler<T> responseHandler) {
        RequestParams requestParams = new RequestParams();
        for (BasicNameValuePair basicNameValuePair : httpParams.getParamsList()) {
            requestParams.put(basicNameValuePair.getName(), basicNameValuePair.getValue());
        }
        try {
            for (String key : httpParams.getFileList().keySet()) {
                requestParams.put(key, httpParams.getFileList().get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.i("uploadFile传参", url + httpParams.toString());
        mAsyncHttpClient.post(context, url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                LogUtil.i("Volley返回值GET (" + url + ")", result);
                responseHandler.finish(result);
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                responseHandler.onProgress((int) bytesWritten, (int) totalSize);
                super.onProgress(bytesWritten, totalSize);
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                if (null != bytes) {
                    LogUtil.i("Volley错误GET (" + url + ")", new String(bytes));
                }
                responseHandler.error(null);
            }
        });
    }

    public void post(final String url, final HttpParams httpParams,
                     final VolleyCallBack callBack) {
        if (!isNull()) {
            LogUtil.i("Volley传参post", url + httpParams.toString());
            StringRequest jr = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String result) {
                            LogUtil.e("Volley返回值 (" + url + ")", result);
                            callBack.finish(result);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LogUtil.i("Volley错误" + url + "", error.getMessage());
                    callBack.error(error);
                }
            }) {
                @Override
                protected Map<String, String> getParams()
                        throws AuthFailureError {
                    // TODO Auto-generated method stub
                    Map<String, String> map = new HashMap<String, String>();
                    for (BasicNameValuePair basicNameValuePair : httpParams
                            .getParamsList()) {
                        map.put(basicNameValuePair.getName(),
                                basicNameValuePair.getValue());
                    }
                    return map;
                }
            };
            jr.setRetryPolicy(new DefaultRetryPolicy(
                    8000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mRequestQueue.add(jr);
        }
    }
}
