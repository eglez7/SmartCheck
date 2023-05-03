package app.smartdevelop.smartcheck.ui.list

import android.annotation.SuppressLint
import android.os.Looper
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.smartdevelop.smartcheck.OnItemClickListener
import app.smartdevelop.smartcheck.R
import app.smartdevelop.smartcheck.databinding.ViewListBinding
import app.smartdevelop.smartcheck.inflate
import app.smartdevelop.smartcheck.model.Checklistdb
import app.smartdevelop.smartcheck.model.Checklists
import app.smartdevelop.smartcheck.model.Details
import app.smartdevelop.smartcheck.model.getDatabase
import app.smartdevelop.smartcheck.ui.dialogConfirm
import app.smartdevelop.smartcheck.ui.dialogEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListAdapter(private var items: List<Checklists> = emptyList(), private val listener: OnItemClickListener) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    private fun updateItems(newItems: List<Checklists>) {
        items = newItems
        android.os.Handler(Looper.getMainLooper()).post { notifyDataSetChanged() }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        val view = parent.inflate(R.layout.view_list)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener.onItemClick(item.id)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnCreateContextMenuListener {

        private val binding = ViewListBinding.bind(view)
        private lateinit var currentItem: Checklists
        var database: Checklistdb

        init {
            itemView.setOnCreateContextMenuListener(this)
            database = getDatabase(itemView.context)
        }

        fun bind(listItem: Checklists) {
            currentItem = listItem
            with(binding) {
                itemList.text = listItem.name
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
                itemView.context, binding.itemList.text as String,R.string.edit_checklist,R.string.save,R.string.cancel
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
                        deleteList()
                        updateItemsAdapter()
                    }
                }
            }

            true
        }

        private fun updateListActivity(updatedChecklistName: String) {
            CoroutineScope(Dispatchers.IO).launch {
                updateList(updatedChecklistName)
                updateItemsAdapter()
            }
        }

        private suspend fun updateList(updatedChecklistName: String) {
            database.checklistsDao().update(
                Checklists(currentItem.id, updatedChecklistName)
            )
        }

        private suspend fun updateItemsAdapter() {
            val updatedChecklists = database.checklistsDao().getChecklists()
            updateItems(updatedChecklists)
        }

        private suspend fun deleteList() {
            val dependientes: List<Details> =
                database.detailsDao().getDetailsById(currentItem.id)
            database.detailsDao().deleteAll(dependientes)
            database.checklistsDao().delete(currentItem)
        }


    }

}