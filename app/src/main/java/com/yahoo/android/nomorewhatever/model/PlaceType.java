package com.yahoo.android.nomorewhatever.model;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by andychw on 8/23/15.
 */
public class PlaceType implements Serializable{
    public long uid;
    public String name;
    public String imageName;
    public String desciption;
    public String photoURL;
    public boolean isFav;

    public int getImageResourceId(Context context) {
        return context.getResources().getIdentifier(this.imageName, "drawable", context.getPackageName());
    }
}
