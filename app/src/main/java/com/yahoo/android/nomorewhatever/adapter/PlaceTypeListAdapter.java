package com.yahoo.android.nomorewhatever.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yahoo.android.nomorewhatever.R;
import com.yahoo.android.nomorewhatever.activity.PlacesActivity;
import com.yahoo.android.nomorewhatever.model.PlaceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andychw on 8/23/15.
 */
public class PlaceTypeListAdapter extends RecyclerView.Adapter<PlaceTypeListAdapter.ViewHolder> {

    Context mContext;
    OnItemClickListener mItemClickListener;
    private List<PlaceType> mPlaceTypes;

    public PlaceTypeListAdapter(List<PlaceType> mPlaceTypes, Context mContext) {
        this.mPlaceTypes = mPlaceTypes;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_places, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PlaceType place = mPlaceTypes.get(position);

        holder.placeName.setText(place.name);
        holder.mSelectedIv.setVisibility(mPlaceTypes.get(position).isFav() ? View.VISIBLE : View.GONE);
        Picasso.with(mContext).load(place.getImageResourceId(mContext)).into(holder.placeImage);

        Bitmap photo = BitmapFactory.decodeResource(mContext.getResources(), place.getImageResourceId(mContext));

        Palette.generateAsync(photo, new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                int mutedLight = palette.getMutedColor(mContext.getResources().getColor(android.R.color.black));
                holder.placeNameHolder.setBackgroundColor(mutedLight);
            }
        });
    }

    @Override
    public int getItemCount() {
        return PlaceType.getPlaceTypes(20).size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout placeHolder;
        public LinearLayout placeNameHolder;
        public TextView placeName;
        public ImageView placeImage;
        public ImageView mSelectedIv;
        public Button mDetailButton;

        public ViewHolder(View itemView) {
            super(itemView);
            placeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            placeName = (TextView) itemView.findViewById(R.id.placeName);
            placeNameHolder = (LinearLayout) itemView.findViewById(R.id.placeNameHolder);
            placeImage = (ImageView) itemView.findViewById(R.id.placeImage);
            mSelectedIv = (ImageView) itemView.findViewById(R.id.iv_select);
            placeHolder.setOnClickListener(this);
            mDetailButton = (Button) itemView.findViewById(R.id.btn_detail);
            mDetailButton.setVisibility(View.GONE);

            placeName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Context context = v.getContext();
                    ArrayList<Long> selectedPlacesIds = new ArrayList<>();
                    selectedPlacesIds.add(mPlaceTypes.get(getPosition()).getId());
                    Intent intent = new Intent(context, PlacesActivity.class);
                    intent.putExtra("selected_places", selectedPlacesIds);
                    ((Activity) context).startActivity(intent);
                }
            });


        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }

            Log.e("isFav", mPlaceTypes.get(getPosition()).isFav() + " : before" + " " + getPosition());
            boolean isFav = !mPlaceTypes.get(getPosition()).isFav();
            mPlaceTypes.get(getPosition()).setIsFav(isFav);
            mPlaceTypes.get(getPosition()).save();
            mSelectedIv.setVisibility(isFav ? View.VISIBLE : View.GONE);
            Log.e("isFav", mPlaceTypes.get(getPosition()).isFav() + " : after");
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


}