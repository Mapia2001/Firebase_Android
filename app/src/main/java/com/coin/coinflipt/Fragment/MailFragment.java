package com.coin.coinflipt.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coin.coinflipt.Adapter.MailAdapter;
import com.coin.coinflipt.Adapter.TradeAdapter;
import com.coin.coinflipt.Adapter.UserAdapter;
import com.coin.coinflipt.Component.Mail;
import com.coin.coinflipt.Component.Trade;
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

import java.util.ArrayList;
import java.util.Map;

public class MailFragment extends Fragment

{

    RecyclerView list_mail;
    ArrayList<Mail> mails = new ArrayList<>();

    private FirebaseFirestore db;
    private FirebaseUser current_user;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_mail, container, false);

        list_mail = view.findViewById(R.id.list_mail);

        if (!mails.isEmpty()){
            list_mail.removeAllViewsInLayout(); //removes all the views

            //then reload the data
            mails.clear();
        }

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();

        db.collection("userList").document(current_user.getEmail()).collection("invite")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        Map<String, Object> map = documentSnapshot.getData();
                        final String status = (String) map.get("accept_status");
                        final String coin = (String) map.get("coin");
                        final String send_date = (String) map.get("send_date");
                        mails.add(new Mail(documentSnapshot.getId(), send_date, coin, status));
                    }
                    MailAdapter adapter  = new MailAdapter(getContext(), mails);
                    list_mail.setLayoutManager(new LinearLayoutManager(HomeActivity.getInstance()));
                    list_mail.setAdapter(adapter);

                }else{
                    Toast.makeText(HomeActivity.getInstance(), "load failed", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        return view;
    }
}
