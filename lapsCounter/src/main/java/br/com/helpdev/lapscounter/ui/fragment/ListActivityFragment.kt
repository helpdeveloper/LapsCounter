package br.com.helpdev.lapscounter.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import br.com.helpdev.lapscounter.databinding.FragmentListActivityBinding
import br.com.helpdev.lapscounter.injector.InjectorUtils
import br.com.helpdev.lapscounter.model.entity.ActivityEntity
import br.com.helpdev.lapscounter.ui.adapter.ListActivityAdapter
import br.com.helpdev.lapscounter.ui.viewmodel.ListActivityViewModel

class ListActivityFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentListActivityBinding.inflate(inflater, container, false).apply {
            subscribeUI(this)
        }.let { it.root }
    }

    private fun subscribeUI(binding: FragmentListActivityBinding) {
        val listActivityAdapter = createAdapter()
        binding.recyclerView.adapter = listActivityAdapter
        val viewModel = loadViewModel()
        viewModel.observerActivities(listActivityAdapter, binding)
    }

    private fun createAdapter() = ListActivityAdapter { navigateToActivity(it) }

    private fun loadViewModel() = ViewModelProviders.of(this, InjectorUtils.provideActivityViewModelFactory())
        .get(ListActivityViewModel::class.java)

    private fun ListActivityViewModel.observerActivities(
        adapter: ListActivityAdapter,
        binding: FragmentListActivityBinding
    ) {
        activities.observe(this@ListActivityFragment, Observer { realmResults ->
            val toList = realmResults.toList()
            if (toList.isNullOrEmpty()) {
                binding.hasItems = false
            } else {
                binding.hasItems = true
                adapter.submitList(toList)
            }
        })
    }

    private fun navigateToActivity(activityEntity: ActivityEntity) {
        Navigation.findNavController(view!!).navigate(
            ListActivityFragmentDirections.actionListActivityFragmentToActivityFragment(activityEntity.id)
        )
    }
}