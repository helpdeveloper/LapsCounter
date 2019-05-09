package br.com.helpdev.lapscounter.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import br.com.helpdev.lapscounter.R
import br.com.helpdev.lapscounter.databinding.FragmentActivityBinding
import br.com.helpdev.lapscounter.injector.InjectorUtils
import br.com.helpdev.lapscounter.ui.ActivitiesActivity
import br.com.helpdev.lapscounter.ui.adapter.LapsAdapter
import br.com.helpdev.lapscounter.ui.viewmodel.ActivityViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_activity.*
import kotlinx.android.synthetic.main.include_lap_log.*


class ActivityFragment : Fragment() {

    private var dialog: AlertDialog? = null
    private val activityEntity by lazy { ActivityFragmentArgs.fromBundle(arguments!!).activityEntity }
    private lateinit var viewModel: ActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = loadViewModel()
    }

    override fun onPause() {
        super.onPause()
        dialog?.let {
            it.dismiss()
            dialog = null
        }
    }

    private fun loadViewModel() = ViewModelProviders.of(this, InjectorUtils.provideActivityViewModelFactory())
        .get(ActivityViewModel::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init(context!!, activityEntity)
        bind(view)
        loadAds()
        configureToolbar()
        configureLapsLog()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.activity_saved_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.isRemoveItem()) showDialogDelete()
        return super.onOptionsItemSelected(item)
    }

    private fun MenuItem.isRemoveItem() = itemId == R.id.menu_remove


    private fun showDialogDelete() {
        AlertDialog.Builder(context!!).apply {
            setTitle(R.string.app_name)
            setMessage(R.string.confirm_delete)
            setPositiveButton(android.R.string.ok) { _, _ -> deleteActivity() }
            setNegativeButton(android.R.string.cancel, null)
        }.let { dialog = it.create(); dialog!! }.let { it.show() }
    }

    private fun deleteActivity() {
        viewModel.deleteActivity()
        Snackbar.make(view!!, getString(R.string.activity_removed), Snackbar.LENGTH_LONG).show()
        Navigation.findNavController(view!!).navigateUp()
    }

    private fun bind(view: View) {
        with(FragmentActivityBinding.bind(view)) {
            viewModel = this@ActivityFragment.viewModel
        }
    }

    private fun loadAds() {
        adView.loadAd(AdRequest.Builder().build())
    }

    private fun configureToolbar() {
        (activity as ActivitiesActivity).supportActionBar?.title = activityEntity.name
    }

    private fun configureLapsLog() {
        val toObLaps = activityEntity.chronometer!!.toObLaps()
        recycler_view.adapter = LapsAdapter(context!!, toObLaps, false)
        if (toObLaps.isNotEmpty()) text_view_empty.visibility = View.GONE
    }

}