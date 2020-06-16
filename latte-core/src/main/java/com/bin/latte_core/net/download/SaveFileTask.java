package com.bin.latte_core.net.download;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.bin.latte_core.app.Latte;
import com.bin.latte_core.net.callback.IRequest;
import com.bin.latte_core.net.callback.ISuccess;
import com.bin.latte_core.utils.FileUtil;

import java.io.File;
import java.io.InputStream;
import java.security.PrivateKey;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @author： libd
 * @date： 2020/6/9 12:43
 * @version: 1.0
 */
public class SaveFileTask extends AsyncTask<Object,Void, File> {

    private final IRequest REQUEST;
    private final ISuccess SUCCESS;

    public SaveFileTask(IRequest REQUEST, ISuccess SUCCESS) {
        this.REQUEST = REQUEST;
        this.SUCCESS = SUCCESS;
    }

    @Override
    protected File doInBackground(Object... params) {

        String downloadDir = (String) params[0];
        String extension = (String) params[1];
        final ResponseBody body = (ResponseBody) params[2];
        final String name = (String) params[3];
        final InputStream is = body.byteStream();
        if(downloadDir == null || "".equals(downloadDir)){
            downloadDir = "down_loads";
        }
        if (extension == null || "".equals(extension)){
            extension = "";
        }
        if(name == null){
            return FileUtil.writeToDisk(is,downloadDir,extension.toUpperCase(),extension);
        }else {
            return FileUtil.writeToDisk(is, downloadDir, name);
        }
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if(SUCCESS != null){
            SUCCESS.onSuccess(file.getPath());
        }
        if(REQUEST != null){
            REQUEST.onRequestEnd();
        }
        autoInstallApk(file);
    }

    private void autoInstallApk(File file){
        if(FileUtil.getExtension(file.getPath()).equals("apk")){
            final Intent install = new Intent();
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setAction(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
//            Latte.getApplicationContext().startActivity(install);


        }
    }
}
