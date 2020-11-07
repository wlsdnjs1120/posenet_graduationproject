//package com.edvard.poseestimation.pushUp
//
//import android.content.Intent
//import android.hardware.camera2.CameraManager
//import android.media.MediaPlayer
//import android.os.Bundle
//import android.os.Handler
//import android.os.Message
//import android.provider.Settings
//import android.support.annotation.BinderThread
//import android.util.DisplayMetrics
//import android.util.Log
////import android.view.fragment_camera2_basic
//import android.widget.ImageView
//import android.widget.TextView
//import com.edvard.poseestimation.CameraActivity
//import com.edvard.poseestimation.R
//
//class PushUpActivity : CameraActivity() {
//    //implements CountResult.ResultListener{
////    @BindView(R.id.cameraSurface)
////    var cameraSurface: SurfaceView? = null
////
////    @BindView(R.id.BitmapSurface)
////    var BitmapSurface: SurfaceView? = null
////
////    @BindView(R.id.square)
////    var square: ImageView? = null
////
////    @BindView(R.id.result_text)
////    var result_text: TextView? = null
////
////    @BindView(R.id.action_text)
////    var action_text: TextView? = null
////
////    @BindView(R.id.time_text)
////    var time_text: TextView? = null
//
////    @
//
//    var count: TextView?= null
//    var startButton: TextView? = null
//
////    private val mPlayer = arrayOfNulls<MediaPlayer>(3)
//    private var isInit = false
//    private var playID = 0
//
//    companion object {
//        var pushUpActivity: PushUpActivity? = null
//
//        private const val CurrentStatus = false
//        const val CALCULATE_RESULT = "CALCULATE_RESULT"
//        const val TAG = "MainActivity"
//
//        init {
//            System.loadLibrary("jniKCF")
//        }
//    }
//
//    private var cameraManager: CameraManager? = null
//    protected override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }
//
//    val layoutId: Int
//        get() = R.layout.fragment_camera2_basic
//
//    fun initViews(savedInstanceState: Bundle?) {}
////    fun initToolBar() {}
//
//
////    private fun initSettings() {
////        val metrics = DisplayMetrics()
////        getWindowManager().getDefaultDisplay().getRealMetrics(metrics)
////        Settings.setVideoHeight(metrics.widthPixels)
////        Settings.setVideoWidth(metrics.heightPixels)
////    }
//
////    protected fun onDestroy() {
////        WorkLine.getInstance().clear()
////        for (i in mPlayer.indices) {
////            mPlayer[i]!!.stop()
////            mPlayer[i]!!.release()
////        }
////        super.onDestroy()
////    }
//
//    // 시간 표시
////    var handler: Handler = object : Handler() {
////        override fun handleMessage(msg: Message) {
////            super.handleMessage(msg)
////            val t = System.currentTimeMillis()
////            val min: String = ((t - CameraApplication.getFwcCounter().mCounter.timeBegin) / 60000) as Int.toString()
////            val sec: String = java.lang.String.valueOf((t - CameraApplication.getFwcCounter().mCounter.timeBegin) / 1000 % 60)
////            time_text!!.text = "Time： " + min + "min" + sec + "sec"
////        }
////    }
//
///*
//    //PlayThread를 실행해서 결과를 보여줌
//*/
////    fun onEventMainThread(event: PushUpToastEvent) {
////        val msg: String = event.getMsg()
////        if (msg === "display") {
////
////            val c: CountResult = CameraApplication.takeCountResultQue()
////            val s = "Count:" + java.lang.String.valueOf(c.countNum) + " Angle:" + c.angle as Int.toString()
////            var s2 = "동작 오류：" // 동작오류
////
////            // 시간표시
////            // 실시간 표시 사용시
////            val t = ((System.currentTimeMillis() - CameraApplication.getFwcCounter().mCounter.timeBegin) / 1000) as Int
////            val min: String = (t / 60) as Int.toString()
////            val sec = (t % 60).toString()
////            time_text!!.text = "Time： " + min + "min" + sec + "sec"
////
////            // 语音提示
////            if (!isInit) {
////                isInit = true
////                playBell(2)
////            }
////            if (!c.isLegalWrist) {
////                s2 += "팔넓이가 너무 넓어요! "
////                playBell(0)
////            }
////            if (!c.isLegalElbow) {
////                s2 += " 팔꿈치가 밖으로 향해 있어요! "
////                playBell(1)
////            }
////            action_text!!.text = s2
////            result_text!!.text = s
////
////            // 훈련을 마침
////            if (c.countNum > 5) {
//////                val i = Intent(this, PushUP_ResultActivity::class.java)
////
////                startActivity(i)
////            }
////        } else {
////        }
////    }
//
////    fun playBell(index: Int) {
////        when (index) {
////            0 -> {
////                if (mPlayer[0] == null) {
////                    mPlayer[0] = MediaPlayer.create(this, R.raw.alarmhand)
////                }
////                try {
////                    if (mPlayer[0] != null) {
////                        if (!mPlayer[0]!!.isPlaying && !mPlayer[1]!!.isPlaying) {
////                            mPlayer[0]!!.start()
////                        }
////                    }
////                } catch (e: Exception) {
////                }
////                playID = 0
////            }
////            1 -> {
////                if (mPlayer[1] == null) {
//////                    mPlayer[1] = MediaPlayer.create(this, R.raw.alarmbell)
////                }
////                try {
////                    if (mPlayer[1] != null) {
////                        if (!mPlayer[0]!!.isPlaying && !mPlayer[1]!!.isPlaying) {
////                            mPlayer[1]!!.start()
////                        }
////                    }
////                } catch (e: Exception) {
////                }
////                playID = 1
////            }
////            2 -> {
////                if (mPlayer[2] == null) {
//////                    mPlayer[2] = MediaPlayer.create(this, R.raw.completebell)
////                }
////                try {
////                    if (mPlayer[2] != null) {
////                        if (!mPlayer[2]!!.isPlaying) {
////                            mPlayer[2]!!.start()
////                        }
////                    }
////                } catch (e: Exception) {
////                }
////                playID = 2
////            }
////        }
////    }
////}