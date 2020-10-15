/**
 * (C) Copyright 2019 Ildar Ishalin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package kz.ildar.sandbox

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import kz.ildar.sandbox.ui.main.child.ChildFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            val fragment = navHost?.childFragmentManager?.fragments?.get(0)
            if (fragment is ChildFragment) {
                Navigation.findNavController(this, R.id.nav_host_fragment)
                    .navigate(
                        R.id.mainFragment, null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.mainFragment, true)
                            .setEnterAnim(R.anim.slide_from_left)
                            .build()
                    )
            } else {
                onBackPressed()
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}