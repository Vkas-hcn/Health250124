package com.healthybody.happyeveryday.xxs.ui.sugar

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.imageview.ShapeableImageView
import com.healthybody.happyeveryday.xxs.bean.SugarInfo
import com.healthybody.happyeveryday.xxs.databinding.ActivityAddSugarBinding
import com.healthybody.happyeveryday.xxs.dialog.SceneDialog
import com.healthybody.happyeveryday.xxs.util.AppUtil
import com.healthybody.happyeveryday.xxs.util.Local
import com.healthybody.happyeveryday.xxs.util.Util

class AddSugarActivity : AppCompatActivity() {

    companion object {
        var tmpData: SugarInfo? = null
        fun start(mContext: Context, info: SugarInfo?) {
            tmpData = info
            mContext.startActivity(Intent(mContext, AddSugarActivity::class.java))
        }
    }

    private lateinit var binding: ActivityAddSugarBinding

    val indiBg = mutableListOf<ShapeableImageView>()
    val indiLab = mutableListOf<ImageView>()

    fun setStateUI() {
        binding.stateIv.setImageResource(AppUtil.sugarStateImgArr.get(stateIndex))
        binding.stateTv.text = AppUtil.sugarStateTextArr.get(stateIndex)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSugarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener { finish() }

        indiBg.add(binding.selBgIv0);indiBg.add(binding.selBgIv1);indiBg.add(binding.selBgIv2);indiBg.add(binding.selBgIv3);
        indiLab.add(binding.selLabIv0);indiLab.add(binding.selLabIv1);indiLab.add(binding.selLabIv2);indiLab.add(binding.selLabIv3);

        binding.save.setOnClickListener {
            addInfo()
        }
        binding.switchUnit.setOnClickListener {
            if (curUnit == 0) {
                curUnit = 1
                binding.switchUnit.text = "Mmol"
            } else {
                curUnit = 0
                binding.switchUnit.text = "Mg/dl"
            }
            updateIndicator()
        }
        binding.stateLa.setOnClickListener {
            SceneDialog(this@AddSugarActivity, stateIndex) { index ->
                stateIndex = index
                setStateUI()
                updateIndicator()
            }.show()
        }
        setStateUI()

        binding.input.addTextChangedListener(onTextChanged = { text: CharSequence?, start: Int, count: Int, after: Int ->
            val text = text.toString().trim()
            if (text.isEmpty()) {
            } else {
                curValue = text.toDouble()
                updateIndicator()
            }
        })


        val mData = tmpData
        if (mData!=null){
            isAdd = false
            binding.save.text = "Update"
            binding.delete.visibility = View.VISIBLE
            binding.delete.setOnClickListener {
                Local.rmSugarInfo(mData)
                Util.toast("Record deleted successfully")
                finish()
            }
            curTime = mData.time
            curUnit = mData.unit
            stateIndex = mData.state
            curValue = mData.value
        }


        val curDatetime = Util.dateFormat(curTime)
        binding.time.text = curDatetime

        setStateUI()
        updateIndicator()
        binding.input.setText("$curValue")
        if (curUnit == 0) {
            binding.switchUnit.text = "Mg/dl"
        } else {
            binding.switchUnit.text = "Mmol"
        }
    }

    var curTime = System.currentTimeMillis()


    var curUnit = 0
    var stateIndex = 0
    var curValue = 85.0


    var lvIndex = 1
    fun updateIndicator() {
        val lv = AppUtil.sugarLevel(curUnit, stateIndex, curValue)

        indiBg.get(lvIndex).strokeColor = ColorStateList.valueOf(Color.WHITE)
        indiLab.get(lvIndex).visibility = View.GONE
        lvIndex = lv
        indiBg.get(lvIndex).strokeColor = ColorStateList.valueOf(Color.parseColor("#222222"))
        indiLab.get(lvIndex).visibility = View.VISIBLE

        binding.title.text = AppUtil.sugarNameArr.get(lv)
        val color = Color.parseColor(AppUtil.sugarColorArr.get(lv))
        binding.title.compoundDrawableTintList = ColorStateList.valueOf(color)
        binding.title.setTextColor(color)

        binding.desc.text = AppUtil.sugarDesc(curUnit, stateIndex, lv)
    }


    var isAdd = true;
    fun addInfo(){
        val info = SugarInfo(curTime,curUnit,stateIndex,curValue)
        if (isAdd){
            Local.addSugarInfo(info)
            Util.toast("Record added successfully")
        }else{
            Local.updateSugarInfo(info)
            Util.toast("Record updated successfully")
        }
        finish()
    }

}