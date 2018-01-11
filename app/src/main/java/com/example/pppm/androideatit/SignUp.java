package com.example.pppm.androideatit;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pppm.androideatit.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    EditText edtPhone, edtName, edtPassword;
    Button btnSignUp;

    FirebaseDatabase firebaseDatabase = null;
    DatabaseReference tblUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Init();

        Connect_Firebase();

        Event_Controller();
    }

    private void Event_Controller() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                tblUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        mDialog.dismiss();

                        String userPhone = edtPhone.getText().toString();
                        String userName = edtName.getText().toString();
                        String userPassword = edtPassword.getText().toString();

                        // Check if user phone exists in database
                        if (!dataSnapshot.child(userPhone).exists()) {
                            // Get user information
                            User user = new User(userName, userPassword);
                            tblUser.child(userPhone).setValue(user);
                            Toast.makeText(SignUp.this, "Sign up successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(SignUp.this, "Phone number is already registered!", Toast.LENGTH_SHORT).show();
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
        edtName = (MaterialEditText) findViewById(R.id.edtName);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
    }
}
