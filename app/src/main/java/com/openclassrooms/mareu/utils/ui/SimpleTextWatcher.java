package com.openclassrooms.mareu.utils.ui;

import android.text.TextWatcher;

/**
 * Simplifiez TextWatcher en supprimant les méthodes beforeTextChanged et onTextChanged jamais utilisées
 */
public abstract class SimpleTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
