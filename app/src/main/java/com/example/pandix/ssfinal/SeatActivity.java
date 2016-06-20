package com.example.pandix.ssfinal;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
public class SeatActivity extends Activity {

    private ImageView bg;
    private ImageView seat;
    private Button detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bg = (ImageView)findViewById(R.id.img1);
        seat = (ImageView)findViewById(R.id.img2);
        detail = (Button)findViewById(R.id.btn);
        detail.setOnClickListener(btnOnclick);
        detail.getBackground().setColorFilter(0xffff0000, PorterDuff.Mode.MULTIPLY);
    }

    private Button.OnClickListener btnOnclick = new Button.OnClickListener() {
        public void onClick(View v){
            openDialog();
        }
    };

    private void openDialog() {
        String output = "";
        output += "溫度" + 23 + '\n';
        output += "濕度" + 15 + '\n';
        output += "剩餘座位" + 5 + '\n';

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
}