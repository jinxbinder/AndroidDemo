package com.bin.latte_core.net;

import android.content.Context;
import android.print.PrinterId;

import com.bin.latte_core.net.callback.IError;
import com.bin.latte_core.net.callback.IFailure;
import com.bin.latte_core.net.callback.IRequest;
import com.bin.latte_core.net.callback.ISuccess;
import com.bin.latte_core.net.callback.RequestCallbacks;
import com.bin.latte_core.net.download.DownloadHandler;
import com.bin.latte_core.ui.LatteLoader;
import com.bin.latte_core.ui.LoaderStyle;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * @author： libd
 * @date： 2020/6/7 18:44
 * @version: 1.0
 */
public class RestClient {

    private final String URL;
    private static final Map<String,Object> PARAMS = RestCreator.getParams();
    private final IRequest IREQUEST;
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String NAME;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final RequestBody BODY;
    private final LoaderStyle LOADER_STYLE;
    private final File FILE;
    private final Context CONTEXT;


    public RestClient(String url,
                      Map<String, Object> params,
                      IRequest request,
                      String downloadDir,
                      String extension,
                      String name,
                      ISuccess success,
                      IFailure failure,
                      IError error,
                      RequestBody body,
                      Context context,
                      LoaderStyle loaderStyle,
                      File file) {
        this.URL = url;
        PARAMS.putAll(params);
        this.IREQUEST = request;
        this.DOWNLOAD_DIR = downloadDir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.BODY = body;
        this.CONTEXT = context;
        this.FILE = file;
        this.LOADER_STYLE = loaderStyle;
    }

    public static RestClientBuilder builder(){
        return new RestClientBuilder();
    }

    private void request(HttpMethod method){
        final RestService service = RestCreator.getRestService();
        Call<String> call = null;

        if(IREQUEST != null){
            IREQUEST.onRequestStart();
        }

        if(LOADER_STYLE != null){
            LatteLoader.showLoading(CONTEXT, LOADER_STYLE);
        }

        switch (method){
            case GET:
                call = service.get(URL, PARAMS);
                break;
            case POST:
                call = service.post(URL, PARAMS);
                break;
            case POST_RAW:
                call = service.postRaw(URL, BODY);
                break;
            case PUT:
                call = service.put(URL, PARAMS);
                break;
            case PUT_RAW:
                call = service.putRaw(URL, BODY);
                break;
            case DELETE:
                call = service.delete(URL, PARAMS);
                break;
            case UPLOAD:
                final RequestBody requestBody =
                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), FILE);
                final MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", FILE.getName());
                break;
            default:
                break;

        }

        if(call != null){
            call.enqueue(getRequestCallback());
        }
    }

    private Callback<String> getRequestCallback(){
        return new RequestCallbacks(IREQUEST, SUCCESS, FAILURE, ERROR,LOADER_STYLE);
    }

    public final void get(){
        request(HttpMethod.GET);
    }

    public final void post(){
        if(BODY == null){
            request(HttpMethod.POST);
        }else {
            if(!PARAMS.isEmpty()){
                throw new RuntimeException("PARAMS MUST BE NULL!");
            }
            request(HttpMethod.POST_RAW);
        }
    }

    public final void put(){
        if(BODY == null){
            request(HttpMethod.PUT);
        }else {
            if(!PARAMS.isEmpty()){
                throw new RuntimeException("PARAMS MUST BE NULL!");
            }
            request(HttpMethod.PUT_RAW);
        }
    }

    public final void delete(){
        request(HttpMethod.DELETE);
    }

    public final void download(){
        new DownloadHandler(URL, IREQUEST, DOWNLOAD_DIR, EXTENSION, NAME, SUCCESS, FAILURE, ERROR);
    }
}
