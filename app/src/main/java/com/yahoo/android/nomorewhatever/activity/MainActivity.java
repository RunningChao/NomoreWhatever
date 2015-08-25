package com.yahoo.android.nomorewhatever.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.yahoo.android.nomorewhatever.R;
import com.yahoo.android.nomorewhatever.adapter.PlaceTypeListAdapter;


public class MainActivity extends Activity {

  private Menu menu;
  private boolean isListView;

  private RecyclerView mRecyclerView;
  private StaggeredGridLayoutManager mStaggeredLayoutManager;

  private PlaceTypeListAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mRecyclerView = (RecyclerView) findViewById(R.id.list);
    mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    mRecyclerView.setLayoutManager(mStaggeredLayoutManager);

    mAdapter = new PlaceTypeListAdapter(this);
    mRecyclerView.setAdapter(mAdapter);

    mAdapter.setOnItemClickListener(new PlaceTypeListAdapter.OnItemClickListener() {

      @Override
      public void onItemClick(View view, int position) {
        Toast.makeText(MainActivity.this, "Clicked " + position, Toast.LENGTH_SHORT).show();




        Intent intent = new Intent(MainActivity.this, PlaceDetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_PARAM_ID, position);

        ActivityOptions options =
                ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, view, "photo_hero");
        startActivity(intent, options.toBundle());
      }
    });

    isListView = true;
  }

  public void showPhoto(View view) {
    Toast.makeText(MainActivity.this, "Clicked " + view.getId(), Toast.LENGTH_SHORT).show();
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
}
