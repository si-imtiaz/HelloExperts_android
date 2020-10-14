package com.HelloExperts.HelloExpert.ui.expertfaq;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.HelloExperts.HelloExpert.R;

import customfonts.MyRegulerText;

public class ExpertFaqFragment extends Fragment {

    private ExpertFaqViewModel ViewModel;

    private SharedPreferences dashboard_preferences;
    private MyRegulerText question_1,question_2,question_3,question_4,question_5,question_6,question_7,question_8
            ,question_9,question_10,question_11,question_12,question_13,question_14,question_15,question_16,expert_faq_note;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewModel =
                ViewModelProviders.of(this).get(ExpertFaqViewModel.class);
        View root = inflater.inflate(R.layout.fragment_expert_faq, container, false);
        question_1 = (MyRegulerText)root.findViewById(R.id.question_1);
        question_2 = (MyRegulerText)root.findViewById(R.id.question_2);
        question_3 = (MyRegulerText)root.findViewById(R.id.question_3);
        question_4 = (MyRegulerText)root.findViewById(R.id.question_4);
        question_5 = (MyRegulerText)root.findViewById(R.id.question_5);
        question_6 = (MyRegulerText)root.findViewById(R.id.question_6);
        question_7 = (MyRegulerText)root.findViewById(R.id.question_7);
        question_8 = (MyRegulerText)root.findViewById(R.id.question_8);
        question_9 = (MyRegulerText)root.findViewById(R.id.question_9);
        question_10 = (MyRegulerText)root.findViewById(R.id.question_10);
        question_11 = (MyRegulerText)root.findViewById(R.id.question_11);
        question_12 = (MyRegulerText)root.findViewById(R.id.question_12);
        question_13 = (MyRegulerText)root.findViewById(R.id.question_13);
        question_14 = (MyRegulerText)root.findViewById(R.id.question_14);
        question_15 = (MyRegulerText)root.findViewById(R.id.question_15);
        question_16 = (MyRegulerText)root.findViewById(R.id.question_16);
        expert_faq_note = (MyRegulerText)root.findViewById(R.id.expert_faq_note);
        question_1.setTypeface(null, Typeface.BOLD);
        question_2.setTypeface(null, Typeface.BOLD);
        question_3.setTypeface(null, Typeface.BOLD);
        question_4.setTypeface(null, Typeface.BOLD);
        question_5.setTypeface(null, Typeface.BOLD);
        question_6.setTypeface(null, Typeface.BOLD);
        question_7.setTypeface(null, Typeface.BOLD);
        question_8.setTypeface(null, Typeface.BOLD);
        question_9.setTypeface(null, Typeface.BOLD);
        question_10.setTypeface(null, Typeface.BOLD);
        question_11.setTypeface(null, Typeface.BOLD);
        question_12.setTypeface(null, Typeface.BOLD);
        question_13.setTypeface(null, Typeface.BOLD);
        question_14.setTypeface(null, Typeface.BOLD);
        question_15.setTypeface(null, Typeface.BOLD);
        question_16.setTypeface(null, Typeface.BOLD);
        expert_faq_note.setTypeface(null,Typeface.BOLD);



        SharedPreferences sp=this.getActivity().getSharedPreferences("Login", 0);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putInt("navigation_checked_item_id",R.id.nav_expert_faq);
        Ed.commit();

        dashboard_preferences=getActivity().getSharedPreferences("Login",0);

        if(dashboard_preferences.getString("customers_verification_status",null).equals("1")){

            String [] currency_text = dashboard_preferences.getString("customers_pending_payment",null).split(" ");

        }



        return root;
    }
}