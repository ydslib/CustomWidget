package com.ydslib.widget.dot

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.ydslib.widget.R

/**
 * 圆
 */
class DotView : View {

    private val paint by lazy { Paint() }

    private var mDotColor: ColorStateList? = null

    //当前颜色
    private var mCurDotColor: Int = Color.BLACK

    private var mCx: Float? = null
    private var mCy: Float? = null

    private var mRadius: Float? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DotView)
        val color = a.getColor(R.styleable.DotView_dotColor, Color.BLACK)
        mCurDotColor = color
        a.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = mCurDotColor

        if (mCx == null) {
            mCx = width.toFloat() / 2
        }

        if (mCy == null) {
            mCy = height.toFloat() / 2
        }

        if (mRadius == null) {
            mRadius = width.toFloat() / 2
        }
        /**
         * cx：圆心在x轴方向的位置
         * cy：圆心在y轴方向的位置
         */
        canvas?.drawCircle(mCx ?: 0f, mCy ?: 0f, mRadius ?: 0f, paint)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    fun setDotColor(@ColorInt color: Int) {
        mDotColor = ColorStateList.valueOf(color)

        updateDotColors()
    }

    private fun updateDotColors() {
        var inval = false
        val drawableState = drawableState
        val color = mDotColor?.getColorForState(drawableState, 0) ?: Color.BLACK
        if (color != mCurDotColor) {
            mCurDotColor = color
            inval = true
        }
        //颜色无变化则不更新UI
        if (inval) {
            invalidate()
        }
    }

    fun setDotColor(colors: ColorStateList?) {
        if (colors == null) {
            throw NullPointerException()
        }
        mDotColor = colors
        updateDotColors()
    }

    fun setCircleCenter(cx: Float, cy: Float, radius: Float) {
        mCx = cx
        mCy = cy
        mRadius = radius
//        requestLayout()
//        invalidate()
    }

}