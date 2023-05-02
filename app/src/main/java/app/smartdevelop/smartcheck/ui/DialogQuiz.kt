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
): String {

    lateinit var answer: String
    val input = EditText(context)
    val dialog = AlertDialog.Builder(context)
        .setTitle(textTitle)
        .setView(input)
        .setPositiveButton(textCreate) { _, _ ->
            answer = input.text.toString()
        }
        .setNegativeButton(
            textCancel
        ) { dialog, _ ->
            dialog.cancel()
            answer=""
        }
        .create()
    dialog.show()

    return answer
}



