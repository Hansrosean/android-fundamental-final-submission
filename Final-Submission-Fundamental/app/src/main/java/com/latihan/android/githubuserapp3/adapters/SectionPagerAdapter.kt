package com.latihan.android.githubuserapp3.adapters

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.latihan.android.githubuserapp3.fragments.FollowerFragment
import com.latihan.android.githubuserapp3.fragments.FollowingFragment

class SectionPagerAdapter(activity: AppCompatActivity, data: Bundle) :
    FragmentStateAdapter(activity){
    
    private var fragmentBundle : Bundle = data

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = FollowerFragment()
            1 -> fragment = FollowingFragment()
        }
        
        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
    }
    
    override fun getItemCount(): Int {
        return 2
    }
}