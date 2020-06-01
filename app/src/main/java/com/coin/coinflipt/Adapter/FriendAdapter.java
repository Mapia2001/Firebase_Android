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
import com.coin.coinflipt.Component.Friend;
import com.coin.coinflipt.R;
import com.coin.coinflipt.Activity.HomeActivity;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.PlanetHolder> implements Filterable {

    private Context context;
    private ArrayList<Friend> clients;
    boolean like = false;

    public FriendAdapter(Context context, ArrayList<Friend> guiders) {
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
        final Friend friend = clients.get(position);
        holder.setDetails(friend);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.getInstance(), ChatActivity.class);
                intent.putExtra("partner_name",clients.get(position).getUser_name());
                intent.putExtra("partner_uri", clients.get(position).getUser_photo().toString());
                intent.putExtra("partner_email", clients.get(position).getUser_email());
                HomeActivity.getInstance().startActivity(intent);
            }
        });

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

        private TextView name, client_email;
        private TextView num_message;
        private CardView new_message;
        private ImageView photo;
        View mView;


        PlanetHolder(View itemView) {
            super(itemView);
            mView = itemView;
            name = itemView.findViewById(R.id.invite_name_friend);
            client_email = itemView.findViewById(R.id.invite_email_friend);
            photo = itemView.findViewById(R.id.invite_photo_friend);
        }

        void setDetails(Friend friend) {

            name.setText(friend.getUser_name());
            client_email.setText(friend.getUser_email());
            if(friend.getUser_photo() == null){
                photo.setImageResource(R.drawable.unknow_profile);
            }
            else
            {
                Glide.with(HomeActivity.getInstance()).load(friend.getUser_photo()).into(photo);
            }
//            last_message.setText(client.getLastMessage());
//            if(Integer.parseInt(client.getNumNewMessage()) > 0){
//                new_message.setVisibility(View.VISIBLE);
//                num_message.setText(client.getNumNewMessage());
//            }
        }
    }
}
