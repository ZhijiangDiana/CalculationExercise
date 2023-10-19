package com.example.calculationexercises;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.sql.*;

import static com.example.calculationexercises.MainActivity.mainActivity;

public class ExerciseSQLiteOpenHelper extends SQLiteOpenHelper {
    public static int cnt = 0;
    private static SQLiteOpenHelper Instance;
    public static synchronized SQLiteOpenHelper getInstance(Context context){
        if(Instance == null)
            Instance = new ExerciseSQLiteOpenHelper(context, "ExerciseDB.db", null, 1);
        return Instance;
    }

    private ExerciseSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        db.execSQL("CREATE TABLE AddExercise(_id integer primary key autoincrement not null, " +
                "firstNum integer not null, " +
                "secondNum integer not null, " +
                "ans integer not null)");
        db.execSQL("CREATE TABLE SubtractExercise(_id integer primary key autoincrement not null, " +
                "firstNum integer not null, " +
                "secondNum integer not null, " +
                "ans integer not null)");
        db.execSQL("CREATE TABLE MultiplyExercise(_id integer primary key autoincrement not null, " +
                "firstNum integer not null, " +
                "secondNum integer not null, " +
                "ans integer not null)");
        db.execSQL("CREATE TABLE DivisionExercise(_id integer primary key autoincrement not null, " +
                "firstNum integer not null, " +
                "secondNum integer not null, " +
                "ans integer not null, " +
                "mod integer not null)");
//        db.execSQL("CREATE TABLE MultiplyExercise(_id integer primary key autoincrement not null, " +
//                "firstNum integer not null, " +
//                "secondNum integer not null, " +
//                "ans integer not null)");


        try {
            conn = DriverManager.getConnection("jdbc:mysql://114514/Calculation_Exercise?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "1919810");
            stmt = conn.createStatement();
            // 加法
            rs = stmt.executeQuery("SELECT * FROM AddExercise");
            while (rs.next()){
                int firstNum = rs.getInt("firstNum");
                int secondNum = rs.getInt("secondNum");
                int ans = rs.getInt("ans");
                db.execSQL("INSERT INTO AddExercise(firstNum, secondNum, ans) VALUES ('"+firstNum+"' ,'"+secondNum+"','"+ans+"')");
                Log.e("SQLiteInit", firstNum+"+"+secondNum+"="+ans);
                cnt++;
            }
            rs.close();
            // 减法
            rs = stmt.executeQuery("SELECT * FROM SubtractExercise");
            while (rs.next()){
                int firstNum = rs.getInt("firstNum");
                int secondNum = rs.getInt("secondNum");
                int ans = rs.getInt("ans");
                db.execSQL("INSERT INTO SubtractExercise(firstNum, secondNum, ans) VALUES ('"+firstNum+"' ,'"+secondNum+"','"+ans+"')");
                Log.e("SQLiteInit", firstNum+"-"+secondNum+"="+ans);
                cnt++;
            }
            rs.close();
            // 乘法
            rs = stmt.executeQuery("SELECT * FROM MultiplyExercise");
            while (rs.next()){
                int firstNum = rs.getInt("firstNum");
                int secondNum = rs.getInt("secondNum");
                int ans = rs.getInt("ans");
                db.execSQL("INSERT INTO MultiplyExercise(firstNum, secondNum, ans) VALUES ('"+firstNum+"' ,'"+secondNum+"','"+ans+"')");
                Log.e("SQLiteInit", firstNum+"*"+secondNum+"="+ans);
                cnt++;
            }
            rs.close();
            // 除法
            rs = stmt.executeQuery("SELECT * FROM DivisionExercise");
            while (rs.next()){
                int firstNum = rs.getInt("firstNum");
                int secondNum = rs.getInt("secondNum");
                int ans = rs.getInt("ans");
                int mod = rs.getInt("mod");
                db.execSQL("INSERT INTO DivisionExercise(firstNum, secondNum, ans, mod) VALUES ('"+firstNum+"' ,'"+secondNum+"','"+ans+"','"+mod+"')");
                Log.e("SQLiteInit", firstNum+"/"+secondNum+"="+ans+"······"+mod);
                cnt++;
            }
            rs.close();
//            // 综合运算
//            rs = stmt.executeQuery("SELECT * FROM MultiplyExercise");
//            while (rs.next()){
//                int firstNum = rs.getInt("firstNum");
//                int secondNum = rs.getInt("secondNum");
//                int ans = rs.getInt("ans");
//                db.execSQL("INSERT INTO MultiplyExercise(firstNum, secondNum, ans) VALUES ('"+firstNum+"' ,'"+secondNum+"','"+ans+"')");
//                Log.e("SQLiteInit", firstNum+"*"+secondNum+"="+ans);
//            }
//            rs.close();
        } catch (SQLException e) {
            //todo 未知问题，在这里写弹窗就一闪而过
//            e.printStackTrace();
//            AlertDialog.Builder al = new AlertDialog.Builder(mainActivity);
//            al.setTitle("连接远程数据库出错，请检查互联网");
//            al.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    System.exit(0);
//                }
//            });
//            al.setMessage(e.getMessage())
//                    .create()
//                    .show();
            System.exit(1);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
