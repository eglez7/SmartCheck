package app.smartdevelop.smartcheck.ui.detail

import android.content.Context
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import app.smartdevelop.smartcheck.databinding.FragmentDetailBinding
import app.smartdevelop.smartcheck.model.Checklistdb
import app.smartdevelop.smartcheck.model.Details
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailPresenter(
    val database: Checklistdb,
    private val scope: CoroutineScope,
    private val context: Context,
    private val binding: FragmentDetailBinding,
)  {
    interface View {
        fun creatorDetail()
        fun getDetailsDB()
        fun uncheckedAll()
    }

    fun creatorDetail(itemId:Int, textTitle:String, textCreate:String, textCancel:String) {
        val input = EditText(context)
        val dialog = AlertDialog.Builder(context)
            .setTitle(textTitle)
            .setView(input)
            .setPositiveButton(textCreate) { _, _ ->
                val detailText = input.text.toString()
                insertDetailDB(detailText, itemId)
            }
            .setNegativeButton(
                textCancel
            ) { dialog, _ ->
                dialog.cancel()
            }
            .create()

        dialog.show()
    }

    private fun insertDetailDB(detailText: String, itemId:Int) {
        scope.launch {
            database.detailsDao()
                .insert(Details(detail = detailText, selected = false, listId = itemId))
            getDetailsDB(itemId)
        }
    }

    fun getDetailsDB(itemId: Int) {
        scope.launch {
            val items = withContext(Dispatchers.IO) {
                database.detailsDao().getDetailsById(itemId)
            }
            binding.recyclerDetail.adapter = DetailAdapter(items, itemId)
        }
    }

    fun uncheckedAll(itemId : Int) {
        scope.launch {
            (database.detailsDao().getDetailsById(itemId)).forEach() {
                it.selected = false
                database.detailsDao().update(it)
            }
            getDetailsDB(itemId)
        }
    }

}