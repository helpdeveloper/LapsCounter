package br.com.helpdev.lapscounter.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import br.com.helpdev.lapscounter.R
import br.com.helpdev.lapscounter.databinding.FragmentListActivityBinding
import br.com.helpdev.lapscounter.injector.InjectorUtils
import br.com.helpdev.lapscounter.model.entity.ActivityEntity
import br.com.helpdev.lapscounter.ui.adapter.ListActivityAdapter
import br.com.helpdev.lapscounter.ui.viewmodel.ListActivityViewModel
import kotlinx.android.synthetic.main.fragment_list_activity.*

class ListActivityFragment : Fragment() {

    private lateinit var viewModel: ListActivityViewModel
    private lateinit var listActivityAdapter: ListActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = loadViewModel()
    }

    private fun loadViewModel() = ViewModelProviders.of(this, InjectorUtils.provideActivityViewModelFactory())
        .get(ListActivityViewModel::class.java)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listActivityAdapter = createAdapter()
        initData()
    }

    private fun createAdapter() = ListActivityAdapter { navigateToActivity(it) }
        .also { recycler_view.adapter = it }

    private fun initData() {
        val bind = FragmentListActivityBinding.bind(view!!)
        viewModel.activities.observe(this, Observer { realmResults ->
            val toList = realmResults.toList()
            if (toList.isNullOrEmpty()) {
                bind.hasItems = false
            } else {
                bind.hasItems = true
                listActivityAdapter.submitList(toList)
            }
        })
    }

    private fun navigateToActivity(activityEntity: ActivityEntity) {
        Navigation.findNavController(view!!).navigate(
            ListActivityFragmentDirections.actionListActivityFragmentToActivityFragment(activityEntity)
        )
    }
}