package com.xiaohu.fireworkssystem.ui.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xiaohu.fireworkssystem.R;
import com.xiaohu.fireworkssystem.config.MyKeys;
import com.xiaohu.fireworkssystem.model.BlueToothModel;
import com.xiaohu.fireworkssystem.model.PersonModel;
import com.xiaohu.fireworkssystem.utils.Utils;
import com.xiaohu.fireworkssystem.view.DatePickerDialog;
import com.xiaohu.fireworkssystem.view.SpinnerDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ivsign.android.IDCReader.CopyFile;
import com.ivsign.android.IDCReader.Cvr100bMYTask;
import com.ivsign.android.IDCReader.Cvr100bTask;
import com.xiaohu.fireworkssystem.view.scan.CaptureActivity;

/**
 * Created by Administrator on 2016/8/1.
 * 知识点总结整理
 */
public class TestActivity extends BaseActivity {
    SpinnerDialog Contentdialog;//选择
    Button btnSelector, btnRead, btnBluetooth,btnScan;
    DatePickerDialog dateDialog;//时间选择控件
    EditText txt_time;
    List<BlueToothModel> listBlue;

    @Override
    public void initView() {
        setContentView(R.layout.test_layout);
        //时间选择控件
        btnSelector = (Button) findViewById(R.id.btn_time_selecter);
        //显示时间
        txt_time = (EditText) findViewById(R.id.txt_time);
        //读取身份证
        btnRead = (Button) findViewById(R.id.btn_readCard);
        //选择蓝牙设备
        btnBluetooth = (Button) findViewById(R.id.btn_select_bluetooth);
        btnScan= (Button) findViewById(R.id.btn_scan);
    }

    @Override
    public void initEvent() {
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TestActivity.this, CaptureActivity.class);
                startActivity(intent);
            }
        });
        CopyFile.CopyWltlib(this);
        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contentdialog = new SpinnerDialog(TestActivity.this);
                getBluetooth();
            }
        });
        btnSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //字符串为空，默认为当前系统时间
                dateDialog = new DatePickerDialog(TestActivity.this, "");
                dateDialog.datePickerDialog(txt_time);//直接改变edittext的控件的值
            }
        });
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到选择蓝牙设备页面
                //读取身份证
                if (utils.ReadString(MyKeys.KEY_BlueToothAddress).equals("")) {
                    ToastShort("请检查蓝牙读卡设备设置！");
                } else {
                    onReadCardCvr();
                }
            }
        });
    }

    /**
     * 获得蓝牙的相关信息
     */
    private void getBluetooth() {
        BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            ToastShort("不支持蓝牙设备！");
        } else {
            Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
            if (pairedDevices.size() == 0) {
                ToastShort("没有配对蓝牙设备！");
                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
            }
            // blue_device_scale = new String[pairedDevices.size()];
            int count = 0;
            listBlue = new ArrayList<BlueToothModel>();
            //listAddress = new ArrayList<String>();
            for (BluetoothDevice device : pairedDevices) {
//                BlueToothModel blue = new BlueToothModel();
//                blue.setDeviceName(device.getName());
//                blue.setDeviceAddress(device.getAddress());
                listBlue.add(new BlueToothModel(device.getName(), device.getAddress()));
                // listAddress.add(device.getAddress());
                // blue_device_scale[count++] = device.getName() + "\n" + device.getAddress();
            }
            //蓝牙设置选择
            // MainActivity.mInstance.ToastShort("list size:"+listblue.size());
            if (listBlue.size() > 0) {
                Contentdialog.setListView(listBlue);
                Contentdialog.show();
                Contentdialog.setOnItemClick(new SpinnerDialog.OnItemClick() {
                    @Override
                    public void onClick(int position, BlueToothModel val) {
                        //myBlooth.setText(val.getVal());
                        utils.WriteString(MyKeys.KEY_BlueToothName, listBlue.get(position).getDeviceName());
                        utils.WriteString(MyKeys.KEY_BlueToothAddress, listBlue.get(position).getDeviceAddress());
                    }
                });
            }
        }
    }

    //cvr
    private void onReadCardCvr() {
        if (Utils.checkBluetooth(this, 2)) {
            final ProgressDialog progressDialog = ProgressDialog.show(this, "", "正在读取身份证信息...", true, false);
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Bundle bundle = msg.getData();
                    boolean find = false;
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (bundle != null) {
                        boolean result = bundle.getBoolean("result");
                        PersonModel person = (PersonModel) bundle.getSerializable("person_info");
                        if (result) {
                            find = true;
                            //setPerson(person);
                        } else {
                            if (null != person) {
                                find = true;
                                ToastShort(person.getPersonName());
                            }
                        }
                    }
                    if (!find) {
                        ToastShort("证件读取失败！（cvr）");
                    }
                }
            };
            new Cvr100bMYTask().startCvr100bTask(this, new Cvr100bTask.Cvr100bListener() {
                @Override
                public BlueToothModel reauestBlueDevice() {
                    BlueToothModel blue = new BlueToothModel();
                    blue.setDeviceAddress(utils.ReadString(MyKeys.KEY_BlueToothAddress));
                    return blue;
                }

                @Override
                public void onResult(boolean result, PersonModel person) {
                    Message msg = handler.obtainMessage(1);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("result", result);
                    bundle.putSerializable("person_info", person);
                    msg.setData(bundle);
                    msg.sendToTarget();
                }
            });
        } else {
            ToastShort("请检查蓝牙读卡设备设置！");
        }
    }
}
