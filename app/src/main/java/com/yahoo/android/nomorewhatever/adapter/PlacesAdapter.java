package com.yahoo.android.nomorewhatever.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yahoo.android.nomorewhatever.R;
import com.yahoo.android.nomorewhatever.model.Place;

import java.util.List;

/**
 * Created by andychw on 8/23/15.
 */
public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {

    Context mContext;
    OnItemClickListener mItemClickListener;
    private List<Place> mPlaces;

    public PlacesAdapter(List<Place> mPlaces, Context mContext) {
        this.mPlaces = mPlaces;
        this.mContext = mContext;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_places, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Place place = mPlaces.get(position);
        holder.placeName.setText(place.name);
        holder.mSelectedIv.setVisibility(mPlaces.get(position).isFav() ? View.VISIBLE : View.GONE);
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
        return Place.getPlaceCount(1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout placeHolder;
        public LinearLayout placeNameHolder;
        public TextView placeName;
        public ImageView placeImage;
        public ImageView mSelectedIv;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d("debug", "ffffffffffffffffff2");
            placeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            placeName = (TextView) itemView.findViewById(R.id.placeName);
            placeNameHolder = (LinearLayout) itemView.findViewById(R.id.placeNameHolder);
            placeImage = (ImageView) itemView.findViewById(R.id.placeImage);
            mSelectedIv = (ImageView) itemView.findViewById(R.id.iv_select);
            placeHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
            Log.e("isFav", mPlaces.get(getPosition()).isFav() + " : before" + " " + getPosition());
            boolean isFav = !mPlaces.get(getPosition()).isFav();
            mPlaces.get(getPosition()).setIsFav(isFav);
            mPlaces.get(getPosition()).save();
            mSelectedIv.setVisibility(isFav ? View.VISIBLE : View.GONE);
            Log.e("isFav", mPlaces.get(getPosition()).isFav() + " : after");
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


}