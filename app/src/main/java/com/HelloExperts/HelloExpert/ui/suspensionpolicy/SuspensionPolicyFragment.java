package com.HelloExperts.HelloExpert.ui.suspensionpolicy;

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

public class SuspensionPolicyFragment extends Fragment {


    private SuspensionPolicyViewModel ViewModel;
    private MyRegulerText level_1,level_2,level_3,level_4,level_5,level_6,duration_1,duration_2,duration_3,duration_4,duration_5,duration_6;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewModel =
                ViewModelProviders.of(this).get(SuspensionPolicyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_suspension_policy, container, false);

        level_1 = (MyRegulerText)root.findViewById(R.id.level_1);
        level_2 = (MyRegulerText)root.findViewById(R.id.level_2);
        level_3 = (MyRegulerText)root.findViewById(R.id.level_3);
        level_4 = (MyRegulerText)root.findViewById(R.id.level_4);
        level_5 = (MyRegulerText)root.findViewById(R.id.level_5);
        level_6 = (MyRegulerText)root.findViewById(R.id.level_6);
        duration_1 = (MyRegulerText)root.findViewById(R.id.duration_1);
        duration_2 = (MyRegulerText)root.findViewById(R.id.duration_2);
        duration_3 = (MyRegulerText)root.findViewById(R.id.duration_3);
        duration_4 = (MyRegulerText)root.findViewById(R.id.duration_4);
        duration_5 = (MyRegulerText)root.findViewById(R.id.duration_5);
        duration_6 = (MyRegulerText)root.findViewById(R.id.duration_6);
        level_1.setTypeface(null, Typeface.BOLD);
        level_2.setTypeface(null, Typeface.BOLD);
        level_3.setTypeface(null, Typeface.BOLD);
        level_4.setTypeface(null, Typeface.BOLD);
        level_5.setTypeface(null, Typeface.BOLD);
        level_6.setTypeface(null, Typeface.BOLD);
        duration_1.setTypeface(null, Typeface.BOLD);
        duration_2.setTypeface(null, Typeface.BOLD);
        duration_3.setTypeface(null, Typeface.BOLD);
        duration_4.setTypeface(null, Typeface.BOLD);
        duration_5.setTypeface(null, Typeface.BOLD);
        duration_6.setTypeface(null, Typeface.BOLD);


        SharedPreferences sp=this.getActivity().getSharedPreferences("Login", 0);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putInt("navigation_checked_item_id",R.id.nav_suspension_policy);
        Ed.commit();
        return root;
    }
}