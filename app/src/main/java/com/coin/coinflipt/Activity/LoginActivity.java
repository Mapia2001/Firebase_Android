package com.coin.coinflipt.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coin.coinflipt.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private boolean isValid = false, isNameCheck = false, isPassCheck = false;
    String userEmail = "", password = "";
    public static final String MyPREFERENCES = "data_coinflipt" ;
    String firsttime = "firsttime";
    EditText useredit, passwordedit;
    Button login;
    TextView signup,btn_forgot;
    RelativeLayout google_login;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(sharedpreferences.getBoolean(firsttime,true)){
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(firsttime, false);
            editor.commit();
            Intent intent = new Intent(LoginActivity.this, IntroActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_login);
        init_view();
        init_action();




    }

    private void init_view() {
        useredit = findViewById(R.id.btn_loguseremail);
        passwordedit = findViewById(R.id.btn_logpassword);
        login  = findViewById(R.id.btn_login);
        signup = findViewById(R.id.btn_signup);
        btn_forgot = findViewById(R.id.btn_forgot);
        google_login = findViewById(R.id.google_login);
    }

    private void init_action() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Patterns.EMAIL_ADDRESS.matcher(useredit.getText().toString()).matches()){
                    useredit.setError("invalid email type");
                    useredit.requestFocus();
                    return;
                }
                mAuth.sendPasswordResetEmail(userEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "google log in", Toast.LENGTH_LONG).show();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                //Then we will get the GoogleSignInClient object from GoogleSignIn class
//                mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
//                signIn();
            }
        });
    }

    private void login(){

        userEmail = useredit.getText().toString();
        password = passwordedit.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            useredit.setError("invalid email type");
            useredit.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)){
            passwordedit.setError("enter password");
            passwordedit.requestFocus();
            return;
        }

        FirebaseApp.initializeApp(LoginActivity.this);
        mAuth.signInWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent;
                            intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(LoginActivity.this,"failed.", Toast.LENGTH_LONG).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}
