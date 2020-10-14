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

import org.jsoup.Jsoup;

import java.util.List;


public class Newly_questions_adapter extends RecyclerView.Adapter<Newly_questions_adapter.MyViewHolder> {
    private List<Newly_questions_data> newly_questions_data;

    public Newly_questions_adapter(List<Newly_questions_data> data){
        this.newly_questions_data = data;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newly_questions_template,parent,false);
       MyViewHolder myViewHolder = new MyViewHolder(view);
       return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Newly_questions_data newly_questions_data_list= this.newly_questions_data.get(position);
        holder.question.setTypeface(null, Typeface.BOLD);
        holder.price.setTypeface(null, Typeface.BOLD);

        holder.question.setText(Jsoup.parse(this.newly_questions_data.get(position).getQuestion()).text());
        holder.date.setText(this.newly_questions_data.get(position).getDate());
        holder.price.setText(this.newly_questions_data.get(position).getPrice());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle question_info = new Bundle();
                question_info.putInt("question_id",newly_questions_data_list.getQuestion_id());
                question_info.putBoolean("show_status",false);
                question_info.putBoolean("This_is_already_locked_question",false);
                question_info.putString("from_recent","false");
               Intent question_view_intent = new Intent(view.getContext(),Question_view.class);
               question_view_intent.putExtras(question_info);
                view.getContext().startActivity(question_view_intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return this.newly_questions_data.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView question,price,date;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question=(TextView)itemView.findViewById(R.id.question);
            price=(TextView)itemView.findViewById(R.id.price);
            date=(TextView)itemView.findViewById(R.id.date);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.list_template);
        }
    }
}
