package com.healthybody.happyeveryday.xxs.ui.setup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.healthybody.happyeveryday.xxs.BuildConfig
import com.healthybody.happyeveryday.xxs.databinding.FragmentSetupBinding
import com.healthybody.happyeveryday.xxs.util.Util

class SetupFragment : Fragment() {

    private var _binding: FragmentSetupBinding? = null
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
        _binding = FragmentSetupBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val verName = BuildConfig.VERSION_NAME
        binding.verTv.setText("V"+verName)

        binding.privacy.setOnClickListener {
            val https = "https://sites.google.com/view/privacypolicy-sugarrecord/home"
            ActivityCompat.startActivity(mContext, Util.getWebIntent(https),null)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}