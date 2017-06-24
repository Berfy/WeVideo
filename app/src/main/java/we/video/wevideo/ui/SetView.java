package we.video.wevideo.ui;

import android.content.Context;
import android.net.TrafficStats;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import we.video.wevideo.BaseView;
import we.video.wevideo.R;
import we.video.wevideo.cons.Constants;
import we.video.wevideo.ui.support.switchButton.SwitchButton;
import we.video.wevideo.util.FileUtils;
import we.video.wevideo.util.LogUtil;
import we.video.wevideo.util.PopupWindowUtil;
import we.video.wevideo.util.StringUtil;
import we.video.wevideo.util.TempSharedData;

/**
 * Created by Berfy on 2016/4/7.
 * 设置
 */
public class SetView extends BaseView {

    private SwitchButton sBtn_push, sBtn_liuliang;
    private PopupWindowUtil popupWindowUtil;

    public SetView(Context context) {
        super(context);
        setContent(R.layout.view_set);
    }

    @Override
    protected void initView() {
        popupWindowUtil = new PopupWindowUtil(mContext);
        findViewById(R.id.layout_push).setOnClickListener(this);
        findViewById(R.id.layout_liuliang).setOnClickListener(this);
        findViewById(R.id.layout_cache).setOnClickListener(this);
        findViewById(R.id.layout_about).setOnClickListener(this);
        findViewById(R.id.layout_contact).setOnClickListener(this);

        sBtn_push = ((SwitchButton) findViewById(R.id.sbtn_push));
        sBtn_liuliang = ((SwitchButton) findViewById(R.id.sbtn_liuliang));

        sBtn_push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TempSharedData.getInstance(mContext).put(Constants.TEMP_ISPUSH, isChecked);
            }
        });
        sBtn_liuliang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TempSharedData.getInstance(mContext).put(Constants.TEMP_ISLIULIANG, isChecked);
            }
        });
    }

    public void updateData() {
        LogUtil.e("开关", TempSharedData.getInstance(mContext).get(Constants.TEMP_ISPUSH, true) + "   " + TempSharedData.getInstance(mContext).get(Constants.TEMP_ISLIULIANG, true));
        sBtn_push.setChecked(TempSharedData.getInstance(mContext).get(Constants.TEMP_ISPUSH, true));
        sBtn_liuliang.setChecked(TempSharedData.getInstance(mContext).get(Constants.TEMP_ISLIULIANG, true));
        ((TextView) findViewById(R.id.tv_liuliang)).setText(StringUtil.
                fomatScale((float) (((TrafficStats.getMobileTxBytes() + TrafficStats.getMobileTxBytes()) * 0.1) / 102.4 / 1024)) + "MB");
        ((TextView) findViewById(R.id.tv_cache)).setText(StringUtil.
                fomatScale((float) ((FileUtils.getDirSize(FileUtils.getFilePath(mContext, Constants.TEMP_PATH)) * 0.1) / 102.4 / 1024)) + "MB");
    }

    @Override
    protected void doClickEvent(View v) {
        switch (v.getId()) {
            case R.id.layout_push:
                sBtn_push.setChecked(!sBtn_push.isChecked());
                break;
            case R.id.layout_liuliang:
                sBtn_liuliang.setChecked(!sBtn_liuliang.isChecked());
                break;
            case R.id.layout_cache:
                popupWindowUtil.showTipChoosePop(getView(), "提示", "确定清空缓存吗？", "确定", "取消", new PopupWindowUtil.OnPopListener() {
                    @Override
                    public void ok() {
                        FileUtils.deleteFolder(FileUtils.getFilePath(mContext, Constants.TEMP_PATH));
                        updateData();
                    }

                    @Override
                    public void cancel() {

                    }
                });
                break;
            case R.id.layout_about:
                break;
            case R.id.layout_contact:
                break;
        }
    }
}
