package com.example.ali.linkusapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MainChats extends AppCompatActivity {

//ebNfszGLvrc:APA91bEuQ44oxjouFPHIOACBKxjlugZqPz6Fx2-rQ8yTDzOGy79mMmr3GEr8etHMXpqXtVcKE05ZBpWUY-7DbyHf_t773UcJqld_1g0gT_rloISA7GluRt4x-kGVijgrgPfNB0vtedlE
    private ImageView profile;
    private FirebaseAuth mAuth;
    private static final String TAG = "MainChats";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chats);
        profile=findViewById(R.id.profileMain);

        Log.d(TAG, "onCreate: "+FirebaseInstanceId.getInstance().getToken());
        mAuth=FirebaseAuth.getInstance();
//        Toast.makeText(this, mAuth.getCurrentUser().getPhotoUrl().toString(), Toast.LENGTH_SHORT).show();
       Glide.with(MainChats.this).load(mAuth.getCurrentUser().getPhotoUrl()).into(profile);

        findViewById(R.id.mainBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainChats.this,Login.class));
                finish();
            }
        });
    }

}
