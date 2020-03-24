package com.pluu.sample.activityresult

import android.app.Activity
import androidx.activity.result.ActivityResult

inline val ActivityResult.prettyString: String
    get() = buildString {
        val resultString = when (resultCode) {
            Activity.RESULT_OK -> "RESULT_OK"
            Activity.RESULT_CANCELED -> "RESULT_CANCELED"
            else -> "Result $resultCode"
        }
        append("ResultCode: $resultString")

        val data = data?.extras?.let { bundle ->
            bundle.keySet()
                .map { it to bundle.get(it) }
                .joinToString()
        }.orEmpty()

        if (data.isNotEmpty()) {
            append(System.lineSeparator())
            append("Extra: $data")
        }
    }