//package com.edvard.poseestimation.pushUp
//
//import android.media.MediaPlayer
//import android.os.Bundle
//import android.widget.Button
//import android.widget.TextView
//import com.edvard.poseestimation.CameraActivity
//import com.edvard.poseestimation.R
//
///**
// */
//class PushUP_ResultActivity : BasePushUP_Activity() {
////    @BindView(R.id.return_bt)
////    var return_bt: Button? = null
//
//    @BindView(R.id.result_num)
//    var resultNum: TextView? = null
//
////    @BindView(R.id.result_time)
////    var resultTime: TextView? = null
//    private var mPlayerCom: MediaPlayer? = MediaPlayer()
//    val layoutId: Int
//        get() = R.layout.fragment_camera2_basic
//
//    fun initViews(savedInstanceState: Bundle?) {}
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        displayResult()
//        playBellCom()
//    }
//
//    fun displayResult() {
//        val num: String = java.lang.String.valueOf(CameraApplication.getFwcCounter().mCounter.count)
//        resultNum!!.text = "Complete " + num + "times"
//        val min: String = (CameraApplication.getFwcCounter().mCounter.timeUse / 60000) as Int.toString()
//        val sec: String = java.lang.String.valueOf(CameraApplication.getFwcCounter().mCounter.timeUse / 1000 % 60)
//        resultTime!!.text = "use time " + min + "min" + sec + "sec"
//    }
//
////    fun playBellCom() {
////        if (mPlayerCom == null) {
////            mPlayerCom = MediaPlayer.create(this, R.raw.completebell)
////        }
////        try {
////            if (mPlayerCom != null) {
////                if (!mPlayerCom!!.isPlaying) {
////                    mPlayerCom!!.start()
////                }
////            }
////        } catch (e: Exception) {
////        }
////    }
//
//    fun initToolBar() {}
//    fun loadData() {}
//    protected override fun onDestroy() {
//        super.onDestroy()
//    }
//}