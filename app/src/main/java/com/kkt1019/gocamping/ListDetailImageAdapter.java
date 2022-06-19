package com.kkt1019.gocamping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kkt1019.gocamping.databinding.RecyclerItemBinding;

import java.util.ArrayList;

public class ListDetailImageAdapter extends RecyclerView.Adapter<ListDetailImageAdapter.VH> {

    Context context;
    ArrayList<Item> items;

    public ListDetailImageAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.list_detail_image, parent, false);

        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Item item = items.get(position);

        Glide.with(context).load(item.imageUrl).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{

        ImageView iv;

        public VH(@NonNull View itemView) {
            super(itemView);
            iv= itemView.findViewById(R.id.iv);
        }


    }
}
