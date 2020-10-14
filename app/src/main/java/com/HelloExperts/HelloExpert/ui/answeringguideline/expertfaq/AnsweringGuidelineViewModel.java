package com.HelloExperts.HelloExpert.ui.answeringguideline.expertfaq;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AnsweringGuidelineViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AnsweringGuidelineViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}