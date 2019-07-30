package com.nwagu.multistatetogglebutton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.LinearLayout
import android.text.Spannable
import android.text.style.ImageSpan
import android.provider.SyncStateContract
import android.text.SpannableString
import android.view.View
import android.widget.Button
import com.nwagu.mstb.MultiStateToggleButton
import com.nwagu.mstb.ToggleButton

class MainActivity : AppCompatActivity() {

    lateinit var mstb: MultiStateToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setUpButton()
    }

    /*private fun setUpButton() {
        val buttons = arrayListOf<Button>()

        repeat(5) {

            val button = Button(this)

            button.setPadding(0, 0, 0, 0)
            if(false) {  // it == clientState) {
                val buttonLabel = SpannableString(" ")

                buttonLabel.setSpan(ImageSpan(this, R.mipmap.ic_launcher, ImageSpan.ALIGN_BOTTOM),
                        0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                button.text = buttonLabel;
            } else {
                button.text = it.toString()
            }
            buttons.add(button);
        }

        val buttonsArray: ArrayList<Button> //View[buttons.size()]
        buttonsArray = buttons
        mstb.setButtons(buttonsArray,  Boolean[buttonsArray.length])

        mstb.orientation = LinearLayout.HORIZONTAL;

        mstb.setOnValueChangedListener(null)
        mstb.setValue(0)

        mstb.setOnValueChangedListener(object: ToggleButton.OnValueChangedListener {

            override fun onValueChanged(value: Int) {

            }
        });
    }*/

}
