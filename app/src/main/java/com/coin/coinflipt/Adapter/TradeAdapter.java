package com.coin.coinflipt.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.coin.coinflipt.Component.Trade;
import com.coin.coinflipt.Fragment.TradeEditFragment;
import com.coin.coinflipt.R;
import com.coin.coinflipt.Activity.HomeActivity;

import java.util.ArrayList;

public class TradeAdapter extends RecyclerView.Adapter<TradeAdapter.PlanetHolder> implements Filterable {

    private Context context;
    private ArrayList<Trade> clients;
    boolean like = false;
    TradeEditFragment trade_edit_fra = new TradeEditFragment();
    public TradeAdapter(Context context, ArrayList<Trade> guiders) {
        this.context = context;
        this.clients = guiders;
    }


    @NonNull
    @Override
    public PlanetHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trade, parent, false);
        return new PlanetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetHolder holder, final int position) {
        final Trade trade = clients.get(position);
        holder.setDetails(trade);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(GuiderActivity.getInstance(), ChatActivity.class);
//                intent.putExtra("partner_index",position);
//                GuiderActivity.getInstance().startActivity(intent);

                FragmentTransaction transaction = HomeActivity.getInstance().getSupportFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                args.putString("src",clients.get(position).getUser_photo());
                args.putString("name", clients.get(position).getUser_name());
                args.putString("email", clients.get(position).getUser_email());
                args.putString("tradeamount", "20");
                trade_edit_fra.setArguments(args);
                transaction.replace(R.id.frame_main, trade_edit_fra);
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


        PlanetHolder(View itemView) {
            super(itemView);
            mView = itemView;
            name = itemView.findViewById(R.id.invite_name_friend);
            client_email = itemView.findViewById(R.id.invite_email_friend);
            photo = itemView.findViewById(R.id.invite_photo_friend);
        }

        void setDetails(Trade trade) {

            name.setText(trade.getUser_name());
            client_email.setText(trade.getUser_email());
            if(trade.getUser_photo().equals("")){
                photo.setImageResource(R.drawable.unknow_profile);
            }
            else
            {
                Glide.with(HomeActivity.getInstance()).load(trade.getUser_photo()).into(photo);
            }
//            last_message.setText(client.getLastMessage());
//            if(Integer.parseInt(client.getNumNewMessage()) > 0){
//                new_message.setVisibility(View.VISIBLE);
//                num_message.setText(client.getNumNewMessage());
//            }
        }
    }
}
