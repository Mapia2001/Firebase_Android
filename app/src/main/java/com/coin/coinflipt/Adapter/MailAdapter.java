package com.coin.coinflipt.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.coin.coinflipt.Activity.ChatActivity;
import com.coin.coinflipt.Activity.ProfileActivity;
import com.coin.coinflipt.Component.Chat;
import com.coin.coinflipt.Component.Mail;
import com.coin.coinflipt.Component.User;
import com.coin.coinflipt.Fragment.InviteEditFragment;
import com.coin.coinflipt.Fragment.MailFragment;
import com.coin.coinflipt.R;
import com.coin.coinflipt.Activity.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MailAdapter extends RecyclerView.Adapter<MailAdapter.PlanetHolder> implements Filterable {

    private Context context;
    private ArrayList<Mail> clients;
    boolean like = false;
    MailFragment mail_frag = new MailFragment();
    String invite_coin, invite_name, invite_email, invite_status;
    private TextView invite_sender, invite_date;
    private ImageView mail_photo;

    private FirebaseFirestore db;
    private FirebaseUser current_user;
    private FirebaseAuth mAuth;
    public MailAdapter(Context context, ArrayList<Mail> guiders) {
        this.context = context;
        this.clients = guiders;
    }


    @NonNull
    @Override
    public PlanetHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mail, parent, false);
        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();
        return new PlanetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetHolder holder, final int position) {
        final Mail mail = clients.get(position);
        holder.setDetails(mail);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invite_coin = clients.get(position).getInvite_coin();
                invite_email = clients.get(position).getInvite_email();
                invite_status = clients.get(position).getInvite_status();
                db = FirebaseFirestore.getInstance();

                db.collection("userList").document(invite_email).get().
                        addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Map<String, Object> map = documentSnapshot.getData();
                                invite_name = (String) map.get("name");

                                FirebaseStorage storage = FirebaseStorage.getInstance();;

                                StorageReference storageReference = storage.getReference().child("images").child(invite_email);

                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        showAcceptDlg(invite_name, uri);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });


                            }
                        });

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


        View mView;
        PlanetHolder(View itemView) {
            super(itemView);
            mView = itemView;
            invite_sender = itemView.findViewById(R.id.label_invite_sender);
            invite_date = itemView.findViewById(R.id.label_invite_date);
            mail_photo = itemView.findViewById(R.id.invite_status);
        }

        void setDetails(Mail mail) {
            if ( mail.getInvite_status().equals("1") ){
                mail_photo.setImageResource(R.drawable.ico_open_invitation);
            }else{
                mail_photo.setImageResource(R.drawable.ico_new_invitatiaon);
            }
            invite_sender.setText(mail.getInvite_email());
            invite_date.setText(mail.getInvite_date());
        }
    }


    private void showAcceptDlg(final String name, final Uri uri ){
        final Dialog dialog = new Dialog(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialog_view = inflater.inflate(R.layout.item_accept_dlg, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        HomeActivity.getInstance().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        dialog.setContentView(dialog_view);
        Rect displayRectangle = new Rect();
        Window full_window = HomeActivity.getInstance().getWindow();
        full_window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog_view.setMinimumWidth((int)(displayRectangle.width() * 0.9f));

        CircleImageView sender_photo = dialog_view.findViewById(R.id.sender_photo);
        TextView invited_user_name = dialog_view.findViewById(R.id.invited_user_name);
        TextView invited_coin_amount = dialog_view.findViewById(R.id.invited_coin_amount);
        Button btn_accept = dialog_view.findViewById(R.id.btn_accept);
        Button invite_cancel = dialog_view.findViewById(R.id.invite_cancel);
        invited_coin_amount.setText(invite_coin);
        invited_user_name.setText(name);
        Glide.with(HomeActivity.getInstance()).load(uri).into(sender_photo);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Map<String, Object> accepted = new HashMap<>();
                accepted.put("accept_status", "1");
                accepted.put("send_date", invite_date.getText().toString());
                accepted.put("coin", invite_coin);
                db.collection("userList").document(current_user.getEmail()).collection("invite")
                        .document(invite_email).set(accepted).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

                addFriend(name, uri);
                startChat(name, uri);

            }
        });

        if ( invite_status.equals("1") ){
            btn_accept.setVisibility(View.INVISIBLE);
            invite_cancel.setVisibility(View.INVISIBLE);
        }

        invite_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void addFriend(String name, Uri uri){

        Map <String, Object> friendInfo = new HashMap<>();
        friendInfo.put("name", name);
        friendInfo.put("email", invite_email);
        friendInfo.put("photo", uri.toString());

        db.collection("userList").document(current_user.getEmail()).collection("friendList")
                .document(invite_email).set(friendInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    private void startChat(String name, Uri uri){
        Intent intent = new Intent(HomeActivity.getInstance(), ChatActivity.class);

        intent.putExtra("partner_email", invite_email);
        intent.putExtra("partner_name", name);
        intent.putExtra("partner_uri", uri.toString());
        HomeActivity.getInstance().startActivity(intent);
    }
}
