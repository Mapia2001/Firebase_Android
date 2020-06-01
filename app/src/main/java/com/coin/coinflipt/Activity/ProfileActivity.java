package com.coin.coinflipt.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coin.coinflipt.Component.Constants;
import com.coin.coinflipt.Component.Upload;
import com.coin.coinflipt.Component.User;
import com.coin.coinflipt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.opencensus.common.ToLongFunction;

public class ProfileActivity extends AppCompatActivity {

    TextView edt_username, edt_email, uri_path;
    EditText edt_phone;
    Button btn_profile_save;
    de.hdodenhof.circleimageview.CircleImageView user_photo;
    private String user_name;
    private final int SELECT_FILE = 22;
    private Uri filePath = null;
    boolean isSelectImage = false;


    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        edt_username = (TextView)findViewById(R.id.edt_username);
        edt_email = (TextView)findViewById(R.id.edt_email);
        edt_phone = (EditText)findViewById(R.id.edt_phone);
        user_photo = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.user_photo);
        btn_profile_save = (Button)findViewById(R.id.btn_profile_save);
        uri_path = (TextView)findViewById(R.id.uri_path);
        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();



        // get the Firebase  storage reference

        loadData();

        user_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectImage = true;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
            }
        });

        btn_profile_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_profile();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode,
                resultCode,
                data);
        if (requestCode == SELECT_FILE
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                user_photo.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void upload_profile(){

        if ( isSelectImage ){
            if (filePath != null) {


//
//            // Code for showing progressDialog while uploading
//            // Code for showing progressDialog while uploading
                final ProgressDialog progressDialog
                        = new ProgressDialog(this);
                progressDialog.setTitle("Saving...");
                progressDialog.show();
//
                FirebaseStorage storage = FirebaseStorage.getInstance();;
                StorageReference storageReference = storage.getReference();

//            // Defining the child of storageReference
                StorageReference ref
                        = storageReference
                        .child(
                                "images/"
                                        + current_user.getEmail());
//
//            // adding listeners on upload
//            // or failure of image
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

                                        getUriStorage();

                                    }
                                })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {

                                // Error, Image not uploaded
                                progressDialog.dismiss();
                                Toast
                                        .makeText(ProfileActivity.this,
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

        update_profile();

    }

    private void loadData(){


        db.collection("userList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //Task successful
                    //Getting each document one by one in that collection
                    for (QueryDocumentSnapshot document: task.getResult()){
                        //Performing the delete operation
                        if (document.getReference().getId().equals(current_user.getEmail())){
                            Map<String, Object> map = document.getData();
                            String name = (String)map.get("name");
                            filePath = Uri.parse((String)map.get("photo"));
                            String phone_number = (String)map.get("phonenumber");
                            uri_path.setText((String)map.get("photo"));
                            edt_username.setText(name);
                            Glide.with(ProfileActivity.this).load(filePath).into(user_photo);
                            edt_phone.setText(phone_number);
                            edt_email.setText(current_user.getEmail());
                        }
                    }
                }
            }
        });

        // Create a reference to a file from a Google Cloud Storage URI



    }

    private void update_profile(){

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("photo", uri_path.getText().toString());
        userInfo.put("phonenumber", edt_phone.getText().toString());
        userInfo.put("email", edt_email.getText().toString());
        userInfo.put("name", edt_username.getText().toString());

        final ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setTitle("Saving...");

        if ( !isSelectImage ){
            progressDialog.show();
        }

        db.collection("userList").document(current_user.getEmail())
                .set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if ( !isSelectImage ){
                    progressDialog.dismiss();
                }
                Toast.makeText(ProfileActivity.this, "Saved!!", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "no member", Toast.LENGTH_LONG).show();
            }
        });

        isSelectImage = false;
    }

    private void getUriStorage(){
        FirebaseStorage storage = FirebaseStorage.getInstance();;

        StorageReference storageReference = storage.getReference().child("images").child(current_user.getEmail());

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                uri_path.setText(uri.toString());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "register failed", Toast.LENGTH_LONG).show();

            }
        });
    }

}
