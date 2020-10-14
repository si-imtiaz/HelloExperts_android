package com.HelloExperts.HelloExpert.ui.alertSetting.alertSettings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlertSettingsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AlertSettingsViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}