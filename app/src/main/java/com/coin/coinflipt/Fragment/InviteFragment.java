package com.coin.coinflipt.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coin.coinflipt.Adapter.UserAdapter;
import com.coin.coinflipt.Component.User;
import com.coin.coinflipt.R;
import com.coin.coinflipt.Activity.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

public class InviteFragment extends Fragment

{
    RecyclerView user_list;
    ArrayList<User> users = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    Bitmap btp;
//    InviteEditFragment inviteedit_fragment = new InviteEditFragment();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_invite, container, false);
        db = FirebaseFirestore.getInstance();
        user_list = view.findViewById(R.id.user_list);
        mAuth = FirebaseAuth.getInstance();
        if (!users.isEmpty()){
            user_list.removeAllViewsInLayout(); //removes all the views

            //then reload the data
           users.clear();
        }

        currentUser = mAuth.getCurrentUser();

        db.collection("userList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if ( !document.getId().equals(currentUser.getEmail()) ){
                                    Map<String, Object> map = document.getData();
                                    final String username = (String) map.get("name");
                                    final String email = (String) map.get("email");
                                    final String phonenumber = (String) map.get("phonenumber");
                                    final Uri uri = Uri.parse((String) map.get("photo"));
                                    users.add(new User(username,email,phonenumber,"assd",uri));
                                }
                            }
                            UserAdapter adapter  = new UserAdapter(getContext(), users);
                            user_list.setLayoutManager(new LinearLayoutManager(HomeActivity.getInstance()));
                            user_list.setAdapter(adapter);
                        } else {
                            Toast.makeText(HomeActivity.getInstance(), "loading failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        return view;
    }


}
