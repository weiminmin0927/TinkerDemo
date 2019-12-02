package com.lihang.tinkerstu;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {
    private RxPermissions rxPermissions;
    private boolean keyPress = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {

                    //        patch_signed_7zip.apk
                    String path = "/sdcard/Tinker/";
                    File dir = new File(path);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    File file = new File(path, "patch_signed_7zip.apk");
                    if (file.exists()) {
                        if (file.length() > 0) {
                            Log.e("我就想看看路径", file.getAbsolutePath());
                            TinkerInstaller.onReceiveUpgradePatch(MainActivity.this, file.getAbsolutePath());
                        }
                    }

                } else {
                    Toast.makeText(MainActivity.this, "热修复技术需要用到此权限", Toast.LENGTH_SHORT).show();
                }

            }
        });


        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "了了分明", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("祝福:")
                        .setMessage("西安大地测绘股份有限公司")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
    }

    /**
     * 按键执行操作，连续两次点击退出程序
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断按下的键是否是返回键
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(keyPress){
                Toast.makeText(MainActivity.this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
                keyPress = false;
                // 使用定时器修改keyPress的值，按下两秒后将keyPress设为true
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        keyPress = true;
                    }
                },2000);
            } else {
                android.os.Process.killProcess(android.os.Process.myPid());
                // 关闭页面
                finish();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
