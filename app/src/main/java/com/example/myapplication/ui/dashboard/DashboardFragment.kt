package com.example.myapplication.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.common.Constant
import com.example.myapplication.databinding.FragmentDashboardBinding
import com.example.myapplication.utils.MySharedPreferences

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var shrdPre: MySharedPreferences
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        shrdPre = MySharedPreferences(requireContext())
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        binding.memberName.text = shrdPre.getData(Constant.ANDROID_ID)
        binding.acvtTime.text = shrdPre.getData(Constant.ACVT_TIME)
        binding.remainingTime.text = shrdPre.getData(Constant.RES_MATURITY)
        binding.expireTime.text = shrdPre.getData(Constant.EXPIRE_TIME)
        binding.token.text = shrdPre.getData(Constant.TOKEN)
        binding.editButton.setOnClickListener {
            requireActivity().setContentView(R.layout.fragment_blank)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}