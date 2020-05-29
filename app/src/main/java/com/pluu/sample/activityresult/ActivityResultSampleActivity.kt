package com.pluu.sample.activityresult

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.activity.result.launch
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity

class ActivityResultSampleActivity : AppCompatActivity() {

    val requestActivity = registerForActivityResult(
        StartActivityForResult()
    ) { activityResult ->
        toast(activityResult.prettyString)
    }

    val requestSecondVanilla = registerForActivityResult(
        object : ActivityResultContract<Unit, ActivityResult>() {
            override fun createIntent(context: Context, input: Unit?): Intent {
                return Intent(context, ResultSecondActivity::class.java)
            }

            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            override fun parseResult(resultCode: Int, intent: Intent?): ActivityResult {
                return ActivityResult(resultCode, intent)
            }
        }) { activityResult ->
        toast(activityResult.prettyString)
    }

    val secondCustom = registerForActivityResult(
        object : ActivityResultContract<Unit, SecondResult?>() {
            override fun createIntent(context: Context, input: Unit?): Intent {
                return Intent(context, ResultSecondActivity::class.java)
            }

            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            override fun parseResult(resultCode: Int, intent: Intent?): SecondResult? {
                return if (resultCode == Activity.RESULT_OK && intent != null) {
                    SecondResult(
                        typeString = intent.getStringExtra(ResultSecondActivity.key_string_case),
                        typeInt = intent.getIntExtra(ResultSecondActivity.key_int_case, 0)
                    )
                } else {
                    null
                }
            }
        }) { result: SecondResult? ->
        if (result != null) {
            toast(result.toString())
        }
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

    val takePicturePreview = registerForActivityResult(
        TakePicturePreview()
    ) { bitmap ->
        toast("Got picture: $bitmap")
    }

    val getContent = registerForActivityResult(
        GetContent()
    ) { uri ->
        toast("Got image: $uri")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this

        setContentView {
            add(::LinearLayout) {
                orientation = VERTICAL
                button("Show second Activity") {
                    val intent = Intent(context, ResultSecondActivity::class.java)
                    requestActivity.launch(intent)
                }
                button("Show second Activity (Vanilla)") {
                    requestSecondVanilla.launch()
                }
                button("Show second Activity (Custom Result)") {
                    secondCustom.launch()
                }
                button("Request location permission") {
                    requestLocation()
                }
                button("Request location permission (Vanilla)") {
                    requestPermission.launch(ACCESS_FINE_LOCATION)
                }
                button("Take pic") {
                    takePicturePreview.launch()
                }
                button("Pick an image") {
                    getContent.launch("image/*")
                }
                button("Show fragment Sample", color = 0xFF81D4FA.toInt()) {
                    val intent = Intent(context, ActivityResultSampleFragmentActivity::class.java)
                    requestActivity.launch(intent)
                }
                button("Go Detail Setting", color = 0xFF81D4FA.toInt()) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", packageName, null)
                    }
                    requestActivity.launch(intent)
                }
            }
        }
    }
}

data class SecondResult(
    val typeString: String,
    val typeInt: Int
)