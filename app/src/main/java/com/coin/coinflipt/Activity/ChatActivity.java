package com.coin.coinflipt.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coin.coinflipt.Adapter.ChatAdapter;
import com.coin.coinflipt.Component.Chat;
import com.coin.coinflipt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String partner_name;
    CircleImageView partner_photo;
    TextView partner_username;
    Intent intent;
    Uri photo_uri;
    String partner_email, receive_mes_string, send_mes_string;
    ImageButton btn_msg_send, btn_file_send;
    EditText msg_text;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser current_user;
    private DatabaseReference databaseReference;
    ArrayList<Chat> chats = new ArrayList<>();
    private RecyclerView chat_list;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        partner_photo = findViewById(R.id.partner_photo);
        partner_username = findViewById(R.id.partner_name);
        btn_msg_send = findViewById(R.id.btn_msg_send);
        btn_file_send = findViewById(R.id.btn_file_send);
        msg_text = findViewById(R.id.msg_text);

        chat_list = findViewById(R.id.chat_list);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        current_user = mAuth.getCurrentUser();



        LoadAvatar();

        btn_msg_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_mes_string = msg_text.getText().toString();
                sendMessage();
            }
        });

    }

    private void LoadAvatar(){
        intent = getIntent();
        partner_name = intent.getStringExtra("partner_name");
        photo_uri = Uri.parse(intent.getStringExtra("partner_uri")) ;
        partner_email = intent.getStringExtra("partner_email");
        if(photo_uri == null){
            partner_photo.setImageResource(R.drawable.unknow_profile);
        }
        else
        {
            Glide.with(HomeActivity.getInstance()).load(photo_uri).into(partner_photo);
        }
        partner_username.setText(partner_name);


    }

    private void sendMessage(){

    }

}
