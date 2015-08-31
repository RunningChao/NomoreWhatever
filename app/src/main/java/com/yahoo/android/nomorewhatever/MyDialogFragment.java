package com.yahoo.android.nomorewhatever;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by mchsieh on 8/31/15.
 */
public class MyDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sample_dialog, container, false);
        ImageView iv_place = (ImageView) view.findViewById(R.id.iv_place);
        Button btn_tryagain = (Button) view.findViewById(R.id.btn_tryagain);
        Button btn_showdetail = (Button) view.findViewById(R.id.btn_showdetail);

        // set title
        getDialog().setTitle("Your Lucky Place Today:");
        // set try again button
        btn_tryagain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;
    }


}