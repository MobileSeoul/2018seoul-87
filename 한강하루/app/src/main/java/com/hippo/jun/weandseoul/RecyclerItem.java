package com.hippo.jun.weandseoul;

import android.graphics.Bitmap;

/**
 * Created by JUN on 2018-08-03.
 */

public class RecyclerItem {
    int image;
    Bitmap bitmapImage;
    String title;
    String url;
    String parkName;
    String name;
    String text;
    String id;
    String imageSource;
    String imgFileName;
    String deleteNum;

    String mName, mContent, mTel, mDate, mPlace, mNumber, mPosition;
    String bName, bFee, bInfo, bTel, bBicycle, bAddr;
    double bXcode, bYcode;

    int getImage(){ return this.image; }
    Bitmap getBitmapImage() { return this.bitmapImage; }
    String getTitle(){
        return this.title;
    }

    public RecyclerItem(String url, String name){
        this.url=url;
        this.name=name;
    }
    public RecyclerItem(String imageSource, String imgFileName, String id, String text){
        this.deleteNum=deleteNum;
        this.imgFileName=imgFileName;
        this.imageSource=imageSource;
        this.id=id;
        this.text=text;
    }
    public RecyclerItem(String mName, String mContent, String mTel, String mDate, String mPlace, String mNumber, String mPosition){
        this.mName=mName;
        this.mContent=mContent;
        this.mTel=mTel;
        this.mDate=mDate;
        this.mPlace=mPlace;
        this.mNumber=mNumber;
        this.mPosition=mPosition;
    }
    public RecyclerItem(String bName, String bFee, String bInfo, String bTel, String bBicycle, String bAddr, double bXcode, double bYcode){
        this.bName=bName;
        this.bFee=bFee;
        this.bInfo=bInfo;
        this.bTel=bTel;
        this.bBicycle=bBicycle;
        this.bAddr=bAddr;
        this.bXcode=bXcode;
        this.bYcode=bYcode;
    }
    public RecyclerItem(Bitmap bitmapImage, String title){
        this.bitmapImage=bitmapImage;
        this.title=title;
        this.image=0;
    }
}