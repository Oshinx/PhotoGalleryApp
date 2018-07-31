package com.michael.Com6510.photogallary.adapter;
//inspiration where gotten from the following sources   https://www.youtube.com/watch?v=USbTcGx1mD0&list=PLk7v1Z2rk4hjHrGKo9GqOtLs1e2bglHHA, https://www.youtube.com/watch?v=zHGgSd1wvxY
//https://www.youtube.com/watch?v=dVwR5Gpw1_E, https://www.youtube.com/results?search_query=telusko+android+beginners,https://www.youtube.com/watch?v=Zxf1mnP5zcw
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.michael.Com6510.photogallary.database.entity.Image;
import com.michael.Com6510.photogallary.R;
import com.michael.Com6510.photogallary.utils.Utils;

import java.util.List;

/**
 * Created by Michael Oshinaike on 02/01/2018.
 */

public class SwipeAdapter extends PagerAdapter {

    private List<Image> images;
    private Context context;
    private LayoutInflater layoutInflater;
    private int position;


    public SwipeAdapter(Context context,List<Image> images,int position) {
        this.images = images;
        this.context = context;
    }





    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
       if(view == (LinearLayoutCompat)object){
        return true ;
       }
        else {
           return false;
       }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View image = layoutInflater.inflate(R.layout.image_swipe_layout,container,false);
        ImageView imageView =image.findViewById(R.id.slidable_image_view);
        if(container != null && images.get(position) !=null) {
           if(images.get(position).getImage() != null){
               String path = images.get(position).getImage();
               Bitmap bitmap = Utils.decodeSampledBitmapFromResources(path,500,500);
               imageView.setImageBitmap(bitmap);
                container.addView(image);
            }

        }
        return image;

    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}
