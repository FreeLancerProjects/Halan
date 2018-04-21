package com.semicolon.Halan.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.semicolon.Halan.Activities.DriverNotificationActivity;
import com.semicolon.Halan.Activities.DriverOrderDetailsActivity;
import com.semicolon.Halan.Models.MyOrderModel;
import com.semicolon.Halan.R;

import java.util.List;


public class DriverNotficationAdapter extends RecyclerView.Adapter<DriverNotficationAdapter.Holder> {
    Context context;
    MyOrderModel mmodel;
    List<MyOrderModel> mylist;
    DriverNotificationActivity activity;

    public DriverNotficationAdapter(Context context, List<MyOrderModel> mylist) {
        this.context = context;
        this.activity = (DriverNotificationActivity) context;
        this.mylist = mylist;

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_orders, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        mmodel = mylist.get(position);

        //holder.container.setTag(position);

        holder.client_location.setText(mmodel.getClient_location());
        holder.market_location.setText(mmodel.getMarket_location());
        holder.cost.setText(mmodel.getOrder_driver_cost());
        holder.order_start_time.setText(mmodel.getOrder_date());



    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView client_location, market_location, cost,order_start_time;
        CardView container;


        public Holder(View itemView) {
            super(itemView);

            client_location = itemView.findViewById(R.id.txt_client_location);
            market_location = itemView.findViewById(R.id.txt_market_location);
            cost = itemView.findViewById(R.id.txt_cost);
            order_start_time = itemView.findViewById(R.id.txt_date);
            container=itemView.findViewById(R.id.card_order);
            container.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();

            mmodel = mylist.get(position);
            Intent intent=new Intent(context, DriverOrderDetailsActivity.class);
            intent.putExtra("client_location",mmodel.getClient_location());
            intent.putExtra("market_location",mmodel.getMarket_location());
            intent.putExtra("market_lat",mmodel.getMarket_google_lat());
            intent.putExtra("client_lat",mmodel.getClient_google_lat());
            intent.putExtra("market_long",mmodel.getMarket_google_lang());
            intent.putExtra("client_long",mmodel.getClient_google_lang());
            intent.putExtra("order_detail",mmodel.getOrder_details());
            intent.putExtra("cost",mmodel.getOrder_driver_cost());
            intent.putExtra("phone",mmodel.getClient_phone());
            intent.putExtra("client_id",mmodel.getClient_id_fk());
            intent.putExtra("order_id",mmodel.getOrder_id());
            intent.putExtra("message_id",mmodel.getMessage_id());

            context.startActivity(intent);
            activity.finish();


        }

    }



}
