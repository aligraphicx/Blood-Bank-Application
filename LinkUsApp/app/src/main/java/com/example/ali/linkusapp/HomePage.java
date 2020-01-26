package com.example.ali.linkusapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{

    private ImageView mainProfile;
    private TextView mainTitle;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    private RecyclerViewAdaptor adaptor;
    private List<BlodDonor> blodDonors;
    private static final String TAG = "HomePage";
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomePage.this,DonorCreator.class));
                finish();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        initObjects(navigationView.getHeaderView(0));
        setMainProfileData(FirebaseAuth.getInstance().getCurrentUser());

        recyclerView=findViewById(R.id.recView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        blodDonors=new ArrayList<>();
//        for(int i=0;i<20;i++){
//
//            BlodDonor blodDonor =new BlodDonor();
//            blodDonor.setName("Name:: "+i);
//            blodDonors.add(blodDonor);
//
//        }


        firebaseDatabase.getReference("Donors").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                BlodDonor temp;
                temp=dataSnapshot.getValue(BlodDonor.class);
                Log.d(TAG, "onChildAdded: new "+dataSnapshot.child("profile").getValue(String.class));
                temp.setProfileLink(dataSnapshot.child("profile").getValue(String.class));

                blodDonors.add(temp);
                adaptor=new RecyclerViewAdaptor(blodDonors, HomePage.this);
                recyclerView.setAdapter(adaptor);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d(TAG, "onCreate: Donors "+blodDonors.size());


    }




    private void initObjects(View nav){
        mainTitle=nav.findViewById(R.id.mainTitle);
        mainProfile=nav.findViewById(R.id.mainProfile);
        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        //currentUser=mAuth.getCurrentUser();


    }

    private void setMainProfileData(FirebaseUser currentUser){


      //  mainTitle.setText(currentUser.getDisplayName());
        Picasso.get().load(currentUser.getPhotoUrl()).centerCrop().resize(512,512).
                transform(new RoundedCornersTransformation(300,0)).into(mainProfile);
        mainTitle.setText(currentUser.getDisplayName());

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_change_pss) {
            startActivity(new Intent(HomePage.this,ChangePassword.class));
        } else if (id == R.id.nav_change_email) {
            startActivity(new Intent(HomePage.this,UpdateEmail.class));


        } else if (id == R.id.nav_change_name) {
            startActivity(new Intent(HomePage.this,UpdateName.class));
            finish();
        } else if (id == R.id.nav_signout) {
            SignOut();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }


    private void SignOut(){

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(HomePage.this,Login.class));
        finish();
    }

}
