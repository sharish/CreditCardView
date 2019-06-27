package com.cooltechworks.creditcarddesign.views

import android.content.Context
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.View

class HeightWrappingViewPager : ViewPager {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val firstChild = getChildAt(0)
        firstChild.measure(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(firstChild.measuredHeight, View.MeasureSpec.EXACTLY))
    }
}