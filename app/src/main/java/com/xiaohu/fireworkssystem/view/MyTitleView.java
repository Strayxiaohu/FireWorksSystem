package com.xiaohu.fireworkssystem.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaohu.fireworkssystem.R;
import com.xiaohu.fireworkssystem.utils.ImageDemoTest;

/**
 * Created by Administrator on 2016/7/29.
 */
public class MyTitleView extends RelativeLayout {
    ImageView leftImage, rightImage;
    TextView name;
    ImageDemoTest imageDemoTest;

    public MyTitleView(Context context) {
        super(context);
    }

    public MyTitleView(Context context, AttributeSet atts) {
        super(context, atts);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_title, this, true);
        imageDemoTest=new ImageDemoTest(context);
        leftImage = (ImageView) view.findViewById(R.id.img_left);
        rightImage = (ImageView) view.findViewById(R.id.img_right);
        name = (TextView) view.findViewById(R.id.title_name);

setupAttributes(atts);
    }

    private void setupAttributes(AttributeSet attrs) {

        //提取自定义属性到typedArray对象中
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs,
                R.styleable.TitleView, 0, 0);
        int leftid = a.getResourceId(R.styleable.TitleView_leftimg, R.mipmap.ic_launcher);
        int rightid = a.getResourceId(R.styleable.TitleView_rightimg, R.mipmap.ic_launcher);


        rightImage.setImageBitmap(getBitmeap(rightid));
        leftImage.setImageBitmap(getBitmeap(leftid));
        name.setText(a.getText(R.styleable.TitleView_name));

        // 将属性赋值给成员变量
        try {
//            shapeColor = a.getColor(R.styleable.CustomView_shapeColor,
//                    Color.BLACK);
//            displayShapeName = a.getBoolean(
//                    R.styleable.CustomView_displayShapeName, false);
        } finally {
            // TypedArray对象是共享的必须被重复利用。
            a.recycle();
        }
    }

    private Bitmap getBitmeap(int Rid) {
        return imageDemoTest.decodeSampledBitmapFromResourse(Rid, 25, 35);
    }

    public void setLeftOnClickListener(OnClickListener listener) {
        leftImage.setVisibility(VISIBLE);
        leftImage.setOnClickListener(listener);
    }

    public void setRightOnClickListener(OnClickListener listener) {
        rightImage.setVisibility(VISIBLE);
        rightImage.setOnClickListener(listener);
    }
}
