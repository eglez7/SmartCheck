package app.smartdevelop.smartcheck.ui.detail

import android.annotation.SuppressLint
import android.os.Looper
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.smartdevelop.smartcheck.R
import app.smartdevelop.smartcheck.databinding.ViewDetailBinding
import app.smartdevelop.smartcheck.inflate
import app.smartdevelop.smartcheck.model.Checklistdb
import app.smartdevelop.smartcheck.model.Details
import app.smartdevelop.smartcheck.model.getDatabase
import app.smartdevelop.smartcheck.ui.dialogConfirm
import app.smartdevelop.smartcheck.ui.dialogEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("NotifyDataSetChanged")
class DetailAdapter(private var items: List<Details>, private val currentList:Int) :
    RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    private fun updateItems(newItems: List<Details>) {
        items = newItems
        android.os.Handler(Looper.getMainLooper()).post { notifyDataSetChanged() }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.view_detail)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener {

        private val binding = ViewDetailBinding.bind(view)
        private lateinit var currentItem: Details
        val database: Checklistdb = getDatabase(itemView.context)

        fun bind(listItem: Details) {
            with(binding) {
                currentItem = listItem
                checkBox.isChecked = listItem.selected
                checkBox.text = listItem.detail
                checkBox.setOnClickListener() {
                    CoroutineScope(Dispatchers.IO).launch {
                        updateCheckbox(listItem)
                    }
                }
                checkBox.setOnCreateContextMenuListener(this@ViewHolder)
            }
        }

        override fun onCreateContextMenu(
            menu: ContextMenu,
            v: View,
            menuInfo: ContextMenu.ContextMenuInfo?,
        ) {
            menu.add(R.string.edit).setOnMenuItemClickListener(onEditMenu)
            menu.add(R.string.delete).setOnMenuItemClickListener(onDeleteMenu)
        }

        private val onEditMenu = MenuItem.OnMenuItemClickListener {

            dialogEditText(
                itemView.context, binding.checkBox.text as String,R.string.edit_detail,R.string.save,R.string.cancel
            ) { result ->
                if (result != "") updateListActivity(result)
            }

            true
        }

        private val onDeleteMenu = MenuItem.OnMenuItemClickListener {

            dialogConfirm(
                itemView.context,
                R.string.text_confirm_title_delete,
                R.string.text_confirm_message
            ) { result ->
                if (result == "yes") {
                    CoroutineScope(Dispatchers.IO).launch {
                        deleteDetail()
                        updateItemsAdapter()
                    }
                }
            }

            true
        }

        private suspend fun updateCheckbox(detail:Details){
            database.detailsDao().update(
                Details(
                    detail.id,
                    detail.detail,
                    !detail.selected,
                    detail.listId
                )
            )
        }

        private suspend fun deleteDetail() {
            database.detailsDao().delete(currentItem)
        }

        private fun updateListActivity(updatedItemName: String) {
            CoroutineScope(Dispatchers.IO).launch {
                updateList(updatedItemName)
                updateItemsAdapter()
            }
        }

        private suspend fun updateList(updatedItemName: String){
            database.detailsDao().update(
                Details(
                    currentItem.id,
                    updatedItemName,
                    currentItem.selected,
                    currentItem.listId
                )
            )
        }

        private suspend fun updateItemsAdapter() {
            val updateDetails = database.detailsDao().getDetailsById(currentList)
            updateItems(updateDetails)
        }

    }

}

