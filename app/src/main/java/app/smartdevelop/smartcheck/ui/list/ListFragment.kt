package app.smartdevelop.smartcheck.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import app.smartdevelop.smartcheck.R
import app.smartdevelop.smartcheck.databinding.FragmentListBinding
import app.smartdevelop.smartcheck.model.Checklists

class ListFragment : Fragment(), ListPresenter.View {

    private var _binding: FragmentListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var presenter : ListPresenter
    lateinit var adapter : List<Checklists>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val listViewModel =
            ViewModelProvider(this).get(ListViewModel::class.java)

        _binding = FragmentListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //presenter = ListPresenter(requireContext().applicationContext, lifecycleScope, binding, adapter)

//        getListsDB()
//        binding.addList.setOnClickListener {
//            creatorList()
//        }

        listViewModel.text.observe(viewLifecycleOwner) {

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getListsDB() {
        presenter.getListsDB()
    }

    override fun creatorList() {
        presenter.creatorList(getString(R.string.add_checklist),getString(R.string.create),getString(
            R.string.cancel))
    }
}