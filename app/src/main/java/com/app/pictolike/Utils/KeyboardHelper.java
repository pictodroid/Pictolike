package com.app.pictolike.Utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Alexandr on 10/22/2014.
 */
public final class KeyboardHelper {
    /**
     * Hides keyboard
     * @param pContext
     * @param pFocus
     */
    public static void hideKeyboard(Context pContext,View pFocus){
        if (pFocus!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) pContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(pFocus.getWindowToken(), 0);
        }
    }
}
