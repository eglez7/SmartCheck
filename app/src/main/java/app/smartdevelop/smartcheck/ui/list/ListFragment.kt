package app.smartdevelop.smartcheck.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.smartdevelop.smartcheck.R
import app.smartdevelop.smartcheck.databinding.FragmentListBinding
import app.smartdevelop.smartcheck.model.Checklistdb
import app.smartdevelop.smartcheck.model.Checklists
import app.smartdevelop.smartcheck.model.DatabaseProvider
import app.smartdevelop.smartcheck.ui.dialogText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListFragment :  Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    lateinit var database: Checklistdb

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val listViewModel =
            ViewModelProvider(this).get(ListViewModel::class.java)

        _binding = FragmentListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        database = DatabaseProvider.getDatabase(requireContext().applicationContext)

        getListsDB()

        binding.addList.setOnClickListener {
            creatorList(getString(R.string.add_checklist),getString(R.string.create),getString(R.string.cancel))
        }

        listViewModel.text.observe(viewLifecycleOwner) {

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getListsDB() {
        CoroutineScope(Dispatchers.IO).launch {
            val adapter = database.checklistsDao().getChecklists()
            binding.recyclerList.adapter = ListAdapter(adapter)
        }
    }

    fun creatorList( textTitle:String, textCreate:String, textCancel:String) {

        val answer : String = dialogText(requireContext(),textTitle,textCreate,textCancel)

        val input = EditText(requireContext())
        val dialog = AlertDialog.Builder(requireContext())
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




    private fun insertListDB(checklistName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            database.checklistsDao().insert(Checklists(name = checklistName))
        }
    }
}