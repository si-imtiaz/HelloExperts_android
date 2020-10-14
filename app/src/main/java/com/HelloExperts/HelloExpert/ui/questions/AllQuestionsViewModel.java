package com.HelloExperts.HelloExpert.ui.questions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AllQuestionsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AllQuestionsViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}