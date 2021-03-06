package com.bin.latte_core.app;

import android.content.Context;

import java.util.ArrayList;
import java.util.WeakHashMap;

import okhttp3.Interceptor;

public final class Latte {
    public static Configurator init(Context context){
        getConfigurations().put(ConfigType.APPLICATION_CONTEXT.name(), context.getApplicationContext());
        return Configurator.getInstance();
    }

    public static WeakHashMap<String,Object> getConfigurations(){
        return Configurator.getInstance().getLatteConfigs();
    }


    public static ArrayList<Interceptor> getConfiguration(ConfigType interceptor) {
        return null;
    }
}
