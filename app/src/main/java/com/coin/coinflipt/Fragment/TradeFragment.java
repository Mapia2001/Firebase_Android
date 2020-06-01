package com.coin.coinflipt.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coin.coinflipt.Adapter.TradeAdapter;
import com.coin.coinflipt.Adapter.UserAdapter;
import com.coin.coinflipt.Component.Trade;
import com.coin.coinflipt.Component.User;
import com.coin.coinflipt.R;
import com.coin.coinflipt.Activity.HomeActivity;

import java.util.ArrayList;

public class TradeFragment extends Fragment

{

    RecyclerView trade_list;
    ArrayList<Trade> trades = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_trade, container, false);

        trade_list = view.findViewById(R.id.list_trade);

        if (!trades.isEmpty()){
            trade_list.removeAllViewsInLayout(); //removes all the views

            //then reload the data
            trades.clear();
        }

        trades.add(new Trade("Trade2","trade coin amount1","+7887439943874","sfsfdsd9ofwfejsdf","https://lh3.googleusercontent.com/a-/AOh14Gi0oykjWSvXqX7tQlsh9iZEa_2_sv2Utl5CM4Xa=s96-c-rg-br100"));
        trades.add(new Trade("Trade4","trade coin amount3","+7887439943874","sfsfdsd9ofwfejsdf","https://lh3.googleusercontent.com/a-/AOh14Gi0oykjWSvXqX7tQlsh9iZEa_2_sv2Utl5CM4Xa=s96-c-rg-br100"));
        trades.add(new Trade("Trade5","trade coin amount4","+7887439943874","sfsfdsd9ofwfejsdf","https://lh3.googleusercontent.com/a-/AOh14Gi0oykjWSvXqX7tQlsh9iZEa_2_sv2Utl5CM4Xa=s96-c-rg-br100"));

        TradeAdapter adapter  = new TradeAdapter(getContext(), trades);
        trade_list.setLayoutManager(new LinearLayoutManager(HomeActivity.getInstance()));
        trade_list.setAdapter(adapter);
        return view;
    }
}
