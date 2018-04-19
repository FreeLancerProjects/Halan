package com.semicolon.Halan.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.semicolon.Halan.Activities.ClientOrderDetailsActivity;
import com.semicolon.Halan.Activities.DriverOrderDetailActivity;
import com.semicolon.Halan.Models.MyOrderModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Tags;

import java.util.List;


public class CancelOrdersAdapter extends RecyclerView.Adapter<CancelOrdersAdapter.Holder> {
    Context context;
    MyOrderModel mmodel;
    List<MyOrderModel> mylist;
    String user_type;

    public CancelOrdersAdapter(Context context, List<MyOrderModel> mylist) {
        this.context = context;
        this.mylist = mylist;
        SharedPreferences preferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        user_type = preferences.getString("user_type","");

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cancel_order_item, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        mmodel = mylist.get(position);

        holder.container.setTag(position);

        holder.client_location.setText(mmodel.getClient_location());
        holder.market_location.setText(mmodel.getMarket_location());
        holder.cost.setText(mmodel.getCost());

        try {
            if (user_type.equals(Tags.Driver))
            {
                holder.name.setText(mmodel.getClient_name());


            }else if (user_type.equals(Tags.Client))
                {
                    holder.name.setText(mmodel.getDriver_name());

                }

        }catch (NullPointerException e)
        {

        }



    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,client_location, market_location, cost;
        CardView container;


        public Holder(View itemView) {
            super(itemView);

            client_location = itemView.findViewById(R.id.txt_client_location);
            market_location = itemView.findViewById(R.id.txt_market_location);
            cost = itemView.findViewById(R.id.txt_cost);
            name = itemView.findViewById(R.id.name);
            container=itemView.findViewById(R.id.card_order);
            container.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            mmodel = mylist.get(position);

            if (user_type.equals(Tags.Client))
            {
                Intent intent = new Intent(context, ClientOrderDetailsActivity.class);
                intent.putExtra("order",mmodel);
                context.startActivity(intent);
            }else if (user_type.equals(Tags.Driver))
            {
                Intent intent = new Intent(context, DriverOrderDetailActivity.class);
                intent.putExtra("order",mmodel);
                context.startActivity(intent);
            }


        }

    }



}
