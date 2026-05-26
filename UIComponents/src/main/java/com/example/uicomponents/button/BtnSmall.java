package com.example.uicomponents.button;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.uicomponents.R;

public class BtnSmall extends BthCustom{
    public BtnSmall(@NonNull Context context) {
        super(context);
    }

    public BtnSmall(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BtnSmall(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init(Integer idLayout) {
        super.init(R.layout.btn_small);
    }
}
