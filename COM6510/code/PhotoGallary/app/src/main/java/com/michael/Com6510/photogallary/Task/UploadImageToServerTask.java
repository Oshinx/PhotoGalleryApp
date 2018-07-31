package com.michael.Com6510.photogallary.Task;
//https://www.youtube.com/watch?v=V4q0sTIntsk,https://www.youtube.com/watch?v=zHGgSd1wvxY&list=RDzHGgSd1wvxY,https://www.youtube.com/watch?v=DF5GNszDOtY
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.michael.Com6510.photogallary.net.MultipartRequest;

import java.io.File;

/**
 * Created by Michael Oshinaike on 12/01/2018.
 */

public class UploadImageToServerTask extends AsyncTask<String, Void,String> {
   public String json;
   public File file;
   public Context context;
   public MultipartRequest multipartRequest;
    public UploadImageToServerTask(String json, File file, Context context) {
        this.json = json;
        this.file = file;
        this.context = context;
    }


    @Override
    protected String doInBackground(String... strings) {
        String fileName = file.getName();
        String output;
        multipartRequest = new MultipartRequest(context);
        multipartRequest.addString("Image Details",json);
       // multipartRequest.addFile("Image",file.getPath(),fileName);
        multipartRequest.execute("http://wesenseit-vm1.shef.ac.uk:8091/uploadImages");
        output=multipartRequest.execute("http://wesenseit-vm1.shef.ac.uk:8091/uploadImages");

        return output;
    }

    @Override
    protected void onPostExecute(String  output) {
        Log.e("server output", "onPostExecute: " + output);
        Toast.makeText(context,""+"image sent to the server",Toast.LENGTH_LONG).show();
    }
}
