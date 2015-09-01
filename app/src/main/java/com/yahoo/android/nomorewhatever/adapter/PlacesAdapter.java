package com.yahoo.android.nomorewhatever.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yahoo.android.nomorewhatever.R;
import com.yahoo.android.nomorewhatever.activity.PlaceDetailActivity;
import com.yahoo.android.nomorewhatever.activity.PlacesActivity;
import com.yahoo.android.nomorewhatever.model.Place;

import java.util.List;

/**
 * Created by andychw on 8/23/15.
 */
public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {

    private static final String TAG = "Debug";
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

        // TODO: 8/29/15 if mPlaces size is 0
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


        holder.mDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, PlaceDetailActivity.class);
                Log.d(TAG, "onClick " + place.getName());
                intent.putExtra(PlaceDetailActivity.EXTRA_PARAM_ID, place);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, v, "photo_hero");
                ((Activity) context).startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        //return Place.getPlaceCount(1);
        return mPlaces.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout placeHolder;
        public LinearLayout placeNameHolder;
        public TextView placeName;
        public ImageView placeImage;
        public ImageView mSelectedIv;
        public Button mDetailButton;
        public MenuItem ami_count;

        public ViewHolder(View itemView) {
            super(itemView);

            placeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            placeName = (TextView) itemView.findViewById(R.id.placeName);
            placeNameHolder = (LinearLayout) itemView.findViewById(R.id.placeNameHolder);
            placeImage = (ImageView) itemView.findViewById(R.id.placeImage);
            mSelectedIv = (ImageView) itemView.findViewById(R.id.iv_select);
            placeHolder.setOnClickListener(this);
            mDetailButton = (Button) itemView.findViewById(R.id.btn_detail);
            ami_count=(MenuItem) itemView.findViewById(R.id.action_count);
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

            // referesh count of fav
            int original_count = Integer.parseInt(PlacesActivity.ami_count.getTitle().toString());
            if(mPlaces.get(getPosition()).isFav()){
                PlacesActivity.ami_count.setTitle(String.valueOf(original_count - 1));
            }else{
                PlacesActivity.ami_count.setTitle(String.valueOf(original_count+1));
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


}