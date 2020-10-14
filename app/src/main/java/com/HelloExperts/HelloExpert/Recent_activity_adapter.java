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


public class Recent_activity_adapter extends RecyclerView.Adapter<Recent_activity_adapter.MyViewHolder> {
    private List<Recent_activity_data> recent_activity_data;
    public Recent_activity_adapter(List<Recent_activity_data> data){
        this.recent_activity_data = data;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_activity_template,parent,false);
       MyViewHolder myViewHolder = new MyViewHolder(view);
       return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Recent_activity_data recent_activity_data_list= this.recent_activity_data.get(position);
        holder.question.setTypeface(null, Typeface.BOLD);

        holder.question.setText(Jsoup.parse(recent_activity_data.get(position).getQuestion()).text());
        holder.date.setText(recent_activity_data.get(position).getDate());
        if(recent_activity_data.get(position).getStatus().equals("Accepted") ){
            holder.status.setText(recent_activity_data.get(position).getStatus());
            holder.status.setBackgroundResource(R.drawable.accepted_background);
        }else  if(recent_activity_data.get(position).getStatus().equals("In-Progress")){
            holder.status.setText(recent_activity_data.get(position).getStatus());
            holder.status.setBackgroundResource(R.drawable.in_progress_background);
        }
        else  if(recent_activity_data.get(position).getStatus().equals("Rejected")){
            holder.status.setText(recent_activity_data.get(position).getStatus());
            holder.status.setBackgroundResource(R.drawable.rejected_background);
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                Fragment myFragment = new QuestionPreviewFragment();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, myFragment).addToBackStack(null).commit();
//
                Bundle question_info = new Bundle();
                question_info.putInt("question_id",recent_activity_data_list.getQuestion_id());
                question_info.putString("from_recent","true");
                question_info.putBoolean("show_status",false);
                question_info.putBoolean("This_is_already_locked_question",false);
                question_info.putString("status",recent_activity_data_list.getStatus());
                Intent question_view_intent = new Intent(view.getContext(),Question_view.class);
                question_view_intent.putExtras(question_info);
                view.getContext().startActivity(question_view_intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.recent_activity_data.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView question,status,date;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question=(TextView)itemView.findViewById(R.id.recent_activity_question);
            status=(TextView)itemView.findViewById(R.id.recent_activity_status);
            date=(TextView)itemView.findViewById(R.id.recent_activity_date);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.list_template);
        }
    }
}
