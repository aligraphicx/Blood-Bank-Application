package com.example.ali.linkusapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UpdateName extends AppCompatActivity {

    private EditText nameField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);

        nameField=findViewById(R.id.updateName);
        findViewById(R.id.updateNameBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(UpdateName.this, "Nwo Change Pressed", Toast.LENGTH_SHORT).show();
                updateName();
            }
        });

    }



    private void updateName(){


        final ProgressDialog progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Updating . . .");
        if(nameField.getText().toString().length()<3){
            nameField.setError("Name too short");
        }else {

            progressDialog.show();
            UserProfileChangeRequest request=new UserProfileChangeRequest.Builder().setDisplayName(nameField.getText().toString()).build();
            FirebaseAuth.getInstance().getCurrentUser().updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    progressDialog.dismiss();
                    if(task.isSuccessful()){
                        startActivity(new Intent(UpdateName.this,HomePage.class));
                        finish();
                        Toast.makeText(UpdateName.this, "NameChanged", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }



    }
}
