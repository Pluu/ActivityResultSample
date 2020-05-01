package com.pluu.sample.activityresult

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView {
            add(::LinearLayout) {
                orientation = VERTICAL
                button("New ActivityResultContract", color = 0xFF81D4FA.toInt()) {
                    startActivity(Intent(context, ActivityResultSampleActivity::class.java))
                }
                button("Old Pattern") {
                    startActivity(Intent(context, OldActivityResultSampleActivity::class.java))
                }
            }
        }
    }
}