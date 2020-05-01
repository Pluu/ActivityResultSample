package com.pluu.sample.activityresult

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class ActivityResultSampleFragment : Fragment() {

    val requestActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        toast(activityResult.prettyString)
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
                    requestActivity.launch(intent)
                }
            }
        }
    }
}