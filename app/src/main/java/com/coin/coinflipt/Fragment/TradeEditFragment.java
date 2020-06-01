package com.coin.coinflipt.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.coin.coinflipt.Adapter.FriendAdapter;
import com.coin.coinflipt.Adapter.UserAdapter;
import com.coin.coinflipt.Component.Friend;
import com.coin.coinflipt.Component.User;
import com.coin.coinflipt.R;
import com.coin.coinflipt.Activity.HomeActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TradeEditFragment extends Fragment
{
    String photouri;
    CircleImageView profileimage;
    TextView trade_name, trade_email, account_balance, trade_amount;
    Button trade_send;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tradeedit, container, false);
        profileimage = view.findViewById(R.id.invite_photo_friend);
        trade_name = view.findViewById(R.id.invite_name_friend);
        trade_email = view. findViewById(R.id.invite_email_friend);
        trade_amount = view.findViewById(R.id.trade_amount);
        account_balance = view.findViewById(R.id.account_balance);
        trade_send = view.findViewById(R.id.trade_send);

        //retreving data using bundle
        Bundle bundle=getArguments();
        trade_name.setText(String.valueOf(bundle.getString("name")));
        trade_email.setText(String.valueOf(bundle.getString("email")));
        photouri = String.valueOf(bundle.getString("src"));
        trade_amount.setText(String.valueOf(bundle.getString("tradeamount")));
        if(photouri.equals("")){
            profileimage.setImageResource(R.drawable.anonymous_user);
        }
        else
        {
            Glide.with(HomeActivity.getInstance()).load(photouri).into(profileimage);
        }

        return view;
    }
}
