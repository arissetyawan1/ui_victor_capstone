package com.android.victor.ui.intro

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.android.victor.R
import com.android.victor.databinding.ActivityIntroBinding

class IntroActivity: AppCompatActivity(), View.OnClickListener, ViewPager.OnPageChangeListener {

    private var layouts = arrayOf<Int>()
    private var dots = arrayOf<TextView>()

    private lateinit var adapter: IntroAdapter
    private lateinit var b: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(b.root)
        initiate()
        addLayout()
        setAdapterSlider()
        setListener()
    }

    private fun initiate() {
        adapter = IntroAdapter(layouts, this)
    }

    private fun addLayout() {
        layouts = arrayOf(
            R.layout.item_slide_1,
            R.layout.item_slide_2
        )
        adapter.updateLayout(layouts)
        addBottomDots(0)
        changeStatusBarColor()
    }

    private fun setAdapterSlider() {
        b.viewPager.adapter = adapter
        b.viewPager.addOnPageChangeListener(this)
    }

    private fun setListener() {
        b.btnSkip.setOnClickListener(this)
        b.btnNext.setOnClickListener(this)
    }

    private fun addBottomDots(currentPage: Int){
        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)
        b.layoutDots.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i].text = Html.fromHtml("&#8226;")
            dots[i].textSize = 35f
            dots[i].setTextColor(colorsInactive[currentPage])
            b.layoutDots.addView(dots[i])
        }
        if (dots.isNotEmpty()) {
            dots[currentPage].setTextColor(colorsActive[currentPage])
        }
    }

    private fun getItem(i: Int): Int {
        return b.viewPager.currentItem + i
    }

    private fun changeStatusBarColor() {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSkip -> {
                Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show()
                finish()
            }
            R.id.btnNext -> {
                val current = getItem(+1)
                if (current < layouts.size) {
                    b.viewPager.currentItem = current
                } else {
                    Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    override fun onPageSelected(position: Int) {
        addBottomDots(position)
        if (position == layouts.size - 1) {
            b.btnNext.text = getString(R.string.next)
            b.btnSkip.visibility = View.GONE
        } else {
            b.btnNext.text = getString(R.string.next)
            b.btnSkip.visibility = View.VISIBLE
        }
    }
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageScrollStateChanged(state: Int) {}
}