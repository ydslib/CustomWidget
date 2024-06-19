package com.ydslib.customerwiget

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ydslib.customerwiget.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityMainBinding

    var showDot = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.dotView.setDotColor(resources.getColor(R.color.teal_200))

        mBinding.textStateDotView.setOnClickListener {
            mBinding.textStateDotViewV2.showDot(showDot)
            println("showDot:$showDot")
            showDot = !showDot
        }

        mBinding.textStateDotViewV2.setDotViewRadius(20)
            .setDotTextSize(30)
            .setStateStrokeWidth(7)
            .setDotText("Test test Test test Test test Test test Test test Test test")
    }
}
