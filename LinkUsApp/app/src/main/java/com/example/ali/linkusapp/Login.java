package com.example.ali.linkusapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private ImageButton loginBtn;
    private EditText loginEmail;
    private EditText loginPassword;
    private Button signUpBtn;
    private ProgressDialog progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        objectsIniti();



    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void objectsIniti(){

        progressBar=new ProgressDialog(this);


        firebaseAuth = FirebaseAuth.getInstance();
        loginBtn=findViewById(R.id.loginBtn);
        loginEmail=findViewById(R.id.login_Email);
        loginPassword=findViewById(R.id.login_Password);
        signUpBtn=findViewById(R.id.signupBtn);
        loginBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);


    }

    private void firebaseAuth(){


        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth!=null){
                    currentUser=firebaseAuth.getCurrentUser();
                    if(currentUser!=null){
                        startActivity(new Intent(Login.this,HomePage.class));
                        finish();
                    }

                 //   Toast.makeText(Login.this, currentUser.getPhotoUrl().toString(), Toast.LENGTH_SHORT).show();
                  //  Toast.makeText(Login.this, currentUser.getPhoneNumber(), Toast.LENGTH_SHORT).show();
                }else{

                }

            }
        };

    }
    private void loginThisUser(String e,String p){
        if(Patterns.EMAIL_ADDRESS.matcher(e).matches()&&p.length()>=6) {

            progressBar.setTitle("Validation Please wait ...");
            progressBar.show();
            firebaseAuth.signInWithEmailAndPassword(e, p)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(Login.this, "User Found", Toast.LENGTH_SHORT).show();
                                progressBar.dismiss();
                            } else {

                                Toast.makeText(Login.this, "User Not Found", Toast.LENGTH_SHORT).show();
                                progressBar.dismiss();
                            }

                        }
                    });
        }else if(!Patterns.EMAIL_ADDRESS.matcher(e).matches()){
            loginEmail.setError("Email is not Correct");
        }
        else if(p.length()<6){
            loginPassword.setError("Password too short");
        }


    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){


            case R.id.loginBtn:

                String userEmail=loginEmail.getText().toString();
                String userPass=loginPassword.getText().toString();
                loginThisUser(userEmail,userPass);

                break;


            case R.id.signupBtn:
                startActivity(new Intent(Login.this,SignUp.class));
                finish();


                break;
        }
    }
}
