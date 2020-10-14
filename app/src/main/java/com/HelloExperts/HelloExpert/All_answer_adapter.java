package com.HelloExperts.HelloExpert;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HelloExperts.HelloExpert.R;

import java.util.List;


public class All_answer_adapter extends RecyclerView.Adapter<All_answer_adapter.MyViewHolder> {
    private List<All_answers_data> all_answers_data;
    public All_answer_adapter(List<All_answers_data> data){
        this.all_answers_data = data;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_answers_template,parent,false);
       MyViewHolder myViewHolder = new MyViewHolder(view);
       return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final All_answers_data all_answers_data_list= this.all_answers_data.get(position);
        holder.question.setTypeface(null, Typeface.BOLD);
        holder.price.setText(all_answers_data.get(position).getPrice());
        holder.question.setText(all_answers_data.get(position).getQuestion());
        holder.date.setText(all_answers_data.get(position).getDate());
        if(all_answers_data.get(position).getStatus().equals("Accepted") ){
            holder.status.setText(all_answers_data.get(position).getStatus());
            holder.status.setBackgroundResource(R.drawable.answer_accepted_background);
        }else  if(all_answers_data.get(position).getStatus().equals("Submitted")){
            holder.status.setText(all_answers_data.get(position).getStatus());
            holder.status.setBackgroundResource(R.drawable.answer_in_progress_background);
        }
        else  if(all_answers_data.get(position).getStatus().equals("Rejected")){
            holder.status.setText(all_answers_data.get(position).getStatus());
            holder.status.setBackgroundResource(R.drawable.answer_rejected_background);
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle question_info = new Bundle();
                question_info.putInt("question_id",all_answers_data_list.getQuestion_id());
                question_info.putBoolean("show_status",true);
                question_info.putString("from_recent","false");
                question_info.putString("status",all_answers_data_list.getStatus());
                Intent question_view_intent = new Intent(view.getContext(),Question_view.class);
                question_view_intent.putExtras(question_info);
                view.getContext().startActivity(question_view_intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return this.all_answers_data.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView question,status,date,price;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question=(TextView)itemView.findViewById(R.id.all_answers_question);
            status=(TextView)itemView.findViewById(R.id.all_answers_status);
            date=(TextView)itemView.findViewById(R.id.all_answers_date);
            price=(TextView)itemView.findViewById(R.id.all_answers_price);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.list_template);
        }
    }
}
