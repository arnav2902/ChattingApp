package com.example.chatapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.chatapp.fragments.InboxFragment
import com.example.chatapp.fragments.PeopleFragment


class ScreenSlidePagerAdapter(fa:FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int =2

    override fun createFragment(position: Int): Fragment = when(position){
          0 -> InboxFragment()
          else -> PeopleFragment()
    }


}
