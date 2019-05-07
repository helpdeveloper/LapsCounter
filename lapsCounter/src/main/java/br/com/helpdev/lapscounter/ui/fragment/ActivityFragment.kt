package br.com.helpdev.lapscounter.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.helpdev.lapscounter.R

class ActivityFragment : Fragment() {

    private val activityEntity by lazy { ActivityFragmentArgs.fromBundle(arguments!!).activityEntity }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_activity, container, false)
    }

}