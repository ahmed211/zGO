package com.example.ahmed.zgo;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by ahmed on 1/16/2017.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {


    public ArrayList<Bitmap> imageList;

    public RecyclerAdapter(ArrayList<Bitmap> imageList) {
        this.imageList=imageList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView)itemView.findViewById(R.id.history_image);

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.image.setImageBitmap(imageList.get(position));
    }


    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
