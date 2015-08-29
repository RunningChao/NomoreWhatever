package com.yahoo.android.nomorewhatever.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yahoo.android.nomorewhatever.R;
import com.yahoo.android.nomorewhatever.model.Place;
import com.yahoo.android.nomorewhatever.ui.LuckyPanView;

import java.util.List;

public class LuckyBoardActivity extends Activity {

    private Button mStartBtn;
    private LuckyPanView mLuckyPanView;

    private List<Place> mPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_board);

        Intent intent = getIntent();
        mPlaces = (List<Place>) intent.getSerializableExtra("selected_places");

        mLuckyPanView = (LuckyPanView) findViewById(R.id.id_luckypan);
        mLuckyPanView.setPlacesonBoard(mPlaces);


        mLuckyPanView.setListener(new LuckyPanView.onClickLuckyPanListener() {
            @Override
            public void onClickLuckyPan(int luckyId) {
                Toast.makeText(LuckyBoardActivity.this, "click item with id " + luckyId, Toast.LENGTH_SHORT).show();
            }
        });

        mStartBtn = (Button) findViewById(R.id.id_start_btn);
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mLuckyPanView.isStart()) {
                    // TODO :change icon layout here
                    // mStartBtn.setImageResource(R.drawable.stop);
                    mLuckyPanView.luckyStart((int) Math.floor(Math.random() * 6));
                } else {
                    if (!mLuckyPanView.isShouldEnd()) {
                        // mStartBtn.setImageResource(R.drawable.start);
                        mLuckyPanView.luckyEnd();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
