package com.example.jokeapp.presentation

import com.google.android.material.tabs.TabLayout

class TabListener(private val tabChosen: (Int) -> Unit) : TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab?) = tabChosen.invoke(tab?.position ?: 0)

    override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

    override fun onTabReselected(tab: TabLayout.Tab?) = Unit
}