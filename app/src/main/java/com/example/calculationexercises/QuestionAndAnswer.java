package com.example.calculationexercises;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import static com.example.calculationexercises.MainActivity.mainActivity;

public class QuestionAndAnswer extends LinearLayout {

    public static QuestionAndAnswer questionAndAnswer;
    public static TextView question;
    public static TextView modPoint;
    public static EditText res;
    public static EditText mod;
    public QuestionAndAnswer(Context context) {
        super(context);
        questionAndAnswer = this;
        setFrame();
    }

    public QuestionAndAnswer(Context context, AttributeSet attrs) {
        super(context, attrs);
        questionAndAnswer = this;
        setFrame();
    }

    public QuestionAndAnswer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        questionAndAnswer = this;
        setFrame();
    }

    private void setFrame(){
        setOrientation(VERTICAL);

        question = new TextView(mainActivity);
        question.setTextSize(40);
        question.setWidth(900);
        this.addView(question);

        LinearLayout ansWindow = new LinearLayout(mainActivity);
        ansWindow.setOrientation(HORIZONTAL);
        res = new EditText(mainActivity);
        res.setTextSize(40);
        res.setMinWidth(300);
        res.setSingleLine();
        res.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        res.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        modPoint = new TextView(mainActivity);
        modPoint.setText("······");
        modPoint.setTextSize(40);
        mod = new EditText(mainActivity);
        mod.setTextSize(40);
        mod.setMinWidth(300);
        mod.setSingleLine();
        mod.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        mod.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        ansWindow.addView(res);
        ansWindow.addView(modPoint);
        ansWindow.addView(mod);
        this.addView(ansWindow);
    }
}
