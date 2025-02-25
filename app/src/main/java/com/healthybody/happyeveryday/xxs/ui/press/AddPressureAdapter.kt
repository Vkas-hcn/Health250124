package com.healthybody.happyeveryday.xxs.ui.press

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.healthybody.happyeveryday.xxs.databinding.ItemAddPressureBinding

class AddPressureAdapter(val mContext: Context) : RecyclerView.Adapter<AddPressureAdapter.VH>() {
    inner class VH(val bin:ItemAddPressureBinding):RecyclerView.ViewHolder(bin.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemAddPressureBinding.inflate(LayoutInflater.from(mContext),parent,false))
    }
    var selectIndex = 1 // 高度为3个item，def = 1
    val itemList = mutableListOf<String>()
    override fun getItemCount(): Int {
        return  itemList.size
    }
    override fun onBindViewHolder(vh: VH, position: Int) {
        val data = itemList.get(position)
        vh.bin.name.text = data

        if (selectIndex == position) {
            vh.bin.name.setTextColor(Color.parseColor("#FF00CECE"))
            vh.bin.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28.0f)
        } else {
            vh.bin.name.setTextColor(Color.parseColor("#FFD2E7E7"))
            vh.bin.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24.0f)
        }
    }


}