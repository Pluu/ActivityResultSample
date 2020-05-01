package com.pluu.sample.activityresult

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class OldActivityResultSampleActivity : AppCompatActivity() {
    private val request_second_code = 100
    private val request_location_code = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView {
            add(::LinearLayout) {
                orientation = VERTICAL
                button("Show second Activity") {
                    startSecondView()
                }
                button("Request location permission") {
                    requestLocation()
                }
                button("Show fragment Sample", color = 0xFF81D4FA.toInt()) {
                    startOldStyleFragmentView()
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
        val intent = Intent(this, ResultSecondActivity::class.java)
        startActivityForResult(intent, request_second_code)
    }

    private fun startOldStyleFragmentView() {
        startActivity(Intent(this, OldActivityResultSampleFragmentActivity::class.java))
    }

    private fun requestLocation() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        if (checkPermissions(permission)) {
            toast("Permission granted")
        } else {
            requestPermissions(arrayOf(permission), request_location_code)
        }
    }

    private fun checkPermissions(vararg permissions: String): Boolean {
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }
    }
}