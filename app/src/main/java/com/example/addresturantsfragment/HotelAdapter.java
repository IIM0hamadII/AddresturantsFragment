package com.example.addresturantsfragment;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.MyViewHolder> {
    Context context;
    ArrayList<Hotel> restList;
    private FirebaseServices fbs;

    public HotelAdapter(Context context, ArrayList<Hotel> restList) {
        this.context = context;
        this.restList = restList;
        this.fbs = FirebaseServices.getInstance();
    }

    @NonNull
    @Override
    public HotelAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v= LayoutInflater.from(context).inflate(R.layout.rest_item,parent,false);
        return  new HotelAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelAdapter.MyViewHolder holder, int position) {
        Hotel rest = restList.get(position);
        holder.tvName.setText(rest.getName());
        holder.tvPhone.setText(rest.getPhone());
        //holder.photo.setImageURI(rest.getPhoto());
        Picasso.get().load(rest.getPhoto()).into(holder.ivPhoto);
    }

    @Override
    public int getItemCount(){
        return restList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvPhone;
        ImageView ivPhoto;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvNameRestItem);
            tvPhone=itemView.findViewById(R.id.tvPhoneRestItem);
            ivPhoto=itemView.findViewById(R.id.imPhoto);
        }
    }
}
