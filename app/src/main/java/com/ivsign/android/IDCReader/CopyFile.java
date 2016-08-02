package com.ivsign.android.IDCReader;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2015/12/1.
 */
public class CopyFile {
    public static void FileCopy(Context context, InputStream in, String name) {
        try {
            File paths = new File(context.getFilesDir().getAbsolutePath() + "/wltlib/");
            String p = context.getFilesDir().getAbsolutePath();
            if (!paths.isDirectory()) {
                paths.mkdir();
            }
            File path_xml = new File(paths, name);
            if (!path_xml.exists()) {
                FileOutputStream fos = new FileOutputStream(path_xml);
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = in.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                in.close();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void CopyWltlib(Context context) {
        InputStream lic = context.getClass().getClassLoader().getResourceAsStream("assets/wltlib/license.lic");
        InputStream dat = context.getClass().getClassLoader().getResourceAsStream("assets/wltlib/base.dat");
        FileCopy(context, lic, "license.lic");
        FileCopy(context, dat, "base.dat");
    }

}
