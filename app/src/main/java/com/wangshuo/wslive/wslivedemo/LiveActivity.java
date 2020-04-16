package com.wangshuo.wslive.wslivedemo;

import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;

import me.lake.librestreaming.core.listener.RESConnectionListener;
import me.lake.librestreaming.filter.hardvideofilter.BaseHardVideoFilter;
import me.lake.librestreaming.filter.hardvideofilter.HardVideoGroupFilter;
import me.lake.librestreaming.ws.StreamAVOption;
import me.lake.librestreaming.ws.StreamLiveCameraView;
import me.lake.librestreaming.ws.filter.hardfilter.GPUImageBeautyFilter;
import me.lake.librestreaming.ws.filter.hardfilter.WatermarkFilter;
import me.lake.librestreaming.ws.filter.hardfilter.extra.GPUImageCompatibleFilter;

public class LiveActivity extends AppCompatActivity {
    private StreamLiveCameraView mLiveCameraView;
    private String rtmpUrl = "rtmp://52.17.182.110:1935/livestream/ea9a510c-7ef3-11ea-be3b-b36c03d4ddb1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        StatusBarUtils.setTranslucentStatus(this);

        initLiveConfig();
        new LiveUI(this, mLiveCameraView, rtmpUrl);
    }

    /**
     * Set streaming parameters
     */
    public void initLiveConfig() {
        mLiveCameraView = findViewById(R.id.stream_previewView);

        //Parameter configuration start
        StreamAVOption streamAVOption = new StreamAVOption();
        streamAVOption.streamUrl = rtmpUrl;
        //Parameter configuration end

        mLiveCameraView.init(this, streamAVOption);
        mLiveCameraView.addStreamStateListener(resConnectionListener);
        LinkedList<BaseHardVideoFilter> files = new LinkedList<>();
        //noinspection unchecked
        files.add(new GPUImageCompatibleFilter(new GPUImageBeautyFilter()));
        files.add(new WatermarkFilter(BitmapFactory.decodeResource(getResources(), R.mipmap.live), new Rect(100, 100, 200, 200)));
//        mLiveCameraView.setHardVideoFilter(new HardVideoGroupFilter(files));
    }

    RESConnectionListener resConnectionListener = new RESConnectionListener() {
        @Override
        public void onOpenConnectionResult(int result) {
            //result 0成功  1 失败
            Toast.makeText(LiveActivity.this, "Streaming connection status opened" +
                    "：" + result + " Streaming address：" + rtmpUrl, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWriteError(int errno) {
            Toast.makeText(LiveActivity.this,
                    "An error occurred in the live stream, please try to reconnect",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCloseConnectionResult(int result) {
            //result 0成功  1 失败
            Toast.makeText(LiveActivity.this, "Connection closed："
                    + result, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLiveCameraView.destroy();
    }
}
