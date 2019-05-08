package br.com.helpdev.lapscounter.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.helpdev.lapscounter.R
import br.com.helpdev.lapscounter.databinding.FragmentActivityBinding
import br.com.helpdev.lapscounter.ui.ActivitiesActivity
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.fragment_activity.*


class ActivityFragment : Fragment() {

    private val activityEntity by lazy { ActivityFragmentArgs.fromBundle(arguments!!).activityEntity }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind(view)
        loadAds()
        configureToolbar()
    }

    private fun configureToolbar() {
        (activity as ActivitiesActivity).supportActionBar?.title = activityEntity.name
    }

    private fun bind(view: View) {
        with(FragmentActivityBinding.bind(view)) {
            activityEntity = this@ActivityFragment.activityEntity
        }
    }

    private fun loadAds() {
        adView.loadAd(AdRequest.Builder().build())
    }

}