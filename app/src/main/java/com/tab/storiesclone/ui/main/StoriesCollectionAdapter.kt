package com.tab.storiesclone.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.gson.Gson

class StoriesCollectionAdapter(
    private val storiesList: List<UserStories>,
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = storiesList.size

    override fun createFragment(position: Int): Fragment {
        val fragment = StoriesFragment()

        val fragmentContent = Gson().toJson(storiesList[position])
        fragment.arguments = Bundle().apply {
            putString("storiesContent", fragmentContent)
        }
        return fragment
    }

}