package com.bin.latte_core.net.download;

import android.os.AsyncTask;

import com.bin.latte_core.net.RestCreator;
import com.bin.latte_core.net.callback.IError;
import com.bin.latte_core.net.callback.IFailure;
import com.bin.latte_core.net.callback.IRequest;
import com.bin.latte_core.net.callback.ISuccess;

import java.security.cert.Extension;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author： libd
 * @date： 2020/6/9 12:37
 * @version: 1.0
 */
public class DownloadHandler {

    private final String URL;
    private static final Map<String,Object> PARAMS = RestCreator.getParams();
    private final IRequest IREQUEST;
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String NAME;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;

    public DownloadHandler(String url,
                           IRequest request,
                           String dir,
                           String extension,
                           String name,
                           ISuccess success,
                           IFailure failure,
                           IError error) {
        this.URL = url;
        this.IREQUEST = request;
        this.DOWNLOAD_DIR = dir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
    }

    public final void handleDownload(){
        if(IREQUEST != null){
            IREQUEST.onRequestStart();
        }

        RestCreator.getRestService().download(URL, PARAMS)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            final ResponseBody responseBody = response.body();

                            final SaveFileTask task = new SaveFileTask(IREQUEST, SUCCESS);
                            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,DOWNLOAD_DIR,EXTENSION, response,NAME);

                            //不判断下载不全
                            if (task.isCancelled()){
                                if (IREQUEST != null){
                                    IREQUEST.onRequestEnd();
                                }
                            }
                        }else {
                            if(ERROR != null){
                                ERROR.onError(response.code(), response.message());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if(FAILURE != null){
                            FAILURE.onFailure();
                        }
                    }
                });
    }
}
