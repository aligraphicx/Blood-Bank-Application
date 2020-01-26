package com.example.ali.linkusapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class DonorCreator extends AppCompatActivity implements View.OnClickListener {

    private ImageView newDonorProfile;
    private EditText newDonorName;
    private EditText newDonorCity;
    private EditText newDonorPhone;
    private EditText newDonorBloodG;
    private Button addDonorBtn;

    private BlodDonor newBloodDonor;
    private Uri localProUri;
    private ProgressDialog progressDialog;

    private static final String TAG = "DonorCreator";
    private final static int IMAGE_REQ=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_creator);

        iniObjects();

    }

    private void iniObjects(){


        newDonorProfile=findViewById(R.id.newDonorProfile);
        newDonorProfile.setOnClickListener(this);
        newDonorName=findViewById(R.id.newDonorName);
        newDonorPhone=findViewById(R.id.newDonorPhon);
        newDonorBloodG=findViewById(R.id.newDonorBloodGroup);
        newDonorCity=findViewById(R.id.newDonorCity);
        addDonorBtn=findViewById(R.id.addNewDonor);
        addDonorBtn.setOnClickListener(this);
        progressDialog=new ProgressDialog(this );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.newDonorProfile:
                getPicture();
                break;
            case R.id.addNewDonor:
                newBloodDonor=getDonorData();

                break;

        }
    }


    private void getPicture(){

        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQ);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    if(requestCode==IMAGE_REQ&&resultCode==RESULT_OK){

        Picasso.get().load(data.getData()).centerCrop().resize(512,512).
                transform(new RoundedCornersTransformation(300,0)).into(newDonorProfile);
        localProUri=data.getData();
    }

    }

    private BlodDonor getDonorData(){

        BlodDonor d=new BlodDonor();

        d.setName(newDonorName.getText().toString());
        d.setPhone(newDonorPhone.getText().toString());
        d.setCity(newDonorCity.getText().toString());
        d.setGroup(newDonorBloodG.getText().toString());
        uploadProfilePic(localProUri, FirebaseAuth.getInstance().getCurrentUser(), d);


        return  d;
    }

    private void uploadProfilePic(Uri uri, final FirebaseUser cUser, final BlodDonor donor){

        progressDialog.setMessage("Uploading profile . . .");
        progressDialog.show();
        final StorageReference ref= FirebaseStorage.getInstance().
                getReference("Donors/"+donor.getName()+"/"+uri.getLastPathSegment());

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
                    donor.setProfileLink(task.getResult().toString());
                    uploadDonorData(donor);


                }

            }
        });

    }

    private void uploadDonorData(final BlodDonor donor){

        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Donors/"+donor.getName());
progressDialog.setMessage("uploading personal info. . .");
        reference.child("name").setValue(donor.getName());
        reference.child("city").setValue(donor.getCity());
        reference.child("phone").setValue(donor.getPhone());
        reference.child("group").setValue(donor.getGroup());
        reference.child("profile").setValue(donor.getProfileLink());
        Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show();
        finish();
        progressDialog.dismiss();
    }
}
