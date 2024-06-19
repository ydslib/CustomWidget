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

    companion object {
        /**
         * 对钩
         */
        const val STATE_CHECK = "Check"

        /**
         * 叉叉
         */
        const val STATE_FORK = "Fork"
    }

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

    private var mState: String = STATE_CHECK

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.StateDotView)
        mCurDotColor = a.getColor(R.styleable.StateDotView_stateDotColor, Color.BLACK)
        mState = a.getString(R.styleable.StateDotView_dotState) ?: STATE_CHECK
        initDefaultStatePaint()
        initCirclePaint()
        a.recycle()
    }

    private fun initCirclePaint() {
        mCirclePaint.color = mCurDotColor
    }

    private fun initDefaultStatePaint() {
        mStatePaint.strokeWidth = defaultStrokeWidth
        mStatePaint.strokeCap = Paint.Cap.ROUND
        mStatePaint.isAntiAlias = true
        mStatePaint.color = Color.WHITE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val l = if (width > height) height else width
        val cx = mCx ?: (l / 2f)
        val cy = mCy ?: (l / 2f)
        val r = mRadius ?: (l / 2f)
        if (mState == STATE_CHECK) {
            drawCheckCircle(canvas, cx, cy, r)
        } else {
            drawForkCircle(canvas, cx, cy, r)
        }
    }

    private fun drawForkCircle(canvas: Canvas?, cx: Float, cy: Float, r: Float) {
        canvas?.drawCircle(cx, cy, r, mCirclePaint)

        val widthDivide = r / 16f
        val heightDivide = r / 16f

        val baseX = cx - r
        val baseY = cy - r

        //左上
        var startX = 10 * widthDivide + baseX
        var startY = 10 * heightDivide + baseY
        var stopX = 16 * widthDivide + baseX
        var stopY = 16 * heightDivide + baseY

        canvas?.drawLine(startX, startY, stopX, stopY, mStatePaint)

        // 左下
        startY = 22 * heightDivide + baseY
        canvas?.drawLine(startX, startY, stopX, stopY, mStatePaint)

        //右上
        startX = stopX
        startY = stopY
        stopX = 22 * widthDivide + baseX
        stopY = 10 * heightDivide + baseY
        canvas?.drawLine(startX, startY, stopX, stopY, mStatePaint)

        //右下
        stopY = 22 * heightDivide + baseY
        canvas?.drawLine(startX, startY, stopX, stopY, mStatePaint)
    }

    private fun drawCheckCircle(canvas: Canvas?, cx: Float, cy: Float, r: Float) {

        canvas?.drawCircle(cx, cy, r, mCirclePaint)

        val widthDivide = r / 20f
        val heightDivide = r / 20f

        val baseX = cx - r
        val baseY = cy - r

        //前半截
        var startX = 11 * widthDivide + baseX
        var startY = 18 * heightDivide + baseY
        var stopX = 18 * widthDivide + baseX
        var stopY = 24 * heightDivide + baseY

        canvas?.drawLine(startX, startY, stopX, stopY, mStatePaint)

        //后半截
        startX = stopX
        startY = stopY
        stopX = 29 * widthDivide + baseX
        stopY = 14 * heightDivide + baseY
        canvas?.drawLine(startX, startY, stopX, stopY, mStatePaint)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            width = (mRadius?.toInt() ?: 0) * 2 + paddingStart + paddingEnd
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            height = (mRadius?.toInt() ?: 0) * 2 + paddingTop + paddingBottom
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
            mCirclePaint.color = mCurDotColor
            invalidate()
        }
    }

    fun setCircleCenter(cx: Float, cy: Float, radius: Float) {
        mCx = cx
        mCy = cy
        mRadius = radius
    }

}