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


public class Questions_categories_adapter extends RecyclerView.Adapter<Questions_categories_adapter.MyViewHolder> {
    private List<Question_Categories_data> question_categories_data;
    public Questions_categories_adapter(List<Question_Categories_data> data){
        this.question_categories_data = data;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_questions_template,parent,false);
       MyViewHolder myViewHolder = new MyViewHolder(view);
       return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Question_Categories_data question_categories_data_list= this.question_categories_data.get(position);
        holder.category_wise_question.setTypeface(null, Typeface.BOLD);
        holder.question_category_price.setTypeface(null, Typeface.BOLD);
        holder.category_wise_question.setText(Jsoup.parse(this.question_categories_data.get(position).getQuestion()).text());
        holder.question_category_date.setText(this.question_categories_data.get(position).getDate());
        holder.question_category_price.setText(this.question_categories_data.get(position).getPrice());
        holder.question_category_skill.setText(this.question_categories_data.get(position).getSkill());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle question_info = new Bundle();
                question_info.putInt("question_id",question_categories_data_list.getQuestion_id());
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
        return this.question_categories_data.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView category_wise_question,question_category_skill,question_category_price,question_category_date;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            category_wise_question=(TextView)itemView.findViewById(R.id.category_wise_question);
            question_category_price=(TextView)itemView.findViewById(R.id.question_category_price);
            question_category_date=(TextView)itemView.findViewById(R.id.question_category_date);
            question_category_skill=(TextView)itemView.findViewById(R.id.question_category_skill);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.list_template);
        }
    }
}
