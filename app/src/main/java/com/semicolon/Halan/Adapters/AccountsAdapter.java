package com.semicolon.Halan.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.semicolon.Halan.Models.BankAccountModel;
import com.semicolon.Halan.R;

import java.util.List;

public class AccountsAdapter extends RecyclerView.Adapter <AccountsAdapter.myHolder>{
    private Context context;
    private List<BankAccountModel> bankAccountModelList;

    public AccountsAdapter(Context context, List<BankAccountModel> bankAccountModelList) {
        this.context = context;
        this.bankAccountModelList = bankAccountModelList;
    }

    @Override
    public myHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.account_item_row,parent,false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(final myHolder holder, int position) {
        BankAccountModel bankAccountModel = bankAccountModelList.get(position);
        holder.BindData(bankAccountModel);

    }

    @Override
    public int getItemCount() {
        return bankAccountModelList.size();
    }

    public class myHolder extends RecyclerView.ViewHolder{
        TextView name,iban,bank_name,number;
        public myHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            iban = itemView.findViewById(R.id.iban);
            bank_name = itemView.findViewById(R.id.bank_name);
            number = itemView.findViewById(R.id.number);


        }

        public void BindData(BankAccountModel bankAccountModel)
        {
            name.setText(bankAccountModel.getAccount_name());
            iban.setText(bankAccountModel.getAccount_IBAN());
            bank_name.setText(bankAccountModel.getAccount_bank_name());
            number.setText(bankAccountModel.getAccount_number());
        }
    }
}
