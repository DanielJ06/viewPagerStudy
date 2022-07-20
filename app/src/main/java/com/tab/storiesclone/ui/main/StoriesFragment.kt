package com.tab.storiesclone.ui.main

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.google.gson.Gson
import com.tab.storiesclone.databinding.StoriesFragmentBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

class StoriesFragment : Fragment() {

    private var _binding: StoriesFragmentBinding? = null
    private val binding: StoriesFragmentBinding get() = _binding!!
    private val viewModel: TimerViewModel by sharedViewModel()

    private lateinit var content: UserStories
    private var currentStories = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = StoriesFragmentBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObservers(viewLifecycleOwner)

        arguments?.takeIf { it.containsKey("storiesContent") }?.apply {
            content = Gson().fromJson(getString("storiesContent"), UserStories::class.java)
        }

        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        binding.ivPhoto.setBackgroundColor(color)

        binding.tvUserName.text = content.username
        binding.tvStoriesText.text = content.photos[currentStories]

        binding.segmentProgress.timePerSegmentMs = 10000

        binding.btnClose.setOnClickListener {
            viewModel.stopTimer()
            pauseProgress()
        }

        binding.btnSend.setOnClickListener {
            viewModel.resumeTimer(15000)
        }
    }

    private fun addObservers(owner: LifecycleOwner) {
        viewModel.timerState.observe(owner) {
            when (it) {
                TimesStates.FINISHED -> {
                    if (currentStories + 1 < (content.photos.size)) {
                        setNextStories()
                    } else {
                        Log.i("TESTE", "onFinish: acabou os stories")
                        val parent = parentFragment as MainFragment
                        parent.navigateToNextPage()
                        currentStories = 0
                    }
                }
                TimesStates.RUNNING -> {
                    startProgress()
                }
                TimesStates.STOPPED -> {
                    pauseProgress()
                }
                else -> {}
            }
        }
    }

    private fun pauseProgress() {
        binding.segmentProgress.pause()
    }

    private fun startProgress() {
        binding.segmentProgress.start()
    }

    private fun setNextStories() {
        Log.i(
            "TESTE",
            "stories em tela: ${content.photos[currentStories]} - próximo stories: ${content.photos[currentStories + 1]}"
        )
        currentStories += 1
        binding.tvStoriesText.text = content.photos[currentStories]
        binding.segmentProgress.next()
        viewModel.startTimer()
    }

}