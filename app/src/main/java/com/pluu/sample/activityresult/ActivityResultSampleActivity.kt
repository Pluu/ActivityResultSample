package com.pluu.sample.activityresult

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import androidx.activity.prepareCall
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AppCompatActivity

class ActivityResultSampleActivity : AppCompatActivity() {

    val requestActivity = prepareCall(StartActivityForResult()) { activityResult ->
        toast(activityResult.prettyString)
    }

    val requestSecondVanilla =
        prepareCall(object : ActivityResultContract<Void, ActivityResult>() {
            override fun createIntent(input: Void?): Intent {
                return Intent(this@ActivityResultSampleActivity, ResultSecondActivity::class.java)
            }

            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            override fun parseResult(resultCode: Int, intent: Intent?): ActivityResult {
                return ActivityResult(resultCode, intent)
            }
        }) { activityResult ->
            toast(activityResult.prettyString)
        }

    val secondCustom = prepareCall(object : ActivityResultContract<Void, SecondResult?>() {
        override fun createIntent(input: Void?): Intent {
            return Intent(this@ActivityResultSampleActivity, ResultSecondActivity::class.java)
        }

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        override fun parseResult(resultCode: Int, intent: Intent?): SecondResult? {
            return if (resultCode == Activity.RESULT_OK && intent != null) {
                SecondResult(
                    typeString = intent.getStringExtra("typeString"),
                    typeInt = intent.getIntExtra("typeInt", 0)
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

    // TODO: RequestPermission이 활성화된 후 isGranted 결과 미취득
    // TODO: In Activity-1.2.0-alpha02, Fragment-1.3.0-alpha02
    val requestPermission = prepareCall(RequestPermission()) { isGranted ->
        toast("Location granted: $isGranted")
    }

    // TODO: RequestPermission이 활성화된 후 isGranted 결과 미취득
    // TODO: In Activity-1.2.0-alpha02, Fragment-1.3.0-alpha02
    val requestLocation = prepareCall(RequestPermission(), ACCESS_FINE_LOCATION) { isGranted ->
        toast("Location granted: $isGranted")
    }

    val takePicture = prepareCall(TakePicture()) { bitmap ->
        toast("Got picture: $bitmap")
    }

    val dial = prepareCall(Dial()) { success ->
        toast("Dial success: $success")
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
                    requestSecondVanilla.launch(null)
                }
                button("Show second Activity (Custom Result)") {
                    secondCustom.launch(null)
                }
                button("Request location permission") {
                    requestLocation()
                }
                button("Request location permission (Vanilla)") {
                    requestPermission.launch(ACCESS_FINE_LOCATION)
                }
                button("Take pic") {
                    takePicture.launch(null)
                }
                button("Dial 1234-5678-9012") {
                    dial.launch("1234-5678-9012")
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