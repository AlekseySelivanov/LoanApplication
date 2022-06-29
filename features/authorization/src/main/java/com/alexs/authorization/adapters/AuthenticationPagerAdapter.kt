package com.alexs.authorization.adapters

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alexs.authorization.ui.LoginFragment
import com.alexs.authorization.ui.RegistrationFragment

class AuthenticationPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments = listOf(RegistrationFragment(), LoginFragment())

    override fun getItemCount() = fragments.count()

    override fun createFragment(position: Int) = fragments[position]
}