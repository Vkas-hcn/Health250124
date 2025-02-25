package com.healthybody.happyeveryday.xxs.ui.press

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.healthybody.happyeveryday.xxs.bean.PressInfo
import com.healthybody.happyeveryday.xxs.databinding.ItemPressureBinding
import com.healthybody.happyeveryday.xxs.util.AppUtil
import com.healthybody.happyeveryday.xxs.util.Util

class PressureAdapter(val mContext: Context) : RecyclerView.Adapter<PressureAdapter.VH>() {
    inner class VH(val bin:ItemPressureBinding):RecyclerView.ViewHolder(bin.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemPressureBinding.inflate(LayoutInflater.from(mContext),parent,false))
    }

    val itemList = mutableListOf<PressInfo>()
    override fun getItemCount(): Int {
        return  itemList.size
    }
    override fun onBindViewHolder(vh: VH, position: Int) {
        val data = itemList.get(position)

        val lv = AppUtil.pressureLevel(data.sys,data.dia)

        vh.bin.title.backgroundTintList = ColorStateList.valueOf(Color.parseColor(AppUtil.pressureColorArr2.get(lv)))
        vh.bin.title.text = AppUtil.pressureNameArr.get(lv)
        vh.bin.time.text = "${Util.dateFormat(data.time)}"
        vh.bin.sys.text = "${data.sys}"
        vh.bin.dia.text = "${data.dia}"
        vh.bin.bpm.text = "${data.bpm}"
        vh.bin.edit.setOnClickListener {
            AddPressureActivity.start(mContext,data)
        }
    }


}