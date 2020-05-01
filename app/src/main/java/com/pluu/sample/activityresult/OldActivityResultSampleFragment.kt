package com.pluu.sample.activityresult

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class OldActivityResultSampleFragment : Fragment() {

    private val request_second_code = 100
    private val request_location_code = 101

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
                text("Here is old-style Fragment")
                button("Show second Activity", color = 0xFFC5CAE9.toInt()) {
                    startSecondView()
                }
                button("Request location permission") {
                    requestLocation()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == request_second_code) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                toast(data.extras?.prettyString.orEmpty())
            } else {
                toast("RESULT_CANCELED")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == request_location_code) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                toast("Permission granted")
            } else {
                toast("Permission denied or canceled")
            }
        }
    }

    private fun startSecondView() {
        val intent = Intent(requireContext(), ResultSecondActivity::class.java)
        startActivityForResult(intent, request_second_code)
    }

    private fun requestLocation() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        if (checkPermissions(permission)) {
            toast("Permission granted")
        } else {
            requestPermissions(
                arrayOf(permission),
                request_location_code
            )
        }
    }

    private fun checkPermissions(vararg permissions: String): Boolean {
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}