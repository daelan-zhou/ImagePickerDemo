package com.liangfeizc.slidepager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ikkong.imagepickerdemo.R;
import com.lzy.imagepicker.ImagePicker;

import uk.co.senab.photoview.PhotoView;

public class SlidePageFragment extends Fragment {
    private static final String PIC_PATH = "slidepagefragment.picpath";

    public static SlidePageFragment newInstance(@NonNull final String picPath) {
        Bundle arguments = new Bundle();
        arguments.putString(PIC_PATH, picPath);

        SlidePageFragment fragment = new SlidePageFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_image, container, false);
        PhotoView mImg = (PhotoView) rootView.findViewById(R.id.images);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String path = arguments.getString(PIC_PATH);
            //显示图片
            ImagePicker.getInstance().getImageLoader().displayImage(getActivity(),
                    path, mImg, 0, 0);
        }


        return rootView;
    }
}
