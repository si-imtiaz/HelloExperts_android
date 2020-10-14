package com.HelloExperts.HelloExpert;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import cdflynn.android.library.checkview.CheckView;

public class successfully_uploaded_dialog extends Dialog {

    public Activity c;
    public Dialog d;
    CheckView check;


    public successfully_uploaded_dialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.successfully_uploaded);
        check = (CheckView)findViewById(R.id.check);
        check.check();

    }


}