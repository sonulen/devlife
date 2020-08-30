package com.example.devlife

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.devlife.data.Category
import com.example.devlife.ui.main.MainFragment
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null
    private val POSITION = "POSITION"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        viewPager = findViewById<ViewPager>(R.id.viewpager)
        tabLayout = findViewById<TabLayout>(R.id.tablayout)

        if (savedInstanceState == null) {
            viewPager?.let {
                it.offscreenPageLimit = 3
                setupViewPager(it)
            }

            tabLayout?.setupWithViewPager(viewPager)

        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(
            MainFragment.newInstance(Category.LATEST),
            getString(R.string.first_category)
        )
        adapter.addFragment(
            MainFragment.newInstance(Category.BESTS),
            getString(R.string.second_category)
        )
        adapter.addFragment(
            MainFragment.newInstance(Category.HOTS),
            getString(R.string.third_category)
        )
        viewPager.adapter = adapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tabLayout?.let {
            outState.putInt(POSITION, it.selectedTabPosition)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val tabIndex = savedInstanceState.getInt(POSITION)
        viewPager?.let {
            it.currentItem = tabIndex;
        }
        tabLayout?.let {
            it.setScrollPosition(tabIndex, 0f, true);
        }
    }

}