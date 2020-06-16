package com.bin.latte_core.utils;

import android.annotation.SuppressLint;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author： libd
 * @date： 2020/6/9 12:55
 * @version: 1.0
 */
public class FileUtil {

    //格式化模板
    public static final String TIME_FORMAT = "_yyyyMMdd_HHmmss";

    public static final String SDCARD_DIR =
            Environment.getRootDirectory().getPath();

    public static File writeToDisk(InputStream is, String downloadDir, String toUpperCase, String extension) {
        File file = FileUtil.createFileByTime(downloadDir,toUpperCase,extension);
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(is);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            byte data[] = new byte[1024 * 4];

            int count;
            while ((count = bis.read(data)) != -1){
                bos.write(data,0,count);
            }

            bos.flush();
            fos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(bos != null){
                    bos.close();
                }
                if(fos != null){
                    fos.close();
                }
                if(bis != null){
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private static File createFileByTime(String downloadDir, String toUpperCase, String extension) {
        String fileName = getFileNameByTime(toUpperCase,extension);
        return createFile(downloadDir,fileName);
    }

    public static File createFile(String sdcardDirName,String fileName){
        return new File(createDir(sdcardDirName),fileName);
    }

    private static File createDir(String sdcardDirName) {
        //拼接成SD卡中完整的dir
        String dir = SDCARD_DIR + "/" + sdcardDirName + "/";
        File fileDir = new File(dir);
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        return fileDir;
    }

    public static File writeToDisk(InputStream is, String dir, String name) {
        File file = FileUtil.createFile(dir,name);
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            byte data[] = new byte[1024 * 4];

            int count;
            while ((count = bis.read(data)) != -1){
                bos.write(data,0,count);
            }

            bos.flush();
            fos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(bos != null){
                    bos.close();
                }
                if(fos != null){
                    fos.close();
                }
                if(bis != null){
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static String getFileNameByTime(String timeFormatHeader,String extension){
        return getTimeFormatName(timeFormatHeader) + "." + extension;
    }

    private static String getTimeFormatName(String timeFormatHeader) {
        Date date = new Date(System.currentTimeMillis());
        //必须加单引号
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("'" + timeFormatHeader + "'" + TIME_FORMAT);
        return dateFormat.format(date);
    }

    /** 
     *  获取文件后缀名
     * @author libd
     * @date 2020/6/9  13:02
     */
    public static String getExtension(String path) {
        String suffix = "";
        File file = new File(path);
        String name = file.getName();
        final int idx = name.lastIndexOf(".");
        if(idx > 0){
            suffix = name.substring(idx + 1);
        }
        return suffix;
    }
}
