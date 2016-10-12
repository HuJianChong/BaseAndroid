package com.hjc.baselibrary.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.ContextCompat;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * SD卡相关的辅助类
 */
public class SDCardUtils {
    public static final long MIN_STORAGE_SIZE = 1024 * 1024 * 10; // 最小SD卡剩余空间，10M

    private SDCardUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("SDCardUtils cannot be instantiated");
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    public static boolean isSDCardStorageSufficient() {
        return getSdcardUsableSpace() >= MIN_STORAGE_SIZE;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    @SuppressWarnings("deprecation")
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }


    /**
     * 获取设备可存储空间的appCache完成路径(不是sd卡)
     *
     * @param context
     * @param dirName 表示文件夹名，不是全路径
     * @return app_cache_path/dirName
     */
    public static String getAppCacheDir(Context context, String dirName) {
        return context.getCacheDir().getPath() + File.separator + dirName;
    }

    /**
     * 获取设备可存储空间的appCache完成路径
     *
     * @param context
     * @param dirName 表示文件夹名，不是全路径
     * @return app_cache_path/dirName
     */
    public static String getDiskCacheDir(Context context, String dirName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(context, null);
            if (externalFilesDirs != null && externalFilesDirs.length > 0 && externalFilesDirs[0] != null) {
                cachePath = externalFilesDirs[0].getAbsolutePath();
            } else {
                cachePath = context.getCacheDir().getPath();
               Logger.i("cachePath: " + cachePath);
            }
        } else {
            cachePath = context.getCacheDir().getPath();
           Logger.i("cachePath: " + cachePath);
        }
        return cachePath + File.separator + dirName;
    }

    /**
     * 路径下的可存储剩余空间
     *
     * @param dir
     * @return
     */
    @SuppressWarnings("deprecation")
    public static long getAvailableSpace(File dir) {
        try {
            final StatFs stats = new StatFs(dir.getPath());
            return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
        } catch (Throwable e) {
            return -1;
        }

    }

    /**
     * 判断sd卡是否存在
     *
     * @return true:存在；false:不存在
     */
    public static boolean sdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡根目录
     *
     * @return
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = sdCardExist(); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取根目录
        } else {
            return null;
        }
        return sdDir.toString();

    }

    /**
     * 吧输入流保存到本地文件
     *
     * @param file
     * @param in
     * @return
     * @throws IOException
     * @throws
     * @Title: saveInputstream
     */
    public static boolean saveInputstream(File file, InputStream in) {

        if (file == null || in == null) {
            return false;
        }

        OutputStream out = null;

        try {
            if (!file.exists()) {
                File parentFile = file.getParentFile();
                if (!parentFile.exists())
                    parentFile.mkdirs();
                file.delete();
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }

            out = new FileOutputStream(file);
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

            out.flush();
        } catch (IOException e) {
            return false;
        } finally {
            try {
                in.close();

                if (out != null) {
                    out.close();
                }

            } catch (IOException e) {
                return false;
            }

        }

        // 如果不存在，也有可能图片文件目录也不存在

        return true;
    }

    /**
     * 获取文件路径空间大小
     *
     * @return
     * @parampath
     */
    @SuppressWarnings("deprecation")
    public static long getSdcardUsableSpace() {
        long availableSize = 0;

        try {
            final StatFs stats = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long blockSize = stats.getBlockSize();
            long alBlock = stats.getAvailableBlocks();
            availableSize = blockSize * alBlock;
        } catch (Exception e) {
            availableSize = 0;
        }

        return availableSize;
    }

}
