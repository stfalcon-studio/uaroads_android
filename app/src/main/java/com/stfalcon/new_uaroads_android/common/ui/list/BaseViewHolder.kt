package com.michaldrabik.kotlintest.ui.base.list

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout

abstract class BaseViewHolder<T> : RelativeLayout {

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, layoutResId(), this)
    }

    protected abstract fun layoutResId(): Int

    abstract fun bind(item: T)

}
