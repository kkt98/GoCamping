package com.kkt1019.gocamping;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment {

    FusedLocationProviderClient providerClient;

//    String apiKey = "H7PvoIiO2D6%2BqVfe6kF2WAoJgdpbVUtJT52Wx7dL6%2BDLP4IEk5i5xqP%2BGZMDktix9xaYS03X6YP4JtLGSnuunw%3D%3D";

    String title;
    Double mapX, mapY;

    EditText editText;


    @Override
    public void onResume() {
        super.onResume();

        editText = getActivity().findViewById(R.id.editText);

        //위치정보 제공자 객체얻어오기
        providerClient = LocationServices.getFusedLocationProviderClient(getActivity());

        //위치 정보 요청 객체 생성 및 설정
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//높은 정확도 우선시..[gps]
        locationRequest.setInterval(5000);//5000ms[5초]간격으로 갱신

        //내 위치 실시간 갱신 요청
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        providerClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        getActivity().findViewById(R.id.button2).setOnClickListener(v->aaa());
    }

    @Override
    public void onPause() {
        super.onPause();

        providerClient.removeLocationUpdates(locationCallback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("캠핑캠핑캠핑");

//        Intent intent = getActivity().getIntent();
//
//        title = intent.getExtras().getString("title");
//        mapX = intent.getExtras().getDouble("mapX");
//        mapY = intent.getExtras().getDouble("mapY");

        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    GoogleMap mGoogleMap;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //xml에 만들 지도프레그먼트 찾아오기
        FragmentManager fragmentManager= getChildFragmentManager();
        SupportMapFragment mapFragment= (SupportMapFragment)fragmentManager.findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                LatLng seoul = new LatLng(37.5663, 126.9779);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 16)); //줌 1~25

                mGoogleMap= googleMap;

                UiSettings settings = googleMap.getUiSettings();
                settings.setZoomControlsEnabled(true);


                settings.setMyLocationButtonEnabled(true);

                //내 위치 표시하기
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);


            }
        });



    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            Location location = locationResult.getLastLocation();


        }
    };

    void loadMapData(String title, double mapX, double mapY){

        LatLng position = new LatLng(mapY, mapX);
        Toast.makeText(getActivity(), ""+mapX + ",   " + mapY, Toast.LENGTH_SHORT).show();
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

        MarkerOptions marker = new MarkerOptions();
        marker.title(title);
//        marker.snippet("왕십리역에 있는 미래IT캠퍼스");
        marker.position(position);
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name));//벡터이미지는 안됨. .png or .jpg만 됨
        marker.anchor(0.5f, 1.0f); //x축, y축

        mGoogleMap.addMarker(marker);

    }

    double X;
    double Y;

    void aaa(){

        // 주소 -> 좌표 (Geocoding : 지오코딩)

        String addr = editText.getText().toString();

        //지오코딩 작업을 수행하는 객체 생성

        Geocoder geocoder = new Geocoder(getActivity(), Locale.KOREA);

        try {
            List<Address> addressList = geocoder.getFromLocationName(addr, 3); //최대 3개

            StringBuffer buffer = new StringBuffer();
            for(Address address : addressList){
                double lat = address.getLatitude(); //위도
                double lng = address.getLongitude(); //경도

                buffer.append(lat+" , "+lng+"\n");
            }

            X = addressList.get(0).getLatitude();
            Y = addressList.get(0).getLongitude();

            new AlertDialog.Builder(getActivity()).setMessage(buffer.toString()).create().show();

        } catch (IOException e) {
            e.printStackTrace();
        }

        LatLng position = new LatLng(X, Y);
        Toast.makeText(getActivity(), ""+X + ",   " + Y, Toast.LENGTH_SHORT).show();
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

        MarkerOptions marker = new MarkerOptions();
        marker.title(addr);
//        marker.snippet("왕십리역에 있는 미래IT캠퍼스");
        marker.position(position);
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name));//벡터이미지는 안됨. .png or .jpg만 됨
        marker.anchor(0.5f, 1.0f); //x축, y축

        mGoogleMap.addMarker(marker);

    }

}
