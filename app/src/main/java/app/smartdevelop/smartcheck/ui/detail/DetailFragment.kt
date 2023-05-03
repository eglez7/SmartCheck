package app.smartdevelop.smartcheck.ui.detail

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
import app.smartdevelop.smartcheck.R
import app.smartdevelop.smartcheck.databinding.FragmentDetailBinding
import app.smartdevelop.smartcheck.model.Checklistdb
import app.smartdevelop.smartcheck.model.DatabaseProvider
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class DetailFragment : Fragment(), DetailPresenter.View, MenuProvider {

    companion object {
        private const val ARG_PARAM = "List"

        fun newInstance(itemId: Int): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putInt(ARG_PARAM, itemId)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: FragmentDetailBinding
    private lateinit var presenter : DetailPresenter
    private lateinit var database:Checklistdb
    var itemId by Delegates.notNull<Int>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        super.onCreate(savedInstanceState)
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        database = DatabaseProvider.setDatabase(requireContext().applicationContext)

        arguments?.let {
            itemId = it.getInt(ARG_PARAM)
        }

        presenter= DetailPresenter(database, lifecycleScope, requireContext(),binding)

        lifecycleScope.launch {
            val queryTitle = database.checklistsDao().getChecklistsById(itemId)
            setCustomActionBar(queryTitle?.name, true)
        }

        getDetailsDB()

        binding.addItem.setOnClickListener{
            creatorDetail()
        }

        return root
    }


    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_detail, menu)    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.create -> { creatorDetail() }
            R.id.uncheckAll -> { uncheckedAll() }
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setCustomActionBar("", false)
    }

    override fun creatorDetail() {
        presenter.creatorDetail(itemId, getString(R.string.add_detail),getString(R.string.create),getString(
            R.string.cancel))
    }

    override fun uncheckedAll() {
        presenter.uncheckedAll(itemId)
    }

    override fun getDetailsDB() {
        presenter.getDetailsDB(itemId)
    }

    private fun setCustomActionBar(name: String?, enabled : Boolean) {
        val activity = requireActivity() as AppCompatActivity
        val actionBarLayout = layoutInflater.inflate(R.layout.app_bar_detail, null)

        val customTitle = actionBarLayout.findViewById<TextView>(R.id.toolbar_detail)
        customTitle.text = name

        with(activity.supportActionBar!!) {
            setDisplayShowTitleEnabled(!enabled)
            setDisplayShowCustomEnabled(enabled)
            setCustomView(actionBarLayout)
        }
    }


}