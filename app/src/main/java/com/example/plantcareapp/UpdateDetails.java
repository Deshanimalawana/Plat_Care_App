package com.example.plantcareapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UpdateDetails extends AppCompatActivity {

    EditText update_name,update_desc;
    ImageView update_images;
    Button update;
    String id,name,desc,image;
    byte[] imageBytes;

    private static final int GALLERY_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);

        update_name = (EditText) findViewById(R.id.update_plant);
        update_desc = (EditText) findViewById(R.id.update_desc);
        update_images = (ImageView) findViewById(R.id.imageView2);
        update = (Button) findViewById(R.id.update);

        update_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });

        getIntentData();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = ((BitmapDrawable) update_images.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] imageBytes = stream.toByteArray();

                DBHelper db = new DBHelper(UpdateDetails.this);
                db.updateData(id,
                        update_name.getText().toString(),
                        update_desc.getText().toString(),
                        imageBytes);

                Intent intent = new Intent(UpdateDetails.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getIntentData(){
        if(getIntent().hasExtra("id")){
            id=getIntent().getStringExtra("id");
            name=getIntent().getStringExtra("name");
            desc=getIntent().getStringExtra("desc");
            image=getIntent().getStringExtra("image");

            if (update_images.getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) update_images.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageBytes = stream.toByteArray();
            }

            update_name.setText(name);
            update_desc.setText(desc);

            if (imageBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                update_images.setImageBitmap(bitmap);
            }
        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                imageBytes = getBytes(inputStream);

                // Show the selected image
                update_images.setVisibility(View.VISIBLE);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                update_images.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }

}