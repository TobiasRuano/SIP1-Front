package com.example.sip1.ui.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SlideshowViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SlideshowViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Aca encontraremos graficos con informacion relevante sobre los servicios subscriptos");
    }

    public LiveData<String> getText() {
        return mText;
    }
}