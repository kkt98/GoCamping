package com.kkt1019.gocamping;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;

public class ListFragment extends Fragment {

    String apiKey = "H7PvoIiO2D6%2BqVfe6kF2WAoJgdpbVUtJT52Wx7dL6%2BDLP4IEk5i5xqP%2BGZMDktix9xaYS03X6YP4JtLGSnuunw%3D%3D";

    ArrayList<Item> items = new ArrayList<>();
    ArrayList<Item> allitems = new ArrayList<>();
    RecyclerView recyclerView;
    ListAdapter adapter;
    Spinner spinner;
    ArrayAdapter spinnerAdapter;

    ProgressBar pb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(){
            @Override
            public void run() {
                loadData();
            }
        }.start();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("캠핑캠핑캠핑");

        setHasOptionsMenu(true);



        return inflater.inflate(R.layout.fragment_list, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = view.findViewById(R.id.recycler);
        adapter = new ListAdapter(getActivity(), items);
        recyclerView.setAdapter(adapter);
        pb = view.findViewById(R.id.pro);

        spinner = getActivity().findViewById(R.id.spinner);
        spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.city, R.layout.list_item_spinner);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                items.clear();

                if (i == 0){

                    items.addAll(allitems);
                    adapter.notifyDataSetChanged();
                    return;
                }

                String[] arr = getResources().getStringArray(R.array.city);

                String[] arr2 = getResources().getStringArray(R.array.city2);

                for (Item item : allitems){
                    Log.i("aaa", item.addr1);
                    if (item.addr1.contains(arr[i]) || item.addr1.contains(arr2[i]) ){

                        items.add(item);

                    }
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.list_search, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Toast.makeText(getActivity(), ""+query, Toast.LENGTH_SHORT).show();
                loadDataBySearch(query);

                searchView.setQuery("", true);
                //혹시 곧바로 아이콘 모양으로 돌아가고 싶다면
                searchView.setIconified(true);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;

            }
        });
        super.onCreateOptionsMenu(menu, inflater);

    }

    void loadDataBySearch(String search){

        items.clear();
        adapter.notifyDataSetChanged();

        new Thread(){

            @Override
            public void run() {

                String name = "";

                String appname = "캠핑캠핑캠핑";
                try {
                    appname = URLEncoder.encode("켐핑캠핑캠핑", "UTF-8");
                    name = URLEncoder.encode(search, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String address = "http://api.visitkorea.or.kr/openapi/service/rest/GoCamping/searchList"
                        + "?serviceKey=" + apiKey
                        + "&pageNo=1&numOfRows=30"
                        + "&MobileOS=AND"
                        + "&MobileApp=" + appname
                        + "&keyword=" + name;

                try {
                    URL url = new URL(address);

                    InputStream is = url.openStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(isr);

                    int eventType = xpp.getEventType();

                    Item item = null;

                    while (eventType != XmlPullParser.END_DOCUMENT){

                        switch (eventType){
                            case XmlPullParser.START_DOCUMENT:
                                break;

                            case XmlPullParser.START_TAG:
                                String tagName = xpp.getName();
                                if (tagName.equals("item")){
                                    item = new Item();

                                }else if (tagName.equals("firstImageUrl")){
                                    xpp.next();
                                    if (item != null) item.image = xpp.getText();

                                }else if (tagName.equals("facltNm")){
                                    xpp.next();
                                    if (item != null) item.title = xpp.getText();

                                }else if (tagName.equals("addr1")){
                                    xpp.next();
                                    if (item != null) item.addr1 = xpp.getText();

                                }else if (tagName.equals("tel")){
                                    xpp.next();
                                    if (item != null) item.tell = xpp.getText();

                                }else if (tagName.equals("lineIntro")){
                                    xpp.next();
                                    if (item != null) item.lineintro = xpp.getText();

                                }else if (tagName.equals("homepage")){
                                    xpp.next();
                                    if (item != null) item.homepage = xpp.getText();

                                }else if (tagName.equals("sbrsCl")){
                                    xpp.next();
                                    if (item != null) item.sbrsCl = xpp.getText();

                                }else if (tagName.equals("intro")){
                                    xpp.next();
                                    if (item != null) item.intro = xpp.getText();

                                }else if (tagName.equals("contentId")){
                                    xpp.next();
                                    if (item != null) item.contentId = xpp.getText();

                                }else if (tagName.equals("mapX")){
                                    xpp.next();
                                    if (item != null) item.mapX = xpp.getText();

                                }else if (tagName.equals("mapY")){
                                    xpp.next();
                                    if (item != null) item.mapY = xpp.getText();

                                }

                                break;

                            case XmlPullParser.END_TAG:
                                String tagName2 = xpp.getName();
                                if (tagName2.equals("item") ){
                                    items.add(item);
                                }
                                break;

                        }
                        eventType = xpp.next();

                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            pb.setVisibility(View.GONE);
                        }
                    });


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

            }
        }.start();


    }

    void loadData(){

        String appname = "캠핑캠핑캠핑";
        try {
            appname = URLEncoder.encode("캠핑캠핑캠핑", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String address = "http://api.visitkorea.or.kr/openapi/service/rest/GoCamping/basedList"
                + "?serviceKey=" + apiKey
                + "&pageNo=1&numOfRows=1000"
                + "&MobileOS=AND"
                + "&MobileApp=" + appname;

        try {
            URL url = new URL(address);

            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(isr);

            int eventType = xpp.getEventType();

            Item item = null;

            while (eventType != XmlPullParser.END_DOCUMENT){

                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        String tagName = xpp.getName();
                        if (tagName.equals("item")){
                            item = new Item();

                        }else if (tagName.equals("firstImageUrl")){
                            xpp.next();
                            if (item != null) item.image = xpp.getText();

                        }else if (tagName.equals("facltNm")){
                            xpp.next();
                            if (item != null) item.title = xpp.getText();

                        }else if (tagName.equals("addr1")){
                            xpp.next();
                            if (item != null) item.addr1 = xpp.getText();

                        }else if (tagName.equals("tel")){
                            xpp.next();
                            if (item != null) item.tell = xpp.getText();

                        }else if (tagName.equals("lineIntro")){
                            xpp.next();
                            if (item != null) item.lineintro = xpp.getText();

                        }else if (tagName.equals("homepage")){
                            xpp.next();
                            if (item != null) item.homepage = xpp.getText();

                        }else if (tagName.equals("sbrsCl")){
                            xpp.next();
                            if (item != null) item.sbrsCl = xpp.getText();

                        }else if (tagName.equals("intro")){
                            xpp.next();
                            if (item != null) item.intro = xpp.getText();

                        }else if (tagName.equals("contentId")){
                            xpp.next();
                            if (item != null) item.contentId = xpp.getText();

                        }else if (tagName.equals("mapX")){
                            xpp.next();
                            if (item != null) item.mapX = xpp.getText();

                        }else if (tagName.equals("mapY")){
                            xpp.next();
                            if (item != null) item.mapY = xpp.getText();

                        }
                        break;

                    case XmlPullParser.END_TAG:
                        String tagName2 = xpp.getName();
                        if (tagName2.equals("item")){
                            allitems.add(item);
                        }
                        break;

                }
                eventType = xpp.next();

            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    items.addAll(allitems);
                    adapter.notifyDataSetChanged();
                    pb.setVisibility(View.GONE);
                }
            });


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }




}

