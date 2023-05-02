package app.smartdevelop.smartcheck.ui

import android.content.Context
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment



fun Fragment.dialogText(
    context: Context,
    textTitle: String,
    textCreate: String,
    textCancel: String,
    onResult: (String) -> Unit
) {

    val input = EditText(context)
    val dialog = AlertDialog.Builder(context)
        .setTitle(textTitle)
        .setView(input)
        .setPositiveButton(textCreate) { _, _ ->
            onResult(input.text.toString())
        }
        .setNegativeButton(
            textCancel
        ) { _, _ ->
            onResult("")
        }
        .create()
    dialog.show()
}




