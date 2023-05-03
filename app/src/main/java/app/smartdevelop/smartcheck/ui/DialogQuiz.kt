package app.smartdevelop.smartcheck.ui

import android.content.Context
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import app.smartdevelop.smartcheck.R

fun dialogNewText(
    context: Context,
    title: Int,
    create: Int,
    cancel: Int,
    onResult: (String) -> Unit
) {

    val input = EditText(context)
    val dialog = AlertDialog.Builder(context)
        .setTitle(title)
        .setView(input)
        .setPositiveButton(create) { _, _ ->
            onResult(input.text.toString())
        }
        .setNegativeButton(
            cancel
        ) { _, _ ->
            onResult("")
        }
        .create()
    dialog.show()
}

fun dialogEditText(
    context: Context,
    text: String,
    edit: Int,
    save: Int,
    cancel: Int,
    onResult: (String) -> Unit
){
    val input = EditText(context)
    input.setText(text)
    val dialog = android.app.AlertDialog.Builder(context)
        .setTitle(edit)
        .setView(input)
        .setPositiveButton(save) { _, _ ->
            onResult(input.text.toString())
        }
        .setNegativeButton(cancel) { dialog, _ ->
            onResult("")
            dialog.cancel()
        }
        .create()

    dialog.show()
}

fun dialogConfirm(
    context: Context,
    title: Int,
    message: Int,
    onResult: (String) -> Unit
    ){
    val builder = android.app.AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton(R.string.button_yes) { dialog, which ->
        onResult("yes")
    }
    builder.setNegativeButton(R.string.button_no) { dialog, which ->
    }
    builder.show()

}



