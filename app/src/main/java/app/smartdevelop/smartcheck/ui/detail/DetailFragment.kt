package app.smartdevelop.smartcheck.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.lifecycle.lifecycleScope
import app.smartdevelop.smartcheck.R
import app.smartdevelop.smartcheck.databinding.FragmentDetailBinding
import app.smartdevelop.smartcheck.ui.MainActivity
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class DetailFragment : Fragment(), DetailPresenter.View {

    companion object {
        const val EXTRA_ID = "DetailActivity:id"
    }

    private lateinit var binding: FragmentDetailBinding
    private lateinit var presenter : DetailPresenter
    var itemId by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        super.onCreate(savedInstanceState)
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        lifecycleScope.launch {

            val queryTitle = MainActivity.room.checklistsDao().getChecklistsById(itemId)
            //supportActionBar?.title = queryTitle?.name
        }

        getDetailsDB()

        binding.addItem.setOnClickListener{
            creatorDetail()
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
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


}