package com.yahoo.android.nomorewhatever.model;

import android.content.Context;

public class Place {

  public long placeId;
  public String name;
  public String imageName;
  public boolean isFav;
  public double lat;
  public double lng;
  public String zoom;
  public String desciption;
  public String photoURL;



  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImageName() {
    return imageName;
  }

  public void setImageName(String imageName) {
    this.imageName = imageName;
  }

  public boolean isFav() {
    return isFav;
  }

  public void setIsFav(boolean isFav) {
    this.isFav = isFav;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLng() {
    return lng;
  }

  public void setLng(double lng) {
    this.lng = lng;
  }

  public String getZoom() {
    return zoom;
  }

  public void setZoom(String zoom) {
    this.zoom = zoom;
  }

  public String getDesciption() {
    return desciption;
  }

  public void setDesciption(String desciption) {
    this.desciption = desciption;
  }

  public String getPhotoURL() {
    return photoURL;
  }

  public void setPhotoURL(String photoURL) {
    this.photoURL = photoURL;
  }

  public int getImageResourceId(Context context) {
    return context.getResources().getIdentifier(this.imageName, "drawable", context.getPackageName());
  }
}
