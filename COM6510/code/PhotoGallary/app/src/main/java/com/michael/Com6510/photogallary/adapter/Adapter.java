package com.michael.Com6510.photogallary.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.michael.Com6510.photogallary.database.entity.Image;
import com.michael.Com6510.photogallary.R;
import com.michael.Com6510.photogallary.activity.ShowImage;
import com.michael.Com6510.photogallary.Task.UploadSingleImageTask;

import java.io.Serializable;
import java.util.List;
//inspiration where gotten from the following sources   https://www.youtube.com/watch?v=USbTcGx1mD0&list=PLk7v1Z2rk4hjHrGKo9GqOtLs1e2bglHHA, https://www.youtube.com/watch?v=zHGgSd1wvxY
//https://www.youtube.com/watch?v=dVwR5Gpw1_E, https://www.youtube.com/results?search_query=telusko+android+beginners,https://www.youtube.com/watch?v=Zxf1mnP5zcw
/**
 * Created by Michael Oshinaike on 01/01/2018.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {


   private Context context;
   private static List<Image> images;


    public Adapter( List<Image> image) {
        super();
        this.images = image;
    }

    public Adapter(Context context) {

        this.context = context;

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_holder,parent,false);
        context =parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(holder != null && images.get(position) != null) {
            if (images.get(position).getImage() != null) {
               new  UploadSingleImageTask(images,position).execute(holder);

            }
        }

        //onclick listerner for respond to activity on the images such as touch
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ShowImage.class);
                intent.putExtra("position",position);
                intent.putExtra("imageList", (Serializable) images);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
        }
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        Adapter.images = images;
    }


}
