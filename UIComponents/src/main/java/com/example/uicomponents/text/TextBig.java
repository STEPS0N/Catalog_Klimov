package com.example.uicomponents.text;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.uicomponents.R;
import com.example.uicomponents.button.BthCustom;

public class TextBig extends TextCustom {

    public TextBig(@NonNull Context context) {
        super(context);
    }

    public TextBig(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextBig(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(Integer idLayout) {
        super.init(R.layout.text);
    }
}
