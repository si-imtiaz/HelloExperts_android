package com.HelloExperts.HelloExpert;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HelloExperts.HelloExpert.R;

import java.util.List;


public class Withdraw_methods_list_adapter extends RecyclerView.Adapter<Withdraw_methods_list_adapter.MyViewHolder> {
    private List<Withdraw_methods_list_data> withdraw_methods_list_data;
    public Withdraw_methods_list_adapter(List<Withdraw_methods_list_data> data){
        this.withdraw_methods_list_data = data;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.withdraw_methods_list,parent,false);
       MyViewHolder myViewHolder = new MyViewHolder(view);
       return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Withdraw_methods_list_data withdraw_methods_list_data1= this.withdraw_methods_list_data.get(position);
        holder.account_tile.setText(this.withdraw_methods_list_data.get(position).getAccount_title());
        holder.source_name.setText(this.withdraw_methods_list_data.get(position).getSource_name());
        holder.paypal_email.setText(this.withdraw_methods_list_data.get(position).getPaypalEmail());
        holder.status.setText(this.withdraw_methods_list_data.get(position).getStatus());
        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                new AlertDialog.Builder(view.getContext())

                        .setIcon(R.drawable.ic_error_black_24dp)

                        .setTitle("Warning")

                        .setMessage("Are you sure you want to delete this method")

                        .setPositiveButton("YES", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                   Toast.makeText(view.getContext(),withdraw_methods_list_data.get(position).getWithdraw_method_id()+" id:",Toast.LENGTH_LONG).show();
                                   // withdraw_methods_list_data.remove(position);
                                   // notifyItemRemoved(position);
                                   // notifyItemRangeChanged(position,getItemCount());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                            }
                        })
                        .show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.withdraw_methods_list_data.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView account_tile,source_name,paypal_email,status,action;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            account_tile=(TextView)itemView.findViewById(R.id.account_title);
            source_name=(TextView)itemView.findViewById(R.id.source_name);
            paypal_email=(TextView)itemView.findViewById(R.id.paypal_email);
            status=(TextView)itemView.findViewById(R.id.status);
            action=(TextView)itemView.findViewById(R.id.action);

        }
    }
}
