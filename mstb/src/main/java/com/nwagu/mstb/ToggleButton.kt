package com.nwagu.mstb

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

abstract class ToggleButton : LinearLayout {

    internal var listener: OnValueChangedListener? = null
    internal var context: Context
    @ColorInt
    internal var colorPressed: Int = 0
    @ColorInt
    internal var colorNotPressed: Int = 0
    internal var colorPressedText: Int = 0
    internal var colorPressedBackground: Int = 0
    internal var colorNotPressedText: Int = 0
    internal var colorNotPressedBackground: Int = 0
    internal var pressedBackgroundResource: Int = 0
    internal var notPressedBackgroundResource: Int = 0

    interface OnValueChangedListener {
        // TODO: Add this callback:
        // public void onValueChanged(int value, boolean selected);
        fun onValueChanged(value: Int)
    }

    constructor(context: Context) : super(context, null) {
        this.context = context
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.context = context
    }

    fun setOnValueChangedListener(l: OnValueChangedListener?) {
        this.listener = l
    }

    open fun setValue(value: Int) {
        if (this.listener != null) {
            listener!!.onValueChanged(value)
        }
    }


    fun setColorRes(@ColorRes colorPressed: Int, @ColorRes colorNotPressed: Int) {
        setColors(ContextCompat.getColor(context, colorPressed), ContextCompat.getColor(context, colorNotPressed))
    }

    open fun setColors(@ColorInt colorPressed: Int, @ColorInt colorNotPressed: Int) {
        this.colorPressed = colorPressed
        this.colorNotPressed = colorNotPressed
    }

    fun setPressedColorsRes(@ColorRes colorPressedText: Int, @ColorRes colorPressedBackground: Int) {
        setPressedColors(
            ContextCompat.getColor(context, colorPressedText),
            ContextCompat.getColor(context, colorPressedBackground)
        )
    }

    fun setPressedColors(@ColorInt colorPressedText: Int, @ColorInt colorPressedBackground: Int) {
        this.colorPressedText = colorPressedText
        this.colorPressedBackground = colorPressedBackground
    }

    fun setNotPressedColorRes(@ColorRes colorNotPressedText: Int, @ColorRes colorNotPressedBackground: Int) {
        setNotPressedColors(
            ContextCompat.getColor(context, colorNotPressedText),
            ContextCompat.getColor(context, colorNotPressedBackground)
        )
    }

    fun setNotPressedColors(colorNotPressedText: Int, colorNotPressedBackground: Int) {
        this.colorNotPressedText = colorNotPressedText
        this.colorNotPressedBackground = colorNotPressedBackground
    }

    fun setBackgroundResources(@DrawableRes pressedBackgroundResource: Int, @DrawableRes notPressedBackgroundResource: Int) {
        this.pressedBackgroundResource = pressedBackgroundResource
        this.notPressedBackgroundResource = notPressedBackgroundResource
    }

    fun setForegroundColorsRes(@ColorRes colorPressedText: Int, @ColorRes colorNotPressedText: Int) {
        setForegroundColors(
            ContextCompat.getColor(context, colorPressedText),
            ContextCompat.getColor(context, colorNotPressedText)
        )
    }

    private fun setForegroundColors(colorPressedText: Int, colorNotPressedText: Int) {
        this.colorPressedText = colorPressedText
        this.colorNotPressedText = colorNotPressedText
    }
}