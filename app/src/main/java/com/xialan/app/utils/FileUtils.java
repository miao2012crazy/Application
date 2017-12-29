package com.xialan.app.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/10/24.
 */

public class FileUtils {
    /**
     * 获取指定文件大小
     * @param
     * @return
     * @throws Exception 　　
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }
    /**
     * 获取指定文件夹
     * @param f
     * @return
     * @throws Exception
     *
     */
    public static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     * @param fileS
     * @return
     *
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 根据文件名获得文件的扩展名
     *
     * @param fileName
     *            文件名
     * @return 文件扩展名（不带点）
     */
    public static String getFileSuffix(String fileName)
    {
        int index = fileName.lastIndexOf(".");
        String suffix = fileName.substring(index + 1, fileName.length());
        return suffix;
    }


    /**
     * 重命名文件
     *
     * @param oldPath
     *            旧文件的绝对路径
     * @param newPath
     *            新文件的绝对路径
     * @return 文件重命名成功则返回true
     */
    public static boolean renameTo(String oldPath, String newPath)
    {
        if (oldPath.equals(newPath))
        {
            return false;
        }
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);

        boolean isSuccess = oldFile.renameTo(newFile);
        return isSuccess;
    }

    /**
     * 重命名文件
     *
     * @param oldFile
     *            旧文件对象
     * @param newFile
     *            新文件对象
     * @return 文件重命名成功则返回true
     */
    public static boolean renameTo(File oldFile, File newFile)
    {
        if (oldFile.equals(newFile))
        {

            return false;
        }
        boolean isSuccess = oldFile.renameTo(newFile);
        return isSuccess;
    }

    /**
     * 获取某个路径下的文件列表
     *
     * @param path
     *            文件路径
     * @return 文件列表File[] files
     */
    public static File[] getFileList(String path)
    {
        File file = new File(path);
        if (file.isDirectory())
        {
            File[] files = file.listFiles();
            if (files != null)
            {
                return files;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * 删除文件夹及其包含的所有文件
     *
     * @param file
     * @return
     */
    public static boolean deleteFolder(File file)
    {
        boolean flag = false;
        File files[] = file.listFiles();
        if (files != null && files.length >= 0) // 目录下存在文件列表
        {
            for (int i = 0; i < files.length; i++)
            {
                File f = files[i];
                if (f.isFile())
                {
                    // 删除子文件
                    flag = deleteFile(f);
                    if (flag == false)
                    {
                        return flag;
                    }
                }
                else
                {
                    // 删除子目录
                    flag = deleteFolder(f);
                    if (flag == false)
                    {
                        return flag;
                    }
                }
            }
        }
        flag = file.delete();
        if (flag == false)
        {
            return flag;
        }
        else
        {
            return true;
        }
    }

    /**
     * 删除单个文件
     *
     * @param file
     *            要删除的文件对象
     * @return 文件删除成功则返回true
     */
    public static boolean deleteFile(File file)
    {
        if (file.exists())
        {
            boolean isDeleted = file.delete();
            Log.w("miao", file.getName() + "删除结果：" + isDeleted);
            return isDeleted;
        }
        else
        {
            Log.w("miao", "文件删除失败：文件不存在！");
            return false;
        }
    }

    /**
     * 删除单个文件
     *
     * @param path
     *            文件所在路径名
     * @param fileName
     *            文件名
     * @return 删除成功则返回true
     */
    public static boolean deleteFile(String path, String fileName)
    {
        File file = new File(path + File.separator + fileName);
        if (file.exists())
        {
            boolean isDeleted = file.delete();
            return isDeleted;
        }
        else
        {
            return false;
        }
    }
}
