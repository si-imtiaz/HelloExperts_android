package com.HelloExperts.HelloExpert.ui.suspensionpolicy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SuspensionPolicyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SuspensionPolicyViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}