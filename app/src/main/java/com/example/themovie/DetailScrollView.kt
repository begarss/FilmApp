package com.example.themovie

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView


class DetailScrollView(context: Context) : NestedScrollView(context) {
    private var mDetector: GestureDetector? = null
    fun setDetector(detector: GestureDetector) {
        mDetector = detector
    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        super.dispatchTouchEvent(ev)
        return mDetector?.onTouchEvent(ev)!!
    }
}