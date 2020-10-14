package com.HelloExperts.HelloExpert;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.HelloExperts.HelloExpert.R;

import cdflynn.android.library.checkview.CheckView;

public class access_granted_dialog extends Dialog {

    public Activity c;
    public Dialog d;
    CheckView check;


    public access_granted_dialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.access_granted);
        check = (CheckView)findViewById(R.id.check);
        check.check();

    }


}