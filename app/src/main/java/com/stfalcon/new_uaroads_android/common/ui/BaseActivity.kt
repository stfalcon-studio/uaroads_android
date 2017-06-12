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

package com.stfalcon.new_uaroads_android.common.ui

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity

/*
 * Created by Anton Bevza on 3/28/17.
 */
abstract class BaseActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        parseIntent()
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
    }

    open fun parseIntent() {
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    protected abstract fun getLayoutResId(): Int

}