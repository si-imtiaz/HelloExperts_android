package com.HelloExperts.HelloExpert.ui.expertfaq;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExpertFaqViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ExpertFaqViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}