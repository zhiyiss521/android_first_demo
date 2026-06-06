package com.zhiyi.android_first_demo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.zhiyi.android_first_demo.databinding.FragmentHomeBinding
import com.zhiyi.android_first_demo.ui.baseList.BaseListFragment
import com.zhiyi.android_first_demo.ui.baseList.ListDataType

// 当我更换接口的时候，我发现activity完全不用改，只是为了给view绑定东西
// 调用vm的方法，监听vm，赋值给view
class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate( layoutInflater )
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabTypes = ListDataType.entries.toTypedArray()

        binding!!.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = tabTypes.size

            override fun createFragment(position: Int): Fragment {
                val currentType = tabTypes[position]
                return BaseListFragment.newInstance(currentType)
            }
        }

        TabLayoutMediator(binding!!.tabLayout, binding!!.viewPager) { tab, position ->
            tab.text = tabTypes[position].name
        }.attach()

    }

}