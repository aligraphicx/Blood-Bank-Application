package com.example.ali.linkusapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UpdateEmail extends AppCompatActivity {

    private EditText emailField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        emailField=findViewById(R.id.updateEmail);
        findViewById(R.id.updateEmailBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmail();
            }
        });
    }




    private void updateEmail(){


        final ProgressDialog progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Updating . . .");
        if(!Patterns.EMAIL_ADDRESS.matcher(emailField.getText().toString()).matches()){
            emailField.setError("Incorrect Email");
        }else {

            progressDialog.show();
         //   UserProfileChangeRequest request=new UserProfileChangeRequest.Builder().setDisplayName(nameField.getText().toString()).build();
            FirebaseAuth.getInstance().getCurrentUser().updateEmail(emailField.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    progressDialog.dismiss();
                    if(task.isSuccessful()){
                        Toast.makeText(UpdateEmail.this, "Email Changed", Toast.LENGTH_SHORT).show();
                        finish();

                    }else{
                        Toast.makeText(UpdateEmail.this, "Email not Changed"+emailField.getText().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }



    }
}
