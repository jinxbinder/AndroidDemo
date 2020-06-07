package com.bin.latte_core.net;

import com.bin.latte_core.app.ConfigType;
import com.bin.latte_core.app.Latte;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.http.OkHeaders;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author： libd
 * @date： 2020/6/7 19:04
 * @version: 1.0
 */
public class RestCreator {

    public static RestService getRestService(){
        return RestServiceHolder.REST_SERVICE;
    }

    private static final class RetrofitHolder {
        private static final String BASE_URL = (String) Latte.getConfigurations().get(ConfigType.API_HOST.name());
        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    private static final class OkHttpHolder{
        private static final int TIME_OUT = 60;
        private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    private static final class RestServiceHolder{
        private static final RestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);

    }
}