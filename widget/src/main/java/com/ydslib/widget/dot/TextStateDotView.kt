package com.ydslib.widget.dot

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import com.ydslib.widget.R

class TextStateDotView : FrameLayout {

    private var mDotViewRadius: Int = 0

    private var defaultStrokeWidth = 7f

    private var mDotText: CharSequence? = null

    private var mDotTextSize: Int = 16

    private var mCurTextColor: Int = Color.BLACK

    private var mDotView: StateDotView? = null

    private var mTextView: TextView? = null

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


    private val mGradientDrawable: GradientDrawable by lazy { GradientDrawable() }

    private val mBound by lazy { Rect() }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TextStateDotView)
        mDotViewRadius = a.getDimensionPixelSize(R.styleable.TextStateDotView_dotViewRadius, 0)
        mDotText = a.getString(R.styleable.TextStateDotView_dotViewText)
        mDotTextSize = a.getDimensionPixelSize(R.styleable.TextStateDotView_dotTextSize, -1)
        mCurTextColor = a.getColor(R.styleable.TextStateDotView_dotTextColor, Color.WHITE)
        initDefaultStatePaint()
        initCirclePaint()
        initTextView()
        initDotView()
//        post {
//            val cx = paddingStart.toFloat().plus((mTextView?.width?.toFloat() ?: 0f))
//            val cy = mTextView?.top?.toFloat() ?: 0f
//            val radius = paddingEnd / 2.0f
//            mDotView?.setCircleCenter(cx, cy, radius)
//        }
        a.recycle()
    }

    private fun initDotView() {
        mDotView = StateDotView(this.context)
        mTextView?.paint?.getTextBounds(mTextView?.text.toString(), 0, mTextView?.text?.length ?: 0, mBound)
        val cx = mBound.width().toFloat()
        val cy = paddingTop.toFloat()
        mDotView?.setCircleCenter(cx, cy, paddingTop.toFloat() / 2)
        addView(mDotView)
    }


    private fun initTextView() {
        mTextView = TextView(this.context)
        mTextView?.setTextColor(mCurTextColor)
        mTextView?.textSize = mDotTextSize.toFloat()
        mTextView?.text = mDotText
        mTextView?.layoutParams = FrameLayout.LayoutParams(-2, -2)
        mGradientDrawable.setColor(Color.BLUE)
        mTextView?.background = mGradientDrawable
        addView(mTextView)
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


//        //画圆
//        val cx = (width - mBound.width()).toFloat() / 2 + mBound.width()
//        val cy = (height - mBound.height()).toFloat()/2 + mBound.height()
//        if (cx < cy) {
//            canvas?.drawCircle(cx, cy, mDotViewRadius.toFloat(), mCirclePaint)
//        } else {
//            canvas?.drawCircle(cx, cy, mDotViewRadius.toFloat(), mCirclePaint)
//        }
//
//
//        val widthDivide = (2 * mDotViewRadius) / 40f
//        val heightDivide = (2 * mDotViewRadius) / 40f
//
//        //前半截
//        var startX = 11 * widthDivide
//        var startY = 18 * heightDivide
//        var stopX = 18 * widthDivide
//        var stopY = 24 * heightDivide
//        canvas?.drawLine(startX, startY, stopX, stopY, mStatePaint)
//
//        //后半截
//        startX = stopX
//        startY = stopY
//        stopX = 29 * widthDivide
//        stopY = 14 * heightDivide
//        canvas?.drawLine(startX, startY, stopX, stopY, mStatePaint)
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
//        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
//        var width = MeasureSpec.getSize(widthMeasureSpec)
//        var height = MeasureSpec.getSize(heightMeasureSpec)
//        if (widthMode == MeasureSpec.AT_MOST) {
//            width = mDotViewRadius + paddingStart + paddingEnd
//        }
//        if (heightMode == MeasureSpec.AT_MOST) {
//            height = mDotViewRadius + paddingTop + paddingBottom
//        }
//        setMeasuredDimension(width, height)
//    }

}