package com.itcluster.mobile.app.view

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.itcluster.mobile.app.R
import com.itcluster.mobile.app.databinding.ActivityNewsBinding
import com.itcluster.mobile.domain.entity.News
import com.itcluster.mobile.feature.list.di.ListFactory
import com.itcluster.mobile.feature.list.presentation.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import dev.icerock.moko.mvvm.MvvmActivity
import dev.icerock.moko.mvvm.createViewModelFactory
import com.itcluster.mobile.app.BR
import javax.inject.Inject

// MvvmActivity for simplify creation of MVVM screen with https://github.com/icerockdev/moko-mvvm
@AndroidEntryPoint
class NewsActivity : MvvmActivity<ActivityNewsBinding, ListViewModel<News>>() {
    override val layoutId: Int = R.layout.activity_news

    @Suppress("UNCHECKED_CAST")
    override val viewModelClass = ListViewModel::class.java as Class<ListViewModel<News>>
    override val viewModelVariableId: Int = BR.viewModel

    @Inject
    lateinit var factory: ListFactory<News>

    // createViewModelFactory is extension from https://github.com/icerockdev/moko-mvvm
    // ViewModel not recreating at configuration changes
    override fun viewModelFactory(): ViewModelProvider.Factory = createViewModelFactory {
        factory.createListViewModel().apply { onCreated() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding.refreshLayout) {
            setOnRefreshListener {
                viewModel.onRefresh { isRefreshing = false }
            }
        }
    }
}
