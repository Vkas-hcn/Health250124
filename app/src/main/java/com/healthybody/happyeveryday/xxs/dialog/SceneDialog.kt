package com.healthybody.happyeveryday.xxs.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.google.android.material.button.MaterialButton

import com.healthybody.happyeveryday.xxs.databinding.DialogSceneBinding

class SceneDialog(val mContext: Context,var selectIndex:Int,val call:(Int)->Unit) : Dialog(mContext) {

    private lateinit var binding: DialogSceneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogSceneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list:MutableList<MaterialButton>  = mutableListOf<MaterialButton>()
        initView(list)

        for ((index, btn) in list.withIndex()) {
            if (index == selectIndex){
                list.get(selectIndex).strokeColor = ColorStateList.valueOf(Color.parseColor("#000000"))
            }

            btn.setOnClickListener {
                list.get(selectIndex).strokeColor = ColorStateList.valueOf(Color.parseColor("#F6F9F9"))
                selectIndex = index
                list.get(selectIndex).strokeColor = ColorStateList.valueOf(Color.parseColor("#000000"))
            }
        }

        binding.cancel.setOnClickListener { dismiss() }
        binding.ok.setOnClickListener {
            dismiss();
            call .invoke(selectIndex)
        }

        window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val attr = it.attributes
            attr.width = WindowManager.LayoutParams.MATCH_PARENT
            attr.gravity = Gravity.BOTTOM
            it.attributes = attr
        }

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    fun initView(list:MutableList<MaterialButton> ){
        list.add(binding.opt0); list.add(binding.opt1); list.add(binding.opt2);
        list.add(binding.opt3); list.add(binding.opt4); list.add(binding.opt5);
        list.add(binding.opt6); list.add(binding.opt7);
    }
}