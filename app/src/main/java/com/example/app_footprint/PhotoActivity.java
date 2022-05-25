package com.example.app_footprint;

import static com.example.app_footprint.Json.myLatitude;
import static com.example.app_footprint.Json.myLongitude;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.app_footprint.module.Photo;
import com.example.app_footprint.module.PhotoActivityModel;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoActivity extends AppCompatActivity {
    private ImageView image;
    private RequestQueue requestQueue;
    private int PICK_IMAGE_REQUEST = 111;
    private Bitmap bitmap;
    private Bundle extras;
    private boolean flag = false;
    private int i = 0;
    private PhotoActivityModel photo;
    private Json baseConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        extras = getIntent().getExtras();
        image = (ImageView)findViewById(R.id.img_choose);
        requestQueue = Volley.newRequestQueue(this);
        baseConnection = new Json(requestQueue);

        photo = new PhotoActivityModel(extras.getString("userid"),(String)extras.get("groupId"));
        selectPosition();
    }

    private void selectPosition() {
        double latitude = extras.getDouble("latitude");
        double longitude = extras.getDouble("longitude");
        while(flag == false && i < myLatitude.size()) {
            if (Math.abs(latitude - myLatitude.get(i)) < 0.05 &&
                    Math.abs(longitude - myLongitude.get(i)) < 0.05) {
                flag = true;
                break;
            }
            i++;
        }
        if(flag)
        {
            photo.setLatitude(myLatitude.get(i));
            photo.setLongitude(myLongitude.get(i));
        }
        else
        {
            photo.setLatitude(latitude);
            photo.setLongitude(longitude);
        }
    }

    public void onBtnPickClicked(View caller)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Rescale the bitmap to 400px wide (avoid storing large images!)
                bitmap = getResizedBitmap( bitmap, 400 );
                //Setting image to ImageView
                image.setImageBitmap(bitmap);
                photo.setBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onBtnPostClicked(View caller)
    {
        photo.setBitmapBase();
        baseConnection.addPhotoInfo(photo,caller);
        finish();

    }



    public Bitmap getResizedBitmap(Bitmap bm, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scale = ((float) newWidth) / width;

        // We create a matrix to transform the image
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create the new bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public void onClick_close(View caller){
        finish();
        //requestQueue.add(Json.showPhoto());
    }
}