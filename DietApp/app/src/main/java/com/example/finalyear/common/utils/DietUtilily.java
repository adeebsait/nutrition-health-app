package com.example.finalyear.common.utils;

import android.content.Context;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

public class DietUtilily {
    public static void HideKeyBoard(Context context, ViewGroup viewGroup){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(viewGroup.getWindowToken(), 0);
    }
}
