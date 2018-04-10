package com.semicolon.Halan.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.semicolon.Halan.Models.MyOrderModel;
import com.semicolon.Halan.R;
import java.util.List;



public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.Holder> {
    Context context;
    MyOrderModel mmodel;
    List<MyOrderModel> mylist;

    public MyOrdersAdapter(Context context, List<MyOrderModel> mylist) {
        this.context = context;
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

        holder.container.setTag(position);

        holder.client_location.setText(mmodel.getClient_location());
        holder.market_location.setText(mmodel.getMarket_location());
        holder.cost.setText(mmodel.getCost());
        holder.order_start_time.setText(mmodel.getOrder_start_time());



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

            int position = (int) view.getTag();

            mmodel = mylist.get(position);


        }

    }



}
