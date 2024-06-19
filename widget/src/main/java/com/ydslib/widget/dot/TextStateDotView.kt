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
import kotlin.math.ceil

class TextStateDotView : View {

    /**
     * 圆的半径
     */
    private var mDotViewRadius: Int = 0

    /**
     * 按钮文本
     */
    private var mDotText: CharSequence? = null

    /**
     * 文本的字体大小
     */
    private var mDotTextSize: Int = 16

    /**
     * 文本的字体颜色
     */
    private var mCurTextColor: Int = Color.BLACK

    /**
     * 文本的水平padding
     */
    private var mTextPaddingHorizontal = 30

    /**
     * 文本的垂直padding
     */
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
     * 圆点的背景色
     */
    private var mDotBgColor: Int = -1

    /***
     * 画外形
     */
    private val mTextShapePaint by lazy {
        Paint()
    }

    /**
     * 圆点中间图案的线条宽度
     */
    private var mStateStrokeWidth = -1


    /**
     * 为获取文字总长度
     */
    private val mBound by lazy { Rect() }

    /**
     * 为获取一行文字长度
     */
    private val mLineTextBound by lazy { Rect() }

    /**
     * 用于带圆角的方形
     */
    private val mRoundRectF by lazy {
        RectF()
    }

    /**
     * 屏幕高度
     */
    private var mScreenHeight: Int = -1

    /**
     * 屏幕宽度
     */
    private var mScreenWidth: Int = -1

    /**
     * 每行文字绘制的高度
     */
    private var drawTextHeight = 0

    /**
     * 文本列表
     */
    private val mTextList = ArrayList<String>()

    /**
     * 行数
     */
    private var lineNum = 0

    /**
     * 半径，用于记录上一次的半径
     */
    private var mRadius = 0

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

        mRadius = mDotViewRadius

        initDefaultStatePaint()
        initCirclePaint()
        initDefaultTextPaint()
        initDefaultTextShapePaint()
        initDefaultData()
        a.recycle()
    }

    private fun initDefaultData() {
        val dm = resources.displayMetrics
        mScreenHeight = dm.heightPixels
        mScreenWidth = dm.widthPixels
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        mTextPaint.getTextBounds(mDotText.toString(), 0, mDotText?.length ?: 0, mBound)

        //文字的宽度
        val mTextWidth = mBound.width()
        //每次测量重新添加
        mTextList.clear()

        lineNum = 0

        //文本不为空
        var mText = mDotText.toString()
        if (mText.isNotEmpty()) {
            val paddingHorizontal = paddingStart + paddingEnd + mDotViewRadius + mTextPaddingHorizontal * 2
            //可显示文本的最大宽度
            val specMaxWidth = width - paddingHorizontal
            if (specMaxWidth >= mTextWidth) {
                lineNum = 1
                mTextList.add(mText)
            } else { //超过一行，切割
                val maxLine = mTextWidth * 1.0f / specMaxWidth
                //向上取整
                lineNum = ceil(maxLine).toInt()
                //每行展示文字的长度，除以maxLine，则每行以最大数展示，除以lineNum，则平分所有字数
                val lineLength = (mText.length / maxLine).toInt()
                for (i in 0 until lineNum) {
                    var lineStr = ""
                    lineStr = if (mText.length <= lineLength) {
                        mText
                    } else {
                        mText.substring(0, lineLength)
                    }
                    mTextList.add(lineStr)
                    if (mText.isNotEmpty()) {
                        mText = if (mText.length <= lineLength) "" else mText.substring(lineLength, mText.length)
                    }
                }
            }
        }


        if (widthMode == MeasureSpec.AT_MOST) {
            val textWidth = if (mTextList.size > 1) {
                mTextPaint.getTextBounds(mTextList[0], 0, mTextList[0].length, mLineTextBound)
                mLineTextBound.width()
            } else {
                mTextWidth
            }
            width = textWidth + paddingStart + paddingEnd + mDotViewRadius + mTextPaddingHorizontal * 2
        }
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            val textHeight = mTextList.size * (mBound.bottom - mBound.top)
            height = textHeight + paddingTop + paddingBottom + mDotViewRadius + mTextPaddingVertical * 2
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (mTextList.isNotEmpty()) {
            //获取文字的Rect
            mTextPaint.getTextBounds(mTextList[0], 0, mTextList[0].length, mLineTextBound)
            val right = (width - mDotViewRadius).toFloat()
            val top = (mDotViewRadius + paddingTop).toFloat()
            //画外形
            val bottom =
                height - (height - mLineTextBound.height() * mTextList.size - mTextPaddingVertical * 2 - mDotViewRadius) / 2f
            mRoundRectF.apply {
                this.left = 0f
                this.top = top
                this.right = right
                this.bottom = bottom
            }
            canvas?.drawRoundRect(mRoundRectF, 8f, 8f, mTextShapePaint)
            for (i in 0 until mTextList.size) {
                //文案居中显示
                canvas?.drawText(
                    mTextList[i],
                    (width - mLineTextBound.width() - mDotViewRadius) / 2f,
                    (paddingTop + mDotViewRadius + mTextPaddingVertical + mLineTextBound.height() * (i + 1)).toFloat(),
                    mTextPaint
                )
            }

            //画圆
            val cx = right
            val cy = top //cy = 30 mCy = 30  height = 60
            drawCheckCircle(canvas, cx, cy)
        }

    }

    /**
     * 画带对钩的圆
     */
    private fun drawCheckCircle(canvas: Canvas?, cx: Float, cy: Float) {

        val r = mDotViewRadius.toFloat()
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
        val fm = mTextPaint.fontMetrics
        drawTextHeight = (fm.descent - fm.ascent).toInt()
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
        mRadius = mDotViewRadius
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

    fun showDot(showDot: Boolean) = apply {
        mDotViewRadius = if (showDot) {
            mRadius
        } else {
            0
        }
        invalidate()
    }

}