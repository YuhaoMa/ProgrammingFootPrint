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

import java.io.ByteArrayOutputStream;

public class PhotoActivity extends AppCompatActivity {
    private ImageView image;
    private RequestQueue requestQueue;
    private int PICK_IMAGE_REQUEST = 111;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;
    private Bundle extras;
    private boolean flag = false;
    private int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        extras = getIntent().getExtras();
        image = (ImageView)findViewById(R.id.img_choose);
        requestQueue = Volley.newRequestQueue(this);
        selectPosition();
    }

    private void selectPosition() {
        double latitude = (double)extras.get("latitude");
        double longitude = (double)extras.get("longitude");
        System.out.println(myLatitude.toString());
        while(flag == false && i < myLatitude.size()) {
            if (Math.abs(latitude - myLatitude.get(i)) < 0.05 &&
                    Math.abs(longitude - myLongitude.get(i)) < 0.05) {
                flag = true;
                break;
            }
            i++;
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onBtnPostClicked(View caller)
    {
        //Start an animating progress widget
            progressDialog = new ProgressDialog(PhotoActivity.this);
            progressDialog.setMessage("Uploading, please wait...");
            progressDialog.show();

            //convert image to base64 string
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            if(flag){
                requestQueue.add(Json.addPhotoInfo((String) extras.get("date"),(String) extras.get("userid")
                        ,(String) extras.get("groupId"),
                        myLatitude.get(i),myLongitude.get(i), progressDialog,this
                        ,imageString));
            }
            else {
                requestQueue.add(Json.addPhotoInfo((String) extras.get("date"), (String) extras.get("userid")
                        , (String) extras.get("groupId"), (double) extras.get("latitude")
                        , (double) extras.get("longitude"), progressDialog, this, imageString));
            }
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