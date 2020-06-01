package com.coin.coinflipt.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.coin.coinflipt.Activity.ChatActivity;
import com.coin.coinflipt.Component.Chat;
import com.coin.coinflipt.Component.Friend;
import com.coin.coinflipt.R;
import com.coin.coinflipt.Activity.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.PlanetHolder> implements Filterable {

    private Context context;
    private ArrayList<Chat> clients;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser current_user;



    public ChatAdapter(Context context, ArrayList<Chat> guiders) {
        this.context = context;
        this.clients = guiders;
    }


    @NonNull
    @Override
    public PlanetHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false);
        return new PlanetHolder(view);
    }


    @Override

    public void onBindViewHolder(@NonNull final PlanetHolder holder, final int position) {
        final Chat chat = clients.get(position);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        current_user = mAuth.getCurrentUser();

        holder.sender_message_text.setVisibility(View.INVISIBLE);
        holder.sender_message_image.setVisibility(View.INVISIBLE);
        holder.receive_message_text.setVisibility(View.INVISIBLE);
        holder.receive_message_image.setVisibility(View.INVISIBLE);
        holder.receive_message_photo.setVisibility(View.INVISIBLE);

        holder.setDetails(chat);


    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    class PlanetHolder extends RecyclerView.ViewHolder {

        private TextView receive_message_text, sender_message_text;
        private CircleImageView receive_message_photo;
        private ImageView receive_message_image, sender_message_image;
        View mView;


        PlanetHolder(View itemView) {
            super(itemView);
            mView = itemView;
            receive_message_photo = (CircleImageView) itemView.findViewById(R.id.receive_message_photo);
            receive_message_text = (TextView) itemView.findViewById(R.id.receive_message_text);
            sender_message_text = (TextView) itemView.findViewById(R.id.sender_message_text);
            sender_message_image = (ImageView) itemView.findViewById(R.id.sender_message_image);
            receive_message_image = (ImageView) itemView.findViewById(R.id.receive_message_image);

        }

        void setDetails(Chat chat) {


        }
    }
}
