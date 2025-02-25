package com.healthybody.happyeveryday.xxs.ui.press

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.healthybody.happyeveryday.xxs.bean.PressInfo

import com.healthybody.happyeveryday.xxs.databinding.ActivityAddPressureBinding
import com.healthybody.happyeveryday.xxs.util.AppUtil
import com.healthybody.happyeveryday.xxs.util.Local
import com.healthybody.happyeveryday.xxs.util.Util

class AddPressureActivity : AppCompatActivity() {

    companion object {
        var tmpData: PressInfo? = null
        fun start(mContext: Context, info: PressInfo?){
            tmpData = info
            mContext.startActivity(Intent(mContext,AddPressureActivity::class.java))
        }
    }


    private lateinit var binding: ActivityAddPressureBinding


    val indiBg = mutableListOf<ShapeableImageView>()
    val indiLab = mutableListOf<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPressureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener { finish() }

        indiBg.add(binding.selBgIv0);indiBg.add(binding.selBgIv1);indiBg.add(binding.selBgIv2);
        indiBg.add(binding.selBgIv3);indiBg.add(binding.selBgIv4);indiBg.add(binding.selBgIv5);
        indiLab.add(binding.selLabIv0);indiLab.add(binding.selLabIv1);indiLab.add(binding.selLabIv2);
        indiLab.add(binding.selLabIv3);indiLab.add(binding.selLabIv4);indiLab.add(binding.selLabIv5);

        loadData()

        binding.save.setOnClickListener {
            addInfo()
        }

        val mData = tmpData
        if (mData!=null){
            isAdd = false
            binding.save.text = "Update"
            binding.delete.visibility = View.VISIBLE
            binding.delete.setOnClickListener {
                Local.rmPressInfo(mData)
                Util.toast("Record deleted successfully")
                finish()
            }
            curTime = mData.time
            sysData = mData.sys
            diaData = mData.dia
            bpmData = mData.bpm
        }


        initRv(binding.rv0,sysData,sysListData){selectIndex ->
            Util.i { "selectIndex=$selectIndex" }
            sysData = sysListData.get(selectIndex).toInt()
            updateIndicator()
        }
        initRv(binding.rv1,diaData,diaListData){selectIndex ->
            Util.i { "selectIndex=$selectIndex" }
            diaData = diaListData.get(selectIndex).toInt()
            updateIndicator()
        }
        initRv(binding.rv2,bpmData,bpmListData){selectIndex ->
            Util.i { "selectIndex=$selectIndex" }
            bpmData = bpmListData.get(selectIndex).toInt()
            updateIndicator()
        }



        val curDatetime = Util.dateFormat(curTime)
        binding.time.text = curDatetime

        updateIndicator()
    }

    var curTime = System.currentTimeMillis()

    fun initRv(rv:RecyclerView,select :Int,list: MutableList<String>,call:(Int)->Unit){
        val adapter:AddPressureAdapter = AddPressureAdapter(this)
        adapter.itemList.addAll(list)

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rv)

        rv.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val itemView = snapHelper.findSnapView(recyclerView.layoutManager)
                    val position = if (itemView == null) -1 else recyclerView.getChildAdapterPosition(itemView)
                    call.invoke(position)

                    adapter.notifyItemChanged(adapter.selectIndex)
                    adapter.selectIndex = position
                    adapter.notifyItemChanged(adapter.selectIndex)
                }
            }
        })
        adapter.selectIndex = select-20+1

        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
        rv.adapter = adapter

        rv.scrollToPosition(select-20)
    }

    var sysData = 100
    var diaData = 70
    var bpmData = 80

    val sysListData = mutableListOf<String>()
    val diaListData = mutableListOf<String>()
    val bpmListData = mutableListOf<String>()
    fun loadData(){
        sysListData.add("")
        diaListData.add("")
        for (ii in 20..300) {
            sysListData.add("$ii")
            diaListData.add("$ii")
        }
        sysListData.add("")
        diaListData.add("")
        //
        bpmListData.add("")
        for (ii in 20..300) {
            bpmListData.add("$ii")
        }
        bpmListData.add("")
    }


    var lvIndex = 1

    fun updateIndicator(){
       val lv =  AppUtil.pressureLevel(sysData,diaData)

        indiBg.get(lvIndex).strokeColor = ColorStateList.valueOf(Color.WHITE)
        indiLab.get(lvIndex).visibility = View.GONE
        lvIndex = lv
        indiBg.get(lvIndex).strokeColor = ColorStateList.valueOf(Color.parseColor("#222222"))
        indiLab.get(lvIndex).visibility = View.VISIBLE

        binding.title.text = AppUtil.pressureNameArr.get(lv)
        val color = Color.parseColor(AppUtil.pressureColorArr.get(lv))
        binding.title.compoundDrawableTintList = ColorStateList.valueOf(color)
        binding.title.setTextColor(color)

        binding.desc.text = AppUtil.pressureDescArr.get(lv)
    }

    var isAdd = true;
    fun addInfo(){
        val info = PressInfo(curTime,sysData,diaData,bpmData)
        if (isAdd){
            Local.addPressInfo(info)
            Util.toast("Record added successfully")
        }else{
            Local.updatePressInfo(info)
            Util.toast("Record updated successfully")
        }
        finish()
    }

}