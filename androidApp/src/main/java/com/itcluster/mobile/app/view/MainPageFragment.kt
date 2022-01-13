package com.itcluster.mobile.app.view

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.itcluster.mobile.app.R
import dev.icerock.moko.mvvm.MvvmFragment
import dev.icerock.moko.mvvm.createViewModelFactory
import com.itcluster.mobile.app.BR
import com.itcluster.mobile.app.databinding.FragmentMainPageBinding
import com.itcluster.mobile.feature.list.presentation.MainPageViewModel
import dev.icerock.moko.mvvm.livedata.addCloseableObserver

class MainPageFragment : MvvmFragment<FragmentMainPageBinding, MainPageViewModel>() {

    override val layoutId: Int = R.layout.fragment_main_page
    override val viewModelVariableId: Int = BR.viewModel
    override val viewModelClass = MainPageViewModel::class.java

    override fun viewModelFactory(): ViewModelProvider.Factory {
        return createViewModelFactory { MainPageViewModel().apply { onCreated() } }
    }

    private val progressBarView: ProgressBar by lazy { requireView().findViewById(R.id.progressBar) }
    private val authText: TextView by lazy { requireView().findViewById(R.id.auth) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.auth.addCloseableObserver { authString ->
            authText.text = authString
            progressBarView.isVisible = false
        }
    }
}