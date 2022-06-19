package com.kkt1019.gocamping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bnv;
    FragmentManager fragmentManager;
    ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase db = openOrCreateDatabase("Data.db", Context.MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS faver(num INTEGER PRIMARY KEY AUTOINCREMENT, image text, title text, addr1 text, tell text, lineintro text, homepage text, intro text, sbrsCl text, contentId text, fav integer)");

        //동적퍼미션
        String[] permissions= new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        int checkResult= checkSelfPermission(permissions[0]);
        if(checkResult == PackageManager.PERMISSION_DENIED){
            requestPermissions(permissions, 10);
        }

        fragmentManager = getSupportFragmentManager();

        fragments.add( new ListFragment()  );
        fragments.add( null );
        fragments.add( new MapFragment() );

        fragmentManager.beginTransaction().add(R.id.container, fragments.get(0)).commit();
        fragmentManager.beginTransaction().add(R.id.container, fragments.get(2)).commit();
        fragmentManager.beginTransaction().hide(fragments.get(2)).commit();

        bnv = findViewById(R.id.bnv);
        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentTransaction tran= fragmentManager.beginTransaction();

                if(fragments.get(0)!=null) tran.hide(fragments.get(0));
                if(fragments.get(1)!=null) tran.hide(fragments.get(1));
                if(fragments.get(2)!=null) tran.hide(fragments.get(2));

                switch ( item.getItemId() ){
                    case R.id.menu_bnv_list:
                        tran.show(fragments.get(0));
                        break;

                    case R.id.menu_bnv_mark:
                        if(fragments.get(1)==null){
                            fragments.set(1, new MarkFragment());
                            tran.add( R.id.container, fragments.get(1) );
                        }
                        tran.show(fragments.get(1));
                        break;

                    case R.id.menu_bnv_map:
                        tran.show(fragments.get(2));
                        break;
                }
                tran.commit();


                return true;
            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==10){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "위치정보 사용 가능", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "위치정보 사용 불가", Toast.LENGTH_SHORT).show();
            }
        }

    }

//    void showFragment(int position){
//
//        FragmentTransaction tran = fragmentManager.beginTransaction();
//
//        if(fragments.get(0)!=null) tran.hide(fragments.get(0));
//        if(fragments.get(1)!=null) tran.hide(fragments.get(1));
//        if(fragments.get(2)!=null) tran.hide(fragments.get(2));
//
//        tran.show(fragments.get(position));
//
//        tran.commit();
//
//    }

}