package com.coin.coinflipt.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.coin.coinflipt.Fragment.FriendListFragment;
import com.coin.coinflipt.Fragment.MailFragment;
import com.coin.coinflipt.Fragment.TradeFragment;
import com.coin.coinflipt.Fragment.InviteFragment;
import com.coin.coinflipt.Fragment.SettingFragment;
import com.coin.coinflipt.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView menu;
    public static HomeActivity myself;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        myself = this;
        menu = findViewById(R.id.bottom_navigation);
        final TradeFragment trade = new TradeFragment();
        final InviteFragment invite = new InviteFragment();
        final FriendListFragment friends = new FriendListFragment();
        final SettingFragment setting = new SettingFragment();
        final MailFragment mail = new MailFragment();

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_tradelist);
        openFragment(trade);


        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_tradelist:
                                openFragment(trade);
                                return true;
                            case R.id.menu_invite:
                                openFragment(invite);
                                return true;
                            case R.id.menu_friendlist:
                                openFragment(friends);
                                return true;
                            case R.id.menu_setting:
                                openFragment(setting);
                                return true;
                            case R.id.menu_mail:
                                openFragment(mail);
                                return true;
                        }
                        return false;
                    }
                };

        menu.setOnNavigationItemSelectedListener( navigationItemSelectedListener);
    }
    public void openFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, fragment);

        transaction.commit();
    }

    public static HomeActivity getInstance(){
        return myself;
    }

    @Override
    public void onBackPressed(){
//        super.onBackPressed();
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button okButton = (Button) dialog.findViewById(R.id.btn_logout);
        Button cancelButton = (Button) dialog.findViewById(R.id.btn_cancel);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
