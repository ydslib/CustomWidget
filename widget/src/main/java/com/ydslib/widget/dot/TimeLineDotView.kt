package com.ydslib.widget.dot

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.ydslib.widget.R

class TimeLineDotView : View {

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

    private val mLinePaint by lazy {
        Paint()
    }

    private var mCx: Float? = null
    private var mCy: Float? = null

    private var mRadius: Float? = null

    private var mDotStateColor: ColorStateList? = null

    private var defaultStrokeWidth = 3f

    //当前颜色
    private var mCurDotColor: Int = Color.parseColor("#367ef4")

    private var mState: String = STATE_CHECK

    private var mShowState: Boolean = true

    private val defaultLineHeight = 150

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TimeLineDotView)
        mRadius = a.getDimensionPixelSize(R.styleable.TimeLineDotView_timeLineRadius, 0).toFloat()
        initDefaultStatePaint()
        initCirclePaint()
        initDefaultLinePaint()
        a.recycle()
    }

    private fun initCirclePaint() {
        mCirclePaint.color = mCurDotColor
        mCirclePaint.strokeWidth = 4f
        mCirclePaint.isAntiAlias = true
    }

    private fun initDefaultStatePaint() {
        mStatePaint.strokeWidth = defaultStrokeWidth
        mStatePaint.strokeCap = Paint.Cap.ROUND
        mStatePaint.isAntiAlias = true
        mStatePaint.color = Color.WHITE
    }

    private fun initDefaultLinePaint() {
        mLinePaint.strokeWidth = 4f
        mLinePaint.color = Color.parseColor("#367ef4")
        mStatePaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val l = if (width > height) height else width
        val cx = mCx ?: (l / 2f)
        val cy = mCy ?: (l / 2f)
        val r = mRadius ?: (l / 2f)
        canvas?.drawCircle(cx, cy, r, mCirclePaint)

        if (mShowState) {
            if (mState == STATE_CHECK) {
                drawCheckCircle(canvas, cx, cy, r)
            } else {
                drawStateFork(canvas, cx, cy, r)
            }
        }
        drawTimeLine(canvas, cx, cy + r, cx, height.toFloat())
    }

    private fun drawTimeLine(canvas: Canvas?, startX: Float, startY: Float, endX: Float, endY: Float) {
        canvas?.drawLine(startX, startY, endX, endY, mLinePaint)
    }

    private fun drawStateFork(canvas: Canvas?, cx: Float, cy: Float, r: Float) {

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
            width = ((mRadius ?: 0f) * 2 + paddingStart + paddingEnd + mCirclePaint.strokeWidth).toInt()
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            height = (mRadius?.toInt() ?: 0) * 2 + paddingTop + paddingBottom + defaultLineHeight
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

    fun setCirclePaintStyle(style: Style) = apply {
        mCirclePaint.style = style
        invalidate()
    }

    fun setShowState(show: Boolean) = apply {
        if (mShowState != show) {
            mShowState = show
            invalidate()
        }
    }

    /**
     * 设置当前节点
     */
    fun setDefaultCurrentNode() = apply {
        mShowState = false
        mCirclePaint.style = Style.STROKE
        mCirclePaint.color = Color.parseColor("#367ef4")
        mLinePaint.color = Color.parseColor("#e5e5e4")
        invalidate()
    }

    fun setDefaultCheckNode() = apply {
        mShowState = true
        mState = STATE_CHECK
        mCirclePaint.style = Style.FILL
        mCirclePaint.color = Color.parseColor("#367ef4")
        mLinePaint.color = Color.parseColor("#367ef4")
        invalidate()
    }

    fun setDefaultNotCheckNode() = apply {
        mShowState = false
        mCirclePaint.style = Style.STROKE
        mCirclePaint.color = Color.parseColor("#e5e5e4")
        mLinePaint.color = Color.parseColor("#e5e5e4")
        invalidate()
    }

    fun setDotRadius(radius: Int) = apply {
        mRadius = radius.toFloat()
        invalidate()
    }


}