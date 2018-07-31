package com.michael.Com6510.photogallary.Task;
//https://www.youtube.com/watch?v=V4q0sTIntsk,https://www.youtube.com/watch?v=zHGgSd1wvxY&list=RDzHGgSd1wvxY,https://www.youtube.com/watch?v=DF5GNszDOtY
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.michael.Com6510.photogallary.adapter.Adapter;
import com.michael.Com6510.photogallary.database.entity.Image;
import com.michael.Com6510.photogallary.utils.Utils;

import java.util.List;

/**
 * Created by Michael Oshinaike on 10/01/2018.
 */

public class UploadSingleImageTask extends AsyncTask<Adapter.ViewHolder,Void,Bitmap> {


    private List<Image> imageList;
    private Adapter.ViewHolder viewHolder;
    private int position;

    public UploadSingleImageTask(List<Image> imageList, int position) {
        this.imageList = imageList;
        this.position = position;
    }



    @Override
    protected Bitmap doInBackground(Adapter.ViewHolder... viewHolders) {
        this.viewHolder = viewHolders [0];
        String path = imageList.get(position).getImage();
        Bitmap bitmap = Utils.decodeSampledBitmapFromResources(path,150,150);

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        viewHolder.imageView.setImageBitmap(bitmap);
    }


}
