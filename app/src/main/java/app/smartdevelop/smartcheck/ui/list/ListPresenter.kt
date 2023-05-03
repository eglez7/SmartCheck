package app.smartdevelop.smartcheck.ui.list

import android.content.Context
import app.smartdevelop.smartcheck.OnItemClickListener
import app.smartdevelop.smartcheck.databinding.FragmentListBinding
import app.smartdevelop.smartcheck.model.Checklistdb
import app.smartdevelop.smartcheck.model.Checklists
import app.smartdevelop.smartcheck.model.getDatabase
import app.smartdevelop.smartcheck.ui.dialogNewText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ListPresenter(
    private val scope: CoroutineScope,
    private val context: Context,
    private val binding: FragmentListBinding,
    private val listener: OnItemClickListener
) {
    interface View {
        fun getListsDB()
        fun creatorList()
    }

    lateinit var adapter: List<Checklists>
    lateinit var database : Checklistdb

    private fun insertListDB(checklistName: String) {
        database = getDatabase(context)
        scope.launch {
            database.checklistsDao().insert(Checklists(name = checklistName))
            getListsDB()
        }
    }

    fun getListsDB() {
        database = getDatabase(context)
        scope.launch {
            adapter = database.checklistsDao().getChecklists()
            binding.recyclerList.adapter = ListAdapter(adapter, listener)
        }
    }

    fun creatorList(textTitle: Int, textCreate: Int, textCancel: Int) {
        dialogNewText(
            context, textTitle, textCreate, textCancel
        ) { result ->
            if (result != "") insertListDB(result)
        }
    }

}