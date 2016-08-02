package com.xiaohu.fireworkssystem.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.xiaohu.fireworkssystem.R;
import com.xiaohu.fireworkssystem.adapter.CommonAdapter;
import com.xiaohu.fireworkssystem.adapter.ViewHolder;
import com.xiaohu.fireworkssystem.model.BlueToothModel;
import com.xiaohu.fireworkssystem.utils.Utils;

import java.util.List;

/**
 * 自定义Dialog只有listview
 * Created by Administrator on 2016/5/24.
 */
public class SpinnerDialog extends Dialog {
    TotalListView listView;
    Context mContext;
    OnItemClick mClick;
    SpinnerDialog dialog;

    public SpinnerDialog(Context context) {
        super(context, R.style.Base_Theme_AppCompat_Light_Dialog);
        mContext = context;
        dialog = this;
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_dialog, null);
        listView = (TotalListView) mView.findViewById(R.id.dialog_lv);
        super.setContentView(mView);
    }

    public void setListView(final List<BlueToothModel> list) {
        CommonAdapter<BlueToothModel> mAdapter = new CommonAdapter<BlueToothModel>(mContext, list, R.layout.spinner_dialog_item) {
            @Override
            public void convert(ViewHolder holder, BlueToothModel model, int position) {
                holder.setText(R.id.dialog_val_tv, model.getDeviceName());
            }
        };
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mClick.onClick(position, list.get(position));
                dialog.dismiss();
            }
        });
        Utils.setListViewHeight(listView);//设置高度
    }

    public void setOnItemClick(OnItemClick mClick) {
        this.mClick = mClick;
    }

    public interface OnItemClick {
        void onClick(int position, BlueToothModel model);
    }
}
