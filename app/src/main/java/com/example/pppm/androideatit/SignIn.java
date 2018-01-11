package com.example.pppm.androideatit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.pppm.androideatit.Common.Common;
import com.example.pppm.androideatit.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    Button btnSignIn;

    FirebaseDatabase firebaseDatabase = null;
    DatabaseReference tblUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Init();

        Connect_Firebase();

        Event_Controller();
    }

    private void Event_Controller() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                tblUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        mDialog.dismiss();

                        // Check if user phone exists in database 
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            // Get user information
                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);

                            if (user.getPassword().equals(edtPassword.getText().toString())) {
//                                Toast.makeText(SignIn.this, "Sign in successfully!", Toast.LENGTH_SHORT).show();
                                Intent homeIntent = new Intent(SignIn.this, Home.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();
                            } else {
                                Toast.makeText(SignIn.this, "Sign in failed!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignIn.this, "User does not exist", Toast.LENGTH_SHORT).show();
                        }
                        
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void Connect_Firebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        tblUser = firebaseDatabase.getReference("User");
    }

    private void Init() {
        edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
    }
}
