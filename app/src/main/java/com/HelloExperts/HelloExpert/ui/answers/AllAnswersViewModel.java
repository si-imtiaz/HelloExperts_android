package com.HelloExperts.HelloExpert.ui.answers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AllAnswersViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AllAnswersViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}