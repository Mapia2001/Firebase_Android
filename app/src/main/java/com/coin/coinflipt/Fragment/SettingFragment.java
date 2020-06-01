package com.coin.coinflipt.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.coin.coinflipt.Activity.HomeActivity;
import com.coin.coinflipt.Activity.ProfileActivity;
import com.coin.coinflipt.R;

public class SettingFragment extends Fragment
{
    Button btn_profile, secure_save;
    EditText setting_username, setting_userpass;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        btn_profile = view.findViewById(R.id.user_profile);
        secure_save = view.findViewById(R.id.usersecu_save);
        setting_username = view.findViewById(R.id.setting_username);
        setting_userpass = view.findViewById(R.id.setting_userpass);

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.getInstance(), ProfileActivity.class);
                HomeActivity.getInstance().startActivity(intent);
            }
        });

        return view;
    }
}
