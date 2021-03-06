package com.yahoo.android.nomorewhatever.model;

import java.util.ArrayList;

public class PlaceData {

    public static String[] placeNameArray = {"BBQ", "HotPot", "Italian", "Italian2", "Iceland", "India", "Kenya", "London", "Switzerland", "Sydney"};

    public static ArrayList<Place> placeList() {
        ArrayList<Place> list = new ArrayList<>();
        for (int i = 0; i < placeNameArray.length; i++) {
            Place place = new Place(placeNameArray[i]);
            place.name = placeNameArray[i];
            place.imageName = placeNameArray[i].replaceAll("\\s+", "").toLowerCase();
            if (i == 2 || i == 5) {
                place.setIsFav(true);
            }

            list.add(place);
        }
        return (list);
    }


}
