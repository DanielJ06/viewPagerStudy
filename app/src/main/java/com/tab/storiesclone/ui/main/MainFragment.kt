package com.tab.storiesclone.ui.main

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.tab.storiesclone.databinding.MainFragmentBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding: MainFragmentBinding get() = _binding!!
    private val viewModel: TimerViewModel by sharedViewModel()

    private lateinit var vpAdapter: FragmentStateAdapter
    private lateinit var storiesList: List<UserStories>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = MainFragmentBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        addObservers(viewLifecycleOwner)
    }

    private fun setupView() {
        storiesList = listOf(
            UserStories(
                username = "djr.ok",
                listOf("djr stories 1", "djr stories 2", "djr stories 3")
            ),
            UserStories(
                username = "idk.ok",
                listOf("idk stories 1", "idk stories 2", "idk stories 3")
            ),
            UserStories(
                username = "west.ok",
                listOf("west stories 1", "west stories 2", "west stories 3")
            ),
        )

        vpAdapter = StoriesCollectionAdapter(fragment = this, storiesList = storiesList)
        binding.vpPager.adapter = vpAdapter

        binding.vpPager.setPageTransformer(CubeTransformer())
        binding.vpPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (binding.vpPager.isFakeDragging.not()) {
//                    if (state == SCROLL_STATE_DRAGGING) {
//                        viewModel.stopTimer()
//                    } else if (state == 0) {
//                        viewModel.startTimer(where = "On Page scrolled")
//                    }
                }
            }
        })

        viewModel.startTimer()
    }

    private fun addObservers(owner: LifecycleOwner) {
        viewModel.lastRegisteredTime.observe(owner) {
            binding.tvTimer.text = "${it / 1000} seconds"
        }
    }

    fun navigateToNextPage() {
        val currentIndex = binding.vpPager.currentItem
        binding.vpPager.setCurrentItem(currentIndex + 1, duration = 1000)
        viewModel.startTimer()
    }

    private fun ViewPager2.setCurrentItem(
        item: Int,
        duration: Long,
        interpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),
        pagePxWidth: Int = width // Default value taken from getWidth() from ViewPager2 view
    ) {
        val pxToDrag: Int = pagePxWidth * (item - currentItem)
        val animator = ValueAnimator.ofInt(0, pxToDrag)
        var previousValue = 0
        animator.addUpdateListener { valueAnimator ->
            val currentValue = valueAnimator.animatedValue as Int
            val currentPxToDrag = (currentValue - previousValue).toFloat()
            fakeDragBy(-currentPxToDrag)
            previousValue = currentValue
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                beginFakeDrag()
            }

            override fun onAnimationEnd(animation: Animator?) {
                endFakeDrag()
            }

            override fun onAnimationCancel(animation: Animator?) { /* Ignored */
            }

            override fun onAnimationRepeat(animation: Animator?) { /* Ignored */
            }
        })
        animator.interpolator = interpolator
        animator.duration = duration
        animator.start()
    }

}
