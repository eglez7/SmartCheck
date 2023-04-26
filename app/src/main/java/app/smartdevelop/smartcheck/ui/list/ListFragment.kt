package app.smartdevelop.smartcheck.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import app.smartdevelop.smartcheck.R
import app.smartdevelop.smartcheck.databinding.FragmentListBinding
import app.smartdevelop.smartcheck.model.Checklistdb
import app.smartdevelop.smartcheck.ui.detail.DetailFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class ListFragment : Fragment(), CoroutineScope, ListPresenter.View {



    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private lateinit var job: Job

    private val adapter = ListAdapter {
        //startActivity (DetailFragment.EXTRA_ID to it.id)
    }

    private lateinit var binding: FragmentListBinding
    private lateinit var presenter : ListPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        super.onCreate(savedInstanceState)
        binding = FragmentListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        presenter = ListPresenter(requireContext(), lifecycleScope, binding, adapter)

        getListsDB()
        binding.addList.setOnClickListener {
            creatorList()
        }

        return root
    }

    override fun onResume() {
        getListsDB()
        super.onResume()
    }


    override fun onDestroyView() {
        job.cancel()
        super.onDestroyView()
    }

    override fun getListsDB() {
        presenter.getListsDB()
    }

    override fun creatorList () {
        presenter.creatorList(getString(R.string.add_checklist),getString(R.string.create),getString(R.string.cancel))
    }
}