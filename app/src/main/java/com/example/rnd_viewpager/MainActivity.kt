package com.example.rnd_viewpager

import ItemTabViewModel
import TabAdapter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private var isHorizontal : Boolean = true
    private val viewModel : ItemTabViewModel by viewModels()
    lateinit var viewPager2: ViewPager2
    lateinit var tabLayout2: TabLayout
    lateinit var btnOrientation: Button
    lateinit var btnAddPage: Button
    lateinit var btnRemovePage: Button
    lateinit var btnNext: Button
    var count=0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager2=findViewById(R.id.viewPager2);
        tabLayout2=findViewById(R.id.tabLayout2);
        btnOrientation=findViewById(R.id.btnOrientation);
        btnAddPage=findViewById(R.id.btnAddPage);
        btnRemovePage=findViewById(R.id.btnRemovePage);
        btnNext=findViewById(R.id.btnNext);

        viewPager2.adapter = TabAdapter(this, viewModel).apply { setHasStableIds(true) }

        TabLayoutMediator(tabLayout2, viewPager2) { tab, position ->
            tab.text = viewModel.items[position]
        }.attach()

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            viewPager2.apply {
                layoutDirection = View.LAYOUT_DIRECTION_RTL
                currentItem = 0
            }
        }

        changeOrientation()
        addPage()
        removePage()

        btnNext.setOnClickListener {
            count++
            viewPager2.setCurrentItem(count);

        }
    }

    private fun changeOrientation() {
        viewPager2.orientation = ORIENTATION_VERTICAL
        isHorizontal = false
        btnOrientation.text = getString(R.string.horizontal)
        viewPager2.setOffscreenPageLimit(3);

        /*
        btnOrientation.setOnClickListener {
             when {
                 isHorizontal -> {
                     viewPager2.orientation = ORIENTATION_VERTICAL
                     isHorizontal = false
                     btnOrientation.text = getString(R.string.horizontal)

                 }
                 else -> {
                     viewPager2.orientation = ORIENTATION_HORIZONTAL
                     isHorizontal = true
                     btnOrientation.text = getString(R.string.vertical)

                 }
             }
         }
         */
    }

    private fun addPage() {
        btnAddPage.setOnClickListener {
            changeItem { viewModel.addNewItem(viewPager2.currentItem+1) }
        }
    }

    private fun removePage() {
        btnRemovePage.setOnClickListener {
            changeItem { viewModel.removeItem(viewPager2.currentItem) }
        }
    }

    private fun changeItem(performChanges: () -> Unit) {
        val oldPosition = viewPager2.currentItem
        val currentItemId = viewModel.itemId(oldPosition)
        performChanges()
        if(viewModel.contains(currentItemId)) {
            val newPosition = (0 until viewModel.size).indexOfLast {
                viewModel.itemId(it) == currentItemId }
            viewPager2.apply {
                adapter?.notifyDataSetChanged()
                currentItem = newPosition + 1
            }
        } else {
            viewPager2.apply {
                currentItem = oldPosition - 1
                adapter?.notifyDataSetChanged()

            }
        }
    }
}