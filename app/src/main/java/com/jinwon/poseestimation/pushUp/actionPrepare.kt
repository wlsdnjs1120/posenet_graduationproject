package com.jinwon.poseestimation.pushUp
//import android.graphics.Bitmap
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.Paint
//import camera.hj.cameracontroller.CameraApplication
//import camera.hj.cameracontroller.constant.Settings
//import camera.hj.cameracontroller.controller.event.PushUpToastEvent
//import camera.hj.cameracontroller.utils.GCUtils
//import camera.hj.cameracontroller.utils.SizeUtils
//import de.greenrobot.event.EventBus
//
///**
// *
// */
//class actionPrepare(workLine: WorkLine?, flag: WorkingFlag?) : AbstractPattern(workLine, flag) {
//    var CocoPairs = arrayOf(intArrayOf(2, 3), intArrayOf(3, 4), intArrayOf(5, 6), intArrayOf(6, 7))
//    private val BodyPoints = intArrayOf(2, 3, 4, 5, 6, 7)
//    private val timeUse: Long = 0
//    fun init(resource: Bitmap?, eigen: IntArray?): IntArray {
//        return IntArray(36)
//    }
//
//    fun update(resource: Bitmap?): IntArray? {
//        return null
//    }
//
//    fun draw(resource: Bitmap?, eigen: IntArray) {
//        val canvas = Canvas(resource)
//        val paint = Paint()
//        paint.color = Color.GREEN
//        paint.style = Paint.Style.STROKE
//        paint.strokeWidth = 2f
//        for (i in BodyPoints.indices) {
//            val p1 = BodyPoints[i]
//            if (eigen[2 * p1] != 0 && eigen[2 * p1 + 1] != 0) {
//                canvas.drawCircle(eigen[2 * p1].toFloat(), eigen[2 * p1 + 1].toFloat(), 3f, paint)
//            }
//        }
//    }
//
//    fun run() {
//        val runModel: RunModel = CameraApplication.getRunModel()
//        while (true) {
//            val temp: Bitmap = mWorkLine.getPrepareData()
//            val result: IntArray = runModel.detect(temp)
//            var isAllPointsDetected = true
//            for (i in BodyPoints.indices) {
//                val index = BodyPoints[i]
//                if (result[index * 2] == 0 || result[index * 2 + 1] == 0) {
//                    isAllPointsDetected = false
//                }
//            }
//            if (isAllPointsDetected) {
//                CameraApplication.getFwcCounter().mCounter.initBodyPoints = result
//                draw(temp, result)
//                CameraApplication.getFwcCounter().mCounter.update(result)
//                if (CameraApplication.getFwcCounter().mCounter.isLegal) {
//                    CameraApplication.getFwcCounter().mCounter.isReady = true
//                    break
//                } else {
//                    //준비가 안되면 바로 프론트 프레임으로
//                    mWorkLine.clearPrepareData()
//                }
//                CameraApplication.getFwcCounter().mCounter.isReady = true
//                break
//            } else {
//                // 준비가 안되면 프론트 프레임으로
//                mWorkLine.clearPrepareData()
//            }
//        }
//    }
//}