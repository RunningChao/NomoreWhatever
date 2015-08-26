package com.yahoo.android.nomorewhatever.model;

import android.content.Context;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.List;

/**
 * Created by andychw on 8/23/15.
 */
@Table(name = "Placetypes")
public class PlaceType extends Model implements Serializable{
    @Column(name = "name", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String name;

    @Column(name = "imageName")
    public String imageName;

    @Column(name = "description")
    public String desciption;

    @Column(name = "photoURL")
    public String photoURL;

    @Column(name = "isFav")
    public boolean isFav;

    public PlaceType() {

    }

    public PlaceType(String name) {
        this.name = name;
    }


    public int getImageResourceId(Context context) {
        return context.getResources().getIdentifier(this.imageName, "drawable", context.getPackageName());
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

    public boolean isFav() {
        return isFav;
    }

    public void setIsFav(boolean isFav) {
        this.isFav = isFav;
    }

    public static List<PlaceType> getPlaceTypes(int retrieveNumbers) {
        From sql = new Select()
                .from(PlaceType.class)
                .limit(retrieveNumbers);
        return sql.execute();
    }

}
