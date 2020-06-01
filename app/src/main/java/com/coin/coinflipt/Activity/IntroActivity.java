package com.coin.coinflipt.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coin.coinflipt.Adapter.IntroAdapter;
import com.coin.coinflipt.R;
import com.coin.coinflipt.Views.ClickableViewPager;
import com.coin.coinflipt.Views.PrefManager;
import com.github.vivchar.viewpagerindicator.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ClickableViewPager view_pager_slide;
    private IntroAdapter slide_adapter;
    private List<Integer> slideList= new ArrayList<>();
    private ViewPagerIndicator view_pager_indicator;
    private RelativeLayout relative_layout_slide;
    private LinearLayout linear_layout_skip;
    private PrefManager prefManager;
    private LinearLayout linear_layout_next;
    private TextView text_view_next_done;

    private String email, password, name;
    private String TAG = "PrivacyPolicy";
    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        prefManager= new PrefManager(getApplicationContext());

        slideList.add(1);
        slideList.add(2);
        slideList.add(3);
        slideList.add(4);
        slideList.add(5);

        this.text_view_next_done=(TextView) findViewById(R.id.text_view_next_done);
        this.linear_layout_next=(LinearLayout) findViewById(R.id.linear_layout_next);
        this.linear_layout_skip=(LinearLayout) findViewById(R.id.linear_layout_skip);
        this.view_pager_indicator=(ViewPagerIndicator) findViewById(R.id.view_pager_indicator);
        this.view_pager_slide=(ClickableViewPager) findViewById(R.id.view_pager_slide);
        this.relative_layout_slide=(RelativeLayout) findViewById(R.id.relative_layout_slide);
        slide_adapter = new IntroAdapter(getApplicationContext(),slideList);
        view_pager_slide.setAdapter(this.slide_adapter);
        view_pager_slide.setOffscreenPageLimit(1);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        name = intent.getStringExtra("name");
        loading = new ProgressDialog(IntroActivity.this);
        loading.setTitle(getString(R.string.progress_sign_up));
        //view_pager_slide.setPageTransformer(false, new CarouselEffectTransformer(IntroActivity.this)); // Set transformer


        view_pager_slide.setOnItemClickListener(new ClickableViewPager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position <6){
                    view_pager_slide.setCurrentItem(position+1);
                }else{
                    sign_up();
                    finish();
                }
            }
        });
        this.linear_layout_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (text_view_next_done.getText().equals("DONE")){

                    sign_up();
                    finish();
                }
                if ( view_pager_slide.getCurrentItem() < slideList.size()) {
                    view_pager_slide.setCurrentItem(view_pager_slide.getCurrentItem() + 1);
                    return;
                }

            }
        });
        view_pager_slide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position+1==slideList.size()){
                    text_view_next_done.setText("DONE");
                }else{
                    text_view_next_done.setText("NEXT");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        this.linear_layout_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
            }
        });
        this.view_pager_slide.setClipToPadding(false);
        this.view_pager_slide.setPageMargin(0);
        view_pager_indicator.setupWithViewPager(view_pager_slide);
    }

    private void sign_up() {

        Intent intent = new Intent(IntroActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
        /*
        loading.show();
        FirebaseApp.initializeApp(LoginActivity.getInstance());
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.getInstance(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loading.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Global.my_email = user.getEmail();
                            upload_data();
                            Client client = new Client(name,email,"","",null);
                            Global.my_user_data = client;
                            Intent intent = new Intent(PrivacyActivity.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            LoginActivity.getInstance().finish();
                        } else {
                            loading.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toasty.error(LoginActivity.getInstance(), getString(R.string.error_authentication),
                                    Toasty.LENGTH_SHORT).show();
                        }

                    }
                });

         */
    }


    private void upload_data() {
        /*
        FirebaseApp.initializeApp(LoginActivity.getInstance());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put("user_email", email);
        user.put("user_name", name);
        user.put("user_photo", "");
        user.put("user_status", "online");
        user.put("user_type", "client");
        ArrayList<String> linked_gudider = new ArrayList<>();
        user.put("user_linked_guiders", linked_gudider);
        db.collection("users").document(email)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "upload user data:success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"Failed");
                    }
                });

         */

    }



}
