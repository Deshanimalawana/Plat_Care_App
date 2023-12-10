package com.example.plantcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.plantcareapp.databinding.ActivitySignupPageBinding;


public class SignupPage extends AppCompatActivity {

    ActivitySignupPageBinding binding;
    DBHelper dbHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DBHelper(this);

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.signupEmail.getText().toString();
                String password = binding.signupPassword.getText().toString();
                String confirmPassword = binding.signupConfirmPassword.getText().toString();

                if (email.equals("") || password.equals("") || confirmPassword.equals(""))
                    Toast.makeText(SignupPage.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                else{
                    if (password.equals(confirmPassword)){
                        Boolean checkUserEmail = dbHelper.checkEmail(email);

                        if(checkUserEmail == false){
                            Boolean insert = dbHelper.insertData(email, password);

                            if(insert == true){
                                Toast.makeText(SignupPage.this, "Signup Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),LoginPage.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(SignupPage.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignupPage.this, "User already exists, Please login", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignupPage.this, "Password should be matched", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(intent);
            }
        });
    }
}