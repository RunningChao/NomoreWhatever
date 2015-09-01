package com.yahoo.android.nomorewhatever.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.yahoo.android.nomorewhatever.MyDialogFragment;
import com.yahoo.android.nomorewhatever.R;
import com.yahoo.android.nomorewhatever.model.Place;
import com.yahoo.android.nomorewhatever.ui.LuckyPanView;

import java.util.List;

public class LuckyBoardActivity extends Activity {

    private Button mStartBtn;
    private LuckyPanView mLuckyPanView;

    private List<Place> mPlaces;

    public static Place lucky_place_today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_board);

        Intent intent = getIntent();
        mPlaces = (List<Place>) intent.getSerializableExtra("selected_places");
Log.d("Debug", "id:" + mPlaces.get(0).getId());
        mLuckyPanView = (LuckyPanView) findViewById(R.id.id_luckypan);
        mLuckyPanView.setPlacesonBoard(mPlaces);


        mLuckyPanView.setListener(new LuckyPanView.onClickLuckyPanListener() {
            @Override
            public void onClickLuckyPan(int luckyId) {
                //Toast.makeText(LuckyBoardActivity.this, "click item with id " + luckyId, Toast.LENGTH_SHORT).show();
            }
        });

        mStartBtn = (Button) findViewById(R.id.id_start_btn);
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int lucky_number=0;
                if (!mLuckyPanView.isStart()) { // click run button
                    // pick a lucky number
                    lucky_number = (int) Math.floor(Math.random() * 6);
                    // set lucky place
                    lucky_place_today = mPlaces.get(LuckyPanView.placeInBoard[lucky_number]);
                    mLuckyPanView.luckyStart(lucky_number);
                    // set button to stop
                    mStartBtn.setText("Stop!");
                } else {                        // click stop button
                    // stop lucky pan
                    if (!mLuckyPanView.isShouldEnd()) {
                        // mStartBtn.setImageResource(R.drawable.start);
                        mLuckyPanView.luckyEnd();
                    }

                    // wait lucky pan animation stop
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // show dialogFragment
                    FragmentManager fm = getFragmentManager();
                    MyDialogFragment dialogFragment = new MyDialogFragment();
                    dialogFragment.show(fm, String.valueOf(lucky_number));

                    mStartBtn.setText("NOMORE WHATEVER!");
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lucky_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
