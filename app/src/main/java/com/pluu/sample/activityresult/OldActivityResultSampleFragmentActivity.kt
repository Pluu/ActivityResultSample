package com.pluu.sample.activityresult

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit

class OldActivityResultSampleFragmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rooViewId = View.generateViewId()

        setContentView {
            add(::FrameLayout) {
                id = rooViewId
            }
        }

        supportFragmentManager.commit(allowStateLoss = true) {
            add(rooViewId, OldActivityResultSampleFragment())
        }
    }
}