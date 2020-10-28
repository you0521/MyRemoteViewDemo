package com.tbz.by.myremoteviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private RemoteControlView mRemoteSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mRemoteSurfaceView.setOnItemClickListener(new RemoteControlView.OnItemClickListener() {
            @Override
            public void onTouch(float item) {
                if (item>0&&item<45){
                    Log.e("fangxiang", "右");
                }else if (item>45&&item<135){
                    Log.e("fangxiang", "下");
                }else if (item>135&&item<225){
                    Log.e("fangxiang", "左");
                }else if (item>225&&item<315){
                    Log.e("fangxiang", "上");
                }else if (item>315&&item<360){
                    Log.e("fangxiang", "右");
                }
            }
        });
    }

    private void initView() {
        mRemoteSurfaceView = (RemoteControlView) findViewById(R.id.remote_surface_view);
    }
}
