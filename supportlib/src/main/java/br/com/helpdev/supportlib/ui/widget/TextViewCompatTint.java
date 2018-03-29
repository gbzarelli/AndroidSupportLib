package br.com.grupocriar.swapandroid.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import br.com.grupocriar.swapandroid.R;


/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-27
 */

public class TextViewCompatTint extends AppCompatTextView {
    public TextViewCompatTint(Context context) {
        this(context, null);
    }

    public TextViewCompatTint(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public TextViewCompatTint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewCompatTint, defStyleAttr, 0);
        if (typedArray.hasValue(R.styleable.TextViewCompatTint_drawableTint)) {
            int color = typedArray.getColor(R.styleable.TextViewCompatTint_drawableTint, 0);
            changeColorDrawables(color, TextViewCompat.getCompoundDrawablesRelative(this));
            changeColorDrawables(color, getCompoundDrawables());
        }
        typedArray.recycle();

    }

    private void changeColorDrawables(int color, Drawable[] drawables) {
        for (Drawable drawable : drawables) {
            if (drawable == null) continue;
            drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }
}