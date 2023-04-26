package app.smartdevelop.smartcheck.ui.detail

import android.annotation.SuppressLint
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import app.smartdevelop.smartcheck.R
import app.smartdevelop.smartcheck.databinding.ViewDetailBinding
import app.smartdevelop.smartcheck.inflate
import app.smartdevelop.smartcheck.model.Details
import app.smartdevelop.smartcheck.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@SuppressLint("NotifyDataSetChanged")
class DetailAdapter(
    items: List<Details>,
) : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    var items: List<Details> by Delegates.observable(items){ _, _, _, ->
        notifyDataSetChanged()
    }

    // DEVUELVE EL NÚMERO DE items DE LA LISTA
    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.view_detail)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view),  View.OnCreateContextMenuListener {
        private val binding = ViewDetailBinding.bind(view)
        private lateinit var itemNow : Details

        fun bind(listItem: Details) {
            with(binding) {
                itemNow=listItem
                checkBox.isChecked=listItem.selected
                checkBox.text = listItem.detail
                checkBox.setOnClickListener(){
                    CoroutineScope(Dispatchers.IO).launch {
                        MainActivity.room.detailsDao().update(
                            Details(
                                listItem.id,
                                listItem.detail,
                                !listItem.selected,
                                listItem.listId
                            )
                        )
                    }
                }
                checkBox.setOnCreateContextMenuListener(this@ViewHolder)
            }
        }

        override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu.add(R.string.edit).setOnMenuItemClickListener(onEditMenu)
            menu.add(R.string.delete).setOnMenuItemClickListener(onDeleteMenu)
        }

        private val onEditMenu = MenuItem.OnMenuItemClickListener {
            val input = EditText(itemView.context)
            input.setText(binding.checkBox.text)

            val dialog = AlertDialog.Builder(itemView.context)
                .setTitle(R.string.edit_checklist)
                .setView(input)
                .setPositiveButton(R.string.save, { _, _ ->
                    val updatedChecklistName = input.text.toString()
                    if (updatedChecklistName.isNotEmpty()) {
                        updateListActivity(updatedChecklistName)
                    }
                })
                .setNegativeButton(R.string.cancel, { dialog, _ ->
                    dialog.cancel()
                })
                .create()
            dialog.show()
            true
        }

        private val onDeleteMenu = MenuItem.OnMenuItemClickListener {

                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("Confirmación")
                builder.setMessage("¿Estás seguro de que quieres continuar?")
                builder.setPositiveButton("Sí") { dialog, which ->
                    CoroutineScope(Dispatchers.IO).launch {
                        MainActivity.room.detailsDao().delete(itemNow)
                    }
                }
                builder.setNegativeButton("No") { dialog, which ->
                }
                builder.show()


            true
        }
        private fun updateListActivity(updatedChecklistName: String) {
            CoroutineScope(Dispatchers.IO).launch {
                MainActivity.room.detailsDao().update(
                    Details(itemNow.id, updatedChecklistName,itemNow.selected,itemNow.listId)
                )
            }
        }

    }

}

