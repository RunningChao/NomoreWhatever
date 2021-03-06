package com.yahoo.android.nomorewhatever.model;

import android.content.Context;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;
import com.yahoo.android.nomorewhatever.common.Helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Table(name = "Places")
public class Place extends Model implements Serializable, Comparator<Place> {
    @Column(name = "place_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long placeId;

    @Column(name = "name")
    public String name;

    @Column(name = "image_name")
    public String imageName;

    @Column(name = "is_fav")
    public boolean isFav;

    @Column(name = "lat")
    public double lat;

    @Column(name = "lng")
    public double lng;

    @Column(name = "zoom")
    public String zoom;

    @Column(name = "desciption")
    public String desciption;

    @Column(name = "photo_url")
    public String photoURL;

    @Column(name = "phone")
    public String phone;

    @Column(name = "place_type", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
    public PlaceType placeType;


    public Double distance;

    public Place() {
    }

    public Place(String name) {
        this.name = name;
    }

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

    public String getPhone() {
    return phone;
  }

    public void setPhone(String phone) {
    this.phone = phone;
  }

    public PlaceType getPlaceType() {
    return placeType;
  }

    public void setPlaceType(PlaceType placeType) {
        this.placeType = placeType;
    }


    public static Place getPlace(long uid) {
        return new Select().from(Place.class).where("place_id=?", uid).executeSingle();
    }

    public static int getPlaceCount(long uid) {
        return new Select().from(Place.class).where("place_id=?", uid).execute().size();
    }

    public static List<Place> getPlacesByType(ArrayList<Long> placeTypes) {
        List<Place> allplaces = new ArrayList<Place>();
        if (placeTypes != null) {
            List<Place> places = new ArrayList<Place>();
            for (int i=0; i<placeTypes.size(); i++) {
                places = new Select().from(Place.class).where("place_type=?",placeTypes.get(i).longValue()).execute();
                allplaces.addAll(places);
            }
        }
        return allplaces;
    }

    public static List<Place> getPlacesByLatAndLng(ArrayList<Long> placeTypes, double east, double wes, double south, double north){
//        double east = curLat + Helper.SCOPE;
//        double wes = curLat - Helper.SCOPE;
//        double south = curlng - Helper.SCOPE;
//        double north = curlng + Helper.SCOPE;

        List<Place> allplaces = new ArrayList<Place>();
        if (placeTypes != null) {

            String sql = "select * from Places where ( lat >= " + wes + " and lat <= " +  east + " )" +
                    " and ( lng >=  " + south + " and lng <= " + north + " ) " +
                    " and place_type in ( "+ listToQueryStr(placeTypes) +" )";

            allplaces =  SQLiteUtils.rawQuery(Place.class, sql, new String[0]);

//
        }
        return allplaces;



    }

    public void calcDistance(double srcLat, double srcLng){
         this.distance = Helper.gps2m(srcLat, srcLng, lat, lng);
    }

    public static String listToQueryStr(ArrayList<Long> ids){
        String id = "";
        for(Long data : ids){
            id += ","+data;
        }
        return id.substring(1);
    }

    public Double getDistance() {
        return distance;
    }


    @Override
    public int compare(Place p1, Place p2) {
       return   p1.getDistance().compareTo(p2.getDistance());
    }
}
