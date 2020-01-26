package com.example.ali.linkusapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;



public class RecyclerViewAdaptor extends RecyclerView.Adapter<RecyclerViewAdaptor.MyViewHolder> {

    private List<BlodDonor> blodDonors;
    private Context context;

    public RecyclerViewAdaptor(List<BlodDonor> blodDonors, Context context) {
        this.blodDonors = blodDonors;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.donorview, viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdaptor.MyViewHolder myViewHolder, int i) {

        myViewHolder.name.setText(blodDonors.get(i).getName());
        myViewHolder.group.setText(blodDonors.get(i).getGroup());
        myViewHolder.city.setText(blodDonors.get(i).getCity());
       //Picasso.get().load(Uri.parse(blodDonors.get(i).getProfileLink())).centerCrop().resize(512,512).
        //        transform(new RoundedCornersTransformation(50,0)).into(myViewHolder.profile);
      //  Glide.with(context).load(blodDonors.get(i).getProfileLink()).into(myViewHolder.profile);
        Glide.with(context)
                .load(blodDonors.get(i).getProfileLink())
                .apply(RequestOptions.circleCropTransform())
                .into(myViewHolder.profile);
    }

    @Override
    public int getItemCount() {
        return blodDonors.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView name;
        public TextView group;
        public TextView city;
        public ImageView profile;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
           name= itemView.findViewById(R.id.donorName);
           group=itemView.findViewById(R.id.donoBlodGroup);
           city=itemView.findViewById(R.id.donoAddress);
           profile=itemView.findViewById(R.id.donorProfile);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Now Clicked On "+getAdapterPosition(), Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(context,DonorFullView.class);
            intent.putExtra("Donor", blodDonors.get(getAdapterPosition()));
            Log.d("Recy", "onClick: Rec"+blodDonors.get(getAdapterPosition()).getProfileLink());
            context.startActivity(intent);

        }
    }
}
