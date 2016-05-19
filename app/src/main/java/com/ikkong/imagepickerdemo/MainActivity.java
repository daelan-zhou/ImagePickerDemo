package com.ikkong.imagepickerdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.liangfeizc.slidepager.SlidePagerActivity;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.GlideImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        ImagePickerAdapter.OnRecyclerViewItemClickListener{

    private RecyclerView recylerView;
    private ImagePickerAdapter adapter;
    private List<ImageItem> selImageList;
    private int maxImgCount = 8;//允许选择图片最大数

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recylerView = (RecyclerView) findViewById(R.id.recylerView);
        initImagePicker();//最好放到 Application oncreate执行
        initWidget();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SlidePagerActivity.ACTION_REMOVE_IMAGE);
        registerReceiver(removeImageReceiver,filter);
    }
    
    BroadcastReceiver removeImageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(SlidePagerActivity.ACTION_REMOVE_IMAGE)){
                //接收到移除广播 
                selImageList.remove(intent.getIntExtra(SlidePagerActivity.EXTRA_REMOVE_IMAGE_INDEX,-1));
                adapter.refresh();
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(removeImageReceiver);
        super.onDestroy();
    }

    private void initWidget() {
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this,selImageList,maxImgCount);
        adapter.refresh();
        GridLayoutManager manager = new GridLayoutManager(this,4);
        recylerView.setLayoutManager(manager);
        recylerView.setHasFixedSize(true);
        recylerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                selImageList.addAll(selImageList.size()-1,images);
                adapter.refresh();
            }
        }
    }

    @Override
    public void onItemClick(View view, String data) {
        switch(data){
            case Constants.IMAGEITEM_DEFAULT_ADD:
                //本次允许选择的数量
                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size() + 1);
                //打开选择器
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                break;
            default:
                String[] sArray = getPaths();
                //打开预览
                Intent intent1 = new Intent(this, SlidePagerActivity.class);
                intent1.putExtra(SlidePagerActivity.EXTRA_TITLE, "");
                intent1.putExtra(SlidePagerActivity.EXTRA_PICTURES, sArray);
                intent1.putExtra(SlidePagerActivity.EXTRA_INDEX,Integer.parseInt(data));
                startActivity(intent1);
                break;
        }
    }
    
    private String[] getPaths(){
        //判断最后一个图片是否是+号
        boolean hasSpace = selImageList.get(selImageList.size()-1).path.equals(Constants.IMAGEITEM_DEFAULT_ADD);
        String str[] = new String[hasSpace?selImageList.size()-1:selImageList.size()];
        //最后一个不显示
        for (int i = 0;i< str.length;i++) {
            str[i] = selImageList.get(i).path;
        }
        return str;
    }
}
