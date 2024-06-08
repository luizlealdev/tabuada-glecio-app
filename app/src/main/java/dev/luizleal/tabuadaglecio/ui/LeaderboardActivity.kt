package dev.luizleal.tabuadaglecio.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import dev.luizleal.tabuadaglecio.adapter.LeaderboardViewPagerAdapter
import dev.luizleal.tabuadaglecio.databinding.ActivityLeaderboardBinding

class LeaderboardActivity : AppCompatActivity() {

    private var leaderboardBinding: ActivityLeaderboardBinding? = null
    private val binding get() = leaderboardBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        leaderboardBinding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTabLayout()
    }

    override fun onDestroy() {
        super.onDestroy()
        leaderboardBinding = null
    }

    private fun setupTabLayout() {
        val tabLayout = binding.tabLeaderboardType
        val viewPager = binding.viewpagerLeaderboard

        val viewPagerAdapter = LeaderboardViewPagerAdapter(this@LeaderboardActivity)
        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) {tab, position ->
            tab.text = when(position) {
                0 -> "Placar Normal"
                1 -> "Placar Global"
                else -> null
            }
        }.attach()
    }
}