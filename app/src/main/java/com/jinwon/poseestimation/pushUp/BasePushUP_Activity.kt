package com.jinwon.poseestimation.pushUp

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
//import camera.edvard.poseestimation.ui.activity.BaseActivity
// 위에꺼 대신에 밑에꺼
import com.jinwon.poseestimation.CameraActivity


abstract class BasePushUP_Activity : CameraActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
    }
}