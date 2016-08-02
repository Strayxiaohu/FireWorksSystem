package com.xiaohu.fireworkssystem.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.xiaohu.fireworkssystem.config.MyKeys;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/8/1.
 */
public class Utils {
    Context context;

    public Utils(Context con) {
        context = con;
    }
    /**
     * 设置TotalListView(自定义)的高度
     *
     * @param listView
     */
    public static void setListViewHeight(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    /**
     * 根据键值对读取存储在本地的数据
     *
     * @param key 键
     * @return 存储的值
     */
    public  String ReadString(String key) {
        SharedPreferences sp = context.getSharedPreferences(MyKeys.KEY_Preferences_name,
                Context.MODE_PRIVATE);
        if (sp != null) {
            return sp.getString(key, "");
        } else {
            return "";
        }

    }
    /**
     * 将图片bitmap转换为base64字符串
     * http://bbs.3gstdy.com
     *
     * @param bitmap
     * @return 根据url读取出的图片的Bitmap信息
     */
    public static String encodeBitmap(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,80, baos);
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
                    .trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 检查蓝牙设备
     * http://bbs.3gstdy.com
     *
     * @param context,requestcode
     * @return
     */
    public static boolean checkBluetooth(Activity context, int requestCode) {
        /*
         * Intent serverIntent = new Intent(context, DeviceListActivity.class);
		 * context.startActivity(serverIntent); return true;
		 */

        boolean result = true;
        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        if (null != ba) {
            if (!ba.isEnabled()) {
                result = false;
                Intent intent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                context.startActivityForResult(intent, requestCode);// 或者ba.enable();
                // //同样的关闭WIFi为ba.disable();
            }
        }
        return result;
    }
    /**
     * 将内容以键值对的形式存储在本地
     *
     * @param key   键
     * @param value 值
     */
    public  void WriteString(String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(MyKeys.KEY_Preferences_name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }
    /**
     * 对URL进行编码操作
     *
     * @param text
     * @return
     */
    public static String URLEncodeImage(String text) {
        if (Utils.isEmptyString(text))
            return "";

        return URLEncoder.encode(text);
    }
    /**
     * 判断字符串是否为空,为空返回空串
     * http://bbs.3gstdy.com
     *
     * @param text
     * @return
     */
    public static String URLEncode(String text) {
        if (isEmptyString(text))
            return "";
        if (text.equals("null"))
            return "";
        return text;
    }
    /**
     * 判断字符串是否为空
     * http://bbs.3gstdy.com
     *
     * @param str
     * @return
     */
    public static boolean isEmptyString(String str) {
        return (str == null || str.length() == 0);
    }


}
