package com.semicolon.Halan.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.semicolon.Halan.Activities.ClientNotificationActivity;
import com.semicolon.Halan.Models.ClientNotificationModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Tags;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Delta on 13/04/2018.
 */

public class ClientNotificationAdapter extends RecyclerView.Adapter<ClientNotificationAdapter.myHolder> {

    private Context context;
    private List<ClientNotificationModel> clientNotificationModelList;
    private Target target;
    private ClientNotificationActivity activity;

    public ClientNotificationAdapter(Context context, List<ClientNotificationModel> clientNotificationModelList) {
        this.context = context;
        activity = (ClientNotificationActivity) context;
        this.clientNotificationModelList = clientNotificationModelList;
    }

    @Override
    public myHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.client_notification_row,parent,false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(myHolder holder, int position) {
        ClientNotificationModel model = clientNotificationModelList.get(position);
        holder.BindData(model);
    }

    @Override
    public int getItemCount() {
        return clientNotificationModelList.size();
    }

    public class myHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView not_date,driver_name,rate,date,cost;
        private CircleImageView driver_image;
        private RatingBar ratingBar;
        private Button accept,refuse;

        public myHolder(View itemView) {
            super(itemView);
            not_date = itemView.findViewById(R.id.not_date);
            rate = itemView.findViewById(R.id.rate);
            date = itemView.findViewById(R.id.date);
            driver_name = itemView.findViewById(R.id.driver_name);
            cost = itemView.findViewById(R.id.cost);
            driver_image = itemView.findViewById(R.id.driver_image);
            ratingBar = itemView.findViewById(R.id.rateBar);
            accept = itemView.findViewById(R.id.accept);
            refuse = itemView.findViewById(R.id.refuse);
            //ratingBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(context,R.color.rate), PorterDuff.Mode.SRC_IN);
            try {
                LayerDrawable drawable = (LayerDrawable) ratingBar.getProgressDrawable();
                drawable.getDrawable(0).setColorFilter(ContextCompat.getColor(context,R.color.gray3), PorterDuff.Mode.SRC_ATOP);

                drawable.getDrawable(1).setColorFilter(ContextCompat.getColor(context,R.color.rate), PorterDuff.Mode.SRC_ATOP);
                drawable.getDrawable(2).setColorFilter(ContextCompat.getColor(context,R.color.rate), PorterDuff.Mode.SRC_ATOP);

            }catch (NullPointerException e){}


            ratingBar.setEnabled(false);
            accept.setOnClickListener(this);
            refuse.setOnClickListener(this);

        }
        public void BindData(ClientNotificationModel model)
        {
            not_date.setText(model.getOrder_date());
            rate.setText(String.valueOf(model.getRate_evaluation()));
            date.setText(model.getOrder_date());
            driver_name.setText(model.getDriver_name());
            cost.setText(model.getOrder_cost()+" ريال");

            target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    driver_image.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            if (model.getDriver_image()!=null|| TextUtils.isEmpty(model.getDriver_image()) || !model.getDriver_image().equals("0"))
            {
                Picasso.with(context).load(Uri.parse(Tags.ImgPath+model.getDriver_image())).placeholder(R.drawable.user_profile).into(target);
            }
            ratingBar.setRating((float) model.getStars_evaluation());
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id)
            {
                case R.id.accept:
                    activity.setPos(getAdapterPosition(),Tags.accept);
                    break;

                case R.id.refuse:
                    activity.setPos(getAdapterPosition(),Tags.refuse);
                    break;
            }

        }
    }
}
