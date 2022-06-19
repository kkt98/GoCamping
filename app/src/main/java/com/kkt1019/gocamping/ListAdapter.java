package com.kkt1019.gocamping;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.kkt1019.gocamping.databinding.RecyclerItemBinding;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.VH> {

    Context context;
    ArrayList<Item> items;



    public ListAdapter(Context context, ArrayList<Item> items) {

        this.context = context;
        this.items = items;

        db = context.openOrCreateDatabase("Data.db", Context.MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS faver(num INTEGER PRIMARY KEY AUTOINCREMENT, image text, title text, addr1 text, tell text, lineintro text, homepage text, intro text, sbrsCl text, contentId text UNIQUE, fav integer)");

    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);

        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        Item item = items.get(position);

        Glide.with(context).load(item.image).into(holder.binding.ivImage);
        holder.binding.tvTitle.setText(item.title);
        holder.binding.tvAddr1.setText(item.addr1);
        holder.binding.tvTel.setText(item.tell);
        holder.binding.tvLineintro.setText(item.lineintro);

        holder.itemView.setOnClickListener(view -> {

            int mPosition = holder.getAdapterPosition();

            Intent detail = new Intent(context, List_Detail.class);

            detail.putExtra("title", items.get(mPosition).title);
            detail.putExtra("address", items.get(mPosition).addr1);
            detail.putExtra("tell", items.get(mPosition).tell);
            detail.putExtra("homepage", items.get(mPosition).homepage);
            detail.putExtra("intro", items.get(mPosition).intro);
            detail.putExtra("sbrsCl", items.get(mPosition).sbrsCl);
            detail.putExtra("contentId", items.get(mPosition).contentId);

            context.startActivity(detail);
        });


        holder.binding.btn.setOnClickListener(view -> {

//            int map = holder.getAdapterPosition();

            ((MainActivity)context).bnv.setSelectedItemId(R.id.menu_bnv_map);
            MapFragment mapFragment = (MapFragment) ((MainActivity)context).fragments.get(2);
            mapFragment.loadMapData(item.title, Double.parseDouble(item.mapX), Double.parseDouble(item.mapY));

//            Intent xy = new Intent(context, MapFragment);
//            xy.putExtra("title", items.get(map).title);
//            xy.putExtra("mapX", items.get(map).mapX);
//            xy.putExtra("mapY", items.get(map).mapY);


//            context.startActivity(xy);
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder =new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.ic_baseline_star_rate_24);
                builder.setMessage("즐겨찾기에 추가하시겠습니까?");

                builder.setNegativeButton("즐겨찾기 추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        saveSQLiteDatabase(item);

                        Toast.makeText(context,"즐겨찾기 추가 완료", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        Toast.makeText(context,"즐겨찾기 취소", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog= builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                return true;
            }
        });


    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    SQLiteDatabase db;

    void saveSQLiteDatabase(Item item){

        String[] values = new String[]{item.image, item.title, item.addr1, item.tell, item.lineintro, item.homepage, item.intro, item.sbrsCl, item.contentId, 1+""};
        db.execSQL("INSERT OR REPLACE INTO faver(image, title, addr1, tell, lineintro, homepage, intro, sbrsCl, contentId, fav) VALUES(?,?,?,?,?,?,?,?,?,?)",values);

    }



    void deleteSQLiteDatabase(){

        String sqlDelete = "DELETE FROM faver WHERE fav = 0" ;

        db.execSQL(sqlDelete) ;
    }

    void update(String title){


        db.execSQL("UPDATE faver SET fav = 0 WHERE title =?", new String[]{title} );

        deleteSQLiteDatabase();

    }


    class VH extends RecyclerView.ViewHolder{

        RecyclerItemBinding binding;

        public VH(@NonNull View itemView) {
            super(itemView);

            binding =RecyclerItemBinding.bind(itemView);

        }
    }
}
