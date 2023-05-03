package app.smartdevelop.smartcheck.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import app.smartdevelop.smartcheck.OnItemClickListener
import app.smartdevelop.smartcheck.R
import app.smartdevelop.smartcheck.databinding.FragmentListBinding
import app.smartdevelop.smartcheck.model.Checklistdb
import app.smartdevelop.smartcheck.ui.detail.DetailFragment

class ListFragment : Fragment(), ListPresenter.View, OnItemClickListener, MenuProvider {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var presenter: ListPresenter
    lateinit var database: Checklistdb
    private lateinit var listener: OnItemClickListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        listener = this
        presenter = ListPresenter(lifecycleScope, requireContext(), binding, listener)

        getListsDB()

        binding.addList.setOnClickListener {
            creatorList()
        }

        return root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_list, menu)    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.create) {
            creatorList()
        }
        return true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getListsDB() {
        presenter.getListsDB()
    }

    override fun creatorList() {
        presenter.creatorList(
            R.string.add_checklist,
            R.string.create,
            R.string.cancel
        )
    }

    override fun onItemClick(item: Int) {
        val nuevoFragmento = DetailFragment.newInstance(item)
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_content_main, nuevoFragmento)
            .addToBackStack(null)
            .commit()
    }


}