package com.alpha.team.eventhub.utils;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;

import com.alpha.team.eventhub.R;

/**
 * Created by clasence on 27,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 * Validate EditText inputs
 */
public class Validation {

    private static String passwordError, emailError, required;
    private  Context context;

    public Validation(Context context) {
        this.context = context;
        passwordError = context.getResources().getString(R.string.passwordErr);
        emailError  = context.getResources().getString(R.string.emailErr);
        required = context.getResources().getString(R.string.required);
    }


    /**
     * Check if input to editText is valid
     * @param editText
     * @return
     */
    public  boolean isEmailAddress(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);
        if (!hasText(editText))
            return false;

        //check email address patterns
        if (Patterns.EMAIL_ADDRESS.matcher(text).matches()) {

            return true;
        }

        editText.setError(context.getString(R.string.emailErr));
        return false;
    }


    /**
     * Check if EditText has text
     * @param editText
     * @return
     */
    public  boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(required);
            editText.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Check if password is valid
     * @param editText
     * @return
     */

    public  boolean isPasswordValid(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        //valid password is beyond 4
        if (text.length() < 4) {
            editText.setError(passwordError);
            editText.requestFocus();
            return false;
        }

        return true;
    }
}
