package com.semicolon.Halan.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.semicolon.Halan.Activities.NearbyPlacesActivity;
import com.semicolon.Halan.Models.NearbyItem;
import com.semicolon.Halan.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NearbyAdapter extends RecyclerView.Adapter <NearbyAdapter.myHolder>{
    private Context context;
    private List<NearbyItem> nearbyItemList;
    private NearbyPlacesActivity activity;

    public NearbyAdapter(Context context, List<NearbyItem> nearbyItemList) {
        this.context = context;
        this.activity = (NearbyPlacesActivity) context;
        this.nearbyItemList = nearbyItemList;
    }

    @Override
    public myHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.nearby_item_row,parent,false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(final myHolder holder, int position) {
        NearbyItem nearbyItem = nearbyItemList.get(position);
        holder.BindData(nearbyItem);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setItem(nearbyItemList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return nearbyItemList.size();
    }

    public class myHolder extends RecyclerView.ViewHolder{
        CircleImageView icon;
        TextView name,distance;
        public myHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.place_name);
            distance = itemView.findViewById(R.id.distance);


        }

        public void BindData(NearbyItem nearbyItem)
        {
            Picasso.with(context).load(Uri.parse(nearbyItem.getIcon())).into(icon);
            name.setText(nearbyItem.getName());
            distance.setText(String.valueOf(Math.floor(nearbyItem.getDistance())+" "+context.getString(R.string.km)));
        }
    }
}
