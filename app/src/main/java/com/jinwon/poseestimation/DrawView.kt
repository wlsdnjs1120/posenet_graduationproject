
package com.jinwon.poseestimation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style.FILL
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import java.util.ArrayList

/**
 * Created by edvard on 18-3-23.
 */

class DrawView : View {

    private var mRatioWidth = 0
    private var mRatioHeight = 0

    private val mDrawPoint = ArrayList<PointF>()
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mRatioX: Float = 0.toFloat()
    private var mRatioY: Float = 0.toFloat()
    private var mImgWidth: Int = 0
    private var mImgHeight: Int = 0

    // 필요없는 코, 두 눈, 양쪽 귀 제외시키고 top,neck 추가 해서 총 15개
    private val mColorArray = intArrayOf(
            resources.getColor(R.color.color_top, null),    // 0
            resources.getColor(R.color.color_neck, null),   // 1
            resources.getColor(R.color.color_l_shoulder, null), // 2
            resources.getColor(R.color.color_l_elbow, null),    // 3
            resources.getColor(R.color.color_l_wrist, null),    // 4
            resources.getColor(R.color.color_r_shoulder, null), //5
            resources.getColor(R.color.color_r_elbow, null),     //6
            resources.getColor(R.color.color_r_wrist, null),    //7
            resources.getColor(R.color.color_l_hip, null),  //8
            resources.getColor(R.color.color_l_knee, null), //9
            resources.getColor(R.color.color_l_ankle, null),    //10
            resources.getColor(R.color.color_r_hip, null),  //11
            resources.getColor(R.color.color_r_knee, null), //12
            resources.getColor(R.color.color_r_ankle, null),    //13
            resources.getColor(R.color.color_background, null)  //14
    )

    /*
       왼쪽어깨 2 왼쪽 팔꿈치 3 왼쪽 무릎 0
       오른쪽 어깨 5 오른쪽 팔꿈치 6 오른쪽 무릎 11
    //        this.thresh_h = 140;
    //        this.thresh_l = 100;
    //        this.thresh_score = 120;
    //        this.angle = -1;
    //        this.angle_score = -1;
    //        angElbow1 = -1;
    //        angElbow2 = -1;
    //        oldElbow = new int[2];
    //    }

     */
    private val circleRadius: Float by lazy {
        dip(3).toFloat()
        //dip(3).toFloat()
    }

    private val mPaint: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            style = FILL
            strokeWidth = dip(2).toFloat()
            textSize = sp(13).toFloat()
        }
    }

    constructor(context: Context) : super(context)

    constructor(
            context: Context,
            attrs: AttributeSet?
    ) : super(context, attrs)

    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    fun setImgSize(
            width: Int,
            height: Int
    ) {
        mImgWidth = width
        mImgHeight = height
        requestLayout()
    }

    /**
     * Scale according to the device.
     * @param point 2*14
     */
    fun setDrawPoint(
            point: Array<FloatArray>,
            ratio: Float
    ) {
        mDrawPoint.clear()

        var tempX: Float
        var tempY: Float
        for (i in 0..13) {
            tempX = point[0][i] / ratio / mRatioX
            tempY = point[1][i] / ratio / mRatioY
            mDrawPoint.add(PointF(tempX, tempY))
        }
    }

    /**
     * Sets the aspect ratio for this view. The size of the view will be measured based on the ratio
     * calculated from the parameters. Note that the actual sizes of parameters don't matter, that is,
     * calling setAspectRatio(2, 3) and setAspectRatio(4, 6) make the same result.
     *
     * @param width  Relative horizontal size
     * @param height Relative vertical size
     */
    fun setAspectRatio(
            width: Int,
            height: Int
    ) {
        if (width < 0 || height < 0) {
            throw IllegalArgumentException("Size cannot be negative.")
        }
        mRatioWidth = width
        mRatioHeight = height
        requestLayout()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mDrawPoint.isEmpty()) return
        var prePointF: PointF? = null
        // 색상 변결할거면 이거 변경하면 됨.
        mPaint.color = 0xff6fa8dc.toInt()
        val p1 = mDrawPoint[1]
        for ((index, pointF) in mDrawPoint.withIndex()) {
            if (index == 1) continue
            when (index) {
                //0-1
                0 -> {
                    canvas.drawLine(pointF.x, pointF.y, p1.x, p1.y, mPaint)
                }
                // 1-2, 1-5, 1-8, 1-11
                2, 5, 8, 11 -> {
                    canvas.drawLine(p1.x, p1.y, pointF.x, pointF.y, mPaint)
                }
                else -> {
                    if (prePointF != null) {
                        mPaint.color = 0xff6fa8dc.toInt()
                        canvas.drawLine(prePointF.x, prePointF.y, pointF.x, pointF.y, mPaint)
                    }
                }
            }
            prePointF = pointF
        }

        for ((index, pointF) in mDrawPoint.withIndex()) {
            mPaint.color = mColorArray[index]
            canvas.drawCircle(pointF.x, pointF.y, circleRadius, mPaint)
        }
    }

    override fun onMeasure(
            widthMeasureSpec: Int,
            heightMeasureSpec: Int
    ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec)
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height)
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                mWidth = width
                mHeight = width * mRatioHeight / mRatioWidth
            } else {
                mWidth = height * mRatioWidth / mRatioHeight
                mHeight = height
            }
        }

        setMeasuredDimension(mWidth, mHeight)

        mRatioX = mImgWidth.toFloat() / mWidth
        mRatioY = mImgHeight.toFloat() / mHeight
    }
    /*
      // To solve mirror problem on front camera
          if (x > 320) {
            var temp = x - 320;
            x = 320 - temp;
          } else {
            var temp = 320 - x;
            x = 320 + temp;
          }
          return Positioned(
            left: x - 275,
            top: y - 50,
            width: 100,
            height: 15,
            child: Container(
              child: Text(
                "●",
                style: TextStyle(
                  color: Color.fromRGBO(37, 213, 253, 1.0),
                  fontSize: 12.0,
                ),
              ),
            ),
          );
        }).toList();
     */
}
