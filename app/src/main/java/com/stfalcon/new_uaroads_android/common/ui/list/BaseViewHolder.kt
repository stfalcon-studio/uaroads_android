/*
 * Copyright (c) 2017 stfalcon.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.stfalcon.new_uaroads_android.common.ui.list

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
