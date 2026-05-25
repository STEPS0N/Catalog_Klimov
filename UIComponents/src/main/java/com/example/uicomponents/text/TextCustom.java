package com.example.uicomponents.text;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.uicomponents.R;

public class TextCustom extends ConstraintLayout {

    public EditText Text;
    public TextView message;

    public enum TypeText {
        DEFAULT, HOVER, ERROR, DATE;
    }

    public TextCustom(@NonNull Context context) {
        super(context);
        init(null);
    }

    public TextCustom(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(null);
    }

    public TextCustom(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(null);
    }

    public void init(String title, String hint, String text) {
        if (Text == null) {
            LayoutInflater.from(getContext()).inflate(R.layout.text, this, true);
            Text = findViewById(R.id.text);
            message = findViewById(R.id.textViewMessage);
        }
        Text.setHint(hint);
    }

    public void init(Integer idLayout) {
        if (idLayout == null) return;

        LayoutInflater.from(this.getContext()).inflate(idLayout, this, true);

        Text = findViewById(R.id.text);
        message = findViewById(R.id.textViewMessage);
    }

    public void OnError(boolean isError, String textError) {
        if (isError) {
            Text.setBackgroundResource(R.drawable.text_error);
            message.setText(textError);
        } else {
            Text.setBackgroundResource(R.drawable.text_default);
            message.setText("");
        }
    }

//    public void init(String value, TypeText type) {
//        Text.setText(value);
//        if (type == TypeText.DEFAULT) {
//            Text.setBackgroundResource(R.drawable.text_default);
//            Text.setTextColor(Color.parseColor("#000000"));
//            NameListener();
//        } else if (type == TypeText.HOVER) {
//            Text.setBackgroundResource(R.drawable.text_hover);
//            Text.setTextColor(Color.parseColor("#000000"));
//            NameListener();
//        } else if (type == TypeText.ERROR) {
//            Text.setBackgroundResource(R.drawable.text_error);
//            Text.setTextColor(Color.parseColor("#000000"));
//            NameListener();
//        } else if (type == TypeText.DATE) {
//            Text.setBackgroundResource(R.drawable.text_default);
//            DateListener();
//        }
//    }

//    private void NameListener() {
//        Text.setOnFocusChangeListener((v, hasFocus) -> {
//            if (hasFocus) {
//                Text.setBackgroundResource(R.drawable.text_hover);
//                message.setText("");
//            } else {
//                if (Text.getText().toString().trim().isEmpty()) {
//                    Text.setBackgroundResource(R.drawable.text_error);
//                    message.setText("Поле не может быть пустым");
//                } else if (Text.getText().toString().trim().matches("\\d+")) {
//                    Text.setBackgroundResource(R.drawable.text_error);
//                    message.setText("Не корректный ввод значений");
//                } else {
//                    Text.setBackgroundResource(R.drawable.text_default);
//                    message.setText("");
//                }
//            }
//        });
//    }
//
//    private void DateListener() {
//        Text.setOnFocusChangeListener((v, hasFocus) -> {
//            if (hasFocus) {
//                Text.setBackgroundResource(R.drawable.text_hover);
//                message.setText("");
//            } else {
//                if (Text.getText().toString().trim().isEmpty()) {
//                    Text.setBackgroundResource(R.drawable.text_error);
//                    message.setText("Поле не может быть пустым");
//                } else if (!Text.getText().toString().trim().matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
//                    Text.setBackgroundResource(R.drawable.text_error);
//                    message.setText("Введите дату в формате ДД.ММ.ГГГГ");
//                } else {
//                    Text.setBackgroundResource(R.drawable.text_default);
//                    message.setText("");
//                }
//            }
//        });
//    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        Text.setEnabled(enabled);
    }
}
