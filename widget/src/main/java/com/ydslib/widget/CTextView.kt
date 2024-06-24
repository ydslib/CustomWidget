package com.ydslib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import kotlin.math.ceil
import kotlin.math.floor

class CTextView : View {

    private var mText: String? = null

    //默认18像素
    private var mTextColor: Int = 18

    //默认颜色
    private var mCurTextColor: Int = Color.BLACK

    private var mTextSize: Int = 16

    private var drawTextHeight: Int = 0

    private var mTextBaseLine: Float = 0f

    private var mGravity = Gravity.TOP or Gravity.START

    private val mPaint by lazy {
        Paint()
    }

    /**
     * 屏幕高度
     */
    private var mScreenHeight: Int = -1

    /**
     * 屏幕宽度
     */
    private var mScreenWidth: Int = -1

    private val mBound by lazy { Rect() }

    private val mTmpBound by lazy { Rect() }

    private val mDrawLinePaint by lazy { Paint() }

    private val mTextList by lazy {
        ArrayList<String>()
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CTextView)
        mCurTextColor = a.getColor(R.styleable.CTextView_cTextColor, Color.BLACK)
        mTextSize = a.getDimensionPixelSize(R.styleable.CTextView_cTextSize, -1)
        mText = a.getString(R.styleable.CTextView_cText)
        mGravity = a.getInt(R.styleable.CTextView_cGravity, Gravity.CENTER or Gravity.START)
        initDefaultTextPaint()
        a.recycle()
    }

    private fun initDefaultTextPaint() {
        mPaint.color = mCurTextColor
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = 5f
        mPaint.style = Paint.Style.FILL
        mPaint.textAlign = Paint.Align.LEFT
        mPaint.textSize = mTextSize.toFloat()
        if (!mText.isNullOrEmpty()) {
            mPaint.getTextBounds(mText, 0, mText!!.length, mBound)
        }

        val fm = mPaint.fontMetricsInt
        drawTextHeight = fm.descent - fm.ascent
        // drawTextHeight + paddingTop = baseY + fm.bottom

        mDrawLinePaint.color = Color.RED
        mDrawLinePaint.strokeWidth = 2f
        initDefaultData()
    }

    private fun initDefaultData() {
        val dm = resources.displayMetrics
        mScreenHeight = dm.heightPixels
        mScreenWidth = dm.widthPixels
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val fm = mPaint.fontMetricsInt

        //左上
        if (!mText.isNullOrEmpty()) {
            mPaint.getTextBounds(mText, 0, mText!!.length, mBound)
        }

        when (mGravity) {
            //左上
            (Gravity.TOP or Gravity.START), Gravity.START -> {
                val x = paddingStart
                if (mTextList.isNotEmpty()) {
                    for (i in 0 until mTextList.size) {
                        mTextBaseLine = (fm.bottom - fm.top) * i + (paddingTop - fm.top).toFloat()
                        canvas?.drawText(mTextList[i], x.toFloat(), mTextBaseLine, mPaint)
                    }
                }
            }

            //上中
            (Gravity.TOP or Gravity.CENTER), Gravity.CENTER_HORIZONTAL -> {
                if (mTextList.isNotEmpty()) {
                    for (i in 0 until mTextList.size) {
                        mPaint.getTextBounds(mTextList[i], 0, mTextList[i].length, mTmpBound)
                        val x = (width - mTmpBound.width()) / 2f - paddingStart
                        mTextBaseLine = (fm.bottom - fm.top) * i + (paddingTop - fm.top).toFloat()
                        canvas?.drawText(mTextList[i], x, mTextBaseLine, mPaint)
                    }
                }
            }
            //右上
            (Gravity.TOP or Gravity.END), Gravity.END -> {
                if (mTextList.isNotEmpty()) {
                    for (i in 0 until mTextList.size) {
                        mPaint.getTextBounds(mTextList[i], 0, mTextList[i].length, mTmpBound)
                        val x = (width - mTmpBound.width() - paddingEnd).toFloat()
                        mTextBaseLine = (fm.bottom - fm.top) * i + (paddingTop - fm.top).toFloat()
                        canvas?.drawText(mTextList[i], x, mTextBaseLine, mPaint)
                    }
                }
            }

            //右中
            (Gravity.END or Gravity.CENTER) -> {
                if (mTextList.isNotEmpty()) {
                    //文本总高度
                    val totalHeight = (fm.bottom - fm.top) * mTextList.size
                    //文本居中时上面空白
                    val topSpace = (height - totalHeight) / 2f

                    for (i in 0 until mTextList.size) {
                        mPaint.getTextBounds(mTextList[i], 0, mTextList[i].length, mTmpBound)
                        val x = (width - mTmpBound.width() - paddingEnd).toFloat()
                        mTextBaseLine = (fm.bottom - fm.top) * i + topSpace - fm.top
                        canvas?.drawText(mTextList[i], x, mTextBaseLine, mPaint)
                    }
                }
            }

            //居中
            Gravity.CENTER -> {
                if (mTextList.isNotEmpty()) {
                    //文本总高度
                    val totalHeight = (fm.bottom - fm.top) * mTextList.size
                    //文本居中时上面空白
                    val topSpace = (height - totalHeight) / 2f

                    for (i in 0 until mTextList.size) {
                        mPaint.getTextBounds(mTextList[i], 0, mTextList[i].length, mTmpBound)
                        val x = (width - mTmpBound.width()) / 2f - paddingStart
                        mTextBaseLine = (fm.bottom - fm.top) * i + topSpace - fm.top
                        canvas?.drawText(mTextList[i], x, mTextBaseLine, mPaint)
                    }
                }
            }

            //右下
            (Gravity.END or Gravity.BOTTOM) -> {
                if (mTextList.isNotEmpty()) {
                    //文本总高度
                    val totalHeight = (fm.bottom - fm.top) * mTextList.size
                    for (i in 0 until mTextList.size) {
                        mPaint.getTextBounds(mTextList[i], 0, mTextList[i].length, mTmpBound)
                        val x = (width - mTmpBound.width() - paddingEnd).toFloat()
                        mTextBaseLine = ((fm.bottom - fm.top) * i + height - totalHeight - paddingBottom - fm.top).toFloat()
                        canvas?.drawText(mTextList[i], x, mTextBaseLine, mPaint)
                    }
                }
            }

            //下中
            (Gravity.BOTTOM or Gravity.CENTER) -> {
                if (mTextList.isNotEmpty()) {
                    //文本总高度
                    val totalHeight = (fm.bottom - fm.top) * mTextList.size
                    for (i in 0 until mTextList.size) {
                        mPaint.getTextBounds(mTextList[i], 0, mTextList[i].length, mTmpBound)
                        val x = (width - mTmpBound.width()) / 2.0f - paddingStart
                        mTextBaseLine = ((fm.bottom - fm.top) * i + height - totalHeight - paddingBottom - fm.top).toFloat()
                        canvas?.drawText(mTextList[i], x, mTextBaseLine, mPaint)
                    }
                }
            }

            //左下
            (Gravity.START or Gravity.BOTTOM) -> {
                if (mTextList.isNotEmpty()) {
                    val totalHeight = (fm.bottom - fm.top) * mTextList.size
                    for (i in 0 until mTextList.size) {
                        mPaint.getTextBounds(mTextList[i], 0, mTextList[i].length, mTmpBound)
                        val x = paddingStart.toFloat()
                        mTextBaseLine = ((fm.bottom - fm.top) * i + height - totalHeight - paddingBottom - fm.top).toFloat()
                        canvas?.drawText(mTextList[i], x, mTextBaseLine, mPaint)
                    }
                }
            }

            //左中
            (Gravity.CENTER or Gravity.START), Gravity.CENTER_VERTICAL -> {
                if (mTextList.isNotEmpty()) {
                    val totalHeight = (fm.bottom - fm.top) * mTextList.size
                    for (i in 0 until mTextList.size) {
                        mPaint.getTextBounds(mTextList[i], 0, mTextList[i].length, mTmpBound)
                        val x = paddingStart.toFloat()
                        mTextBaseLine = (fm.bottom - fm.top) * i + (height - totalHeight) / 2.0f + paddingTop - fm.top
                        canvas?.drawText(mTextList[i], x, mTextBaseLine, mPaint)
                    }
                }
            }

            else -> {
                if (mTextList.isNotEmpty()) {
                    val totalHeight = (fm.bottom - fm.top) * mTextList.size
                    for (i in 0 until mTextList.size) {
                        mPaint.getTextBounds(mTextList[i], 0, mTextList[i].length, mTmpBound)
                        val x = paddingStart.toFloat()
                        mTextBaseLine = (fm.bottom - fm.top) * i + (height - totalHeight) / 2.0f + paddingTop - fm.top
                        canvas?.drawText(mTextList[i], x, mTextBaseLine, mPaint)
                    }
                }
            }
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

        var text = mText
        mPaint.getTextBounds(text, 0, text?.length ?: 0, mBound)
        mTextList.clear()

        //文字长度
        val textMaxWidth = mBound.width()

        if (!text.isNullOrEmpty()) {

            //水平padding
            val paddingHorizontal = paddingStart + paddingEnd
            //最大宽度
            val textLineMaxWidth = widthSize - paddingHorizontal

            if (textLineMaxWidth > textMaxWidth) {
                //只有一行
                mTextList.add(text)
            } else {
                //最大行数
                val maxLine = textMaxWidth * 1.0f / textLineMaxWidth

                //一行多少个字
                val lenline = floor((text.length / maxLine)).toInt()
                //分割成多少行
                val max = ceil(maxLine).toInt()
                for (i in 0 until max) {
                    val lineStr = if (text!!.length <= lenline) {
                        text
                    } else {
                        text.substring(0, lenline)
                    }
                    mTextList.add(lineStr.trimStart())
                    text = if (text.length <= lenline) "" else text.substring(lenline, text.length)
                }
            }
        }

        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            val w = if (mTextList.size > 1) {
                mPaint.getTextBounds(mTextList[0], 0, mTextList[0].length, mBound)
                mBound.width()
            } else {
                textMaxWidth
            }
            widthSize = w + paddingStart + paddingEnd
        }
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            val fm = mPaint.fontMetricsInt
            val baseHeight = fm.bottom - fm.top
            mPaint.getTextBounds(mTextList[0], 0, mTextList[0].length, mBound)
            heightSize = mTextList.size * baseHeight + paddingTop + paddingBottom
        }

        setMeasuredDimension(widthSize, heightSize)
    }

}