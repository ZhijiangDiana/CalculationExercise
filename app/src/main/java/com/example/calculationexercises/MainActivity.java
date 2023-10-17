package com.example.calculationexercises;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static MainActivity mainActivity;
    private final int ADD = 0;
    private final int SUB = 1;
    private final int MUL = 2;
    private final int DIV = 3;
    private final int COMPLEX = 4;
    public static Pair<Integer, Integer> thisAnswer;

    public MainActivity(){
        mainActivity = this;
    }
    SQLiteOpenHelper helper;
    SQLiteDatabase db;
    int addMaxID, subMaxID, mulMaxID, divMaxID, complexMaxID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        initDB();

        CheckBox add = findViewById(R.id.addExercise);
        CheckBox sub = findViewById(R.id.subExercise);
        CheckBox mul = findViewById(R.id.mulExercise);
        CheckBox div = findViewById(R.id.divExercise);
        CheckBox complex = findViewById(R.id.complexExercise);
        Button apply = findViewById(R.id.apply);
        Button reset = findViewById(R.id.reset);
        thisAnswer = nextQuestion(MUL);
        complex.setVisibility(View.INVISIBLE);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> checked = new ArrayList<>(10);
                if(add.isChecked()) checked.add(ADD);
                if(sub.isChecked()) checked.add(SUB);
                if(mul.isChecked()) checked.add(MUL);
                if(div.isChecked()) checked.add(DIV);
                if(complex.isChecked()) checked.add(COMPLEX);
                if(checked.isEmpty()){
                    Toast.makeText(mainActivity, "未选择题库，请选择后再提交", Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                Random rand = new Random();
                if(!checkAnswer(thisAnswer))
                    return;
                thisAnswer = nextQuestion(checked.get(rand.nextInt(checked.size())));
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionAndAnswer.res.setText("");
                QuestionAndAnswer.mod.setText("");
            }
        });
    }

    private void initDB(){
        helper = ExerciseSQLiteOpenHelper.getInstance(mainActivity);
        db = helper.getReadableDatabase();
        Cursor cursor;

        // 加法题库最大id
        cursor = db.rawQuery("SELECT MAX(_id) FROM AddExercise", null);
        if(cursor.moveToFirst()) addMaxID = cursor.getInt(0);
        cursor.close();
        // 减法题库最大id
        cursor = db.rawQuery("SELECT MAX(_id) FROM SubtractExercise", null);
        if(cursor.moveToFirst()) subMaxID = cursor.getInt(0);
        cursor.close();
        // 乘法题库最大id
        cursor = db.rawQuery("SELECT MAX(_id) FROM MultiplyExercise", null);
        if(cursor.moveToFirst()) mulMaxID = cursor.getInt(0);
        cursor.close();
        // 除法题库最大id
        cursor = db.rawQuery("SELECT MAX(_id) FROM DivisionExercise", null);
        if(cursor.moveToFirst()) divMaxID = cursor.getInt(0);
        cursor.close();
//        // 综合题库最大id
//        cursor = db.rawQuery("SELECT MAX(id) FROM ComplexExercise", null);
//        if(cursor.moveToFirst()) complexMaxID = cursor.getInt(0);
//        cursor.close();
    }

    private boolean checkAnswer(Pair<Integer, Integer> answer){
        boolean isTrue = true;
        boolean hasMod = answer.second != null;
        Integer ans, mod;
        if(!QuestionAndAnswer.res.getText().toString().equals(""))
            ans = Integer.parseInt(QuestionAndAnswer.res.getText().toString().trim());
        else
            ans = null;

        if(!QuestionAndAnswer.mod.getText().toString().equals(""))
            mod = Integer.parseInt(QuestionAndAnswer.mod.getText().toString().trim());
        else
            mod = null;

        Pair<Integer, Integer> appliedAnswer = new Pair<>(ans, mod);
        if(!Objects.equals(appliedAnswer.first, answer.first)){
            isTrue = false;
        }
        if(hasMod && !answer.second.equals(appliedAnswer.second)){
            isTrue = false;
        }


        if(isTrue){
            Toast.makeText(mainActivity, "回答正确", Toast.LENGTH_LONG)
                    .show();
            QuestionAndAnswer.res.setText("");
            QuestionAndAnswer.mod.setText("");
            return true;
        }else{
            AlertDialog.Builder wrongAnswer = new AlertDialog.Builder(mainActivity);
            if(hasMod){
                wrongAnswer.setTitle("答案错误，请重新作答")
                        .setMessage("正确答案是" + answer.first + "余" + answer.second + "\n"+
                                "而你的答案是" + appliedAnswer.first + "余" + appliedAnswer.second)
                        .create()
                        .show();
            }else{
                wrongAnswer.setTitle("答案错误，请重新作答")
                        .setMessage("正确答案是" + answer.first + "\n"+
                                "而你的答案是" + appliedAnswer.first)
                        .create()
                        .show();
            }
            return false;
        }
    }

    private Pair<Integer, Integer> nextQuestion(int qKind){
        // 初始化界面
        QuestionAndAnswer.modPoint.setVisibility(View.INVISIBLE);
        QuestionAndAnswer.mod.setVisibility(View.INVISIBLE);

        int selID = 0;
        String table = "";
        Random rand = new Random();
        switch (qKind){
            case ADD:
                table = "AddExercise";
                selID = rand.nextInt(addMaxID)+1;
                break;
            case SUB:
                table = "SubtractExercise";
                selID = rand.nextInt(subMaxID)+1;
                break;
            case MUL:
                table = "MultiplyExercise";
                selID = rand.nextInt(mulMaxID)+1;
                break;
            case DIV:
                QuestionAndAnswer.modPoint.setVisibility(View.VISIBLE);
                QuestionAndAnswer.mod.setVisibility(View.VISIBLE);
                table = "DivisionExercise";
                selID = rand.nextInt(divMaxID)+1;
                break;
            case COMPLEX:
                //todo 综合题，暂时没做
                break;
        }
        Cursor cursor = db.rawQuery("SELECT * FROM " + table + " WHERE _id == " + selID, null);
        cursor.moveToNext();
        int firstNum=0, secondNum=0, ans=0, mod=0;
        String nextQuestion;
        if(qKind != COMPLEX){
            firstNum = cursor.getInt(cursor.getColumnIndexOrThrow("firstNum"));
            secondNum = cursor.getInt(cursor.getColumnIndexOrThrow("secondNum"));
            ans = cursor.getInt(cursor.getColumnIndexOrThrow("ans"));
            if(qKind == DIV)
                mod = cursor.getInt(cursor.getColumnIndexOrThrow("mod"));
        }else{
            nextQuestion = cursor.getString(cursor.getColumnIndexOrThrow("question"));
        }

        switch (qKind){
            case ADD:
                QuestionAndAnswer.question.setText(firstNum + "+" + secondNum + "=");
                return new Pair<>(ans, null);
            case SUB:
                QuestionAndAnswer.question.setText(firstNum + "-" + secondNum + "=");
                return new Pair<>(ans, null);
            case MUL:
                QuestionAndAnswer.question.setText(firstNum + "×" + secondNum + "=");
                return new Pair<>(ans, null);
            case DIV:
                QuestionAndAnswer.question.setText(firstNum + "÷" + secondNum + "=");
                return new Pair<>(ans, mod);
            case COMPLEX:
                //todo 要改
                break;
        }
        return null;
    }
}