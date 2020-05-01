package com.pluu.sample.activityresult

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.invoke
import androidx.activity.registerForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment

class ActivityResultSampleFragment : Fragment() {

    val requestActivity = registerForActivityResult(
        StartActivityForResult()
    ) { activityResult ->
        toast(activityResult.prettyString)
    }

    val requestPermission = registerForActivityResult(
        RequestPermission()
    ) { isGranted ->
        toast("Location granted: $isGranted")
    }

    val requestLocation = registerForActivityResult(
        RequestPermission(), ACCESS_FINE_LOCATION
    ) { isGranted ->
        toast("Location granted: $isGranted")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FrameLayout(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContentView {
            add(::LinearLayout) {
                orientation = LinearLayout.VERTICAL
                text("Here is Fragment")
                button("Show second Activity", color = 0xFFC5CAE9.toInt()) {
                    val intent = Intent(context, ResultSecondActivity::class.java)
                    requestActivity(intent)
                }
                button("Request location permission") {
                    requestLocation()
                }
                button("Request location permission (Vanilla)") {
                    requestPermission(ACCESS_FINE_LOCATION)
                }
            }
        }
    }
}