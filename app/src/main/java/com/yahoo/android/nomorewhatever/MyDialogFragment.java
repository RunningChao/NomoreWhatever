package com.yahoo.android.nomorewhatever;

import android.app.ActivityOptions;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yahoo.android.nomorewhatever.activity.LuckyBoardActivity;
import com.yahoo.android.nomorewhatever.activity.PlaceDetailActivity;

/**
 * Created by mchsieh on 8/31/15.
 */
public class MyDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sample_dialog, container, false);
        ImageView iv_place = (ImageView) view.findViewById(R.id.iv_place);
        TextView tv_title = (TextView)view.findViewById(R.id.title);
        Button btn_tryagain = (Button) view.findViewById(R.id.btn_tryagain);
        Button btn_showdetail = (Button) view.findViewById(R.id.btn_showdetail);

        // set dialog title
        getDialog().setTitle("Your Lucky Place Today:");
        // set restaurant name
        tv_title.setText((CharSequence) LuckyBoardActivity.lucky_place_today.getName());
        // set restaruant image
        iv_place.setImageResource(LuckyBoardActivity.lucky_place_today.getImageResourceId(getActivity()));
        // set try again button
        btn_tryagain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        // set show detail page
        btn_showdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlaceDetailActivity.class);
                intent.putExtra("place_id", LuckyBoardActivity.lucky_place_today.getId());

                intent.putExtra(PlaceDetailActivity.EXTRA_PARAM_ID,  LuckyBoardActivity.lucky_place_today);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), v, "photo_hero");
                (getActivity()).startActivity(intent, options.toBundle());

                //startActivityForResult(intent, 20);
            }
        });
        return view;
    }


}