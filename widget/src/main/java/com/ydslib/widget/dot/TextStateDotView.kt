package com.ydslib.widget.dot

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.ydslib.widget.R

class TextStateDotView : View {

    private var mDotViewRadius: Int = 0

    private var mDotText: CharSequence? = null

    private var mDotTextSize: Int = 16

    private var mCurTextColor: Int = Color.BLACK

    private var mTextPaddingHorizontal = 30

    private var mTextPaddingVertical = 20


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

    /**
     * 画文本
     */
    private val mTextPaint by lazy {
        Paint()
    }

    /**
     * 文本的背景色
     */
    private var mTextBgColor: Int = -1

    /**
     *
     */
    private var mDotBgColor: Int = -1

    /***
     * 画外形
     */
    private val mTextShapePaint by lazy {
        Paint()
    }

    private var mStateStrokeWidth = -1


    private val mBound by lazy { Rect() }

    private val mRoundRectF by lazy {
        RectF()
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TextStateDotView)
        mDotViewRadius = a.getDimensionPixelSize(R.styleable.TextStateDotView_dotViewRadius, 0)
        mDotText = a.getString(R.styleable.TextStateDotView_dotViewText)
        mDotTextSize = a.getDimensionPixelSize(R.styleable.TextStateDotView_dotTextSize, -1)
        mCurTextColor = a.getColor(R.styleable.TextStateDotView_dotTextColor, Color.WHITE)
        mTextPaddingHorizontal = a.getDimensionPixelSize(R.styleable.TextStateDotView_textPaddingHorizontal, 30)
        mTextPaddingVertical = a.getDimensionPixelSize(R.styleable.TextStateDotView_textPaddingVertical, 20)
        mStateStrokeWidth = a.getDimensionPixelSize(R.styleable.TextStateDotView_stateStrokeWidth, -1)
        initDefaultStatePaint()
        initCirclePaint()
        initDefaultTextPaint()
        initDefaultTextShapePaint()
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        mTextPaint.getTextBounds(mDotText.toString(), 0, mDotText?.length ?: 0, mBound)

        if (widthMode == MeasureSpec.AT_MOST) {
            width = mBound.width() + paddingStart + paddingEnd + mDotViewRadius + mTextPaddingHorizontal * 2
        }
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            height = mBound.height() + paddingTop + paddingBottom + mDotViewRadius + mTextPaddingVertical * 2
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //画外形
        val left = (width - mBound.width() - mTextPaddingHorizontal * 2 - mDotViewRadius) / 2f
        val top = (mDotViewRadius + paddingTop).toFloat()
        val right = (width - left - mDotViewRadius)
        val bottom = height - (height - mBound.height() - mTextPaddingVertical * 2 - mDotViewRadius) / 2f
        mRoundRectF.apply {
            this.left = left
            this.top = top
            this.right = right
            this.bottom = bottom
        }
        canvas?.drawRoundRect(mRoundRectF, 8f, 8f, mTextShapePaint)
        //画文本
        if (!mDotText.isNullOrEmpty()) {
            canvas?.drawText(
                mDotText.toString(),
                (width - mBound.width() - mDotViewRadius) / 2.0f,
                (paddingTop + mBound.height() + mDotViewRadius + mTextPaddingVertical).toFloat(),
                mTextPaint
            )
        }


        //画圆
        val cx = right
        val cy = top //cy = 30 mCy = 30  height = 60
        val r = mDotViewRadius.toFloat()
        canvas?.drawCircle(cx, cy, r, mCirclePaint)

        val widthDivide = mDotViewRadius / 20f
        val heightDivide = mDotViewRadius / 20f

        val baseX = cx - mDotViewRadius
        val baseY = cy - mDotViewRadius

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


    /**
     * 画圆
     */
    private fun initCirclePaint() {
        mCirclePaint.color = Color.RED
    }

    /**
     * 圆中间图案的宽度，比如对钩的宽度
     */
    private fun initDefaultStatePaint() {
        mStatePaint.strokeWidth = if (mStateStrokeWidth == -1) mDotViewRadius / 3.0f else mStateStrokeWidth.toFloat()
        mStatePaint.strokeCap = Paint.Cap.ROUND
        mStatePaint.isAntiAlias = true
        mStatePaint.color = Color.WHITE
    }

    /**
     * 画文本
     */
    private fun initDefaultTextPaint() {
        mTextPaint.color = mCurTextColor
        mTextPaint.isAntiAlias = true
        mTextPaint.strokeWidth = 5f
        mTextPaint.style = Paint.Style.FILL
        mTextPaint.textAlign = Paint.Align.LEFT
        mTextPaint.textSize = mDotTextSize.toFloat()
        mTextPaint.getTextBounds(mDotText.toString(), 0, mDotText?.length ?: 0, mBound)
    }

    /**
     * 画文本的外框
     */
    private fun initDefaultTextShapePaint() {
        mTextShapePaint.color = Color.GREEN
        mTextPaint.isAntiAlias = true
        mTextPaint.strokeWidth = 5f
    }

    fun setDotText(dotText: CharSequence?) = apply {
        mDotText = dotText
        invalidate()
    }

    fun setDotTextSize(dotTextSize: Int) = apply {
        if (mDotTextSize != dotTextSize) {
            mDotTextSize = dotTextSize
            mTextPaint.textSize = mDotTextSize.toFloat()
            invalidate()
        }
    }

    fun setDotTextColor(dotTextColor: Int) = apply {
        if (mCurTextColor != dotTextColor) {
            mCurTextColor = dotTextColor
            mTextPaint.color = dotTextColor
            invalidate()
        }
    }

    fun setDotViewRadius(radius: Int) = apply {

        mDotViewRadius = radius
        invalidate()
    }

    fun setStateStrokeWidth(stateStrokeWidth: Int) = apply {
        if (stateStrokeWidth != mStateStrokeWidth) {
            mStateStrokeWidth = stateStrokeWidth
            mStatePaint.strokeWidth = stateStrokeWidth.toFloat()
            invalidate()
        }
    }

    fun setTextBgColor(textBgColor: Int) = apply {
        if (textBgColor != mTextBgColor) {
            mTextBgColor = textBgColor
            mTextShapePaint.color = textBgColor
            invalidate()
        }
    }

    fun setDotBgColor(dotBgColor: Int) = apply {
        if (dotBgColor != mDotBgColor) {
            mDotBgColor = dotBgColor
            mCirclePaint.color = dotBgColor
            invalidate()
        }
    }

}