package com.healthybody.happyeveryday.xxs.ui.press

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.healthybody.happyeveryday.xxs.bean.PressInfo
import com.healthybody.happyeveryday.xxs.databinding.FragmentPressureBinding
import com.healthybody.happyeveryday.xxs.util.Local

class PressureFragment : Fragment() {

    private var _binding: FragmentPressureBinding? = null
    private val binding get() = _binding!!

    lateinit var mContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPressureBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.add.setOnClickListener {
            AddPressureActivity.start(mContext,null)
        }

          adapter = PressureAdapter(mContext)
        binding.rv.layoutManager = LinearLayoutManager(mContext)
        binding.rv.setHasFixedSize(true)
        binding.rv.adapter = adapter

        return root
    }
    lateinit var adapter:PressureAdapter

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    fun loadData(){
        val list:MutableList<PressInfo> = Local.getAllPressInfo()
        adapter.itemList.clear()
        adapter.itemList.addAll(list)
        adapter.notifyDataSetChanged()

        if (list.isNullOrEmpty()){
            binding.emptyView.visibility = View.VISIBLE
        }else{
            binding.emptyView.visibility = View.GONE
        }
    }


}