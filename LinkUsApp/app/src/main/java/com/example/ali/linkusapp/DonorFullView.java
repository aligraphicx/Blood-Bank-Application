package com.example.ali.linkusapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class DonorFullView extends AppCompatActivity {

    private ImageView pr;
    private TextView title,city,phone,group;
    private Button call,msg;
    private BlodDonor donor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_full_view);
        title=findViewById(R.id.fullDName);
        city=findViewById(R.id.fullDCity);
        phone=findViewById(R.id.fullDPhone);
        group=findViewById(R.id.fullDGroup);
        call=findViewById(R.id.donorCallBtn);
        msg=findViewById(R.id.donorMsgBtn);
         donor =getIntent().getParcelableExtra("Donor");
         setData(donor);
     //    if(PackageManager.)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    1);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" +donor.getPhone()));
                startActivity(intent);
            }
        });
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);

                intent.addCategory(Intent.CATEGORY_DEFAULT);

                intent.setType("vnd.android-dir/mms-sms");

                startActivity(intent);
            }
        });


    }


    private void setData(BlodDonor donor){


        title.setText(donor.getName());
        city.setText(donor.getCity());
        phone.setText(donor.getPhone());
        group.setText(donor.getGroup());
       // Picasso.get().load(Uri.parse(donor.getProfileLink())).centerCrop().resize(512,512).
               // transform(new RoundedCornersTransformation(50,0)).into(pr);
       // Glide.with(getApplicationContext()).load(Uri.parse(donor.getProfileLink())).into(pr);
    }
}
