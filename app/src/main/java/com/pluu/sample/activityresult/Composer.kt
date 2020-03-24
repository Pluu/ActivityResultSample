package com.pluu.sample.activityresult

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

inline fun Activity.setContentView(ui: ViewManager.() -> Unit) =
    ActivityViewManager(this).apply(ui)

class ActivityViewManager(
    val activity: Activity
) : ViewManager {
    override fun addView(view: View?, params: ViewGroup.LayoutParams?) {
        activity.setContentView(view)
    }

    override fun updateViewLayout(view: View?, params: ViewGroup.LayoutParams?) {}

    override fun removeView(view: View?) {}
}

inline fun Fragment.setContentView(ui: ViewManager.() -> Unit) =
    FragmentViewManager(view!!).apply(ui)

class FragmentViewManager(
    val rootView: View
) : ViewManager {
    override fun addView(view: View?, params: ViewGroup.LayoutParams?) {
        (rootView as ViewGroup).addView(view)
    }

    override fun updateViewLayout(view: View?, params: ViewGroup.LayoutParams?) {}

    override fun removeView(view: View?) {}
}

val ViewManager.context
    get() = when (this) {
        is View -> context
        is ActivityViewManager -> activity
        is FragmentViewManager -> rootView.context
        else -> TODO()
    }

fun <VM : ViewManager, V : View> VM.add(construct: (Context) -> V, init: V.() -> Unit) {
    construct(context).apply(init).also {
        addView(it, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
    }
}

fun ViewManager.text(txt: String) {
    add(::TextView) {
        text = txt
        transformationMethod = null
    }
}

fun ViewManager.button(txt: String, color: Int = Color.LTGRAY, listener: (View) -> Unit) {
    add(::Button) {
        text = txt
        transformationMethod = null

        val bgDrawable = DrawableCompat.wrap(background)
        DrawableCompat.setTint(bgDrawable, color)
//        backgroundTintMode = ColorStateList.valueOf(color)
        setOnClickListener(listener)
    }
}