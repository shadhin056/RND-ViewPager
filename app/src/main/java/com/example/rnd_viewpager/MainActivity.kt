package com.example.rnd_viewpager

import ItemTabViewModel
import TabAdapter
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_VERTICAL
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    var movieList: java.util.ArrayList<Movie> = java.util.ArrayList<Movie>()
    private var recyclerView: RecyclerView? = null
    private var mAdapter: MoviesAdapter? = null

    private var isHorizontal: Boolean = true
    private val viewModel: ItemTabViewModel by viewModels()
    lateinit var viewPager2: ViewPager2
    lateinit var tabLayout2: TabLayout
    lateinit var btnOrientation: Button
    lateinit var btnAddPage: Button
    lateinit var btnRemovePage: Button
    lateinit var sv: ScrollView
    lateinit var btnNext: Button
    lateinit var llFirst: LinearLayout
    lateinit var llSecond: LinearLayout
    lateinit var llThird: LinearLayout
    var count = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager2 = findViewById(R.id.viewPager2);
        tabLayout2 = findViewById(R.id.tabLayout2);
        btnOrientation = findViewById(R.id.btnOrientation);
        btnAddPage = findViewById(R.id.btnAddPage);
        btnRemovePage = findViewById(R.id.btnRemovePage);
        btnNext = findViewById(R.id.btnNext);
        recyclerView = findViewById(R.id.recycler_view)
        sv = findViewById(R.id.sv)
        llFirst = findViewById(R.id.llFirst)
        llSecond = findViewById(R.id.llSecond)
        llThird = findViewById(R.id.llThird)

        viewPager2.adapter = TabAdapter(this, viewModel).apply { setHasStableIds(true) }

        TabLayoutMediator(tabLayout2, viewPager2) { tab, position ->
            tab.text = viewModel.items[position]
            Log.e("Tab", "inside");
        }.attach()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
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
            viewPager2.setCurrentItem(viewPager2.currentItem + 1);

        }




        /*
            tabLayout2.getChildAt(1).setOnClickListener { // custom code
                   val tab = tabLayout2.getTabAt(1)
                   tab!!.select()
                   Log.e("Tab", "1");
               }

            tabLayout2.getChildAt(2).setOnClickListener { // custom code
                val tab = tabLayout2.getTabAt(2)
                tab!!.select()
                Log.e("Tab", "2");
            }*/


        tabLayout2.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {


            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {

                when (tab.position) {
                    0 -> {
                        Log.e("Tab", "1");

                         sv.scrollTo(0, llFirst.getY().toInt())

                    }
                    1 -> {
                        Log.e("Tab", "2");
                          sv.scrollTo(0, llSecond.getY().toInt())
                    }
                    2 -> {
                        Log.e("Tab", "3");
                         sv.scrollTo(0, llThird.getY().toInt())
                    }
                }


            }
        })



        mAdapter = MoviesAdapter(movieList)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.setLayoutManager(mLayoutManager)
        recyclerView!!.setItemAnimator(DefaultItemAnimator())
        recyclerView!!.setAdapter(mAdapter);
        prepareMovieData()
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // Log.d("scroll", "idle")

                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    // Log.d("scroll", "settling")
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    //Log.d("scroll", "dragging")
                }


            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visiblePosition: Int = mLayoutManager.findFirstCompletelyVisibleItemPosition()
                if (visiblePosition > -1) {
                    val v: View = mLayoutManager.findViewByPosition(visiblePosition)!!

                    Log.d("scroll---", visiblePosition.toString())

                    if (visiblePosition >= 0 && visiblePosition < 5) {
                        viewPager2.setCurrentItem(0);
                    } else if (visiblePosition >= 5 && visiblePosition < 10) {

                        viewPager2.setCurrentItem(1);

                    } else if (visiblePosition >= 10 && visiblePosition < 15) {

                        viewPager2.setCurrentItem(2);
                    } else if (visiblePosition >= 15 && visiblePosition < 20) {

                        viewPager2.setCurrentItem(3);
                    } else if (visiblePosition >= 20 && visiblePosition < 25) {

                        viewPager2.setCurrentItem(4);
                    } else if (visiblePosition >= 25 && visiblePosition < 30) {

                        viewPager2.setCurrentItem(5);
                    } else if (visiblePosition >= 30 && visiblePosition < 35) {

                        viewPager2.setCurrentItem(6);
                    }

                }
            }
        })

        sv.getViewTreeObserver().addOnScrollChangedListener(OnScrollChangedListener {
            // val scrollY: Int = rootScrollView.getScrollY() // For ScrollView
            //val scrollX: Int = rootScrollView.getScrollX() // For HorizontalScrollView
            // DO SOMETHING WITH THE SCROLL COORDINATES

            if (getVisiblePercent(llFirst) > 5 && getVisiblePercent(llFirst) != 100) {
                Log.e("height first", getVisiblePercent(llFirst).toString());
                viewPager2.setCurrentItem(0);
            } else if (getVisiblePercent(llSecond) > 5 && getVisiblePercent(llSecond) != 100) {
                Log.e("height second", getVisiblePercent(llSecond).toString());
                viewPager2.setCurrentItem(1);
            } else if (getVisiblePercent(llThird) > 5 && getVisiblePercent(llThird) != 100) {
                Log.e("height third", getVisiblePercent(llThird).toString());
                viewPager2.setCurrentItem(2);
            }
        })
    }
    fun getVisiblePercent(v: View): Int {
        return if (v.isShown) {
            val r = Rect()
            v.getGlobalVisibleRect(r)
            val sVisible: Double = (r.width() * r.height()).toDouble()
            val sTotal = (v.width * v.height).toDouble()
            (100 * sVisible / sTotal).toInt()
        } else {
            -1
        }
    }
    private fun prepareMovieData() {
        var movie = Movie("Mad Max: Fury Road", "Action & Adventure", "2015")
        movieList.add(movie)
        movie = Movie("Inside Out", "Animation, Kids & Family", "2015")
        movieList.add(movie)
        movie = Movie("Star Wars: Episode VII - The Force Awakens", "Action", "2015")
        movieList.add(movie)
        movie = Movie("Shaun the Sheep", "Animation", "2015")
        movieList.add(movie)
        movie = Movie("The Martian", "Science Fiction & Fantasy", "2015")
        movieList.add(movie)
        movie = Movie("Mission: Impossible Rogue Nation", "Action", "2015")
        movieList.add(movie)
        movie = Movie("Up", "Animation", "2009")
        movieList.add(movie)
        movie = Movie("Star Trek", "Science Fiction", "2009")
        movieList.add(movie)
        movie = Movie("The LEGO Movie", "Animation", "2014")
        movieList.add(movie)
        movie = Movie("Iron Man", "Action & Adventure", "2008")
        movieList.add(movie)
        movie = Movie("Aliens", "Science Fiction", "1986")
        movieList.add(movie)
        movie = Movie("Chicken Run", "Animation", "2000")
        movieList.add(movie)
        movie = Movie("Back to the Future", "Science Fiction", "1985")
        movieList.add(movie)
        movie = Movie("Raiders of the Lost Ark", "Action & Adventure", "1981")
        movieList.add(movie)
        movie = Movie("Goldfinger", "Action & Adventure", "1965")
        movieList.add(movie)
        movie = Movie("Guardians of the Galaxy", "Science Fiction & Fantasy", "2014")
        movieList.add(movie)
        movie = Movie("Inside Out", "Animation, Kids & Family", "2015")
        movieList.add(movie)
        movie = Movie("Star Wars: Episode VII - The Force Awakens", "Action", "2015")
        movieList.add(movie)
        movie = Movie("Shaun the Sheep", "Animation", "2015")
        movieList.add(movie)
        movie = Movie("The Martian", "Science Fiction & Fantasy", "2015")
        movieList.add(movie)
        movie = Movie("Mission: Impossible Rogue Nation", "Action", "2015")
        movieList.add(movie)
        movie = Movie("Up", "Animation", "2009")
        movieList.add(movie)
        movie = Movie("Star Trek", "Science Fiction", "2009")
        movieList.add(movie)
        movie = Movie("The LEGO Movie", "Animation", "2014")
        movieList.add(movie)
        movie = Movie("Iron Man", "Action & Adventure", "2008")
        movieList.add(movie)
        movie = Movie("Aliens", "Science Fiction", "1986")
        movieList.add(movie)
        movie = Movie("Chicken Run", "Animation", "2000")
        movieList.add(movie)
        movie = Movie("Back to the Future", "Science Fiction", "1985")
        movieList.add(movie)
        movie = Movie("Raiders of the Lost Ark", "Action & Adventure", "1981")
        movieList.add(movie)
        movie = Movie("Goldfinger", "Action & Adventure", "1965")
        movieList.add(movie)
        movie = Movie("Guardians of the Galaxy", "Science Fiction & Fantasy", "2014")
        movieList.add(movie)
        mAdapter!!.notifyDataSetChanged()
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
            changeItem { viewModel.addNewItem(viewPager2.currentItem + 1) }
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
        if (viewModel.contains(currentItemId)) {
            val newPosition = (0 until viewModel.size).indexOfLast {
                viewModel.itemId(it) == currentItemId

            }
            viewPager2.apply {
                adapter?.notifyDataSetChanged()
                currentItem = newPosition + 1
                Log.e("Tab", currentItem.toString());
            }
        } else {
            viewPager2.apply {
                currentItem = oldPosition - 1
                adapter?.notifyDataSetChanged()
                Log.e("Tab", currentItem.toString());
            }
        }
    }
}



