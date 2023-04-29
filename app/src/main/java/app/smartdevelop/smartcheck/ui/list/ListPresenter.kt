package app.smartdevelop.smartcheck.ui.list

import android.content.Context
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import app.smartdevelop.smartcheck.databinding.FragmentListBinding
import app.smartdevelop.smartcheck.model.Checklists
import app.smartdevelop.smartcheck.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ListPresenter(private val context: Context, private val scope: CoroutineScope, val binding: FragmentListBinding, var adapter: List<Checklists>) {
    interface View {
        fun getListsDB()
        fun creatorList()
    }

    private fun insertListDB(checklistName: String) {
        scope.launch {
            MainActivity.room.checklistsDao().insert(Checklists(name = checklistName))
            getListsDB()
        }
    }

    fun getListsDB() {
        scope.launch {
//            adapter = MainActivity.room.checklistsDao().getChecklists()
            //binding.recyclerList.adapter = adapter
        }

    }

    fun creatorList( textTitle:String, textCreate:String, textCancel:String) {
        val input = EditText(context)
        val dialog = AlertDialog.Builder(context)
            .setTitle(textTitle)
            .setView(input)
            .setPositiveButton(textCreate) { _, _ ->
                val checklistName = input.text.toString()
                if (checklistName.isNotEmpty()) {
                    insertListDB(checklistName)
                }
            }
            .setNegativeButton(
                textCancel
            ) { dialog, _ ->
                dialog.cancel()
            }
            .create()
        dialog.show()
    }


}

