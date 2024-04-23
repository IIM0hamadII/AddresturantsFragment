package com.example.addresturantsfragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Hotel> restList;
    private FirebaseServices fbs;
    private OnItemClickListener itemClickListener;

    public HotelAdapter(Context context, ArrayList<Hotel> restList) {
        this.context = context;
        this.restList = restList;
        this.fbs = FirebaseServices.getInstance();
        this.itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                fbs.setSelectedHotel(restList.get(position));

                DetailedFragment cd = new DetailedFragment();
                FragmentTransaction ft= ((MainActivity)context).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout,cd);
                ft.commit();
            }
        };
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
        User u = fbs.getCurrentUser();
        if (u != null)
        {
            if (u.getFavorites().contains(rest.getName()))
                Picasso.get().load(R.drawable.favcheck).into(holder.ivFavourite);
            else
                Picasso.get().load(R.drawable.ic_fav).into(holder.ivFavourite);
        }
        holder.tvName.setText(rest.getName());
        holder.tvPhone.setText(rest.getPhone());
        //holder.photo.setImageURI(rest.getPhoto());
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(position);
            }
        });
        if (rest.getPhoto() == null || rest.getPhoto().isEmpty())
        {
            Picasso.get().load(R.drawable.ic_fav).into(holder.ivPhoto);
        }
        else {
            Picasso.get().load(rest.getPhoto()).into(holder.ivPhoto);
        }
        holder.ivFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite(rest) == true)
                {
                    removeStar(rest, holder);
                }
                else
                {
                    addStar(rest, holder);
                }
                fbs.setUserChangeFlag(true);
                //setFavourite(holder, car);
            }
        });
    }

    private void removeStar(Hotel hotel, HotelAdapter.MyViewHolder holder) {
        User u = fbs.getCurrentUser();
        if (u != null) {
            if (u.getFavorites().contains(hotel.getName())) {
                u.getFavorites().remove(hotel.getName());
                holder.ivFavourite.setImageResource(android.R.color.transparent);
                Picasso.get().load(R.drawable.ic_fav).into(holder.ivFavourite);
            }
        }
    }

    private void addStar(Hotel hotel, HotelAdapter.MyViewHolder holder) {
        User u = fbs.getCurrentUser();
        if (u != null) {
            u.getFavorites().add(hotel.getName());
            holder.ivFavourite.setImageResource(android.R.color.transparent);
            Picasso.get().load(R.drawable.favcheck).into(holder.ivFavourite);

        }
    }

    private boolean isFavorite(Hotel hotel) {
        User u = fbs.getCurrentUser();
        if (u != null)
        {
            if (u.getFavorites().contains(hotel.getName()))
                return true;
        }
        return false;
    }


    @Override
    public int getItemCount(){
        return restList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvPhone;
        ImageView ivPhoto,ivFavourite;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvNameRestItem);
            tvPhone=itemView.findViewById(R.id.tvPhoneRestItem);
            ivPhoto=itemView.findViewById(R.id.ivPhoto);
            ivFavourite = itemView.findViewById(R.id.ivFavoriteIcon);

        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }
}
