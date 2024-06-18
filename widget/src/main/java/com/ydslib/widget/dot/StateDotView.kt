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
 * 带有状态的圆圈
 * 比如对钩，叉叉
 */
class StateDotView : View {

    /**
     * 画圆的画笔
     */
    private val mCirclePaint by lazy {
        Paint()
    }

    /**
     * 画对钩的画笔
     */
    private val mStatePaint by lazy {
        Paint()
    }

    private var mCx: Float? = null
    private var mCy: Float? = null

    private var mRadius: Float? = null

    private var mDotStateColor: ColorStateList? = null

    private var defaultStrokeWidth = 7f

    //当前颜色
    private var mCurDotColor: Int = Color.BLACK

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.StateDotView)
        val color = a.getColor(R.styleable.StateDotView_stateDotColor, Color.BLACK)
        mCurDotColor = color
        initDefaultStatePaint()
        initCirclePaint()
        a.recycle()
    }

    private fun initCirclePaint() {
        mCirclePaint.color = Color.GREEN
    }

    private fun initDefaultStatePaint() {
        mStatePaint.strokeWidth = defaultStrokeWidth
        mStatePaint.strokeCap = Paint.Cap.ROUND
        mStatePaint.isAntiAlias = true
        mStatePaint.color = Color.WHITE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //画圆
        val cx = width / 2f
        val cy = height / 2f
        if (cx < cy) {
            canvas?.drawCircle(cx, cy, cx, mCirclePaint)
        } else {
            canvas?.drawCircle(cx, cy, cy, mCirclePaint)
        }
//        val cx = mCx ?: 0f
//        val cy = mCy ?: 0f
//        val r = mRadius ?: 0f
//        canvas?.drawCircle(cx, cy, r, mCirclePaint)


        val widthDivide = width / 40f
        val heightDivide = height / 40f

        //前半截
        var startX = 11 * widthDivide
        var startY = 18 * heightDivide
        var stopX = 18 * widthDivide
        var stopY = 24 * heightDivide

//        if (cx != 0f && cx > r) {
//            startX = startX + cx - r
//            stopX = stopX + cx - r
//        }
//        if (cy != 0f && cy > r) {
//            startY = startY + cy - r
//            stopY = stopY + cy - r
//        }

        canvas?.drawLine(startX, startY, stopX, stopY, mStatePaint)

        //后半截
        startX = stopX
        startY = stopY
        stopX = 29 * widthDivide
        stopY = 14 * heightDivide
//        if (cx != 0f && cx > r) {
//            stopX = stopX + cx - r
//        }
//        if (cy != 0f && cy > r) {
//            stopY = stopY + cy - r
//        }
        canvas?.drawLine(startX, startY, stopX, stopY, mStatePaint)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (widthMode == MeasureSpec.AT_MOST) {
            width = (mRadius?.toInt() ?: 0) + paddingStart + paddingEnd
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            height = (mRadius?.toInt() ?: 0) + paddingTop + paddingBottom
        }
        setMeasuredDimension(width, height)
    }

    fun setDotStateColor(@ColorInt color: Int) = apply {
        mDotStateColor = ColorStateList.valueOf(color)

        updateDotStateColor()
    }

    private fun updateDotStateColor() {
        var inval = false
        val color = mDotStateColor?.getColorForState(drawableState, 0) ?: Color.BLUE
        if (color != mCurDotColor) {
            mCurDotColor = color
            inval = true
        }
        if (inval) {
            invalidate()
        }
    }

    fun setCircleCenter(cx: Float, cy: Float, radius: Float) {
        mCx = cx
        mCy = cy
        mRadius = radius
    }

}