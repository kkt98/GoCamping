package com.kkt1019.gocamping;

public class Item {

    String image;
    String title;
    String addr1;
    String tell;
    String lineintro;
    String homepage;
    String intro;
    String sbrsCl;
    String contentId;
    String imageUrl;
    int fav;
    String mapX;
    String mapY;


    public Item(String image, String title, String addr1, String tell, String lineintro, String homepage, String intro, String sbrsCl, String contentId, String imageUrl, int fav) {
        this.image = image;
        this.title = title;
        this.addr1 = addr1;
        this.tell = tell;
        this.lineintro = lineintro;
        this.homepage = homepage;
        this.intro = intro;
        this.sbrsCl = sbrsCl;
        this.contentId = contentId;
        this.imageUrl = imageUrl;
        this.fav = fav;
        this.mapX= mapX;
        this.mapY = mapY;
    }

    public Item() {
    }

    public String title() {
        return title;
    }
}
