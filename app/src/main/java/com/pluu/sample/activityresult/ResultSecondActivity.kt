package com.pluu.sample.activityresult

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf

class ResultSecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView {
            add(::LinearLayout) {
                orientation = LinearLayout.VERTICAL
                button("Call Finish ~ Result Ok") {
                    val result = Intent().apply {
                        putExtras(
                            bundleOf(
                                key_string_case to "ABCD",
                                key_int_case to 1
                            )
                        )
                    }
                    setResult(Activity.RESULT_OK, result)
                    finish()
                }
            }
        }
    }

    companion object {
        const val key_string_case = "typeString"
        const val key_int_case = "typeInt"
    }
}