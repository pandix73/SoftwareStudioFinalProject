package com.example.pandix.ssfinal;

/**
 * Created by pandix on 2016/6/20.
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class ChatActivity extends Activity {
    public static Handler mHandler = new Handler();
    TextView TextView01;    // 用來顯示文字訊息
    EditText EditText02;    // 文字方塊
    String tmp;                // 暫存文字訊息
    Socket clientSocket;    // 客戶端socket
    ArrayList<String> userName = new ArrayList<String>(10);
    Random ran = new Random();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userName.add(0, "小民");
        userName.add(1, "小敏");
        userName.add(2, "小琪");
        userName.add(3, "小安");
        userName.add(4, "小馬");
        userName.add(5, "小雞");
        userName.add(6, "小小");
        userName.add(7, "小鳥");
        userName.add(8, "小狗");
        userName.add(9, "小朱");

        // 從資源檔裡取得位址後強制轉型成文字方塊
        TextView01 = (TextView) findViewById(R.id.TextView01);
        TextView01.setMovementMethod(ScrollingMovementMethod.getInstance());
        EditText02=(EditText) findViewById(R.id.EditText02);

        // 以新的執行緒來讀取資料
        Thread t = new Thread(readData);

        // 啟動執行緒
        t.start();

        // 從資源檔裡取得位址後強制轉型成按鈕
        Button button1=(Button) findViewById(R.id.Button01);

        // 設定按鈕的事件
        button1.setOnClickListener(new Button.OnClickListener() {
            // 當按下按鈕的時候觸發以下的方法
            public void onClick(View v) {
                // 如果已連接則
                if(clientSocket.isConnected()){

                    BufferedWriter bw;

                    try {
                        // 取得網路輸出串流
                        bw = new BufferedWriter( new OutputStreamWriter(clientSocket.getOutputStream()));

                        // 寫入訊息
                        bw.write(userName.get(ran.nextInt(10)) + ": " +EditText02.getText()+"\n");

                        // 立即發送
                        bw.flush();
                    } catch (IOException e) {

                    }
                    // 將文字方塊清空
                    EditText02.setText("");
                }
            }
        });

    }

    // 顯示更新訊息
    private Runnable updateText = new Runnable() {
        public void run() {
            // 加入新訊息並換行
            TextView01.append(tmp + "\n");
        }
    };

    // 取得網路資料
    private Runnable readData = new Runnable() {
        public void run() {
            // server端的IP
            InetAddress serverIp;

            try {
                // 以內定(本機電腦端)IP為Server端
                serverIp = InetAddress.getByName("140.114.207.109");
                int serverPort = 5050;

                clientSocket = new Socket(serverIp, serverPort);

                // 取得網路輸入串流
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        clientSocket.getInputStream()));

                // 當連線後
                while (clientSocket.isConnected()) {
                    // 取得網路訊息
                    tmp = br.readLine();

                    // 如果不是空訊息則
                    if(tmp!=null)
                        // 顯示新的訊息
                        mHandler.post(updateText);
                }

            } catch (IOException e) {

            }
        }
    };

}