package com.kkt1019.gocamping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.kkt1019.gocamping.databinding.ActivityListDetailBinding;

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

public class List_Detail extends AppCompatActivity {

    ActivityListDetailBinding binding;

    String title, address, homepage, intro, tell, sbrsCl, contentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_list_detail);

        this.setTitle("상세정보");
        binding = ActivityListDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        title = intent.getExtras().getString("title");
        address = intent.getExtras().getString("address");
        tell = intent.getExtras().getString("tell");
        homepage = intent.getExtras().getString("homepage");
        intro = intent.getExtras().getString("intro");
        sbrsCl = intent.getExtras().getString("sbrsCl");
        contentId = intent.getExtras().getString("contentId");


        binding.title.setText(title);
        binding.address.setText("주소 : " + address);
        binding.tell.setText("전화번호 : " + tell);
        binding.homepage.setText("홈페이지 : " + homepage);
        binding.intro.setText(intro);
        binding.sbrsCl.setText("부대정보 : " + sbrsCl);



        imageload();

    }

    ArrayList<Item> items = new ArrayList<>();
    ListDetailImageAdapter adapter;

    String apiKey = "H7PvoIiO2D6%2BqVfe6kF2WAoJgdpbVUtJT52Wx7dL6%2BDLP4IEk5i5xqP%2BGZMDktix9xaYS03X6YP4JtLGSnuunw%3D%3D";

    void imageload(){


        adapter = new ListDetailImageAdapter(this, items);
        binding.pager.setAdapter(adapter);

        new Thread(){

            @Override
            public void run() {
                String appname = "캠핑캠핑캠핑";
                try {
                    appname = URLEncoder.encode("켐핑캠핑캠핑", "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String address = "http://api.visitkorea.or.kr/openapi/service/rest/GoCamping/imageList"
                        + "?serviceKey=" + apiKey
                        + "&MobileOS=AND"
                        + "&MobileApp=" + appname
                        + "&contentId=" + contentId;

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

                                }else if (tagName.equals("imageUrl")){
                                    xpp.next();
                                    if (item != null) item.imageUrl = xpp.getText();

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

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();

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
}