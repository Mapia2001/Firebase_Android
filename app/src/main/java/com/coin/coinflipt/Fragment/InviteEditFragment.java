package com.coin.coinflipt.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.coin.coinflipt.Activity.HomeActivity;
import com.coin.coinflipt.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class InviteEditFragment extends Fragment {

    CircleImageView profileimage;
    TextView invite_name_friend, invite_email_friend, invite_balance;
    EditText invite_coin;
    Button invite_send;
    View view;
    String invite_email, invite_user_name;


    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser current_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_inviteedit, container, false);
        profileimage = view.findViewById(R.id.invite_photo_friend);
        invite_name_friend = view.findViewById(R.id.invite_name_friend);
        invite_email_friend = view. findViewById(R.id.invite_email_friend);
        invite_balance = view.findViewById(R.id.account_balance);
        invite_send = view.findViewById(R.id.invite_send);
        invite_coin = view.findViewById(R.id.invite_coin_amount);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        current_user = mAuth.getCurrentUser();

        //retreving data using bundle
        Bundle bundle=getArguments();
        invite_name_friend.setText(String.valueOf(bundle.getString("name")));
        invite_email_friend.setText(String.valueOf(bundle.getString("email")));
        invite_email = String.valueOf(bundle.getString("email"));

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageReference = storage.getReference().child("images").child(String.valueOf(bundle.getString("email")));

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(HomeActivity.getInstance()).load(uri).into(profileimage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                profileimage.setImageResource(R.drawable.unknow_profile);
            }
        });

        invite_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_invite();
            }
        });


        return view;
    }

    private void send_invite(){

        if (invite_coin.getText().toString().equals("")){
            Toast.makeText(HomeActivity.getInstance(), "Enter coin amount", Toast.LENGTH_LONG).show();
            return;
        }else if ( Integer.parseInt(invite_coin.getText().toString()) >= Integer.parseInt(invite_balance.getText().toString()) ){
            Toast.makeText(HomeActivity.getInstance(), "coin must be less than balance.", Toast.LENGTH_LONG).show();
            return;
        }else {

            Map<String, Object> accepted = new HashMap<>();
            accepted.put("accept_status", "0");
            accepted.put("coin", invite_coin.getText().toString());
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            accepted.put("send_date", currentDate + " " + currentTime);
            db.collection("userList").document(invite_email).
                    collection("invite").document(current_user.getEmail())
                    .set(accepted).addOnSuccessListener(new  OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(HomeActivity.getInstance(), "Invited succefully.", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(HomeActivity.getInstance(), "Invited Failed.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}
