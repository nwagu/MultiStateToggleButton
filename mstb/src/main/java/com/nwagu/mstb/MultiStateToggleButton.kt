package com.nwagu.mstb

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import java.util.*

class MultiStateToggleButton : ToggleButton {

    private var buttons: MutableList<View>? = null

    var texts: Array<CharSequence>? = null
        internal set

    private var mMultipleChoice = false

    private var mainLayout: LinearLayout? = null

    val value: Int
        get() {
            for (i in this.buttons!!.indices) {
                if (buttons!![i].isSelected) {
                    return i
                }
            }
            return -1
        }

    var states: BooleanArray?
        get() {
            val size = if (this.buttons == null) 0 else this.buttons!!.size
            val result = BooleanArray(size)
            for (i in 0 until size) {
                result[i] = this.buttons!![i].isSelected
            }
            return result
        }
        set(selected) {
            if (this.buttons == null || selected == null ||
                this.buttons!!.size != selected.size
            ) {
                return
            }
            var count = 0
            for (b in this.buttons!!) {
                setButtonState(b, selected[count])
                count++
            }
        }

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.MultiStateToggleButton, 0, 0)
        try {
            val texts = a.getTextArray(R.styleable.MultiStateToggleButton_values)
            colorPressed = a.getColor(R.styleable.MultiStateToggleButton_mstbPrimaryColor, 0)
            colorNotPressed = a.getColor(R.styleable.MultiStateToggleButton_mstbSecondaryColor, 0)
            colorPressedText = a.getColor(R.styleable.MultiStateToggleButton_mstbColorPressedText, 0)
            colorPressedBackground = a.getColor(R.styleable.MultiStateToggleButton_mstbColorPressedBackground, 0)
            pressedBackgroundResource =
                a.getResourceId(
                    R.styleable.MultiStateToggleButton_mstbColorPressedBackgroundResource,
                    0
                )

            colorNotPressedText = a.getColor(R.styleable.MultiStateToggleButton_mstbColorNotPressedText, 0)
            colorNotPressedBackground = a.getColor(R.styleable.MultiStateToggleButton_mstbColorNotPressedBackground, 0)
            notPressedBackgroundResource = a.getResourceId(R.styleable.MultiStateToggleButton_mstbColorNotPressedBackgroundResource, 0)

            var length = 0
            if (texts != null) {
                length = texts.size
            }
            setElements(texts, null, BooleanArray(length))
        } finally {
            a.recycle()
        }
    }

    /**
     * If multiple choice is enabled, the user can select multiple
     * values simultaneously.
     *
     * @param enable
     */
    fun enableMultipleChoice(enable: Boolean) {
        this.mMultipleChoice = enable
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(KEY_INSTANCE_STATE, super.onSaveInstanceState())
        bundle.putBooleanArray(KEY_BUTTON_STATES, states)
        return bundle
    }

    public override fun onRestoreInstanceState(state: Parcelable?) {
        var state_ = state
        if (state_ is Bundle) {
            val bundle = state_ as Bundle?
            states = bundle!!.getBooleanArray(KEY_BUTTON_STATES)
            state_ = bundle.getParcelable(KEY_INSTANCE_STATE)
        }
        super.onRestoreInstanceState(state_)
    }

    /**
     * Set the enabled state of this MultiStateToggleButton, including all of its child buttons.
     *
     * @param enabled True if this view is enabled, false otherwise.
     */
    override fun setEnabled(enabled: Boolean) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.isEnabled = enabled
        }
    }

    private fun setElements(texts: Array<CharSequence>?, imageResourceIds: IntArray?, selected: BooleanArray?) {
        this.texts = texts
        val textCount = texts?.size ?: 0
        val iconCount = imageResourceIds?.size ?: 0
        val elementCount = Math.max(textCount, iconCount)
        if (elementCount == 0) {
            return
        }

        var enableDefaultSelection = true
        if (selected == null || elementCount != selected.size) {
            enableDefaultSelection = false
        }

        //setOrientation(LinearLayout.HORIZONTAL);
        gravity = Gravity.CENTER_VERTICAL

        val inflater = getContext()
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (mainLayout == null) {
            mainLayout = inflater.inflate(R.layout.view_multi_state_toggle_button, this, true) as LinearLayout
        }
        mainLayout!!.removeAllViews()

        this.buttons = ArrayList(elementCount)
        for (i in 0 until elementCount) {
            val b: Button
            if (i == 0) {
                // Add a special view when there's only one element
                if (elementCount == 1) {
                    b = inflater.inflate(R.layout.view_single_toggle_button, mainLayout, false) as Button
                } else {
                    b = inflater.inflate(R.layout.view_left_toggle_button, mainLayout, false) as Button
                }
            } else if (i == elementCount - 1) {
                b = inflater.inflate(R.layout.view_right_toggle_button, mainLayout, false) as Button
            } else {
                b = inflater.inflate(R.layout.view_center_toggle_button, mainLayout, false) as Button
            }
            b.text = if (texts != null) texts[i] else ""
            if (imageResourceIds != null && imageResourceIds[i] != 0) {
                b.setCompoundDrawablesWithIntrinsicBounds(imageResourceIds[i], 0, 0, 0)
            }
            b.setOnClickListener { setValue(i) }
            mainLayout!!.addView(b)
            if (enableDefaultSelection) {
                setButtonState(b, selected!![i])
            }
            this.buttons!!.add(b)
        }
        mainLayout!!.setBackgroundResource(R.drawable.button_section_shape)
    }

    /**
     * Set multiple buttons with the specified texts and default
     * initial values. Initial states are allowed, but both
     * arrays must be of the same size.
     *
     * @param buttons  the array of button views to use
     * @param selected The default value for the buttons
     */
    fun setButtons(buttons: Array<View>, selected: BooleanArray?) {
        val elementCount = buttons.size
        if (elementCount == 0) {
            return
        }

        var enableDefaultSelection = true
        if (selected == null || elementCount != selected.size) {
            Log.d(TAG, "Invalid selection array")
            enableDefaultSelection = false
        }

        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER_VERTICAL

        val inflater = getContext()
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (mainLayout == null) {
            mainLayout = inflater.inflate(R.layout.view_multi_state_toggle_button, this, true) as LinearLayout
        }
        mainLayout!!.removeAllViews()

        this.buttons = ArrayList()
        for (i in 0 until elementCount) {
            val b = buttons[i]
            b.setOnClickListener { setValue(i) }
            mainLayout!!.addView(b)
            if (enableDefaultSelection) {
                setButtonState(b, selected!![i])
            }
            this.buttons!!.add(b)
        }
        mainLayout!!.setBackgroundResource(R.drawable.button_section_shape)
    }

    fun setElements(elements: Array<CharSequence>?) {
        val size = elements?.size ?: 0
        setElements(elements, null, BooleanArray(size))
    }

    fun setElements(elements: List<*>?) {
        val size = elements?.size ?: 0
        setElements(elements, BooleanArray(size))
    }

    fun setElements(elements: List<*>?, selected: Any) {
        var size = 0
        var index = -1
        if (elements != null) {
            size = elements.size
            index = elements.indexOf(selected)
        }
        val selectedArray = BooleanArray(size)
        if (index != -1 && index < size) {
            selectedArray[index] = true
        }
        setElements(elements, selectedArray)
    }

    private fun setElements(texts: List<*>?, selected: BooleanArray) {
        var texts_ = texts
        if (texts_ == null) {
            texts_ = arrayListOf(String)
        }
        val size = texts_.size
        setElements(texts_.toTypedArray<String>(), null, selected)
    }

    fun setElements(arrayResourceId: Int, selectedPosition: Int) {
        // Get resources
        val elements = this.resources.getStringArray(arrayResourceId)

        // Set selected boolean array
        val size = elements.size ?: 0
        val selected = BooleanArray(size)
        if (selectedPosition in 0..(size - 1)) {
            selected[selectedPosition] = true
        }

        // Super
        setElements(elements, null, selected)
    }

    fun setElements(arrayResourceId: Int, selected: BooleanArray) {
        setElements(this.resources.getStringArray(arrayResourceId), null, selected)
    }

    fun setButtonState(button: View?, selected: Boolean) {
        if (button == null) {
            return
        }
        button.isSelected = selected
        button.setBackgroundResource(if (selected) R.drawable.button_pressed else R.drawable.button_not_pressed)
        if (colorPressed != 0 || colorNotPressed != 0) {
            button.setBackgroundColor(if (selected) colorPressed else colorNotPressed)
        } else if (colorPressedBackground != 0 || colorNotPressedBackground != 0) {
            button.setBackgroundColor(if (selected) colorPressedBackground else colorNotPressedBackground)
        }
        if (button is Button) {
            val style = if (selected) R.style.WhiteBoldText else R.style.PrimaryNormalText
            (button as AppCompatButton).setTextAppearance(this.getContext(), style)
            if (colorPressed != 0 || colorNotPressed != 0) {
                button.setTextColor(if (!selected) colorPressed else colorNotPressed)
            }
            if (colorPressedText != 0 || colorNotPressedText != 0) {
                button.setTextColor(if (selected) colorPressedText else colorNotPressedText)
            }
            if (pressedBackgroundResource != 0 || notPressedBackgroundResource != 0) {
                button.setBackgroundResource(if (selected) pressedBackgroundResource else notPressedBackgroundResource)
            }
        }
    }

    override fun setValue(position: Int) {
        for (i in this.buttons!!.indices) {
            if (mMultipleChoice) {
                if (i == position) {
                    val b = buttons!![i]
                    setButtonState(b, !b.isSelected)
                }
            } else {
                if (i == position) {
                    setButtonState(buttons!![i], true)
                } else if (!mMultipleChoice) {
                    setButtonState(buttons!![i], false)
                }
            }
        }
        super.setValue(position)
    }

    /**
     * {@inheritDoc}
     */
    override fun setColors(colorPressed: Int, colorNotPressed: Int) {
        super.setColors(colorPressed, colorNotPressed)
        refresh()
    }

    private fun refresh() {
        val states = states
        for (i in states.indices) {
            setButtonState(buttons!![i], states[i])
        }
    }

    companion object {

        private val TAG = MultiStateToggleButton::class.java.simpleName

        private val KEY_BUTTON_STATES = "button_states"
        private val KEY_INSTANCE_STATE = "instance_state"
    }
}
