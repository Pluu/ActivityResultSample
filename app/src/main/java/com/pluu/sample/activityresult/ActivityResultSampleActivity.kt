package com.pluu.sample.activityresult

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import androidx.activity.prepareCall
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AppCompatActivity

class ActivityResultSampleActivity : AppCompatActivity() {

    val second = prepareCall(StartActivityForResult()) { activityResult ->
        actionResultData(activityResult)
    }

    val secondVanilla =
        prepareCall(object : ActivityResultContract<Void, ActivityResult>() {
            override fun createIntent(input: Void?): Intent {
                return Intent(this@ActivityResultSampleActivity, ResultSecondActivity::class.java)
            }

            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            override fun parseResult(resultCode: Int, intent: Intent?): ActivityResult {
                return ActivityResult(resultCode, intent)
            }
        }) { activityResult ->
            actionResultData(activityResult)
        }

    private fun actionResultData(activityResult: ActivityResult) {
        val result = buildString {
            val resultString = when (activityResult.resultCode) {
                Activity.RESULT_OK -> "RESULT_OK"
                Activity.RESULT_CANCELED -> "RESULT_CANCELED"
                else -> "Result ${activityResult.resultCode}"
            }
            append("ResultCode: $resultString")

            val data = activityResult.data?.extras?.let { bundle ->
                bundle.keySet()
                    .map { it to bundle.get(it) }
                    .joinToString()
            }.orEmpty()

            if (data.isNotEmpty()) {
                append(System.lineSeparator())
                append("Extra: $data")
            }
        }

        toast(result)
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
                    second.launch(intent)
                }
                button("Show second Activity (Vanilla)") {
                    secondVanilla.launch(null)
                }
                button("Show second Activity (Custom Result)") {
                    secondCustom.launch(null)
                }
                button("Request location permission") {
                    requestLocation()
                }
                button("Take pic") {
                    takePicture.launch(null)
                }
                button("Dial 1234-5678-9012") {
                    dial.launch("1234-5678-9012")
                }
            }
        }
    }
}

data class SecondResult(
    val typeString: String,
    val typeInt: Int
)