
package com.jinwon.poseestimation

import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import android.app.Activity

/**
 * Pose Estimator
 * 자세추정 클래스
 */


class ImageClassifierFloatInception private constructor(
        activity: Activity,
        imageSizeX: Int,
        imageSizeY: Int,
        private val outputW: Int,
        private val outputH: Int,
        modelPath: String,
        numBytesPerChannel: Int = 4 // a 32bit float value requires 4 bytes, override val textView: Any
) : ImageClassifier(activity, imageSizeX, imageSizeY, modelPath, numBytesPerChannel) {

    /**
     * An array to hold inference results, to be feed into Tensorflow Lite as outputs.
     * This isn't part of the super class, because we need a primitive array here.
     */
    // 14개의 key point 를 저장하는 heatMapArray
    private val heatMapArray: Array<Array<Array<FloatArray>>> =
            Array(1) { Array(outputW) { Array(outputH) { FloatArray(14) } } }

    private var mMat: Mat? = null

    //    override var textView: TextView? = null
    override val textView: Any
        get() {
            TODO()
        }

    override fun addPixelValue(pixelValue: Int) {
        //bgr
        imgData!!.putFloat((pixelValue and 0xFF).toFloat())
        imgData!!.putFloat((pixelValue shr 8 and 0xFF).toFloat())
        imgData!!.putFloat((pixelValue shr 16 and 0xFF).toFloat())
    }

    override fun getProbability(labelIndex: Int): Float {
        //    return heatMapArray[0][labelIndex];
        return 0f
    }

    override fun setProbability(
            labelIndex: Int,
            value: Number
    ) {
        //    heatMapArray[0][labelIndex] = value.floatValue();
    }

    override fun getNormalizedProbability(labelIndex: Int): Float {
        return getProbability(labelIndex)
    }

    override fun runInference() {
        tflite?.run(imgData!!, heatMapArray)

        if (mPrintPointArray == null)
        /////// !-!
            mPrintPointArray = Array(2) { FloatArray(14) }

        if (!CameraActivity.isOpenCVInit)
            return

        // Gaussian Filter 5*5
        if (mMat == null)
            mMat = Mat(outputW, outputH, CvType.CV_32F)

        val tempArray = FloatArray(outputW * outputH)
        val outTempArray = FloatArray(outputW * outputH)
        for (i in 0..13) {
            var index = 0
            for (x in 0 until outputW) {
                for (y in 0 until outputH) {
                    tempArray[index] = heatMapArray[0][y][x][i]
                    index++
                }
            }

            mMat!!.put(0, 0, tempArray)
            Imgproc.GaussianBlur(mMat!!, mMat!!, Size(5.0, 5.0), 0.0, 0.0)
            mMat!!.get(0, 0, outTempArray)

            var maxX = 0f
            var maxY = 0f
            var max = 0f

            // Find keypoint coordinate through maximum values
            for (x in 0 until outputW) {
                for (y in 0 until outputH) {
                    val center = get(x, y, outTempArray)
                    if (center > max) {
                        max = center
                        maxX = x.toFloat()
                        maxY = y.toFloat()
                    }
                }
            }

            if (max == 0f) {
                mPrintPointArray = Array(2) { FloatArray(14) }
                return
            }

            mPrintPointArray!![0][i] = maxX
            mPrintPointArray!![1][i] = maxY
//      Log.i("TestOutPut", "pic[$i] ($maxX,$maxY) $max")
        }
    }

    private operator fun get(
            x: Int,
            y: Int,
            arr: FloatArray
    ): Float {
        return if (x < 0 || y < 0 || x >= outputW || y >= outputH) -1f else arr[x * outputW + y]
    }

    companion object {

        /**
         * Create ImageClassifierFloatInception instance
         *
         * @param imageSizeX Get the image size along the x axis.
         * @param imageSizeY Get the image size along the y axis.
         * @param outputW The output width of model
         * @param outputH The output height of model
         * @param modelPath Get the name of the model file stored in Assets.
         * @param numBytesPerChannel Get the number of bytes that is used to store a single
         * color channel value.
         */
        fun create(
                activity: Activity,
                imageSizeX: Int = 192,
                imageSizeY: Int = 192,
                outputW: Int = 96,
                outputH: Int = 96,
                modelPath: String = "model.tflite",
                numBytesPerChannel: Int = 4
        ): ImageClassifierFloatInception =
                ImageClassifierFloatInception(
                        activity,
                        imageSizeX,
                        imageSizeY,
                        outputW,
                        outputH,
                        modelPath,
                        numBytesPerChannel)
    }

    //==============================================================================================

    protected var flag1: Int = 0
    protected var flag2 = 0
    protected var flag_from = 0
    var count = 0
    protected var count2 = 0
    protected var score = 0.0
    val TAG = "Counting"
    var CocoPairs = arrayOf(intArrayOf(1, 2), intArrayOf(1, 5), intArrayOf(2, 3), intArrayOf(3, 4), intArrayOf(5, 6), intArrayOf(6, 7), intArrayOf(1, 8), intArrayOf(1, 11), intArrayOf(1, 0))
    //{1, 0}, {0, 14}, {14, 16}, {0, 15}, {15, 17}, {2, 16}, {5, 17}


    fun counter() {
        flag1 = 0
        flag2 = 0
        flag_from = 0
        count = 0
        count2 = 0
        score = 10.0 // 각 동작의 초기 점수 10회 측정 , 각도가 기준치를 초과할때마다 감점.
    }

    fun reset_flag() {
        flag1 = 0
        flag2 = 0
        flag_from = 0
    }

    fun get_point_by_id(keypoints: DoubleArray, id: Int): DoubleArray? {
        val keypoint = DoubleArray(2)
        try {
            keypoint[0] = keypoints[2 * id]
            keypoint[1] = keypoints[2 * id + 1]
        } catch (e: java.lang.Exception) {
            return null
        }
        return keypoint
    }

    private fun get_angle(p1: DoubleArray, p2: DoubleArray, p3: DoubleArray): Double {
        // angle = arcos(p1-p2-p3)
        var ang = -1.0
        val x2 = p2[0]
        val y2 = p2[1]
        val x3 = p3[0]
        val y3 = p3[1]
        val x1 = p1[0]
        val y1 = p1[1]
        val temp1 = (x3 - x2) * (x1 - x2) + (y3 - y2) * (y1 - y2)
        val temp2 = Math.sqrt(((x3 - x2) * (x3 - x2) + (y3 - y2) * (y3 - y2)) * ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)))
        ang = Math.acos(temp1 / temp2)
        ang = ang / Math.PI * 180
        return ang
    }

    protected fun get_vertical_angle(p2: DoubleArray, p3: DoubleArray): Double {
        var ang = -1.0
        val x2 = p2[0]
        val y2 = p2[1]
        val x3 = p3[0]
        val y3 = p3[1]
        val y1 = 0.0
        val p1 = DoubleArray(2)
        p1[0] = x2
        p1[1] = y1
        ang = get_angle(p1, p2, p3)
        //        if (Math.abs(ang)>90){
//            ang = 180 - Math.abs(ang);
//        }
        return ang
    }

    protected fun get_horizen_angle(p2: DoubleArray, p3: DoubleArray): Double {
        var ang = -1.0
        val x2 = p2[0]
        val y2 = p2[1]
        val x3 = p3[0]
        val y3 = p3[1]
        val x1 = 0.0
        val p1 = DoubleArray(2)
        p1[0] = x1
        p1[1] = y2
        ang = get_angle(p1, p2, p3)
        //        if (Math.abs(ang)>90){
//            ang = 180 - Math.abs(ang);
//        }
        return ang
    }

    protected fun get_distance(p1: DoubleArray, p2: DoubleArray): Double {
        val x2 = p2[0]
        val y2 = p2[1]
        val x1 = p1[0]
        val y1 = p1[1]
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
    }


    //    class fwc_counter extends ImageClassifier
//    {
    var thresh_h = 0.0 // <손-팔꿈치-어깨> 각도가 이 최소보다 크면 카운트

    var thresh_l = 0.0// <<손-팔꿈치-어깨> 각도가 이 최소보다 작으면 카운트

    var thresh_score = 0.0// <손-어깨-반대 어깨> 각도가 이 값을 넘으면 감점한다.

    var angle = 0.0// 각도<손-팔꿈치-어깨>

    var angle_score = 0.0// score의 각도<손-어깨-다른 어깨>


    // 추적 알고리즘 팔꿈치점 3번 6번
    var angElbow1 = 0.0
    var angElbow2 = 0.0
    lateinit var oldElbow: IntArray

    val angThresh = 10.0 // 좌우 차이가 angle Thresh 보다 크면 교정해야함.


    fun fwc_counter() {
//        super()
        this.thresh_h = 140.0
        this.thresh_l = 100.0
        this.thresh_score = 120.0
        this.angle = -1.0
        this.angle_score = -1.0
        angElbow1 = -1.0
        angElbow2 = -1.0
        oldElbow = IntArray(2)
    }


    fun update(keypoint: IntArray): Int {
        // single img : 1person-1pt_dict , default count N0.0-person's action
        // find Hip ~ Knee
        val keypoints = DoubleArray(keypoint.size)
        for (i in keypoint.indices) {
            keypoints[i] = keypoint[i].toDouble()
        }
        var lshoudlder: DoubleArray? = DoubleArray(2)
        var lelbow: DoubleArray? = DoubleArray(2)
        var lwrist: DoubleArray? = DoubleArray(2)
        var rshoudlder: DoubleArray? = DoubleArray(2)
        var relbow: DoubleArray? = DoubleArray(2)
        var rwrist: DoubleArray? = DoubleArray(2)
        var langle = 0.0
        var rangle = 0.0
        return try {
            // 좌우 팔이 모두 검출되었다면
            //   // 각도angle = arcos(p1-p2-p3)
            var flagL = 0
            var flagR = 0
            if (get_point_by_id(keypoints, 2) != null && get_point_by_id(keypoints, 4) != null && get_point_by_id(keypoints, 3) != null) {
                lshoudlder = get_point_by_id(keypoints, 2)
                lwrist = get_point_by_id(keypoints, 4)
                lelbow = get_point_by_id(keypoints, 3)
//                langle = get_angle(lshoudlder, lelbow, lwrist)
                flagL = 1
            }
            if (get_point_by_id(keypoints, 5) != null && get_point_by_id(keypoints, 7) != null && get_point_by_id(keypoints, 6) != null) {
                rshoudlder = get_point_by_id(keypoints, 5)
                rwrist = get_point_by_id(keypoints, 7)
                relbow = get_point_by_id(keypoints, 6)
//                rangle = get_angle(rshoudlder, relbow, rwrist)
                flagR = 1
            }

            // elbow 팔꿈치
            this.angElbow1 = this.angElbow2
            this.angElbow2 = (langle + rangle) / 2.toDouble()
            if (this.angElbow1 < 0) {
                this.angElbow1 = this.angElbow2
                this.angle = angElbow2
            } else {
                // 좌우의 각도의 차이가 기준치보다 높으면 수정/교정.
                val angDel = Math.abs(langle - rangle)
                if (angDel > this.angThresh) {
                    var indexElbow = 3
                    var indexShoulder = 2
                    var indexElbowReplace = 6
                    var indexShoulderReplace = 5
                    if ((this.angElbow2 - this.angElbow1) as Double <= 0) {
                        // 각도가 낮아요.
                        this.angle = Math.min(langle, rangle)
                        if (langle > rangle) {
                            indexElbow = 6
                            indexShoulder = 5
                            indexElbowReplace = 3
                            indexShoulderReplace = 2
                        }
                    } else if ((this.angElbow2 - this.angElbow1) as Double > 0) {
                        // 각도를 높여요
                        this.angle = Math.max(langle, rangle)
                        if (langle < rangle) {
                            indexElbow = 6
                            indexShoulder = 5
                            indexElbowReplace = 3
                            indexShoulderReplace = 2
                        }
                    }
//                    // 교정할 포인트를 저장한다.
//                    val ptReplace: DoubleArray = get_point_by_id(keypoints, indexElbowReplace)
//                    this.oldElbow.get(0) = ptReplace[0].toInt()
//                    this.oldElbow.get(1) = ptReplace[1].toInt()
                    // 교정할 좌표
                    keypoint[2 * indexElbowReplace] = keypoint[2 * indexShoulderReplace] + keypoint[2 * indexShoulder] - keypoint[2 * indexElbow]
                    keypoint[2 * indexElbowReplace + 1] = keypoint[2 * indexShoulderReplace + 1] - keypoint[2 * indexShoulder + 1] + keypoint[2 * indexElbow + 1]
                } else {
                    this.angle = this.angElbow2
                }
            }
            if (flagL + flagR < 1) {
                0 // keypoint not found
            } else {
                // update count 갯수 업데이트
                if (this.angle >= 0 && this.angle <= this.thresh_l) {
                    this.flag1 = 1
                    this.flag_from = 1
                }
                if (this.angle >= this.thresh_h) {
                    this.flag2 = 1
                    this.flag_from = 2
                }
                if (this.flag1 == 1 && this.flag2 == 1) {
                    this.count2++
                    val old_count: Int = this.count
                    this.count = Math.floor(this.count2 / 2.0).toInt()
                    if (this.flag_from == 1) {
                        this.flag2 = 0
                    }
                    if (this.flag_from == 2) {
                        this.flag1 = 0
                    }
                    if (old_count < this.count) {
                        // reset the score for next action
                        this.score = 10.0
    //                    reset_flag()

                    }
                }
                1
            }
        } catch (e: Exception) {
            -1
        }
    }


//    val intent = Intent(this,Camera2BasicFragment::class.java)
//    private fun showToast(text: String) {
//        val activity = activity
//        activity?.runOnUiThread {
////            textView!!.text = count
//            textView!!.Any = count
//
//            drawView!!.invalidate()
//        }
//    }
}

