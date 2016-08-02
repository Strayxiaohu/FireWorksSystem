package com.xiaohu.fireworkssystem.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Administrator on 2015/10/28.
 */
public class ImageDemoTest {
    Context myContext;
    int imageHeight = 0;
    int imageWidth = 0;
    String imageType = "";

    public ImageDemoTest(Context context) {
        myContext = context;
    }

    //得到图片的宽，高，类型
    public void getImageType(int rid) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(myContext.getResources(), rid);
        imageHeight = options.outHeight;
        imageWidth = options.outWidth;
        imageType = options.outMimeType;
    }

    //根据目标计算缩放比例系数
    public int calculateInSampleSize(int iWidth, int iHeight) {
        int inSampleSize = 1;

        if (imageWidth > iWidth || imageHeight > iHeight) {
            int heightRatio = Math.round((float) imageHeight / (float) iHeight);
            int widthRatio = Math.round((float) imageWidth / (float) iWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    //对图片进行解码注意，真正解码时需要把inJustDecodeBounds属性重置为false，
    // 这样就可以把一张十分巨大的图轻松显示在一个100x100的ImageView中了
    public Bitmap decodeSampledBitmapFromResourse( int iId, int iWidth, int iHeight) {
       // System.out.println("888888888888888888888");
        Resources res=myContext.getResources();
        //getImageType(iId);
        BitmapFactory.Options options = new BitmapFactory.Options();

        //BitmapFactory.decodeResource(res, iId, options);
        options.inSampleSize =9;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, iId, options);
    }
}
