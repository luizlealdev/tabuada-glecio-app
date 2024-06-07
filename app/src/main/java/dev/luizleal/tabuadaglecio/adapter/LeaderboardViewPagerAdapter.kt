package dev.luizleal.tabuadaglecio.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.luizleal.tabuadaglecio.fragments.ScoreboardFragment
import dev.luizleal.tabuadaglecio.fragments.ScoreboardGlobalFragment

class LeaderboardViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    private val fragmentList = listOf(
        ScoreboardFragment(),
        ScoreboardGlobalFragment()
    )
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}