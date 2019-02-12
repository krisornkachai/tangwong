
package com.example.krisorn.tangwong;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class registerActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private EditText mPasswordField;
    private EditText mEmailField;
    private EditText nameField;
    private EditText sernameField;
    private EditText phoneNumberField;
    private EditText universityField;
    private EditText facultyField;
    private Button btn_add_img;
    private String url =null;
    public ImageView img_profile;
    //temp
/*
    private int tempRequestCode;
    private int tempResultCode;
    private Intent tempData;*/

    // [START declare_auth]
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    FirebaseStorage storage;
    StorageReference storageReference;
    private ProgressDialog mProgressDialog;


    // [END declare_auth]
    private static final int GALLERY_INTENT =2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Views
        mEmailField = findViewById(R.id.email);
        mPasswordField = findViewById(R.id.password);
        nameField=findViewById(R.id.name);
        phoneNumberField = findViewById(R.id.phone_number_register);
        btn_add_img = findViewById(R.id.btn_add_img_register);
        img_profile = findViewById(R.id.img_register_add_img);
        mProgressDialog= new ProgressDialog(this);



        // Buttons
        findViewById(R.id.btn_register).setOnClickListener(this);


        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_auth]
      /*  mStorage=FirebaseStorage.getInstance().getReference();
        mselectImage=(Button) findViewById(R.id.btn_addImage);
        mselectImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });*/

        btn_add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
       // FirebaseUser currentUser = mAuth.getCurrentUser();
       // updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        if (!validateForm()) {
            return;
        }

        //    showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                          //  updateUI(user);
                            mDatabase.child("user").child(user.getUid()).child("phoneNumber").setValue(phoneNumberField.getText().toString());
                            mDatabase.child("user").child(user.getUid()).child("name").setValue(nameField.getText().toString());
                            mDatabase.child("user").child(user.getUid()).child("notification").setValue("0");
                            mDatabase.child ("user").child (user.getUid()).child ("time").child ("status").setValue("0");
                            mDatabase.child("user").child(user.getUid()).child("pathPhoto").setValue(url);
                            mDatabase.child("user").child(user.getUid()).child("livenow").setValue("0");

                            /*

                            if(tempRequestCode==GALLERY_INTENT && tempResultCode==RESULT_OK){
                                Uri uri=tempData.getData();
                                StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());
                                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(registerActivity.this,"upload Done",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            */

                            toUserpage();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(registerActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //    hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }



private void toUserpage(){
    Intent i =new Intent(this,UsersActivity.class);
    startActivity(i);
}


    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    public void selectImge(){
        Intent intent =new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_register) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }else if (i==R.id.btn_addImage){
            //selectImge();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK){

            Uri uri=data.getData();
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            final StorageReference filepath = storageReference.child("Photos").child(uri.getLastPathSegment()+ UUID.randomUUID());

            mProgressDialog.setMessage("Uploading....");
            mProgressDialog.show();
            Log.d("11111111","11111111");

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("uploadscuccess", "onSuccess: uri= "+ uri.toString());
                            url = uri.toString();

                            Picasso.get().load(url).into(img_profile);
                            Toast.makeText(registerActivity.this,"upload Done",Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }
                    });
                }
            });

        }

    }

}
