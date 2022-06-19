package com.kkt1019.gocamping;

import static android.content.Context.MODE_PRIVATE;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.kkt1019.gocamping.databinding.FragmetMarkBinding;

import java.util.ArrayList;

public class MarkFragment extends Fragment {

    ArrayList<Item> items = new ArrayList<>();

    RecyclerView recyclerView;
    MarkListAdapter adapter;

    SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("캠핑캠핑캠핑");

        View view = inflater.inflate(R.layout.fragmet_mark, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        recyclerView = view.findViewById(R.id.recycler);
        adapter = new MarkListAdapter(getActivity(), items);
        recyclerView.setAdapter(adapter);
        loadDB();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){

           loadDB();

        }


    }

    void loadDB(){

        items.clear();

        db= getActivity().openOrCreateDatabase("Data.db", MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("SELECT * FROM faver", null);
        int rowCnt= cursor.getCount();
        cursor.moveToFirst();

        for (int i=0; i<rowCnt; i++){

            int num =cursor.getInt(0);
            String image = cursor.getString(1);
            String title = cursor.getString(2);
            String addr1 = cursor.getString(3);
            String tell = cursor.getString(4);
            String lineintro = cursor.getString(5);
            String homepage = cursor.getString(6);
            String intro = cursor.getString(7);
            String sbrsCl = cursor.getString(8);
            String contantId = cursor.getString(9);
            int fav = cursor.getInt(10);



            Item item = new Item(image, title, addr1, tell, lineintro, homepage, intro, sbrsCl, contantId, null, fav);

            items.add(0, item);

            cursor.moveToNext();

        }
        adapter.notifyDataSetChanged();
    }



}



