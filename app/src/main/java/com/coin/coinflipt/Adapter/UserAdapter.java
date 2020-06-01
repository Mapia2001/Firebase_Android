package com.coin.coinflipt.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.coin.coinflipt.Component.User;
import com.coin.coinflipt.Fragment.InviteEditFragment;
import com.coin.coinflipt.R;
import com.coin.coinflipt.Activity.HomeActivity;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.PlanetHolder> implements Filterable {

    private Context context;
    private ArrayList<User> clients;
    boolean like = false;
    InviteEditFragment inviteedit_frag = new InviteEditFragment();
    public UserAdapter(Context context, ArrayList<User> guiders) {
        this.context = context;
        this.clients = guiders;
    }


    @NonNull
    @Override
    public PlanetHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new PlanetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetHolder holder, final int position) {
        final User user = clients.get(position);
        holder.setDetails(user);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(GuiderActivity.getInstance(), ChatActivity.class);
//                intent.putExtra("partner_index",position);
//                GuiderActivity.getInstance().startActivity(intent);
     }

        });

        holder.btn_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = HomeActivity.getInstance().getSupportFragmentManager().beginTransaction();

                String src = clients.get(position).getUser_email();

                Bundle args = new Bundle();
                args.putString("name", clients.get(position).getUser_name());
                args.putString("email", clients.get(position).getUser_email());
                inviteedit_frag.setArguments(args);
                transaction.replace(R.id.frame_main, inviteedit_frag);
                transaction.commit();
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
        private Button btn_invite;
        PlanetHolder(View itemView) {
            super(itemView);
            mView = itemView;
            name = itemView.findViewById(R.id.invite_name_friend);
            client_email = itemView.findViewById(R.id.invite_email_friend);
            photo = itemView.findViewById(R.id.invite_photo_friend);
            btn_invite = (Button) itemView.findViewById(R.id.btn_invite);
        }

        void setDetails(User user) {

            name.setText(user.getUser_name());
            client_email.setText(user.getUser_email());
            if(user.getUser_photo() == null){
                photo.setImageResource(R.drawable.unknow_profile);
            }
            else
            {
                Glide.with(HomeActivity.getInstance()).load(user.getUser_photo()).into(photo);
            }
//            last_message.setText(client.getLastMessage());
//            if(Integer.parseInt(client.getNumNewMessage()) > 0){
//                new_message.setVisibility(View.VISIBLE);
//                num_message.setText(client.getNumNewMessage());
//            }
        }
    }
}
