package com.coin.coinflipt.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coin.coinflipt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    ImageView btn_to_login;
    Button btn_signup;
    EditText user_up_name, user_up_email, user_up_pass, user_up_confirm;
    String username, useremail, userpass, userconfirmpass, docname = "";
    boolean  isDoc = false;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        btn_to_login =findViewById(R.id.btn_to_login);
        btn_signup = findViewById(R.id.btn_signup);
        user_up_name = findViewById(R.id.user_up_name);
        user_up_email = findViewById(R.id.user_up_email);
        user_up_pass = findViewById(R.id.user_up_pass);
        user_up_confirm = findViewById(R.id.user_up_confirmpass);

        filePath = Uri.parse("android.resource://com.coin.coinflipt/" + R.drawable.unknow_profile);

        db = FirebaseFirestore.getInstance();

        btn_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    private void signup(){

        username = user_up_name.getText().toString();
        useremail = user_up_email.getText().toString();
        userpass = user_up_pass.getText().toString();
        userconfirmpass = user_up_confirm.getText().toString();

        if (TextUtils.isEmpty(username)){
            user_up_name.setError("Please enter user name");
            user_up_name.requestFocus();
            return;
        }

        if ( !Patterns.EMAIL_ADDRESS.matcher(useremail).matches() ){
            user_up_email.setError("Invalid mail");
            user_up_email.requestFocus();
            return;
        }

        if ( !userpass.equals(userconfirmpass) ){
            user_up_confirm.setError("Invalid confirm password");
            user_up_confirm.requestFocus();
             return;
        }

        if ( userpass.length() < 6 ){
            user_up_pass.setError("password length than 6");
            user_up_pass.requestFocus();
            return;
        }

        checkDbDoc();
    }

    private  void checkDbDoc(){

        db.collection("userList").get().addOnCompleteListener (new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                // Task complete
                if (task.isSuccessful()) {
                    //Task successful
                    //Getting each document one by one in that collection
                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        //Performing the delete operation
                        if (documentSnapshot.getReference().getId().equals(useremail)){
                            isDoc = true;
                        }
                    }

                    if ( isDoc ){
                        Toast.makeText(RegisterActivity.this, "already email registered", Toast.LENGTH_LONG).show();
                    }else{

                        register();
                    }

                    isDoc = false;
                }
            }
        });
    }

    private void register(){
        FirebaseApp.initializeApp(RegisterActivity.this);
        mAuth.createUserWithEmailAndPassword(useremail, userpass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in: success
                            // update UI for current User
                            FirebaseUser user = mAuth.getCurrentUser();
                            upload_user_details();
                        } else {
                            // Sign in: fail
                            Toast.makeText(RegisterActivity.this, "failed auth", Toast.LENGTH_LONG).show();
                            Log.e("TAG", "create Account: Fail!", task.getException());
                        }

                        // ...
                    }
                });

    }

    private void upload_user_details(){
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", username);
        userInfo.put("email", useremail);
        userInfo.put("phonenumber", "");
        db.collection("userList").document(useremail)
                .set(userInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        upload_photo();

                        Log.d("TAG", "DocumentSnapshot successfully written!");
                        Toast.makeText(RegisterActivity.this,"register success",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this,"false",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void upload_photo(){
            if (filePath != null) {
//
//            // Code for showing progressDialog while uploading
                // Code for showing progressDialog while uploading
                final ProgressDialog progressDialog
                        = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                // Defining the child of storageReference

                storageReference = FirebaseStorage.getInstance().getReference();

                StorageReference ref
                        = storageReference
                        .child(
                                "images/"
                                        + useremail);
                // adding listeners on upload
                // or failure of image
                ref.putFile(filePath)
                        .addOnSuccessListener(
                                new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onSuccess(
                                            UploadTask.TaskSnapshot taskSnapshot)
                                    {

                                        // Image uploaded successfully
                                        // Dismiss dialog
                                        progressDialog.dismiss();
                                        Toast
                                                .makeText(RegisterActivity.this,
                                                        "Image Uploaded!!",
                                                        Toast.LENGTH_SHORT)
                                                .show();
                                        saveUri();
                                    }
                                })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {

                                // Error, Image not uploaded
                                progressDialog.dismiss();
                                Toast
                                        .makeText(RegisterActivity.this,
                                                "Failed " + e.getMessage(),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .addOnProgressListener(
                                new OnProgressListener<UploadTask.TaskSnapshot>() {

                                    // Progress Listener for loading
                                    // percentage on the dialog box
                                    @Override
                                    public void onProgress(
                                            UploadTask.TaskSnapshot taskSnapshot)
                                    {
                                        double progress
                                                = (100.0
                                                * taskSnapshot.getBytesTransferred()
                                                / taskSnapshot.getTotalByteCount());
                                        progressDialog.setMessage(
                                                "Uploaded "
                                                        + (int)progress + "%");
                                    }
                                });



            }else{

            }



    }

    private void saveUri(){
        FirebaseStorage storage = FirebaseStorage.getInstance();;

        StorageReference storageReference = storage.getReference().child("images").child(useremail);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("name", username);
                userInfo.put("email", useremail);
                userInfo.put("photo", uri.toString());
                userInfo.put("phonenumber", "");
                db.collection("userList").document(useremail).set(userInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "register failed", Toast.LENGTH_LONG).show();

            }
        });
    }

}
