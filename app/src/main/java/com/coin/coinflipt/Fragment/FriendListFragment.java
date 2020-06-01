package com.coin.coinflipt.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coin.coinflipt.Adapter.FriendAdapter;
import com.coin.coinflipt.Adapter.UserAdapter;
import com.coin.coinflipt.Component.Friend;
import com.coin.coinflipt.Component.User;
import com.coin.coinflipt.R;
import com.coin.coinflipt.Activity.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FriendListFragment extends Fragment
{
    public RecyclerView friend_list;
    ArrayList<Friend> friends = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    boolean isMember = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friendlist, container, false);
        friend_list = view.findViewById(R.id.friend_list);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        if (!friends.isEmpty()){
            friend_list.removeAllViewsInLayout(); //removes all the views

            //then reload the data
            friends.clear();
        }

        db.collection("userList").document(currentUser.getEmail()).collection("friendList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> map = document.getData();
                                    final String username = (String) map.get("name");
                                    final String email = (String) map.get("email");
                                    final String phonenumber = (String) map.get("phonenumber");
                                    final Uri uri = Uri.parse((String) map.get("photo"));
                                    friends.add(new Friend(username,email,phonenumber,uri));
                                    isMember = true;
                            }

                            if ( !isMember ){
                                Toast.makeText(HomeActivity.getInstance(), "no members", Toast.LENGTH_LONG).show();
                            }else{
                                FriendAdapter adapter  = new FriendAdapter(getContext(), friends);
                                friend_list.setLayoutManager(new LinearLayoutManager(HomeActivity.getInstance()));
                                friend_list.setAdapter(adapter);
                            }
                        } else {
                            Toast.makeText(HomeActivity.getInstance(), "loading failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeActivity.getInstance(), "loading failed", Toast.LENGTH_LONG).show();
                    }
        });

        isMember = false;

        return view;

    }
}
