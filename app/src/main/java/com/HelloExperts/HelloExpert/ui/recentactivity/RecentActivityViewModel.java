package com.HelloExperts.HelloExpert.ui.recentactivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecentActivityViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RecentActivityViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}