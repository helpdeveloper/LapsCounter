package br.com.helpdev.lapscounter.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.view.LayoutInflater
import android.view.View
import br.com.helpdev.lapscounter.R

class SaveActivityDialog(context: Context) : AlertDialog(context), View.OnClickListener, DialogInterface.OnShowListener {

    private lateinit var callback: (name: String, description: String) -> Unit
    private var editActivityName: TextInputEditText
    private var editActivityDescription: TextInputEditText
    private var layoutName: TextInputLayout
    private var layoutDescription: TextInputLayout

    init {
        setOnShowListener(this)
        setTitle(R.string.app_name)
        setMessage(context.getString(R.string.save_activity_message))
        val customView = LayoutInflater.from(context).inflate(R.layout.save_activity_dialog, null)
        editActivityName = customView.findViewById(R.id.edit_activity_name)
        editActivityDescription = customView.findViewById(R.id.edit_activity_description)
        layoutName = customView.findViewById(R.id.text_input_layout_name)
        layoutDescription = customView.findViewById(R.id.text_input_layout_description)
        setView(customView)
        setButton(DialogInterface.BUTTON_POSITIVE, context.getString(android.R.string.ok)) { _, _ -> }
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(android.R.string.cancel)) { _, _ -> dismiss() }
    }

    override fun onShow(dialog: DialogInterface?) {
        val positiveButton = getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.id = DialogInterface.BUTTON_POSITIVE
        positiveButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val name = editActivityName.text.toString().trim()
        val description = editActivityDescription.text.toString().trim()
        if (checkInputName(name)) callback.invoke(name, description)
    }

    private fun checkInputName(name: String): Boolean {
        if (name.isEmpty()) {
            layoutName.error = context.getString(R.string.error_empty_name)
            return true
        }
        return false
    }

    fun show(callback: (name: String, description: String) -> Unit) {
        this.callback = callback
        super.show()
    }

}