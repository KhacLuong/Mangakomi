package com.example.mangakomi.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.text.Normalizer;
import java.util.regex.Pattern;


public class GlobalFunction {
    public static void startActivity(Context context, Class<?> clz ){
        Intent intent = new Intent(context, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void startActivity(Context context, Class<?> clz, String keyName, String value ){

        Intent intent = new Intent(context, clz);
        intent.putExtra(keyName, value);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void startActivity(Context context, Class<?> clz,String keyAction, String valueAction, String keyName, String value ){

        Intent intent = new Intent(context, clz);
        intent.putExtra(keyAction, valueAction);
        intent.putExtra(keyName, value);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void startActivity(Context context, Class<?> clz, Bundle bundle){
        Intent intent = new Intent(context, clz);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void  hideSoftKeyboard(Activity activity){
        try{
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }
    public static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static  String getTextSearch(String input){
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
//        Pattern pattern = Pattern.compile("\\p{IsM}+");

        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }



}
