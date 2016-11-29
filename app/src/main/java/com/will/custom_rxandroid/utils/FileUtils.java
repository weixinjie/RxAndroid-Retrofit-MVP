package com.will.custom_rxandroid.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by will on 2016/11/26.
 */

public class FileUtils {

    public static void copyfile(File fromFile, File toFile, Boolean rewrite)


    {

        if (!fromFile.exists()) {

            return;

        }

        if (!fromFile.isFile()) {

            return;

        }

        if (!fromFile.canRead()) {

            return;

        }

        if (!toFile.getParentFile().exists()) {

            toFile.getParentFile().mkdirs();

        }

        if (toFile.exists() && rewrite) {

            toFile.delete();

        }

//当文件不存时，canWrite一直返回的都是false

        // if (!toFile.canWrite()) {

// MessageDialog.openError(new Shell(),"错误信息","不能够写将要复制的目标文件" + toFile.getPath());

// Toast.makeText(this,"不能够写将要复制的目标文件", Toast.LENGTH_SHORT);

// return ;

// }

        try {

            java.io.FileInputStream fosfrom = new java.io.FileInputStream(fromFile);

            java.io.FileOutputStream fosto = new FileOutputStream(toFile);

            byte bt[] = new byte[1024];
            int c;

            while ((c = fosfrom.read(bt)) > 0) {

                fosto.write(bt, 0, c); //将内容写到新文件当中

            }

            fosfrom.close();

            fosto.close();

        } catch (Exception ex) {

            Log.e("readfile", ex.getMessage());

        }

    }
}
