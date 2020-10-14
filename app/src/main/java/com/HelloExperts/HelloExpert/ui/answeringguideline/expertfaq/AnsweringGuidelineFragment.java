package com.HelloExperts.HelloExpert.ui.answeringguideline.expertfaq;

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

public class AnsweringGuidelineFragment extends Fragment {

    private AnsweringGuidelineViewModel ViewModel;

    private MyRegulerText answer_writing_heading_1,answer_writing_heading_2,answer_writing_heading_3,
            best_practices_heading_1,best_practices_heading_2,best_practices_heading_3,
            action_to_strictly_avoid_heading_1,action_to_strictly_avoid_heading_2,action_to_strictly_avoid_heading_3,action_to_strictly_avoid_heading_4,action_to_strictly_avoid_heading_5,
            expert_guideline_note;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewModel =
                ViewModelProviders.of(this).get(AnsweringGuidelineViewModel.class);
        View root = inflater.inflate(R.layout.fragment_answering_guidelines, container, false);

        answer_writing_heading_1 = (MyRegulerText)root.findViewById(R.id.answer_writing_heading_1);
        answer_writing_heading_2 = (MyRegulerText)root.findViewById(R.id.answer_writing_heading_2);
        answer_writing_heading_3 = (MyRegulerText)root.findViewById(R.id.answer_writing_heading_3);
        answer_writing_heading_1.setTypeface(null, Typeface.BOLD);
        answer_writing_heading_2.setTypeface(null, Typeface.BOLD);
        answer_writing_heading_3.setTypeface(null, Typeface.BOLD);

        best_practices_heading_1 = (MyRegulerText)root.findViewById(R.id.best_practices_heading_1);
        best_practices_heading_2 = (MyRegulerText)root.findViewById(R.id.best_practices_heading_2);
        best_practices_heading_3 = (MyRegulerText)root.findViewById(R.id.best_practices_heading_3);
        best_practices_heading_1.setTypeface(null, Typeface.BOLD);
        best_practices_heading_2.setTypeface(null, Typeface.BOLD);
        best_practices_heading_3.setTypeface(null, Typeface.BOLD);

        action_to_strictly_avoid_heading_1 = (MyRegulerText)root.findViewById(R.id.action_to_strictly_avoid_heading_1);
        action_to_strictly_avoid_heading_2 = (MyRegulerText)root.findViewById(R.id.action_to_strictly_avoid_heading_2);
        action_to_strictly_avoid_heading_3 = (MyRegulerText)root.findViewById(R.id.action_to_strictly_avoid_heading_3);
        action_to_strictly_avoid_heading_4 = (MyRegulerText)root.findViewById(R.id.action_to_strictly_avoid_heading_4);
        action_to_strictly_avoid_heading_5 = (MyRegulerText)root.findViewById(R.id.action_to_strictly_avoid_heading_5);
        action_to_strictly_avoid_heading_1.setTypeface(null, Typeface.BOLD);
        action_to_strictly_avoid_heading_2.setTypeface(null, Typeface.BOLD);
        action_to_strictly_avoid_heading_3.setTypeface(null, Typeface.BOLD);
        action_to_strictly_avoid_heading_4.setTypeface(null, Typeface.BOLD);
        action_to_strictly_avoid_heading_5.setTypeface(null, Typeface.BOLD);

        expert_guideline_note = (MyRegulerText)root.findViewById(R.id.expert_guideline_note);
        expert_guideline_note.setTypeface(null,Typeface.BOLD);



        SharedPreferences sp=this.getActivity().getSharedPreferences("Login", 0);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putInt("navigation_checked_item_id",R.id.nav_expert_answering_guideline);
        Ed.commit();



        return root;
    }
}