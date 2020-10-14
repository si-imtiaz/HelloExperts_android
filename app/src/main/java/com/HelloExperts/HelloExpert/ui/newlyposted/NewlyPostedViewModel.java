package com.HelloExperts.HelloExpert.ui.newlyposted;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewlyPostedViewModel extends ViewModel {
  private MutableLiveData<String> mText;

  public NewlyPostedViewModel() {
    mText = new MutableLiveData<>();

  }

  public LiveData<String> getText() {
    return mText;
  }

}