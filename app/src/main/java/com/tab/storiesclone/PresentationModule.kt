package com.tab.storiesclone

import com.tab.storiesclone.ui.main.TimerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel { TimerViewModel() }

}