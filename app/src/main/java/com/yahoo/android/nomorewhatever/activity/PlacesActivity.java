package com.yahoo.android.nomorewhatever.activity;

import android.app.Activity;
import android.app.ActivityOptions;
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
import android.widget.Toast;

import com.yahoo.android.nomorewhatever.R;
import com.yahoo.android.nomorewhatever.adapter.PlacesAdapter;
import com.yahoo.android.nomorewhatever.model.Place;
import com.yahoo.android.nomorewhatever.model.PlaceData;

import java.util.List;


public class PlacesActivity extends Activity implements LocationListener {

    private boolean isListView;
    private Menu menu;

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;

    private PlacesAdapter mAdapter;

    private List<Place> mPlaces;

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

//        Place p = new Place();
//        p.setName("TAMAMA");
//        p.setImageName("borabora");
//        p.setIsFav(false);
//        p.save();
//        List<Place> list = new LinkedList<>();
//        list.add(p);
        mPlaces = PlaceData.placeList();
        //mPlaces = Place.getPlace(1); //mock
        for (int i = 0; i < mPlaces.size(); i++) {
            mPlaces.get(i).setIsFav(false);
            //mPlaces.get(i).save();
        }
        mAdapter = new PlacesAdapter(mPlaces, this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new PlacesAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(PlacesActivity.this, "Clicked " + position, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(PlacesActivity.this, PlaceDetailActivity.class);
                intent.putExtra(PlaceDetailActivity.EXTRA_PARAM_ID, position);

                ActivityOptions options =
                        ActivityOptions.makeSceneTransitionAnimation(PlacesActivity.this, view, "photo_hero");
                startActivity(intent, options.toBundle());
            }
        });

        isListView = true;
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

        Log.d("Latitude","status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }
}
