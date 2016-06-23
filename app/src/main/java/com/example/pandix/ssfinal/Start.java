package com.example.pandix.ssfinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by mirat on 2016/6/23.
 */
public class Start extends Activity {

    ImageButton imageButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingpage);

        addListenerOnButton();

    }

    public void addListenerOnButton() {

        imageButton = (ImageButton) findViewById(R.id.imageButton1);

        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(Start.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
