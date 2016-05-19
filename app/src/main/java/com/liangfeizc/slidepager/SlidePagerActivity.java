package com.liangfeizc.slidepager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ikkong.imagepickerdemo.R;
import com.lzy.imagepicker.Utils;
import com.lzy.imagepicker.ui.ImageBaseActivity;

import java.util.ArrayList;

public class SlidePagerActivity extends ImageBaseActivity implements View.OnClickListener{

    public static final String EXTRA_TITLE = "slidepageractivity.extra.title";
    public static final String EXTRA_PICTURES = "slidepageractivity.extra.pictures";
    public static final String EXTRA_INDICATOR_TYPE = "slidepageractivity.extra.indicator.type";
    public static final String EXTRA_INDEX = "slidepageractivity.extra.index";
    
    public static final String ACTION_REMOVE_IMAGE = "SlidePagerActivity.intent.remove.image";
    public static final String EXTRA_REMOVE_IMAGE_INDEX = "SlidePagerActivity.extra.remove.image.index";

    private FrameLayout frameLay;
    private ImageView btn_back,btn_del;
    private TextView tv_des;
    private SlidePagerAdapter pagerAdapter;
    private ViewPager pager;
    private ArrayList<String> picPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_pager);
        pager = (ViewPager) findViewById(R.id.pager);
        getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs
        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager());

        if (getIntent() == null) return;

        //因为状态栏透明后，布局整体会上移，所以给头部加上状态栏的margin值，保证头部不会被覆盖
        frameLay = (FrameLayout) findViewById(R.id.frameLay);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) frameLay.getLayoutParams();
        params.topMargin = Utils.getStatusHeight(this);
        frameLay.setLayoutParams(params);

        // set pictures
        String[] pics = getIntent().getStringArrayExtra(EXTRA_PICTURES);
        picPaths = new ArrayList<>();
        for(int i = 0;i<pics.length;i++){
            picPaths.add(pics[i]);
        }
        pagerAdapter.addAll(picPaths);
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("onPageSelected","all:"+pagerAdapter.getCount());
                tv_des.setText((position+1)+"/" + pagerAdapter.getCount());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tv_des = (TextView) findViewById(R.id.tv_des);
        tv_des.setText("1/" + pagerAdapter.getCount());
        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_del = (ImageView) findViewById(R.id.btn_del);
        btn_back.setOnClickListener(this);
        btn_del.setOnClickListener(this);

        pager.setCurrentItem(getIntent().getIntExtra(EXTRA_INDEX,0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_del:
                //是否删除此张图片
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("要删除这张照片吗？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int index = pager.getCurrentItem();
                                pagerAdapter.remove(pager,index);
                                //发广播告诉 MainActivity 移除了哪张图片
                                Intent intent = new Intent(ACTION_REMOVE_IMAGE);
                                intent.putExtra(EXTRA_REMOVE_IMAGE_INDEX,index);
                                sendBroadcast(intent);
                                if(pagerAdapter.getCount() > 0){
                                    pager.setCurrentItem(index);
                                    tv_des.setText((pager.getCurrentItem()+1)+"/" + pagerAdapter.getCount());
                                }
                                if(pagerAdapter.getCount() == 0){
                                    onBackPressed();
                                }
                            }
                        }).show();
                break;
        }
    }

}
