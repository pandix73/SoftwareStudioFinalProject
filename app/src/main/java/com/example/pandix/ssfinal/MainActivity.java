package com.example.pandix.ssfinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends Activity {

    private ViewPager mViewPager;
    private MyViewPagerAdapter mAdapter;
    private ArrayList<View> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        final LayoutInflater mInflater = getLayoutInflater().from(this);

        View v1 = mInflater.inflate(R.layout.activity_start1, null);
        View v2 = mInflater.inflate(R.layout.activity_start2, null);
        View v3 = mInflater.inflate(R.layout.activity_start3, null);

        viewList = new ArrayList<View>();
        viewList.add(v1);
        viewList.add(v2);
        viewList.add(v3);

        mViewPager.setAdapter(new MyViewPagerAdapter(viewList));
        mViewPager.setCurrentItem(0);
        Button button = (Button)findViewById(R.id.btn);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                if(mViewPager.getCurrentI;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScroltem() == 0)
                    intent.setClass(MainActivity.this, BookActivity.class);
            else if(mViewPager.getCurrentItem() == 1)
                    intent.setClass(MainActivity.this, ChatActivity.class);
            else if(mViewPager.getCurrentItem() == 2)
                    intent.setClass(MainActivity.this, SeatActivity.class);
            startActivity(intent)led(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Button button = (Button)findViewById(R.id.btn);
                if(position == 0){
                    button.setText("書籍查詢");
                } else if (position == 1) {
                    button.setText("線上回報");
                } else if (position == 2) {
                    button.setText("館內狀況");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }



}