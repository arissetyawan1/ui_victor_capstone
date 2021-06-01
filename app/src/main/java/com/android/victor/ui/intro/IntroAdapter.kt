package com.android.victor.ui.intro

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class IntroAdapter(private var layouts: Array<Int>, var context: Context) : PagerAdapter() {

    fun updateLayout(layouts: Array<Int>){
        this.layouts = layouts
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = LayoutInflater.from(container.context).inflate(
            layouts[position],
            container,
            false
        )
        container.addView(view, 0)
        return view
    }

    override fun getCount(): Int {
        return layouts.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        val view = `object` as View?
        container.removeView(view)
    }
}