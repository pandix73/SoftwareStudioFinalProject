package com.example.pandix.ssfinal;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mediatek.mcs.Mcs;
import com.mediatek.mcs.Utils.UIUtils;
import com.mediatek.mcs.domain.McsDataChannel;
import com.mediatek.mcs.domain.McsResponse;
import com.mediatek.mcs.entity.DataChannelEntity;
import com.mediatek.mcs.entity.DataPointEntity;
import com.mediatek.mcs.entity.api.DeviceInfoEntity;
import com.mediatek.mcs.entity.api.DeviceSummaryEntity;
import com.mediatek.mcs.net.McsJsonRequest;
import com.mediatek.mcs.net.RequestApi;
import com.mediatek.mcs.net.RequestManager;
import com.mediatek.mcs.socket.McsSocketListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SeatActivity extends Activity {


    private Handler handler_2 = null;//老闆
    private HandlerThread handlerThread_2 = null;//員工
    private String handlerThread_2_name = "我是1號員工";private Handler handler_1 = null;//老闆

    private Handler handler = new Handler();

    private ImageView bg;
    private ImageView seat;
    private Button detail;

    private double temperature = 25.5;
    private double humid = 30;
    private int seats = 5;

    private Map<String,String> data = new HashMap<String, String>();

    String mDeviceId = "DYfaDGZe";
    DeviceInfoEntity mDeviceInfo;
    McsDataChannel mDataChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mcs.initialize(this, "787198520648829", "oL0zzBZMoMBqasQa0V10UbraE7gexSrH");

        //新增一個員工 給他一個名字
        handlerThread_2 = new HandlerThread(handlerThread_2_name);
        //讓他開始上班
        handlerThread_2.start();
        //新增一個老闆 他是員工1號的老闆
        handler_1 = new Handler(handlerThread_2.getLooper());
        //老闆指派員工1號去做事(runnable_2)
        handler_1.post(runnable_2);

        setContentView(R.layout.activity_main);
        bg = (ImageView)findViewById(R.id.img1);
        seat = (ImageView)findViewById(R.id.img2);
        detail = (Button)findViewById(R.id.btn);
        detail.setOnClickListener(btnOnclick);
        detail.getBackground().setColorFilter(0xffff0000, PorterDuff.Mode.MULTIPLY);
    }

    private Button.OnClickListener btnOnclick = new Button.OnClickListener() {
        public void onClick(View v){
            //requestDevices();
            openDialog();
        }
    };

    private void openDialog() {
        String output = "";
        output += "溫度" + String.valueOf(temperature) + "°C\n";
        output += "濕度" + String.valueOf(humid) + "%\n";
        output += "剩餘座位" + Integer.toString(seats) + "\n";

        new AlertDialog.Builder(this)
                .setTitle("詳細資料")
                .setMessage(output)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                //按下按鈕後執行的動作，沒寫則退出Dialog
                            }
                        }
                )
                .show();
    }

    private void requestDevices(){

        int method = McsJsonRequest.Method.GET;
        String url = RequestApi.DEVICES;
        //Toast.makeText(getApplication(), url, Toast.LENGTH_LONG).show();
        McsResponse.SuccessListener<JSONObject> successListener =
                new McsResponse.SuccessListener<JSONObject>() {
                    @Override public void onSuccess(JSONObject response) {
                        List<DeviceSummaryEntity> summary = new Gson().fromJson(
                                response.toString(), DeviceSummaryEntity.class).getResults();

                        // ...
                        System.out.print(response.toString());
                        if(summary.size() > 0){
                            mDeviceId = summary.get(0).getDeviceId();
                            //Toast.makeText(getApplication(), mDeviceId, Toast.LENGTH_LONG).show();
                        }

                        requestDeviceInfo(mDeviceId);
                    }
                };
        McsResponse.ErrorListener errorListener = new McsResponse.ErrorListener() {
            @Override public void onError(Exception e) {
                // network request failed
                Toast.makeText(getApplication(), "RequestDevice FAIL", Toast.LENGTH_LONG).show();
            }
        };

        McsJsonRequest request = new McsJsonRequest(method, url, successListener, errorListener);
        RequestManager.sendInBackground(request);

    }

    private void requestDeviceInfo(String deviceId) {

        //Toast.makeText(getApplication(), "Trying to get "+RequestApi.DEVICE.replace("{deviceId}", deviceId)+" info.", Toast.LENGTH_LONG).show();

        int method = McsJsonRequest.Method.GET;
        String url = RequestApi.DEVICE.replace("{deviceId}", deviceId);

        McsResponse.ErrorListener errorListener = new McsResponse.ErrorListener() {
            @Override public void onError(Exception e) {
                Toast.makeText(getApplication(), "RequestDeviceInfo FAIL", Toast.LENGTH_LONG).show();
            }
        };
        McsResponse.SuccessListener<JSONObject> successListener =
                new McsResponse.SuccessListener<JSONObject>() {
                    @Override public void onSuccess(JSONObject response) {
                        mDeviceInfo = UIUtils.getFormattedGson()
                                .fromJson(response.toString(), DeviceInfoEntity.class)
                                .getResults().get(0);

                        //printJson(response);
                        System.out.print(response.toString());
                        showDataChannel(mDeviceInfo);
                    }
                };
        HashMap<String, String> headers = new HashMap();
        headers.put("deviceKey","tyGdJHGe5xz53qJh");
        McsJsonRequest request = new McsJsonRequest(method, url,headers,"", successListener, errorListener);
        System.out.println(request);

    }



    /**
     * GET data channel
     */
    private void showDataChannel(DeviceInfoEntity deviceInfo) {

        if (deviceInfo.getDataChannels().size() == 0) {
            Toast.makeText(getApplication(), "Empty Data Channel", Toast.LENGTH_LONG).show();
            return;
        }

        /**
         * Optional.
         * Default message of socket update shows in log.
         */
        McsSocketListener socketListener = new McsSocketListener(
                new McsSocketListener.OnUpdateListener() {
                    @Override public void onUpdate(JSONObject data) {
                        System.out.print(data.toString());
                    }
                }
        );

        DataChannelEntity dust_channelEntity = deviceInfo.getDataChannels().get(0);
        mDataChannel = new McsDataChannel(deviceInfo, dust_channelEntity, socketListener);
        DataPointEntity dust = mDataChannel.getDataPointEntity();

        DataChannelEntity temp_channelEntity = deviceInfo.getDataChannels().get(1);
        mDataChannel = new McsDataChannel(deviceInfo, temp_channelEntity, socketListener);
        DataPointEntity temp = mDataChannel.getDataPointEntity();

        DataChannelEntity hum_channelEntity = deviceInfo.getDataChannels().get(2);
        mDataChannel = new McsDataChannel(deviceInfo, hum_channelEntity, socketListener);
        DataPointEntity hum = mDataChannel.getDataPointEntity();

        if(dust != null){
            seats = Integer.valueOf(dust.getValues().getValue());
        }

        if( temp != null){
            temperature = Double.valueOf(temp.getValues().getValue());
        }

        if(hum != null){
            humid = Double.valueOf(hum.getValues().getValue());
        }

        //Toast.makeText(getApplication(),Integer.toString(deviceInfo.getDataChannels().size()), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplication(),"I love you", Toast.LENGTH_LONG).show();
        Toast.makeText(getApplication(),deviceInfo.getDataChannels().get(0).toString(), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplication(),deviceInfo.getDataChannels().get(1).toString(), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplication(),deviceInfo.getDataChannels().get(2).toString(), Toast.LENGTH_LONG).show();
        //System.out.println(deviceInfo.getDataChannels());
        //System.out.println(temp);
        //System.out.println(hum);
        //Toast.makeText(getApplication(),qq.toString(), Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplication(),temp.getValues().getValue(), Toast.LENGTH_LONG).show();

        // Toast.makeText(getApplication(),dust.getValues().getValue(), Toast.LENGTH_LONG).show();
        return;

    }


    private Runnable runnable_2 = new Runnable() {

        @Override
        public void run() {
            //要做的事情寫在這
            requestDevices();


            //老闆指定每隔幾秒要做一次工作1 (單位毫秒:1000等於1秒)
            handler_1.postDelayed(this, 5000);

        }
    };
}