package com.hjc.baselibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.hjc.baselibrary.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutionException;

/**
 * 图片请求参数拼接类
 */
public class ImageUtils {

    private ImageUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("ImageUtils cannot be instantiated");
    }

    /**
     * @Title: checkFileIsExits
     * @Description: 检测图片是否存在
     * @param @param filename
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public static boolean checkFileIsExits(Context context, String filename) {
        String sDir = Environment.getExternalStorageDirectory() + File.separator + context.getString(R.string.app_name);
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            File destDir = new File(sDir);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            String filepath = sDir + File.separator + filename;
            File mIvFile = new File(filepath);
            if (mIvFile.exists()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 取缩放比例
     *
     * @param maxwidth
     * @param width
     * @return
     */
    public static double Geometric(int maxwidth, int width) {
        double scale = 1;
        scale = (double) maxwidth / (double) width;
        scale = scale > 1 ? scale : 1;
        return scale;
    }

    /**
     * 拼接ImgUrl，将需要显示的宽传给服务器
     *
     * @param url
     * @return
     */
    public static String composeImgUrl(String url, int width) {
        return new StringBuilder().append(url).append("&width=").append(width).toString();
    }

    /**
     * 点击image保存图片
     */
    public static boolean saveFile(Context mcontext, String url, String filename) {
        try {

            // 检查手机上是否有外部存储卡
            boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
            if (sdCardExist) {

                String sDir = Environment.getExternalStorageDirectory() + File.separator + AppUtils.getAppName(mcontext);

                File destDir = new File(sDir);
                if (!destDir.exists()) {
                    if (!destDir.mkdirs()) {
                        return false;
                    }
                }

                Log.e("Download", url);
                File downloadedFile = Glide.with(mcontext)
                        .load(url)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
                Log.e("Download", downloadedFile.getAbsolutePath());

                String filepath = sDir + File.separator + filename;
                Log.e("Download", filepath);

                FileChannel in = null;
                FileChannel out = null;
                FileInputStream inStream = null;
                FileOutputStream outStream = null;
                try {
                    inStream = new FileInputStream(downloadedFile);
                    outStream = new FileOutputStream(filepath);
                    in = inStream.getChannel();
                    out = outStream.getChannel();
                    in.transferTo(0, in.size(), out);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    // TODO 写一个流关闭方法
                    if (inStream != null) {
                        try {
                            inStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            inStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (outStream != null) {
                        try {
                            inStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (out != null) {
                        try {
                            inStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // 发送相册广播
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File file = new File(filepath);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                mcontext.sendBroadcast(intent);
                return true;
            } else {
                return false;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 点击image保存图片
     */
    public static boolean saveFile(Context mcontext, ImageView image, String filename) {
        Bitmap obmp;
        // ImageView对象(iv_photo)必须做如下设置后，才能获取其中的图像
        image.setDrawingCacheEnabled(true);
        // 在ImageView对象(iv_photo)被touch down的时候，获取ImageView中的图像
        obmp = Bitmap.createBitmap(image.getDrawingCache());
        // 然后在OK按钮(btn_photo)被touch
        // down的时候，比较ImaageView对象(iv_photo)中的图像和
        // obmp是否一致，以便做进一步的处理，比如，如果不一致就保存，否则就不保存到数据库中。
        // 从ImaggeView对象中获取图像后，要记得调用setDrawingCacheEnabled(false)清空画图缓
        // 冲区，否则，下一次用getDrawingCache()方法回去图像时，还是原来的图像
        image.setDrawingCacheEnabled(false);
        // 将得到obmp写入文件
        FileOutputStream m_fileOutPutStream = null;
        String sDir = Environment.getExternalStorageDirectory() + File.separator + AppUtils.getAppName(mcontext);
        // 检查手机上是否有外部存储卡
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            File destDir = new File(sDir);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            String filepath = sDir + File.separator + filename;
//            File mIvFile = new File(filepath);
//            if (mIvFile.exists()) {
//                return true;
//            }
            try {
                m_fileOutPutStream = new FileOutputStream(filepath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            obmp.compress(Bitmap.CompressFormat.PNG, 100, m_fileOutPutStream);
            try {
                m_fileOutPutStream.flush();
                m_fileOutPutStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 发送相册广播
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File file = new File(filepath);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            mcontext.sendBroadcast(intent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 点击image保存图片
     */
    public static void saveFile(Context mcontext, ImageView image, String filename, String alter_success,
                                String alter_fail, String alter_saved) {
        Bitmap obmp;
        // ImageView对象(iv_photo)必须做如下设置后，才能获取其中的图像
        image.setDrawingCacheEnabled(true);
        // 在ImageView对象(iv_photo)被touch down的时候，获取ImageView中的图像
        obmp = Bitmap.createBitmap(image.getDrawingCache());
        // 然后在OK按钮(btn_photo)被touch
        // down的时候，比较ImaageView对象(iv_photo)中的图像和
        // obmp是否一致，以便做进一步的处理，比如，如果不一致就保存，否则就不保存到数据库中。
        // 从ImaggeView对象中获取图像后，要记得调用setDrawingCacheEnabled(false)清空画图缓
        // 冲区，否则，下一次用getDrawingCache()方法回去图像时，还是原来的图像
        image.setDrawingCacheEnabled(false);
        // 将得到obmp写入文件
        FileOutputStream m_fileOutPutStream = null;
        String sDir = Environment.getExternalStorageDirectory() + File.separator + AppUtils.getAppName(mcontext);
        // 检查手机上是否有外部存储卡
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            File destDir = new File(sDir);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            String filepath = sDir + File.separator + filename;
            File mIvFile = new File(filepath);
            if (mIvFile.exists()) {
                ToastUtils.show(mcontext, alter_saved, 5000);
                return;
            }
            try {
                m_fileOutPutStream = new FileOutputStream(filepath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            obmp.compress(Bitmap.CompressFormat.PNG, 100, m_fileOutPutStream);
            try {
                m_fileOutPutStream.flush();
                m_fileOutPutStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 发送相册广播
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File file = new File(filepath);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            mcontext.sendBroadcast(intent);
            ToastUtils.showShort(mcontext, alter_success);
        } else {
            ToastUtils.show(mcontext, alter_fail, 5000);
        }
    }


}
