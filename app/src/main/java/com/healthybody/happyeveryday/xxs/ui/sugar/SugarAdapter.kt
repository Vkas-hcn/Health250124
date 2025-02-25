package com.healthybody.happyeveryday.xxs.ui.sugar

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.healthybody.happyeveryday.xxs.bean.SugarInfo
import com.healthybody.happyeveryday.xxs.databinding.ItemSugarBinding
import com.healthybody.happyeveryday.xxs.util.AppUtil
import com.healthybody.happyeveryday.xxs.util.Util

class SugarAdapter(val mContext: Context) : RecyclerView.Adapter<SugarAdapter.VH>() {
    inner class VH(val bin:ItemSugarBinding):RecyclerView.ViewHolder(bin.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemSugarBinding.inflate(LayoutInflater.from(mContext),parent,false))
    }

    val itemList = mutableListOf<SugarInfo>()
    override fun getItemCount(): Int {
        return  itemList.size
    }
    override fun onBindViewHolder(vh: VH, position: Int) {
        val data = itemList.get(position)

        val lv = AppUtil.sugarLevel(data.unit,data.state,data.value)

        vh.bin.title.backgroundTintList = ColorStateList.valueOf(Color.parseColor(AppUtil.sugarColorArr2.get(lv)))
        vh.bin.title.text =  AppUtil.sugarNameArr.get(lv)
        vh.bin.time.text = "${Util.dateFormat(data.time)}"
        vh.bin.vale.text = "${data.value}"
        if (data.unit == 0) {
            vh.bin.unitTv.text = "Mg/dl"
        } else {
            vh.bin.unitTv.text = "Mmol"
        }
        vh.bin.stateIv.setImageResource(AppUtil.sugarStateImgArr.get(data.state))
        vh.bin.stateTv.text=AppUtil.sugarStateTextArr.get(data.state)
        vh.bin.edit.setOnClickListener {
            AddSugarActivity.start(mContext,data)
        }
    }


}