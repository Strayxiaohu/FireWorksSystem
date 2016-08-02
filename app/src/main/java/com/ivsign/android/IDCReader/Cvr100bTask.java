package com.ivsign.android.IDCReader;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import com.xiaohu.fireworkssystem.model.BlueToothModel;
import com.xiaohu.fireworkssystem.model.PersonModel;
import com.xiaohu.fireworkssystem.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;



public class Cvr100bTask extends Thread {
    private Context mContext;
    BluetoothAdapter myBluetoothAdapter = null;
    BluetoothServerSocket mBThServer = null;
    BluetoothSocket mBTHSocket = null;
    InputStream mmInStream = null;
    OutputStream mmOutStream = null;
    int Readflage = -99;
    String ett = "";
    byte[] cmd_SAM = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x12, (byte) 0xFF, (byte) 0xEE};
    byte[] cmd_find = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x20, 0x01, 0x22};
    byte[] cmd_selt = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x20, 0x02, 0x21};
    byte[] cmd_read = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x30, 0x01, 0x32};
    byte[] cmd_sleep = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x02, 0x00, 0x02};
    byte[] cmd_weak = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x02, 0x01, 0x03};
    byte[] recData = new byte[1500];

    String DEVICE_NAME1 = "CVR-100B";
    String DEVICE_NAME2 = "com/ivsign/android/IDCReader";
    String DEVICE_NAME3 = "COM2";
    String DEVICE_NAME4 = "BOLUTEK";

    UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String[] decodeInfo = new String[10];

    private boolean read_ok;
    private Bitmap mBitmap;
    private BlueToothModel mBlueToothModel;

    public Cvr100bTask(Context context) {
        mContext = context;
    }

    private PersonModel mPerson;
    private Cvr100bListener cvr100bListener;

    public interface Cvr100bListener {
        BlueToothModel reauestBlueDevice();

        void onResult(boolean result, PersonModel person);
    }

    public void setCvr100bListener(Cvr100bListener listener, Context context) {
        cvr100bListener = listener;
        if (cvr100bListener != null)
            mBlueToothModel = cvr100bListener.reauestBlueDevice();
    }

    public void stopRun() {
        closeConnect();
    }

    private void init() {
        mBitmap = null;
        read_ok = false;
        mBitmap = null;
        decodeInfo = new String[10];
    }

    @Override
    public void run() {
        Looper.prepare();
        try {
            if (mBlueToothModel != null) {
                init();
                startConnect();
                startReadCard();
                closeConnect();
                returnData();
                return;
            }
        } catch (Exception e) {
            closeConnect();
        }
        mPerson = null;
        cvr100bListener.onResult(false, null);
        Looper.loop();
    }

    private void returnData() throws IOException {
        if (read_ok) {
            mPerson = new PersonModel();
            mPerson.setPersonName(decodeInfo[0].trim());
            System.out.println("person" + decodeInfo[0]);
            mPerson.setPersonSex(decodeInfo[1].trim());
            mPerson.setPersonNation(decodeInfo[2].trim());
            mPerson.setPersonAddress(decodeInfo[4].trim());
            mPerson.setPersonCardType("11");
            mPerson.setPersonCardId(decodeInfo[5].trim());
            if (mPerson.getPersonCardId().length() > 5)
                mPerson.setPersonNative(mPerson.getPersonCardId().substring(0, 6));
            mPerson.setPersonBirthday(decodeInfo[3].trim());
            if (!Utils.isEmptyString(mPerson.getPersonBirthday()) &&
                    mPerson.getPersonBirthday().length() == 8) {
                mPerson.setPersonBirthday(String.format("%s-%s-%s",
                        mPerson.getPersonBirthday().substring(0, 4),
                        mPerson.getPersonBirthday().substring(4, 6),
                        mPerson.getPersonBirthday().substring(6, 8)));
            }
            if (mBitmap != null) {
                mPerson.setPersonCardImage(Utils.encodeBitmap(mBitmap));
                mPerson.setPersonImgUrl(path);
                Log.i("path:", path);
            }
            cvr100bListener.onResult(true, mPerson);
        } else {
            mPerson = new PersonModel();
            mPerson.setPersonName(ett);
            cvr100bListener.onResult(false, mPerson);
        }
    }

    private void ReadCard() throws IOException {
        try {
            if ((mmInStream == null) || (mmInStream == null)) {
                Readflage = -2;//连接异常
                return;
            }
            mmOutStream.write(cmd_find);
            Thread.sleep(200);
            int datalen = mmInStream.read(recData);
            if (recData[9] == -97) {
                mmOutStream.write(cmd_selt);
                Thread.sleep(200);
                datalen = mmInStream.read(recData);
                if (recData[9] == -112) {
                    mmOutStream.write(cmd_read);
                    Thread.sleep(1000);
                    byte[] tempData = new byte[1500];
                    if (mmInStream.available() > 0) {
                        datalen = mmInStream.read(tempData);
                    } else {
                        Thread.sleep(500);
                        if (mmInStream.available() > 0) {
                            datalen = mmInStream.read(tempData);
                        }
                    }
                    int flag = 0;
                    if (datalen < 1294) {
                        for (int i = 0; i < datalen; i++, flag++) {
                            recData[flag] = tempData[i];
                        }
                        Thread.sleep(1000);
                        if (mmInStream.available() > 0) {
                            datalen = mmInStream.read(tempData);
                        } else {
                            Thread.sleep(500);
                            if (mmInStream.available() > 0) {
                                datalen = mmInStream.read(tempData);
                            }
                        }
                        for (int i = 0; i < datalen; i++, flag++) {
                            recData[flag] = tempData[i];
                        }

                    } else {
                        for (int i = 0; i < datalen; i++, flag++) {
                            recData[flag] = tempData[i];
                        }
                    }
                    tempData = null;
                    if (flag == 1295) {
                        if (recData[9] == -112) {

                            byte[] dataBuf = new byte[256];
                            for (int i = 0; i < 256; i++) {
                                dataBuf[i] = recData[14 + i];
                            }
                            String TmpStr = new String(dataBuf, "UTF16-LE");
                            TmpStr = new String(TmpStr.getBytes("UTF-8"));
                            decodeInfo[0] = TmpStr.substring(0, 15);
                            decodeInfo[1] = TmpStr.substring(15, 16);
                            decodeInfo[2] = TmpStr.substring(16, 18);
                            decodeInfo[3] = TmpStr.substring(18, 26);
                            decodeInfo[4] = TmpStr.substring(26, 61);
                            decodeInfo[5] = TmpStr.substring(61, 79);
                            decodeInfo[6] = TmpStr.substring(79, 94);
                            decodeInfo[7] = TmpStr.substring(94, 102);
                            decodeInfo[8] = TmpStr.substring(102, 110);
                            decodeInfo[9] = TmpStr.substring(110, 128);

                            //照片解码
                            try {
                                int ret = IDCReaderSDK.Init(mContext);
                                System.out.println("ret::" + ret);
                                if (ret == 0) {
                                    byte[] datawlt = new byte[1384];
                                    byte[] byLicData = {(byte) 0x05, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x5B, (byte) 0x03, (byte) 0x33, (byte) 0x01, (byte) 0x5A, (byte) 0xB3, (byte) 0x1E, (byte) 0x00};
                                    for (int i = 0; i < 1295; i++) {
                                        datawlt[i] = recData[i];
                                    }
                                    int t = IDCReaderSDK.unpack(datawlt, byLicData);
                                    System.out.println("t" + t);
                                    if (t == 1) {
                                        Readflage = 1;//读卡成功
                                    } else {
                                        Readflage = 6;//照片解码异常
                                    }
                                } else {
                                    Readflage = 6;//照片解码异常
                                }
                            } catch (Exception e) {
                                Readflage = 6;//照片解码异常
                            }

                        } else {
                            Readflage = -5;//读卡失败！
                        }
                    } else {
                        Readflage = -5;//读卡失败
                    }
                } else {
                    Readflage = -4;//选卡失败
                }
            } else {
                Readflage = -3;//寻卡失败
            }

        } catch (IOException e) {
            Readflage = -99;//读取数据异常
        } catch (InterruptedException e) {
            Readflage = -99;//读取数据异常
        }
    }

    public static void saveBitmap(Bitmap mBitmap) {
        FileOutputStream fOut = null;

        try {
            File f = new File("/sdcard/header.png");
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            return;
        }

        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String path = "";

    private void startReadCard() {
        int readcount = 10;
        try {
            while (readcount > 1) {
                ReadCard();
                readcount = readcount - 1;
                if (Readflage > 0) {
                    readcount = 0;
                    String text = "姓名：" + decodeInfo[0] + "\n" + "性别：" + decodeInfo[1] + "\n" + "民族：" + decodeInfo[2] + "\n"
                            + "出生日期：" + decodeInfo[3] + "\n" + "地址：" + decodeInfo[4] + "\n" + "身份号码：" + decodeInfo[5] + "\n"
                            + "签发机关：" + decodeInfo[6] + "\n" + "有效期限：" + decodeInfo[7] + "-" + decodeInfo[8] + "\n"
                            + decodeInfo[9] + "\n";

                    read_ok = true;
                    if (Readflage == 1) {
                        path = mContext.getFilesDir().getAbsolutePath() + "/wltlib/zp.bmp";
                        FileInputStream fis = new FileInputStream(path);
                        mBitmap = BitmapFactory.decodeStream(fis);
                        fis.close();
                    } else {
                        ett = "照片解码失败，请检查路径" + mContext.getFilesDir().getAbsolutePath() + "/wltlib/";

                    }
                } else {
                    if (Readflage == -2) {
                        //fw.write("蓝牙连接异常\n");
                        ett = "蓝牙连接异常";
                    }
                    if (Readflage == -3) {
                        //fw.write("无卡或卡片已读过\n");
                        ett = "无卡或卡片已读过";
                    }
                    if (Readflage == -4) {
                        //fw.write("无卡或卡片已读过\n");
                        ett = "无卡或卡片已读过";
                    }
                    if (Readflage == -5) {
                        //fw.write("读卡失败\n");
                        ett = "读卡失败";
                    }
                    if (Readflage == -99) {
                        //fw.write("操作异常\n");
                        ett = "操作异常";
                    }
                }
                Thread.sleep(100);
            }

        } catch (IOException e) {
            ett = "操作异常";
            //image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.face));
        } catch (InterruptedException e) {
            ett = "异常";
        }
    }

    private void startConnect() throws Exception {
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = myBluetoothAdapter.getRemoteDevice(mBlueToothModel.getDeviceAddress());

        try {
            myBluetoothAdapter.enable();
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);// 使得蓝牙处于可发现模式，持续时间150s
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 150);
            mBTHSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            int sdk = Integer.parseInt(Build.VERSION.SDK);
            if (sdk >= 10) {
                mBTHSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            } else {
                mBTHSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            }

            mBThServer = myBluetoothAdapter.listenUsingRfcommWithServiceRecord("myServerSocket", MY_UUID);

            mBTHSocket.connect();
            mmInStream = mBTHSocket.getInputStream();
            mmOutStream = mBTHSocket.getOutputStream();
        } catch (IOException e) {
            ett = "设备连接异常";
            throw new Exception("设备连接异常！");
        }

        if (mmInStream == null || mmInStream == null) {
            ett = "设备连接失败,请检查设备设置！";
            throw new Exception("");
        }
    }

    private void closeConnect() {
        try {
            if ((mmInStream == null) || (mmInStream == null)) {
                return;
            }
            mmOutStream.close();
            mmOutStream = null;
            mmInStream.close();
            mmInStream = null;
            mBTHSocket.close();
            mBTHSocket = null;
            mBThServer.close();
            mBThServer = null;

        } catch (IOException e) {
        } catch (Exception e) {
        }
    }

    private void startSleep() throws Exception {
        try {
            if ((mmInStream == null) || (mmInStream == null)) {
                throw new Exception("设备未正常连接！");
            }
            mmOutStream.write(cmd_sleep);
        } catch (IOException e) {
            throw new Exception("设备未正常连接！");
        }
    }

    private void startWeakup() throws Exception {
        try {
            if ((mmInStream == null) || (mmInStream == null)) {
                throw new Exception("设备未正常连接！");
            }
            mmOutStream.write(cmd_weak);
        } catch (IOException e) {
            throw new Exception("设备未正常连接！");
        }
    }
}
