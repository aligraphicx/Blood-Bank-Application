package com.example.ali.linkusapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.io.Serializable;
import java.net.URI;
import java.util.Calendar;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    //==============================================================================================
    //Objects
    //==============================================================================================

    private EditText signUpEmail;
    private EditText signUpPhoneNumber;
    private EditText phoneNumberCode;
    private EditText signUpPassword;
    private EditText signUpConPassword;
    private EditText firstName;
    private EditText lastName;
    private ImageButton createAccount;
    private ImageView profilePic;
    private TextView errorMsg;
    private ProgressDialog progressDialog;
    private Uri profileUri;
    private static final String TAG = "SignUp";




    private FirebaseAuth mAuth;
    private int PICK_IMAGE_REQUEST = 1;
    private User signUpUser;
    private boolean profileIsSelected;

    //==============================================================================================
    //Override Methods
    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        objectsInit();


        findViewById(R.id.backToSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this,Login.class));
                finish();
            }
        });
    }



    private void objectsInit(){


        signUpUser=new User();
        mAuth=FirebaseAuth.getInstance();
        createAccount=findViewById(R.id.createBtn);
        createAccount.setOnClickListener(this);
        signUpEmail=findViewById(R.id.signUp_Email);
        signUpPhoneNumber=findViewById(R.id.signUp_Number);
        signUpPassword=findViewById(R.id.signUp_Password);
        signUpConPassword=findViewById(R.id.signUp_Con_Password);
        profilePic=findViewById(R.id.profilePic);
        profilePic.setOnClickListener(this);
        firstName=findViewById(R.id.firstName);
        lastName=findViewById(R.id.lastName);
        errorMsg=findViewById(R.id.errorMsg);
        phoneNumberCode=findViewById(R.id.countryCode);
        progressDialog =new ProgressDialog(SignUp.this);





    }




    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.createBtn:

                signUpUser=  UserDataCompressor(signUpEmail.getText().toString(),
                        firstName.getText().toString(),lastName.getText().toString()
                        ,signUpPhoneNumber.getText().toString(),phoneNumberCode.getText().toString()
                        ,signUpPassword.getText().toString(),profileUri);

                userDataValidation(signUpUser, signUpConPassword.getText().toString());



                break;


            case R.id.profilePic:

                getPictureFromGallery();
                break;
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST&&resultCode==RESULT_OK&&data.getData()!=null){
            Toast.makeText(this, "Now Image Return", Toast.LENGTH_SHORT).show();

            profileUri=data.getData();
            signUpUser.setProfileUri(profileUri);

            Log.d(TAG, "onActivityResult: "+signUpUser.getProfileUri().toString());
            Picasso.get().load(signUpUser.getProfileUri()).centerCrop().resize(512,512).
                    transform(new RoundedCornersTransformation(300,0)).into(profilePic);
        }
    }




    //==============================================================================================
    //Custom Methods
    //==============================================================================================


    private void userDataValidation(User newUser,String conPass){

        if(!profileIsSelected){
            errorMsg.setText("Please select profile ");
        }else if(newUser.getFirstName().isEmpty()){

            firstName.setError("name not be empty");
            Log.d(SignUp.this.getClass().getName(), "lastNaem");
        }else if(newUser.getLastName().isEmpty()){

            lastName.setError("name not be empty");
            Log.d(SignUp.this.getClass().getName(), "FName");

        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(newUser.getEmail()).matches()){

            signUpEmail.setError("Incorrect email");
            Log.d(SignUp.this.getClass().getName(), "email");

        }else if(phoneNumberCode.getText().toString().isEmpty()||phoneNumberCode.getText().toString().length()<3){
            phoneNumberCode.setError("Incorrect code");
        }else if(newUser.getPhoneNumber().length()<10){

            Log.d(SignUp.this.getClass().getName(), "Number");
            signUpPhoneNumber.setError("incorrect phone number");

        }else if(newUser.getPassword().length()<6){

            signUpPassword.setError("Password too short");
            Log.d(SignUp.this.getClass().getName(), "pass2");
        }else if(!newUser.getPassword().equals(conPass)){

            signUpConPassword.setError("Password not match");
            Log.d(SignUp.this.getClass().getName(), "pass2");
        }
        else{
            createUser();
        }
             Log.d(SignUp.this.getClass().getName(), "CreateBtn");

    }





    private User UserDataCompressor(String email,String firstName,
                                    String lastName,String phoneNumber,String cCode,String paasword,Uri profileLink
                                    ){
        User user=new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        phoneNumber=cCode+phoneNumber;
        user.setPhoneNumber(phoneNumber);
        user.setProfileUri(profileLink);
        user.setPassword(paasword);
        user.setFullName(user.getFirstName()+" "+user.getLastName());

        return  user;
    }





    private void getPictureFromGallery(){

        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        profileIsSelected=true;
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }


    private void createUser(){


        progressDialog.setMessage("Creating User");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(signUpUser.getEmail(), signUpUser.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            Log.d(TAG, "onComplete: Now user Created");

                            mAuth.signInWithEmailAndPassword(signUpUser.getEmail(), signUpUser.getPassword())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if(task.isSuccessful()){



                                                progressDialog.setMessage("Checking User Information");
                                                final FirebaseUser firebaseUser=mAuth.getCurrentUser();
                                                uploadProfilePic(signUpUser.getProfileUri(),firebaseUser );


                                            }
                                        }
                                    });









                        }
                    }
                });

    }







    private void uploadProfilePic(Uri uri, final FirebaseUser cUser){

        progressDialog.setMessage("Profile picture uploading . . .");
        final StorageReference ref= FirebaseStorage.getInstance().getReference(cUser.getUid()+"/"+"Profile/"+uri.getLastPathSegment());

        ref.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

                if(task.isSuccessful()){

                    Log.d(TAG, "onComplete: "+task.getResult().toString());
                    signUpUser.setProfileUri(task.getResult());
                    progressDialog.setMessage("Setting up your account");
                    updateUserProfile(cUser);


                }

            }
        });

    }


    private UserProfileChangeRequest getUpdateProfileData(){

        return new UserProfileChangeRequest.Builder()
                .setDisplayName(signUpUser.getFullName())
                .setPhotoUri(signUpUser.getProfileUri())
                .build();

    }


    private void personalDataSaver(FirebaseUser fUser){

        DatabaseReference databaseReference=
                FirebaseDatabase.getInstance().getReference(fUser.getUid()).child("Personal Info");
        databaseReference.child("FName").setValue(signUpUser.getFirstName());
        databaseReference.child("LName").setValue(signUpUser.getLastName());
        databaseReference.child("Full Name").setValue(signUpUser.getFullName());
        databaseReference.child("Profile").setValue(String.valueOf(signUpUser.getProfileUri()));
        databaseReference.child("Phone Number").setValue(signUpUser.getPhoneNumber());
        progressDialog.dismiss();
        mAuth.signOut();
        startActivity(new Intent(SignUp.this,Login.class));
        finish();

    }

    private void updateUserProfile(final FirebaseUser c){
        c.updateProfile(getUpdateProfileData()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    progressDialog.setMessage("Thank you !");
                    personalDataSaver(c);

                }
            }
        });

    }

}
