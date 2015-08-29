package com.yahoo.android.nomorewhatever.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.yahoo.android.nomorewhatever.R;
import com.yahoo.android.nomorewhatever.adapter.PlacesAdapter;
import com.yahoo.android.nomorewhatever.model.Place;

import java.util.ArrayList;
import java.util.List;


public class PlacesActivity extends Activity implements LocationListener {

    private boolean isListView;
    private Menu menu;

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;

    private PlacesAdapter mAdapter;

    private List<Place> mPlaces;
    private ArrayList<Long> mPlaceTypeIds;
    private Button mLuckyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        /* gps testing*/
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        /* gps testing*/

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);

        Intent intent = getIntent();
        mPlaceTypeIds = (ArrayList<Long>) intent.getSerializableExtra("selected_places");

        for (Long placeTypeLongId : mPlaceTypeIds) {
            Log.d("Debug", "id: " + placeTypeLongId);
        }

        mPlaces = Place.getPlacesByType(mPlaceTypeIds);

        for (int i = 0; i < mPlaces.size(); i++) {
            mPlaces.get(i).setIsFav(false);
            mPlaces.get(i).save();
        }
        mAdapter = new PlacesAdapter(mPlaces, this);
        mRecyclerView.setAdapter(mAdapter);
        isListView = true;


        mLuckyButton=(Button)findViewById(R.id.btn_lucky);
        mLuckyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Place> selectedPlacesIds = new ArrayList<>();
                for (int i = 0; i < mPlaces.size(); i++) {
                    if (mPlaces.get(i).isFav()) {
                        selectedPlacesIds.add(mPlaces.get(i));

                    }
                }
                Log.e("size", "size" + selectedPlacesIds.size());
                Intent intent = new Intent(PlacesActivity.this, LuckyBoardActivity.class);
                intent.putExtra("selected_places", selectedPlacesIds);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_places, menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_toggle) {
            toggle();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggle() {
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (isListView) {
            mStaggeredLayoutManager.setSpanCount(2);
            item.setIcon(R.drawable.ic_action_list);
            item.setTitle("Show as list");
            isListView = false;
        } else {
            mStaggeredLayoutManager.setSpanCount(1);
            item.setIcon(R.drawable.ic_action_grid);
            item.setTitle("Show as grid");
            isListView = true;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        int latitude = (int) (location.getLatitude());
        int longitude = (int) (location.getLongitude());

        Log.i("Geo_Location", "Latitude: " + latitude + ", Longitude: " + longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

        Log.d("Latitude", "status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }
}
