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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AddDetails extends AppCompatActivity {

    EditText plant_name;
    EditText plant_desc;
    Button addButton;
    ImageView plant_image;
    byte[] imageBytes;

    private static final int GALLERY_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);

        plant_name = (EditText) findViewById(R.id.plant);
        plant_desc = (EditText) findViewById(R.id.desc);
        addButton = (Button) findViewById(R.id.add);
        plant_image = (ImageView) findViewById(R.id.imageView2);

        plant_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = plant_name.getText().toString();
                String desc = plant_desc.getText().toString();

                if (name.isEmpty() || desc.isEmpty()) {
                    Toast.makeText(AddDetails.this, "Please enter both name and description", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (imageBytes == null) {
                    Toast.makeText(AddDetails.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bitmap bitmap = ((BitmapDrawable) plant_image.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] imageBytes = stream.toByteArray();

                DBHelper dbHelper = new DBHelper(AddDetails.this);
                dbHelper.addDetails(name, desc, imageBytes);

                Intent intent = new Intent(AddDetails.this,MainActivity.class);
                startActivity(intent);
            }
        });
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
                plant_image.setVisibility(View.VISIBLE);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                plant_image.setImageBitmap(bitmap);
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