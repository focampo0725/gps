package com.kloubit.gps.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kloubit.gps.ui.main.route_map.RouteMapFragment
import com.kloubit.gps.ui.main.session.SessionFragment


class MyFragmentPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragmentList = mutableMapOf<Int, Fragment>()
    companion object {
        private const val NUM_ITEMS = 2

    }
    override fun getItemCount(): Int {
        return NUM_ITEMS
    }
    override fun createFragment(position: Int): Fragment {
        // Verificamos si ya existe un fragment para la posición dada
        val existingFragment = fragmentList[position]
        return if (existingFragment != null) {
            existingFragment
        } else {
            // Si no existe, creamos un nuevo fragment y lo almacenamos en la lista
            val newFragment = when (position) {
                0 -> SessionFragment.newInstanceSessionFragment()
                1 -> RouteMapFragment.newInstanceRouteMapFragment()
//                2 -> LocalizatorFragment.newInstanceLocalizatorFragment()
//                3 -> DispatchFragment.newInstanceDispatchFragment()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
            fragmentList[position] = newFragment
            newFragment
        }
    }

    /**
     * Destruye instancias
     */
    fun destroyInstances() {
        fragmentList.forEach { (_, fragment) ->
            fragment.onDestroyView()
        }
        fragmentList.clear()
    }
    enum class EFRAGMENTS(val value: Int) {
        SessionFragment(0),
        RouteMapFragment(1)

    }
}