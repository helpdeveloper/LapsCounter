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
        viewModel = ViewModelProviders.of(this, InjectorUtils.provideActivityViewModelFactory())
                .get(ListActivityViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createAdapter()
        initData()
    }

    private fun initData() {
        viewModel.activities.observe(this, Observer { realmResults ->
            listActivityAdapter.submitList(realmResults.toList())
        })
    }

    private fun createAdapter() {
        listActivityAdapter = ListActivityAdapter { navigateToActivity(it) }
        recycler_view.adapter = listActivityAdapter
    }

    private fun navigateToActivity(activityEntity: ActivityEntity) {
        Navigation.findNavController(view!!).navigate(
                ListActivityFragmentDirections.actionListActivityFragmentToActivityFragment(activityEntity)
        )
    }
}