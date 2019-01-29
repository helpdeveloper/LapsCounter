package br.com.helpdev.lapscounter.widget

import android.content.Context
import android.content.res.TypedArray
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.View
import android.widget.NumberPicker
import br.com.helpdev.lapscounter.R
import java.util.regex.Pattern
import android.content.DialogInterface


class FloatPickerPreference(context: Context?, attrs: AttributeSet?) : DialogPreference(context, attrs) {

    private var initialValue = 10f
    private var pk1: NumberPicker? = null
    private var pk2: NumberPicker? = null

    init {
        dialogLayoutResource = R.layout.widget_float_picker_preference
        setPositiveButtonText(android.R.string.ok)
        setNegativeButtonText(android.R.string.cancel)
    }

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)
        pk1 = view.findViewById(R.id.num_picker_1)
        pk2 = view.findViewById(R.id.num_picker_2)
        if (null != pk1) {
            pk1!!.minValue = 1
            pk1!!.maxValue = 999
            pk1!!.value = initialValue.toInt()
        }
        if (null != pk2) {
            pk2!!.minValue = 0
            pk2!!.maxValue = 99
            pk2!!.value = floatPointToInt(initialValue)
        }
    }

    private fun floatPointToInt(value: Float): Int {
        return value.toString().split('.')[1].toInt()
    }

    override fun onGetDefaultValue(a: TypedArray?, index: Int): Float {
        val def = context.resources.getInteger(R.integer.pref_lap_distance_default_value).toFloat()
        return a?.getFloat(index, def) ?: def
    }


    override fun onClick(dialog: DialogInterface, which: Int) {
        super.onClick(dialog, which)
        if (which == DialogInterface.BUTTON_POSITIVE) {
            initialValue = String.format("%d.%d", pk1?.value ?: 0, pk2?.value ?: 0).toFloat()
            persistFloat(initialValue)
            callChangeListener(initialValue)
        }
    }

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {
        super.onSetInitialValue(restorePersistedValue, defaultValue)

        val def = (defaultValue as? Number)?.toFloat()
                ?: (defaultValue?.toString()?.toFloat()
                        ?: context.resources.getInteger(R.integer.pref_lap_distance_default_value).toFloat())

        if (restorePersistedValue) {
            this.initialValue = getPersistedFloat(def)
        } else
            this.initialValue = defaultValue as Float
    }
}