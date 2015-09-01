package com.yahoo.android.nomorewhatever.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.yahoo.android.nomorewhatever.R;
import com.yahoo.android.nomorewhatever.adapter.PlaceTypeListAdapter;
import com.yahoo.android.nomorewhatever.model.PlaceType;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private Menu menu;
    private boolean isListView;

    private RecyclerView mRecyclerView;
    private Button mConfirmBtn;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;

    private PlaceTypeListAdapter mAdapter;

    private List<PlaceType> mPlaceTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mConfirmBtn = (Button) findViewById(R.id.btn_confirm);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);

        mPlaceTypes = PlaceType.getPlaceTypes(20);
        for (int i = 0; i < mPlaceTypes.size(); i++) {
            mPlaceTypes.get(i).setIsFav(false);
            mPlaceTypes.get(i).save();
        }
        mAdapter = new PlaceTypeListAdapter(mPlaceTypes, this);
        mRecyclerView.setAdapter(mAdapter);

        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Long> selectedPlacesIds = new ArrayList<>();
                for (int i = 0; i < mPlaceTypes.size(); i++) {
                    if (mPlaceTypes.get(i).isFav()) {
                        selectedPlacesIds.add(mPlaceTypes.get(i).getId());

                    }
                }
                //Log.e("size", "size" + selectedPlaces.size());

                Intent intent = new Intent(MainActivity.this, PlacesActivity.class);
                intent.putExtra("selected_places", selectedPlacesIds);
                startActivity(intent);
            }
        });
        isListView = false;
    }

    public void showPhoto(View view) {
        //Toast.makeText(MainActivity.this, "Clicked " + view.getId(), Toast.LENGTH_SHORT).show();

//        ArrayList<Long> selectedPlacesIds = new ArrayList<>();
//        selectedPlacesIds.add(mPlaces.get(0).getId());
//        Intent intent = new Intent(MainActivity.this, PlacesActivity.class);
//        intent.putExtra("selected_places", selectedPlacesIds);
//        startActivity(intent);
    }


    private void setUpActionBar() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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
            item.setIcon(R.drawable.list);
            item.setTitle("Show as list");
            isListView = false;
        } else {
            mStaggeredLayoutManager.setSpanCount(1);
            item.setIcon(R.drawable.grid);
            item.setTitle("Show as grid");
            isListView = true;
        }
    }
}
