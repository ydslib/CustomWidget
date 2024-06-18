package com.ydslib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.view.marginStart

class CTextView : View {

    private var mText: String? = null

    //默认18像素
    private var mTextColor: Int = 18

    //默认颜色
    private var mCurTextColor: Int = Color.BLACK

    private var mTextSize: Int = 16

    private val mPaint by lazy {
        Paint()
    }

    private val mBound by lazy { Rect() }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CTextView)
        mCurTextColor = a.getColor(R.styleable.CTextView_textColor, Color.BLACK)
        mTextSize = a.getDimensionPixelSize(R.styleable.CTextView_textSize, -1)

        initDefaultTextPaint()
        a.recycle()
    }

    private fun initDefaultTextPaint() {
        mPaint.color = Color.BLACK
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = 5f
        mPaint.style = Paint.Style.FILL
        mPaint.textAlign = Paint.Align.LEFT
        mPaint.textSize = mTextSize.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

//        val fontMetricsInt = mPaint.fontMetricsInt
//        val dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom
//        val baseLine = height / 2 + dy
//        if (!mText.isNullOrEmpty()) {
//            canvas?.drawText(mText!!, paddingStart * 1.0f, baseLine * 1.0f, mPaint)
//        }


        if (!mText.isNullOrEmpty()) {
            mPaint.getTextBounds(mText, 0, mText!!.length, mBound)
            canvas?.drawText(mText!!, (width - mBound.width()) / 2.0f, (paddingTop + mBound.height()).toFloat(), mPaint)
        }
    }

    fun setText(text: String) {
        mText = text
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode == MeasureSpec.AT_MOST) {
            if (!mText.isNullOrEmpty()) {
                mPaint.getTextBounds(mText, 0, mText!!.length, mBound)
                // getPaddingStart()+getPaddingEnd()不添加这个在页面布局中添加padding值是无效的
                widthSize = mBound.width() + paddingStart + paddingEnd
            }
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            if (!mText.isNullOrEmpty()) {
                mPaint.getTextBounds(mText, 0, mText!!.length, mBound)
                heightSize = mBound.height() + paddingTop + paddingBottom
            }
        }

        setMeasuredDimension(widthSize, heightSize)
    }

}