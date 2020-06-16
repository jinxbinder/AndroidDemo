package com.bin.latte_core.app;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.WeakHashMap;

import okhttp3.Interceptor;

/** 
 *  配置类
 * @author libd
 * @date 2020/6/6  18:13
 */

public class Configurator {
    private static final WeakHashMap<String,Object> LATTE_CONFIGS = new WeakHashMap<>();
//    private static final ArrayList<IconFontDescriptor> ICONS = new ArrayList<>();
    private static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();

    private Configurator(){
        LATTE_CONFIGS.put(ConfigType.CONFIG_READY.name(),false);
    }
    public static Configurator getInstance(){
        return Holder.INSTANCE;
    }
    final WeakHashMap<String,Object> getLatteConfigs(){
        return LATTE_CONFIGS;
    }

    /** 
     *  静态内部类 线程安全懒汉单例
     * @author libd
     * @date 2020/6/6  18:29
     */

    private static class Holder{
        private static final Configurator INSTANCE = new Configurator();
    }
    public final void configure(){
        LATTE_CONFIGS.put(ConfigType.CONFIG_READY.name(), true);
    }
    public final  Configurator withApiHost(String host){
        LATTE_CONFIGS.put(ConfigType.API_HOST.name(), host);
        return this;
    }
    private void checkConfiguration(){
        final boolean isReady = (boolean) LATTE_CONFIGS.get(ConfigType.CONFIG_READY.name());
        if(!isReady){
            throw new RuntimeException("Configuration is not ready,call configure");
        }
    }
    @SuppressWarnings("unchecked")
    final <T> T getConfiguration(Enum<ConfigType> key){
        checkConfiguration();
        return (T) LATTE_CONFIGS.get(key.name());
    }

    public final Configurator withInterceptor(Interceptor interceptor){
        INTERCEPTORS.add(interceptor);
        LATTE_CONFIGS.put(ConfigType.INTERCEPTOR.name(), INTERCEPTORS);
        return this;
    }

    public final Configurator withInterceptors(ArrayList<Interceptor> interceptors){
        INTERCEPTORS.addAll(interceptors);
        LATTE_CONFIGS.put(ConfigType.INTERCEPTOR.name(), INTERCEPTORS);
        return this;
    }
}
